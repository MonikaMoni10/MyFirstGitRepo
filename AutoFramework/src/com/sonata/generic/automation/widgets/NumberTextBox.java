/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.widgets;

import com.sonata.generic.automation.browser.Browser;
import com.sonata.generic.automation.browser.SpecialKey;

/**
 * The <code>NumberTextBox</code> class represents a Number TextBox.
 */
public class NumberTextBox extends Widget
{
   /**
    * constructs an instance of the {@link NumberTextBox} class.
    * 
    * @param locator
    *           the locator of the text box
    * @param browser
    *           the browser that will be used to access the control
    */
   public NumberTextBox(final String locator, final Browser browser)
   {
      super(locator, browser);
   }

   /**
    * Clear the content in a text box.
    * 
    * @return <li><code>true</code> if the text box was cleared.</li> <li>
    *         <code>false</code> if the text box was not found, or the box
    *         failed to be cleared.</li>
    */
   public boolean clear()
   {
      return this.getBrowser().clearText(this.getLocator());
   }

   /**
    * Clears a text box and validates its default value after clearing.
    * 
    * @param defaultValue
    *           the default value of the text box after clearing.
    * @return <li><code>True </code>if Cleared and Validated</li> <li>
    *         <code>False</code> otherwise</li>
    */
   public boolean clearAndValidate(final String defaultValue)
   {
      return this.getBrowser().clearAndValidateField(this.getLocator(), defaultValue);
   }

   /**
    * Clicks into the numeric text box to place the cursor inside and to activate
    * the actual <input> element that needs to be interacted with.
    * 
    * @return <li><code>true</code> if the text box is clicked successfully</li>
    *         <li><code>false</code> if the text box could not be located.</li>
    */
   public boolean click()
   {
      // Need to click the element above the that input with the actual id because
      // initially the element with the id is disabled
      return this.getBrowser().click("//input[@id='" + this.getLocator() + "']/..", false);
   }

   /**
    * Type a string value in a numeric text box. This function will actually 
    * not clear the number text box otherwise we receive error.
    * 
    * @param value
    *           The string to be typed in the field.
    * @return <li><code>true</code> if the field was typed in, and the text was
    *         verified in the field.</li> <li><code>false</code> if the field
    *         was not found, or the field did not contain what was typed.</li>
    */
   public boolean type(final String value)
   {
      return this.type(value, true);
   }
   
   /**
    * Types the specified value into the specified widget. This action will
    * clear any existing value from the widget before typing in the new value.
    * Telephone number is specially treated with format like (123)-456-7890,
    * the text validation need follow this final format shown on the screen.
    * 
    * @param value
    *           The string to be typed in the field.
    * @return <li><code>true</code> if the field was typed in, and the text was
    *         verified in the field.</li> <li><code>false</code> if the field
    *         was not found, or the field did not contain what was typed.</li>
    */
   public boolean typeTelephoneNumber(final String value)
   {
      return this.typeTelephoneNumber(value, true);
   }

   /**
    * Type a string value in a numeric text box. This function will actually 
    * not clear the number text box otherwise we receive error.
    * 
    * @param value
    *           The string to be typed in the field.
    * @param tab
    *           determination to do a tab or not
    * 
    * @return <li><code>true</code> if the field was typed in, and the text was
    *         verified in the field.</li> <li><code>false</code> if the field
    *         was not found, or the field did not contain what was typed.</li>
    */
   public boolean type(final String value, final boolean tab)
   {
      // Need to click the element to activated it.  
      // For now we must also type without clearing first otherwise an error occurs
      // regarding the element not being visible enough to use clear.
      
      this.click();
      this.getBrowser().clearByCtrlADelete(this.getLocator());
      
      return this.typeWithoutClear(value);
   }
   
