/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.fixture.configuration;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sonata.generic.automation.fixture.configuration.ConfigurationConstants;
import com.sonata.generic.automation.fixture.configuration.DefaultConfigurationParser;

/**
 * Mock DOM documents to be parsed by the {@link DefaultConfigurationParser}
 * under test.
 */
final class DefaultConfigurationParserMockDOMs
{
   private static final DocumentBuilder DOC_BUILDER;

   /**
    * Don't create instances.
    */
   private DefaultConfigurationParserMockDOMs()
   {
   }

   static
   {
      try
      {
         DOC_BUILDER = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      }
      catch (ParserConfigurationException e)
      {
         throw new IllegalStateException("Could not create default document builder.");
      }
   }

   /**
    * Creates an empty DOM.
    * 
    * @return An empty DOM.
    */
   public static Document createEmptyDOM()
   {
      return DOC_BUILDER.newDocument();
   }

   /**
    * Creates a DOM whose root element is not a "ui" element.
    * 
    * @return A DOM whose root element is not a "ui" element.
    */
   public static Document createDOMNonUIRootElem()
   {
      Document doc = createEmptyDOM();

      Element rootElem = doc.createElement("badRootElemName");
      doc.appendChild(rootElem);

      return doc;
   }

   /**
    * Creates a DOM with a "ui" element with an empty value for its "name"
    * attribute.
    * 
    * @return A DOM with a "ui" element with an empty value for its "name"
    *         attribute.
    */
   public static Document createDOMUIElemEmptyName()
   {
      return createDOMWithUIElem("", DefaultConfigurationParserMockConstants.MOCK_APPLICATION);
   }

   /**
    * Creates a DOM with a "ui" element without a "name" attribute.
    * 
    * @return A DOM with a "ui" element without a "name" attribute.
    */
   public static Document createDOMUIElemNoName()
   {
      return createDOMWithUIElem(null, DefaultConfigurationParserMockConstants.MOCK_APPLICATION);
   }

   /**
    * Creates a DOM with a "ui" element with an empty value for its
    * "application" attribute.
    * 
    * @return A DOM with a "ui" element with an empty value for its
    *         "application" attribute.
    */
   public static Document createDOMUIElemEmptyApplication()
   {
      return createDOMWithUIElem(DefaultConfigurationParserMockConstants.MOCK_UI_NAME, "");
   }

   /**
    * Creates a DOM with a "ui" element without an "application" attribute.
    * 
    * @return A DOM with a "ui" element without an "application" attribute.
    */
   public static Document createDOMUIElemNoApplication()
   {
      return createDOMWithUIElem(DefaultConfigurationParserMockConstants.MOCK_UI_NAME, null);
   }

   /**
    * Creates a DOM with only a valid "ui" element.
    * 
    * @return A DOM with only a valid "ui" element.
    */
   public static Document createDOMToUIElemOnly()
   {
      return createDOMWithUIElem(DefaultConfigurationParserMockConstants.MOCK_UI_NAME,
            DefaultConfigurationParserMockConstants.MOCK_APPLICATION);
   }

   /**
    * Creates a DOM with a valid "ui" element but with a popup "form" element
    * that has an empty "name" attribute.
    * 
    * @return A DOM with a valid "ui" element but with a popup "form" element
    *         that has an empty "name" attribute.
    */
   public static Document createDOMFormElemPopupEmptyName()
   {
      return createDOMWithFormElem("", DefaultConfigurationParserMockConstants.MOCK_DEFINITION_ID,
            DefaultConfigurationParserMockConstants.MOCK_EXISTENCE_VALIDATION_WIDGET,
            ConfigurationConstants.FormType.POPUP);
   }

   /**
    * Creates a DOM with a valid "ui" element but with a popup "form" element
    * that has no "name" attribute.
    * 
    * @return A DOM with a valid "ui" element but with a popup "form" element
    *         that has no "name" attribute.
    */
   public static Document createDOMFormElemPopupNoName()
   {
      return createDOMWithFormElem(null, DefaultConfigurationParserMockConstants.MOCK_DEFINITION_ID,
            DefaultConfigurationParserMockConstants.MOCK_EXISTENCE_VALIDATION_WIDGET,
            ConfigurationConstants.FormType.POPUP);
   }

