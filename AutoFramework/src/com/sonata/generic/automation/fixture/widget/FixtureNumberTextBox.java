/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.fixture.widget;

import com.sonata.generic.automation.browser.Browser;
import com.sonata.generic.automation.widgets.NumberTextBox;

/**
 * A fixture-friendly representation of a password text box.
 */
public final class FixtureNumberTextBox extends AbstractFixtureTextBox
{
   // NOTE: For security reasons we don't expose functions that get or try to
   //       validate the content of the number text box.
   private static final FixtureWidgetCreator CREATOR = new FixturePasswordTextBoxCreator();
   private final NumberTextBox             numberTextBox;

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
   private FixtureNumberTextBox(final String widgetName, final String widgetID, final String idBase,
         final Browser browser)
   {
      super(widgetName, widgetID, idBase, browser);
      this.numberTextBox = new NumberTextBox(getLocator(), browser);
   }

   /**
    * The {@link FixtureWidgetCreator} that is used to create instances of this
    * type of {@link ModifiableFixtureWidget}.
    */
   private static class FixturePasswordTextBoxCreator implements FixtureWidgetCreator
   {
      /**
       * {@inheritDoc}
       */
      @Override
      public ModifiableFixtureWidget createFixtureWidget(final String widgetName, final String widgetID,
            final String idBase, final Browser browser)
      {
         return new FixtureNumberTextBox(widgetName, widgetID, idBase, browser);
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
      return "NumberTextBox";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected String getFriendlyWidgetType()
   {
      return "NumberTextBox";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean clear()
   {
      return numberTextBox.clear();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean click()
   {
      return numberTextBox.click();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean type(final String value)
   {
      return numberTextBox.type(value);
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean typeTelephoneNumber(final String value)
   {
      return numberTextBox.typeTelephoneNumber(value);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean typeWithoutTab(final String value)
   {
      return numberTextBox.type(value, false);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean typeWithoutClear(final String value)
   {
      return numberTextBox.typeWithoutClear(value);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean typeWithoutClearAndWithoutTab(final String value)
   {
      return numberTextBox.typeWithoutClear(value, false);
   }

}
