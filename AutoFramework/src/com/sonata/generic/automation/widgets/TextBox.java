/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.widgets;

import org.sikuli.script.FindFailed;
import org.sikuli.script.Region;
import org.sikuli.script.Screen;

import com.sonata.generic.automation.browser.Browser;

/**
 * The <code>TextBox</code> class represents a text box
 */
public class TextBox extends Widget
{
   /**
    * constructs an instance of the {@link TextBox} class.
    * 
    * @param locator
    *           the name of the text box as it appears in the DOM on the browser
    * @param browser
    *           the browser that will be used to access the text box
    */
   public TextBox(final String locator, final Browser browser)
   {
      super(locator, browser);
   }
   /**
    * Verify if a TextBox is editable or not.
    * 
    * @return <li><code>true</code>if the TextBox is visible and not disabled</li>
    *         <li><code>false</code>otherwise</li>
    */
   public boolean isEditable()
   {
      return this.getBrowser().isVisible(this.getLocator()) && !(this.getBrowser().isDisabled(this.getLocator()));
   }

   /**
    * Verify if a TextBox is visible but disabled.
    * 
    * @return <li><code>true</code>if the widget is visible but disabled</li>
    *         <li><code>false</code>otherwise</li>
    */
   public boolean isDisabled()
   {
      return this.getBrowser().isVisible(this.getLocator()) && (this.getBrowser().isDisabled(this.getLocator()));
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
    * clicks into the TextBox to place the cursor inside
    * 
    * @return <li><code>true</code> if the TextBox is clicked successfully</li>
    *         <li><code>false</code> if the TextBox could not be located.</li>
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
    * first before typing in the new value. No tab after the input text
    * 
    * @param value
    *           The string to be typed in the field.
    * @return <li><code>true</code> if the field was typed in, and the text was
    *         verified in the field.</li> <li><code>false</code> if the field
    *         was not found, or the field did not contain what was typed.</li>
    */
   public boolean typeNoTab(final String value)
   {
      return this.type(value, false);
   }
   
   /**
    * Type a string value in a text box. This function will clear the text box
    * first before typing in the new value.
    * Used when Type() not working for clear() lose focus
    * 
    * @param value
    *           The string to be typed in the field.
    * @return <li><code>true</code> if the field was typed in, and the text was
    *         verified in the field.</li> <li><code>false</code> if the field
    *         was not found, or the field did not contain what was typed.</li>
    */
   public boolean typeWithCtrlADel(final String value)
   {
      return this.typeWithCtrlADel(value, true);
   }
   
   /**
    * Types the specified value into this widget. This action will clear any
    * existing value from this widget before typing in the new value.
    * Used for some special textbox for which type() not working because wait
    * for elementToBeClickable is not working, so bypass this wait condition
    * e.g.: the "page" text box for a table.
    * 
    * @param value
    *           The value to type into this widget.
    * 
    * @return Whether or not the value was successfully typed into this widget.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   public boolean typeWithoutWaitForClicable(final String value)
   {
      return this.typeWithoutWaitForClicable(value, true);
   }
   
   /**
    * Type a string value in a text box in a cell. This function will clear the 
    * text box first before typing in the new value.
    * 
    * @param value
    *           The string to be typed in the field.
    * @return <li><code>true</code> if the field was typed in, and the text was
    *         verified in the field.</li> <li><code>false</code> if the field
    *         was not found, or the field did not contain what was typed.</li>
    */
   public boolean typeIntoCell(final String value)
   {
      return this.typeIntoCell(value, true);
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
    * Type a string value in a text box. This function will clear the text box
    * first before typing in the new value.
    * 
    * @param value
    *           The string to be typed in the field.
    * 
    * @return <li><code>true</code> if the field was typed in, and the text was
    *         verified in the field.</li> <li><code>false</code> if the field
    *         was not found, or the field did not contain what was typed.</li>
    */
   public boolean typeByJavaScript(final String value)
   {
      return this.getBrowser().typeByJavaScript(this.getLocator(), value);
   }
   
   /**
    * Types the specified value into this widget. This action will clear any
    * existing value from this widget before typing in the new value.
    * Used for some special textbox for which type() not working because wait
    * for elementToBeClickable is not working, so bypass this wait condition
    * e.g.: the "page" text box for a table.
    * 
    * @param value
    *           The value to type into this widget.
    * @param tab
    *           determination to do a tab or not
    * 
    * @return Whether or not the value was successfully typed into this widget.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   public boolean typeWithoutWaitForClicable(final String value, final boolean tab)
   {
      return this.getBrowser().typeWithoutWaitForClicable(this.getLocator(), value, tab);
   }
   
   /**
    * Type a string value in a text box in a cell. This function will clear the 
    * text box first before typing in the new value.
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
   public boolean typeIntoCell(final String value, final boolean tab)
   {
      return this.getBrowser().typeIntoCell(this.getLocator(), value, tab);
   }
   
   /**
    * Type a string value in a text box. This function will clear the text box
    * first before typing in the new value.
    * Used when Type() not working for clear() lose focus
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
   public boolean typeWithCtrlADel(final String value, final boolean tab)
   {
      return this.getBrowser().typeWithCtrlADel(this.getLocator(), value, tab);
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
    * returns a {@link Button} representing the calendar icon, which can then be
    * clicked.
    * 
    * @return a {@link Button} representing the calendar icon.
    */
   public Button getCalendarButton()
   {
      // based on the locator get the value of the attribute aria-controls for the same element
      String calendarAlias = this.getBrowser().getAttribute(this.getLocator(), "aria-owns");
      
      if (calendarAlias == null)
         return null;
      
      // then use that value as the id of the <div> element that contains the aria-expanded (true or false) attribute 
      String calendarButtonLocator = "//span[@aria-controls='" + calendarAlias + "']";  
      
      if (!this.getBrowser().exists(calendarButtonLocator))
         return null;
      
      return new Button(calendarButtonLocator, this.getBrowser());
   }

   /**
    * returns a {@link Calendar} representing the Calendar Widget.
    * 
    * @return a {@link Calendar} representing the Calendar Widget.
    */
   public Calendar getCalendarWidget()
   {
      // based on the locator get the value of the attribute aria-controls for the same element
      String calendarAlias = this.getBrowser().getAttribute(this.getLocator(), "aria-owns");
      
      if (calendarAlias == null)
         return null;
      
      // then use that value as the id of the <div> element that contains the aria-expanded (true or false) attribute 
      String calendarWidgetLocator = "//div[@id='" + calendarAlias + "']";      
      
      return new Calendar(calendarWidgetLocator, this.getBrowser());
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
    * Retrieves placeholder text from a specified text box. 
    * E.g.: The original value (000000) in batch number  
    * 
    * @return the placeholder text from the specified text box, or null if the text box
    *         cannot be located.
    */
   public String getPlaceHolderText()
   {
      return this.getBrowser().getPlaceHolderText(this.getLocator());
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
      return this.getBrowser().waitForContent(this.getLocator(), content);
   }
   
   /**
    * ==============================================================
    * Sikuli related actions
    * 
    */
   
   /**
    * Types into an image, using the Sikuli tool.
    * 
    * @param value
    *    The value to type
    *    
    * @return boolean whether action was success or not.
    */
   public boolean typeIntoImage(String value)
   {
      Screen screen = new Screen();

      try {
         String image = this.getLocator();
         Region textBox = screen.wait(image, 60).right(20).highlight(5);
         textBox.click();
         textBox.type(value);
         
         return true;

      } catch (FindFailed e) {
         e.printStackTrace();
         return false;
      }
   }      
}