   /**
    * Creates a DOM with a valid "ui" element but with a (main) "form" element
    * that has an empty "definitionID" attribute.
    * 
    * @return A DOM with a valid "ui" element but with a (main) "form" element
    *         that has an empty "definitionID" attribute.
    */
   public static Document createDOMFormElemEmptyDefinitionID()
   {
      return createDOMWithFormElem(null, "", DefaultConfigurationParserMockConstants.MOCK_EXISTENCE_VALIDATION_WIDGET,
            ConfigurationConstants.FormType.MAIN);
   }

   /**
    * Creates a DOM with a valid "ui" element but with a (main) "form" element
    * that has no "definitionID" attribute.
    * 
    * @return A DOM with a valid "ui" element but with a (main) "form" element
    *         that has no "definitionID" attribute.
    */
   public static Document createDOMFormElemNoDefinitionID()
   {
      return createDOMWithFormElem(null, null,
            DefaultConfigurationParserMockConstants.MOCK_EXISTENCE_VALIDATION_WIDGET,
            ConfigurationConstants.FormType.MAIN);
   }

   /**
    * Creates a DOM with a valid "ui" element but with a (main) "form" element
    * that has an empty "existenceValidationWidget" attribute.
    * 
    * @return A DOM with a valid "ui" element but with a (main) "form" element
    *         that has an empty "existenceValidationWidget" attribute.
    */
   public static Document createDOMFormElemEmptyExistenceValidationWidget()
   {
      return createDOMWithFormElem(null, DefaultConfigurationParserMockConstants.MOCK_DEFINITION_ID, "",
            ConfigurationConstants.FormType.MAIN);
   }

   /**
    * Creates a DOM with a valid "ui" element but with a (main) "form" element
    * that has no "existenceValidationWidget" attribute.
    * 
    * @return A DOM with a valid "ui" element but with a (main) "form" element
    *         that has no "existenceValidationWidget" attribute.
    */
   public static Document createDOMFormElemNoExistenceValidationWidget()
   {
      return createDOMWithFormElem(null, DefaultConfigurationParserMockConstants.MOCK_DEFINITION_ID, null,
            ConfigurationConstants.FormType.MAIN);
   }

   /**
    * Creates a DOM with a valid "ui" element but with a (main) "form" element
    * that has an empty "type" attribute.
    * 
    * @return A DOM with a valid "ui" element but with a (main) "form" element
    *         that has an empty "type" attribute.
    */
   public static Document createDOMFormElemEmptyType()
   {
      return createDOMWithFormElem(null, DefaultConfigurationParserMockConstants.MOCK_DEFINITION_ID,
            DefaultConfigurationParserMockConstants.MOCK_EXISTENCE_VALIDATION_WIDGET, "");
   }

   /**
    * Creates a DOM with a valid "ui" element but with a (main) "form" element
    * that has no "type" attribute.
    * 
    * @return A DOM with a valid "ui" element but with a (main) "form" element
    *         that has no "type" attribute.
    */
   public static Document createDOMFormElemNoType()
   {
      return createDOMWithFormElem(null, DefaultConfigurationParserMockConstants.MOCK_DEFINITION_ID,
            DefaultConfigurationParserMockConstants.MOCK_EXISTENCE_VALIDATION_WIDGET, null);
   }

   /**
    * Creates a DOM with a valid "ui" element but with a (main) "form" element
    * that has an invalid "type" attribute.
    * 
    * @return A DOM with a valid "ui" element but with a (main) "form" element
    *         that has an invalid "type" attribute.
    */
   public static Document createDOMFormElemInvalidType()
   {
      return createDOMWithFormElem(null, DefaultConfigurationParserMockConstants.MOCK_DEFINITION_ID,
            DefaultConfigurationParserMockConstants.MOCK_EXISTENCE_VALIDATION_WIDGET, "badFormType");
   }

   /**
    * Creates a DOM with a valid "ui" element and a valid main "form" element
    * under it (a valid main form does not need a name), but no more than that.
    * 
    * @return A DOM with only a valid "ui" element and a valid main "form"
    *         element under it, but no more than that.
    */
   public static Document createDOMToFormElemOnly()
   {
      return createDOMWithFormElem(null, DefaultConfigurationParserMockConstants.MOCK_DEFINITION_ID,
            DefaultConfigurationParserMockConstants.MOCK_EXISTENCE_VALIDATION_WIDGET,
            ConfigurationConstants.FormType.MAIN);
   }

