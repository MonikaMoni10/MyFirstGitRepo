package com.sonata.generic.automation.widgets;

/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

import org.openqa.selenium.JavascriptExecutor;

import com.sonata.generic.automation.browser.Browser;

/**
 * The <code>ComboBoxFinder</code> class represents a ComboBox in a Finder Page for CNA2.0 [B-02907]
 */
public class ComboBoxFinder extends Widget
{
   private String ComboBoxDynamicLocator = "";
   /**
    * constructs an instance of the {@link ComboBoxFinder} class.
    * 
    * @param locator
    *           the ComboBoxFinder id as it appears in the browser
    * @param browser
    *           the browser that will be used to access the ComboBoxFinder
    */
   public ComboBoxFinder(final String locator, final Browser browser)
   {
      super(locator, browser);
      
      // Determine if we're dealing with a ComboBoxFinder that is not in a table,
      // if so then adjust the locator to an XPATH as it is used widely throughout
      // If it is part of a table then the locator is already an XPATH
      if (!(this.getLocator().contains("/table/")))
         ComboBoxDynamicLocator = "//select[@id='" + this.getLocator() + "']/..";
      else
         ComboBoxDynamicLocator = locator + "/..";
   }

   /**
    * Selects an option from a ComboBoxFinder.
    * 
    * @param optionValue
    *           The option value to be selected
    * @return <li><code>true</code> if the the selection was successful.</li>
    *         <li><code>false</code> if the selection was not successful.</li>
    */
   @SuppressWarnings("finally")
   public boolean selectComboBox(final String optionValue)
   {
      // based on the locator get the value of the attribute aria-controls for the same element
      String listAlias = this.getBrowser().getAttribute(ComboBoxDynamicLocator, "aria-owns");  

      // Click the comboBox by javascript to activate it.
      String comboBoxLocator = "//span[@aria-owns='" + listAlias + "']/span/span";     
      JavascriptExecutor js = (JavascriptExecutor) this.getBrowser().getDriver();
      String javascript = "return document.evaluate(\"" + comboBoxLocator + "\", document, null, 9, null).singleNodeValue.click();";
      js.executeScript(javascript);
      
      int idx = 0;
      String optionLocator = null;
      String option = "initialNonNull";
      
      while(option != null)
      {      
         if (option.trim().equals(optionValue))
         {
            JavascriptExecutor js1 = (JavascriptExecutor) this.getBrowser().getDriver();
            String javascript1 = "return document.evaluate(\"" + optionLocator + "\", document, null, 9, null).singleNodeValue.click();";
            js1.executeScript(javascript1);
            
            // wait for 1s to make it ready for next time select
            try
            {
               Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {
               return false;
            }
            
            if (!(this.getLocator().contains("/table/")))
            {
               // Check that the Java script worked by selecting the expected value
               String a = this.getSelectedValue();           
               return (a.equals(optionValue)) ? true: false;
            }
            
            return true;
         }
         
         // In a finder page, the two comboBox is dynamically generated with class='k-animation-container'
         // and the active comboBox always has "display: block" in its style attribute, while others with "dispaly:none" 
         idx++;
         optionLocator = "//div[@class='k-animation-container' and contains(@style,'display: block')]/div/ul[@id='" + listAlias + "']/li[" + idx +"]";
                 
         if(! this.getBrowser().existsNoWait(optionLocator))
         {
            js.executeScript(javascript);
            
            // If an option is not found, click the comboBox to wrap it up.
            // Wait for 1s to make it ready for next select
            try
            {
               Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {
            }
            finally
            {
               return false;
            }
         }
         
         option = this.getBrowser().getAttribute(optionLocator, "innerHTML");
      }
      
      return false;
   }
   
   /**
    * Selects an option from a ComboBoxFinder by index.
    * 
    * @param index
    *           The index of an option to be selected
    * @return <li><code>true</code> if the the selection was successful.</li>
    *         <li><code>false</code> if the selection was not successful.</li>
    */
   public boolean selectComboBoxByIdx(final int index )
   {
      // based on the locator get the value of the attribute aria-controls for the same element
      String listAlias = this.getBrowser().getAttribute(ComboBoxDynamicLocator, "aria-owns");     
      int realIdx = index -1; 
            
      this.getBrowser().executeJavaScriptReturnString("$(\"[aria-owns='" + listAlias + "'] > span > span\").click();");            
      this.getBrowser().executeJavaScriptReturnString("$(\"div.k-animation-container > div > ul#" + listAlias + " > li:eq(" + realIdx + ")\").click();");
       
      return true;
   }

   /**
    * Returns a string array, containing all the available options for a
    * ComboBoxFinder.
    * 
    * @return a String array containing all the available options. If the
    *         ComboBoxFinder cannot be located, or if there are no values, the return
    *         value is <code>null</code>.
    */
   public String[] getAllOptions()
   {
      try
      {
         return this.getBrowser().getAllOptions(this.getLocator());
      }
      catch (Exception e)
      {
         throw new IllegalArgumentException("Could not get the options for widget", e);
      }

   }

   /**
    * Verify if a widget is editable or not.
    * 
    * This method should be different for ComboBoxFinder, will need to override in the
    * subclass.
    * 
    * @return <li><code>true</code>if the widget is visible and not disabled</li>
    *         <li>
    *         <code>false</code>otherwise</li>
    */
   public boolean isEditable()
   {
      return this.getBrowser().isVisible(ComboBoxDynamicLocator) && !(this.getBrowser().isDisabled(ComboBoxDynamicLocator));
   }

   /**
    * Returns the string which was selected in a ComboBoxFinder.
    * 
    * @return the value of that ComboBoxFinder selected. if ComboBoxFinder cannot be
    *         located, or if there are no values, the return value is
    *         <code>null</code>.
    * @throws IllegalArgumentException
    */
   public String getSelectedValue()
   {
      try
      {
         return this.getBrowser().getText(ComboBoxDynamicLocator);
      }
      catch (IllegalArgumentException e)
      {
         throw new IllegalArgumentException("cannot locate ComboBoxFinder");
      }
   }

   /**
    * Waits for the specified content
    * 
    * @param content
    *           the expected content to wait for
    * 
    * @return <li><code>true</code> if the content was displayed</li> <li>
    *         <code>false</code> if the timeout was reached.</li>
    */
   public boolean waitForContent(final String content)
   {
      return this.getBrowser().waitForContent(ComboBoxDynamicLocator, content);
   }
}
