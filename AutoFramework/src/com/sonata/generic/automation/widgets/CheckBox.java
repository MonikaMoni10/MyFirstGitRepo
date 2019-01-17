/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.widgets;

import com.sonata.generic.automation.browser.Browser;

/**
 * The <code>CheckBox</code> class represents a check box
 */
public class CheckBox extends Widget
{

   /**
    * constructs an instance of the {@link CheckBox} class.
    * 
    * @param locator
    *           the locator of the check box as it appears in the DOM on the
    *           browser. As of July 2011, the locator passed in is to a <span>
    *           element, to get at the CheckBox we must tweak the locator to be
    *           an XPath to the <input> of the <span>. A direct id of this
    *           <input> cannot be used as the id is somewhat random (for
    *           example, "gwt-uid-11"). An alternative would be to append the
    *           xpath when accessing the methods, however when using
    *           getLocator() it will return not the actual CheckBox.
    * @param browser
    *           the browser that will be used to access the check box
    */
   public CheckBox(final String locator, final Browser browser)
   {
      super(locator, browser);
   }

   /**
    * Clear (uncheck) the check box.
    * 
    * @return <li><code>true</code> if clear was successful.</li> <li>
    *         <code>false</code> if the check box was not found, or clear was
    *         unsuccessful.</li>
    */
   public boolean clear()
   {
      return this.getBrowser().clearCheckBox(this.getLocator());
   }

   /**
    * Select (Check) a check box. The method will first check to make sure the
    * location is visible and editable.
    * 
    * @return <li><code>true</code> if select was successful.</li> <li>
    *         <code>false</code> if select was unsuccessful.</li>
    */
   public boolean select()
   {
      return this.getBrowser().selectCheckBox(this.getLocator());
   }

   /**
    * Select (Check) a check box by clicking the text to the right of the
    * checkbox.
    * 
    * @return <li><code>true</code> if select was successful.</li> <li>
    *         <code>false</code> if select was unsuccessful.</li>
    */
   public boolean selectByLabel()
   {
      return this.getBrowser().click(this.getLocator());
   }

   /**
    * Validates if the check box is selected (checked) or not.
    * 
    * @return <li><code>True</code> if the value is checked / selected.</li> <li>
    *         <code>False</code> if the value is not checked / selected</li>
    */
   public boolean isSelected()
   {
      return this.getBrowser().isSelected(this.getLocator());
   }

   /**
    * Overwritten as CheckBox locator based in is not actually the element that
    * has the disabled attribute
    * 
    * @return <li><code>True</code> if the value is disabled.</li> <li>
    *         <code>False</code> if the value is not disabled</li>
    */
   public boolean isDisabled()
   {
      return this.getBrowser().isDisabled(this.getLocator());
   }
}