   /**
    * Creates a DOM up to a valid "form" element but with a "widget" element
    * that has an empty "name" attribute.
    * 
    * @return A DOM up to a valid "form" element but with a "widget" element
    *         that has an empty "name" attribute.
    */
   public static Document createDOMWidgetElemEmptyName()
   {
      Document doc = createDOMToUIElemOnly();

      Element uiElem = doc.getDocumentElement();
      Element formElem = createValidMainFormElemInParent(doc, uiElem);
      createWidgetElemInParent(doc, formElem, "", DefaultConfigurationParserMockConstants.MOCK_WIDGET_ID1,
            DefaultConfigurationParserMockConstants.MOCK_WIDGET_TYPE);

      return doc;
   }

   /**
    * Creates a DOM up to a valid "form" element but with a "widget" element
    * that has no "name" attribute.
    * 
    * @return A DOM up to a valid "form" element but with a "widget" element
    *         that has no "name" attribute.
    */
   public static Document createDOMWidgetElemNoName()
   {
      Document doc = createDOMToUIElemOnly();

      Element uiElem = doc.getDocumentElement();
      Element formElem = createValidMainFormElemInParent(doc, uiElem);
      createWidgetElemInParent(doc, formElem, null, DefaultConfigurationParserMockConstants.MOCK_WIDGET_ID1,
            DefaultConfigurationParserMockConstants.MOCK_WIDGET_TYPE);

      return doc;
   }

   /**
    * Creates a DOM up to a valid "form" element but with two "widget" elements
    * that both have the same "name" attribute.
    * 
    * @return A DOM up to a valid "form" element but with two "widget" elements
    *         that both have the same "name" attribute.
    */
   public static Document createDOMWidgetElemsSameName()
   {
      Document doc = createDOMToUIElemOnly();

      Element uiElem = doc.getDocumentElement();
      Element formElem = createValidMainFormElemInParent(doc, uiElem);
      createWidgetElemInParent(doc, formElem, DefaultConfigurationParserMockConstants.MOCK_WIDGET_NAME1,
            DefaultConfigurationParserMockConstants.MOCK_WIDGET_ID_EXISTENCE,
            DefaultConfigurationParserMockConstants.MOCK_WIDGET_TYPE);
      createWidgetElemInParent(doc, formElem, DefaultConfigurationParserMockConstants.MOCK_WIDGET_NAME1,
            DefaultConfigurationParserMockConstants.MOCK_WIDGET_ID2,
            DefaultConfigurationParserMockConstants.MOCK_WIDGET_TYPE);

      return doc;
   }

   /**
    * Creates a DOM up to a valid "form" element but with a "widget" element
    * that has an empty "id" attribute.
    * 
    * @return A DOM up to a valid "form" element but with a "widget" element
    *         that has an empty "id" attribute.
    */
   public static Document createDOMWidgetElemEmptyID()
   {
      Document doc = createDOMToUIElemOnly();

      Element uiElem = doc.getDocumentElement();
      Element formElem = createValidMainFormElemInParent(doc, uiElem);
      createWidgetElemInParent(doc, formElem, DefaultConfigurationParserMockConstants.MOCK_WIDGET_NAME1, "",
            DefaultConfigurationParserMockConstants.MOCK_WIDGET_TYPE);

      return doc;
   }

   /**
    * Creates a DOM up to a valid "form" element but with a "widget" element
    * that has no "id" attribute.
    * 
    * @return A DOM up to a valid "form" element but with a "widget" element
    *         that has no "id" attribute.
    */
   public static Document createDOMWidgetElemNoID()
   {
      Document doc = createDOMToUIElemOnly();

      Element uiElem = doc.getDocumentElement();
      Element formElem = createValidMainFormElemInParent(doc, uiElem);
      createWidgetElemInParent(doc, formElem, DefaultConfigurationParserMockConstants.MOCK_WIDGET_NAME1, null,
            DefaultConfigurationParserMockConstants.MOCK_WIDGET_TYPE);

      return doc;
   }

   /**
    * Creates a DOM up to a valid "form" element but with only one "widget"
    * element that has an empty "type" attribute.
    * 
    * @return A DOM up to a valid "form" element but with only one "widget"
    *         element that has an empty "type" attribute.
    */
   public static Document createDOMWidgetElemEmptyType()
   {
      Document doc = createDOMToUIElemOnly();

      Element uiElem = doc.getDocumentElement();
      Element formElem = createValidMainFormElemInParent(doc, uiElem);
      createWidgetElemInParent(doc, formElem, DefaultConfigurationParserMockConstants.MOCK_WIDGET_NAME1,
            DefaultConfigurationParserMockConstants.MOCK_WIDGET_ID1, "");

      return doc;
   }

