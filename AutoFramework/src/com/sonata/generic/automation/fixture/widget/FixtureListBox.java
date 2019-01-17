/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.fixture.widget;

import com.sonata.generic.automation.browser.Browser;
import com.sonata.generic.automation.widgets.ComboBox;
import com.sonata.generic.automation.widgets.ListBox;
import com.sonata.generic.automation.widgets.Widget;

/**
 * A fixture-friendly representation of a list box.
 */
public final class FixtureListBox extends AbstractFixtureWidget
{
   private static final FixtureWidgetCreator CREATOR = new FixtureListBoxCreator();
   private final ListBox                    listbox;

   /**
    * Constructs a fixture-friendly representation of a list box. This
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
   private FixtureListBox(final String widgetName, final String widgetID, final String idBase, final Browser browser)
   {
      super(widgetName, widgetID, idBase, browser);
      this.listbox = new ListBox(getLocator(), browser);
   }

   /**
    * The {@link FixtureWidgetCreator} that is used to create instances of this
    * type of {@link FixtureWidget}.
    */
   private static class FixtureListBoxCreator implements FixtureWidgetCreator
   {
      /**
       * {@inheritDoc}
       */
      @Override
      public ModifiableFixtureWidget createFixtureWidget(final String widgetName, final String widgetID,
            final String idBase, final Browser browser)
      {
         return new FixtureListBox(widgetName, widgetID, idBase, browser);
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
      return "genericListBox";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected String getFriendlyWidgetType()
   {
      return "ListBox";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected Widget getAutomationWidget()
   {
      return listbox;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String[] getAllOptions()
   {
      String[] options;
      try
      {
         options = listbox.getAllOptions();
      }
      catch (Exception e)
      {
         throw new IllegalArgumentException("Could not get the options for widget '" + getWidgetName() + "'.", e);
      }

      return options;
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public String[] getSelectedOptions()
   {
      String[] options;
      try
      {
         options = listbox.getSelectedOptions();
      }
      catch (Exception e)
      {
         throw new IllegalArgumentException("Could not get the options for widget '" + getWidgetName() + "'.", e);
      }

      return options;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getText()
   {
      return listbox.getSelectedValue();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean select(final String option)
   {
      return listbox.selectComboBox(option, true);
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean selectOption(final String option)
   {
      return listbox.selectOption(option);
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean deselect(final String option)
   {
      return listbox.deselectFromListBox(option);
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean deselectAll()
   {
      return listbox.deselectAllFromListBox();
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean selectAll()
   {
      return listbox.selectAllFromListBox();
   }
}
