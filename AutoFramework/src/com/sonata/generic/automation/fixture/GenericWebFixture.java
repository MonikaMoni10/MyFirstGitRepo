/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.fixture;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.SessionNotFoundException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sonata.generic.automation.browser.Browser;
import com.sonata.generic.automation.browser.StopTestException;
import com.sonata.generic.automation.fixture.browser.BrowserTiming;
import com.sonata.generic.automation.fixture.browser.DefaultBrowserTiming;
import com.sonata.generic.automation.fixture.configuration.ConfigurationParser;
import com.sonata.generic.automation.fixture.configuration.ConfigurationParserFactory;
import com.sonata.generic.automation.fixture.configuration.DefaultConfigurationParserFactory;
import com.sonata.generic.automation.fixture.configuration.FixtureProperties;
import com.sonata.generic.automation.fixture.widget.FixtureWidget;
import com.sonata.generic.automation.library.FileHelper;
import com.sonata.generic.automation.library.LoggingHelper;
import com.sonata.generic.automation.library.TimeDelay;
import com.sonata.generic.automation.uibase.UI;

import static org.junit.Assert.*;


/**
 * <p>
 * This is the generic FitNesse fixture for a CNA2.0 UI. It exposes all possible
 * {@link FixtureWidget} actions as well as {@link UI} actions (such as signing
 * on to a UI) and {@link Browser} actions (such as waiting for a widget to
 * appear).
 * </p>
 * 
 * <p>
 * Here is an example of a SLIM-style FitNesse test that uses the fixture:
 * </p>
 * 
 * <pre>
 * |script|Generic Web Fixture|${FIXTURECONFIG_DIR}\ar1200.xml|browser is Firefox, server is localhost, port is 80|
 * |ensure|open;|
 * |ensure|click;|newVendor|
 * |ensure|close;|
 * </pre>
 * 
 * <p>
 * <b>FitNesse Syntax Tips:</b>
 * </p>
 * 
 * <ul>
 * <li>General documentation on writing (SLIM) test tables is available at: <a
 * href="http://fitnesse.org/FitNesse.UserGuide.SliM.ScriptTable"
 * >http://fitnesse.org/FitNesse.UserGuide.SliM.ScriptTable</a></li>
 * <li>General documentation on FitNesse wiki markup syntax is available at: <a
 * href="http://fitnesse.org/FitNesse.UserGuide.MarkupLanguageReference"
 * >http://fitnesse.org/FitNesse.UserGuide.MarkupLanguageReference</a>
 * <li>If you have a mixed case argument with no spaces between the words and
 * without two or more consecutive capital letters, you need to add special
 * markup so that the FitNesse wiki does not treat it as a wiki link. For
 * example, an argument of "MyMixedCaseText" needs to be specified as
 * {@code !-MyMixedCaseText-!} so that it is treated as literal text instead of
 * a wiki link. For details, see: <a
 * href="http://fitnesse.org/FitNesse.UserGuide.MarkupLiteralText"
 * >http://fitnesse.org/FitNesse.UserGuide.MarkupLiteralText</a></li>
 * <li>
 * FitNesse allows you to alternate cells that have partial action names with
 * cells that have arguments. For example, you could write:
 * {@code |check|get|arDistributionCodesTEXTDESC|text|Retail Sales Revenue|}.
 * The fact that you can do so is also a problem because if you have a
 * single-word action name with two arguments, it will will get confused unless
 * you use a semicolon after the action name. For example, if you removed the
 * semi-colon after "navigate", following line would fail because FitNesse gets
 * "too smart" and tries to look for a "navigateLast" action with one argument:
 * {@code |ensure|navigate;|navigatorARRDCiddist|last|}.</li>
 * <li>It's good practice to put a semicolon after each action's name so that
 * FitNesse won't try to do something you don't expect.</li>
 * <li>Be careful about the case of the arguments: unlike action names (e.g. for
 * the isSelected action you can write {@code isSelected;}, {@code is selected;}
 * , or {@code is Selected;}), <b>arguments are case-sensitive</b>. This means
 * that if the widget is named {@code oeOrdersONHOLD}, you will get an error if
 * you write {@code OeOrdersONHOLD}.</li>
 */
public final class GenericWebFixture
{
   private final BrowserTiming browserTiming;
   private final String brwoserType;
   private FixtureProperties   properties;
   private FixtureUI     ui;
   private String              currentFormName;
   private ConfigurationParser parser;
   private String tenantInfo; 
   
   
   private static String           PATH              = new File("").getAbsolutePath();
   private static String           LAYOUT_MAP_DIR    = PATH + "\\LayoutMaps\\web\\";

   /**
    * <p>
    * Constructs a Generic Web Fixture for the UI whose fixture configuration is
    * contained in the file at the specified configuration path, and using an
    * automation {@link Browser} object with default settings.
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |script|Generic Web Fixture|${FIXTURECONFIG_DIR}\ar1200.xml|
    * </pre>
    * 
    * @param configurationPath
    *           The path to the file containing fixture configuration
    *           information for the UI (for example,
    *           "c:\fitnesse\layoutmaps\ar1200.xml").
    * 
    * @throws IllegalArgumentException
    *            The configuration path is null or invalid.
    */
   public GenericWebFixture(final String configurationPath)
   {
      this(configurationPath, null);
   }

   /**
    * <p>
    * Constructs a Generic Web Fixture for the UI whose fixture configuration is
    * contained in the file at the specified configuration path, and using an
    * automation {@link Browser} object with the specified settings.
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |script|Generic Web Fixture|${FIXTURECONFIG_DIR}\ar1200.xml|browser is Internet_Explorer, server is localhost, port is 80|
    * </pre>
    * 
    * @param configurationPath
    *           The path to the file containing fixture configuration
    *           information for the UI (for example,
    *           "c:\fitnesse\layoutmaps\ar1200.xml").
    *           
    * @param browserSettings
    *           The settings used for creating a specific kind of automation
    *           {@link Browser} object for running the UI, or null to create an
    *           automation {@code Browser} object with default settings. The
    *           specific browser can be one of the following options:
    *           "Internet_Explorer", "Firefox", or "Chrome".
    * 
    * @throws IllegalArgumentException
    *            The configuration path is null, or one or more arguments are
    *            invalid.
    */
   public GenericWebFixture(final String configurationPath, final String browserSettings)
   {
      this(configurationPath, browserSettings, new DefaultConfigurationParserFactory(), new DefaultBrowserTiming());
   }

   /**
    * <p>
    * Package-private constructor for facilitating unit tests.
    * </p>
    * 
    * <p>
    * Constructs a Generic Web Fixture for the UI whose fixture configuration is
    * contained in the file at the specified configuration path, and using the
    * specified configuration parser to process the data from that file.
    * </p>
    * 
    * @param configurationPath
    *           The path to the file containing fixture configuration
    *           information for the UI (for example,
    *           "c:\fitnesse\layoutmaps\ar1200.xml").
    * @param browserSettings
    *           The settings used for creating a specific kind of automation
    *           {@link Browser} object for running the UI, or null to create an
    *           automation {@code Browser} object with default settings.
    * @param configurationParserFactory
    *           The factory for creating a parser that transforms fixture
    *           configuration data into a {@link FixtureProperties} object that
    *           the fixture can use easily.
    * @param browserTiming
    *           The browser timing object for doing pauses and timeouts, among
    *           other things.
    * 
    * @throws IllegalArgumentException
    *            One or more arguments are null or invalid.
    */
   GenericWebFixture(final String configurationPath, final String browserSettings,
         final ConfigurationParserFactory configurationParserFactory, final BrowserTiming browserTiming)
   {
      if ((null == configurationPath) || configurationPath.isEmpty())
         throw new IllegalArgumentException("The configuration path must be non-empty.");
      if (null == configurationParserFactory)
         throw new IllegalArgumentException("The configuration parser factory must be non-null.");
      if (null == browserTiming)
         throw new IllegalArgumentException("The browser timing object must be non-null.");

      this.browserTiming = browserTiming;

      // Parse configuration data into the fixture properties member and use
      // that member to create the fixture UI member that drives the actual UI.
      parser = configurationParserFactory.createConfigurationParser(browserSettings);
      if (null == parser)
         throw new IllegalArgumentException(
               "A null configuration parser was created by the configuration parser factory.");

      this.properties = parser.parse(configurationPath);
      this.ui = new FixtureUI(this.properties);

      // The current form starts at the main UI (whose form name is "").
      this.currentFormName = "";
      this.brwoserType = this.getBrowserType(browserSettings);
   }
   
   /** 
    * Gets the browser type by extracting it from browserSettings.
    * @param browserSettings, with format like:
    *    "browser is CHROME,server is columbus20nastaging.sagenephos.com/GLREGL, port is 443"
    * @return browserType, one of below:
    *           INTERNET_EXPLORER/FIREFOX/CHROME
    */
   private String getBrowserType(String browserSettings)
   {
      String[] tmp = browserSettings.split(",");
      return tmp[0].substring(11);
   }

   /**
    * A concrete implementation of the {@link UI} base class (which is really a
    * concrete implementation that just happens to be marked as abstract).
    */
   private static class FixtureUI extends UI
   {
      /**
       * Constructs a fixture UI with the specified UI properties derived from
       * fixture configuration data and the specified automation {@link Browser}
       * object in which the UI will run.
       * 
       * @param properties
       *           UI properties derived from fixture configuration data for the
       *           UI being tested by the fixture instance.
       */
      public FixtureUI(final FixtureProperties properties)
      {
         super(properties, properties.getBrowser());
      }
   }

   /**
    * <p>
    * To allow for switching between layout maps within the same test.
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|change layout map;|c:\\ar5020.xml|
    * </pre>
    * 
    * @param configurationPath
    *           The layout map to switch to
    * 
    */   
   public void changeLayoutMap(final String configurationPath)
   {
      this.properties = parser.parse(configurationPath);
      this.pauseForSeconds(2);
   }
   
