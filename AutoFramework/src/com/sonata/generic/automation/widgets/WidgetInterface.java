/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.widgets;

import com.sonata.generic.automation.browser.Browser;

/**
 * The <code>WidgetInterface</code> interface contains methods for interacting
 * with the Widgets.
 */
public interface WidgetInterface
{

   /**
    * public method to get locator
    * 
    * @return locator the locator of the class.
    */
   String getLocator();

   /**
    * public method to get browser
    * 
    * @return browser the browser of the class.
    */
   Browser getBrowser();

   /**
    * Checks to see if a widget is visible.
    * 
    * @return <li><code>true</code> if the widget was visible.</li> <li>
    *         <code>false</code> if the widget was not visible.</li>
    */
   boolean isVisible();

   /**
    * Checks to see if a widget exists in DOM.
    * 
    * @return <li><code>true</code> if the widget exists.</li> <li>
    *         <code>false</code> if the widget does not exists.</li>
    */
   boolean exists();

   /**
    * Verify if a widget is disabled or not.
    * 
    * @return <li><code>true</code> if widget is disabled</li> <li>
    *         <code>false</code> otherwise</li>
    */
   boolean isDisabled();

   /**
    * Verify if a widget is editable or not.
    * 
    * This method should be different for combo box, will need to override in
    * the subclass.
    * 
    * This function also tests if the element is visible, if not, it should not
    * be editable to begin with.
    * 
    * @return <li><code>true</code> if the widget is editable</li> <li>
    *         <code>false</code> otherwise</li>
    */
   boolean isEditable();

   /**
    * Waits for an element to exist in DOM
    * 
    * @return <li><code>true</code> if the item was found within the timeout.</li>
    *         <li><code>false</code> if the timeout was reached.</li>
    */
   boolean waitForElement();

   /**
    * Waits for an element, that is in a different window, to exist in DOM
    * 
    * @param window
    *           the window name where the element to wait for resides
    * 
    * @return <li><code>true</code> if the item was found within the timeout.</li>
    *         <li><code>false</code> if the timeout was reached.</li>
    */
   boolean waitForElementInDifferentWindow(final String window);

   /**
    * Clears the contents of this widget (or in the case of a widget such as
    * check box or radio button, deselects it).
    * 
    * @return Whether or not this widget's contents were successfully cleared.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean clear();

   /**
    * Clears the contents of this widget and validates it contains the specified
    * default value after clearing.
    * 
    * @param defaultValue
    *           The default value this widget should contain after clearing its
    *           contents.
    * 
    * @return Whether or not this widget's contents were successfully cleared
    *         and this widget contained the default value after clearing.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean clearAndValidate(String defaultValue);

   /**
    * Clicks the widget.
    * 
    * @return Whether or not the value was successfully clicked the widget.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean click();

   /**
    * Closes the Currently opened window.
    * 
    * @return <li><code>true</code>if the window was closed.</li> <li>
    *         <code>false</code> if the window was not closed.</li>
    */
   boolean closeCurrentWindow();

