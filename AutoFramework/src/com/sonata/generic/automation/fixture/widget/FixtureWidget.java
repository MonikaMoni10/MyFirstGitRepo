/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.fixture.widget;

import java.util.Map;

/**
 * A FitNesse fixture friendly representation of a widget. This includes all
 * possible widget actions (each different kind of widget will support a subset
 * while throwing an exception on actions that aren't supported). It also
 * includes other properties such as a locator to use if this widget is acting
 * as the target of another fixture widget's "do an action and wait" call.
 */
public interface FixtureWidget
{
   /**
    * Gets the parent {@code FixtureWidget} of this fixture widget, or null if
    * this fixture widget is a top-level widget of the form it belongs to.
    * 
    * @return The parent {@code FixtureWidget} of this fixture widget, or null
    *         if this fixture widget is a top-level widget of the form it
    *         belongs to.
    */
   FixtureWidget getParent();

   /**
    * <p>
    * Gets the unmodifiable map of {@code FixtureWidget} children of this
    * fixture widget (keyed by descriptive widget name), or an empty map if this
    * widget has no children.
    * </p>
    * 
    * <p>
    * This method never returns null.
    * </p>
    * 
    * @return The unmodifiable map of {@code FixtureWidget} children of this
    *         fixture widget (keyed by descriptive widget name), or an empty map
    *         if this widget has no children.
    */
   Map<String, FixtureWidget> getChildren();

   /**
    * Gets the locator to use if this widget is acting as the target of another
    * fixture widget's "wait" type of action.
    * 
    * @return The locator (usually the widget's own locator but possibly the
    *         locator of a sub-widget) to use as a target for another widget's
    *         "wait" type of action. For example, when we click another button
    *         we may want to wait for this widget (or a certain part of it) to
    *         show up.
    */
   String getWaitTargetLocator();

   /**
    * Sets this widget's state to being selected (for example, by checking a
    * checkbox).
    * 
    * @return Whether or not this widget's state was set to being selected.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean check();   
   
   /**
    * Sets this widget's state to being selected (for example, by checking a
    * check box) by clicking the checkbox's label / text instead of the actual
    * square checkbox.
    * 
    * @return Whether or not this widget's state was set to being selected.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean checkByLabel();   
  
  
   
   /**
    * Clears the contents of this widget (or in the case of a widget such as
    * radio button, deselects it).
    * 
    * @return Whether or not this widget's contents were successfully cleared.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean clear();

   /**
    * Clears the contents of this widget and validates that it contains the
    * specified default value after clearing.
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
    * Clicks this widget.
    * 
    * @return Whether or not this widget was clicked successfully.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean click();


   /**
    * Clicks this widget by seding special key "RETURN".
    * 
    * @return Whether or not this widget was clicked successfully.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean clickByReturn();  
   
   /**
    * Clicks the specified cell in this widget.
    * 
    * <p>
    * Row Index is 1 based.
    * </p>
    * 
    * @param columnId
    *           The id of the column(date-field of columnHeader) where the cell is located.
    * @param rowIndex
    *           The index of the row where the cell is located.
    * 
    * @return Whether or not the specified cell in this widget was clicked
    *         successfully.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean clickCellByColumnId(String columnId, int rowIndex);
   
   /**
    * Clicks the specified cell in this widget.
    * 
    * <p>
    * Row Index is 1 based.
    * </p>
    * 
    * @param columnName
    *           The name of the column where the cell is located.
    * @param rowIndex
    *           The index of the row where the cell is located.
    * 
    * @return Whether or not the specified cell in this widget was clicked
    *         successfully.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean clickCell(String columnName, int rowIndex);
   
   /**
    * Clicks the specified cell in this widget.
    * 
    * <p>
    * Row Index is 1 based.
    * </p>
    * 
    * @param rowIndex
    *           The index of the row where the cell is located.
    * @param columnIdx
    *           The index of the column where the cell is located.
    * @return Whether or not the specified cell in this widget was clicked
    *         successfully.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean clickCell(int rowIndex, int columnIdx);

   /**
    * Clicks the Finder button of the specified cell of this widget.
    * 
    * <p>
    * Row Index is 1 based.
    * </p>
    * 
    * @param columnName
    *           The name of the column where the cell is located.
    * @param rowIndex
    *           The index of the row where the cell is located.
    * 
    * @return Whether or not the value was successfully click into the
    *         specified cell of this widget.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean clickFinderInCell(String columnName, int rowIndex);  
   
   /**
    * Clicks the Finder button of the specified cell of this widget.
    * 
    * <p>
    * Row Index is 1 based.
    * </p>
    * 
    * @param rowIndex
    *           The index of the row where the cell is located.
    * @param columnIdx
    *           The index of the column where the cell is located.           
    *           
    * @return Whether or not the value was successfully click into the
    *         specified cell of this widget.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean clickFinderInCell(int rowIndex, int columnIdx); 
   
   /**
    * Clicks the Pencil Edit button of the specified cell of this widget.
    * 
    * 
    * @param rowIndex
    *           The index of the row where the cell is located.
    * @param columnIdx
    *           The index of the column where the cell is located.           
    *           
    * @return Whether or not the value was successfully click into the
    *         specified cell of this widget.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean clickPencilButtonInCell(int rowIndex, int columnIdx); 
   
   /**
    * Closes this widget (in other words, changes its state from open to
    * closed).
    * 
    * @return Whether or not this widget was closed successfully (in other
    *         words, whether its state changed from open to closed).
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean close();

   /**
    * Delete the row of the specified table 
    * by giving the row index.

    * @param rowIndex
    *           The 1-based index of the row to delete.
    *            
    * @return Whether or not the delete row action could take place.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget or the widget does not
    *            support this action.
    */
    boolean deleteRow(final int rowIndex);   
   
