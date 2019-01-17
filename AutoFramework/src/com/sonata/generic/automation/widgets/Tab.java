/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.widgets;

import com.sonata.generic.automation.browser.Browser;

/**
 * the <code>Tab</code> class represents a Tab
 * 
 */
public class Tab extends Widget
{
   /**
    * constructs an instance of the {@link Tab} class.
    * 
    * @param locator
    *           the tab label locator of the Tab as it appears in the DOM on the
    *           browser
    * @param browser
    *           the browser that will be used to access the Tab
    */
   public Tab(final String locator, final Browser browser)
   {
      super(locator, browser);
   }

   /**
    * Selects a Tab.
    * 
    * @return <li><code>True</code> if Tab was selected</li> <li>
    *         <code>False</code> otherwise</li>
    */
   public final boolean select()
   {
      if (!(this.getBrowser().selectTab(this.getLocator())))
            return false;
                  
      return waitUntilSelected();             

   }
   
   /**
    * Verify if a Tab is visible but disabled.
    * 
    * @return <li><code>true</code>if the widget is visible but disabled</li>
    *         <li><code>false</code>otherwise</li>
    */
   public boolean isDisabled()
   {
      return (this.getBrowser().isDisabledForTab(this.getLocator()));
   }

   /**
    * Verifies if a Tab is selected.
    * 
    * @return <li><code>True </code>if Tab is selected</li> <li>
    *         <code>False </code>otherwise</li>
    */
   public final boolean isSelected()
   {
      // based on the locator get the value of the attribute aria-controls for the same element
      String tabAlias = this.getBrowser().getAttribute(this.getLocator(), "aria-controls");
      
      // then use that value as the id of the <div> element that contains the aria-expanded (true or false) attribute 
      String tabExpanded = this.getBrowser().getAttribute(tabAlias, "aria-expanded");
      
      // use that to determine if the tab is selected
      return (tabExpanded.equals("true") ? true : false);
   }
   
   /**
    * Wait until a widget becomes enabled before the timeout is reached.
    * 
    * @return <li><code>true</code> if widget becomes disabled</li> <li>
    *         <code>false</code> otherwise</li>
    */
   private boolean waitUntilSelected()
   {
      for (int index = 0; index < this.getBrowser().getBrowserSettings().getDefaultTimeOut(); index++)
      {
         if (isSelected())
         {
            return true;
         }
      }
      return false;
   }   
}
