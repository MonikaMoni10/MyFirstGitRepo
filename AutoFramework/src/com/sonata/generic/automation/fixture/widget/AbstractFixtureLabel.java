/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.fixture.widget;

import com.sonata.generic.automation.browser.Browser;
import com.sonata.generic.automation.widgets.Label;
import com.sonata.generic.automation.widgets.Widget;

/**
 * Base class for a {@link FixtureWidget} that wraps the underlying automation
 * {@link Label} widget, but that may represent different SWT widgets (such as
 * label and data label).
 */
public abstract class AbstractFixtureLabel extends AbstractFixtureWidget
{
   private final Label label;

   /**
    * Base class constructor for a fixture-friendly representation of a widget
    * that wraps the underlying automation {@link Label} widget (since more than
    * one type of SWT widget works with that type of automation widget).
    * 
    * @param widgetName
    *           The descriptive name of the widget, as used by callers of the
    *           fixture (such as the FitNesse test page) to identify the widget
    *           on which an action should be performed.
    * @param widgetID
    *           The underlying widget ID (as specified in the declarative UI
    *           definition) that is used to calculate the widget's automation
    *           locator.
    * @param idBase
    *           The debug ID prefix that is used to calculate the widget's
    *           automation locator.
    * @param browser
    *           The automation {@link Browser} object that the widget will run
    *           in.
    * 
    * @throws IllegalArgumentException
    *            One or more arguments are null or empty.
    */
   protected AbstractFixtureLabel(final String widgetName, final String widgetID, final String idBase,
         final Browser browser)
   {
      super(widgetName, widgetID, idBase, browser);
      this.label = new Label(getLocator(), browser);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected Widget getAutomationWidget()
   {
      return label;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getText()
   {
      return label.getText();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean click()
   {
      return label.click();
   }
   
   /**
    * ========================================================
    * Sikuli related actions
    * 
    */
   
   /**
    * {@inheritDoc}
    */ 
   @Override
   public boolean selectMenuItemFromImage(String menuTitle, String menuItem)
   {
      return label.selectMenuItemFromImage(menuTitle, menuItem);
   }   
}