   /**
    * Creates a DOM up to a valid "form" element but with only one "widget"
    * element that has no "type" attribute.
    * 
    * @return A DOM up to a valid "form" element but with only one "widget"
    *         element that has no "type" attribute.
    */
   public static Document createDOMWidgetElemNoType()
   {
      Document doc = createDOMToUIElemOnly();

      Element uiElem = doc.getDocumentElement();
      Element formElem = createValidMainFormElemInParent(doc, uiElem);
      createWidgetElemInParent(doc, formElem, DefaultConfigurationParserMockConstants.MOCK_WIDGET_NAME1,
            DefaultConfigurationParserMockConstants.MOCK_WIDGET_ID1, null);

      return doc;
   }

   /**
    * Creates a DOM up to a valid "form" element but with only one "widget"
    * element that has an invalid "type" attribute.
    * 
    * @return A DOM up to a valid "form" element but with only one "widget"
    *         element that has an invalid "type" attribute.
    */
   public static Document createDOMWidgetElemInvalidType()
   {
      Document doc = createDOMToUIElemOnly();

      Element uiElem = doc.getDocumentElement();
      Element formElem = createValidMainFormElemInParent(doc, uiElem);
      createWidgetElemInParent(doc, formElem, DefaultConfigurationParserMockConstants.MOCK_WIDGET_NAME1,
            DefaultConfigurationParserMockConstants.MOCK_WIDGET_ID1,
            DefaultConfigurationParserMockConstants.BAD_WIDGET_TYPE);

      return doc;
   }

   /**
    * Creates a DOM up to a valid main "form" element with one "widget" element
    * but it is not the existence validation widget.
    * 
    * @return A DOM that is valid and contains one main "form" element with one
    *         "widget" element but it is not the existence validation widget.
    */
   public static Document createDOMToOneWidgetNoExistenceValidation()
   {
      Document doc = createDOMToUIElemOnly();

      Element uiElem = doc.getDocumentElement();
      Element formElem = createValidMainFormElemInParent(doc, uiElem);
      createName1WidgetElemInParent(doc, formElem);

      return doc;
   }

   /**
    * Creates a DOM that is valid and includes a main "form" element with one
    * "widget" element that is the existence validation widget.
    * 
    * @return A DOM that is valid and includes a main "form" element with one
    *         "widget" element that is the existence validation widget.
    */
   public static Document createDOMToExistenceValidationWidgetOnly()
   {
      Document doc = createDOMToUIElemOnly();

      Element uiElem = doc.getDocumentElement();
      Element formElem = createValidMainFormElemInParent(doc, uiElem);
      createExistenceValidationWidgetElemInParent(doc, formElem);

      return doc;
   }

   /**
    * Creates a DOM that is valid and includes a main "form" element with one
    * "widget" element that is the existence validation widget as well as an
    * element with a different tag name (which gets ignored).
    * 
    * @return A DOM that is valid and includes a main "form" element with one
    *         "widget" element that is the existence validation widget as well
    *         as an element with a different tag name (which gets ignored).
    */
   public static Document createDOMToExistenceValidationWidgetPlusIgnoredElem()
   {
      Document doc = createDOMToUIElemOnly();

      Element uiElem = doc.getDocumentElement();
      Element formElem = createValidMainFormElemInParent(doc, uiElem);
      createExistenceValidationWidgetElemInParent(doc, formElem);
      formElem.appendChild(doc.createElement("ignored"));

      return doc;
   }

   /**
    * Creates a DOM that is valid and includes a main "form" element with a
    * parent "widget" element (the existence validation widget) of a valid type
    * and a child "widget" element of an invalid type (which gets ignored).
    * 
    * @return A DOM that is valid and includes a main "form" element with a
    *         parent "widget" element (the existence validation widget) of a
    *         valid type and a child "widget" element of an invalid type (which
    *         gets ignored).
    */
   public static Document createDOMParentWidgetValidTypeChildWidgetBadType()
   {
      Document doc = createDOMToUIElemOnly();

      Element uiElem = doc.getDocumentElement();
      Element formElem = createValidMainFormElemInParent(doc, uiElem);
      Element parentWidgetElem = createExistenceValidationWidgetElemInParent(doc, formElem);
      createWidgetElemInParent(doc, parentWidgetElem, DefaultConfigurationParserMockConstants.MOCK_WIDGET_NAME1,
            DefaultConfigurationParserMockConstants.MOCK_WIDGET_ID1,
            DefaultConfigurationParserMockConstants.BAD_WIDGET_TYPE);

      return doc;
   }