   /**
    * Checks whether or not this widget currently exists in the DOM.
    * 
    * @return Whether or not this widget currently exists in the DOM.
    */
   boolean exists();

   /**
    * Checks whether or not the specified field in this widget currently exists
    * in the DOM.
    * 
    * @param fieldName
    *           The name of the field in question.
    * 
    * @return Whether or not the specified field in this widget currently exists
    *         in the DOM.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action or does not contain
    *            the specified field.
    */
   boolean fieldExists(String fieldName);

   /**
    * Returns the row index of a cell with a given value
    * 
    * @param columnIdx
    *           the the column Index
    * @param value
    *           value of a given cell shown on the screen    
    *               
    * @return Returns the row index of a cell with a given value
    */
   int getCellRowIdxByValue(final int columnIdx, final String value);
   
   /**
    * Gets the text of the specified cell in this widget, or returns null if the
    * cell cannot be located or is invisible.
    * 
    * <p>
    * Row Index is 1 based.
    * </p>
    * 
    * @param columnName
    *           The name of the column where the cell is located.
    * @param rowIndex
    *           The index of the row where the cell is located.
    * 
    * @return The text of the specified cell in this widget, or null if the cell
    *         cannot be located or is invisible.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   String getCellText(String columnName, int rowIndex);
   
   /**
    * Gets the text of the specified cell in this widget, or returns null if the
    * cell cannot be located or is invisible.
    * 
    * <p>
    * Row Index is 1 based.
    * </p>
    * 
    * @param rowIndex
    *           The index of the row where the cell is located.
    * @param columnIdx
    *           The index of the column where the cell is located.
    * 
    * @return The text of the specified cell in this widget, or null if the cell
    *         cannot be located or is invisible.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   String getCellText(int rowIndex, int columnIdx);

   /**
    * Gets the text of the specified cell in this adhoc table widget, or returns
    * null if the cell cannot be located or is invisible.
    * 
    * <p>
    * Row Index is 1 based.
    * </p>
    * 
    * @param columnIndex
    *           The index of the column where the cell is located.
    * @param rowIndex
    *           The index of the row where the cell is located.
    * 
    * @return The text of the specified cell in this widget, or null if the cell
    *         cannot be located or is invisible.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   String getCellTextByColumnIndex(int columnIndex, int rowIndex);

   /**
    * Wait for the specified content to be displayed inside a cell.
    * 
    * <p>
    * Row Index is 1 based.
    * </p>
    * 
    * @param columnName
    *           The name of the column where the cell is located.
    * @param rowIndex
    *           The index of the row where the cell is located.
    * @param content
    *           the expected content to be displayed by the element represented
    *           in the cell
    * @return <li><code>true</code> if the content was displayed by element
    *         within the timeout.</li> <li><code>false</code> if the timeout was
    *         reached.</li>
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean waitForCellContent(String columnName, int rowIndex, String content);

   /**
    * Gets the text of this widget's current page field.
    * 
    * @return The text of this widget's current page field.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   String getCurrentPage();


   /**
    * <p>
    * Gets the text of the specified column in the specified widget, or returns
    * null if the cell cannot be located or is invisible.
    * </p>
    * 
    * @param columnIndex
    *           The 1-based index of the column to get the header text of (in
    *           other words, use 1 for the first row).
    * 
    * @return The text of the specified cell in the widget, or null if the cell
    *         cannot be located or is invisible.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget or the widget does not
    *            support this action.
    */
   String getHeaderCellText(final int columnIndex);

