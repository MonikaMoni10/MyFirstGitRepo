/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.widgets;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.WebElement;

import com.sonata.generic.automation.browser.Browser;
import com.sonata.generic.automation.browser.SpecialKey;

/**
 * The <code>Table</code> class represents an Table that may consist of
 * TextBoxes, TableNavigation.
 */
public class Table extends Widget
{

   /**
    * The list of column headers for the instantiated Table, used to know
    * which column index to set based on the name of the column header. Would be
    * nice to initialize this when old table is created, but cannot due to the
    * fact that the column headers get populated at test runtime, as we can not
    * anticipate what columns and their column indexes are.
    */
   private List<String> columnHeaders;
   private List<String> idOfColumnHeaders;
   private final String DELETE_ROW_COLUMN_IDENTIFIER = "deleteRowColumn";
   private TableNavigation tablenavigator;
   private String headerRowLocator;
   private String dataBodyLocator;
   
   /**
    * constructs an instance of the {@link Table} class.
    * 
    * @param locator
    *           the locator of the Table
    * @param browser
    *           the browser that will be used to access the control
    */
   public Table(final String locator, final Browser browser)
   {
      super(locator, browser);
      headerRowLocator = "//div[@id='" + locator + "']/div[@class='k-grid-header']/div/table/thead/tr";
      dataBodyLocator = "//div[@id='" + locator + "']/div[@class='k-grid-content']/table/tbody";      
   }

   /**
    * Delete a row of the table
    * 
    * @param rowIndex
    *    row to delete
    * @return Whether the delete button of the table was clicked.
    */
   public boolean deleteRow(final int rowIndex)
   {
      locatorCheckAndAdjust();
      
      // assumes the locator is already an XPATH      
      String deleteButtonLocator = getLocatorFor(rowIndex, this.getIndexOfColumn(DELETE_ROW_COLUMN_IDENTIFIER)) + "/a";
      
      return this.getBrowser().click(deleteButtonLocator);

   }
   
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
   public int getCellRowIdxByValue(final int columnIdx, final String value)
   {
      int pages = Integer.parseInt(this.getTotalPages());
      
      for ( int i=1; i <= pages; i++)
      {
         if (i != 1)
            this.goToPage(Integer.toString(i));
         
         int itemsOnCurrentPage = this.getItemsOnCurrentPage();
         for (int j=1; j<= itemsOnCurrentPage; j++)
         {
            if(this.getField(j, columnIdx).getText().equals(value))
            {
               return j;
            }
         }    
      }
      return -1;
   }
   
   /**
    * Returns a {@link WidgetInterface} representing a field (column, row) in
    * this Table.
    * 
    * @param rowIndex
    *           the row index of the Table to access
    * @param columnName
    *           the Column Heading of the OldTable to access
    * @return a {@link WidgetInterface} representing a field (column, row) in
    *         this Table.
    */
   public WidgetInterface getField(final String columnName, final int rowIndex)
   {
      int columnIndex = this.getIndexOfColumn(columnName);

      return getField(rowIndex, columnIndex);
   }   
   
   /**
    * Returns a {@link Label} or {@link TextBox} representing a field (column, row) in this
    * Table.
    * 
    * If a cell is not editable, the value is attached to <td> as a label
    * If a cell is editable, <td>.class has key words like "k-edit-cell", 
    * the value is saved in its extended sub element like input or span, which is a textBox
    * 
    * @param rowIndex
    *           the row index of the Table to access
    * @param columnIndex
    *           the Column index of the Table to access
    * @return a {@link WidgetInterface} representing a field (column, row) in
    *         this OldTable.
    */
   public WidgetInterface getField(final int rowIndex, final int columnIndex)
   {
      locatorCheckAndAdjust();
      
      WidgetType type = null;
      String baseLocator = "";
      
      int row = rowIndex;
      baseLocator = getLocatorFor(row, columnIndex);
      
      if (isValidColumnIndex("", columnIndex) && (isValidRowIndex(row)) && this.getBrowser().exists(baseLocator))
      {
         WebElement element = this.getBrowser().findElement(baseLocator);
         if(element.getAttribute("class").contains("k-edit-cell"))
         {
            return this.getTextBoxField(rowIndex, columnIndex);
         }
         else
         {
            type = WidgetType.LABEL;
            return type.createWidget(baseLocator, this.getBrowser());
         }
      }
      else
         return null;
   }
   
