/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.fixture.widget;

import com.sonata.generic.automation.browser.Browser;
import com.sonata.generic.automation.widgets.Button;
import com.sonata.generic.automation.widgets.Widget;

/**
 * Base class for a {@link FixtureWidget} that wraps the underlying automation
 * {@link Button} widget, but that may represent different SWT widgets (such as
 * Button and ImageButton).
 */
public abstract class AbstractFixtureButton extends AbstractFixtureWidget
{
   private final Button button;

   /**
    * Base class constructor for a fixture-friendly representation of a widget
    * that wraps the underlying automation {@link Button} widget (since more
    * than one type of SWT widget works with that type of automation widget).
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
   protected AbstractFixtureButton(final String widgetName, final String widgetID, final String idBase,
         final Browser browser)
   {
      super(widgetName, widgetID, idBase, browser);
      this.button = new Button(getLocator(), browser);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected Widget getAutomationWidget()
   {
      return button;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean click()
   {
      return button.click();
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean clickByReturn()
   {
      return button.clickByReturn();
   }
   
   /**
    * =========================================================
    * Sikuli related actions
    * 
    */
   
   /**
    * {@inheritDoc}
    */   
   @Override
   public boolean clickImage()
   {
      return button.clickImage();
   }    
   /**
    * =========================================================
    * Sikuli related actions
    * 
    */
   
   /**
    * {@inheritDoc}
    */   
   @Override
   public boolean doubleClickImage()
   {
      return button.doubleClickImage();
   }
   
   /**
    * {@inheritDoc}
    */   
   @Override
   public boolean rightClickImage()
   {
      return button.rightClickImage();
   }
   
}