   /**
    * <p>
    * Gets the text of the specified column in the specified widget, or returns
    * null if the cell cannot be located or is invisible.
    * </p>
    * 
    * @param columnId
    *           The id of the column header, mapping the data-field attribute for 
    *           the column header in source code
    * 
    * @return The index of the specified column by column ID, or null if the column
    *         cannot be located.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget or the widget does not
    *            support this action.
    */
   int getIndexOfColumnById(final String columnId);
   
   /**
    * Gets the options that belong to this widget (for example, the values in a
    * listBox or comboBox).
    * 
    * @return The options that belong to this widget (for example, the values in
    *         a listBox or a comboBox).
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   String[] getAllOptions();
   
   /**
    * get a string array, containing all the selected options from a listBox
    * 
    * @return a String array containing all the selected options. If the list
    *         box cannot be located, or if there are no values, the return value
    *         is <code>null</code>.
    * @throws org.openqa.selenium.NoSuchElementException
    *            if unable to find list box.
    */
   String[] getSelectedOptions();
  
   /**
    * Gets the number of rows in this widget.
    * 
    * @return The number of rows in this widget.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   int getRowCount();

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
    * Gets the placeholder text of this widget, or returns null if this widget cannot be
    * located or is invisible.
    * 
    * @return The placeholder text of this widget, or null if this widget cannot be located
    *         or is invisible.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   String getPlaceHolderText();  

   /**
    * Gets the text of this widget's total pages field.
    * 
    * @return The text of this widget's total pages field.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   String getTotalPages();

   /**
    * Change the page for this widget.
    * 
    * @param page
    *           The page of this widget to go to.
    * 
    * @return Whether or not this widget can change to the specified page.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean goToPage(final String page);

   /**
    * Checks whether or not this widget contains data in the specified cell.
    * 
    * <p>
    * Row Index is 1 based.
    * </p>
    * 
    * @param columnIndex
    *           The index of the column where the cell is located.
    * @param rowIndex
    *           The index of the row where the cell is located.
    * 
    * @return Whether or not this widget contains data in the specified cell.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean isCellDataPresent(int columnIndex, int rowIndex);

   /**
    * Checks whether or not this widget is currently in a selected state.
    * 
    * @return Whether or not this widget is currently in a selected state.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean isChecked();   
   
   /**
    * Checks whether or not this widget is currently disabled.
    * 
    * @return Whether or not this widget is currently disabled.
    */
   boolean isDisabled();

   /**
    * Checks whether or not this widget is currently editable. Note that a
    * widget is never editable when it is invisible.
    * 
    * @return Whether or not this widget is currently editable.
    */
   boolean isEditable();


   /**
    * Checks whether or not this widget is currently in an open state (as
    * opposed to a closed state).
    * 
    * @return Whether or not this widget is currently in an open state (as
    *         opposed to a closed state).
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean isOpen();

   /**
    * Checks whether or not this widget is currently in a selected state.
    * 
    * @return Whether or not this widget is currently in a selected state.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean isSelected();

   /**
    * Checks whether or not this widget is currently visible.
    * 
    * @return Whether or not this widget is currently visible.
    */
   boolean isVisible();
   