   /**
    * Returns a {@link WidgetInterface} representing a field (column, row) in
    * this Table.
    * 
    * @param columnId
    *           the columnId(date-field of columnHeader) to locate a cell
    * @param rowIndex
    *           the row index of the Table to access
    * @return a {@link WidgetInterface} representing a field (column, row) in
    *         this Table.
    */
   public WidgetInterface getFieldByColumnId(final String columnId, final int rowIndex)
   {
      locatorCheckAndAdjust();

      WidgetType type = null;
      String cellLocator = "";
            
      if (isValidRowIndex(rowIndex))
      {
         cellLocator = dataBodyLocator + "/tr[" + rowIndex + "]/td[@date-field='" + columnId + "']"; 

         if (this.getBrowser().exists(cellLocator))
         {
            type = WidgetType.LABEL;
         }
      }
      // It doesn't start with /div or /td/ then it is not a valid widget
      else
      {
         //Throw exception;
         throw new NoSuchElementException("This locator does not exist or does not identify a known widget");
      }

      return type.createWidget(cellLocator, this.getBrowser());      
   } 

   /**
    * Returns a {@link WidgetInterface} representing a field (column, row) in
    * this Table.
    * 
    * @param rowIndex
    *           the row index of the Table to access
    * @param columnName
    *           the Column Heading of the Table to access
    * @return a {@link WidgetInterface} representing a field (column, row) in
    *         this Table.
    */
   public WidgetInterface getComboBoxField(final String columnName, final int rowIndex)
   {
      locatorCheckAndAdjust();
      
      int columnIndex = this.getIndexOfColumn(columnName);

      return getComboBoxField(rowIndex, columnIndex);
   }      
   
   /**
    * Returns a {@link ComboBox} representing a field (column, row) in this
    * Table.
    * 
    * @param rowIndex
    *           the row index of the Table to access
    * @param columnIndex
    *           the Column index of the Table to access
    * @return a {@link WidgetInterface} representing a field (column, row) in
    *         this Table.
    */
   public WidgetInterface getComboBoxField(final int rowIndex, final int columnIndex)
   {
      WidgetType type = null;
      String baseLocator = "";
      
      locatorCheckAndAdjust();      
      
      int row = rowIndex;
      
      if (isValidColumnIndex("", columnIndex) && (isValidRowIndex(row)))
      {
         baseLocator = getLocatorFor(row, columnIndex);
         
         ArrayList<String> helpersList = new ArrayList<String>();
         helpersList.add("/span/select");
         for(String helper:helpersList)
         {
            if (this.getBrowser().exists(baseLocator + helper))
            {
               type = WidgetType.COMBOBOX;
               return type.createWidget(baseLocator + helper, this.getBrowser());
            }
         }
      }

      //Throw exception;
      throw new NoSuchElementException("This locator does not exist or does not identify a known widget");
      
   }      
   
   /**
    * Returns a {@link WidgetInterface} representing a field (column, row) in
    * this Table.
    * 
    * @param columnName
    *           the Column Heading of the Table to access
    * @param rowIndex
    *           the row index of the Table to access
    * @return a {@link WidgetInterface} representing a field (column, row) in
    *         this Table.
    */
   public WidgetInterface getFinderButtonField(final String columnName, final int rowIndex)
   {
      locatorCheckAndAdjust();
      
      int columnIndex = this.getIndexOfColumn(columnName);

      return getFinderButtonField(rowIndex, columnIndex);
   }  
   
   /**
    * Returns a {@link Button} representing a field (column, row) in this
    * Table.
    * 
    * @param rowIndex
    *           the row index of the Table to access
    * @param columnIndex
    *           the Column index of the Table to access
    * @return a {@link WidgetInterface} representing a field (column, row) in
    *         this Table.
    */
   public WidgetInterface getFinderButtonField(final int rowIndex, final int columnIndex)
   {
      WidgetType type = null;
      String baseLocator = "";
      
      locatorCheckAndAdjust();      
      
      int row = rowIndex;
      
      if (isValidColumnIndex("", columnIndex) && (isValidRowIndex(row)))
      {
         baseLocator = getLocatorFor(row, columnIndex);
         
         ArrayList<String> helpersList = new ArrayList<String>();
         // e.g.: Journal Entry -> Grid -> SourceType
         helpersList.add("/input[2]");
         // e.g.: Journal Entry -> Grid -> Rate
         helpersList.add("/input");
         // e.g.: Optional Field table -> Optional Field
         helpersList.add("/div/div[2]/input");
         
         for(String helper:helpersList)
         {
            if (this.getBrowser().existsNoWait(baseLocator + helper))
            {
               type = WidgetType.BUTTON;
               return type.createWidget(baseLocator + helper, this.getBrowser());
            }
         }
      }

      //Throw exception;
      throw new NoSuchElementException("This locator does not exist or does not identify a known widget");
      
   }
   