   /**
    * Creates a DOM that is valid and includes a main "form" element with a
    * parent "widget" element of an invalid type (which gets ignored) and a
    * child "widget" element (the existence validation widget) of a valid type
    * (which gets placed at the parent level).
    * 
    * @return A DOM that is valid and includes a main "form" element with a
    *         parent "widget" element of an invalid type (which gets ignored)
    *         and a child "widget" element (the existence validation widget) of
    *         a valid type (which gets placed at the parent level).
    */
   public static Document createDOMParentWidgetBadTypeChildWidgetValidType()
   {
      Document doc = createDOMToUIElemOnly();

      Element uiElem = doc.getDocumentElement();
      Element formElem = createValidMainFormElemInParent(doc, uiElem);
      Element parentWidgetElem = createWidgetElemInParent(doc, formElem,
            DefaultConfigurationParserMockConstants.MOCK_WIDGET_NAME1,
            DefaultConfigurationParserMockConstants.MOCK_WIDGET_ID1,
            DefaultConfigurationParserMockConstants.BAD_WIDGET_TYPE);
      createExistenceValidationWidgetElemInParent(doc, parentWidgetElem);

      return doc;
   }

   /**
    * Creates a DOM that is valid and includes a main "form" element with a
    * parent "widget" element and children "widget" elements, all of valid
    * types.
    * 
    * @return A DOM that is valid and includes a main "form" element with a
    *         parent "widget" element and children "widget" elements, all of
    *         valid types.
    */
   public static Document createDOMParentChildrenWidgetsValidTypes()
   {
      Document doc = createDOMToUIElemOnly();

      Element uiElem = doc.getDocumentElement();
      Element formElem = createValidMainFormElemInParent(doc, uiElem);
      Element parentWidgetElem = createName1WidgetElemInParent(doc, formElem);
      createName2WidgetElemInParent(doc, parentWidgetElem);
      createExistenceValidationWidgetElemInParent(doc, parentWidgetElem);

      return doc;
   }

   /**
    * Creates a DOM that includes a popup "form" element and the existence
    * widget for that form, but is missing a main "form" element.
    * 
    * @return A DOM that includes a popup "form" element and the existence
    *         widget for that form, but is missing a main "form" element.
    */
   public static Document createDOMPopupFormOnly()
   {
      Document doc = createDOMToUIElemOnly();

      Element uiElem = doc.getDocumentElement();
      Element formElem = createPopup1FormElemInParent(doc, uiElem);
      createExistenceValidationWidgetElemInParent(doc, formElem);

      return doc;
   }

   /**
    * Creates a DOM that includes a valid main "form" element and a popup "form"
    * element whose child widgets do not include an existence validation widget.
    * 
    * @return A DOM that includes a valid main "form" element and a popup "form"
    *         element whose child widgets do not include an existence validation
    *         widget.
    */
   public static Document createDOMMainOKPopupNoExistenceValidation()
   {
      Document doc = createDOMToUIElemOnly();

      Element uiElem = doc.getDocumentElement();

      Element mainFormElem = createValidMainFormElemInParent(doc, uiElem);
      createExistenceValidationWidgetElemInParent(doc, mainFormElem);

      Element popup1FormElem = createPopup1FormElemInParent(doc, uiElem);
      createName1WidgetElemInParent(doc, popup1FormElem);

      return doc;
   }

   /**
    * Creates a DOM that is valid and includes a main "form" element followed by
    * two popup "form" elements, where all three forms have existence validation
    * "widget" elements within them.
    * 
    * @return A DOM that is valid and includes a main "form" element followed by
    *         two popup "form" elements, where all three forms have existence
    *         validation "widget" elements within them.
    */
   public static Document createDOMMainPopup1Popup2()
   {
      Document doc = createDOMToUIElemOnly();

      Element uiElem = doc.getDocumentElement();

      Element mainFormElem = createValidMainFormElemInParent(doc, uiElem);
      createExistenceValidationWidgetElemInParent(doc, mainFormElem);

      Element popup1FormElem = createPopup1FormElemInParent(doc, uiElem);
      createExistenceValidationWidgetElemInParent(doc, popup1FormElem);

      // NOTE: Popup 2's existence validation widget is the "name 2" one.
      Element popup2FormElem = createPopup2FormElemInParent(doc, uiElem);
      createName2WidgetElemInParent(doc, popup2FormElem);

      return doc;
   }