   /**
    * Checks whether a column is visible.
    * 
    * @param columnIdx
    *           The index of a column
    * 
    * @return Whether or not this column is currently visible.
    */
   boolean isColumnHidden(int columnIdx);
   
   /**
    * Move mouse to an element and hover over it.
    */
   void hover();


   /**
    * Clicks on this widget's navigation button for the specified navigation
    * position.
    * 
    * @param action
    *           The position to navigate to, which is either {@code "first"},
    *           {@code "previous"}, {@code "next"}, or {@code "last"}.
    * 
    * @return Whether or not this widget's navigation button for the specified
    *         position was clicked.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action or an invalid position
    *            was specified.
    */
   boolean navigate(String action);
   
   /**
    * Opens this widget (in other words, changes its state from closed to open).
    * 
    * @return Whether or not this widget was opened successfully (in other
    *         words, whether its state changed from closed to open).
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean open();

   /**
    * Just simulate pressing the specified keyboard key for the specified cell.
    * 
    * @param key
    *           The keyboard key to press, which is either {@code "escape"}
    * 
    * @param columnName
    *           The name of the column where the cell is located.
    * 
    * @param rowIndex
    *           The row number of this widget to select and press a keyboard key
    *           on.
    * 
    * @return Whether or not the specified keyboard key was typed for the
    *         specified row of this widget.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action or an invalid position
    *            was specified.
    */
   boolean pressInCell(final String key, final String columnName, final int rowIndex);

   /**
    * Clicks on this widget's second column and then pressing the specified
    * keyboard key for the specified row.
    * 
    * @param key
    *           The keyboard key to press, which is either {@code "delete"},
    *           {@code "alt insert"}, or {@code "alt backspace"}.
    * 
    * @param rowIndex
    *           The row number of this widget to select and press a keyboard key
    *           on.
    * 
    * @return Whether or not the specified keyboard key was typed for the
    *         specified row of this widget.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action or an invalid position
    *            was specified.
    */
   boolean pressInRow(final String key, final int rowIndex);

   /**
    * Sets this widget's state to being selected (for example, by checking a
    * tab).
    * 
    * @return Whether or not this widget's state was set to being selected.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean select();

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
    * Used in special case when type() are not working. type() uses webdriver's 
    * clear() and sendKeys(), which sometimes not work well on FireFox and Chrome. 
    * An example is : gl2100-> type("00001") for BatchNumber -> Reverse...-> 
    * type("00003") for BatchNumber(id="Data_ReverseBatchId"), error happens at this
    * point, input value keep original "000000" instead of expected "00003".
    * So extended this new function by using JavaScript for special case handling.
    * 
    * @param value
    *           The value to type into this widget.
    * 
    * @return Whether or not the value was successfully typed into this widget.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this actio00n.
    */
   boolean typeByJavaScript(String value);
   