   /**
    * Returns a {@link Button} with a pencil icon representing a field (column, row) in this
    * Table.
    * 
    * @param rowIndex
    *           the row index of the Table to access
    * @param columnIndex
    *           the Column index of the Table to access
    * @return a {@link WidgetInterface} representing a field (column, row) in
    *         this Table.
    */
   public WidgetInterface getPencilButtonField(final int rowIndex, final int columnIndex)
   {
      WidgetType type = null;
      String baseLocator = "";
      
      locatorCheckAndAdjust();      
      
      int row = rowIndex;
      
      if (isValidColumnIndex("", columnIndex) && (isValidRowIndex(row)))
      {
         baseLocator = getLocatorFor(row, columnIndex);
         String pencilButtonLocator = baseLocator + "/div/span[2]/input";
         
         if (this.getBrowser().exists(pencilButtonLocator))
            {
               type = WidgetType.BUTTON;
               return type.createWidget(pencilButtonLocator, this.getBrowser());
            }   
         }

      //Throw exception;
      throw new NoSuchElementException("This locator does not exist or does not identify a known widget");
      
   }
   
   /**
    * Returns a {@link WidgetInterface} representing a field (column, row) in
    * this Table.
    * 
    * @param columnName
    *           the Column Heading of the Table to access
    * @param rowIndex
    *           the row index of the Table to access
    * @return a {@link WidgetInterface} representing a field (column, row) in
    *         this Table.
    */
   public WidgetInterface getTextBoxField(final String columnName, final int rowIndex)
   {
      locatorCheckAndAdjust();
      
      int columnIndex = this.getIndexOfColumn(columnName);

      return getTextBoxField(rowIndex, columnIndex);
   }      
   
   /**
    * Returns a {@link TextBox} representing a field (column, row) in this
    * Table.
    * 
    * @param rowIndex
    *           the row index of the Table to access
    * @param columnIndex
    *           the Column index of the Table to access
    * @return a {@link WidgetInterface} representing a field (column, row) in
    *         this Table.
    */
   public WidgetInterface getTextBoxField(final int rowIndex, final int columnIndex)
   {
      WidgetType type = null;
      String baseLocator = "";
      
      locatorCheckAndAdjust();      
      
      int row = rowIndex;
      
      if (isValidColumnIndex("", columnIndex) && (isValidRowIndex(row)))
      {
         baseLocator = getLocatorFor(row, columnIndex);
                  
         ArrayList<String> helpersList = new ArrayList<String>();

         // For a numberTextBox like "GL Jounal Entry-> Grid -> Source Debit" 
         helpersList.add("/span/span/input[2]");
         // For a dateTextBox like "GL Jounal Entry-> Grid -> Date" 
         helpersList.add("/span/span/input");
         // For a normal textBox like "GL Jounal Entry-> Grid -> Account Number"
         helpersList.add("/input");
         // For column OptionalField in "Optional Field" table, its sturcture is special
         helpersList.add("/div/div/input");
         // Suppose the cell is not editable, the xPath is just the td[]
         // Used to get the text from a cell although it's not editable
         helpersList.add("");
         
         for(String helper:helpersList)
         {
            if (this.getBrowser().existsNoWait(baseLocator + helper))
            {
               type = WidgetType.TEXTBOX;
               return type.createWidget(baseLocator + helper, this.getBrowser());
            }
         }
      }

      //Throw exception;
      throw new NoSuchElementException("This locator does not exist or does not identify a known widget");
      
   }
   