   /**
    * Creates a DOM that is valid and includes a popup 1 "form" element,
    * followed by the main "form" element, and followed by a popup 2 "form"
    * element, where all three forms have existence validation "widget" elements
    * within them.
    * 
    * @return A DOM that is valid and includes a popup 1 "form" element,
    *         followed by the main "form" element, and followed by a popup 2
    *         "form" element, where all three forms have existence validation
    *         "widget" elements within them.
    */
   public static Document createDOMPopup1MainPopup2()
   {
      Document doc = createDOMToUIElemOnly();

      Element uiElem = doc.getDocumentElement();

      Element popup1FormElem = createPopup1FormElemInParent(doc, uiElem);
      createExistenceValidationWidgetElemInParent(doc, popup1FormElem);

      Element mainFormElem = createValidMainFormElemInParent(doc, uiElem);
      createExistenceValidationWidgetElemInParent(doc, mainFormElem);

      // NOTE: Popup 2's existence validation widget is the "name 2" one.
      Element popup2FormElem = createPopup2FormElemInParent(doc, uiElem);
      createName2WidgetElemInParent(doc, popup2FormElem);

      return doc;
   }

   /**
    * Creates a DOM that is valid and includes two popup "form" elements
    * followed by the main "form" element, where all three forms have existence
    * validation "widget" elements within them.
    * 
    * @return A DOM that is valid and includes two popup "form" elements
    *         followed by the main "form" element, where all three forms have
    *         existence validation "widget" elements within them.
    */
   public static Document createDOMPopup1Popup2Main()
   {
      Document doc = createDOMToUIElemOnly();

      Element uiElem = doc.getDocumentElement();

      Element popup1FormElem = createPopup1FormElemInParent(doc, uiElem);
      createExistenceValidationWidgetElemInParent(doc, popup1FormElem);

      // NOTE: Popup 2's existence validation widget is the "name 2" one.
      Element popup2FormElem = createPopup2FormElemInParent(doc, uiElem);
      createName2WidgetElemInParent(doc, popup2FormElem);

      Element mainFormElem = createValidMainFormElemInParent(doc, uiElem);
      createExistenceValidationWidgetElemInParent(doc, mainFormElem);

      return doc;
   }

   /**
    * Creates a DOM document with a root "ui" element containing the specified
    * name and application attribute values.
    * 
    * @param name
    *           The value for the "ui" element's "name" attribute.
    * @param application
    *           The value for the "ui" element's "application" attribute.
    * 
    * @return A DOM document with a root "ui" element containing the specified
    *         name and application attribute values.
    */
   private static Document createDOMWithUIElem(final String name, final String application)
   {
      Document doc = createEmptyDOM();

      Element uiElem = createUIElem(doc, name, application);
      doc.appendChild(uiElem);

      return doc;
   }

   /**
    * Creates a DOM document with a valid root "ui" element whose child is a
    * "form" element containing the specified attribute values.
    * 
    * @param name
    *           The value for the "form" element's "name" attribute.
    * @param definitionID
    *           The value for the "form" element's "definitionID" attribute.
    * @param existenceValidationWidget
    *           The value for the "form" element's "existenceValidationWidget"
    *           attribute.
    * @param type
    *           The value for the "form" element's "type" attribute.
    * 
    * @return A DOM document with a valid root "ui" element whose child is a
    *         "form" element containing the specified attribute values.
    */
   private static Document createDOMWithFormElem(final String name, final String definitionID,
         final String existenceValidationWidget, final String type)
   {
      Document doc = createDOMToUIElemOnly();

      Element uiElem = doc.getDocumentElement();
      createFormElemInParent(doc, uiElem, name, definitionID, existenceValidationWidget, type);

      return doc;
   }

   /**
    * Creates a valid main "form" element and appends it to the specified parent
    * element (which would be the "ui" element in a valid DOM).
    * 
    * @param doc
    *           The DOM document.
    * @param parentElem
    *           The parent element to append the "form" element to.
    * 
    * @return The newly created valid main "form" element which is already
    *         appended as a child of the specified parent element.
    */
   private static Element createValidMainFormElemInParent(final Document doc, final Element parentElem)
   {
      return createFormElemInParent(doc, parentElem, null, DefaultConfigurationParserMockConstants.MOCK_DEFINITION_ID,
            DefaultConfigurationParserMockConstants.MOCK_EXISTENCE_VALIDATION_WIDGET,
            ConfigurationConstants.FormType.MAIN);
   }

