/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.fixture.widget;

import java.util.HashMap;
import java.util.Map;

import com.sonata.generic.automation.browser.Browser;
import com.sonata.generic.automation.widgets.Widget;

/**
 * <p>
 * Base class for a fixture widget. This base class is based on the concept of
 * an adapter. For all actions available to one or more widget types, it
 * provides a default implementation that throws an exception indicating that
 * the widget does not support that action. It also provides a default
 * implementation for the locator.
 * </p>
 * 
 * <p>
 * A subclass representing a type of widget (such as a button) would override
 * the methods for the actions that it supports, while leaving the base class to
 * throw an exception on actions that it doesn't support.
 * </p>
 */
public abstract class AbstractFixtureWidget implements ModifiableFixtureWidget
{
   private final String                     widgetName;
   private final Browser                    browser;
   private final String                     locator;
   private FixtureWidget                    parent;
   private final Map<String, FixtureWidget> children;

   /**
    * Base class constructor for a fixture-friendly representation of a widget.
    * 
    * @param widgetName
    *           The descriptive name of the widget, as used by callers of the
    *           fixture (such as the FitNesse test page) to identify the widget
    *           on which an action should be performed.
    * @param widgetID
    *           The underlying widget ID (as specified in the declarative UI
    *           definition) that is used in this widget's automation locator.
    * @param idBase
    *           The form-wide ID base (prefix) used in automation locators for
    *           widgets belonging to the same form.
    * @param browser
    *           The automation {@link Browser} object that the widget will run
    *           in.
    * 
    * @throws IllegalArgumentException
    *            One or more arguments are null or empty.
    */
   protected AbstractFixtureWidget(final String widgetName, final String widgetID, final String idBase,
         final Browser browser)
   {
      if ((null == widgetName) || widgetName.isEmpty())
         throw new IllegalArgumentException("The widget name must be non-empty.");
      if ((null == widgetID) || widgetID.isEmpty())
         throw new IllegalArgumentException("The widget ID must be non-empty.");
      if ((null == idBase) || idBase.isEmpty())
         throw new IllegalArgumentException("The form-wide ID base must be non-empty.");
      if (null == browser)
         throw new IllegalArgumentException("The automation browser object must be non-null.");

      this.widgetName = widgetName;
      this.browser = browser;
      this.locator = createLocator(widgetID, idBase);
      this.parent = null; // and will stay null for top-level widgets of a form
      this.children = new HashMap<String, FixtureWidget>();
   }

   /**
    * <p>
    * Creates the automation locator (basically the HTML ID) for the widget
    * based on its widget ID and the ID base (prefix) for all IDs on that form.
    * </p>
    * 
    * <p>
    * NOTE: This method never returns null.
    * </p>
    * 
    * @param widgetID
    *           The underlying widget ID (as specified in the declarative UI
    *           definition) that is used in the widget's automation locator.
    * @param idBase
    *           The form-wide ID base (prefix) used in automation locators for
    *           widgets belonging to the same form.
    * 
    * @return The widget's automation locator.
    * 
    * @throws IllegalArgumentException
    *            One or more arguments are null or empty.
    */
   private static String createLocator(final String widgetID, final String idBase)
   {
      if ((null == widgetID) || widgetID.isEmpty())
         throw new IllegalArgumentException("The widget ID must be non-empty.");
      if ((null == idBase) || idBase.isEmpty())
         throw new IllegalArgumentException("The form-wide ID base must be non-empty.");

      StringBuilder sbLocator = new StringBuilder();
      //sbLocator.append(idBase);
      sbLocator.append(widgetID);

      return sbLocator.toString();
   }

   /**
    * Gets the "friendly" type name of the widget for use in messages (such as
    * the exception message indicating that the widget does not support the
    * action in question).
    * 
    * @return The "friendly" type name of the widget for use in messages.
    */
   protected abstract String getFriendlyWidgetType();

