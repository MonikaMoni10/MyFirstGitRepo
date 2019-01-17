/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.widgets;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sonata.generic.automation.browser.Browser;

/**
 * The <code>ComboBox</code> class represents a ComboBox VersionOne [B-12249]
 */
public class ComboBox extends Widget
{
   private String comboboxLocator = "";
   private String selectLocator = "";
   /**
    * constructs an instance of the {@link ComboBox} class.
    * 
    * @param comboBoxLocator
    *           the comboBox id as it appears in the browser
    * @param browser
    *           the browser that will be used to access the comboBox
    */
   public ComboBox(final String locator, final Browser browser)
   {
      super(locator, browser);
      selectLocator = locator;
            
      // Determine if we're dealing with a combobox that is not in a table,
      // if so then adjust the locator to an XPATH as it is used widely throughout
      // If it is part of a table then the locator is already an XPATH
      /*if (!(this.getLocator().contains("/table/")))
         comboboxLocator = "//select[@id='" + this.getLocator() + "']/..";
      else
         comboboxLocator = locator + "/..";
      */
      if (!(this.getLocator().contains("/table/")))
          comboboxLocator = this.getLocator();
       else
          comboboxLocator = locator;
   }

   /**
    * Selects an option from a combobox.
    * 
    * @param optionValue
    *           The option value to be selected
    * @param textValidation
    *           Whether to verify if the option is correctly selected          
    * @return <li><code>true</code> if the the selection was successful.</li>
    *         <li><code>false</code> if the selection was not successful.</li>
    */
   @SuppressWarnings("finally")
   public boolean selectComboBox(final String optionValue, boolean textValidation)
   {
      // Most of the ComboBox using <select> tag, but in some special screen like "I/C Recorder Report",
      // ComboBox like "From" and "To" use dynamically generated tag <input>
      if( ! this.getBrowser().exists(comboboxLocator))
      {
         return selectComboBoxWithInputTag(optionValue, textValidation);
      }
      
      // based on the locator get the value of the attribute aria-controls for the same element
      String listAlias = this.getBrowser().getAttribute(comboboxLocator, "aria-owns"); 
      
      // Click the comboBox to extend it for selection
      // this.getBrowser().click(comboboxLocator); not working with firefox for a comboBox in a table(selectFromCell) 
      this.getBrowser().clickByJavaScript(comboboxLocator);

      // getAttribute(optionLocator, "innerHTML") returns empty string for the first option listed in <li>
      // Change the way of getting all the options with Selenium Select 
      // String optionLocator = "//ul[@id='" + listAlias + "']/li[" + index +"]";
      // String optionValue = this.getBrowser().getAttribute(optionLocator, "innerHTML"); 
      String [] options = this.getBrowser().getAllOptions(selectLocator);
      
      
      
      
      
      // go through all the options to locate the exact one
      for (int i=0; i < options.length; i ++)
      {
         if(options[i].equals(optionValue))
         {
            //String javascript = "$(\"div.k-animation-container > div > ul#" + listAlias + " > li:eq(" + i +")\").click();";
           	
        	 
        	// String javascript = "$(\'"+ comboboxLocator+"').find('option:contains("+ optionValue +")\').attr(\"selected\",selected)";
        	 
        	 String javascript = "$(\'select').find('option:contains("+ optionValue +")\').attr(\"selected\",true)";
        	 
        	 
        	 
        	// String javascript =  "$(\"select option[value="+"TRA" + "]\").attr(\"selected\", true);";
            this.getBrowser().executeJavaScriptReturnString(javascript);
            
            // Wait for 1s to make it ready for next select
            try
            {
               Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {
               return false;
            }
            
            // Verify the given option is selected
            if(textValidation)
            {
               String a = this.getSelectedValue();           
               return (a.equals(optionValue)) ? true: false;
            }
            return true;
         }
      }
      
      // If an option is not found, click the comboBox to wrap it up.
      // Wait for 1s to make it ready for next select
      this.getBrowser().click(comboboxLocator);
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
   
   /**
    * Selects an option from a combobox with dynamically generated <input> tag.
    * 
    * @param optionValue
    *           The option value to be selected
    * @param textValidation
    *           Whether to verify if the option is correctly selected          
    * @return <li><code>true</code> if the the selection was successful.</li>
    *         <li><code>false</code> if the selection was not successful.</li>
    */
   @SuppressWarnings("finally")
   private boolean selectComboBoxWithInputTag(final String optionValue, boolean textValidation)
   {
//      String comboboxLocator = "//input[@id='" + this.getLocator() + "']/..";
	      String comboboxLocator = this.getLocator();
      
      // based on the locator get the value of the attribute aria-controls for the same element
      String listAlias = this.getBrowser().getAttribute(comboboxLocator, "aria-owns"); 
      
      // Click the comboBox to extend it for selection
      // this.getBrowser().click(comboboxLocator); not working with firefox for a comboBox in a table(selectFromCell) 
      this.getBrowser().clickByJavaScript(comboboxLocator);

      int index = 1;
      String optionLocator = "//ul[@id='" + listAlias + "']/li[" + index +"]";
            
      while(this.getBrowser().existsNoWait(optionLocator))
      {
         if(optionValue.equals(this.getBrowser().getAttribute(optionLocator, "innerHTML")))
         {
            int i = index -1;
            String javascript = "$(\"div.k-animation-container > div > ul#" + listAlias + " > li:eq(" + i +")\").click();";
            this.getBrowser().executeJavaScriptReturnString(javascript);
            
            // Wait for 1s to make it ready for next select
            try
            {
               Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {
               return false;
            }
           
            // Verify the given option is selected
            if(textValidation)
            {
               // Not use this.getSelectedValue() because in which comboboxLocator is the global one used 
               // for <select> type of comboxBox
               String selectedText = null;   
               try
               {
                  selectedText = this.getBrowser().getText(comboboxLocator);
               }
               catch (IllegalArgumentException e)
               {
                  throw new IllegalArgumentException("cannot locate comboBox");
               }
               return (selectedText.equals(optionValue)) ? true: false;
            }
            return true;
         }
         index++;
         optionLocator = "//ul[@id='" + listAlias + "']/li[" + index +"]";
      }     
      
      // If an option is not found, click the comboBox to wrap it up.
      // Wait for 1s to make it ready for next select
      this.getBrowser().click(comboboxLocator);
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
   
   /**
    * Selects an option from a combobox by index.
    * 
    * @param index
    *           The index of an option to be selected
    * @return <li><code>true</code> if the the selection was successful.</li>
    *         <li><code>false</code> if the selection was not successful.</li>
    */
   public boolean selectComboBoxByIdx(final int index )
   {
      // based on the locator get the value of the attribute aria-controls for the same element
      String listAlias = this.getBrowser().getAttribute(comboboxLocator, "aria-owns");      
     
      // Get the option value for later validation
      // getAttribute(optionLocator, "innerHTML") returns empty string for the first option listed in <li>
      // Change the way of getting all the options with Selenium Select 
      // String optionLocator = "//ul[@id='" + listAlias + "']/li[" + index +"]";
      // String optionValue = this.getBrowser().getAttribute(optionLocator, "innerHTML"); 
      String [] options = this.getBrowser().getAllOptions(selectLocator);     
      if(index < 1 || index > options.length)
         return false;
      String optionValue = options[index-1];
      
      // Click the comboBox to extend it for selection
      this.getBrowser().click(comboboxLocator);
      
      // this.getBrowser().click(optionLocator); //can't locate the element    
      // Use jquery to help select the option.
      int realIdx = index -1; 
      //String javascript = "$(\"div.k-animation-container > div > ul#" + listAlias + " > li:eq(" + realIdx +")\").click();";
      String javascript = "$(\"select option[value="+"TRA" + "]\").attr(\"selected\", true);";
      this.getBrowser().executeJavaScriptReturnString(javascript);
      
      // Cannot do the normal check if the correct value was selected for a combobox in the table since focus is lost
      // Handle this at the FixtureTable level
      if (!(this.getLocator().contains("/table/")))
      {
         // Check that the Java script worked by selecting the expected value      
         String a = this.getSelectedValue();
         return (a.equals(optionValue)) ? true: false;
      }
      return true;
   }
   
   /**
    * Verify if a ComboBox is visible or not.
    * 
    * @return <li><code>true</code>if the ComboBox is visible </li>
    *         <li>
    *         <code>false</code>otherwise</li>
    */
   public boolean isVisible()
   {
      ///if use AbstractBrowser->isVisible(locator)
      ///locator like id='Data_LocationList' is used
      ///locator represent the <select> which has "display: none"
      ///comboboxLocator is like //select[@id='Data_LocationList']/..
      ///comboboxLocator represent a span which contain the selected option
      return this.getBrowser().isVisible(comboboxLocator);

   }

   /**
    * Verify if a ComboBox is visible and editable or not.
    * 
    * @return <li><code>true</code>if the ComboBox is visible and not disabled</li>
    *         <li><code>false</code>otherwise</li>
    */
   public boolean isEditable()
   {
      if ( !(this.getBrowser().isVisible(comboboxLocator)) )
         return false;
      String ariaDisabled= this.getBrowser().getAttribute(comboboxLocator, "aria-disabled");
      return (ariaDisabled.trim().equals("false")) ? true: false;
   }
   
   /**
    * Verify if a ComboBox is visible but disabled or not.
    * 
    * @return <li><code>true</code>if the ComboBox is visible but disabled</li>
    *         <li><code>false</code>otherwise</li>
    */
   public boolean isDisabled()
   {
      if ( !(this.getBrowser().isVisible(comboboxLocator)) )
         return false;
      String ariaDisabled= this.getBrowser().getAttribute(comboboxLocator, "aria-disabled");
      return (ariaDisabled.trim().equals("false")) ? false: true;   
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
         return this.getBrowser().getAllOptions(this.getLocator());
      }
      catch (Exception e)
      {
         throw new IllegalArgumentException("Could not get the options for widget", e);
      }

   }

   /**
    * Returns the string which was selected in a comboBox.
    * @return the value of that comboBox selected. if combobox cannot be
    *         located, or if there are no values, the return value is
    *         <code>null</code>.
    * @throws IllegalArgumentException
    */
   public String getSelectedValue()
   {
      try
      {
         return this.getBrowser().getText(comboboxLocator);
      }
      catch (IllegalArgumentException e)
      {
         throw new IllegalArgumentException("cannot locate comboBox");
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
      return this.getBrowser().waitForContent(comboboxLocator, content);
   }
}