   /**
    * Creates a valid popup 1 "form" element and appends it to the specified
    * parent element (which would be the "ui" element in a valid DOM).
    * 
    * @param doc
    *           The DOM document.
    * @param parentElem
    *           The parent element to append the "form" element to.
    * 
    * @return The newly created popup 1 "form" element which is already appended
    *         as a child of the specified parent element.
    */
   private static Element createPopup1FormElemInParent(final Document doc, final Element parentElem)
   {
      return createFormElemInParent(doc, parentElem, DefaultConfigurationParserMockConstants.MOCK_POPUP1_NAME,
            DefaultConfigurationParserMockConstants.MOCK_DEFINITION_ID,
            DefaultConfigurationParserMockConstants.MOCK_EXISTENCE_VALIDATION_WIDGET,
            ConfigurationConstants.FormType.POPUP);
   }

   /**
    * Creates a valid popup 2 "form" element and appends it to the specified
    * parent element (which would be the "ui" element in a valid DOM). Note that
    * this popup "form" element lists a different existence validation widget
    * than the other forms.
    * 
    * @param doc
    *           The DOM document.
    * @param parentElem
    *           The parent element to append the "form" element to.
    * 
    * @return The newly created popup 2 "form" element which is already appended
    *         as a child of the specified parent element.
    */
   private static Element createPopup2FormElemInParent(final Document doc, final Element parentElem)
   {
      return createFormElemInParent(doc, parentElem, DefaultConfigurationParserMockConstants.MOCK_POPUP2_NAME,
            DefaultConfigurationParserMockConstants.MOCK_DEFINITION_ID,
            DefaultConfigurationParserMockConstants.MOCK_WIDGET_NAME2, ConfigurationConstants.FormType.POPUP);
   }

   /**
    * Creates a "form" element and appends it to the specified parent element
    * (which would be the "ui" element in a valid DOM).
    * 
    * @param doc
    *           The DOM document.
    * @param parentElem
    *           The parent element to append the "form" element to.
    * @param name
    *           The value for the element's "name" attribute, or null to not set
    *           that attribute.
    * @param definitionID
    *           The value for the element's "definitionID" attribute, or null to
    *           not set that attribute.
    * @param existenceValidationWidget
    *           The value for the element's "existenceValidationWidget"
    *           attribute, or null to not set that attribute.
    * @param type
    *           The value for the element's "type" attribute, or null to not set
    *           that attribute.
    * 
    * @return The newly created "form" element which is already appended as a
    *         child of the specified parent element.
    */
   private static Element createFormElemInParent(final Document doc, final Element parentElem, final String name,
         final String definitionID, final String existenceValidationWidget, final String type)
   {
      Element formElem = createFormElem(doc, name, definitionID, existenceValidationWidget, type);
      parentElem.appendChild(formElem);

      return formElem;
   }

   /**
    * Creates a valid existence validation "widget" element and appends it to
    * the specified parent element (which would be either a "form" element or a
    * "widget" element in a valid DOM).
    * 
    * @param doc
    *           The DOM document.
    * @param parentElem
    *           The parent element to append the "widget" element to.
    * 
    * @return The newly created existence validation "widget" element which is
    *         already appended as a child of the specified parent element.
    */
   private static Element createExistenceValidationWidgetElemInParent(final Document doc, final Element parentElem)
   {
      return createWidgetElemInParent(doc, parentElem,
            DefaultConfigurationParserMockConstants.MOCK_EXISTENCE_VALIDATION_WIDGET,
            DefaultConfigurationParserMockConstants.MOCK_WIDGET_ID_EXISTENCE,
            DefaultConfigurationParserMockConstants.MOCK_WIDGET_TYPE);
   }

   /**
    * Creates a valid name 1 "widget" element and appends it to the specified
    * parent element (which would be either a "form" element or a "widget"
    * element in a valid DOM).
    * 
    * @param doc
    *           The DOM document.
    * @param parentElem
    *           The parent element to append the "widget" element to.
    * 
    * @return The newly created name 1 "widget" element which is already
    *         appended as a child of the specified parent element.
    */
   private static Element createName1WidgetElemInParent(final Document doc, final Element parentElem)
   {
      return createWidgetElemInParent(doc, parentElem, DefaultConfigurationParserMockConstants.MOCK_WIDGET_NAME1,
            DefaultConfigurationParserMockConstants.MOCK_WIDGET_ID1,
            DefaultConfigurationParserMockConstants.MOCK_WIDGET_TYPE);
   }

