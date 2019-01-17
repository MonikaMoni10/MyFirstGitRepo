/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.fixture.widget;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.sonata.generic.automation.browser.Browser;
import com.sonata.generic.automation.fixture.browser.BrowserFactory;
import com.sonata.generic.automation.fixture.browser.DefaultBrowserFactory;

/**
 * Default implementation of a {@link FixtureWidgetFactory}.
 */
public final class DefaultFixtureWidgetFactory implements FixtureWidgetFactory
{
   private static final Map<String, FixtureWidgetCreator> CREATORS_BY_TYPE;
   private final Browser                                  browser;

   static
   {
      Map<String, FixtureWidgetCreator> creatorsByType = new HashMap<String, FixtureWidgetCreator>();

      // NOTE: For proper maintenance, please keep these in alphabetical order.
 
      // Standard / generic widgets, from the generic app
      creatorsByType.put(FixtureButton.getSwtWidgetType(), FixtureButton.getFixtureWidgetCreator());
      creatorsByType.put(FixtureCheckBox.getSwtWidgetType(), FixtureCheckBox.getFixtureWidgetCreator());
      creatorsByType.put(FixtureComboBox.getSwtWidgetType(), FixtureComboBox.getFixtureWidgetCreator());
      creatorsByType.put(FixtureComboBoxFinder.getSwtWidgetType(), FixtureComboBoxFinder.getFixtureWidgetCreator());
      creatorsByType.put(FixtureLabel.getSwtWidgetType(), FixtureLabel.getFixtureWidgetCreator());
      creatorsByType.put(FixtureListBox.getSwtWidgetType(), FixtureListBox.getFixtureWidgetCreator());
      creatorsByType.put(FixtureRadioButton.getSwtWidgetType(), FixtureRadioButton.getFixtureWidgetCreator());
      creatorsByType.put(FixtureTab.getSwtWidgetType(), FixtureTab.getFixtureWidgetCreator());
      creatorsByType.put(FixtureTable.getSwtWidgetType(), FixtureTable.getFixtureWidgetCreator());
      creatorsByType.put(FixtureOnePageTable.getSwtWidgetType(), FixtureOnePageTable.getFixtureWidgetCreator());
      creatorsByType.put(FixtureTextBox.getSwtWidgetType(), FixtureTextBox.getFixtureWidgetCreator());

      // Customized widgets, from the "generic app"
      creatorsByType.put(FixtureNumberTextBox.getSwtWidgetType(), FixtureNumberTextBox.getFixtureWidgetCreator());      
      
      CREATORS_BY_TYPE = Collections.unmodifiableMap(creatorsByType);
   }

   /**
    * Constructs a factory instance that creates {@link ModifiableFixtureWidget}
    * instances from fixture configuration data where those fixture widgets will
    * be run in an automation {@link Browser} object with default browser
    * settings. Those fixture widgets can have their parent-child relationships
    * set after creation.
    */
   public DefaultFixtureWidgetFactory()
   {
      this(null);
   }

   /**
    * Constructs a factory instance that creates {@link ModifiableFixtureWidget}
    * instances from fixture configuration data where those fixture widgets will
    * be run in an automation {@link Browser} object with the specified browser
    * settings. Those fixture widgets can have their parent-child relationships
    * set after creation.
    * 
    * @param browserSettings
    *           The settings used for creating a specific kind of automation
    *           {@link Browser} object, or null to create an automation
    *           {@code Browser} object with default settings.
    */
   public DefaultFixtureWidgetFactory(final String browserSettings)
   {
      this(browserSettings, new DefaultBrowserFactory());
   }

   /**
    * <p>
    * Package-private constructor for facilitating unit tests.
    * </p>
    * 
    * <p>
    * Constructs a factory instance that creates {@link ModifiableFixtureWidget}
    * instances from fixture configuration data where those fixture widgets will
    * be run in an automation {@link Browser} object with the specified browser
    * settings (and created by the specified browser factory). Those fixture
    * widgets can have their parent-child relationships set after creation.
    * </p>
    * 
    * @param browserSettings
    *           The settings used for creating a specific kind of automation
    *           {@link Browser} object, or null to create an automation
    *           {@code Browser} object with default settings.
    * @param browserFactory
    *           The {@link BrowserFactory} instance used to create the
    *           automation {@link Browser} object that will be used by the
    *           fixture widget factory.
    * 
    * @throws IllegalArgumentException
    *            The browser factory is null.
    */
   DefaultFixtureWidgetFactory(final String browserSettings, final BrowserFactory browserFactory)
   {
      if (null == browserFactory)
         throw new IllegalArgumentException("The browser factory must be non-null.");

      if (!(browserSettings.equals("use existing")) && (browserSettings.equals(null)) || browserSettings.isEmpty())
         this.browser = browserFactory.createDefaultBrowser();
      else
         this.browser = browserFactory.createSpecifiedBrowser(browserSettings);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Browser getBrowser()
   {
      return browser;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public ModifiableFixtureWidget createFixtureWidget(final String widgetName, final String widgetID,
         final String widgetType, final String idBase)
   {
      FixtureWidgetCreator creator = CREATORS_BY_TYPE.get(widgetType);
      if (null == creator)
      {
         StringBuilder sbExceptionMsg = new StringBuilder();
         sbExceptionMsg.append("Widget '");
         sbExceptionMsg.append(widgetName); // if null, appends "null"
         sbExceptionMsg.append("' is of type '");
         sbExceptionMsg.append(widgetType); // if null, appends "null"
         sbExceptionMsg.append("' which is not supported for FitNesse tests.");

         // This is a non-fatal problem, so just log a message to the system
         // output.  In FitNesse that output is available by clicking on the
         // "output captured" link at the top right corner of the test page
         // after the test run is finished.
         System.out.println(sbExceptionMsg.toString());
         return null;
      }

      // NOTE: Individual fixture widget creators may return null or throw
      //       exceptions when trying to create a fixture widget.
      return creator.createFixtureWidget(widgetName, widgetID, idBase, browser);
   }
}
