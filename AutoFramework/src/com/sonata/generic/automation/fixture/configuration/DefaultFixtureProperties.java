/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.fixture.configuration;

import java.util.Map;

import com.sonata.generic.automation.browser.Browser;
import com.sonata.generic.automation.browser.TestMode;
import com.sonata.generic.automation.fixture.widget.FixtureWidget;

/**
 * Default implementation of {@link FixtureProperties}.
 */
public class DefaultFixtureProperties implements FixtureProperties
{
   private final Browser                                 browser;
   private final String                                  uiName;
   private final String                                  uiMenuName;
   private final String                                  category;
   private  String                                       iframe = null;
   private final String                                  application;
   private final String                                  applicationfullname;
   private final Map<String, FixtureWidget>              existenceValidationWidgets;
   private final Map<String, Map<String, FixtureWidget>> fixtureWidgets;
   private final String                                  signInValidationElement;

   /**
    * Constructs a {@link FixtureProperties} object containing properties
    * related to the current fixture instance (which drives the UI under test).
    * Some of the inputs to the constructor are obtained by parsing fixture
    * configuration data for that UI.
    * 
    * @param browser
    *           The automation {@link Browser} object used by the fixture.
    * @param uiName
    *           The name of the UI (for example, "BatchList") that the fixture will
    *           be testing, as used in determining the URL of the UI's web page
    *           and the automation locators of the widgets.
    * @param uiMenuName
    *           The menu name of the UI (for example, "Batch List") as shown in the portal
    *           page, as used in determining the menu location by navigating through
    *           portal home page. It might be different from the uiName when it's composed 
    *           by more than one word.
    * @param category
    *           The category of the UI (for example, "G/L Setup") that the UI
    *           under test belongs to.
    * @param application
    *           The short name of the application (for example, "GL") that the UI
    *           under test belongs to.
    * @param applicationfullname
    *           The full name of the application (for example, "General Ledger") that the UI
    *           under test belongs to.
    * @param existenceValidationWidgets
    *           The map of existence validation {@link FixtureWidget} objects,
    *           keyed by form name, where the existence of the widget indicates
    *           that its associated form is opened successfully.
    * @param fixtureWidgets
    *           The nested map of {@link FixtureWidget} objects for the widgets
    *           on the UI and its popup forms, with the outer map keyed by
    *           descriptive form name (or "" for the main form) and the inner
    *           map keyed by descriptive descriptive widget names.
    * 
    * @throws IllegalArgumentException
    *            One or more arguments are null, or have invalid contents.
    */
   public DefaultFixtureProperties(final Browser browser, final String uiName, final String uiMenuName,  final String category, final String application, 
         final String applicationfullname,
         final Map<String, FixtureWidget> existenceValidationWidgets,
         final Map<String, Map<String, FixtureWidget>> fixtureWidgets)
   {
      if (null == browser)
         throw new IllegalArgumentException("The automation browser object must be non-null.");
      if (null == uiName)
         throw new IllegalArgumentException("The UI name must be non-empty.");
      //if ((null == application) || application.isEmpty())
      //  	throw new IllegalArgumentException("The application name must be non-empty.");      
      if (null == application)
      	throw new IllegalArgumentException("The application name must be non-empty.");
      if ((null == existenceValidationWidgets) || existenceValidationWidgets.isEmpty())
         throw new IllegalArgumentException("The existence validation widget map must be non-empty.");
      if ((null == fixtureWidgets) || fixtureWidgets.isEmpty())
         throw new IllegalArgumentException("The fixture widget map must be non-empty.");

      // Set the members that come directly from constructor arguments.
      this.browser = browser;
      this.uiName = uiName;
      this.uiMenuName = uiMenuName;
      this.category = category;
      this.application = application;
      this.applicationfullname = applicationfullname;
      this.existenceValidationWidgets = existenceValidationWidgets;
      this.fixtureWidgets = fixtureWidgets;

      // Set the sign-in validation element, which is the wait target locator
      // of the main form's existence validation widget.
      FixtureWidget signInValidationWidget = getExistenceValidationWidget("");
      if (null == signInValidationWidget)
         throw new IllegalArgumentException("The main form must have an existence validation widget.");

      this.signInValidationElement = signInValidationWidget.getWaitTargetLocator();
      if ((this.signInValidationElement == null) || this.signInValidationElement.isEmpty())
         throw new IllegalArgumentException("The sign-in validation widget must have a non-empty wait target locator.");
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
   public FixtureWidget getExistenceValidationWidget(final String formName)
   {
      String formNameToUse = (null != formName) ? formName : "";
      return existenceValidationWidgets.get(formNameToUse); // might be null
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public FixtureWidget getFixtureWidget(final String formName, final String widgetName)
   {
      String formNameToUse = (null != formName) ? formName : "";
      Map<String, FixtureWidget> fixtureWidgetsInForm = fixtureWidgets.get(formNameToUse);
      if (null == fixtureWidgetsInForm)
         return null;

      return fixtureWidgetsInForm.get(widgetName); // might be null
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getApplication()
   {
      return application;
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public String getApplicationfullname()
   {
      return applicationfullname;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getCategory()
   {
      return category;
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public String getUIName()
   {
      return uiName;
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public String getUiMenuName()
   {
      return uiMenuName;
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public String getIFrame()
   {
      return iframe;
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public void setIFrame(String iframe)
   {
      this.iframe = iframe;
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public String getSignInValidationElement()
   {
      return signInValidationElement;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getURL(final TestMode testMode)
   {
      StringBuilder sbURL = new StringBuilder();
      
      if (!application.toLowerCase().equals("redirected") && ! uiName.toLowerCase().equals("redirected"))
      {
         sbURL.append("/");
         sbURL.append(application);
         sbURL.append("/");
         sbURL.append(uiName);  
      }
      
      return sbURL.toString();
   }
}
