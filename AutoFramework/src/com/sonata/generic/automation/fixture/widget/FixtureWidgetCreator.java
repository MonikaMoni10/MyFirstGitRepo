/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.fixture.widget;

import com.sonata.generic.automation.browser.Browser;

/**
 * Interface for classes that that create {@link ModifiableFixtureWidget}
 * instances, which are FitNesse fixture friendly representations of widgets,
 * whose parent-child relationships can be set after creation.
 */
public interface FixtureWidgetCreator
{
   /**
    * Creates a FitNesse fixture friendly representation of the widget with the
    * specified name, widget ID, and form-wide ID base (prefix), where that
    * fixture widget will run in the specified automation {@link Browser}
    * object. The fixture widget's parent-child relationships can be set after
    * creation.
    * 
    * @param widgetName
    *           The descriptive name of the widget, as used by callers of the
    *           fixture (such as the FitNesse test page) to identify the widget
    *           on which an action should be performed.
    * @param widgetID
    *           The underlying widget ID (as specified in the declarative UI
    *           definition) that is used in the widget's automation locator.
    * @param idBase
    *           The form-wide ID base (prefix) used in automation locators for
    *           widgets belonging to the same form.
    * @param browser
    *           The automation {@link Browser} object that the widget will run
    *           in.
    * 
    * @return The {@link ModifiableFixtureWidget} that is a FitNesse fixture
    *         friendly representation of the widget whose parent-child
    *         relationships can be set after creation.
    * 
    * @throws IllegalArgumentException
    *            One or more arguments are null or empty.
    */
   ModifiableFixtureWidget createFixtureWidget(String widgetName, String widgetID, String idBase, Browser browser);
}