   /**
    * Returns a {@link WidgetInterface} representing a field (column, row) in
    * this Table.
    * 
    * @param columnId
    *           the columnId(date-field of columnHeader) to locate a cell.
    * @param rowIndex
    *           the row index of the Table to access
    * @return a {@link WidgetInterface} representing a field (column, row) in
    *         this Table.
    */
   public WidgetInterface getTextBoxFieldByColumnId(final String columnId, final int rowIndex)
   {
      WidgetType type = null;
      String baseLocator = "";
      
      locatorCheckAndAdjust();      
            
      if (isValidRowIndex(rowIndex))
      {
         baseLocator = dataBodyLocator + "/tr[" + rowIndex + "]/td[@date-field='" + columnId + "']";
         
         ArrayList<String> helpersList = new ArrayList<String>();
         
         // For a numberTextBox like "GL Jounal Entry-> Grid -> Source Debit" 
         helpersList.add("/span/span/input[2]");
         // For a dateTextBox like "GL Jounal Entry-> Grid -> Date" 
         helpersList.add("/span/span/input");
         // For a normal textBox like "GL Jounal Entry-> Grid -> Account Number"
         helpersList.add("/input");
         // For column OptionalField in "Optional Field" table, its sturcture is special
         helpersList.add("/div/div/input");
         // Suppose the cell is not editable, the xPath is just the td[]
         // Used to get the text from a cell although it's not editable
         helpersList.add("");
         
         for(String helper:helpersList)
         {
            if (this.getBrowser().existsNoWait(baseLocator + helper))
            {
               type = WidgetType.TEXTBOX;
               return type.createWidget(baseLocator + helper, this.getBrowser());
            }
         }
      }

      //Throw exception;
      throw new NoSuchElementException("This locator does not exist or does not identify a known widget");
   }   
   
   /**
    * Gets the total number of column in a Table.
    * 
    * @return the number of column in a Table.
    */
   public int getColumnCount()
   {
      locatorCheckAndAdjust();
      
      int rowToCheck = 1;
      int columnNumber = 1;

      while (this.getBrowser().exists(getLocatorFor(rowToCheck, columnNumber)))
      {
         columnNumber++;
      }

      return columnNumber - 1;
   }   
   

   /**
    * Get the Column Name for a given column index.
    * 
    * @param columnIndex
    *           column to lookup
    * 
    * @return the column heading name at the specified column index.
    */
   public String getColumnNameByIndex(final int columnIndex)
   {
      locatorCheckAndAdjust();
      
      try
      {
         String currentColumnHeadingText = this.getBrowser().getText(
               headerRowLocator + "/th[" + columnIndex + "]");

         return currentColumnHeadingText;
      }
      catch (NullPointerException e)
      {
         // Instead of previous implementation to check if exists then check getText, meaning 2 separate XPATH calls which is expensive in terms of time, just do the one call and handle the exception
         return null;
      }
   }

   /**
    * Returns the current page shown in a table.
    * 
    * @return the current page shown in a table.
    * 
   **/
   public String getCurrentPage()
   {
      return getTableNavigation().getCurrentPageTextBox().getText();
   }  
   
   /**
    * Returns the total pages shown in a table.
    * 
    * @return the total pages shown in a table.
    * 
    */
   public String getTotalPages()
   {
      return getTableNavigation().getTotalPagesLabel().getText().replace("Pageof", "").trim();
   }   
         
   /**
    * Go to a  given page in a table with more than one page.
    * 
    * @param page
    *           a given page
    * @return Whether the page is changed to the expected given page.
    * 
    */
   public boolean goToPage(final String page)
   {
      getTableNavigation().getCurrentPageTextBox().click();

      if (!getTableNavigation().getCurrentPageTextBox().typeWithoutWaitForClicable(page))
         return false;

      getTableNavigation().getCurrentPageTextBox().click();
      //type with specialKey "ENTER" is not reliable sometimes especially when the length of the input greater than 1
      //e.g. "input is 12", the real result is "1", the last number is removed
      //try with sendSpecialKey "RETURN", it is more reliable
      //return this.getBrowser().type(getTableNavigation().getCurrentPageTextBox().getLocator(), SpecialKey.ENTER);      
      return this.getBrowser().clickByReturn(getTableNavigation().getCurrentPageTextBox().getLocator());
   }   
   
   /**
    * Gets a locator from headerTable for a given row and column.
    * 
    * @param columnIndex
    *           The column used in new locator
    * @return a locator for a given row and column.
    */
   private String getHeaderLocatorFor(int columnIndex)
   {
      return headerRowLocator + "/th[" + columnIndex + "]";
   }   
   
   /**
    * Returns a {@link Label} representing a field (column, row) in this
    * Table.(very unique Table with all label but user could not edit)
    * 
    * @param columnIndex
    *           the Column index of the Table to access
    * @return a {@link WidgetInterface} representing a field (column, row) in
    *         this Table.
    */
   public WidgetInterface getHeaderField(final int columnIndex)
   {
      WidgetType type = null;
      String baseLocator = "";

      locatorCheckAndAdjust();      
      
      if (isValidColumnIndex("", columnIndex))
      {
         if (this.getBrowser().exists(headerRowLocator + "/th[" + columnIndex + "]"))
         {
            baseLocator = getHeaderLocatorFor(columnIndex);
         }
         type = WidgetType.LABEL;
      }
      else
      {
         //Throw exception;
         throw new NoSuchElementException("This locator does not exist or does not identify a known widget");
      }

      return type.createWidget(baseLocator, this.getBrowser());
   }
   