   /**
    * Types the specified value into this widget. This action will clear any
    * existing value from this widget before typing in the new value. No Tab 
    * after the sendText
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
    * Used when Type not working for clear() lose focus
    * 
    * @param value
    *           The value to type into this widget.
    * 
    * @return Whether or not the value was successfully typed into this widget.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean typeWithCtrlADel(String value);

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
    * Types the TAB into this widget. This action will jsut do tab.
    * 
    * @return Whether or not the typing a tab action was successfully typed into
    *         this widget.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean pressTab();

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
   boolean typeWithoutTab(String value);

   /**
    * Types the specified value into the specified cell of this widget. This
    * action will replace any existing value in the cell with the new value.
    * After inputting the data, this action will tab out. Row Index is 1 based.
    * 
    * <p>
    * Row Index is 1 based.
    * </p>
    * 
    * @param columnName
    *           The name of the column where the cell is located.
    * @param rowIndex
    *           The index of the row where the cell is located.
    * @param value
    *           The value to type into the specified cell of this widget.
    * 
    * @return Whether or not the value was successfully typed into the specified
    *         cell of this widget.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean typeIntoCell(String columnName, int rowIndex, String value);
      
   /**
    * Wait until the cell with expected value appears.
    * 
    * <p>
    * Row Index is 1 based.
    * </p>
    *
    * @param rowIndex
    *           The index of the row where the cell is located.
    * @param columnIdx
    *           The index of the column where the cell is located.
    * @param value
    *           The value to type into the specified cell of this widget.
    *           
    * 
    * @return Whether or not the value was successfully typed into the specified
    *         cell of this widget.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean typeIntoCellByColumnIdx(int rowIndex, int columnIdx, String value);
   
   /**
    * Types the specified value into the specified cell of this widget. This
    * action will replace any existing value in the cell with the new value.
    * This action will not Tab after inputting the data.
    * 
    * <p>
    * Row Index is 1 based.
    * </p>
    * 
    * @param rowIndex
    *           The index of the row where the cell is located.
    * @param columnIdx
    *           The index of the column where the cell is located.
    * @param value
    *           The value to type into the specified cell of this widget.
    * 
    * @return Whether or not the value was successfully typed into the specified
    *         cell of this widget.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean typeIntoCellWithoutTab(int rowIndex, int columnIdx, String value);

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
    * 
    * @return Whether or not the value was successfully typed into this widget.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean typeWithoutClearAndWithoutTab(String value);

   /**
    * Clears the contents of this widget (or in the case of a widget such as
    * checkbox deselects it).
    * 
    * @return Whether or not this widget's contents were successfully cleared.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean uncheck();    
   
   /**
    * Waits for this widget to appear before proceeding.
    * 
    * @return Whether or not this widget appeared before the default timeout was
    *         reached.
    */
   boolean waitFor();

   /**
    * Waits for this widget to not be visible before proceeding.
    * 
    * @return Whether or not this widget disappeared before the default timeout was
    *         reached.
    */
   boolean waitForNotVisible();
   
   /**
    * Waits until the specified widget becomes disabled. This action is a better
    * alternative than using a pause.
    * 
    * @return Whether or not the specified widget becomes disabled before the
    *         default timeout was reached.
    * 
    * @throws IllegalArgumentException
    *            This UI does not contain a widget by that name.
    */
   boolean waitUntilDisabled();

   /**
    * Waits until the specified widget becomes enabled (not disabled). This
    * method should be used after inputting or selecting a value in a widget
    * that causes data to load. Such as after inputting a customer number, or
    * item number and before inputting a widget that was just enabled. This
    * action is a better alternative than using a pause.
    * 
    * @return Whether or not the specified widget becomes enabled before the
    *         default timeout was reached.
    * 
    * @throws IllegalArgumentException
    *            This UI does not contain a widget by that name.
    */
   boolean waitUntilEnabled();

   /**
    * Waits until the specified content is present in the widget (or not). This
    * method should be used after inputting or selecting a value in a widget
    * that causes content to display in another widget. Such as after inputting
    * an order number the customer number is displayed. This action is a better
    * alternative than using a pause.
    * 
    * @param content
    *           The expected content to be present in the widget before the
    *           timeout.
    * @return Whether or not the specified content is present in the widget
    *         before the default timeout was reached.
    */
   boolean waitForContent(String content);


