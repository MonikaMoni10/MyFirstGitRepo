/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.widgets;

import com.sonata.generic.automation.browser.Browser;

/**
 * The <code>PasswordTextBox</code> class represents a Password TextBox.
 */
public class PasswordTextBox extends Widget
{
   /**
    * constructs an instance of the {@link PasswordTextBox} class.
    * 
    * @param locator
    *           the locator of the text box
    * @param browser
    *           the browser that will be used to access the control
    */
   public PasswordTextBox(final String locator, final Browser browser)
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
    * clicks into the text box to place the cursor inside
    * 
    * @return <li><code>true</code> if the text box is clicked successfully</li>
    *         <li><code>false</code> if the text box could not be located.</li>
    */
   public boolean click()
   {
      return this.getBrowser().click(this.getLocator(), false);
   }

   /**
    * Type a string value in a text box. This function will clear the text box
    * first before typing in the new value.
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
    * Type a string value in a text box. This function will clear the text box
    * first before typing in the new value.
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
      return this.getBrowser().type(this.getLocator(), value, tab);
   }

   /**
    * Type a string value in a text box. This function will NOT clear the text
    * box before typing in the new value. This is used a lot in Q2O.
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
    * Type a string value in a text box. This function will NOT clear the text
    * box before typing in the new value. This is used a lot in Q2O.
    * 
    * It will press "tab" key after it's finished.
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
    * Retrieves text from a specified text box. The method will first check to
    * make sure the location is visible.
    * 
    * @return the text from the specified text box, or null if the text box
    *         cannot be located.
    */
   public String getText()
   {
      return this.getBrowser().getText(this.getLocator());
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