   /**
    * Iterate through the Table columns header row to populate a list
    * containing these column headings. Would be nice to initialize this when
    * Table is created, but cannot due to the fact that the column headers
    * get populated at test runtime, as we can not anticipate what columns and
    * their column indexes are.
    * 
    * August 2011, refactor this method as it was inefficient to get all the
    * columns in IE it would take 60 seconds to do so for adhoc oldtable. So
    * change the approach to fetch the column until it reaches what is desired.
    * Subsequent calls will first iterate through the list, if not found, then
    * go through more columns of the OldTable. IF the end is reached then the
    * code will enter the catch and then the finally block to just return 0
    * 
    * @param columnInUse
    *           column heading of the column to utilize
    * @return a List<String> that is the column headings list.
    */
   public int getIndexOfColumn(String columnInUse)
   {      
      int columnIndex = 0;
      boolean isColumnFound = false;

      locatorCheckAndAdjust();      
      
      if (columnHeaders == null)
      {
         columnHeaders = new ArrayList<String>();
      }

      for (String column : columnHeaders)
      {
         ++columnIndex;

         isColumnFound = (columnInUse.equalsIgnoreCase(column));

         if (isColumnFound)
            return columnIndex;
      }

      try
      {         
         // if the code reaches here then it is not in the current List so we need to fetch more columns 
         // until we reach the end. 
         // when the end is reached the code should throw an exception         
         while (!isColumnFound)
         {
            ++columnIndex;

            String columnHeaderLocator = headerRowLocator + "/th[" + columnIndex + "]";            
            
            // if the locator does not exist then either we've reached the end of the header row or they may be a change to the structure of the html of the table element
            if (!(this.getBrowser().exists(columnHeaderLocator)))
            {
               throw new NoSuchElementException("Either we've reached the end of the header row or they may be a change to the structure of the html of the table element");
            }            
                        
            // handle if a hidden column is there, such as the first column (id) column
            String attributeStyle = this.getBrowser().getAttribute(columnHeaderLocator, "style");            
            if ((attributeStyle.startsWith("display") && attributeStyle.contains("none")))
            {
               columnHeaders.add("hiddenAtColumn" + columnIndex);
            }
            else
            {
               String currentColumnHeadingText = this.getBrowser().getText(columnHeaderLocator);
   
               // if there is text in the header then add the header text to the list
               // else since this element does exist that must mean an empty column
               // which contains the Delete button in our implementation
               if (currentColumnHeadingText.equals("")||currentColumnHeadingText.equals("&nbsp;"))
               {
                  currentColumnHeadingText = DELETE_ROW_COLUMN_IDENTIFIER;
               }
               
               columnHeaders.add(currentColumnHeadingText);
               
               // If we've reached the column that we need just stop adding to the list and add more when needed
               // This is in hopes to quicken this step as it takes 60 seconds using IE to get the entire 
               // details Table (35 columns).
               isColumnFound = (columnInUse.equalsIgnoreCase(currentColumnHeadingText));
            }
         }

         return columnIndex;
      }
      catch (NullPointerException e)
      {
         // Instead of previous implementation to check if exists then check getText, meaning 2 separate XPATH calls which is expensive in terms of time, just do the one call and handle the exception
         return 0;
      }
      catch (NoSuchElementException e)
      {
         return 0;
      }
   }   
   
