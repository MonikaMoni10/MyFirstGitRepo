/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.fixture.widget;

import com.sonata.generic.automation.browser.Browser;
import com.sonata.generic.automation.widgets.OnePageTable;
import com.sonata.generic.automation.widgets.Widget;

/**
 * A fixture-friendly representation of a table.
 */
public final class FixtureOnePageTable extends AbstractFixtureWidget
{
   private static final FixtureWidgetCreator CREATOR = new FixtureOnePageTableCreator();
   private final OnePageTable                       table;

   /**
    * Constructs a fixture-friendly representation of a table. This constructor
    * is private and only available to the {@link FixtureWidgetCreator}.
    * 
    * @param widgetName
    *           The descriptive name of the widget, as used by callers of the
    *           fixture (such as the FitNesse test page) to identify the widget
    *           on which an action should be performed.
    * @param widgetID
    *           The underlying widget ID (as specified in the declarative UI
    *           definition) that is used to calculate the widget's automation
    *           locator.
    * @param idBase
    *           The debug ID prefix that is used to calculate the widget's
    *           automation locator.
    * @param browser
    *           The automation {@link Browser} object that the widget will run
    *           in.
    * 
    * @throws IllegalArgumentException
    *            One or more arguments are null or empty.
    */
   private FixtureOnePageTable(final String widgetName, final String widgetID, final String idBase, final Browser browser)
   {
      super(widgetName, widgetID, idBase, browser);
      this.table = new OnePageTable(getLocator(), browser);
   }

   /**
    * The {@link FixtureWidgetCreator} that is used to create instances of this
    * type of {@link FixtureWidget}.
    */
   private static class FixtureOnePageTableCreator implements FixtureWidgetCreator
   {
      /**
       * {@inheritDoc}
       */
      @Override
      public ModifiableFixtureWidget createFixtureWidget(final String widgetName, final String widgetID,
            final String idBase, final Browser browser)
      {
         return new FixtureOnePageTable(widgetName, widgetID, idBase, browser);
      }
   }

   /**
    * Gets the {@link FixtureWidgetCreator} that is used to create instances of
    * this type of {@link FixtureWidget}.
    * 
    * @return The {@link FixtureWidgetCreator} that is used to create instances
    *         of this type of {@link FixtureWidget}.
    */
   public static FixtureWidgetCreator getFixtureWidgetCreator()
   {
      return CREATOR;
   }