   /**
    * Selects the specified value from the specified cell of this widget.
    * 
    * <p>
    * Row Index is 1 based.
    * </p>
    * 
    * @param columnName
    *           The name of the column where the cell is located.
    * @param rowIndex
    *           The index of the row where the cell is located.
    * @param value
    *           The value to select from the specified cell of this widget.
    * 
    * @return Whether or not the value was successfully selected into the
    *         specified cell of this widget.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean selectFromCell(String columnName, int rowIndex, String value);

   /**
    * Selects the specified value from the specified cell of this widget.
    * 
    * <p>
    * Row Index is 1 based.
    * </p>
    * 
    * @param rowIndex
    *           The index of the row where the cell is located.
    * @param columnIdx
    *           The index of the column where the cell is located.
    * @param value
    *           The value to select from the specified cell of this widget.
    * 
    * @return Whether or not the value was successfully selected into the
    *         specified cell of this widget.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean selectFromCell(int rowIndex, int columnIdx, String value);
   
   /**
    * <p>
    * Select an option form a comboBox in a cell of a table, App has value validation on 
    * it immediately and each time after an option is selected, <span> and <select> are wrapped up.
    * </p>
    * 
    * <p>
    * Row Index is 1 based.
    * </p>
    * 
    * @param rowIndex
    *           The index of the row where the cell is located.
    * @param columnIdx
    *           The index of the column where the cell is located.
    * @param value
    *           The value to select from the specified cell of this widget.
    * 
    * @return Whether or not the value was successfully selected into the
    *         specified cell of this widget.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean selectFromCellWithoutValidation(int rowIndex, int columnIdx, String value);

   /**
    * <p>
    * Clicks the "Calendar" Button.
    * </p>
    * 
    * @return Whether or not the widget was clicked successfully.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget or the widget does not
    *            support this action.
    */
   boolean clickCalendar();

   /**
    * Selects the specified Date in the Calendar.
    * 
    * <p>
    * Note 1:You must call {@link #clickCalendar() clickCalendar} on the
    * specified widget before you can call this action.
    * </p>
    * <p>
    * Note 2:The format <b>date</b> should be "MM/DD/YYYY".
    * </p>
    * 
    * @param date
    *           the string to be parsed as date.
    * 
    * @return <li><code>True </code>if date is selected successfully.</li> <li>
    *         <code>False</code> if date is not selected.</li>
    */

   boolean selectCalendarDate(String date);

   /**
    * Waits for the Table to load successfully.
    * 
    * @return Whether or not the Table is loaded successfully.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean waitForTableToLoad();

   /**
    * Returns selected item's text from a cell that is represented by a ComboBox
    * 
    * @param columnName
    *           The name of the column whose cell text is retrieved.
    * @param rowIndex
    *           The 1-based index of the row whose text is retrieved
    * @return selected item's text from a cell that is represented by a ComboBox
    */
   String getSelectedCell(final String columnName, int rowIndex);

   /**
    * Selects the specified option on this widget.
    * 
    * @param option
    *           The option to select
    * 
    * @return Whether or not the option was selected successfully.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean select(final String option);
   
   /**
    * Selects the specified option on this widget.
    * 
    * @param option
    *           The option to select
    * 
    * @return Whether or not the option was selected successfully.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean selectOption(final String option);
   
   /**
    * Selects the specified option on this widget.
    * 
    * @param index
    *           The index of an option to select
    * 
    * @return Whether or not the option was selected successfully.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean selectByIdx(int index);
   
   
   /**
    * deSelects the specified option on this widget.
    * 
    * @param option
    *           The option to deselect
    * 
    * @return Whether or not the option was deselected successfully.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean deselect(final String option);

   /**
    * deselectAll the options on this widget.
    * 
    * @return Whether or not All the option were deselected successfully.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean deselectAll();
   
   /**
    * selectAll the options on this widget.
    * 
    * @return Whether or not All the option were selected successfully.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean selectAll();
   /**
    * Ensure whether the specified Radio Button in the Radio Group widget is
    * selected or not.
    * 
    * @param radioButtonLabel
    *           The Label of the Radio Button.
    * 
    * @return <code>true</code> if the specified Radio Button in the Radio Group
    *         is selected. <code>false</code> if the specified Radio Button in
    *         the Radio Group is not selected.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean isSelected(final String radioButtonLabel);

   /**
    * Validates whether the specified Cell in the Table is editable or not.
    * 
    * NOTE: You must call {@link #clickCell(String, int) clickCell} on the
    * specified widget(Cell) before you can call this action.
    * 
    * @param columnName
    *           the column name of the Table to access.
    * @param rowIndex
    *           the row index of the Table to access.
    * 
    * @return <li><code>true</code> if the specified Cell in the Table is
    *         editable.</li> <li><code>false</code> if the specified Cell in the
    *         Table is not editable.</li>
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean isCellEditable(final String columnName, final int rowIndex);
   
   /**
    * select the lines(checkboxes) in a table to be deleted.
    * 
    * @param selectedLines
    *    the lines to be selected for delete
    * 
    * @return Whether or not the lines are successfully selected.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean selectLinesForDelete(final String selectedLines);   
   
   /**
    * unselect the lines(checkboxes) in a table.
    * 
    * @param unselectedLines
    *    the lines to be unselected for delete
    * 
    * @return Whether or not the lines are successfully unselected.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   boolean unselectLinesForDelete(final String unselectedLines);   

   /**
   * Check if all the checkboxes(delete indication) in a table are checked after
   * the checkbox in the table header is checked 
   * 
   * @return Whether or not all the checkboxes in the table are selected.
   * 
   * @throws IllegalArgumentException
   *            The UI does not contain such a widget or the widget does not
   *            support this action.
   */
   boolean isAllChecked(); 
   
   
   /**
   * Check if all the checkboxes(delete indication) in a table are unchecked after
   * the checkbox in the table header is unchecked 
   * 
   * @return Whether or not all the checkboxes in the table are unselected.
   * 
   * @throws IllegalArgumentException
   *            The UI does not contain such a widget or the widget does not
   *            support this action.
   */
   boolean isAllUnChecked(); 
   
