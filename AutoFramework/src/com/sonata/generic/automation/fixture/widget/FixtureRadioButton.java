/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.fixture.widget;

import com.sonata.generic.automation.browser.Browser;
import com.sonata.generic.automation.widgets.RadioButton;
import com.sonata.generic.automation.widgets.Widget;

/**
 * A fixture-friendly representation of a radio button.
 */
public final class FixtureRadioButton extends AbstractFixtureWidget
{
   private static final FixtureWidgetCreator CREATOR = new FixtureRadioButtonCreator();
   private final RadioButton                 radiobutton;

   /**
    * Constructs a fixture-friendly representation of a radio button. This
    * constructor is private and only available to the
    * {@link FixtureWidgetCreator}.
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
   private FixtureRadioButton(final String widgetName, final String widgetID, final String idBase, final Browser browser)
   {
      super(widgetName, widgetID, idBase, browser);
      this.radiobutton = new RadioButton(getLocator(), browser);
   }

   /**
    * The {@link FixtureWidgetCreator} that is used to create instances of this
    * type of {@link FixtureWidget}.
    */
   private static class FixtureRadioButtonCreator implements FixtureWidgetCreator
   {
      /**
       * {@inheritDoc}
       */
      @Override
      public ModifiableFixtureWidget createFixtureWidget(final String widgetName, final String widgetID,
            final String idBase, final Browser browser)
      {
         return new FixtureRadioButton(widgetName, widgetID, idBase, browser);
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
      return "genericRadioButton";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected String getFriendlyWidgetType()
   {
      return "RadioButton";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected Widget getAutomationWidget()
   {
      return radiobutton;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean isSelected()
   {
      return radiobutton.isSelected();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean select()
   {
      return radiobutton.select();
   }
}
