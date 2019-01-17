/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.fixture.configuration;

import com.sonata.generic.automation.browser.Browser;
import com.sonata.generic.automation.fixture.widget.FixtureWidget;
import com.sonata.generic.automation.uibase.UIProperties;

/**
 * Interface for properties for a
 * {@link com.sage.swt.automation.GenericWebFixture.SwtUIFixture SwtUIFixture} instance.
 */
public interface FixtureProperties extends UIProperties
{
   /**
    * Gets the automation {@link Browser} object used by the fixture.
    * 
    * @return The automation {@link Browser} object used by the fixture.
    */
   Browser getBrowser();

   /**
    * Gets the fixture-friendly representation of the widget whose existence
    * indicates that the given form is successfully opened (use "" to represent
    * the main UI).
    * 
    * @param formName
    *           Name of the form whose existence validation widget we want to
    *           get, or use "" if the widget is on the main UI.
    * 
    * @return The fixture-friendly representation of the widget whose existence
    *         indicates that the given form is successfully opened.
    */
   FixtureWidget getExistenceValidationWidget(String formName);

   /**
    * Gets the fixture-friendly representation of the widget with the given name
    * that's on the given form (use "" to represent the main UI), or null if
    * that widget is not found.
    * 
    * @param formName
    *           Name of the form where the widget in question resides, or use ""
    *           if the widget is on the main UI.
    * @param widgetName
    *           Name of the widget to get.
    * 
    * @return A fixture-friendly representation of the widget, or null if that
    *         widget is not found.
    */
   FixtureWidget getFixtureWidget(String formName, String widgetName);
}
