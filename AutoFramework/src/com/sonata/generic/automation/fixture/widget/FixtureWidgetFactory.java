/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.fixture.widget;

import com.sonata.generic.automation.browser.Browser;

/**
 * Interface for a factory used to create {@link ModifiableFixtureWidget}
 * instances from fixture configuration data. These instances are FitNesse
 * fixture friendly representations of widgets, whose parent-child relationships
 * can be set after creation.
 */
public interface FixtureWidgetFactory
{
   /**
    * Gets the automation {@link Browser} object that the widgets created by the
    * factory will run in.
    * 
    * @return The automation {@link Browser} object that the widgets created by
    *         the factory will run in.
    */
   Browser getBrowser();

   /**
    * Uses the specified ID base (prefix), widget name, widget ID, and widget
    * type to creates a {@link ModifiableFixtureWidget} instance, which is a
    * FitNesse fixture friendly representation of the widget whose parent-child
    * relationship can be set later. If a fixture widget of that type cannot be
    * created, null is returned.
    * 
    * @param widgetName
    *           The descriptive name of the widget, as used by callers of the
    *           fixture (such as the FitNesse test page) to identify the widget
    *           on which an action should be performed.
    * @param widgetID
    *           The underlying widget ID (as specified in the declarative UI
    *           definition) that is used to calculate the widget's automation
    *           locator.
    * @param widgetType
    *           The SWT widget type (as listed in the declarative UI definition)
    *           of the widget, which determines the underlying type of fixture
    *           widget that gets created and the actions that it supports.
    * @param idBase
    *           The ID base (prefix) used to calculate automation locators for
    *           widgets on a form.
    * 
    * @return The newly created {@link ModifiableFixtureWidget} instance, or
    *         null if a fixture widget of that type cannot be created.
    * 
    * @throws IllegalArgumentException
    *            One or more arguments are null or invalid.
    */
   ModifiableFixtureWidget createFixtureWidget(String widgetName, String widgetID, String widgetType, String idBase);
}