   /**
    * Get the column Index by the column Id 
    * 
    * @param columnId
    *           The Id of the column(date-field attribute for column header)  
    * @return a List<String> that is the column headings list.
    */
   public int getIndexOfColumnById(String columnId)
   {      
      int columnIndex = 0;
      boolean isColumnFound = false;

      locatorCheckAndAdjust();      
      
      if (idOfColumnHeaders == null)
      {
         idOfColumnHeaders = new ArrayList<String>();
      }

      for (String column : idOfColumnHeaders)
      {
         ++columnIndex;
         isColumnFound = (columnId.equalsIgnoreCase(column));
         if (isColumnFound)
            return columnIndex;
      }

      try
      {         
         // if the code reaches here then it is not in the current List so we need to fetch more columns 
         // until we reach the end. When the end is reached the code should throw an exception         
         while (!isColumnFound)
         {
            ++columnIndex;
            String columnHeaderLocator = headerRowLocator + "/th[" + columnIndex + "]";
            
            // if the locator does not exist then either we've reached the end of the header row or they may be a change to the structure of the html of the table element
            if (!(this.getBrowser().exists(columnHeaderLocator)))
            {
               throw new NoSuchElementException("Either we've reached the end of the header row or they may be a change to the structure of the html of the table element");
            }            
            String currentColumnHeaderId = this.getBrowser().getAttribute(columnHeaderLocator, "data-field");
            idOfColumnHeaders.add(currentColumnHeaderId);            
   
            // If the data-field of the column header matches the expected columnId
            // Then add the Id of the column header to the list
            isColumnFound = (columnId.equals(currentColumnHeaderId));
         }
         return columnIndex;
      }
      catch (NullPointerException e)
      {
         // Instead of previous implementation to check if exists then check getText, meaning 2 separate XPATH calls which is expensive in terms of time, just do the one call and handle the exception
         return 0;
      }
      catch (NoSuchElementException e)
      {
         return 0;
      }
   }  
   
   /**
    * Gets a locator from dataTable for a given column.
    * 
    * @param row
    *           The row used in new locator
    * @param column
    *           The column used in new locator
    * @return a locator for a given row and column.
    */
   private String getLocatorFor(int row, int column)
   {
      return dataBodyLocator + "/tr[" + row + "]/td[" + column + "]";
   }

   /**
    * Returns a {@link TableNavigation} representing the Table's navigation
    * elements
    * 
    * @return a {@link TableNavigation} representing the Table's navigation
    *         elements
    */
   public TableNavigation getTableNavigation()
   {
      locatorCheckAndAdjust();
      
      if (tablenavigator == null)
         return new TableNavigation("//div[@id='" + this.getLocator() + "']/div[3]", this.getBrowser());
      else
         return tablenavigator;
   }   

   /**
    * Validates whether data is present in Grid or not.
    * 
    * @param columnIndex
    *           the index value of column
    * @param rowIndex
    *           the index value of row
    * @return <code>true</code> if data is present <code>false</code> otherwise
    */
   public boolean isCellDataPresent(final int columnIndex, final int rowIndex)
   {
      locatorCheckAndAdjust();
      
      try
      {
         String text = this.getBrowser().getText(getLocatorFor(rowIndex + 1, columnIndex));

         return (text != null) && !(text.equals(""));
      }
      catch (Exception e)
      {
         System.out.println(e.getMessage());
         return false;
      }
   }   
   
   /**
    * Validates column name.
    * 
    * @param columnName
    *           Representing a Column Name in Table
    * @param columnIndex
    *           Represent the column index in the Table
    * 
    * @return true if it is a valid column name
    */
   private boolean isValidColumnIndex(String columnName, int columnIndex)
   {
      if (columnIndex <= 0)
      {
         throw new NoSuchElementException("The specified column name, " + columnName + ", or column index "
               + columnIndex + " do not represent a valid column in this Table");
      }

      return true;
   }

   /**
    * Validates row index
    * 
    * @param rowIndex
    *           Representing a Row Index Number in Table
    * 
    * @return true if it is a valid row index
    */
   private boolean isValidRowIndex(int rowIndex)
   {
      if (rowIndex < 1 || rowIndex > 10)
      {
         throw new IndexOutOfBoundsException(
               "Row index cannot be negative or large than 18. Row numbers start at 1 and larger than 10");
      }

      return true;
   }

   /**
    * Validates row index
    * 
    * @param columnIdx
    *           Representing a Column Index Number in Table
    * 
    * @return true if it is the column is visible
    */
   public boolean isColumnHidden(int columnIdx)
   {
      locatorCheckAndAdjust();      
      String columnHeaderLocator = headerRowLocator + "/th[" + columnIdx + "]";
      if (!(this.getBrowser().exists(columnHeaderLocator)))
      {
         throw new NoSuchElementException("Either we've reached the end of the header row or they may be a change to the structure of the html of the table element");
      }            
      String style = this.getBrowser().getAttribute(columnHeaderLocator, "style");      
      return style.contains("display: none");
   }
   
