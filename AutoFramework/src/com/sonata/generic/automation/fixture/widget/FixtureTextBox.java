/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.fixture.widget;

import com.sonata.generic.automation.browser.Browser;
import com.sonata.generic.automation.library.TimeDelay;
import com.sonata.generic.automation.widgets.TextBox;

/**
 * A fixture-friendly representation of a text box.
 */
public final class FixtureTextBox extends AbstractFixtureTextBox
{
   private static final FixtureWidgetCreator CREATOR = new FixtureTextBoxCreator();
   private final TextBox                     textBox;

   /**
    * Constructs fixture-friendly representation of a text box. This constructor
    * is private and only available to the {@link FixtureWidgetCreator}.
    * 
    * @param widgetName
    *           The descriptive name of the widget, as used by callers of the
    *           fixture (such as the FitNesse test page) to identify the widget
    *           on which an action should be performed.
    * @param widgetID
    *           The underlying widget ID (as specified in the declarative UI
    *           definition) that is used in this widget's automation locator.
    * @param idBase
    *           The form-wide ID base (prefix) used in automation locators for
    *           widgets belonging to the same form.
    * @param browser
    *           The automation {@link Browser} object that the widget will run
    *           in.
    * 
    * @throws IllegalArgumentException
    *            One or more arguments are null or empty.
    */
   private FixtureTextBox(final String widgetName, final String widgetID, final String idBase, final Browser browser)
   {
      super(widgetName, widgetID, idBase, browser);
      this.textBox = super.getAutomationTextBox();
   }

   /**
    * The {@link FixtureWidgetCreator} that is used to create instances of this
    * type of {@link ModifiableFixtureWidget}.
    */
   private static class FixtureTextBoxCreator implements FixtureWidgetCreator
   {
      /**
       * {@inheritDoc}
       */
      @Override
      public ModifiableFixtureWidget createFixtureWidget(final String widgetName, final String widgetID,
            final String idBase, final Browser browser)
      {
         return new FixtureTextBox(widgetName, widgetID, idBase, browser);
      }
   }

   /**
    * Gets the {@link FixtureWidgetCreator} that is used to create instances of
    * this type of {@link FixtureWidget}.
    * 
    * @return The {@link FixtureWidgetCreator} that is used to create instances
    *         of this type of {@link FixtureWidget}.
    */
   public static FixtureWidgetCreator getFixtureWidgetCreator()
   {
      return CREATOR;
   }

   /**
    * Gets the SWT widget type (as listed in the declarative UI definition) of
    * the kind of widget represented by this {@link FixtureWidget}.
    * 
    * @return The SWT widget type (as listed in the declarative UI definition)
    *         of the kind of widget represented by this {@link FixtureWidget}.
    */
   public static String getSwtWidgetType()
   {
      return "genericTextBox";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected String getFriendlyWidgetType()
   {
      return "TextBox";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean clearAndValidate(final String defaultValue)
   {
      return textBox.clearAndValidate(defaultValue);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean selectCalendarDate(String date)
   {
      textBox.getCalendarButton().click();
      
      TimeDelay.doMediumPause();
      
      return textBox.getCalendarWidget().setCalendarDate(date);
   }      
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean waitForContent(final String content)
   {
      return textBox.waitForContent(content);
   }
}