   /**
    * Type a string value in a numeric text box. This function will actually 
    * not clear the number text box otherwise we receive error.
    * Telephone number is specially treated with format like (123)-456-7890,
    * the text validation need follow this final format shown on the screen.
    * 
    * @param value
    *           The string to be typed in the field.
    * @param tab
    *           determination to do a tab or not
    * 
    * @return <li><code>true</code> if the field was typed in, and the text was
    *         verified in the field.</li> <li><code>false</code> if the field
    *         was not found, or the field did not contain what was typed.</li>
    */
   public boolean typeTelephoneNumber(final String value, final boolean tab)
   {
      // Need to click the element to activated it.  
      // For telephone formated element, "Ctrl+A" does not work for selecting the original text.
      
      this.click();
      this.clear();
      
      return this.typeTelNumWithoutClear(value);
   }

   /**
    * Type a string value in a numeric text box. This function will NOT clear the text
    * box before typing in the new value. 
    * 
    * It will press "tab" key after it's finished.
    * 
    * @param value
    *           What is to be typed in the field.
    * @return <li><code>true</code> if the field was typed in, and the text was
    *         verified in the field.</li> <li><code>false</code> if the field
    *         was not found, or the field did not contain what was typed.</li>
    */
   public boolean typeWithoutClear(final String value)
   {
      return this.typeWithoutClear(value, true);
   }
   
   /**
    * Type a string value in a numeric text box. This function will NOT clear the text
    * box before typing in the new value. 
    * Telephone number is specially treated with format like (123)-456-7890,
    * the text validation need follow this final format shown on the screen.
    * 
    * It will press "tab" key after it's finished.
    * 
    * @param value
    *           What is to be typed in the field.
    * @return <li><code>true</code> if the field was typed in, and the text was
    *         verified in the field.</li> <li><code>false</code> if the field
    *         was not found, or the field did not contain what was typed.</li>
    */
   public boolean typeTelNumWithoutClear(final String value)
   {
      return this.typeTelNumWithoutClear(value, true);
   }

   /**
    * Type a string value in a numeric text box. This function will NOT clear the text
    * box before typing in the new value. 
    * 
    * The press of the "tab" key after the input is an option.
    * 
    * @param value
    *           What is to be typed in the field.
    * @param doTab
    *           <li><code>true</code> to tab after inputting the text.</li> <li>
    *           <code>false</code> to not tab after inputting the text.</li>
    * @return <li><code>true</code> if the field was typed in, and the text was
    *         verified in the field.</li> <li><code>false</code> if the field
    *         was not found, or the field did not contain what was typed.</li>
    */
   public boolean typeWithoutClear(final String value, final boolean doTab)
   {      
      return this.getBrowser().typeWithoutClear(this.getLocator(), value, doTab);
   }
   
   /**
    * Type a string value in a numeric text box. This function will NOT clear the text
    * box before typing in the new value. 
    * Telephone number is specially treated with format like (123)-456-7890,
    * the text validation need follow this final format shown on the screen.
    * 
    * The press of the "tab" key after the input is an option.
    * 
    * @param value
    *           What is to be typed in the field.
    * @param doTab
    *           <li><code>true</code> to tab after inputting the text.</li> <li>
    *           <code>false</code> to not tab after inputting the text.</li>
    * @return <li><code>true</code> if the field was typed in, and the text was
    *         verified in the field.</li> <li><code>false</code> if the field
    *         was not found, or the field did not contain what was typed.</li>
    */
   public boolean typeTelNumWithoutClear(final String value, final boolean doTab)
   {      
      return this.getBrowser().typeTelNumWithoutClear(this.getLocator(), value, doTab);
   }

   /**
    * Retrieves text from a specified text box. The method will first check to
    * make sure the location is visible.
    * 
    * @return the text from the specified text box, or null if the text box
    *         cannot be located.
    */
   public String getText()
   {      
      return this.getBrowser().getText("//input[@id='" + this.getLocator() + "']/..");      
   }

   /**
    * 
    * @param content
    *           the expected content to wait for
    * 
    * @return <li><code>true</code> if the content was displayed</li> <li>
    *         <code>false</code> if the timeout was reached.</li>
    */
   public boolean waitForContent(final String content)
   {
      return this.getBrowser().waitForContent(this.getLocator(), content);
   }
}