   /**
    * Gets the options that belong to this widget (for example, the values in a
    * list box).
    * 
    * @return The options that belong to this widget (for example, the values in
    *         a list box).
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   String[] getAllOptions();
   
   /**
    * Gets the selected options from a listBox which may support multi-selections.
    * 
    * @return The options that belong to a listBox widget 
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   String[] getSelectedOptions();

   /**
    * Gets the text of this widget, or returns null if this widget cannot be
    * located or is invisible.
    * 
    * @return The text of this widget, or null if this widget cannot be located
    *         or is invisible.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   String getText();

   /**
    * Checks whether or not this widget is currently in a selected state (for
    * example, whether or not a check box is checked).
    * 
    * @return Whether or not this widget is currently in a selected state.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean isSelected();

   /**
    * Sets this widget's state to being selected (for example, by checking a
    * check box).
    * 
    * @return Whether or not this widget's state was set to being selected.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean select();
   
   /**
    * Sets this widget's state to being selected (for example, by checking a
    * check box) by clicking on the checkbox label / text.
    * 
    * @return Whether or not this widget's state was set to being selected.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean selectByLabel();

   /**
    * Selects the specified option on this widget (for example, selecting one of
    * the values in a list box).
    * 
    * @param optionValue
    *           The value of the option to select.
    * 
    * @return Whether or not the specified option was successfully selected on
    *         this widget.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean selectComboBox(String optionValue, boolean textValidation);
   
   /**
    * deSelects the specified option on this widget (for example, deselecting one of
    * the values in a list box).
    * 
    * @param optionValue
    *           The value of the option to deselect.
    * 
    * @return Whether or not the specified option was successfully deselected on
    *         this widget.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean deselectFromListBox(String optionValue);
   
   /**
    * deSelects all options on this listBox widget 
    * 
    * @return Whether or not all the options were successfully deselected 
    *         on this widget.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean deselectAllFromListBox();
   
   /**
    * Selects all the options on this listBox widget 
    * 
    * @return Whether or not all the options were successfully selected 
    *         on this widget.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean selectAllFromListBox();
   

   /**
    * Types the specified value into this widget. This action will clear any
    * existing value from this widget before typing in the new value.
    * 
    * @param value
    *           The value to type into this widget.
    * 
    * @return Whether or not the value was successfully typed into this widget.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean type(String value);
   
   /**
    * Types the specified value into the specified widget. This action will
    * clear any existing value from the widget before typing in the new value.
    * Telephone number is specially treated with format like (123)-456-7890,
    * the text validation need follow this final format shown on the screen.
    * 
    * @param value
    *           The value to type into this widget.
    * 
    * @return Whether or not the value was successfully typed into this widget.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean typeTelephoneNumber(String value);
   
   
   /**
    * Types the specified value into this widget. This action will clear any
    * existing value from this widget before typing in the new value.
    * 
    * @param value
    *           The value to type into this widget.
    * 
    * @return Whether or not the value was successfully typed into this widget.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean typeByJavaScript(String value);
   
   /**
    * Types the specified value into this widget. This action will clear any
    * existing value from this widget before typing in the new value. No Tab
    * after sendText
    * 
    * @param value
    *           The value to type into this widget.
    * 
    * @return Whether or not the value was successfully typed into this widget.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean typeNoTab(String value);
   
   /**
    * Types the specified value into this widget. This action will clear any
    * existing value from this widget before typing in the new value.
    * Used when type() not working for clear() lose focus
    * 
    * @param value
    *           The value to type into this widget.
    * 
    * @return Whether or not the value was successfully typed into this widget.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean typeWithCtrlADel(final String value);
   
   /**
    * Types the specified value into a cell in a table. This action will clear any
    * existing value from this widget before typing in the new value.
    * 
    * @param value
    *           The value to type into this widget.
    * 
    * @return Whether or not the value was successfully typed into this widget.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean typeIntoCellByColumnIdx(String value);
     
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
   boolean typeWithoutWaitForClicable(String value);

   /**
    * Types the specified value into this widget. This action will clear any
    * existing value from this widget before typing in the new value.
    * 
    * @param value
    *           The value to type into this widget.
    * @param tab
    *           <code>true</code> to indicate to do a tab <code>false</code> to
    *           indicate not to do a tab
    * 
    * @return Whether or not the value was successfully typed into this widget.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean type(final String value, final boolean tab);
   
   /**
    * Types the specified value into this widget. This action will clear any
    * existing value from this widget before typing in the new value.
    * Used when type() not working for clear() lose focus
    * 
    * @param value
    *           The value to type into this widget.
    * @param tab
    *           <code>true</code> to indicate to do a tab <code>false</code> to
    *           indicate not to do a tab
    * 
    * @return Whether or not the value was successfully typed into this widget.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean typeWithCtrlADel(final String value, final boolean tab);

   /**
    * Types the specified value into a cell in a table. This action will clear any
    * existing value from this widget before typing in the new value.
    * 
    * @param value
    *           The value to type into this widget.
    * @param tab
    *           <code>true</code> to indicate to do a tab <code>false</code> to
    *           indicate not to do a tab
    * 
    * @return Whether or not the value was successfully typed into this widget.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean typeIntoCell(final String value, final boolean tab);
   
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
    *           <code>true</code> to indicate to do a tab <code>false</code> to
    *           indicate not to do a tab
    * @return Whether or not the value was successfully typed into this widget.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean typeWithoutWaitForClicable(String value, final boolean tab);
   
   /**
    * Types the specified value into this widget. This action WON'T clear the
    * existing value from this widget before typing in the new value. If
    * {@code doTab} is {@code true}, it will also "press" the Tab key after
    * typing in the value.
    * 
    * @param value
    *           The value to type into this widget.
    * 
    * @return Whether or not the value was successfully typed into this widget.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean typeWithoutClear(String value);

   /**
    * Types the specified value into this widget. This action WON'T clear the
    * existing value from this widget before typing in the new value. If
    * {@code doTab} is {@code true}, it will also "press" the Tab key after
    * typing in the value.
    * 
    * @param value
    *           The value to type into this widget.
    * @param doTab
    *           Whether or not to "press" the Tab key after typing in the value.
    * 
    * @return Whether or not the value was successfully typed into this widget.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean typeWithoutClear(String value, boolean doTab);

   /**
    * Returns the string which was selected in a comboBox.
    * 
    * @return the value of that comboBox selected. if combobox cannot be
    *         located, or if there are no values, the return value is
    *         <code>null</code>.
    * @throws IllegalArgumentException
    */
   String getSelectedValue();

   /**
    * Wait for the specified content to be displayed in this Widget.
    * 
    * @param content
    *           the expected content to be displayed by the widget
    * @return <li><code>true</code> if the content was displayed in the widget
    *         within the timeout.</li> <li><code>false</code> if the timeout was
    *         reached.</li>
    */
   boolean waitForContent(String content);

}