   /**
    * Used to adjust the Table locator as there are atleast 2 known implementations for table
    * that have different HTML structuring.
    *  
    */
   private void locatorCheckAndAdjust()
   {
      if (headerRowLocator.equals("//div[@id='" + this.getLocator() + "']/div[@class='k-grid-header']/div/table/thead/tr") && !this.getBrowser().existsNoWait(headerRowLocator) )
      {
         headerRowLocator = "//div[@id='" + this.getLocator() + "']/table/thead/tr"; 
         dataBodyLocator = "//div[@id='" + this.getLocator() + "']/table/tbody"; 
      }
   }   
   
   /**
    * {@inheritDoc}
    */
   public boolean navigate(final String action)
   {
      Button buttonToClick = null;
      
      if (action.equalsIgnoreCase("first"))
         buttonToClick = getTableNavigation().getFirstButton();
      else if (action.equalsIgnoreCase("next"))
         buttonToClick = getTableNavigation().getNextButton();
      else if (action.equalsIgnoreCase("previous"))
         buttonToClick = getTableNavigation().getPreviousButton();   
      else if (action.equalsIgnoreCase("last"))
         buttonToClick = getTableNavigation().getLastButton();
      
      if (buttonToClick == null)
         return false;
      
      return buttonToClick.click();
   }   
   
   /**
    * table header stored object must be cleared when user wants to do compare
    * again
    * 
    */
   public void resetColumnHeadings()
   {
      columnHeaders = null;
   }   
   
   /**
    * Waits until the Table loads successfully.
    * 
    * @return <li><code>true</code> if the OldTable loads successfully.</li> <li>
    *         <code>false</code> if the OldTable does not loads successfully.</li>
    */
   public boolean waitForTableToLoad()
   {
      // wait for the OldTable spinner to disappear, according to dev the spinner is represented with 
      // <img class="gwt-Image" id=locator + -loadingIndicator> element  
      //return this.getBrowser().waitForNoElement(this.getLocator() + "-loadingIndicator");
      return (this.getBrowser().waitForElement(getField(1, 2).getLocator()) && this.getBrowser().waitForElement(
            getField(1, 3).getLocator()));
   }
   
   /**
    * If given lines(by checkbox) can be successfully selected for delete.
    * 
     * @param selectedLines
    *           The lines to be selected for delete, use format like "3,4,7,10" or "3"
    *           
    * @return Whether or not the given lines are selected successfully.
    *
    */
   public boolean selectLinesForDelete(final String selectedLines)
   {
      locatorCheckAndAdjust();
      String checkboxLocator;
      
      if(selectedLines.contains(","))
      {
         String[] lines = selectedLines.split(",");
         for(int i=0; i< lines.length ; i++)
         {
            int line = Integer.parseInt(lines[i]);
            if (line >=1 && line <=10 )
            {
               checkboxLocator =  dataBodyLocator + "/tr[" + line + "]/td[1]/span/input";
               this.getBrowser().selectCheckBox(checkboxLocator);
               if(! this.getBrowser().isSelected(checkboxLocator))
                  return false;
            }
         }
         return true;
      }else //a given line number, should not greater than 10
      {
         int line = Integer.parseInt(selectedLines);
         if (line >=1 && line <=10 )
         {
            checkboxLocator =  dataBodyLocator + "/tr[" + line + "]/td[1]/span/input";
            this.getBrowser().selectCheckBox(checkboxLocator);
            return (this.getBrowser().isSelected(checkboxLocator)) ? true:false;
         }else
            return false;
      }
   } 
   
   /**
    * If given lines(by checkbox) can be successfully unselected for delete.
    * 
     * @param unselectedLines
    *           The lines to be unselected for delete, use format like "3,4,7,10" or "3"
    *           
    * @return Whether or not the given lines are unselected successfully.
    *
    */
   public boolean unselectLinesForDelete(final String unselectedLines)
   {
      locatorCheckAndAdjust();
      String checkboxLocator;
      
      if(unselectedLines.contains(","))
      {
         String[] lines = unselectedLines.split(",");
         for(int i=0; i< lines.length ; i++)
         {
            int line = Integer.parseInt(lines[i]);
            if (line >=1 && line <=10 )
            {
               checkboxLocator =  dataBodyLocator + "/tr[" + line + "]/td[1]/span/input";   
               if (this.getBrowser().isSelected(checkboxLocator))
                  this.getBrowser().click(checkboxLocator);
               if(this.getBrowser().isSelected(checkboxLocator))
                  return false;               
            }
         }
         return true;
      }else //a given line number, should not greater than 10
      {
         int line = Integer.parseInt(unselectedLines);
         if (line >=1 && line <=10 )
         {
            checkboxLocator =  dataBodyLocator + "/tr[" + line + "]/td[1]/span/input";
            if (this.getBrowser().isSelected(checkboxLocator))
               this.getBrowser().click(checkboxLocator);
            return (this.getBrowser().isSelected(checkboxLocator)) ? false:true;
         }else
            return false;
      }
   } 
   