   /**
    * Gets the SWT widget type (as listed in the declarative UI definition) of
    * the kind of widget represented by this {@link FixtureWidget}.
    * 
    * @return The SWT widget type (as listed in the declarative UI definition)
    *         of the kind of widget represented by this {@link FixtureWidget}.
    */
   public static String getSwtWidgetType()
   {
      return "genericOnePageTable";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected String getFriendlyWidgetType()
   {
      return "OnePageTable";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected Widget getAutomationWidget()
   {
      return table;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean clickCellByColumnId(final String columnId, final int rowIndex)
   {
      int columnIdx = table.getIndexOfColumnById(columnId);
      
      return this.clickCell(rowIndex, columnIdx);
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean clickCell(final String columnName, final int rowIndex)
   {      
      int columnIdx = table.getIndexOfColumn(columnName);
      
      return this.clickCell(rowIndex, columnIdx);
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean clickCell(final int rowIndex, final int columnIdx)
   {
      return table.getField(rowIndex, columnIdx).click();
   }
   
   /**
    * Used for a cell which is binded with a click() function in app source code.
    * this.clickCell uses click() provided by selenium, only working on Chrome, not on IE and FireFox. 
    * Using "click()" will lose focus occasionally, especially for a cell.
    * Changed to javaScript which is more reliable 
    * 
    * @param rowIndex
    *           The index of the row where the cell is located.
    * @param columnIdx
    *           The index of the column where the cell is located
    * 
    * @return Whether or not the cell is successfully clicked.
    */
   private boolean clickCellByJavaScript(final int rowIndex, final int columnIdx)
   {
      // Click the cell to activate the inner element (a textbox, or whatever it is)
      
      // Just use clickCell(), sometimes it will lose focus on certain browser
      // Just use click() of javascript, it will clear the previous cell's content if it is not tab out
      // Use both clickCell() and click() of javascript to make sure the cell is reliably activated.
      this.clickCell(rowIndex, columnIdx);
      
      int rowIdxInJavaScript = rowIndex -1;
      int columIdxInJavaScript = columnIdx -1; 
      // For tables in Journal Entry, Recurring Entries, Optional Fields and Accounts
      String locator1 = "//div[@id='" + this.getLocator() + "']/div[@class='k-grid-content']/table/tbody/tr[" + rowIndex + "]/td[" + columnIdx + "]";
      // For tables in Options
      String locator2 = "//div[@id='" + this.getLocator() + "']/table/tbody/tr[" + rowIndex + "]/td[" + columnIdx + "]";  
      String javascript =null;
      if (table.getBrowser().existsNoWait(locator1))
      {
         javascript = "$(\"div#" + this.getLocator() + " > div.k-grid-content > table > tbody > tr:eq(" + rowIdxInJavaScript + ") > td:eq("+ columIdxInJavaScript + ")\").click();";
      }
      else if (table.getBrowser().exists(locator2))
      {
         javascript = "$(\"div#" + this.getLocator() + " > table > tbody > tr:eq(" + rowIdxInJavaScript + ") > td:eq("+ columIdxInJavaScript + ")\").click();";
      }
      if (javascript != null)
      {
         this.getBrowser().executeJavaScriptReturnString(javascript);
         return true;
      }
      else
         return false;
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean clickFinderInCell(final String columnName, final int rowIndex)
   {
      int columnIdx = table.getIndexOfColumn(columnName);
      
      // click the cell to activate the inner element (a textbox, or whatever it is)
      if(this.clickCellByJavaScript(rowIndex, columnIdx))
      {
         return table.getFinderButtonField(columnName, rowIndex).click();
      }
      else
         return false;
   }   
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean clickFinderInCell(final int rowIndex, final int columnIdx)
   {
      // click the cell to activate the inner element (a textbox, or whatever it is)
      if(this.clickCellByJavaScript(rowIndex, columnIdx))
      {
         return table.getFinderButtonField(rowIndex, columnIdx).click();
      }
      else
         return false;
   } 
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean clickPencilButtonInCell(final int rowIndex, final int columnIdx)
   {
      // click the cell to activate the inner element (a textbox, or whatever it is)
      if(this.clickCellByJavaScript(rowIndex, columnIdx))
      {
         return table.getPencilButtonField(rowIndex, columnIdx).click();
      }
      else
         return false;
   }
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean deleteRow(final int rowIndex)
   {
      return table.deleteRow(rowIndex);
   }   

   /**
    * {@inheritDoc}
    */
   public int getCellRowIdxByValue(final int columnIdx, final String value)
   {
      return table.getCellRowIdxByValue(columnIdx, value);
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public String getCellText(final String columnName, final int rowIndex)
   {
      if(table.getField(columnName, rowIndex) != null)
      {
         return table.getField(columnName, rowIndex).getText();
      }
      else
         return null;
   }
   

   /**
    * {@inheritDoc}
    */
   @Override
   public String getCellText(final int rowIndex, final int columnIdx)
   {
      if(table.getField(rowIndex, columnIdx) != null )
      {
         return table.getField(rowIndex, columnIdx).getText();
      }
      else
         return null;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getCurrentPage()
   {
      return table.getCurrentPage();
   }   
   
   /**
    * {@inheritDoc}
    */
   @Override
   public String getHeaderCellText(final int columnIndex)
   {
      return table.getColumnNameByIndex(columnIndex);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getSelectedCell(final String columnName, int rowIndex)
   {
      return table.getField(columnName, rowIndex).getSelectedValue();
   }
 
   /**
    * Click the first cell in a line to be selected or unselected, the purpose is just
    * to make the table scroll back to its full left side.
    * This is a work around to solve the problem that the delete lines checkbox can't be 
    * selected if the table is not fully shown from the left side. After click the first 
    * cell, the scroll bar of the table will move back to it's original status, full left 
    * side, then the checkbox can be correctly located.
    * @param selectedLines
    *           The line number to be selected, format as: "2,3,6" or "2"
    * @return whether the first cell of the select or unselected line is successfully clicked.          
    */
   private boolean moveToTheLeftSideofTable(final String selectedLines)
   {
      String[] lines = selectedLines.split(",");
      int firstlineNum = Integer.parseInt(lines[0]);
      return table.getField(firstlineNum, 1).click();
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean selectLinesForDelete(final String selectedLines)
   {
      this.moveToTheLeftSideofTable(selectedLines);
      return table.selectLinesForDelete(selectedLines);
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean unselectLinesForDelete(final String unselectedLines)
   {
      this.moveToTheLeftSideofTable(unselectedLines);
      return table.unselectLinesForDelete(unselectedLines);
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean isAllChecked()
   {
      return table.isAllChecked();
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean isAllUnChecked()
   {
      return table.isAllUnChecked();
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public int getTotalItems()
   {
      return table.getTotalItems();
   } 
   
   /**
    * {@inheritDoc}
    */
   @Override
   public int getCheckedItemsToDelete()
   {
      return table.getCheckedItemsToDelete();
   } 

   /**
    * {@inheritDoc}
    */
   @Override
   public int getIndexOfColumnById(final String columnId)
   {
      return table.getIndexOfColumnById(columnId);
   }  
   
   /**
    * {@inheritDoc}
    */
   @Override
   public String getTotalPages()
   {
      return table.getTotalPages();
   }   
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean goToPage(final String page)
   {
      return table.goToPage(page);
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public boolean isCellDataPresent(final int columnIndex, final int rowIndex)
   {
      return table.isCellDataPresent(columnIndex, rowIndex);
   }
 
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean isDisabled()
   {
      return table.isDisabled();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean navigate(final String action)
   {
      if ((null == action) || action.isEmpty())
      {
         throw new IllegalArgumentException("The navigation action must be non-empty.");
      }

      return table.navigate(action);
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean selectFromCell(final String columnName, final int rowIndex, final String value)
   {
      int columnIdx = table.getIndexOfColumn(columnName);
      
      // click the cell to activate the inner element (a textbox, or whatever it is)
      if ( this.clickCellByJavaScript(rowIndex, columnIdx))  
      {      
         table.getComboBoxField(columnName, rowIndex).selectComboBox(value, true);      
         return (this.getCellText(columnName, rowIndex).equals(value)) ? true: false;
      }
      else 
         return false;
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean selectFromCell(final int rowIndex, final int columnIdx, final String value)
   {
      // click the cell to activate the inner element (a textbox, or whatever it is)
      if (this.clickCellByJavaScript(rowIndex, columnIdx))
      {   
         table.getComboBoxField(rowIndex, columnIdx).selectComboBox(value, true);      
         return (this.getCellText(rowIndex, columnIdx).equals(value)) ? true: false;
      }
      else
         return false;
   }

  
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean typeIntoCell(final String columnName, final int rowIndex, final String value)
   {
      int columnIdx = table.getIndexOfColumn(columnName);
      
      // click the cell to activate the inner element (a textbox, or whatever it is)
      if(this.clickCellByJavaScript(rowIndex, columnIdx))
      {
         return table.getTextBoxField(columnName, rowIndex).typeIntoCell(value, true);
      }
      else
         return false;
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean typeIntoCellByColumnIdx(final int rowIndex, final int columnIdx, final String value)
   {
      // click the cell to activate the inner element (a textbox, or whatever it is)
      if(this.clickCellByJavaScript(rowIndex, columnIdx))
      {
         return table.getTextBoxField(rowIndex, columnIdx).typeIntoCell(value, true);
      }
      else
         return false;
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean typeIntoCellWithoutTab(final int rowIndex, final int columnIdx, final String value)
   {
      // click the cell to activate the inner element (a textbox, or whatever it is)
      if(this.clickCellByJavaScript(rowIndex, columnIdx))
      {
         return table.getTextBoxField(rowIndex, columnIdx).typeIntoCell(value, false);
      }
      else
         return false;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean waitForCellContent(final String columnName, final int rowIndex, final String content)
   {
      return table.getField(columnName, rowIndex).waitForContent(content);
   }      
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean waitForTableToLoad()
   {
      return table.waitForTableToLoad();
   }
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean isColumnHidden(final int columnIdx)
   {
      return table.isColumnHidden(columnIdx);
   }

}
