/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.widgets;

import com.sonata.generic.automation.browser.Browser;

/**
 * The <code>WidgetType</code> enumerates the various widgets and provides a
 * method for creating instances of the {@link Widget} interface for them.
 */
public enum WidgetType
{
   /** TextBox */
   TEXTBOX(new TextBoxFactory()),

   /** ComboBox */
   COMBOBOX(new ComboBoxFactory()),
   
   /** ComboBoxFinder */
   COMBOBOXFINDER(new ComboBoxFinderFactory()),

   /** CheckBox */
   CHECKBOX(new CheckBoxFactory()),

   /** Label */
   LABEL(new LabelFactory()),

   /** Link Button */
   BUTTON(new ButtonFactory());

   //   DATEBOX(new DateBoxFactory());

   /** The factory for this type */
   private final WidgetFactory factory;

   /**
    * Constructs an instance of the {@link WidgetType} enumeration with the
    * given factory
    * 
    * @param factory
    *           the factory that can create instances of this type
    */
   private WidgetType(final WidgetFactory factory)
   {
      this.factory = factory;
   }

   /**
    * returns a {@link Widget} of this type with the given settings
    * 
    * @param locator
    *           the locator to use to construct the widget
    * @param browser
    *           the browser to use to construct the widget
    * @return a corresponding {@link Widget}.
    */
   public WidgetInterface createWidget(String locator, Browser browser)
   {
      if (locator == null || browser == null)
      {
         throw new IllegalArgumentException("The locator and the browser you provide must not be null");
      }
      return factory.createWidget(locator, browser);
   }

   /** The interface for the factory */
   private static interface WidgetFactory
   {
      /**
       * Creates an instance of the {@link Widget} interface
       * 
       * @param locator
       *           the locator to use to construct the widget
       * @param browser
       *           the browser to use to construct the widget
       * @return a {@link Widget}
       */
      WidgetInterface createWidget(String locator, Browser browser);
   }

   /** A factory for TextBox widgets */
   private static class TextBoxFactory implements WidgetFactory
   {
      @Override
      public WidgetInterface createWidget(String locator, Browser browser)
      {
         return new TextBox(locator, browser);
      }
   }

   /** A factory for ComboBox widgets */
   private static class ComboBoxFactory implements WidgetFactory
   {
      @Override
      public WidgetInterface createWidget(String locator, Browser browser)
      {
         return new ComboBox(locator, browser);
      }
   }

   /** A factory for ComboBoxFinder widgets */
   private static class ComboBoxFinderFactory implements WidgetFactory
   {
      @Override
      public WidgetInterface createWidget(String locator, Browser browser)
      {
         return new ComboBoxFinder(locator, browser);
      }
   }
   /** a factory for CheckBox widgets */
   private static class CheckBoxFactory implements WidgetFactory
   {
      @Override
      public WidgetInterface createWidget(String locator, Browser browser)
      {
         return new CheckBox(locator, browser);
      }
   }

   /** A factory for Label widgets */
   private static class LabelFactory implements WidgetFactory
   {
      @Override
      public WidgetInterface createWidget(String locator, Browser browser)
      {
         return new Label(locator, browser);
      }
   }

   /** A factory for Button widgets */
   private static class ButtonFactory implements WidgetFactory
   {
      @Override
      public WidgetInterface createWidget(String locator, Browser browser)
      {
         return new Button(locator, browser);
      }
   }
   //
   //   /** A factory for DateBox widgets */
   //   private static class DateBoxFactory implements WidgetFactory
   //   {
   //      @Override
   //      public WidgetInterface createWidget(String locator, Browser browser)
   //      {
   //         return new DateBox(locator, browser);
   //      }
   //   }
}