   /**
    * Check if all the lines shown in current page is checked for delete.
    * 
    * @return Whether all the lines shown in current page is checked for delete.
    */
   public boolean isAllChecked()
   {
      locatorCheckAndAdjust();
      String checkboxLocator;
      
      int itemsOnCurrentPage = getItemsOnCurrentPage();
      
      if(itemsOnCurrentPage > 0)
      {   
         for (int i=1; i<itemsOnCurrentPage+1; i++)
         {
            checkboxLocator =  dataBodyLocator + "/tr[" + i + "]/td[1]/span/input";
            if(! this.getBrowser().isSelected(checkboxLocator))
            {
               return false;
            }
         }
         return true;
      }else //No items to display;
      {
         return true;
      }
   } 
   
   
   /**
    * Check if all the lines shown in current page is unchecked.
    * 
    * @return Whether all the lines shown in current page is unchecked.
    */
   public boolean isAllUnChecked()
   {
      locatorCheckAndAdjust();
      String checkboxLocator;
      
      int itemsOnCurrentPage = getItemsOnCurrentPage();
      if(itemsOnCurrentPage > 0)
      {   
         for (int i=1; i<itemsOnCurrentPage+1; i++)
         {
            checkboxLocator =  dataBodyLocator + "/tr[" + i + "]/td[1]/span/input";
            if(this.getBrowser().isSelected(checkboxLocator))
            {
               return false;
            }
         }
         return true;
      }else //No items to display;
      {
         return true;
      }
   } 
   
   /**
    * {@inheritDoc}
    */
   public int getItemsOnCurrentPage()
   {      
      String itemsOutput = getTableNavigation().getPageItems().getText().trim();
      // For tables in a normal screens other than a finder page, the rows number of the table is 10.
      int itemsPerPage = 10;
      // For tables in a finder page, the row number is 5.
      if (this.getLocator().contains("div_finder_grid"))
         itemsPerPage = 5;

      if(itemsOutput.contains("-"))//like "31 - 31 of 31 items"
      {
         String[] items = itemsOutput.split("\\-");
         String[] items2 = items[1].trim().split("of");
         String itemsTillCurrentPage = items2[0].trim();   

         int itemsOnCurrentPage = Integer.parseInt(itemsTillCurrentPage) % itemsPerPage;
         if(itemsOnCurrentPage ==0)
         {
            itemsOnCurrentPage = itemsPerPage;
         }
         return itemsOnCurrentPage;
      }else //No items to display
      {
         return 0;
      }
   }
   
   /**
    * {@inheritDoc}
    */
   public int getTotalItems()
   {
      
      String itemsOutput = getTableNavigation().getPageItems().getText().trim();
      if (itemsOutput.contains("-")) //format like "1 - 10 of 31 items"
      {
         String[] items = itemsOutput.split("\\-");
         String[] items2 = items[1].trim().split("of");
         String totalItems = items2[1].replace("items", "").trim();
         return Integer.parseInt(totalItems);
      }else //No items to display
      {
         return 0; 
      }
   } 
   
   /**
    * {@inheritDoc}
    */
   public int getCheckedItemsToDelete()
   {
      locatorCheckAndAdjust();
      String checkboxLocator;
      int checkedNum =0;
      
      String itemsOutput = getTableNavigation().getPageItems().getText().trim();
   
      if(itemsOutput.contains("-"))//like "31 - 31 of 31 items"
      {
         String[] items = itemsOutput.split("\\-");
         String[] items2 = items[1].trim().split("of");
         String itemsTillCurrentPage = items2[0].trim();
         
         int itemsOnCurrentPage = Integer.parseInt(itemsTillCurrentPage) % 10;
         
         if((Integer.parseInt(itemsTillCurrentPage) >0 ) && ( itemsOnCurrentPage ==0))
         {
            itemsOnCurrentPage = 10;
         }            
         for (int i=1; i<itemsOnCurrentPage+1; i++)
         {
            checkboxLocator =  dataBodyLocator + "/tr[" + i + "]/td[1]/span/input";
            if( this.getBrowser().isSelected(checkboxLocator))
            {
               checkedNum = checkedNum +1;
            }
         }
         return checkedNum;
      }else //No items to display
      {
         return 0;
      }
   } 
   

}