   /**
    * Gets the number of the total items in a table.
    * 
    * @return The number of the total items in a table.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   int getTotalItems();
   
   /**
    * Gets the number of the checked items in a table to be delete.
    * 
    * @return The number of the checked items in a table to be delete.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   int getCheckedItemsToDelete();

   
   /** 
    * ===========================================================
    * Sikuli related actions
    * 
    */
   
   /**
    * Clicks the image.  Uses the Sikuli tool.
    * 
    * @return Whether the action was successful or not
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */   
   boolean clickImage();   

   
   /**
    * Double-Clicks the image.  Uses the Sikuli tool.
    * 
    * @return Whether the action was successful or not
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */   
   boolean doubleClickImage();   

   /**
    * Right-Click the image.  Uses the Sikuli tool.
    * 
    * @return Whether the action was successful or not
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */   
   boolean rightClickImage();   
   
   /**
    * Pressing a series of keys, such as Ctrl+X, Ctrl+V, or Del. 
    * 
    * @param  keyToHold, the key to press while entering another key. Accept 'CONTROL', 'SHIFT', and 'DELETE'.
    *         This is not required and can be left empty.  If left empty, it will just enter the sequence
    *         of letters in charToPress.
    *         
    * @param  charToPress, the sequence of characters to enter while holding the key 'keyToHold'.  This is 
    *         optional.  If it is empty, it will simply press the single key in keyToHold. e.g. Delete key. 
    * 
    * @return <li><code>true</code> if the region is found and the keys are pressed successfully.</li> <li>
    *         <code>false</code> if the widget does not exists, keyToHold is not recognized,
    *          or issues with pressing the keys.</li>
    */
   boolean pressKeySequence(String keyToHold, String charToPress);
   
   /**
    * Select menu item the image.  Uses the Sikuli tool.
    * 
    * @param menuTitle
    *    The name of the menu where the menu item resides.
    *    
    * @param menuItem
    *    The name of the menu item to select.
    * 
    * @return Whether the action was successful or not
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */     
   boolean selectMenuItemFromImage(String menuTitle, String menuItem);   
   
   /**
    * Types into the image.  Uses the Sikuli tool.
    * 
    * @param value
    *    The value to type into image
    *    
    * @return Whether the action was successful or not
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */  
   boolean typeIntoImage(String value);   
   
   /**
    * Waits for an image.  Uses the Sikuli tool.
    * 
    * @return Whether the action was successful or not
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */  
   boolean waitForImage();   
   
   /**
    * Waits for an image.  Uses the Sikuli tool.
    * 
    * @param  Timeout, in seconds, to wait for the image to show up
    * 
    * @return Whether the action was successful or not
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */  
   boolean waitForImage(int timeout);
}