   /**
    * <p>
    * Sets the specified widget's state to being selected.
    * </p>
    * 
    * <p>
    * Applies to checkboxes
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|check;|onHold|
    * </pre>
    * 
    * @param widgetName
    *           The name of the widget in question.
    * 
    * @return Whether or not the widget's state was set to being selected.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget or the widget does not
    *            support this action.
    */
   public boolean check(final String widgetName)
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.check();
   }
   
   /**
    * <p>
    * Select the checkboxes in a table for delete later.
    * </p>
    * 
    * @param widgetName
    *           The name of the widget.
    * @param selectedLines
    *           The lines to be deleted, use format like "3,4,7,10", or a given line number "3"
    *           
    * @return Whether or not the given lines are successfully selected.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget or the widget does not
    *            support this action.
    */
   public boolean selectLinesForDelete(final String widgetName, final String selectedLines)
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.selectLinesForDelete(selectedLines);
   }
   
   /**
    * <p>
    * Select the checkboxes in a table for delete later.
    * </p>
    * 
    * @param widgetName
    *           The name of the widget.
    * @param unselectedLines
    *           The lines to be deleted, use format like "3,4,7,10", or a given line number "3"
    *           
    * @return Whether or not the given lines are successfully selected.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget or the widget does not
    *            support this action.
    */
   public boolean unselectLinesForDelete(final String widgetName, final String unselectedLines)
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.unselectLinesForDelete(unselectedLines);
   }
   
   /**
    * <p>
    * Check if all the checkboxes(delete indication) in a table are checked after
    * the checkbox in the table header is checked 
    * </p>
    * 
    * @param widgetName
    *           The name of the table.
    * 
    * @return Whether or not all the checkboxes in the table are selected.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget or the widget does not
    *            support this action.
    */
   public boolean isAllChecked(final String widgetName)
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.isAllChecked();
   } 
   
   
   /**
     * <p>
    * Check if all the checkboxes(delete indication) in a table are unchecked after
    * the checkbox in the table header is unchecked 
    * </p>
    * 
    * @param widgetName
    *           The name of the table.
    * 
    * @return Whether or not all the checkboxes in the table are unselected.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget or the widget does not
    *            support this action.
    */
   public boolean isAllUnChecked(final String widgetName)
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.isAllUnChecked();
   }
   
   /**
    * <p>
    * Clicks the "Delete Line" button above a table to delete the checked lines.
    * </p>
    * 
    * @param tableName
    *           The name of the table widget.
    * @param deleteLinesBtn
    *           The name of the "Delete Line" button widget.
    * @param deleteConfirm
    *           "Yes" or "No" for Delete confirmation Dialogue.
    *           
    * @return Whether or not all the checked lines are deleted successfully.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget or the widget does not
    *            support this action.
    */
   public boolean deleteTableLines(final String tableName, final String deleteLinesBtn, final String deleteConfirm )
   {
      if( ! ( deleteConfirm.equals("Delete") || deleteConfirm.equals("Cancel")))
      {
         return false;
      }
      FixtureWidget table = getFixtureWidget(tableName);
      FixtureWidget deletelinesBtn = getFixtureWidget(deleteLinesBtn);
      
      String locator = deletelinesBtn.getWaitTargetLocator();
      if(!deletelinesBtn.waitUntilEnabled())
         return false;
      int tatalItems = table.getTotalItems();
      int checkedItems = table.getCheckedItemsToDelete();
      final By deleteBtn = By.id(locator);
      
      
      WebDriverWait wait1 = new WebDriverWait(getDriver(), 10);
      wait1.until(ExpectedConditions.elementToBeClickable(deleteBtn));
      
      assertTrue("Not enabled", deletelinesBtn.click());
      
      String msgLocator = "//div[@class='k-overlay']/following-sibling::*[1]//div[@id='body-text']";
      
      final By confirmText = By.xpath(msgLocator);
      WebDriverWait wait2 = new WebDriverWait(getDriver(), 10);
      wait2.until(ExpectedConditions.presenceOfElementLocated(confirmText));

      assertTrue("Not able to click on delete confirmation dialogue",clickConfirmationDialogResponse(deleteConfirm));
      // Wait until the confirmation box is fully closed.
      this.pauseForSeconds(3);
      if(deleteConfirm.equals("Delete"))
      {
         int tatalItemsAfterDelete = table.getTotalItems();
         return ( tatalItems - checkedItems == tatalItemsAfterDelete) ? true: false;  
      }else
      {
         int tatalItemsAfterDelete = table.getTotalItems();
         return (tatalItems == tatalItemsAfterDelete) ? true : false;
      }
   }
   
   /**
    * <p>
    * Clears the contents of the specified widget.
    * 
    * In the case of a textbox, clear the contents.
    * In the case of a radio button, deselect it.
    * </p>
    * 
    * <p>
    * Applies to Textboxes,  and RadioButtons
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|clear;|arDistributionCodesTEXTDESC|
    * </pre>
    * 
    * @param widgetName
    *           The name of the widget in question.
    * 
    * @return Whether or not the wiget's contents were successfully cleared.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget or the widget does not
    *            support this action.
    */
   public boolean clear(final String widgetName)
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.clear();
   }

   /**
    * <p>
    * Clears the contents of the specified widget and validates that it contains
    * the specified default value after clearing.
    * </p>
    * 
    * <p>
    * Applies to Textboxes.
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|clear and validate;|oeOrdersORDNUMBER|** NEW **|
    * </pre>
    * 
    * @param widgetName
    *           The name of the widget in question.
    * @param defaultValue
    *           The default value the widget should contain after clearing its
    *           contents.
    * 
    * @return Whether or not the widget's contents were successfully cleared and
    *         the widget contained the default value after clearing.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget or the widget does not
    *            support this action.
    */
   public boolean clearAndValidate(final String widgetName, final String defaultValue)
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.clearAndValidate(defaultValue);
   }

   /**
    * <p>
    * Clicks the specified widget.
    * </p>
    * 
    * <p>
    * Applies to Buttons and Textboxes
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|click;|buttonAdd|
    * </pre>
    * 
    * @param widgetName
    *           The name of the widget in question.
    * 
    * @return Whether or not the widget was clicked successfully.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget or the widget does not
    *            support this action.
    */
   public boolean click(final String widgetName)
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.click();
   }
   
   /**
    * <p>
    * Clicks the specified widget by sending special Key "RETURN".
    * </p>
    * 
    * <p>
    * Applies to Buttons and Textboxes
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|click;|buttonAdd|
    * </pre>
    * 
    * @param widgetName
    *           The name of the widget in question.
    * 
    * @return Whether or not the widget was clicked successfully.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget or the widget does not
    *            support this action.
    */
   public boolean clickByReturn(final String widgetName)
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.clickByReturn();
   }
   
   /**
    * <p>
    * Clicks the specified cell in the specified widget.
    * </p>
    *
    * <p>
    * Applies to Tables
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|click cell;|oeOrderDetails|Description|1|
    * </pre>
    * 
    * @param tableWidgetName
    *           The name of the table widget in question.
    * @param columnWidgetName
    *           The name of the column widget in question.
    * @param rowIndex
    *           The 1-based index of the row where the cell is located (in other
    *           words, use 1 for the first row).
    * 
    * @return Whether or not the specified cell in the widget was clicked
    *         successfully.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget or the widget does not
    *            support this action.
    */
   public boolean clickCell(final String tableWidgetName, final String columnWidgetName, final int rowIndex)
   {
      FixtureWidget table = getFixtureWidget(tableWidgetName);
      FixtureWidget columnHeader = getFixtureWidget(columnWidgetName);
      String columnId = columnHeader.getWaitTargetLocator();
      int columnIdx = table.getIndexOfColumnById(columnId);
      
      return table.clickCell(rowIndex, columnIdx);
   }   
   
   /**
    * <p>
    * Clicks the specified cell in the specified widget.
    * </p>
    *
    * <p>
    * Applies to Tables
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|click cell;|oeOrderDetails|Description|1|
    * </pre>
    * 
    * @param widgetName
    *           The name of the widget in question.
    * @param columnName
    *           The name of the column where the cell is located.
    * @param rowIndex
    *           The 1-based index of the row where the cell is located (in other
    *           words, use 1 for the first row).
    * @return Whether or not the specified cell in the widget was clicked successfully.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget or the widget does not
    *            support this action.
    */
   public boolean clickCellByColumnName(final String widgetName, final String columnName, final int rowIndex)
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.clickCell(columnName, rowIndex);
   } 
   
   /**
    * <p>
    * Clicks the specified cell in the specified widget.
    * </p>
    *
    * <p>
    * Applies to Tables
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|click cell;|oeOrderDetails|Description|1|
    * </pre>
    * 
    * @param widgetName
    *           The name of the widget in question.
    * @param rowIndex
    *           The 1-based index of the row where the cell is located (in other
    *           words, use 1 for the first row).
    * @param columnIdx
    *           The name of the column where the cell is located.
    * @return Whether or not the specified cell in the widget was clicked
    *         successfully.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget or the widget does not
    *            support this action.
    */
   public boolean clickCellByColumnIdx(final String widgetName, final int rowIndex, final int columnIdx)
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.clickCell(rowIndex, columnIdx);
   }    
   /**
    * <p>
    * Clicks the button (in the currently open message dialog) that is
    * associated with the specified response.
    * </p>
    * 
    * <p>
    * Applies to Message Dialogs
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|click message dialog response;|Information|ok|
    * |ensure|click message dialog response;|Confirmation|no|
    * |ensure|click message dialog response;|Error|close|
    * |ensure|click message dialog response;|PMT Processing Codes|yes|
    * </pre>
    * 
    * @param messageType
    *           the type of message which actually is the window title.
    *           "Confirmation", "Error", "Information", or "Warning" are acceptable window titles that represents
    *           the message dialog.
    * 
    * @param responseButton
    *           The message dialog response, which is either {@code "no"},
    *           {@code "ok"}, {@code "yes"}, {@code "cancel"}, or
    *           {@code "close"} .
    * 
    * @return Whether or not the response's associated button was clicked
    *         successfully.
    * 
    * @throws IllegalArgumentException
    *            An invalid response was specified.
    */
   public boolean clickMessageDialogResponse(final String messageType, final String responseButton)
   {
      if ((null == responseButton) || responseButton.isEmpty())
      {
         throw new IllegalArgumentException("The message dialog response must be non-empty.");
      }

      // cycle through the enum til we find the specific response requested
      for(GenericWebFixtureConstants.MessageDialogResponses response: GenericWebFixtureConstants.MessageDialogResponses.values())
      {
         if (responseButton.equalsIgnoreCase(response.toString()))
         {
            return ui.getMessageBox(messageType, false).clickResponseButton(response.toString());
         }
      }
      
      throw new IllegalArgumentException("Message dialog button '" + responseButton + "' is invalid.");
   }   
   
   
   /**
    * <p>
    * Clicks the buttons on the confirmation dialogue.
    * </p>
    * 
    * @param responseButton
    *           The message dialog response, which is either {@code "no"},
    *           {@code "ok"}, {@code "yes"}, {@code "cancel"}, or
    *           {@code "close"} .
    * 
    * @return Whether or not the response's associated button was clicked
    *         successfully.
    * 
    */
   public boolean clickConfirmationDialogResponse(final String responseButton)
   {      
      WebDriverWait wait = new WebDriverWait(getDriver(), 15);
      this.waitForMessageBoxForCNA2("Confirmation");
      
      String confirmationLocator;

      if(responseButton.equalsIgnoreCase("Ok") || responseButton.equalsIgnoreCase("Yes")
            || responseButton.equalsIgnoreCase("Delete") || responseButton.equalsIgnoreCase("Print"))
         confirmationLocator = "//div[@class='k-overlay']/following-sibling::*[1]//input[@id='kendoConfirmationAcceptButton']";
      else if (responseButton.equalsIgnoreCase("Cancel") || responseButton.equalsIgnoreCase("No"))
         confirmationLocator = "//div[@class='k-overlay']/following-sibling::*[1]//input[@id='kendoConfirmationCancelButton']";
      else 
         return false;
      
      wait.until(ExpectedConditions.elementToBeClickable(By.xpath(confirmationLocator)));
      boolean success = ui.getBrowser().clickByReturn(confirmationLocator);
      //boolean success = ui.getBrowser().click(confirmationLocator);
            
      // Confirmation box is implemented with Kendo control, when closing it, it's zooming out gradually 
      // So need wait seconds until it's fully closed.
      this.pauseForSeconds(4);
            
      return success;
   }

   /**
    * <p>
    * Closes the UI.
    * </p>
    * 
    * <p>
    * Applies to a web page
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|close;|
    * </pre>
    * 
    * @return Whether or not the UI closed properly.
    */
   public boolean close()
   {
      // If close immediately just after an action, like Save or Add, 
      // the previous action can't be successfully completed. 
      // So add 2s delay.
      this.pauseForSeconds(2);
      return ui.close();
   }
   
   /**
    * <p>
    * Logout by navigating through portal home page.
    * </p> 
    * 
    * @return Whether or not logout can be done successfully.
    */
   public boolean logoutFromPortal()
   {     
      WebDriverWait wait = new WebDriverWait(getDriver(), 10);
      wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='topMenu']/li[2]"))); 
      
      try
      {
         String jquery = "$(\"ul#topMenu > li:eq(1)\").mouseover()";
         ((JavascriptExecutor)this.getDriver()).executeScript(jquery);
        
         // Wait for elementToBeClickable is not reliable because selenium think the <a> is already clickable
         // While the menu is not full extended yet. So changed to wait for fixed 1s.
         //WebDriverWait wait1 = new WebDriverWait(getDriver(), 10);
         //wait1.until(ExpectedConditions.elementToBeClickable(this.getDriver().findElement(By.xpath("//*[@id='topMenu']/li[2]//a"))));
         this.pauseForSeconds(1);
         
         this.getDriver().findElement(By.xpath("//*[@id='topMenu']/li[2]/div/ul/li[2]/a")).click();
         
         // only wait short time like 1s for the unexpected alert popup
         // the case will fail anyway if it's too short to wait for alert shown up. 
         this.pauseForSeconds(1);

         try
         {
            this.getDriver().switchTo().alert().accept();
         }
         catch (NoAlertPresentException ne)
         {
            this.waitForSignInPageReady();
            ui.close();
            return true;
         }        
         this.waitForSignInPageReady();
         System.out.println("Alert window popups unexpectedly when log out from portal home page");
         ui.close();
         return false;
      }
      catch(Exception e)
      {
         System.out.println("Exception caught during logout, close it anyway.");
         e.printStackTrace();
         ui.close();
         return false;
      }
   }
   
   /**
    * <p>
    * Logout and closes the UI.
    * </p>
    * 
    * @param OpenUiByUrl
    *           "Yes": open the UI by using its full url after sign in 
    *                  in this case, need switch back to the home page, then logout
    *           else: open the UI by navigating through menus in portal home page    
    *                  in this case, the menus is always there, logout directly.
    *                       
    * @return Whether or not logout can be done properly and the browser was closed successfully.
    */
   public boolean logoutAndClose(final String OpenUiByUrl)
   {
      try
      {
         if(OpenUiByUrl.equalsIgnoreCase("Yes"))
         {
            this.switchToPortalHome();
            
            try
            {
               this.getDriver().switchTo().alert().accept();
            }
            catch (NoAlertPresentException ne)
            {
               // Do nothing, keep going
               return this.logoutFromPortal();
            }
            System.out.println("Alert window popups unexpectedly when switch back to portal home page, accept it anyway but case is failed.");
            this.logoutFromPortal();
            return false;  //
         }
         else
         {
            getDriver().switchTo().defaultContent();
            return this.logoutFromPortal();
         }
      }
      catch(SessionNotFoundException e)
      {
         System.out.println("the session has already be closed"); 
         return true;
      }
      catch(Exception e)
      {
         System.out.println("Exception caught during logout and close ui, close it anyway.");
         e.printStackTrace();
         ui.close();
         return false;
      }
   }

   
   /**
    * <p>
    * Waiting for alert Popup and logout by navigating through portal home page.
    * </p> 
    * 
    * @return Whether or not logout can be done successfully. 
    *         If alert is not popup as expected, return false.
    */
   public boolean logoutFromPortalWithAlert()
   {
      WebDriverWait wait = new WebDriverWait(getDriver(), 10);
      wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='topMenu']/li[2]"))); 
            
      try
      {
         String jquery = "$(\"ul#topMenu > li:eq(1)\").mouseover()";
         ((JavascriptExecutor)this.getDriver()).executeScript(jquery);
        
         // Wait for elementToBeClickable is not reliable because selenium think the <a> is already clickable
         // While the menu is not full extended yet. So changed to wait for fixed 1s.
         //WebDriverWait wait1 = new WebDriverWait(getDriver(), 10);
         //wait1.until(ExpectedConditions.elementToBeClickable(this.getDriver().findElement(By.xpath("//*[@id='topMenu']/li[2]//a"))));
         this.pauseForSeconds(1);
         
         this.getDriver().findElement(By.xpath("//*[@id='topMenu']/li[2]/div/ul/li[2]/a")).click();

         try
         {
            WebDriverWait wait1 = new WebDriverWait(getDriver(), 20);
            wait1.until(ExpectedConditions.alertIsPresent());
            
            this.getDriver().switchTo().alert().accept();
         }
         catch (NoAlertPresentException ne)
         {
            // Do nothing
            System.out.println("Alert window does not popup as expected when logout from portal home page");
            this.waitForSignInPageReady();
            ui.close();
            return false; 
         }
         this.waitForSignInPageReady();
         ui.close();
         return true;
      }
      catch(Exception e)
      {
         System.out.println("Exception caught during logout, close it anyway.");
         e.printStackTrace();
         ui.close();
         return false;
      }
   }
   
   /**
    * <p>
    * Logout and closes the UI, do accept for the expected alert popup.
    * </p>
    * 
    * @param OpenUiByUrl
    *           "Yes": open the UI by using its full url after sign in 
    *                  in this case, need switch back to the home page, then logout
    *           else: open the UI by navigating through menus in portal home page    
    *                  in this case, the menus is always there, logout directly.
    *                       
    * @return Whether or not logout can be done properly and the browser was closed successfully.
    */
   public boolean logoutAndCloseWithAlert(final String OpenUiByUrl)
   {
      try
      {
         if(OpenUiByUrl.equalsIgnoreCase("Yes"))
         {
            this.switchToPortalHome();
                        
            try
            {
               WebDriverWait wait = new WebDriverWait(getDriver(), 20);
               wait.until(ExpectedConditions.alertIsPresent());
               
               this.getDriver().switchTo().alert().accept();
            }
            catch (NoAlertPresentException ne)
            {
               // If no alert popup, logout and close the ui anyway, but return false since alert is expected
               System.out.println("Alert window does not popup as expected when switch back to portal home page");
               this.logoutFromPortal();
               return false;
            }  
            return logoutFromPortal();
         }
         else
         {
            getDriver().switchTo().defaultContent();
            return this.logoutFromPortalWithAlert();
         }
      }
      catch(SessionNotFoundException e)
      {
         System.out.println("the session has already be closed"); 
         return true;
      }
      catch(Exception e)
      {
         System.out.println("Exception caught during logout and close ui, close it anyway.");
         e.printStackTrace();
         ui.close();
         return false;
      }
   }
   
   /**
    * <p>
    * Wait for input element "Sign In" in SignIn page.
    * </p>
    */
   private void waitForSignInPageReady()
   {
      WebDriverWait wait = new WebDriverWait(this.getDriver(), 15);    
      wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@value='Sign In']")));
   }
   /**
    * <p>
    * Switch from a specified ui page or crystal print view page to portal home page.
    * </p>
    */
   private void switchToPortalHome()
   {
      String currentUrl = this.getDriver().getCurrentUrl();
      
      String[] tmp = currentUrl.split("/"); 
      String portalHomeUrl = tmp[0] + "//" + tmp[2] + "/" + tmp[3] + "/Core/Home";
      
      // For crystal report view, the ulr looks like:
      // https://columbus20nastaging.sagedatacloud.com/WebForms/ReportViewer.aspx?token=55cd943f-9df4-448f-b81e-8a9d47b1bffc
      // need replace WebForms with tenant info
      if(portalHomeUrl.contains("WebForms"))
         portalHomeUrl = portalHomeUrl.replace("WebForms", this.tenantInfo);

      this.getDriver().get(portalHomeUrl);
      this.pauseForSeconds(1);  
   }
   
   /**
    * <p>
    * From application portal, close the opened UI window.
    * </p>
    * 
    * @param uiWindowId
    *           the id of <div> hold the opened opened ui
    * 
    * @return Whether or not the ui is closed properly.
    */
   public boolean closeUiWindow(final String uiWindowId)
   {
      return ui.closeUiWindow(uiWindowId);
   }
   
   
   /**
    * <p>
    * Closes the UI, bypassing the Javascript confirmation when closing browser
    * window.
    * </p>
    * 
    * <p>
    * Applies to a web page
    * </p>
    *  
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|close;|force|
    * </pre>
    * 
    * @param force
    *           Whether to bypass the Javascript confirmation when closing
    *           browser. Which is either {@code "force"}, or anything else.
    * 
    * @return Whether or not the UI closed properly.
    */
   public boolean close(String force)
   {
      return ui.close(force.toLowerCase().equals("force"));
   }
  
   /**
    * <p>
    * @param messageType        
    *           Success  -- Success Message in a normal form  
    *           successInReversing  -- Success Message shown during reverse a posted batch
    *           Error   --  Errors,Warning and Information in a normal form
    *           Popup  -- To error message in a Popup form
    *           inProcessing   --error or information message in a processing status Popup window
    *           processingResult -- the processing result grid in a processing popup window
    *           importResult   --message for import result
    *           exportResult   --message for export result
    *           Confirmation   -- confirmation dialog box   

    * Close the Error/Confirmation/Information message popup box
    * </p>
    */
   public void  closeMessageBox(final String messageType )
   {
      if( this.waitForMessageBoxForCNA2(messageType))
      {
         String locator;
         if ( messageType.equals("Error"))
            locator = "//span[@class='icon msgCtrl-close']";
         else if (messageType.equals("Confirmation"))
            locator = "//div[@class='k-overlay']/following-sibling::*[1]/div[1]/div/a/span[@class='k-icon k-i-close']";
         else 
            locator = "//span[@class='k-icon k-i-close']";
         
         if ( this.getDriver().findElement(By.xpath(locator)) !=null)
         {
            //this.getDriver().findElement(By.xpath(locator)).click();
            // above line only works for IE and Firefox, not for Chrome
            // so use java script to click the close icon.
            JavascriptExecutor js = (JavascriptExecutor) this.getDriver();
            String javascript = "return document.evaluate(\"" + locator + "\", document, null, 9, null).singleNodeValue.click();";
            js.executeScript(javascript);
            // Message boxes are implemented with Kendo controls, when closing it, it's zooming out gradually 
            // So need wait seconds until it's fully closed.
            this.pauseForSeconds(4);
         }
      }
      
   }
   
   /**
    * To close pop windows(To click on close Image(X) button)
    */
   public void closePopupWindow()
   {
      String closeImageLocator = "//div[@class='k-overlay']/following-sibling::*[1]/div[1]/div/a/span[@class='k-icon k-i-close']";
      // Must pause for seconds to wait until the Popup window is fully displayed.
      // Implicit wait not working because the element is created and get clickable immediately, while 
      // before the window get fully displayed, click just not working, no error, no exception.
      // So just use explicit wait 

      this.pauseForSeconds(1);
      this.getDriver().findElement(By.xpath(closeImageLocator)).click();
      this.pauseForSeconds(4);
      
   }
   /**
    * Decrypts an advisor log file using a known decrypt executable program.
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |note |Run this once the encrypted file Advisor.env exists|
    * |ensure|decrypt; |${ACCPAC_FITNESSE}|${LOGFILEPATH}|Advisor.env|
    * .
    * .
    * |note|The decrypted file is Advisor.env.txt|
    * |check|get value for type and key|C:\AdvisorLog\Advisor.env.txt|first|0|User|UserID|ADMIN|
    * </pre>
    * 
    * @param fitnesseRoot
    *           FitNesse root. FitNesse users can get it using:
    *           ${ACCPAC_FITNESSE}
    * @param logFilePath
    *           FitNesse log file path. FitNesse users can get it using:
    *           ${LOGFILEPATH}
    * @param logFileName
    *           The log file name (with extension) that is encrypted
    * @return <li>
    *         <code>True if logFileName was decrypted</code></li> <li>
    *         <code>False otherwise</code></li>
    */
   public boolean decrypt(final String fitnesseRoot, final String logFilePath, final String logFileName)
   {
      return FileHelper.decrypt(fitnesseRoot, logFilePath, logFileName);
   }   

   /**
    * <p>
    * Because the content table is separate from the  header table. Delete the row of the specified table 
    * by giving the row index.
    * </p>
    * 
    * <p>
    * Applies to Tables
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|delete row;|taxTable|1|
    * </pre>
    * 
    * @param widgetName
    *           The name of the widget in question.
    * @param rowIndex
    *           The 1-based index of the row to delete.
    *            
    * @return Whether or not the delete row action could take place.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget or the widget does not
    *            support this action.
    */
   public boolean deleteRow(final String widgetName, final int rowIndex)
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.deleteRow(rowIndex);
   }      
   
   /**
    * <p>
    * Checks whether or not the specified widget currently exists in the DOM.
    * </p>
    * 
    * <p>
    * Applies to most widgets
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|exists;|buttonDelete|
    * </pre>
    * 
    * @param widgetName
    *           The name of the widget in question.
    * 
    * @return Whether or not the widget currently exists in the DOM.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget.
    */
   public boolean exists(final String widgetName)
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.exists();
   }
 


   /**
    * <p>
    * Export the report to a CSV or PDF or XML file under the "Actual" folder
    * and then compare the export file to a previously set export that resides
    * under the "Expected" folder.
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|click; |buttonPrint   |
    * |ensure|click crystal report export;   |
    * |note |Export and compare report.  Note the file name is "myFileName". and so "myFileName.csv" or "myFileName.pdf" will be created and compared |
    * |note |Also the compare will ignore column 1 which is the report generation date and time|
    * |ensure|export and compare report; |${ACCPAC_FITNESSE}\FitNesseRoot\${TESTPATH}| myFileName | pdf|
    * |note|xml compare will report any different, similar will not work now
    * |ensure|export and compare report;|pathxyz|ap5101|xml|
    * </pre>
    * 
    * @param path
    *           The path to the FitNesse test page. 
    *           The path is where the test case suite is located
    *           e.g.: ..\SuiteGl\SuiteGl4101\
    * 
    * @param caseName
    *           The name of a test case.        
    *           e.g.: Test4101ChartAllocation
    *           the report file will be saved to Actual and Expected for compare:
    *           path\caseName\Actual
    *           path\caseName\Expected
    *           
    * @param fileExtension
    *           The file extension, the type of file, to save as; which is
    *           either {@code "csv"}, {@code "pdf"}, {@code "xml"}.
    * 
    * @return Whether or not the file export and compare were successful.
    * 
    * @throws IllegalArgumentException
    *            The filename must not be empty.
    */
   public boolean exportAndCompareReport(final String path, final String caseName, final String fileExtension)
   {
      if (!this.exportReport(path, caseName, fileExtension))
         return false;

      return ui.compareReport(fileExtension);
   }      
   
   /**
    * <p>
    * Export the report to a CSV or PDF or XML file under the "Actual" folder
    * and then compare the export file to a previously set export that resides
    * under the "Expected" folder.
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|click; |buttonPrint   |
    * |ensure|click crystal report export;   |
    * |note |Export report.  Note the file name is "myFileName". and so "myFileName.csv" or "myFileName.pdf" will be created and compared|
    * |ensure|export report; |${ACCPAC_FITNESSE}\FitNesseRoot\${TESTPATH}| myFileName | pdf|
    * </pre>
    * 
    * @param path
    *           The path to the Fitnesse test page.
    * 
    * @param caseName
    *           The name of the test case, report file will be generated 
    *           under the case's actual and expected dir.
    * 
    * @param fileExtension
    *           The file extension, the type of file, to save as; which is
    *           either {@code "csv"}, {@code "pdf"}, {@code "xml"}.
    * 
    * @return Whether or not the file compare was successful.
    * 
    * @throws IllegalArgumentException
    *            The filename must not be empty.
    */
   public boolean exportReport(final String path, final String caseName, final String fileExtension)
   {
      if ((null == path) || path.isEmpty())
      {
         throw new IllegalArgumentException("The path must be non-empty.");
      }

      if ((null == caseName) || caseName.isEmpty())
      {
         throw new IllegalArgumentException("The file name must be non-empty.");
      }

      String realPath = path;
      //String realPath = extractFitnesseTestPath(path);
      
      if (fileExtension.toLowerCase().equals(GenericWebFixtureConstants.FileExtensions.PDF)
            || fileExtension.toLowerCase().equals(GenericWebFixtureConstants.FileExtensions.CSV)
            || fileExtension.toLowerCase().equals(GenericWebFixtureConstants.FileExtensions.XML))
      {
         return ui.exportReport(realPath, caseName, fileExtension);
      }
      else{
         throw new IllegalArgumentException("The file extension (file type) is not valid.");
      }
   }

   /**
    * <p>
    * Select the a file format to export a report.
    * </p>
    * 
    * @param fileFormat
    *           The file format to export a report.
    * 
    * @return Whether or not the a file format is selected successful.
    * 
    */   
   
   public boolean selectExportFileFormat(final String fileFormat)
   {
         return ui.selectExportFileFormat(fileFormat);
 
   }
   
   /**
    * <p>
    * Used for tables which have multi_pages, automatically turn pages until the give cell is located.
    * </p>
    * 
    * @param tableWidgetName
    *           The name of the table widget in question.
    * @param columnWidgetName
    *           The name of the column widget.  
    * @param value
    *           value of a given cell shown on the screen            
    * @return Returns the row index of a cell with a given value
    * 
    * @throws IllegalArgumentException
    *            The table does not contain such a cell with the given value.
    */
   public int getCellRowIdxByValue(final String tableWidgetName, final String columnWidgetName, final String value)
   {
      FixtureWidget table = getFixtureWidget(tableWidgetName);
      FixtureWidget columnHeader = getFixtureWidget(columnWidgetName);
      String columnId = columnHeader.getWaitTargetLocator();
      int columnIdx = table.getIndexOfColumnById(columnId);
      
      return table.getCellRowIdxByValue(columnIdx, value);
   }
   
   /**
    * <p>
    * Gets the text of the specified cell in the specified widget, or returns
    * null if the cell cannot be located or is invisible.
    * </p>
    * 
    * <p>
    * Applies to Tables
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |check|get cell text;|oeOrderDetails|Description|1|Fluorescent Desk Lamp|
    * </pre>
    * 
    * <pre>
    * |check|get cell text;|queryTable|Query Name|1|General Ledger Transactions|
    * </pre>
    * 
    * @param widgetName
    *           The name of the widget in question.
    * @param columnName
    *           The name of the column where the cell is located.
    * @param rowIndex
    *           The 1-based index of the row where the cell is located (in other
    *           words, use 1 for the first row).
    * 
    * @return The text of the specified cell in the widget, or null if the cell
    *         cannot be located or is invisible.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget or the widget does not
    *            support this action.
    */
   public String getCellTextByColumnName(final String widgetName, final String columnName, final int rowIndex)
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.getCellText(columnName, rowIndex);
   }   
   
   /**
    * <p>
    * Gets the text of the specified cell in the specified widget, or returns
    * null if the cell cannot be located or is invisible.
    * </p>
    * 
    * <p>
    * Applies to Tables
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |check|get cell text;|oeOrderDetails|Description|1|Fluorescent Desk Lamp|
    * </pre>
    * 
    * <pre>
    * |check|get cell text;|queryTable|Query Name|1|General Ledger Transactions|
    * </pre>
    * 
    * @param tableWidgetName
    *           The name of the table widget in question.
    * @param columnWidgetName
    *           The name of the column widget.          
    * @param rowIndex
    *           The 1-based index of the row where the cell is located (in other
    *           words, use 1 for the first row).
    * @return The text of the specified cell in the widget, or null if the cell
    *         cannot be located or is invisible.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget or the widget does not
    *            support this action.
    */
   public String getCellText(final String tableWidgetName, final String columnWidgetName, final int rowIndex)
   {
      FixtureWidget table = getFixtureWidget(tableWidgetName);
      FixtureWidget columnHeader = getFixtureWidget(columnWidgetName);
      String columnId = columnHeader.getWaitTargetLocator();
      int columnIdx = table.getIndexOfColumnById(columnId);
      
      return table.getCellText(rowIndex, columnIdx);
   }
 
   /**
    * <p>
    * Gets the text of the specified cell in the specified widget, or returns
    * null if the cell cannot be located or is invisible.
    * </p>
    * 
    * <p>
    * Applies to Tables
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |check|get cell text;|oeOrderDetails|Description|1|Fluorescent Desk Lamp|
    * </pre>
    * 
    * <pre>
    * |check|get cell text;|queryTable|Query Name|1|General Ledger Transactions|
    * </pre>
    * 
    * @param widgetName
    *           The name of the widget in question.
    * @param rowIndex
    *           The 1-based index of the row where the cell is located (in other
    *           words, use 1 for the first row).
    * @param columnIdx
    *           The name of the column where the cell is located.
    * @return The text of the specified cell in the widget, or null if the cell
    *         cannot be located or is invisible.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget or the widget does not
    *            support this action.
    */
   public String getCellTextByColumnIdx(final String widgetName, final int rowIndex, final int columnIdx)
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.getCellText(rowIndex, columnIdx);
   }
   
   /**
    * <p>
    * Gets the text of this widget's current page field.
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |check|get current page;|finderTable|1|
    * </pre>
    * 
    * @param widgetName
    *           The name of the widget in question.
    * 
    * @return The text of this widget's current page field.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   public String getCurrentPage(final String widgetName)
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.getCurrentPage();
   }   
   
   /**
    * <p>
    * Gets the title of the current window.
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |check|get current window title;|O/E Templates|
    * </pre>
    * 
    * @return The title of the current window.
    */
   public String getCurrentWindowTitle()
   {
      Browser browser = properties.getBrowser();
      return browser.getCurrentWindowTitle();
   }

   /**
    * <p>
    * Gets the {@link FixtureWidget} for the specified widget name on the
    * current form, throwing an exception if this UI does not contain such a
    * widget.
    * </p>
    * 
    * <p>
    * NOTE: This method never returns null.
    * </p>
    * 
    * @param widgetName
    *           The widget name.
    * 
    * @return The {@link FixtureWidget} for the specified name.
    * 
    * @throws IllegalArgumentException
    *            This UI does not contain a widget by that name.
    */
   private FixtureWidget getFixtureWidget(final String widgetName)
   {
      FixtureWidget fixtureWidget = properties.getFixtureWidget(currentFormName, widgetName);
      if (null == fixtureWidget)
      {
         StringBuilder sbExceptionMsg = new StringBuilder();
         sbExceptionMsg.append("UI '");
         sbExceptionMsg.append(properties.getUIName());
         sbExceptionMsg.append("' does not contain widget '");
         sbExceptionMsg.append(widgetName);
         sbExceptionMsg.append("' on its ");

         if ((null == currentFormName) || currentFormName.isEmpty())
         {
            sbExceptionMsg.append("main form.");
         }
         else
         {
            sbExceptionMsg.append("'");
            sbExceptionMsg.append(currentFormName);
            sbExceptionMsg.append("' form.");
         }

         throw new IllegalArgumentException(sbExceptionMsg.toString());
      }

      return fixtureWidget;
   }

   
   /**
    * <p>
    * Gets the underlying web driver for debugging.
    * </p>
    * 
    * @return the underlying web driver.
    */
   public WebDriver getDriver()
   {
      return properties.getBrowser().getDriver();
   } 

   /**
    * <p>
    * Because the content table is separate from the  header table. Gets the text of
    * the specified column in the header table, or returns null if the
    * cell cannot be located or is invisible.
    * </p>
    * 
    * <p>
    * Applies to Tables
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |check|get header cell text;|taxTable|1|Tax Authority|
    * </pre>
    * 
    * @param tableWidgetName
    *           The name of the table widget in question.
    * @param columnWidgetName
    *           The name of the column widget in question.
    * 
    * @return The text of the specified cell in the widget, or null if the cell
    *         cannot be located or is invisible.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget or the widget does not
    *            support this action.
    */
   public String getHeaderCellText(final String tableWidgetName, final String columnWidgetName)
   {
      FixtureWidget table = getFixtureWidget(tableWidgetName);
      FixtureWidget columnHeader = getFixtureWidget(columnWidgetName);
      String columnId = columnHeader.getWaitTargetLocator();
      int columnIdx = table.getIndexOfColumnById(columnId);
      
      return table.getHeaderCellText(columnIdx);
   }

   /**
    * <p>
    * Because the content table is separate from the  header table. Gets the text of
    * the specified column in the header table, or returns null if the
    * cell cannot be located or is invisible.
    * </p>
    * 
    * <p>
    * Applies to Tables
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |check|get header cell text;|taxTable|1|Tax Authority|
    * </pre>
    * 
    * @param widgetName
    *           The name of the widget in question.
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
   public String getHeaderCellTextByIdx(final String widgetName, final int columnIndex)
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.getHeaderCellText(columnIndex);
   }   
   
   /**
    * <p>
    * Gets the text of the message shown in the currently open message
    * dialog.(usually it is summary information for the message box)
    * </p>
    * 
    * <p>
    * Applies to Message dialogs.
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|wait for message dialog;|"Information"|
    * |check|get message dialog text;|Vendor has been saved|
    * </pre>
    * 
    * @param messageType
    *           the type of message which actually is the window title.
    *           "Confirmation", "Error", "Information", or "Warning" are acceptable window titles that represents
    *           the message dialog.
    *           
    * @return The text of the message shown in the currently open message
    *         dialog.
    */
   public String getMessageDialogText(final String messageType)
   {
      return ui.getMessageBox(messageType, false).getMessageDescription();
   }   
   
   /**
    * <p>
    * Gets the options that belong to the specified widget (for example, the
    * values in a listBox or ComboBox).
    * </p>
    * 
    * @param widgetName
    *           The name of the widget in question.
    * 
    * @return The options that belong to the widget (for example, the values in
    *         a listBox or comboBox).
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget or the widget does not
    *            support this action.
    */
   public String[] getAllOptions(final String widgetName)
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.getAllOptions();
   }
   
   /**
    * <p>
    * Gets the selected options from a listBox which may support multi-selections.
    * </p>
    * 
    * @param widgetName
    *           The name of the widget in question.
    * 
    * @return The options that belong to a listBox widget(type genericListBox)
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget or the widget does not
    *            support this action.
    */
   public String[] getSelectedOptions(final String widgetName)
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.getSelectedOptions();
   }

   /**
    * <p>
    * Gets a randomly generated ID using Javas UUID (Universally Unique
    * Identifiers) that can be stored and used through FitNesse tests.
    * 
    * Depending on the usage, the number of characters may vary, so we'll just
    * strip out characters until left with the desired number.
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |note|the 22 represents the number of characters needed, order number is limited to a max of 22 characters 
    * |note|so the random string should be the same number of characters or less.  So putting anything less than 22 is acceptable as well.|
    * |$ORDERNO=|get random id|22|
    * |ensure|type;|orderNumber|$ORDERNO|
    * </pre>
    * 
    * @param length
    *           the number of characters that are needed, so that the method can
    *           be used in different contexts'.
    * 
    * @return The text of the message shown in the currently open message
    *         dialog.
    */
   public String getRandomId(final int length)
   {
      String id = UUID.randomUUID().toString();

      // This should be random enough for the context we use even if UUID generates a big ID
      // and we're only using part of it.  If need be we can implement our own Random generator.
      // Take only the number of characters indicated by the length parameter.
      id = id.substring(id.length() - length, id.length());

      return id;
   }

   /**
    * <p>
    * Gets a randomly generated ID and returns it as the specified string casing
    * (either UPPERCASE or LOWERCASE), using Javas UUID (Universally Unique
    * Identifiers) that can be stored and used through FitNesse tests.
    * 
    * Depending on the usage, the number of characters may vary, so we'll just
    * strip out characters until left with the desired number.
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |note|the 22 represents the number of characters needed, order number is limited to a max of 22 characters 
    * |note|so the random string should be the same number of characters or less.  So putting anything less than 22 is acceptable as well.|
    * |$ORDERNO=|get random id|22| UPPERCASE |
    * |ensure|type;|orderNumber|$ORDERNO|
    * </pre>
    * 
    * @param length
    *           the number of characters that are needed, so that the method can
    *           be used in different contexts'.
    * @param stringCase
    *           indicates to convert the random id to all {@code "UPPERCASE"} or
    *           all {@code "LOWERCASE"} before return.
    * 
    * @return The text of the message shown in the currently open message
    *         dialog.
    */
   public String getRandomId(final int length, final String stringCase)
   {
      if (stringCase.toUpperCase().trim().equals("UPPERCASE"))
         return getRandomId(length).toUpperCase();
      else if (stringCase.toUpperCase().trim().equals("LOWERCASE"))
         return getRandomId(length).toUpperCase();
      else
         throw new IllegalArgumentException(
               "The string casing must be either \"UPPERCASE\" or \"LOWERCASE\".\r\nIf you have no preference then use \"get random id\" that takes only 1 argument.");
   }

   /**
    * <p>
    * Gets the text of the specified widget, or returns null if the widget
    * cannot be located in the DOM or is invisible.
    * </p>
    * 
    * <p>
    * Applies to Textboxes, drop down lists, Labels
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |check|get text;|arDistributionCodesTEXTDESC|Retail Sales Revenue|
    * |check|get text;|oeOrdersORDNUMBER|ORD0000001|
    * |check|get text;|oeOrdersORDDATE|01/25/2019|
    * |check|get text;|oeTemplatesORDTYPE|Active|
    * </pre>
    * 
    * @param widgetName
    *           The name of the widget in question.
    * 
    * @return The text of the widget, or null if the widget cannot be located in
    *         the DOM or is invisible.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget or the widget does not
    *            support this action.
    */
   public String getText(final String widgetName)
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.getText();
   }
   
   /**
    * <p>
    * Gets the placeholder text of the specified widget, or returns null if the widget
    * cannot be located in the DOM or is invisible.
    * * E.g.: The original value (000000) in batch number 
    * </p>
    * 
    * 
    * @param widgetName
    *           The name of the widget in question.
    * 
    * @return The text of the widget, or null if the widget cannot be located in
    *         the DOM or is invisible.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget or the widget does not
    *            support this action.
    */
   public String getPlaceHolderText(final String widgetName)
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.getPlaceHolderText();
   }

   /**
    * <p>
    * Gets the text of this widget's total pages field.
    * </p>
    * 
    * <p>
    * Applies to Tables
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |check|get total pages;|commentsTable|of 2|
    * </pre>
    * 
    * @param widgetName
    *           The name of the widget in question.
    * 
    * @return The text of this widget's total pages field.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   public String getTotalPages(final String widgetName)
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.getTotalPages();
   }

   /**
    * Change the page for this widget.
    * 
    * <p>
    * Applies to Tables
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|go to page;|commentsTable|3|
    * </pre>
    * 
    * @param widgetName
    *           The name of the widget in question.
    * 
    * @param page
    *           The page of this widget to go to.
    * 
    * @return Whether or not this widget can change to the specified page.
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */
   public boolean goToPage(final String widgetName, final String page)
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.goToPage(page);
   }   
   
   /**
    * <p>
    * Checks whether or not the specified widget is currently in a selected
    * state.
    * </p>
    * 
    * <p>
    * Applies to checkboxes
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|is checked;|oeOnHold|
    * </pre>
    * 
    * @param widgetName
    *           The name of the widget in question.
    * 
    * @return Whether or not the widget is currently in a selected state.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget or the widget does not
    *            support this action.
    */
   public boolean isChecked(final String widgetName)
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.isChecked();
   }   
   
   /**
    * <p>
    * Checks whether or not the specified widget is currently disabled. Note:
    * for a finder widget, the ID TextBox status will be used as verification.
    * </p>
    * 
    * <p>
    * Applies to most widgets
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|is disabled;|buttonDelete|
    * |reject|is disabled;|buttonDelete|
    * </pre>
    * 
    * @param widgetName
    *           The name of the widget in question.
    * 
    * @return Whether or not the widget is currently disabled.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget.
    */
   public boolean isDisabled(final String widgetName)
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.isDisabled();
   }

   /**
    * <p>
    * Checks whether or not the specified widget is currently editable. Note
    * that a widget is never editable when it is invisible.
    * </p>
    * 
    * <p>
    * Applies to most widgets
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|is editable;|arDistributionCodesTEXTDESC|
    * |reject|is editable;|arDistributionCodesTEXTDESC|
    * </pre>
    * 
    * @param widgetName
    *           The name of the widget in question.
    * 
    * @return Whether or not the widget is currently editable.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget.
    */
   public boolean isEditable(final String widgetName)
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.isEditable();
   }

   /**
    * <p>
    * Determines if the browser window has a scrollbar of the specified
    * orientation. Note: Uses javascript to do the verification.
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|is scrollbar present;|horizontal|
    * |reject|is scrollbar present;|vertical|
    * |check|is scrollbar present;|horizontal|false|
    * </pre>
    * 
    * @param orientation
    *           the scrollbar to check. Either <code>horizontal</code> or
    *           <code>vertical</code>
    * 
    * @return <li><code>true</code> if the window has a horizontal scrollbar</li>
    *         <li><code>false</code> if the window does not have a horizontal
    *         scrollbar</li>
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget, the widget does not
    *            support this action, or the widget does not contain the
    *            specified field.
    */
   public boolean isScrollbarPresent(final String orientation)
   {
      Browser browser = properties.getBrowser();
      return browser.isScrollbarPresent(orientation);
   }

   /**
    * <p>
    * Checks whether or not the specified widget is currently in a selected
    * state.  As examples, whether or not a tab is selected or not.
    * </p>
    * 
    * <p>
    * Applies to tabs
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|is selected;|Comments|
    * </pre>
    * 
    * @param widgetName
    *           The name of the widget in question.
    * 
    * @return Whether or not the widget is currently in a selected state.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget or the widget does not
    *            support this action.
    */
   public boolean isSelected(final String widgetName)
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.isSelected();
   }

   /**
    * Checks whether or not the specified Radio Button in the Radio Group Widget
    * is selected (Note the user should pass the Label of the Radio Button which
    * need to be validated).
    * 
    * <p>
    * Applies to radio buttons
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|is selected;|radiobuttonDetailSummary|Summary|
    * </pre>
    * 
    * @param widgetName
    *           The name of the widget in question.
    * 
    * @param radioButtonlabel
    *           The Label of the Radio Button.
    * 
    * @return Whether or not the Radio Button in the Radio Group Widget is
    *         selected.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget or the widget does not
    *            support this action.
    */
   public boolean isSelected(final String widgetName, final String radioButtonlabel)
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.isSelected(radioButtonlabel);
   }

   /**
    * <p>
    * Checks whether or not the specified widget is currently visible.
    * </p>
    * 
    * <p>
    * Applies to most widgets
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|is visible;|buttonDelete|
    * |reject|is visible;|buttonDelete|
    * </pre>
    * 
    * @param widgetName
    *           The name of the widget in question.
    * 
    * @return Whether or not the widget is currently visible.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget.
    */
   public boolean isVisible(final String widgetName)
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.isVisible();
   }
   
   /**
    * <p>
    * Checks whether or not the specified widget is currently visible.
    * </p>
    * 
    * <p>
    * Applies to most widgets
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|is visible;|buttonDelete|
    * |reject|is visible;|buttonDelete|
    * </pre>
    * 
    * @param tableWidgetName
    *           The name of the table widget in question.
    *           
    * @param columnWidgetName
    *           The name of the column(column header) widget in question.
    *           
    * @return Whether or not the column widget is currently visible.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget.
    */
   public boolean isColumnHidden(final String tableWidgetName, final String columnWidgetName)
   {
      FixtureWidget table = getFixtureWidget(tableWidgetName);
      FixtureWidget columnHeader = getFixtureWidget(columnWidgetName);
      String columnId = columnHeader.getWaitTargetLocator();
      int columnIdx = table.getIndexOfColumnById(columnId);
      
      return table.isColumnHidden(columnIdx);    
   }
   
   /**
    * <p>
    * Move mouse to an element and hover over it.
    * </p>
    * 
    * @param widgetName
    *           The name of the widget in question.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget, the widget does not
    *            support this action, or an invalid position was specified.
    */
   public void hover(final String widgetName)
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      fixtureWidget.hover();
   } 

   /**
    * <p>
    * Clicks on the specified widget's navigation button for the specified
    * navigation action.
    * </p>
    * 
    * <p>
    * Applies to Tables
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|navigate;|finderTable|next|
    * |ensure|navigate;|finderTable|last|
    * </pre>
    * 
    * @param widgetName
    *           The name of the widget in question.
    * @param action
    *           The action to navigate to, which is either {@code "first"},
    *           {@code "previous"}, {@code "next"}, or {@code "last"}.
    * 
    * @return Whether or not the widget's navigation button for the specified
    *         position was clicked.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget, the widget does not
    *            support this action, or an invalid position was specified.
    */
   public boolean navigate(final String widgetName, final String action)
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      boolean result = fixtureWidget.navigate(action);
      return result;
   }   
   
   /**
    * <p>
    * Opens the UI's web page at its URL. This gets to the UI's signon screen.
    * </p>
    * 
    * <p>
    * Applies to a browser
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|open;|
    * </pre>
    * 
    * @return Whether or not that page was opened.
    */
   public boolean open()
   {
      return ui.open();
   }
   
   /**
    * 
    */
   public void openPage()
   {
      ui.openPage();
   }
   /**
    * <p>
    * Opens the UI's web page at its URL. This gets to the UI's signon screen.
    * Specially, this method is for accessing Q2O directly.
    * </p>
    * 
    * <p>
    * Applies to a browser
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|open;|?APPL=SAGEERP&APPL=SAGECRM&SAGEERP_DATASET=SAMLTD&SAGECRM_SID=123&SAGECRM_COMPANYID=4&SAGECRM_OPPOID=4&SAGECRM_INSTALLNAME=crm&SAGECRM_PERSONID=6&SAGEERP_CUSTOMER=1200&SAGEERP_EDITMODE=1&SAGEERP_ISQUOTE=1"|
    * </pre>
    * 
    * @param urlParameters
    *           additional parameters to append to after the .html in the URL
    * 
    * @return Whether or not that page was opened.
    */
   public boolean open(final String urlParameters)
   {
      return ui.open(urlParameters);
   }
   
   /**
    * <p>
    * Opens the portal home page.
    * </p>
    * 
    * @param sageId
    *           The sageId used during login to Web app.
    * @param password
    *           The password for the sageId used for login.
    * 
    * @return Whether or not that portal home page was opened.
    */
   public boolean openPortal(final String sageId, final String password)
   {
      boolean result = ui.openPortal(sageId, password);
      this.getTenantInfo();
      return result;
   }
   
   /**
    * <p>
    * Opens the UI after sign in either by navigating through portal menus, or by changing url.
    * 
    * @param sageId
    *           The sageId used during login to Web app.
    * @param password
    *           The password for the sageId used for login.
    * @param OpenUiByUrl
    *           "Yes": open the UI by using its full url after sign in 
    *           else: open the UI by navigating through menus in portal home page 
    * 
    * @return Whether or not that page was opened.
    */
   public boolean openUi(final String sageId, final String password, final String OpenUiByUrl)
   {
      boolean result;
      if (OpenUiByUrl.equalsIgnoreCase("Yes"))
         result = ui.openUiByUrl(sageId, password);
      else 
         result = ui.openUiByMenus(sageId, password);
      
      this.getTenantInfo();
      return result;
   }
   
   /**
    * <p>
    * Sign to portal home page, update session date, then open the specified ui 
    * either by navigating through portal menus, or by changing url.
    * 
    * @param sageId
    *           The sageId used during login to Web app.
    * @param password
    *           The password for the sageId used for login.
    * @param OpenUiByUrl
    *           "Yes": open the UI by using its full url after sign in 
    *           else: open the UI by navigating through menus in portal home page            
    * @param sessionDate
    *           The sessionDate is going to update to
    * @return Whether or not that page was opened.
    */
   public boolean openUiWithSessionDate(final String sageId, final String password, final String OpenUiByUrl, final String sessionDate)
   {
      boolean result;
      if (OpenUiByUrl.equalsIgnoreCase("Yes"))
         result = ui.openUiByUrlWithSessionDate(sageId, password, sessionDate);
      else 
         result = ui.openUiByMenusWithSessionDate(sageId, password, sessionDate);
      
      this.getTenantInfo();
      return result;
   }
   
   /**
    * <p>
    * Opens a UI after signed in, don't use GenericWebFixture which will open a new window.
    * Just change the property and open the UI in current signed window.
    * </p>
    * 
    * @param configurationPath
    *           The layout map to switch to
    *           
    * @param OpenUiByUrl
    *           open the ui by changing url or navigationg through portal menus
    *           
    * @return Whether or not that page was opened.
    */
   public boolean openUiAfterSignIn(final String configurationPath, final String OpenUiByUrl)
   {
      this.properties = parser.parse(configurationPath);
      this.ui = new FixtureUI(this.properties);
      
      if (OpenUiByUrl.equalsIgnoreCase("Yes"))
         return ui.openUiAfterSignByUrl();
      else 
         return ui.openUiAfterSigninByMenu();
   }
   
   /**
    * <p>
    * get tennant information from full url after sign in.
    * </p>
    */
   private void getTenantInfo()
   {
      String currentUrl = this.getDriver().getCurrentUrl();
      
      String[] tmp = currentUrl.split("/"); 
      this.tenantInfo = tmp[3];
   }
   
   /**
    * Navigate to a specific ui through portal home page by extending the menus.
    * 
    * @param configurationPath
    *           layoutmap file of a ui                            
    * @return 
    *         return the xpath locator of a ui after the third level menu is extended
    */
   public String navigateToUi(final String configurationPath)
   {
      this.changeLayoutMap(LAYOUT_MAP_DIR + configurationPath);
      
      Browser browser = this.properties.getBrowser();
      
      String appFullName = this.properties.getApplicationfullname();
      String category = this.properties.getCategory();
      String uiMenuName = this.properties.getUiMenuName();
      
      return browser.navigateToUi(appFullName, category, uiMenuName);
   }
   /**
    * <p>
    * Pauses for the specified number of seconds.
    * </p>
    * 
    * <p>
    * <b>Avoid using this unless absolutely necessary</b>. The speed at which a
    * test runs is machine-dependent. For the same test, some machines may need
    * a pause after navigating to a record before typing in some data, while
    * other machines won't.</b>
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|pause for seconds;|2|
    * </pre>
    * 
    * @param seconds
    *           The number of seconds to pause (must be at least 1 second).
    * 
    * @return Whether or not the pause was successful.
    */
   public boolean pauseForSeconds(int seconds)
   {
      if (seconds < 1)
         throw new IllegalArgumentException("The pause must be at least 1 second.");

      return browserTiming.doPause(seconds * 1000);
   }

   /**
    * <p>
    * Press the tab in the specified widget. This action will clear any existing
    * value from the widget before typing a TAB.
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|press tab;|arDistributionCodesTEXTDESC|
    * </pre>
    * 
    * @param widgetName
    *           The name of the widget in question.
    * 
    * @return Whether or not the tab was successfully pressed into the widget.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget or the widget does not
    *            support this action.
    */
   public boolean pressTab(final String widgetName)
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.pressTab();
   }

   /**
    * <p>
    * Sets the specified widget's state to being selected.
    * </p>
    * 
    * <p>
    * Applies to tabs
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|select;|Comments|
    * </pre>
    * 
    * @param widgetName
    *           The name of the widget in question.
    * 
    * @return Whether or not the widget's state was set to being selected.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget or the widget does not
    *            support this action.
    */
   public boolean select(final String widgetName)
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.select();
   }

   /**
    * <p>
    * Sets the specified widget's state to being selected.
    * </p>
    * 
    * <p>
    * Applies to tabs
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|select;|Comments|
    * </pre>
    * 
    * @param widgetName
    *           The name of the widget in question.
    *           
    * @param index
    *           The index of an option in comboBox.         
    * 
    * @return Whether or not the widget's state was set to being selected.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget or the widget does not
    *            support this action.
    */
   public boolean selectByIdx(final String widgetName, final int index)
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.selectByIdx(index);
   }
   
   /**
    * <p>
    * Sets the specified option to being selected. 
    * (Note: For radio buttons, the user should pass the Label of the Radio Button to be
    * selected).
    * </p>
    * 
    * <p>
    * Applies to radio buttons, dropdownlists, listBox
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|select;|radiobuttonDetailSummary|Summary|
    * |ensure|select;|documentType|Credit Note|
    * 
    * ListBox:
    * |ensure|select;|availableSegment|Division|
    * </pre>
    * 
    * @param widgetName
    *           The name of the widget in question.
    * 
    * @param option
    *           The option to select.
    * 
    * @return Whether or not the option was set to being selected.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget or the widget does not
    *            support this action.
    */
   public boolean select(final String widgetName, final String option)
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.select(option);
   }
   
   /**
    * <p>
    * Select an option in a listbox.
    * In CNA2.0, listbox is originally implemented by listbox control which support 
    * multi-selection. But for Account Structure, mulit-selection should not be supported.
    * So Dev change the impelmentation to use div/div instead of listBox control.
    * This function is added per applcaiton change.  
    * </p>
    * 
    * @param widgetName
    *           The name of the widget in question.
    * 
    * @param option
    *           The option to select.
    * 
    * @return Whether or not the option was set to being selected.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget or the widget does not
    *            support this action.
    */
   public boolean selectOption(final String widgetName, final String option)
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.selectOption(option);
   }
   /**
    * <p>
    * Sets the specified option to being deselected. 
    * </p>
    * 
    * <p>
    * Applies to listBox
    * </p>
    * 
    * <pre>
    * ListBox:
    * |ensure|deselect;|availableSegment|Division|
    * </pre>
    * 
    * @param widgetName
    *           The name of the widget in question.
    * 
    * @param option
    *           The option to deselect.
    * 
    * @return Whether or not the option was set to being deselected.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget or the widget does not
    *            support this action.
    */
   public boolean deselect(final String widgetName, final String option)
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.deselect(option);
   }
   /**
    * 
    * <p>
    * Applies to listBox
    * </p>
    * 
    * @param widgetName
    *           The name of the widget in question.
    * 
    * @return Whether or not all the options were set to being deselected.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget or the widget does not
    *            support this action.
    */
   public boolean deselectAll(final String widgetName)
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.deselectAll();
   }
   /**
    * 
    * <p>
    * Applies to listBox
    * </p>
    * 
    * @param widgetName
    *           The name of the widget in question.
    * 
    * @return Whether or not all the options were set to being selected.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget or the widget does not
    *            support this action.
    */
   public boolean selectAll(final String widgetName)
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.selectAll();
   }

   /**
    * Selects the specified Date in the Calendar.
    * 
    * <p>
    * Note 1:You must call {@link #clickCalendar(String) clickCalendar} on the
    * specified widget before you can call this action.
    * </p>
    * <p>
    * Note 2:The format <b>date</b> should be "MM/DD/YYYY"
    * </p>
    * 
    * <p>
    * Applies to TextBoxes that have date / calendar capabilities.
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|click calendar;|oeOrdersORDDATE|
    * |ensure|select calendar date;|oeOrdersORDDATE|10/25/2011|
    * </pre>
    * 
    * @param widgetName
    *           The name of the widget in question.
    * @param date
    *           the string to be parsed as date.
    * 
    * @return <li><code>True </code>if date is selected successfully.</li> <li>
    *         <code>False</code> if date is not selected.</li>
    */
   public boolean selectCalendarDate(String widgetName, String date)
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.selectCalendarDate(date);
   }      
   
   /**
    * <p>
    * Select an option form a comboBox in a cell of the specified widget.
    * </p>
    * 
    * <p>
    * Applies to a comboBox in a cell of a table.
    * </p>
    * 
    * @param tableWidgetName
    *           The name of the table widget in question.
    * @param columnWidgetName
    *           The name of the column widget.
    * @param rowIndex
    *           The 1-based index of the row where the cell is located (in other
    *           words, use 1 for the first row).
    * @param value
    *           The value to select from the specified cell of the widget.
    * 
    * @return Whether or not the value was successfully typed into the specified
    *         cell of the widget.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget or the widget does not
    *            support this action.
    */
   public boolean selectFromCell(final String tableWidgetName, final String columnWidgetName, final int rowIndex, final String value)
   {
      FixtureWidget table = getFixtureWidget(tableWidgetName);
      FixtureWidget columnHeader = getFixtureWidget(columnWidgetName);
      String columnId = columnHeader.getWaitTargetLocator();
      int columnIdx = table.getIndexOfColumnById(columnId);
      
      return table.selectFromCell(rowIndex, columnIdx, value);
   }
   
   /**
    * <p>
    * Select an option form a comboBox in a cell of the specified widget without validation.
    * It's used for some special cases like cells of "ReadyToPost" in batchList grid of GL2000. 
    * When an option is selected from the comboBox, it's automatically wraped up and you can't 
    * do text verifcation like the way used in celectFromCell because <span> which holds the 
    * text value are gone. In addition, App has value validation here. Thought the option is 
    * correctly selected, the finally value shown in the cell may be different from the selected 
    * one if the validation fails. So this method will bypass the text validation. You can use
    * getCellText later to verify if the finaly value is as expected.
    * </p>
    * 
    * <p>
    * Applies to a special comboBox in a cell of a table, App has value validation on 
    * it immediately and each time after an option is selected, <span> and <select> are wrapped up.
    * </p>
    * 
    * @param tableWidgetName
    *           The name of the table widget in question.
    * @param columnWidgetName
    *           The name of the column widget.
    * @param rowIndex
    *           The 1-based index of the row where the cell is located (in other
    *           words, use 1 for the first row).
    * @param value
    *           The value to select from the specified cell of the widget.
    * 
    * @return Whether or not the value was successfully typed into the specified
    *         cell of the widget.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget or the widget does not
    *            support this action.
    */
   public boolean selectFromCellWithoutValidation(final String tableWidgetName, final String columnWidgetName, final int rowIndex, final String value)
   {
      FixtureWidget table = getFixtureWidget(tableWidgetName);
      FixtureWidget columnHeader = getFixtureWidget(columnWidgetName);
      String columnId = columnHeader.getWaitTargetLocator();
      int columnIdx = table.getIndexOfColumnById(columnId);
      
      return table.selectFromCellWithoutValidation(rowIndex, columnIdx, value);
   }
   
   /**
    * <p>
    * Signs into the UI with a default user ID of ADMIN, a default password of
    * ADMIN, a default (displayed) company name of SAMINC, and today's date.
    * This must be called after {@link #open() open}.
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|sign in;|
    * </pre>
    * 
    * @return Whether or not sign-in was successful.
    */
   public boolean signIn()
   {
      boolean result = ui.signIn();
      return result;
   }

   /**
    * <p>
    * Signs into the UI with the specified user ID, password, company name and
    * session date. This must be called after {@link #open() open}.
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|sign in;|sageonline|onpay12|
    * </pre>
    * 
    * @param userID
    *           The ID of the user to sign in as.
    * @param password
    *           The password of the user to sign in as.
    * 
    * @return Whether or not sign-in was successful.
    */
   public boolean signIn(final String userID, final String password)
   {
      boolean result = ui.signIn(userID, password);
      return result;
   }
   
   /**
    * <p>
    * Execute a sql statement to update the data directly by remote SQL server connection.
    * Used to change some global configuration data back to its original default value which is not allowed 
    * to change offically from GUI. like G/L Options, you can change the Decimal places from a biggen value 
    * to small ones, like from 3 to 0. 
    * </p>
    * 
    * @param sqlServer
    *           The name of the sql server to be connected.
    * @param login
    *           The login name to connect to the sql server.
    * @param password
    *           The password used for connecting to the sqlserver.
    * @param db
    *           The db(Comany name) used for connecting.
    * @param sqlStatement
    *           The sql statment to used to update the database.
    * 
    */
   public void sqlDbUpdate(final String sqlServer, final String login, final String password, final String db, final String sqlStatement)
   {
      String sqlCmd="sqlcmd -S " + sqlServer + " -U " + login + " -P " + password + " -d " + db + " -Q \"" + sqlStatement + "\"";
      System.out.println("sqlCmd: " + sqlCmd);
      try {
          Process process = Runtime.getRuntime().exec(sqlCmd);
          System.out.println("the output stream is "+process.getOutputStream());   
          BufferedReader reader=new BufferedReader(
                new InputStreamReader(process.getInputStream())); 

            while (reader.readLine() != null)
            {
                System.out.println("The inout stream is " + reader.readLine());
            }                   
      } catch (IOException e) {
          e.printStackTrace();
      }
   }
   

   /**
    * <p>
    * Switches to the main UI's context, so that subsequent fixture calls
    * involving widget names refer to widgets that belong to the main UI.
    * </p>
    * 
    * <p>
    * NOTE: If we were already in the main UI's context, nothing happens.
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |switch form context;|
    * </pre>
    */
   public void switchFormContext()
   {
      switchFormContext("");
   }

   /**
    * <p>
    * Switches to the specified form's context (where "" switches to the main
    * UI's context), so that subsequent fixture calls involving widget names
    * refer to widgets that belong to the specified form.
    * </p>
    * 
    * <p>
    * NOTE: If we were already in that form's context, nothing happens.
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |switch form context;|popupDetail|
    * </pre>
    * 
    * @param formName
    *           The name of the form to switch to, or "" to switch to the main
    *           form.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a form.
    */
   public void switchFormContext(final String formName)
   {
      // NOTE: Just in case FitNesse passes a blank table cell in as null,
      //       act as if null means "", a.k.a. the main UI.  Also note that
      //       internally the main UI's name must be "" (not null).
      String nonNullFormName = (null != formName) ? formName : "";
      if ((null == properties.getExistenceValidationWidget(nonNullFormName)))
      {
         throw new IllegalArgumentException("UI '" + properties.getUIName() + "' does not contain a popup form named '"
               + nonNullFormName + "'.");
      }

      currentFormName = nonNullFormName;
   }

   /**
    * <p>
    * Switches focus to the default content inside the window (either the first
    * frame on the web page, or the main document when a web page contains
    * iframes). If you are inside a frame and would like to switch to another
    * frame, you must switch to the default content before making a subsequent
    * {@link #switchToFrame(String) switchToFrame} call.
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|switch to frame;|agedPayablesPortlet_PORTLET2|
    * |note|do something inside this frame...|
    * |ensure|switch to default content;|
    * |ensure|switch to frame;|agedReceivablesPortlet_PORTLET5|
    * |note|do something inside this other frame...|
    * |ensure|switch to default content;|
    * |note|do something outside of the frames...|
    * </pre>
    * 
    * @return Whether or not the focus switch was successful.
    * 
    * @throws IllegalStateException
    *            The window had no default frame.
    */
   public boolean switchToDefaultContent()
   {
      Browser browser = properties.getBrowser();

      try
      {
         return browser.switchToDefaultContent();
      }
      catch (Exception e)
      {
         throw new IllegalStateException("The current window does not contain a default frame.");
      }
   }

   /**
    * <p>
    * Checks whether or not the control is switched to the Original Window.
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|wait for crystal report;| OEVIA01|
    * |check|get crystal report company name;|SAMINC|
    * |ensure|close crystal report;|
    * |ensure|switch to default window;|
    * </pre>
    * 
    * @return <li><code>true</code> The control is switched to the Original
    *         Window successfully.</li> <li>Otherwise</li>
    */
   public boolean switchToDefaultWindow()
   {
      Browser browser = properties.getBrowser();
      return browser.switchToDefaultWindow();
   }

   /**
    * <p>
    * Switches focus to the frame with the name. If you are inside a frame and
    * you want to switch to another frame, you must call
    * {@link #switchToDefaultContent() switchToDefaultContent} before switching
    * to that other frame.
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|switch to frame;|agedPayablesPortlet_PORTLET2|
    * |note|do something inside this frame...|
    * |ensure|switch to default content;|
    * |ensure|switch to frame;|agedReceivablesPortlet_PORTLET5|
    * |note|do something inside this other frame...|
    * |ensure|switch to default content;|
    * |note|do something outside of the frames...|
    * </pre>
    * 
    * @param frameName
    *           The name of the frame in question.
    * 
    * @return Whether or not a frame by that name was found and the focus switch
    *         was successful.
    */
   public boolean switchToFrame(final String frameName)
   {
      Browser browser = properties.getBrowser();
      return browser.switchToFrame(frameName);
   }

   /**
    * <p>
    * Switch from a parent frame to one of its child frame.
    * 
    * Don't need to go back to the parent, directly switching is ok
    * Need to switch to the corresponding form thereafter
    * </p>
    * 
    * @param frameName
    *           The name of the frame in question.
    * @param formWidgetName
    *           The widget name of the form in the new popup frame.
    * 
    * @return Whether or not a frame by that name was found and the focus switch
    *         was successful.
    */
   public boolean switchToChildFrame(final String frameName, final String formWidgetName)
   {
      try{
         this.getDriver().switchTo().frame(frameName);
         this.switchFormContext(formWidgetName);
      }
      catch(org.openqa.selenium.NoSuchFrameException e)
      {
         e.printStackTrace();
         return false;
      }
      return true;
   }
   
   /**
    * <p>
    * Switches focus to a new window that has just been opened. For example,
    * switching to a crystal report window that has just been invoked by
    * clicking Print in New tab.
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|switch to new window;|
    * </pre>
    *  
    * @return Whether or not a window by that name was found and the focus
    *         switch was successful.
    */
   public boolean switchToNewWindow()
   {
      Browser browser = properties.getBrowser();
      return browser.switchToNewWindow();
   }
   
   /**
    * Witch to the new window or frame for crystal report view.
    * 
    * @param OpenUiByUrl
    *           The way to opne the Ui. if open by chaning Url, it's "Yes", else othe other value means
    *           open by navigatig through portal menus.
    * @return Whether or not a window the switch is successful.          
    */
   public boolean switchToReportView(String OpenUiByUrl)
   {
     this.pauseForSeconds(10);
     if(OpenUiByUrl.equalsIgnoreCase("Yes"))
        return this.switchToNewWindow();
     else
        return switchToCrystalReportIFrameFromPortal();
   }
   
   /**
    * Switch to the new opened Crystal Report Viewer iFrame.
    * When open a screen from portal to print reports, should not open other screens
    * before clicking "Print" button on this screen. Framework assumes the iFrame Id of the
    * crystal report viewer is just the next one following its parent screen. 
    * e.g. when you open AP7201(A/P Batch Listing Report), say its iFrameID is "iFrameMenu2",
    * then if you click the "Print" button, the crystal report viewer will be opened with a 
    * iFrameID equals "iFrameMenu3". But if you open another screen like GL5100, this new screen 
    * will take "iFrameMenu3", later when you go back to AP7201 and do Print again, the crystal 
    * report viewer may take "iFrameMenu4" or other values which depends how many other screens
    * you've opened. In this case framework can't locate its exact iFrameId.
    *  
    * @return Whether or not the crystal report viewer iFrame was found and the focus
    *         switch was successful.
    */
   public boolean switchToCrystalReportIFrameFromPortal()
   {
      Browser browser = properties.getBrowser();
      return browser.switchToCrystalReportIFrameFromPortal();
   }
   
   /**
    * <p>
    * Switches focus to a new window that has just been opened. For example,
    * switching to a crystal report window that has just been invoked by
    * clicking Print in New tab.
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|switch to window;|OEVIA01|
    * </pre>
    * 
    * @param windowName
    *           The name of the window in question.
    * 
    * @return Whether or not a window by that name was found and the focus
    *         switch was successful.
    */
   public boolean switchToWindow(final String windowName)
   {
      Browser browser = properties.getBrowser();
      return browser.switchToWindow(windowName);
   }

   /**
    * <p>Take a screenshot of the current state of the browser session.
    * Typically, this will be called by a Junit rule for when a test
    * (that follows that rule) fails, then take a screen capture.</p>
    * 
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |$SCREENCAPTURE = |takeScreenshot|
    * </pre>
    *  
    * @return A string representing the name of the file
    *         that is the screen capture.
    */ 
   public String takeScreenshot()
   {
      Browser browser = properties.getBrowser();
      return browser.takeScreenshot();
   }
   
   /**
    * <p>
    * Types the specified value into the specified widget. This action will
    * clear any existing value from the widget before typing in the new value.
    * </p>
    * 
    * <p>
    * Applies to TextBoxes.
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|type;|arDistributionCodesTEXTDESC|I am replacing the description|
    * |ensure|type;|arDistributionCodesIDDIST|SALESR|
    * |ensure|type;|oeOrdersORDDATE|01/25/2019|
    * </pre>
    * 
    * @param widgetName
    *           The name of the widget in question.
    * @param value
    *           The value to type into the widget.
    * 
    * @return Whether or not the value was successfully typed into the widget.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget or the widget does not
    *            support this action.
    */
   public boolean type(final String widgetName, final String value)
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.type(value);
   }
   
   /**
    * <p>
    * Types the specified value into the specified widget. This action will
    * clear any existing value from the widget before typing in the new value.
    * Telephone number is specially treated with format like (123)-456-7890,
    * the text validation need follow this final format shown on the screen.
    * </p>
    * 
    * 
    * @param widgetName
    *           The name of the widget in question.
    * @param value
    *           The value to type into the widget.
    * 
    * @return Whether or not the value was successfully typed into the widget.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget or the widget does not
    *            support this action.
    */
   public boolean typeTelephoneNumber(final String widgetName, final String value)
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.typeTelephoneNumber(value);
   }
   
   /**
    * <p>
    * Types the specified value into the specified widget. This action will
    * clear any existing value from the widget before typing in the new value.
    * </p>
    * 
    * <p>
    * Applies to TextBoxes.
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|type;|arDistributionCodesTEXTDESC|I am replacing the description|
    * |ensure|type;|arDistributionCodesIDDIST|SALESR|
    * |ensure|type;|oeOrdersORDDATE|01/25/2019|
    * </pre>
    * 
    * @param widgetName
    *           The name of the widget in question.
    * @param value
    *           The value to type into the widget.
    * 
    * @return Whether or not the value was successfully typed into the widget.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget or the widget does not
    *            support this action.
    */
   public boolean typeByJavaScript(final String widgetName, final String value)
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.typeByJavaScript(value);
   }
   
   /**
    * <p>
    * Types the specified value into the specified widget. This action will
    * clear any existing value from the widget before typing in the new value.
    * No tab after the sendText.
    * </p>
    * 
    * <p>
    * Applies to TextBoxes.
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|type;|arDistributionCodesTEXTDESC|I am replacing the description|
    * |ensure|type;|arDistributionCodesIDDIST|SALESR|
    * |ensure|type;|oeOrdersORDDATE|01/25/2019|
    * </pre>
    * 
    * @param widgetName
    *           The name of the widget in question.
    * @param value
    *           The value to type into the widget.      
    * 
    * @return Whether or not the value was successfully typed into the widget.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget or the widget does not
    *            support this action.
    */
   public boolean typeNoTab(final String widgetName, final String value)
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.typeNoTab(value);
   }

   /**
    * <p>
    * Types the specified value into the specified cell of the specified widget.
    * This action will replace any existing value in the cell with the new
    * value. This action, if the cell is a Textbox, will tab out.
    * </p>
    * 
    * <p>
    * Applies to Textboxes of a table.
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|type into cell;|taxTable|Registration Number|1|454200|
    * </pre>
    * 
    * @param tableWidgetName
    *           The name of the table widget in question.
    * @param columnWidgetName
    *           The name of the columnHeader widget in question.
    * @param rowIndex
    *           The 1-based index of the row where the cell is located (in other
    *           words, use 1 for the first row).
    * @param value
    *           The value to type into the specified cell of the widget.
    * 
    * @return Whether or not the value was successfully typed into the specified
    *         cell of the widget.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget or the widget does not
    *            support this action.
    */   
   public boolean typeIntoCell(final String tableWidgetName, final String columnWidgetName, final int rowIndex, final String value)
   {    
      FixtureWidget table = getFixtureWidget(tableWidgetName);
      FixtureWidget columnHeader = getFixtureWidget(columnWidgetName);
      
      String columnId = columnHeader.getWaitTargetLocator();
      int columnIdx = table.getIndexOfColumnById(columnId);
      
      return table.typeIntoCellByColumnIdx(rowIndex, columnIdx, value);      

   }
   
   /**
    * <p>
    * Types the specified value into the specified cell of the specified widget.
    * This action will replace any existing value in the cell with the new
    * value. This action will not tab out after entering the value.
    * </p>
    * 
    * @param tableWidgetName
    *           The name of the table widget in question.
    * @param columnWidgetName
    *           The name of the columnHeader widget in question.
    * @param rowIndex
    *           The 1-based index of the row where the cell is located (in other
    *           words, use 1 for the first row).
    * @param value
    *           The value to type into the specified cell of the widget.
    * 
    * @return Whether or not the value was successfully typed into the specified
    *         cell of the widget.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget or the widget does not
    *            support this action.
    */
   public boolean typeIntoCellWithoutTab(final String tableWidgetName, final String columnWidgetName, final int rowIndex, final String value)
   {
      FixtureWidget table = getFixtureWidget(tableWidgetName);
      FixtureWidget columnHeader = getFixtureWidget(columnWidgetName);
      
      String columnId = columnHeader.getWaitTargetLocator();
      int columnIdx = table.getIndexOfColumnById(columnId);      
      
      return table.typeIntoCellWithoutTab(rowIndex, columnIdx, value); 
   }   
   
   /**
    * <p>
    * Types the specified value into the specified widget with Ctrl+A and Delete.
    * Clear() not stable for it will lose focus, so use this as a alternative.
    * e.g.: gl2100->Entry Number, if using type(), focus will be lost after clear()
    * and this element should be empty, so error popup. 
    * typeWithCtrlADel will use Ctrl+A then Delete, after that, focus is still on
    * this element. 
    * </p>
    * 
    * <p>
    * Applies to Textboxes when type() not working due to clear issue.
    * </p>
    * 
    * 
    * @param widgetName
    *           The name of the widget in question.
    * @param value
    *           The value to type into the specified cell of the widget.
    * 
    * @return Whether or not the value was successfully typed into the specified
    *         cell of the widget.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget or the widget does not
    *            support this action.
    */
   public boolean typeWithCtrlADel(final String widgetName, final String value)
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.typeWithCtrlADel(value);
   }
   
   /**
    * <p>
    * Types the specified value into the specified widget. This action WON'T
    * clear the existing value from the widget before typing in the new value.
    * If {@code doTab} is {@code true}, it will also "press" the Tab key after
    * typing in the value.
    * </p>
    * 
    * <p>
    * Applies to Textboxes.
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|type without clear;|arDistributionCodesTEXTDESC|I am appending more information|
    * </pre>
    * 
    * @param widgetName
    *           The name of the widget in question.
    * @param value
    *           The value to type into the widget.
    * 
    * @return Whether or not the value was successfully typed into the widget.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget or the widget does not
    *            support this action.
    */
   public boolean typeWithoutClear(final String widgetName, final String value)
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.typeWithoutClear(value);
   }

   /**
    * <p>
    * Types the specified value into the specified widget. This action WON'T
    * clear the existing value from the widget before typing in the new value.
    * If {@code doTab} is {@code true}, it will also "press" the Tab key after
    * typing in the value.
    * </p>
    * 
    * <p>
    * Applies to Textboxes.
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|type without clear and without tab;|arDistributionCodesTEXTDESC|I am appending more information|
    * </pre>
    * 
    * @param widgetName
    *           The name of the widget in question.
    * @param value
    *           The value to type into the widget.
    * 
    * @return Whether or not the value was successfully typed into the widget.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget or the widget does not
    *            support this action.
    */
   public boolean typeWithoutClearAndWithoutTab(final String widgetName, final String value)
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.typeWithoutClearAndWithoutTab(value);
   }

   /**
    * <p>
    * Types the specified value into the specified widget. This action will
    * clear any existing value from the widget before typing in the new value.
    * And will not tab after inputting as does the "type" action.
    * </p>
    * 
    * <p>
    *    Applies to Textboxes.
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|type without tab;|arDistributionCodesTEXTDESC|I am replacing the description|
    * |ensure|type without tab;|arDistributionCodesIDDIST|SALESR|
    * |ensure|type without tab;|oeOrdersORDDATE|01/25/2019|
    * </pre>
    * 
    * @param widgetName
    *           The name of the widget in question.
    * @param value
    *           The value to type into the widget.
    * 
    * @return Whether or not the value was successfully typed into the widget.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget or the widget does not
    *            support this action.
    */
   public boolean typeWithoutTab(final String widgetName, final String value)
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.typeWithoutTab(value);
   }

   /**
    * <p>
    * Clears the contents of the specified widget.
    * 
    * In the case of a check box, deselect it.
    * </p>
    * 
    * <p>
    * Applies to Checkboxes
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|uncheck;|oeOrdersONHOLD|
    * </pre>
    * 
    * @param widgetName
    *           The name of the widget in question.
    * 
    * @return Whether or not the wiget's contents were successfully cleared.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget or the widget does not
    *            support this action.
    */
   public boolean uncheck(final String widgetName)
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.uncheck();
   }   
   
   /**
    * <p>
    * Waits for the specified widget to appear before proceeding.
    * </p>
    * 
    * <p>
    * Applies to most widgets
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|wait for;|labelOeOrdersRUNNINGTOT|
    * </pre>
    * 
    * @param widgetName
    *           The name of the widget in question.
    * 
    * @return Whether or not the widget appeared before the default timeout was
    *         reached.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget.
    */
   public boolean waitFor(final String widgetName)
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.waitFor();
   }
   
   /**
    * <p>
    * Waits for the specified widget to appear and the ui is fully loaded.
    * </p>
    * 
    * @param iframe
    *           The id of the <iframe> for the ui.
    * 
    * @return Whether or not the the ui is fully loaded.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget.
    */
   public boolean waitForUiReady(final String iframe)
   {
      Browser browser = this.properties.getBrowser(); 
      String iframeLocator = "//div[@id='screenLayout']/iframe[@id='" + iframe + "']";
      browser.waitForElement(iframeLocator);
      this.getDriver().switchTo().frame(browser.findElement(iframe));
      return browser.waitForUiReady(properties.getSignInValidationElement());   
   }

   /**
    * <p>
    * Waits for the specified widget to not be visible before proceeding.
    * </p>
    * 
    * <p>
    * Applies to most widgets
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|wait for not visible;|distributionCode|
    * </pre>
    * 
    * @param widgetName
    *           The name of the widget in question.
    * 
    * @return Whether or not the widget disappeared before the default timeout was
    *         reached.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget.
    */
   public boolean waitForNotVisible(final String widgetName)
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.waitForNotVisible();
   }
   
   /**
    * <p>
    * Waits for the loadingImageDsiappear.
    * In CNA2.0, LoadingImage has the fixed id. don't need use the layoutmap
    * LoadingImage will hide some elements before it disappear, which makes clicking the hidden button failed
    * E.g.: in gl2100, click "Create New Batch" not working before loadingImage disappear
    * </p>
    * 
    * @return Whether or not the loadingImage disappear before the default timeout was
    *         reached.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget.
    */
   public boolean waitForLoadingImageDisappear()
   {
      for (int index = 0; index < 15; index ++)
      {
         if ((!this.getDriver().findElement(By.id("ajaxSpinner")).isDisplayed()))
         {
            return true;
         }
         TimeDelay.doPause(1000);
      }
      return false;
   }
   
   /**
    * <p>
    * Waits for the success message to get disappeared.
    * In CNA2.0, when click a save button, normally there's a success message popup on the top of screen.
    * Before move to next step like click "New", need wait until the success message disappears.
    * </p>
    * 
    * @param locator        
    *           the message locator, normally its visibility is controled by a timer  
    * 
    * @return Whether or not the success message disappear after waiting for a while
    * 
    */
   private boolean waitForSuccessMsgDisappear(String locator)
   {     
      for (int index = 0; index < 15; index ++)
      {
         if ((this.getDriver().findElement(By.xpath(locator)).isDisplayed()))
         {            
            for (int idx = 0; idx < 15; idx ++)
            {
               if ((!this.getDriver().findElement(By.xpath(locator)).isDisplayed()))
               {
                  return true;
               }
               TimeDelay.doPause(1000);
            }
            return false;
         }
         TimeDelay.doPause(1000);
      }
            
      return false;
   }
   
   /**
    * <p>
    * Waits for the confirmation dialogue box to be closed.
    * In CNA2.0, confirmatin dialogure box has the fixed structure. don't need use the layoutmapr
    * </p>
    * 
    * @param messageType        
    *           Success  -- Success Message in a normal form  
    *           successInReversing  -- Success Message shown during reverse a posted batch
    *           Error   --  Errors,Warning and Information in a normal form
    *           Popup  -- To error message in a Popup form
    *           inProcessing   --error or information message in a processing status Popup window
    *           processingResult -- the processing result grid in a processing popup window
    *           importResult   --message for import result
    *           exportResult   --message for export result
    *           Confirmation   -- confirmation dialog box 
    * 
    * @return Whether or not the message box is closed before the default timeout was
    *         reached.
    */

   public boolean waitForMessageBoxDisappear(String messageType)
   {
      String locator= this.getLocatorOfMsgBox(messageType);

      if (!locator.equals(""))
      {
         // Special treatment for success message box which is controled by a timer
         if (messageType.equals("Success") || messageType.equals("successInReversing"))
            return this.waitForSuccessMsgDisappear(locator);
         else
         {
            for (int index = 0; index < 15; index ++)
            {
               if ((!this.getDriver().findElement(By.xpath(locator)).isDisplayed()))
               {
                  return true;
               }
               TimeDelay.doPause(1000);
            }
            return false;
         }
      }else
         return false;
   }
   
   /**
    * <p>
    * Waits until the specified content is present in the widget (or not). This
    * method should be used after inputting or selecting a value in a widget
    * that causes content to display in another widget. Such as after inputting
    * a order number the customer number will be displayed. This action is a
    * better alternative than using a pause.
    * </p>
    * 
    * <p>
    * Applies to textboxes
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|type|oeOrdersORDNUMBER|ORD000000000001|
    * |ensure|wait for content;|oeOrdersCUSTOMER|1200|
    * </pre>
    * 
    * 
    * @param widgetName
    *           The widget name.
    * @param content
    *           The content expected to be present in the widget before the
    *           timeout.
    * @return Whether or not the specified content is present in the widget
    *         before the default timeout was reached.
    * 
    */
   public boolean waitForContent(final String widgetName, final String content)
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.waitForContent(content);
   }
   
   /**
    * <p>
    * Get the xpath format of the mesage box locator accoriding to messageType:
    * Below are the different types of message for CNA2:
    * Normal Forms :
    * <div id="success"></div> -- Success Message   
    * <div id="message"></div> -- Errors,Warning and Information
    *  
    * Pop-Up Forms:
    * <div id="messagePopup"></div> -- To show error message in Popup
    * 
    * Imort and Export: 
    * <div id="importResultMessageDiv"></div> -- Used in Import 
    * <div id="exportResultMessageDiv"></div> -- Used in Export
    *  
    * Processing Screens - Popup
    * <div id="messageDiv">@CommonResx.Processing</div>  --To show processing……. In Export,Import and processing screens
    *
    * Confirmation box - Popup
    * <div id="deleteConfirmation" ...</div>
    *  
    * @param messageType        
    *           Success  -- Success Message in a normal form  
    *           successInReversing  -- Success Message shown during reverse a posted batch
    *           Error   --  Errors,Warning and Information in a normal form
    *           Popup  -- To error message in a Popup form
    *           inProcessing   --error or information message in a processing status Popup window
    *           processingResult -- the processing result grid in a processing popup window
    *           importResult   --message for import result
    *           exportResult   --message for export result
    *           Confirmation   -- confirmation dialog box 
    * 
    * @return the xpath of the message box locator.
    * 
    */
   private String getLocatorOfMsgBox(final String messageType)
   {      
      String msgLocator ;
      if( messageType.equals("Success"))
         msgLocator = "//div[@id='success']";
      else if( messageType.equals("successInReversing"))
         msgLocator = "//div[@id='windowSuccess']";      
      else if (messageType.equals("Error"))
         msgLocator = "//div[@id='message']";
      else if (messageType.equals("Popup"))
         msgLocator = "//div[@id='messagePopup']";
      else if (messageType.equals("inProcessing"))
         msgLocator = "//div[@id='messageDiv']";
      else if (messageType.equals("processingResult"))
         msgLocator = "//div[@id='processingResultGrid']";
      else if (messageType.equals("importResult"))
         msgLocator = "//div[@id='importResultMessageDiv']";
      else if (messageType.equals("exportResult"))
         msgLocator = "//div[@id='exportResultMessageDiv']";
      else if (messageType.equals("Confirmation"))
         msgLocator = "//div[@class='k-overlay']/following-sibling::*[1]//div[@id='deleteConfirmation']";
      else
         msgLocator = "";
      return msgLocator;  
   }  

   /**
    * <p>
    * Waits until the message/dialog box appears.
    * For CNA2, we have limited message box type, see below:
    *
    * @param messageType        
    *           Success  -- Success Message in a normal form  
    *           successInReversing  -- Success Message shown during reverse a posted batch
    *           Error   --  Errors,Warning and Information in a normal form
    *           Popup  -- To error message in a Popup form
    *           inProcessing   --error or information message in a processing status Popup window
    *           processingResult -- the processing result grid in a processing popup window
    *           importResult   --message for import result
    *           exportResult   --message for export result
    *           Confirmation   -- confirmation dialog box 
    * 
    * @return Whether or not the message box appears before the default
    *         timeout was reached.
    * 
    */
   public boolean waitForMessageBoxForCNA2(final String messageType)
   {      
      String locator= this.getLocatorOfMsgBox(messageType);
      if (!locator.equals(""))
         return ui.getBrowser().waitForElement(locator);
      else
         return false;
   }
   
   /**
    * <p>
    * Get the text information from the message box.
    * For CNA2, we have limited message box type, see below:
    *
    * @param messageType        
    *           Success  -- Success Message in a normal form
    *           successInReversing  -- Success Message shown during reverse a posted batch
    *           Error   --  Errors,Warning and Information in a normal form
    *           Popup  -- To error message in a Popup form
    *           inProcessing   --error or information message in a processing status Popup window
    *           processingResult -- the processing result grid in a processing popup window
    *           importResult   --message for import result
    *           exportResult   --message for export result
    *           Confirmation   -- confirmation dialog box 
    * 
    * @return 
    *           The text information from the message box.
    * 
    */
   public String getTextFromMessageBox(final String messageType)
   {      
      String locator= this.getLocatorOfMsgBox(messageType);
      if( waitForMessageBoxForCNA2(messageType))
      {
         if(messageType.equals("Error"))
            locator = locator + "/div/div[2]";
         if(messageType.equals("Confirmation"))
            locator = "//div[@id='body-text']";
         return ui.getBrowser().getDriver().findElement(By.xpath(locator)).getText().trim();
      }else
         return "";
    }
   
   /**
    * <p>
    * Check if the text shown in a message box contains some expected string. Used when only partial string
    * need be verified instead of the whole text in the message box.
    *
    * @param messageType        
    *           Success  -- Success Message in a normal form
    *           successInReversing  -- Success Message shown during reverse a posted batch
    *           Error   --  Errors,Warning and Information in a normal form
    *           Popup  -- To error message in a Popup form
    *           inProcessing   --error or information message in a processing status Popup window
    *           processingResult -- the processing result grid in a processing popup window
    *           importResult   --message for import result
    *           exportResult   --message for export result
    *           Confirmation   -- confirmation dialog box 
    *           
    * @param str        
    *           String expected to be contained in the message box. 
    * 
    * @return 
    *           Whether or not the text in a message box contains an expected string.
    * 
    */
   public Boolean textInMessageBoxContains(final String messageType, final String str)
   {      
      return this.getTextFromMessageBox(messageType).contains(str);
    }
   /**
    * <p>
    * Waits until the message dialog box appears. If text is specified then it
    * will also be used to validate the existence of the specified window.
    * 
    * Actually, this is not specific to 'messages' and can actually work for any
    * window that opens. A regular expression can be used in place of the
    * windowTitle parameter is also allowed. For example, .*Preview.*.RPT.*
    * would match a window with a title for "SAMLTD - Payroll Accruals Report -
    * [Preview for c:\Sage\Sage 300 ERP\CP61A\ENG\CPACCRU1.RPT]".
    * 
    * ".*Preview.*.RPT.*" translates to find a window title that has any
    * character(s) that is followed by "
    * Preview", followed by any character, followed by ".RPT", following by any
    * character.
    * 
    * This action is a better alternative than using a pause. The default
    * timeout is 60 (seconds).
    * </p>
    * 
    * <p>
    * Applies to message dialogs
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|wait for message dialog;|Confirmation||
    * 
    * or 
    * |ensure|wait for message dialog;|.*Preview.*.RPT.*| |
    * </pre>
    * 
    * @param messageType
    *           the type of message which actually is the window title.
    *           "Confirmation", "Error", "Information", or "Warning are acceptable
    *           message dialog types.
    * 
    * @param windowText
    *           some text in the window we are waiting for. This can be left
    *           with an empty string "".
    * 
    * @return Whether or not the message box control appears before the default
    *         timeout was reached.
    * 
    * @throws IllegalArgumentException
    *            This UI does not contain a message dialog box control.
    */
   public boolean waitForMessageDialog(final String messageType, final String windowText)
   {
      final int timeout = 60; // 1 minute (60 seconds)

      if (!(this.waitForMessageDialog(messageType, windowText, timeout)))
      {
         throw new StopTestException("The expected message dialog did not appear");
      }

      return true;
   }

   /**
    * <p>
    * Waits until the message dialog box appears for specified timeout period.
    * If text is specified then it will also be used to validate the existence
    * of the specified window.
    * 
    * Actually, this is not specific to 'messages' and can actually work for any
    * window that opens. A regular expression can be used in place of the
    * windowTitle parameter is also allowed. For example, .*Preview.*.RPT.*
    * would match a window with a title for "SAMLTD - Payroll Accruals Report -
    * [Preview for c:\Sage\Sage 300 ERP\CP61A\ENG\CPACCRU1.RPT]".
    * 
    * ".*Preview.*.RPT.*" translates to find a window title that has any
    * character(s) that is followed by "
    * Preview", followed by any character, followed by ".RPT", following by any
    * character.
    * 
    * This action is a better alternative than using a pause. Used in
    * performance testing to provide a bigger timeout period.
    * </p>
    * 
    * <p>
    * Applies to Message dialogs
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|wait for message dialog;|Confirmation| | 300|
    * 
    * or 
    * |ensure|wait for message dialog;|.*Preview.*.RPT.*| | 300|
    * </pre>
    * 
    * @param messageType
    *           the type of message which actually is the window title.
    *           "Confirmation", "Error", or another window title that represents
    *           the message dialog.
    * 
    * @param windowText
    *           some text in the window we are waiting for. This can be left
    *           with an empty string "".
    * 
    * @param timeout
    *           specified timeout to wait for. In seconds.
    * 
    * @return Whether or not the message box control appears before the default
    *         timeout was reached.
    * 
    * @throws IllegalArgumentException
    *            This UI does not contain a message dialog box control.
    */
   public boolean waitForMessageDialog(final String messageType, final String windowText, final int timeout)
   {      
      String msgLocator = ui.getMessageBox(messageType, true).getLocator();
             
      return ui.getBrowser().waitForElement(msgLocator);
   }
   
   /**
    * <p>
    * Waits until the confirmation dialog box appears for specified timeout period.
    * In CNA2.0, the confirmatin dialogue has structure like <div id='deleteConfirmation'> 
    * 
    * @return Whether or not the message box control appears before the default
    *         timeout was reached.
    * 
    * @throws IllegalArgumentException
    *            This UI does not contain a message dialog box control.
    */
   public boolean waitForConfirmationDialog()
   {      
      // Get the visible confirmation dialogue box instead of the first one
      String msgLocator = "//div[@class='k-overlay']/following-sibling::*[1]//div[@id='deleteConfirmation']";
      return ui.getBrowser().waitForElement(msgLocator);       
   }
   
   /**
    * <p>
    * Waits until the error message box appears for specified timeout period.
    * In CNA2.0, the error dialogue has structure like <div id='message'> 
    * 
    * @return Whether or not the message box control appears before the default
    *         timeout was reached.
    * 
    * @throws IllegalArgumentException
    *            This UI does not contain a message dialog box control.
    */   
   public boolean waitForErrorMessage()
   {      
      String msgLocator = "//div[@id='message']";
      return ui.getBrowser().waitForElement(msgLocator);       
   }
   
   /**
    * <p>
    * Verify if expected error messages are shown in the error message box .
    * @param errorInformation
    *           the expected error information. 
    * @return Whether or not the error message box contains expected error information.
    * 
    * @throws IllegalArgumentException
    *            This UI does not contain a message dialog box control.
    */   
   public boolean errorMessageContains(final String errorInformation)
   {      
      //Get the visible confirmation dialogue box instead of the first one
      String baseMsgLocator = "//div[@id='message']/div/div[2]/ul";
      String errorInfo = ui.getBrowser().getDriver().findElement(By.xpath(baseMsgLocator)).getAttribute("innerHTML");
      return errorInfo.contains(errorInformation);
   }
   
   /**
    * <p>
    * Waits for no more requests of the product, using Javascript and checking of JQuery.
    * Use this after a call that invokes processing of the application. It is especially
    * useful when there is no other way to "wait for" something (like a disabled or
    * enabled element) because it doesn't apply or doesn't work.
    * 
    * This could be built into the framework after doing a "click" or "type" or something, however
    * if this framework is used for performance testing then separating out the method 
    * is important so that the timestamping can occur around the wait call, in order
    * to determine the time it takes to process. 
    * </p>
    * 
    * <p>
    * Applies to web page
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|wait for no requests;|
    * </pre>
    * 
    * @return Whether or not the wait for no requests succeeded or not (timed out).
    * 
    */
   public boolean waitForNoRequests()
   {
      Browser browser = properties.getBrowser();
      return browser.waitForNoRequests();
   }   
   
   /**
    * <p>
    * Waits until the specified widget becomes disabled. This action is a better
    * alternative than using a pause.
    * </p>
    * 
    * <p>
    * Applies to most widgets
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|wait until disabled;|buttonShipAll|
    * </pre>
    * 
    * @param widgetName
    *           The widget name.
    * 
    * @return Whether or not the specified widget becomes disabled before the
    *         default timeout was reached.
    * 
    * @throws IllegalArgumentException
    *            This UI does not contain a widget by that name.
    */
   public boolean waitUntilDisabled(final String widgetName)
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.waitUntilDisabled();
   }

   /**
    * <p>
    * Waits until the specified widget becomes enabled (not disabled). This
    * method should be used after inputting or selecting a value in a widget
    * that causes data to load. Such as after inputting a customer number, or
    * item number and before inputting a widget that was just enabled. This
    * action is a better alternative than using a pause.
    * </p>
    * 
    * <p>
    * Applies to most widgets
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|type|finderOEORDDItemNo|A1-103/0|
    * |ensure|wait until enabled;|textboxOEORDDQtyOrdered|
    * </pre>
    * 
    * @param widgetName
    *           The widget name.
    * 
    * @return Whether or not the specified widget becomes enabled before the
    *         default timeout was reached.
    * 
    * @throws IllegalArgumentException
    *            This UI does not contain a widget by that name.
    */
   public boolean waitUntilEnabled(final String widgetName)
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.waitUntilEnabled();
   }

   /**
    * <p>
    * Checks whether or not a window with the specified title currently exists
    * in the DOM.
    * </p>
    * 
    * <p>
    * Applies to a web page
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|window exists;|O/E Templates|
    * </pre>
    * 
    * @param windowTitle
    *           The title of the window in question.
    * 
    * @return Whether or not a window with the specified title currently exists
    *         in the DOM.
    */
   public boolean windowExists(final String windowTitle)
   {
      Browser browser = properties.getBrowser();
      return browser.isWindowTitleExist(windowTitle);
   }

   /**
    * <p>
    * Clicks the Crystal Report viewer's Export button(with a Print icon)
    * </p>
    * 
    * @return Whether or not the Export button was clicked successfully.
    */
   public boolean clickCrystalReportExport()
   {
      return ui.getCrystalReport(true).clickExportButton(); 
   }

   /**
    * <p>
    * Clicks the specified cell in the specified widget.
    * </p>
    *
    * <p>
    * Applies to Tables
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|click finder in cell;|optionalFieldsTable|Default Value|1|
    * </pre>
    * 
    * @param tableWidgetName
    *           The name of the table widget in question.
    * @param columnWidgetName
    *           The name of the column widget in question.
    * @param rowIndex
    *           The 1-based index of the row where the cell is located (in other
    *           words, use 1 for the first row).
    * @return Whether or not the specified cell in the widget was clicked
    *         successfully.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget or the widget does not
    *            support this action.
    */
   public boolean clickFinderInCell(final String tableWidgetName, final String columnWidgetName, final int rowIndex)
   {
      FixtureWidget table = getFixtureWidget(tableWidgetName);
      FixtureWidget columnHeader = getFixtureWidget(columnWidgetName);
      String columnId = columnHeader.getWaitTargetLocator();
      int columnIdx = table.getIndexOfColumnById(columnId);
            
      return table.clickFinderInCell(rowIndex, columnIdx);
   } 
   
   /**
    * <p>
    * Clicks a Pencil Edit Button in a specified cell in a Table widget.
    * </p>
    *
    * @param tableWidgetName
    *           The name of the table widget in question.
    * @param columnWidgetName
    *           The name of the column widget in question.
    * @param rowIndex
    *           The 1-based index of the row where the cell is located (in other
    *           words, use 1 for the first row).
    * @return Whether or not the specified cell in the widget was clicked
    *         successfully.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget or the widget does not
    *            support this action.
    */
   public boolean clickPencilButtonInCell(final String tableWidgetName, final String columnWidgetName, final int rowIndex)
   {
      FixtureWidget table = getFixtureWidget(tableWidgetName);
      FixtureWidget columnHeader = getFixtureWidget(columnWidgetName);
      String columnId = columnHeader.getWaitTargetLocator();
      int columnIdx = table.getIndexOfColumnById(columnId);
            
      return table.clickPencilButtonInCell(rowIndex, columnIdx);
   } 
   
   /**
    * <p>
    * get the fitnesse Test Path.
    * </p>
    * 
    * @param fitnesseTestPath
    *           The original test path of identified in  ${TESTPATH}.
    *           e.g.:FrontPage.AccpacAcceptanceTests.Sage300Web.SuiteGl4105\TestBt4105
    * @return testPath
    *           The testPath exculding the the test Name
    *           e.g.:FrontPage.AccpacAcceptanceTests.Sage300Web.SuiteGl4105
    *           TestName is TestBt4105 and it can be get it by ${PAGE_NAME}
    */
   public String getFitnesseTestPath(final String fitnesseTestPath)
   {
      String[] parts = fitnesseTestPath.split("\\.");
      String testPath = "";
      for (int i=0; i < parts.length-1; i++)
      {
         testPath = testPath + parts[i] + "\\";
      }
      String[] lastParts = parts[parts.length-1].split("\\\\");
      testPath = testPath + lastParts[0] + "\\";
      return testPath;
   }
   
   
