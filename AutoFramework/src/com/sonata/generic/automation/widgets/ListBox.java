/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */
package com.sonata.generic.automation.widgets;

import com.sonata.generic.automation.browser.Browser;

/**
 * The <code>ListBox</code> class represents a listBox
 */
public class ListBox extends Widget
{
   /**
    * constructs an instance of the {@link ListBox} class.
    * 
    * @param locator
    *           the locator of the listbox as it appears in the spy of the
    *           desktop application
    * 
    * @param browser
    *           the browser that will be used to access the listbox
    * 
    */
   public ListBox(final String locator, final Browser browser)
   {
      super(locator, browser);
   }

   /**
    * Returns a string array, containing all the available options for a
    * comboBox.
    * 
    * @return a String array containing all the available options. If the
    *         comboBox cannot be located, or if there are no values, the return
    *         value is <code>null</code>.
    */
   public String[] getAllOptions()
   {
      try
      {
         return this.getBrowser().getAllOptionsFromListBox(this.getLocator());
      }
      catch (Exception e)
      {
         throw new IllegalArgumentException("Could not get the options for widget", e);
      }

   }   
   
   /**
    * Returns a string array, containing the selected options for a
    * listBox which may support multi-selections.
    * 
    * @return a String array containing the selected options. If the
    *         listBox cannot be located, or if there are no values, the return
    *         value is <code>null</code>.
    */
   public String[] getSelectedOptions()
   {
      try
      {
         return this.getBrowser().getSelectedOptionsFromListBox(this.getLocator());
      }
      catch (Exception e)
      {
         throw new IllegalArgumentException("Could not get the options for widget", e);
      }

   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean selectComboBox(final String value, boolean textValidation)
   {
      return this.getBrowser().selectFromList(this.getLocator(), value);
   }
   
   /**
    * Selects an option from a listBox.
    * 
    * @param option
    *           The option value to be selected
    * @return <li><code>true</code> if the the selection was successful.</li>
    *         <li><code>false</code> if the selection was not successful.</li>
    */      
   public boolean selectOption(final String option)
   {
      String baseLocator = this.getLocator();
      int i = 1;
      String locator = "//div[@id='" + baseLocator + "']/div[1]";

      while (this.getBrowser().existsNoWait(locator))
      {
         if ( this.getBrowser().getText(locator).equals(option))
            return this.getBrowser().click(locator);
         
         i++;
         locator = "//div[@id='" + baseLocator + "']/div[" + i + "]"; 
      }
      return false;      
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean deselectFromListBox(final String value)
   {
      return this.getBrowser().deselectFromList(this.getLocator(), value);
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean deselectAllFromListBox()
   {
      return this.getBrowser().removeAllSelections(this.getLocator());
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean selectAllFromListBox()
   {
      return this.getBrowser().selectAllSelections(this.getLocator());
   }
}
