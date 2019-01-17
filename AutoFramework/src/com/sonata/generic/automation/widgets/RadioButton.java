/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */
package com.sonata.generic.automation.widgets;

import com.sonata.generic.automation.browser.Browser;

/**
 * The <code>RadioButton</code> class represents a radio button VersionOne
 * [B-12262]
 */
public class RadioButton extends Widget
{
   /**
    * constructs an instance of the {@link RadioButton} class.
    * 
    * @param locator
    *           the locator of the radio button as it appears in the DOM on the
    *           browser
    * @param browser
    *           the browser that will be used to access the radio button
    */
   public RadioButton(final String locator, final Browser browser)
   {
      super(locator, browser);
   }

   /**
    * Select (Check) a radio button. The method will first check to make sure
    * the location is visible and editable.
    * 
    * @return <li><code>true</code> if select was successful.</li> <li>
    *         <code>false</code> if select was unsuccessful.</li>
    */
   public boolean select()
   {
      return this.getBrowser().selectRadioButton(this.getLocator());
      
   }

   /**
    * Validates if the radio button is selected (checked) or not.
    * 
    * @return <li><code>True</code> if the value is checked / selected.</li> <li>
    *         <code>False</code> if the value is not checked / selected</li>
    * 
    */
   public boolean isSelected()
   {
      return this.getBrowser().isSelected(this.getLocator());
   }

}
