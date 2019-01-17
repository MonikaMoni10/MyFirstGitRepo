/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.fixture.configuration;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sonata.generic.automation.fixture.util.XMLDocumentBuilder;
import com.sonata.generic.automation.fixture.widget.FixtureWidget;
import com.sonata.generic.automation.fixture.widget.FixtureWidgetFactory;
import com.sonata.generic.automation.fixture.widget.ModifiableFixtureWidget;

/**
 * Default implementation of a {@link ConfigurationParser}.
 */
public final class DefaultConfigurationParser implements ConfigurationParser
{
   private static final XMLDocumentBuilder DOCUMENT_BUILDER = new XMLDocumentBuilder();
   private final FixtureWidgetFactory      factory;

   /**
    * Constructs a default fixture configuration data parser that produces
    * {@link FixtureProperties} objects that the fixture can use easily. The
    * parser uses the specified fixture widget factory to create
    * {@link FixtureWidget} instances (FitNesse fixture friendly representation
    * of widgets) that are contained in the fixture properties object.
    * 
    * @param factory
    *           The factory used to create the {@link FixtureWidget} instances
    *           (FitNesse fixture friendly representation of widgets) that are
    *           contained in fixture properties objects created by the parser.
    * 
    * @throws IllegalArgumentException
    *            The fixture widget factory is null.
    */
   public DefaultConfigurationParser(final FixtureWidgetFactory factory)
   {
      if (null == factory)
         throw new IllegalArgumentException("The fixture widget factory must be non-null.");

      this.factory = factory;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public FixtureProperties parse(final String configurationPath)
   {
      if ((null == configurationPath) || configurationPath.isEmpty())
         throw new IllegalArgumentException("The configuration path must be non-empty.");

      File configurationFile = getConfigurationFile(configurationPath);
      return parse(configurationFile);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public FixtureProperties parse(final File configurationFile)
   {
      // NOTE: The isFile check does an implicit exists check too.
      if ((null == configurationFile) || !configurationFile.isFile())
         throw new IllegalArgumentException("The configuration file must exist and be a file (not a directory).");

      Document configurationDoc = getConfigurationDOM(configurationFile);
      return parse(configurationDoc);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public FixtureProperties parse(final Document configurationDoc)
   {
      if (null == configurationDoc)
         throw new IllegalArgumentException("The configuration DOM document must be non-null.");

      // The root element should be "ui" and it should have name, application,
      // and signin validation widget name attributes.
      Element uiElem = configurationDoc.getDocumentElement();
      if (null == uiElem)
         throw new IllegalArgumentException("The configuration DOM document must contain a root element.");

      String uiElemName = uiElem.getTagName();
      if ((null == uiElemName) || !uiElemName.equals(ConfigurationConstants.Tags.UI))
         throw new IllegalArgumentException("The configuration DOM document must contain a root '"
               + ConfigurationConstants.Tags.UI + "' element.");

      String uiName = uiElem.getAttribute(ConfigurationConstants.Attributes.NAME);
      if (null == uiName)
         throw new IllegalArgumentException("The '" + ConfigurationConstants.Tags.UI + "' element must contain a '"
               + ConfigurationConstants.Attributes.NAME + "' attribute with a non-empty value.");
      
      String uiMenuName = uiElem.getAttribute(ConfigurationConstants.Attributes.MENUNAME);
      
      String category = uiElem.getAttribute(ConfigurationConstants.Attributes.CATEGORY);
      
      String applicationfullname = uiElem.getAttribute(ConfigurationConstants.Attributes.APPLICATIONFULLNAME);

      String application = uiElem.getAttribute(ConfigurationConstants.Attributes.APPLICATION);
      if (null == application)
         throw new IllegalArgumentException("The '" + ConfigurationConstants.Tags.UI + "' element must contain a '"
               + ConfigurationConstants.Attributes.APPLICATION + "' attribute with a non-empty value.");

      Map<String, Map<String, FixtureWidget>> allFixtureWidgets = new HashMap<String, Map<String, FixtureWidget>>();
      Map<String, FixtureWidget> existenceValidationWidgets = new HashMap<String, FixtureWidget>();
      boolean foundMainForm = false; // until told otherwise

      // All form elements are direct children of the root UI element, and as
      // such we can use getElementsByTagName to locate them without looping
      // through extra child nodes such as attributes of the root node and
      // whitespace nodes.
      NodeList formNodes = uiElem.getElementsByTagName(ConfigurationConstants.Tags.FORM);
      int formCount = formNodes.getLength();
      for (int i = 0; i < formCount; i++)
      {
         Node formNode = formNodes.item(i);
         if ((null == formNode) || !(formNode instanceof Element))
            continue; // ignore non-elements (although there shouldn't be any with getElementsByTagName)

         Element formElem = (Element)formNode;

         // Get the form's definition ID, which will be used to form the ID
         // base (prefix) for locators of widgets on that form.
         String definitionID = formElem.getAttribute(ConfigurationConstants.Attributes.DEFINITION_ID);
         if ((null == definitionID) || definitionID.isEmpty())
            throw new IllegalArgumentException("All '" + ConfigurationConstants.Tags.FORM + "' elements must have '"
                  + ConfigurationConstants.Attributes.DEFINITION_ID + "' attributes with non-empty values.");

         // Get the form's existence validation widget name, which will be used
         // add that widget (once it's created) to the "form name to existence
         // validation widget" map.
         String existenceValidationWidgetName = formElem
               .getAttribute(ConfigurationConstants.Attributes.EXISTENCE_VALIDATION_WIDGET);
         if ((null == existenceValidationWidgetName) || existenceValidationWidgetName.isEmpty())
            throw new IllegalArgumentException("The '" + ConfigurationConstants.Tags.FORM
                  + "' element must contain an '" + ConfigurationConstants.Attributes.EXISTENCE_VALIDATION_WIDGET
                  + "' attribute with a non-empty value.");

         // Get the form's type.  Based on that type, get the form name to use.
         String type = formElem.getAttribute(ConfigurationConstants.Attributes.TYPE);
         String nameToUse;
         if (ConfigurationConstants.FormType.MAIN.equals(type))
         {
            if (foundMainForm)
               throw new IllegalArgumentException("There can only be one '" + ConfigurationConstants.Tags.FORM
                     + "' element whose '" + ConfigurationConstants.Attributes.TYPE + "' attribute has a value of '"
                     + ConfigurationConstants.FormType.MAIN + "'.");

            // Note down that we've now found what's supposed to be the one and
            // only main form.  Since it's the main form, we use "" as the
            // form name (ignoring its "name" attribute altogether).
            foundMainForm = true;
            nameToUse = "";
         }
         else if (ConfigurationConstants.FormType.POPUP.equals(type))
         {
            // For popup forms, we use the "name" attribute.
            nameToUse = formElem.getAttribute(ConfigurationConstants.Attributes.NAME);
            if ((null == nameToUse) || nameToUse.isEmpty())
               throw new IllegalArgumentException("Each '" + ConfigurationConstants.Tags.FORM + "' element whose '"
                     + ConfigurationConstants.Attributes.TYPE + "' is '" + ConfigurationConstants.FormType.POPUP
                     + "' must have a '" + ConfigurationConstants.Attributes.NAME
                     + "' attribute with a non-empty value.");
         }
         else
         {
            throw new IllegalArgumentException("Each '" + ConfigurationConstants.Tags.FORM + "' elements must have a '"
                  + ConfigurationConstants.Attributes.TYPE + "' attribute whose value is either '"
                  + ConfigurationConstants.FormType.MAIN + "' or '" + ConfigurationConstants.FormType.POPUP + "'.");
         }

         // Create and populate a fixture widget map keyed by widget name.
         // This map will be populated from the descendant widget elements of
         // the form element.  Once created, add this map to the "map of maps".
         Map<String, FixtureWidget> formFixtureWidgets = new HashMap<String, FixtureWidget>();
         String idBase = createIDBase(definitionID);

         populateWidgets(formFixtureWidgets, idBase, null, formElem.getChildNodes());
         if (formFixtureWidgets.isEmpty())
            throw new IllegalArgumentException("The '" + ConfigurationConstants.Tags.FORM + "' element with a '"
                  + ConfigurationConstants.Attributes.DEFINITION_ID + "' of '" + definitionID
                  + "' must contain at least one child '" + ConfigurationConstants.Tags.WIDGET + "' element.");

         allFixtureWidgets.put(nameToUse, Collections.unmodifiableMap(formFixtureWidgets));

         // Get the form's validation widget and add it to the "form name to
         // existence validation widget" map. 
         FixtureWidget existenceValidationWidget = formFixtureWidgets.get(existenceValidationWidgetName);
         if (null == existenceValidationWidget)
            throw new IllegalArgumentException("An existence validation widget with a '"
                  + ConfigurationConstants.Attributes.NAME + "' of '" + existenceValidationWidgetName
                  + "' must exist among the '" + ConfigurationConstants.Tags.WIDGET + "' elements of the form whose '"
                  + ConfigurationConstants.Attributes.DEFINITION_ID + "' is '" + definitionID + "'.");

         existenceValidationWidgets.put(nameToUse, existenceValidationWidget);
      }

      if (!foundMainForm)
         throw new IllegalArgumentException("There must be one '" + ConfigurationConstants.Tags.FORM
               + "' element whose '" + ConfigurationConstants.Attributes.TYPE + "' attribute has a value of '"
               + ConfigurationConstants.FormType.MAIN + "'");

      // Gather the previously processed information to create and return the
      // fixture properties object.
      FixtureProperties properties = new DefaultFixtureProperties(factory.getBrowser(), uiName, uiMenuName, category, application, applicationfullname,
            Collections.unmodifiableMap(existenceValidationWidgets), Collections.unmodifiableMap(allFixtureWidgets));
      return properties;
   }

   /**
    * Recursively populates the form-wide {@link FixtureWidget} map (keyed by
    * descriptive widget name), setting parent-child widget relationships as
    * needed.
    * 
    * @param formFixtureWidgets
    *           The form-wide {@link FixtureWidget} map (keyed by descriptive
    *           widget name) that is being populated. (Think of this as an "out"
    *           argument whose content is being modified by this method. The
    *           argument is still final because we are not modifying the
    *           reference to the map object.)
    * @param idBase
    *           The form-wide ID base (prefix) used in automation locators for
    *           widgets belonging to the same form.
    * @param parentWidget
    *           The parent {@link ModifiableFixtureWidget} whose associated
    *           child nodes are being processed, or null when processing the
    *           top-level widgets of the form.
    * @param childNodes
    *           The child nodes of the parent widget element or of the form
    *           element if we are populating the top-level widgets (in which
    *           case {@code parentWidget} is null). If there are widget elements
    *           among those child nodes, those elements are used to populate the
    *           map. (Otherwise, the child nodes are just the attributes of the
    *           parent widget element or form, and the recursion stops going
    *           deeper.)
    * 
    * @throws IllegalArgumentException
    *            One or more arguments are null (or empty), or there's invalid
    *            content in the nodes being processed.
    */
   private void populateWidgets(final Map<String, FixtureWidget> formFixtureWidgets, final String idBase,
         final ModifiableFixtureWidget parentWidget, final NodeList childNodes)
   {
      // NOTES:
      // * The formFixtureWidgets is an "out" argument that starts out as empty.
      // * The parentWidget is null when we are populating top-level widgets
      //   and non-null when populating child widgets.
      if (null == formFixtureWidgets)
         throw new IllegalArgumentException("The form's fixture widgets map must be non-null.");
      if ((null == idBase) || idBase.isEmpty())
         throw new IllegalArgumentException("The ID base must be non-empty.");
      if (null == childNodes)
         throw new IllegalArgumentException("The child node list must be non-null.");

      // Go through the child nodes and create fixture widgets based on the
      // widget elements among those child nodes.
      // NOTE: The way the recursion stops is that a widget has child nodes
      //       (such as attribute nodes) but none of those children are widget
      //       elements.
      int childCount = childNodes.getLength();
      for (int i = 0; i < childCount; i++)
      {
         Node childNode = childNodes.item(i);
         if ((null == childNode) || !(childNode instanceof Element)
               || !ConfigurationConstants.Tags.WIDGET.equals(childNode.getNodeName()))
            continue; // ignore everything that isn't a widget element.

         Element widgetElem = (Element)childNode;

         // A widget element must have name, ID, and type attributes.
         String widgetName = widgetElem.getAttribute(ConfigurationConstants.Attributes.NAME);
         if ((null == widgetName) || widgetName.isEmpty())
            throw new IllegalArgumentException("All '" + ConfigurationConstants.Tags.WIDGET + "' elements must have '"
                  + ConfigurationConstants.Attributes.NAME + "' attributes with non-empty values.");
         if (formFixtureWidgets.containsKey(widgetName))
            throw new IllegalArgumentException("The form can only contain one '" + ConfigurationConstants.Tags.WIDGET
                  + "' element whose '" + ConfigurationConstants.Attributes.NAME + "' attribute has the value of '"
                  + widgetName + "'.");

         String widgetID = widgetElem.getAttribute(ConfigurationConstants.Attributes.ID);
         if ((null == widgetID) || widgetID.isEmpty())
            throw new IllegalArgumentException("All '" + ConfigurationConstants.Tags.WIDGET + "' elements must have '"
                  + ConfigurationConstants.Attributes.ID + "' attributes with non-empty values.");

         String widgetType = widgetElem.getAttribute(ConfigurationConstants.Attributes.TYPE);
         if ((null == widgetType) || widgetType.isEmpty())
            throw new IllegalArgumentException("All '" + ConfigurationConstants.Tags.WIDGET + "' elements must have '"
                  + ConfigurationConstants.Attributes.TYPE + "' attributes with non-empty values.");

         // Use the widget element's information to create the fixture widget.
         // If the type of widget is not (yet) supported for FitNesse tests,
         // the factory will log a message and return null.
         ModifiableFixtureWidget fixtureWidget = factory.createFixtureWidget(widgetName, widgetID, widgetType, idBase);
         if (null == fixtureWidget)
         {
            // Even though this element isn't supported for Fitnesse tests,
            // work through any child widget elements within this element - 
            // they will just use our parent element as their parent element.
            populateWidgets(formFixtureWidgets, idBase, parentWidget, widgetElem.getChildNodes());
         }
         else
         {
            // If the widget is a child widget, set parent-child relationships.
            if (null != parentWidget)
            {
               parentWidget.addChild(widgetName, fixtureWidget);
               fixtureWidget.setParent(parentWidget); // null for top-level widgets
            }

            // Add the fixture widget to the map, then work through the child
            // nodes of the widget (those nodes may or may not contain widget
            // elements).
            formFixtureWidgets.put(widgetName, fixtureWidget);
            populateWidgets(formFixtureWidgets, idBase, fixtureWidget, widgetElem.getChildNodes());
         }
      }
   }

   /**
    * <p>
    * Gets the configuration file at the specified configuration path.
    * </p>
    * 
    * <p>
    * NOTE: This method never returns null.
    * </p>
    * 
    * @param configurationPath
    *           The path to the file containing fixture configuration
    *           information for the UI (for example,
    *           "c:\fitnesse\fixtureconfig\ar5104_widgetinfo.xml").
    * 
    * @return The configuration file at the specified configuration path.
    * 
    * @throws IllegalArgumentException
    *            The configuration path is null or invalid.
    */
   private static File getConfigurationFile(final String configurationPath)
   {
      if ((null == configurationPath) || configurationPath.isEmpty())
         throw new IllegalArgumentException("The configuration path must be non-empty.");

      File configurationFile = new File(configurationPath);
      if (!configurationFile.isFile()) // does an implicit "exists" check too
         throw new IllegalArgumentException("'" + configurationPath + "' is not the path to an existing file.");

      return configurationFile;
   }

   /**
    * Gets the XML parser to parse the fixture configuration file at the
    * specified path into a DOM document.
    * 
    * @param configurationFile
    *           The file containing fixture configuration information for the
    *           UI.
    * 
    * @return The DOM document representation of the fixture configuration data
    *         from the specified file.
    * 
    * @throws IllegalArgumentException
    *            The file was not an XML file that could be parsed into a DOM
    *            document.
    */
   private static Document getConfigurationDOM(final File configurationFile)
   {
      if ((null == configurationFile) || !configurationFile.isFile())
         throw new IllegalArgumentException("The configuration file must be an existing file.");

      try
      {
         return DOCUMENT_BUILDER.parse(configurationFile);
      }
      catch (Exception e)
      {
         throw new IllegalArgumentException("The configuration file '" + configurationFile.getPath()
               + "' could not be parsed into a DOM document.", e);
      }
   }

   /**
    * <p>
    * Uses the specified form definition ID to creates the form-wide ID base
    * (prefix) used in automation locators for widgets belonging to the same
    * form.
    * </p>
    * 
    * <p>
    * NOTE: This method never returns null.
    * </p>
    * 
    * @param definitionID
    *           The form definition ID used to create the form-wide ID base
    *           (prefix) used in automation locators for widgets belonging to
    *           the same form.
    * 
    * @return The form-wide ID base (prefix) used in automation locators for
    *         widgets belonging to the same form.
    */
   private static String createIDBase(final String definitionID)
   {
      if ((null == definitionID) || definitionID.isEmpty())
         throw new IllegalArgumentException("The form's definition ID must be non-empty.");

      StringBuilder sbIDBase = new StringBuilder();
      sbIDBase.append(definitionID);
      sbIDBase.append("_");

      return sbIDBase.toString();
   }
}