   /**
    * Creates a valid name 2 "widget" element and appends it to the specified
    * parent element (which would be either a "form" element or a "widget"
    * element in a valid DOM).
    * 
    * @param doc
    *           The DOM document.
    * @param parentElem
    *           The parent element to append the "widget" element to.
    * 
    * @return The newly created name 2 "widget" element which is already
    *         appended as a child of the specified parent element.
    */
   private static Element createName2WidgetElemInParent(final Document doc, final Element parentElem)
   {
      return createWidgetElemInParent(doc, parentElem, DefaultConfigurationParserMockConstants.MOCK_WIDGET_NAME2,
            DefaultConfigurationParserMockConstants.MOCK_WIDGET_ID2,
            DefaultConfigurationParserMockConstants.MOCK_WIDGET_TYPE);
   }

   /**
    * Creates a "widget" element and appends it to the specified parent element
    * (which would be either a "form" element or a "widget" element in a valid
    * DOM).
    * 
    * @param doc
    *           The DOM document.
    * @param parentElem
    *           The parent element to append the "widget" element to.
    * @param name
    *           The value for the element's "name" attribute, or null to not set
    *           that attribute.
    * @param id
    *           The value for the element's "id" attribute, or null to not set
    *           that attribute.
    * @param type
    *           The value for the element's "type" attribute, or null to not set
    *           that attribute.
    * 
    * @return The newly created "widget" element which is already appended as a
    *         child of the specified parent element.
    */
   private static Element createWidgetElemInParent(final Document doc, final Element parentElem, final String name,
         final String id, final String type)
   {
      Element widgetElem = createWidgetElem(doc, name, id, type);
      parentElem.appendChild(widgetElem);

      return widgetElem;
   }

   /**
    * Creates a "ui" element containing the specified attribute values.
    * 
    * @param doc
    *           The DOM document.
    * @param name
    *           The value for the element's "name" attribute, or null to not set
    *           that attribute.
    * @param application
    *           The value for the element's "application" attribute, or null to
    *           not set that attribute.
    * 
    * @return The newly created "ui" element.
    */
   private static Element createUIElem(final Document doc, final String name, final String application)
   {
      Element uiElem = doc.createElement(ConfigurationConstants.Tags.UI);

      if (null != name)
         uiElem.setAttribute(ConfigurationConstants.Attributes.NAME, name);
      if (null != application)
         uiElem.setAttribute(ConfigurationConstants.Attributes.APPLICATION, application);

      return uiElem;
   }

   /**
    * Creates a "form" element containing the specified attribute values.
    * 
    * @param doc
    *           The DOM document.
    * @param name
    *           The value for the element's "name" attribute, or null to not set
    *           that attribute.
    * @param definitionID
    *           The value for the element's "definitionID" attribute, or null to
    *           not set that attribute.
    * @param existenceValidationWidget
    *           The value for the element's "existenceValidationWidget"
    *           attribute, or null to not set that attribute.
    * @param type
    *           The value for the element's "type" attribute, or null to not set
    *           that attribute.
    * 
    * @return The newly created "form" element.
    */
   private static Element createFormElem(final Document doc, final String name, final String definitionID,
         final String existenceValidationWidget, final String type)
   {
      Element formElem = doc.createElement(ConfigurationConstants.Tags.FORM);

      if (null != name)
         formElem.setAttribute(ConfigurationConstants.Attributes.NAME, name);
      if (null != definitionID)
         formElem.setAttribute(ConfigurationConstants.Attributes.DEFINITION_ID, definitionID);
      if (null != existenceValidationWidget)
         formElem
               .setAttribute(ConfigurationConstants.Attributes.EXISTENCE_VALIDATION_WIDGET, existenceValidationWidget);
      if (null != type)
         formElem.setAttribute(ConfigurationConstants.Attributes.TYPE, type);

      return formElem;
   }

   /**
    * Creates a "widget" element containing the specified attribute values.
    * 
    * @param doc
    *           The DOM document.
    * @param name
    *           The value for the element's "name" attribute, or null to not set
    *           that attribute.
    * @param id
    *           The value for the element's "id" attribute, or null to not set
    *           that attribute.
    * @param type
    *           The value for the element's "type" attribute, or null to not set
    *           that attribute.
    * 
    * @return The newly created "widget" element.
    */
   private static Element createWidgetElem(final Document doc, final String name, final String id, final String type)
   {
      Element widgetElem = doc.createElement(ConfigurationConstants.Tags.WIDGET);

      if (null != name)
         widgetElem.setAttribute(ConfigurationConstants.Attributes.NAME, name);
      if (null != id)
         widgetElem.setAttribute(ConfigurationConstants.Attributes.ID, id);
      if (null != type)
         widgetElem.setAttribute(ConfigurationConstants.Attributes.TYPE, type);

      return widgetElem;
   }
}