   /**
    * {@inheritDoc}
    */
   @Override
   public String getHeaderCellText(final int columnIndex)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("getHeaderCellText"));
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public int getIndexOfColumnById(final String columnId)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("getIndexOfColumnById"));
   }

   

   /**
    * <p>
    * Gets the underlying automation widget that this fixture widget is
    * wrapping.
    * </p>
    * 
    * <p>
    * NOTE: Implementations must never return null.
    * </p>
    * 
    * @return The underlying automation widget that this fixture widget is
    *         wrapping.
    */
   protected abstract Widget getAutomationWidget();

   /**
    * <p>
    * Gets the descriptive name of the widget.
    * </p>
    * 
    * <p>
    * NOTE: This method never returns null.
    * </p>
    * 
    * @return The descriptive name of the widget.
    */
   protected String getWidgetName()
   {
      return widgetName;
   }

   /**
    * Gets the automation locator for the widget. By default this is the locator
    * (HTML ID) of the widget itself, but subclasses may choose to override this
    * method and specify the locator of a component of the widget.
    * 
    * @return The automation locator for the widget.
    */
   protected String getLocator()
   {
      return locator;
   }

   /**
    * <p>
    * Gets the automation {@link Browser} object that the widget will run in.
    * </p>
    * 
    * <p>
    * NOTE: This method never returns null.
    * </p>
    * 
    * @return The automation {@link Browser} object that the widget will run in.
    */
   protected Browser getBrowser()
   {
      return browser;
   }

   /**
    * Gets the non-null version of {@link #getFriendlyWidgetType()
    * getFriendlyWidgetType}, where this will return "" if the subclass
    * implementation of {@code getFriendlyWidgetType} is null.
    * 
    * @return The value of {@link #getFriendlyWidgetType()
    *         getFriendlyWidgetType} as returned by the subclass implementation,
    *         or "" if that value is null.
    */
   private String getSafeFriendlyWidgetType()
   {
      String friendlyWidgetType = getFriendlyWidgetType();
      return (null != friendlyWidgetType) ? friendlyWidgetType : "";
   }

   /**
    * Builds the exception message stating that this widget does not support the
    * specified action.
    * 
    * @param action
    *           The friendly name of the action that was attempted on the
    *           widget.
    * @return The exception message stating that this widget does not support
    *         the specified action.
    */
   private String buildActionExceptionMessage(final String action)
   {
      StringBuilder sbExceptionMsg = new StringBuilder();
      sbExceptionMsg.append("'");
      sbExceptionMsg.append(getWidgetName());
      sbExceptionMsg.append("' is a '");
      sbExceptionMsg.append(getSafeFriendlyWidgetType());
      sbExceptionMsg.append("' which does not support '");
      sbExceptionMsg.append(action);
      sbExceptionMsg.append("'");

      return sbExceptionMsg.toString();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public FixtureWidget getParent()
   {
      return parent;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void setParent(final FixtureWidget parent)
   {
      // NOTE: The passed-in parent may be null.  A null parent means that
      //       this is a top-level fixture widget of the form.
      this.parent = parent;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Map<String, FixtureWidget> getChildren()
   {
      return children;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void addChild(final String childWidgetName, final FixtureWidget child)
   {
      if ((null == childWidgetName) || childWidgetName.isEmpty())
         throw new IllegalArgumentException("The child widget name must be non-empty.");
      if (null == child)
         throw new IllegalArgumentException("The child fixture widget must be non-null.");
      if (children.containsKey(childWidgetName))
         throw new IllegalArgumentException("This fixture widget already contains a child widget named '"
               + childWidgetName + "'.");

      children.put(childWidgetName, child);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getWaitTargetLocator()
   {
      return getLocator();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean check()
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("check"));
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean selectLinesForDelete(final String selectedLines)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("selectLinesForDelete"));
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean unselectLinesForDelete(final String unselectedLines)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("unselectLinesForDelete"));
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean isAllChecked()
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("isAllChecked"));
   } 
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean isAllUnChecked()
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("isAllUnChecked"));
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public int getTotalItems()
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("getTotalItems"));
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public int getCheckedItemsToDelete()
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("getCheckedItemsToDelete"));
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean checkByLabel()
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("checkByLabel"));
   }   
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean clear()
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("clear"));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean clearAndValidate(final String defaultValue)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("clearAndValidate"));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean click()
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("click"));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean clickByReturn()
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("clickByReturn"));
   }
      
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean clickCellByColumnId(final String columnId, final int rowIndex)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("clickCellByColumnId"));
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean clickCell(final String columnName, final int rowIndex)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("clickCell"));
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean clickCell(final int rowIndex, final int columnIdx)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("clickCell"));
   }

   /**
    * {@inheritDoc}
    */
   @Override   
   public boolean clickFinderInCell(String columnName, int rowIndex)      
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("clickFinderInCell"));
   }
   
   /**
    * {@inheritDoc}
    */
   @Override   
   public boolean clickFinderInCell(int rowIndex, int columnIdx)      
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("clickFinderInCell"));
   }
   
   /**
    * {@inheritDoc}
    */
   @Override   
   public boolean clickPencilButtonInCell(int rowIndex, int columnIdx)      
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("clickPencilButtonInCell"));
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean close()
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("close"));
   }

   /**
    * {@inheritDoc}
    */
   @Override   
   public boolean deleteRow(final int rowIndex)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("deleteRow"));
   }   
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean exists()
   {
      Widget autoWidget = getAutomationWidget();
      return autoWidget.exists();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean fieldExists(final String fieldName)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("fieldExists"));
   }

   /**
    * {@inheritDoc}
    */
   public int getCellRowIdxByValue(final int columnIdx, final String value)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("getCellRowIdxByValue"));
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public String getCellText(final String columnName, final int rowIndex)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("getCellText"));
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public String getCellText(final int rowIndex, final int columnIdx)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("getCellText"));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getCellTextByColumnIndex(final int columnIndex, final int rowIndex)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("getAdhocCellTextByColumnIndex"));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean waitForCellContent(final String columnName, final int rowIndex, final String content)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("waitForCellContent"));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getCurrentPage()
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("getCurrentPage"));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String[] getAllOptions()
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("getAllOptions"));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String[] getSelectedOptions()
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("getSelectedOptions"));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public int getRowCount()
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("getRowCount"));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getText()
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("getText"));
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public String getPlaceHolderText()
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("getPlaceHolderText"));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getTotalPages()
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("getTotalPages"));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean goToPage(final String page)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("goToPage"));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean isCellDataPresent(final int columnIndex, final int rowIndex)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("isCellDataPresent"));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean isChecked()
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("isSelected"));
   }   
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean isDisabled()
   {
      Widget autoWidget = getAutomationWidget();
      return autoWidget.isDisabled();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean isEditable()
   {
      Widget autoWidget = getAutomationWidget();
      return autoWidget.isEditable();
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public boolean isOpen()
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("isOpen"));
   }


   /**
    * 
    * {@inheritDoc}
    */
   @Override
   public boolean isSelected(final String radioButtonLabel)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("isSelected"));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean isCellEditable(final String columnName, final int rowIndex)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("isCellEditable"));
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean isSelected()
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("isSelected"));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean isVisible()
   {
      Widget autoWidget = getAutomationWidget();
      return autoWidget.isVisible();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean isColumnHidden(final int columnIdx)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("isColumnHidden"));
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public void hover()
   {
      Widget autoWidget = getAutomationWidget();
      autoWidget.hover();
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean navigate(final String action)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("navigate"));
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean open()
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("open"));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean pressInCell(final String key, final String columnName, final int rowIndex)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("pressInCell"));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean pressInRow(final String key, final int rowIndex)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("pressInRow"));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean select()
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("select"));
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean selectOption(final String option)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("selectOption"));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean type(final String value)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("type"));
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean typeTelephoneNumber(final String value)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("typeTelephoneNumber"));
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean typeByJavaScript(final String value)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("typeByJavaScript"));
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean typeNoTab(final String value)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("typeNoTab"));
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean typeWithCtrlADel(final String value)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("typeWithCtrlADel"));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean typeWithoutWaitForClicable(final String value)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("typeWithoutWaitForClicable"));
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean pressTab()
   {
      Widget autoWidget = getAutomationWidget();
      return autoWidget.pressTab();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean typeWithoutTab(final String value)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("typeWithoutTab"));
   } 
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean typeIntoCell(final String columnName, final int rowIndex, final String value)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("typeIntoCell"));
   } 
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean typeIntoCellByColumnIdx(final int rowIndex, final int columnIdx, final String value)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("typeIntoCellByColumnIdx"));
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean typeIntoCellWithoutTab(final int rowIndex, final int columnIdx, final String value)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("typeIntoCellWithoutTab"));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean typeWithoutClear(final String value)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("typeWithoutClear"));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean typeWithoutClearAndWithoutTab(final String value)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("typeWithoutClearAndWithoutTab"));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean uncheck()
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("uncheck"));
   }   
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean waitFor()
   {
      return browser.waitForElement(getWaitTargetLocator());
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean waitForNotVisible()
   {
      return browser.waitForNoElement(getWaitTargetLocator());
   }
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean waitUntilDisabled()
   {
      Widget autoWidget = getAutomationWidget();
      return autoWidget.waitUntilDisabled();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean waitUntilEnabled()
   {
      Widget autoWidget = getAutomationWidget();
      return autoWidget.waitUntilEnabled();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean waitForContent(final String content)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("waitForContent"));
   }


   /**
    * 
    * {@inheritDoc}
    */
   public boolean selectFieldOption(String optionValue)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("selectFieldOption"));
   }


   /**
    * 
    * {@inheritDoc}
    */
   @Override
   public boolean selectFromCell(String columnName, int rowIndex, String value)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("selectFromCell"));
   }
   
   /**
    * 
    * {@inheritDoc}
    */
   @Override
   public boolean selectFromCell(int rowIndex, int columnIdx, String value)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("selectFromCell"));
   }
   
   /**
    * 
    * {@inheritDoc}
    */
   @Override
   public boolean selectFromCellWithoutValidation(int rowIndex, int columnIdx, String value)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("selectFromCellWithoutValidation"));
   }

 
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean clickCalendar()
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("clickCalendar"));
   }



   /**
    * {@inheritDoc}
    */
   @Override
   public String getSelectedCell(final String columnName, int rowIndex)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("getSelectedCell"));
   }


   /**
    * 
    * {@inheritDoc}
    */
   @Override
   public boolean select(final String option)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("select"));
   }
   
   /**
    * 
    * {@inheritDoc}
    */
   @Override
   public boolean selectByIdx(final int index)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("selectByIdx"));
   }
   /**
    * 
    * {@inheritDoc}
    */
   @Override
   public boolean deselect(final String option)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("deselect"));
   }
   
   /**
    * 
    * {@inheritDoc}
    */
   @Override
   public boolean deselectAll()
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("deselectAll"));
   }
   
   /**
    * 
    * {@inheritDoc}
    */
   @Override
   public boolean selectAll()
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("selectAll"));
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean selectCalendarDate(String date)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("selectCalendarDate"));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean waitForTableToLoad()
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("waitForTableToLoad"));
   }   
   
   /**
    * ============================================================
    * Sikuli related actions.
    */
   
   /**
    * {@inheritDoc}
    */   
   @Override
   public boolean clickImage()
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("clickImage"));
   }

   /**
    * {@inheritDoc}
    */   
   @Override
   public boolean selectMenuItemFromImage(String menuTitle, String menuItem)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("selectMenuItemFromImage"));
   }   
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean typeIntoImage(final String value)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("typeIntoImage"));
   }      
   

   /**
    * {@inheritDoc}
    */   
   @Override
   public boolean waitForImage()
   {
      Widget autoWidget = getAutomationWidget();
      return autoWidget.waitForImage();
   }    
   /**
    * {@inheritDoc}
    */   
   @Override
   public boolean waitForImage(int timeout)
   {
      Widget autoWidget = getAutomationWidget();
      return autoWidget.waitForImage(timeout);
   }
   
   
   /**
    * {@inheritDoc}
    */  
   public boolean doubleClickImage()
   {      
     throw new IllegalArgumentException(buildActionExceptionMessage("doubleClickImage"));      
   }
   
   /**
    * {@inheritDoc}
    */  
   public boolean rightClickImage()
   {      
     throw new IllegalArgumentException(buildActionExceptionMessage("rightClickImage"));      
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean pressKeySequence(String keyToHold, String charToPress)
   {
      Widget autoWidget = getAutomationWidget();
      return autoWidget.pressKeySequence(keyToHold, charToPress);
   }
}