//   /**
//    * <p>
//    * Clicks the Crystal Report Viewer's go to "First Page" Button.
//    * </p>
//    * 
//    * <p>
//    * NOTE: You must call {@link #switchToWindow(String) switchToWindow} before
//    * you can call this action.
//    * </p>
//    * 
//    * <p>
//    * <b>FitNesse example(s):</b>
//    * </p>
//    * 
//    * <pre>
//    * |ensure|switch to window;|OEVIA01|
//    * |ensure|click crystal report go to first page;|
//    * </pre>
//    * 
//    * @return Whether or not the Go to First Page Button was clicked
//    *         successfully.
//    */
//   public boolean clickCrystalReportGoToFirstPage()
//   {
//      return ui.getCrystalReport().getFirstPageButton().click();
//   }
//
//   /**
//    * <p>
//    * Clicks the Crystal Report Viewer's go to "Last Page" Button.
//    * </p>
//    * 
//    * <p>
//    * NOTE: You must call {@link #switchToWindow(String) switchToWindow} before
//    * you can call this action.
//    * </p>
//    * 
//    * <p>
//    * <b>FitNesse example(s):</b>
//    * </p>
//    * 
//    * <pre>
//    * |ensure|switch to window;|OEVIA01|
//    * |ensure|click crystal report go to last page;|
//    * </pre>
//    * 
//    * @return Whether or not the go to Last Page Button was clicked
//    *         successfully.
//    */
//   public boolean clickCrystalReportGoToLastPage()
//   {
//      return ui.getCrystalReport().getLastPageButton().click();
//   }
//
//   /**
//    * <p>
//    * Clicks the Crystal Report Viewer's go to "Next Page" button.
//    * </p>
//    * 
//    * <p>
//    * NOTE: You must call {@link #switchToWindow(String) switchToWindow} before
//    * you can call this action.
//    * </p>
//    * 
//    * <p>
//    * <b>FitNesse example(s):</b>
//    * </p>
//    * 
//    * <pre>
//    * |ensure|switch to window;|OEVIA01|
//    * |ensure|click crystal report go to next page;|
//    * </pre>
//    * 
//    * @return Whether or not the go to Next Page Button was clicked
//    *         successfully.
//    */
//   public boolean clickCrystalReportGoToNextPage()
//   {
//      return ui.getCrystalReport().getNextPageButton().click();
//   }
//
//   /**
//    * <p>
//    * Clicks the Crystal Report viewer's go to "Previous Page" button.
//    * </p>
//    * 
//    * <p>
//    * NOTE: You must call {@link #switchToWindow(String) switchToWindow} before
//    * you can call this action.
//    * </p>
//    * 
//    * <p>
//    * <b>FitNesse example(s):</b>
//    * </p>
//    * 
//    * <pre>
//    * |ensure|switch to window;|OEVIA01|
//    * |ensure|click crystal report go to previous page;|
//    * </pre>
//    * 
//    * @return Whether or not go to Previous Page button was clicked
//    *         successfully.
//    */
//   public boolean clickCrystalReportGoToPreviousPage()
//   {
//      return ui.getCrystalReport().getPreviousPageButton().click();
//   }
//
//   /**
//    * <p>
//    * Clicks the Crystal Report Viewer's Print Button
//    * </p>
//    * 
//    * <p>
//    * NOTE: You must call {@link #switchToWindow(String) switchToWindow} before
//    * you can call this action.
//    * </p>
//    * 
//    * <p>
//    * <b>FitNesse example(s):</b>
//    * </p>
//    * 
//    * <pre>
//    * |ensure|switch to window;|OEVIA01|
//    * |ensure|click crystal report print;|
//    * </pre>
//    * 
//    * @return Whether or not the Print Button was clicked successfully.
//    */
//   public boolean clickCrystalReportPrint()
//   {
//      return ui.getCrystalReport().getPrintButton().click();
//   }
//
//   /**
//    * <p>
//    * Clicks the Crystal Report Viewer's "Search for text" Button.
//    * </p>
//    * 
//    * <p>
//    * NOTE: You must call {@link #switchToWindow(String) switchToWindow} before
//    * you can call this action.
//    * </p>
//    * 
//    * <p>
//    * <b>FitNesse example(s):</b>
//    * </p>
//    * 
//    * <pre>
//    * |ensure|switch to window;|OEVIA01|
//    * |ensure|click crystal report search;|
//    * </pre>
//    * 
//    * @return Whether or not "Search for text" Button was clicked successfully.
//    */
//   public boolean clickCrystalReportSearch()
//   {
//      return ui.getCrystalReport().getSearchButton().click();
//   }   
//   

   /**
    * =========================================================================
    * Sikuli related fixture actions
    * 
    */
   
   /**
    * <p>
    * Clicks the specified image.
    * To be used only by CNA 1 - remote desktop session
    * which has the requirement to use Siluki for image recognition
    * UI automation.
    * </p>
    * 
    * <p>
    * Applies to Button Image
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|click image;|sage300TitlebarClose|
    * </pre>
    * 
    * @param widgetName
    *           The name of the image in question.
    * 
    * @return Whether or not the image was clicked successfully.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a image or the image does not
    *            support this action.
    */   
   public boolean clickImage(final String widgetName) 
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.clickImage();     
   }   
   
   /**
    * <p>
    * Double-Clicks the specified image.
    * To be used only by CNA 1 - remote desktop session
    * which has the requirement to use Siluki for image recognition
    * UI automation.
    * </p>
    * 
    * <p>
    * Applies to Button Image
    * </p>
    * 
    * @param widgetName
    *           The name of the image in question.
    * 
    * @return Whether or not the image was clicked successfully.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a image or the image does not
    *            support this action.
    */   
   public boolean doubleClickImage(final String widgetName) 
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);      
      return fixtureWidget.doubleClickImage();     
   }
   
   
   /**
    * <p>
    * Right-click the specified image.
    * To be used only by CNA 1 - remote desktop session
    * which has the requirement to use Siluki for image recognition
    * UI automation.
    * </p>
    * 
    * <p>
    * Applies to Button Image
    * </p>
    * 
    * 
    * @param widgetName
    *           The name of the image in question.
    * 
    * @return Whether or not the image was clicked successfully.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a image or the image does not
    *            support this action.
    */   
   public boolean rightClickImage(final String widgetName) 
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.rightClickImage();     
   }   

   
   /**
    * <p>
    * Select the specified image, that is in a menu structure.
    * To be used only by CNA 1 - remote desktop session
    * which has the requirement to use Siluki for image recognition
    * UI automation.
    * </p>
    * 
    * <p>
    * Applies to Label Image
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|select Menu Item From Image;|toolbar|file|close|
    * </pre>
    * 
    * @param widgetName
    *           The name of the image in question.
    * 
    * @param menuTitle
    *           The name of the menu title in question.
    *           
    * @param menuItem
    *           The name of the menu itme in question.
    *           
    * @return Whether or not the image was clicked successfully.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a image or the image does not
    *            support this action.
    */      
   public boolean selectMenuItemFromImage(String widgetName, String menuTitle, String menuItem)
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.selectMenuItemFromImage(menuTitle, menuItem);
   }   
   
   /**
    * <p>
    * Type into the specified image.
    * To be used only by CNA 1 - remote desktop session
    * which has the requirement to use Siluki for image recognition
    * UI automation.
    * </p>
    * 
    * <p>
    * Applies to TextBox image
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|type into image;|password|ADMIN|
    * </pre>
    * 
    * @param widgetName
    *           The name of the image in question.
    * 
    * @param value
    *           The value to type.
    *                      
    * @return Whether or not the image was clicked successfully.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a image or the image does not
    *            support this action.
    */         
   public boolean typeIntoImage(String widgetName, String value)
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.typeIntoImage(value);
   }
   
   
   /**
    * <p>
    * After recognizing the image, pressing specific keys 
    * to mimic certain actions, such as Ctrl+X, Ctrl+V, and the Del key.
    * </p>
    * 
    * <p>To be used by CNA 1.0 only </p> 
    * 
    * @param widgetName
    *           The name of the widget in question.
    * 
    * @param keyToHold
    *          The key to hold while pressing other character keys in charToPress
    *          
    * @param charToPress
    *          The character keys to type while holding the key in keyToHold
    * 
    * @return Whether or not the tab was successfully pressed into the widget.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget, keyToPress not recognized,
    *            or the widget does not support this action.
    */
   public boolean pressKeySequence(final String widgetName, final String keyToHold, final String charToPress)
   {
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.pressKeySequence(keyToHold, charToPress);
   }
   
   /**
    * <p>
    * Wait for the specified image.
    * To be used only by CNA 1 - remote desktop session
    * which has the requirement to use Siluki for image recognition
    * UI automation.
    * </p>
    * 
    * <p>
    * Applies to any image
    * </p>
    * 
    * <p>
    * <b>FitNesse example(s):</b>
    * </p>
    * 
    * <pre>
    * |ensure|wait for image;|opencompany|
    * </pre>
    * 
    * @param widgetName
    *           The name of the image in question.
    *                       
    * @return Whether or not the image was clicked successfully.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a image or the image does not
    *            support this action.
    */      
   public boolean waitForImage(final String widgetName) 
   {      
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.waitForImage();
   }     
   
   /**
    * <p>
    * Wait for the specified image for a specific period of time.
    * To be used only by CNA 1 - remote desktop session
    * which has the requirement to use Siluki for image recognition
    * UI automation.
    * </p>
    * 
    * <p>
    * Applies to any image
    * </p> 
    * 
    * 
    * @param widgetName
    *           The name of the image in question.
    *        timeout
    *           The time, in seconds, to wait for the image to appear
    *                       
    * @return Whether or not the image was clicked successfully.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a image or the image does not
    *            support this action.
    */      
   public boolean waitForImage(final String widgetName, final int timeout) 
   {      
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      return fixtureWidget.waitForImage(timeout);
   }
   
   
   /**
    * <p>
    * Wait for the specific optional prompt. If it is there then it should accompanied by a 
    * button to continue.  
    * 
    * To be used only by CNA 1 - remote desktop session
    * which has the requirement to use Siluki for image recognition
    * UI automation.
    * </p>
    * 
    * <p>
    * Applies to any image
    * </p>
    *  
    * 
    * @param widgetName
    *           The name of the image in question.
    *        widgetToClick
    *           The name of the image/button to click when the image is found
    *        timeout
    *           The time it should wait for this image to appear
    *                       
    * @return Whether or not the image was found. If not, it returns true assuming it never shows up,
    *          since it is only optional.  It will also return true if the dialogue box is closed properly via
    *          the button click.  Otherwise, it will return false.
    * 
    * @throws IllegalArgumentException
    *            The UI has found the image but the clicking doesn't work afterwards
    *            
    */      
   public boolean waitForOptionalImage(final String widgetName, final String widgetToClick, final int timeout) 
   {      
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      FixtureWidget fixtureWidgetClick = getFixtureWidget(widgetToClick);
      
      boolean clickSuccess=false;
      if (fixtureWidget.waitForImage(timeout)){      
         // If it returns true, then the image exist,  continue to try clicking on the next         
         // Note that this still generates an FindFailed exception if the optional window doesn't appear.
         // However, it will not be caught by and therefore interfer with the Jenkin's test         
         assertTrue("Not able to click 2nd action " + widgetToClick + ", although successfully detected " + widgetName, clickSuccess=fixtureWidgetClick.clickImage());
      } else {
         return true;       // Consider it  ok since the image is optional and doesn't show up 
      }  
      return clickSuccess;
   }
   
   /**
    * <p>
    * Check to see if a specific image appears.  If it does then it will return false. 
    * Such new image should accompany with a way to close it.  
    * 
    * To be used only by CNA 1 - remote desktop session
    * which has the requirement to use Siluki for image recognition
    * UI automation.
    * </p>
    * 
    * <p>
    * Applies to any image
    * </p>
    *  
    * 
    * @param widgetName
    *           The name of the image  to look for
    *        widgetToClick
    *           The name of the image/button to click when the image is found
    *        timeout
    *           The time it should wait before checking for this image
    *                       
    * @return Whether or not the image was found. If not, it returns true becaues we don't expect it to appear.
    *          If the image does appear, or it is not closed properly, it will return false.
    * 
    * @throws IllegalArgumentException
    *            The UI has not found the image.
    *            
    */
   public boolean checkForUnexpectedImage(final String widgetName, final String widgetToClick, final int waitPeriod) 
   {      
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      FixtureWidget fixtureWidgetClick = getFixtureWidget(widgetToClick);
      // Pause to give time a previous action to complete.
      // We have to do this or else it will give a NULL error 
      this.pauseForSeconds(waitPeriod);      
      if (fixtureWidget.waitForImage(1)){      
         /// If it returns true, then the image exist,  continue to try clicking on the next         
         /// Note that this still generates an FindFailed exception if the optional window doesn't appear.
         /// However, it will not be caught and therefore interfer with the Jenkin's test         
         assertTrue("Error: Not able to click 2nd action '" + widgetToClick + "', although successfully detected unwanted image '" + widgetName+"'", fixtureWidgetClick.clickImage());
      } else {
         return true;       // Returns true confirming the unwanted image doesn't show up. 
      }  
      return false;     // If it can get to this point, the image shows up and we should return false.       
   }   
   
   /**
    * <p>
    * Check to see if a specific image appears.  If it does then it will return false. 
    * 
    * To be used only by CNA 1 - remote desktop session
    * which has the requirement to use Siluki for image recognition
    * UI automation.
    * </p>
    * 
    * <p>
    * Applies to any image
    * </p>
    *  
    * 
    * @param widgetName
    *           The name of the image  to look for
    *        timeout
    *           The time it should wait before checking for this image 
    *                       
    * @return Whether or not the image was found. If not, it returns true becaues we don't expect it to appear.
    *          If the image does appear, or it is not closed properly, it will return false.
    * 
    * @throws IllegalArgumentException
    *            The UI has not found the image.
    *            
    */
   public boolean checkForUnexpectedImage(final String widgetName, final int waitPeriod) 
   {      
      FixtureWidget fixtureWidget = getFixtureWidget(widgetName);
      // Pause to give time a previous action to complete, then check if our image exists.
      // We have to do this or otherwise it will yield a NULL error
      this.pauseForSeconds(waitPeriod);
      if (fixtureWidget.waitForImage(1)){      
         /// If it returns true, then the image exist,  continue to try clicking on the next         
         /// Note that this still generates an FindFailed exception if the optional window doesn't appear.
         /// However, it will not be caught and therefore interfer with the Jenkin's test         
         return false;
      } else {
         return true;       // Returns true confirming the unwanted image doesn't show up. 
      }         
   }
   
   //Put the writeResultToLog here
   public void writeResultToLog(String path, String detail){
      LoggingHelper.writeResultToLog(path,detail);
   }
   
   public void writeResultToLog(String path, String detail, boolean captureImage){
      LoggingHelper.writeResultToLog(path,detail,captureImage);
   }
   
}
