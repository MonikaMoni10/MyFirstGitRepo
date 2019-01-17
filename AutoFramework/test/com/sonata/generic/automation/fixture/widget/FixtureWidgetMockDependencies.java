/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.fixture.widget;

import org.easymock.EasyMock;

import com.sonata.generic.automation.browser.Browser;
import com.sonata.generic.automation.browser.SpecialKey;
import com.sonata.generic.automation.browser.SpecialKeyCombo;
import com.sonata.generic.automation.fixture.widget.FixtureWidget;

/**
 * Class containing mock dependencies for various kinds of {@link FixtureWidget}
 * objects under test.
 */
class FixtureWidgetMockDependencies
{
   private final Browser mockBrowser;

   /**
    * Constructs an object that has mock dependencies for a fixture widget.
    */
   public FixtureWidgetMockDependencies()
   {
      this.mockBrowser = createMockBrowser();
   }

   /**
    * Replays the mocks - this must be called right before exercising the class
    * under test.
    */
   public void replayMocks()
   {
      EasyMock.replay(mockBrowser);
   }

   /**
    * Verifies the mocks - this should be called after exercising the class
    * under test.
    */
   public void verifyMocks()
   {
      EasyMock.verify(mockBrowser);
   }

   /**
    * Gets the mock {@link Browser}.
    * 
    * @return The mock {@link Browser}.
    */
   public Browser getMockBrowser()
   {
      return mockBrowser;
   }

   /**
    * Creates a mock {@link Browser}.
    * 
    * @return mock {@link Browser}.
    */
   private static Browser createMockBrowser()
   {

      final String buttonLocator = "mockUINamemockButtonID";
      final String labelLocator = "mockUINamemockLabelID";
      final String textBoxLocator = "mockUINamemockTextBoxID";
      final String textAreaLocator = "mockUINamemockTextAreaID";
      final String passwordTextBoxLocator = "mockUINamemockPasswordTextBoxID";
      final String dateBoxLocator = "mockUINamemockDateBoxID-m_dateBox";
      final String radioGroupLocator = "mockUINamemockRadioGroupID";

      final String tableID = "mockUINamemockTableID-table";
      final String tableLocator = "//table[@id='" + tableID + "']";

      Browser browser = EasyMock.createMock(Browser.class);

      /* Button actions */
      EasyMock.expect(browser.click(buttonLocator)).andReturn(true).anyTimes();
      EasyMock.expect(browser.isVisible(buttonLocator)).andReturn(true).anyTimes();

      /* Text Box actions */
      EasyMock.expect(browser.isVisible(textBoxLocator)).andReturn(true).anyTimes();
      EasyMock.expect(browser.type("//div[@id='" + textBoxLocator + "']/input", "This is a text box.", true))
            .andReturn(true).anyTimes();
      EasyMock
            .expect(
                  browser.type("//div[@id='" + textBoxLocator + "']/input",
                        "This is a text box, input it but do not tab out after", false)).andReturn(true).anyTimes();
      EasyMock.expect(browser.exists("//div[@id='" + textBoxLocator + "']/input")).andReturn(true).anyTimes();
      EasyMock.expect(browser.getText("//div[@id='" + textBoxLocator + "']/input")).andReturn("This is a text box.")
            .anyTimes();

      /* Password Text Box actions */
      // NOTE: We do not allow getting the text out of the password text box,
      //       So don't add that EasyMock expectation.
      EasyMock.expect(browser.clearText(passwordTextBoxLocator)).andReturn(true).anyTimes();
      EasyMock.expect(browser.isVisible(passwordTextBoxLocator)).andReturn(true).anyTimes();
      EasyMock.expect(browser.type(passwordTextBoxLocator, "my password", true)).andReturn(true).anyTimes();
      EasyMock.expect(browser.typeWithoutClear(passwordTextBoxLocator, "my password", false)).andReturn(true)
            .anyTimes();

      /* Date Box actions */
      EasyMock.expect(browser.isVisible(dateBoxLocator)).andReturn(true).anyTimes();
      EasyMock.expect(browser.type("//div[@id='" + dateBoxLocator + "']/input", "12/25/2020", true)).andReturn(true)
            .anyTimes();
      EasyMock.expect(browser.getText("//div[@id='" + dateBoxLocator + "']/input")).andReturn("12/25/2020").anyTimes();
      EasyMock.expect(browser.click("//div[@id='" + dateBoxLocator.replace("m_dateBox", "m_btnPicker") + "']/img"))
            .andReturn(true).anyTimes();

      /* Label actions */
      EasyMock.expect(browser.isVisible(labelLocator)).andReturn(true).anyTimes();
      EasyMock.expect(browser.getText(labelLocator)).andReturn("This is a label!").anyTimes();

      /* Table actions */
      final String cell1 = tableLocator + "/thead/tr[1]/th[1]";
      final String cell2 = tableLocator + "/thead/tr[1]/th[2]";
      final String cell3 = tableLocator + "/thead/tr[1]/th[3]";
      final String exceptionCell = tableLocator + "/thead/tr[1]/th[4]";
      final String tbody = tableLocator + "/tbody/tr[2]/td[2]";
      final String tableNavigation = "//table[@id='" + tableID.replace("-table", "")
            + "']/tbody/tr[1]/td[1]/table/tbody/tr[3]/td/table/tbody/tr";
      final String currentPageLocator = tableNavigation + "/td[3]/input";
      final String totalPagesLocator = tableNavigation + "/td[4]/div";
      final String rowActionMenuLocator = "//div[@id='RowActionMenuPanel']/div/div/table/tbody";
      final String spinnerLocator = tableID + "-loadingIndicator";

      // clickCell
      EasyMock.expect(browser.getText(cell1)).andReturn("First Column").anyTimes();
      EasyMock.expect(browser.getText(cell2)).andReturn("Second Column").anyTimes();
      EasyMock.expect(browser.getText(cell3)).andReturn("Third Column").anyTimes();
      EasyMock.expect(browser.getText(exceptionCell)).andThrow(new NullPointerException()).anyTimes();
      //EasyMock.expect(browser.click(tbody)).andReturn(true).anyTimes();
      EasyMock.expect(browser.click(tbody, true)).andReturn(true).anyTimes();
      EasyMock.expect(browser.click(tbody + "/div/input", false)).andReturn(true).anyTimes();

      // getCellText
      EasyMock.expect(browser.exists(tableLocator + "/tbody/tr[1]/td[1]/div")).andReturn(true).anyTimes();
      EasyMock.expect(browser.exists(tableLocator + "/tbody/tr[1]/td[1]/div/a")).andReturn(false).anyTimes();
      EasyMock.expect(browser.exists(tableLocator + "/tbody/tr[1]/td[1]/div/button")).andReturn(false).anyTimes();
      EasyMock.expect(browser.exists(tableLocator + "/tbody/tr[1]/td[1]/div/span")).andReturn(false).anyTimes();
      EasyMock.expect(browser.isDisabled(tableLocator + "/tbody/tr[1]/td[1]")).andReturn(true).anyTimes();
      EasyMock.expect(browser.isDisabled(tableLocator + "/tbody/tr[1]/td[1]/div")).andReturn(true).anyTimes();

      EasyMock.expect(browser.exists(tableLocator + "/tbody/tr[2]/td[1]/div")).andReturn(true).anyTimes();
      EasyMock.expect(browser.exists(tableLocator + "/tbody/tr[2]/td[1]/div/a")).andReturn(false).anyTimes();
      EasyMock.expect(browser.exists(tableLocator + "/tbody/tr[2]/td[1]/div/button")).andReturn(false).anyTimes();
      EasyMock.expect(browser.getAttribute(tableLocator + "/tbody/tr[2]/td[1]/div/input", "type")).andReturn("")
            .anyTimes();

      EasyMock.expect(browser.exists(tableLocator + "/tbody/tr[2]/td[2]/div")).andReturn(true).anyTimes();
      EasyMock.expect(browser.exists(tableLocator + "/tbody/tr[2]/td[2]/div/a")).andReturn(false).anyTimes();
      EasyMock.expect(browser.exists(tableLocator + "/tbody/tr[2]/td[2]/div/button")).andReturn(false).anyTimes();
      EasyMock.expect(browser.getAttribute(tableLocator + "/tbody/tr[2]/td[2]/div/input", "type")).andReturn("")
            .anyTimes();

      EasyMock.expect(browser.exists(tableLocator + "/tbody/tr[2]/td[2]/div/input")).andReturn(true).anyTimes();
      EasyMock.expect(browser.getText(tableLocator + "/tbody/tr[2]/td[2]/div/input"))
            .andReturn("Text: column 2, row 2").anyTimes();

      EasyMock.expect(browser.exists(tableLocator + "/tbody/tr[2]/td[3]/div")).andReturn(true).anyTimes();
      EasyMock.expect(browser.exists(tableLocator + "/tbody/tr[2]/td[3]/div/a")).andReturn(false).anyTimes();
      EasyMock.expect(browser.exists(tableLocator + "/tbody/tr[2]/td[3]/div/button")).andReturn(false).anyTimes();
      EasyMock.expect(browser.exists(tableLocator + "/tbody/tr[2]/td[3]/div/input")).andReturn(false).anyTimes();
      EasyMock.expect(browser.exists(tableLocator + "/tbody/tr[2]/td[3]/div/span/input")).andReturn(false).anyTimes();
      EasyMock.expect(browser.exists(tableLocator + "/tbody/tr[2]/td[3]/div/select")).andReturn(true).anyTimes();
      EasyMock.expect(browser.getText(tableLocator + "/tbody/tr[2]/td[3]/div/select"))
            .andReturn("Text: row 2, column 3").anyTimes();

      // get row count (=2)
      EasyMock.expect(browser.exists(tableLocator + "/tbody/tr[1]/td[1]")).andReturn(true).anyTimes();
      EasyMock.expect(browser.exists(tableLocator + "/tbody/tr[1]/td[2]")).andReturn(false).anyTimes();
      EasyMock.expect(browser.getText(tableLocator + "/tbody/tr[1]/td[2]")).andReturn("Text1").anyTimes();
      EasyMock.expect(browser.exists(tableLocator + "/tbody/tr[2]/td[1]")).andReturn(true).anyTimes();
      EasyMock.expect(browser.exists(tableLocator + "/tbody/tr[2]/td[2]")).andReturn(false).anyTimes();
      EasyMock.expect(browser.getText(tableLocator + "/tbody/tr[2]/td[2]")).andReturn("Text2").anyTimes();
      EasyMock.expect(browser.exists(tableLocator + "/tbody/tr[3]/td[1]")).andReturn(false).anyTimes();
      EasyMock.expect(browser.isDisabled(tableLocator + "/tbody/tr[2]/td[2]/div")).andReturn(false).anyTimes();

      // isCellDataPresent
      EasyMock.expect(browser.getText(tableLocator + "/tbody/tr[1]/td[1]")).andReturn("Cell text").anyTimes();

      // typeIntoCell
      EasyMock.expect(browser.exists(tableLocator + "/tbody/tr[2]/td[1]/div/input")).andReturn(true).anyTimes();
      EasyMock.expect(browser.type(tableLocator + "/tbody/tr[2]/td[1]/div/input", "Text: column 1, row 2", true))
            .andReturn(true).anyTimes();

      // pressInRow
      EasyMock.expect(browser.click(tableLocator + "/tbody/tr[4]/td[2]/div")).andReturn(true).anyTimes();
      EasyMock.expect(browser.type(tableLocator + "/tbody/tr[4]/td[2]/div", SpecialKeyCombo.ALT__DELETE))
            .andReturn(true).anyTimes();

      EasyMock.expect(browser.click(tableLocator + "/tbody/tr[5]/td[2]/div")).andReturn(true).anyTimes();
      EasyMock.expect(browser.type(tableLocator + "/tbody/tr[5]/td[2]/div", SpecialKey.INSERT)).andReturn(true)
            .anyTimes();

      EasyMock.expect(browser.click(tableLocator + "/tbody/tr[6]/td[2]/div")).andReturn(true).anyTimes();
      EasyMock.expect(browser.type(tableLocator + "/tbody/tr[6]/td[2]/div", SpecialKeyCombo.ALT__BACK_SPACE))
            .andReturn(true).anyTimes();

      EasyMock.expect(browser.click(tableLocator + "/tbody/tr[7]/td[2]")).andReturn(true).anyTimes();
      EasyMock.expect(browser.type(tableLocator + "/tbody/tr[7]/td[2]/div", SpecialKey.ESCAPE)).andReturn(true)
            .anyTimes();

      EasyMock.expect(browser.click(tableLocator + "/tbody/tr[2]/td[1]/div/button")).andReturn(true).anyTimes();

      EasyMock.expect(browser.click(rowActionMenuLocator + "/tr[2]/td")).andReturn(true).anyTimes();

      EasyMock.expect(browser.exists(rowActionMenuLocator + "/tr[1]/td")).andReturn(true).anyTimes();
      EasyMock.expect(browser.getText(rowActionMenuLocator + "/tr[1]/td")).andReturn("Menu 1").anyTimes();
      EasyMock.expect(browser.click(rowActionMenuLocator + "/tr[1]/td")).andReturn(true).anyTimes();

      EasyMock.expect(browser.exists(rowActionMenuLocator + "/tr[2]/td")).andReturn(true).anyTimes();
      EasyMock.expect(browser.exists(rowActionMenuLocator + "/tr[3]/td")).andReturn(true).anyTimes();
      EasyMock.expect(browser.exists(rowActionMenuLocator + "/tr[4]/td")).andReturn(true).anyTimes();
      EasyMock.expect(browser.exists(rowActionMenuLocator + "/tr[5]/td")).andReturn(true).anyTimes();
      EasyMock.expect(browser.exists(rowActionMenuLocator + "/tr[6]/td")).andReturn(false).anyTimes();

      EasyMock.expect(browser.getText(rowActionMenuLocator + "/tr[3]/td")).andReturn("Menu 3").anyTimes();

      EasyMock.expect(browser.isDisabledByClass(rowActionMenuLocator + "/tr[1]/td")).andReturn(true).anyTimes();
      EasyMock.expect(browser.isDisabledByClass(rowActionMenuLocator + "/tr[2]/td")).andReturn(true).anyTimes();
      EasyMock.expect(browser.isDisabledByClass(rowActionMenuLocator + "/tr[3]/td")).andReturn(true).anyTimes();
      EasyMock.expect(browser.isDisabledByClass(rowActionMenuLocator + "/tr[4]/td")).andReturn(true).anyTimes();
      EasyMock.expect(browser.isDisabledByClass(rowActionMenuLocator + "/tr[5]/td")).andReturn(true).anyTimes();
      EasyMock.expect(browser.isDisabledByClass(rowActionMenuLocator + "/tr[6]/td")).andReturn(false).anyTimes();

      // Return cell type as Label
      EasyMock.expect(browser.exists(tableLocator + "/tbody/tr[1]/td[1]/div/input")).andReturn(false).anyTimes();
      EasyMock.expect(browser.exists(tableLocator + "/tbody/tr[1]/td[1]/div/span/input")).andReturn(false).anyTimes();
      EasyMock.expect(browser.exists(tableLocator + "/tbody/tr[1]/td[1]/div/select")).andReturn(false).anyTimes();

      // TABLE NAVIGATION

      // goToPage
      EasyMock.expect(browser.click(currentPageLocator, false)).andReturn(true).anyTimes();
      EasyMock.expect(browser.type(currentPageLocator, SpecialKeyCombo.SHIFT__F8__END)).andReturn(true).anyTimes();
      EasyMock.expect(browser.typeWithoutClear(currentPageLocator, "1", true)).andReturn(true).anyTimes();

      // getCurrentPage
      EasyMock.expect(browser.getText(currentPageLocator)).andReturn("1").anyTimes();

      // getCurrentPage
      EasyMock.expect(browser.getText(totalPagesLocator)).andReturn("2").anyTimes();

      // Table Finder actions   
      EasyMock.expect(browser.click(tableLocator + "/tbody/tr[1]/td[2]/div/span[2]/button")).andReturn(true).anyTimes();

      EasyMock
            .expect(
                  browser
                        .click("//div[@class='swt-Finder-dropDownPanel']/div/table/tbody/tr[2]/td/div/table/tbody/tr[1]/td[1]/div[1]/div[2]/div[1]/table/tbody/tr[1]"))
            .andReturn(true).anyTimes();
      EasyMock
            .expect(
                  browser
                        .exists("//div[@class='swt-Finder-dropDownPanel']/div/table/tbody/tr[2]/td/div/table/tbody/tr[1]/td[1]/div[1]/div[2]/div[1]/table/tbody/tr[1]"))
            .andReturn(true).anyTimes();
      EasyMock
            .expect(
                  browser
                        .getText("//div[@class='swt-Finder-dropDownPanel']/div/table/tbody/tr[2]/td/div/table/tbody/tr[1]/td[1]/div[1]/div[2]/div[1]/table/tbody/tr[1]/td[1]"))
            .andReturn("Mock Item").anyTimes();
      EasyMock
            .expect(
                  browser
                        .exists("//div[@class='swt-Finder-dropDownPanel']/div/table/tbody/tr[2]/td/div/table/tbody/tr[1]/td[1]/div[1]/div[2]/div[1]/table/tbody/tr[2]"))
            .andReturn(false).anyTimes();

      final String view = tableID.replace("-table", "") + "-viewPanel-viewButton";
      final String showColumnsLocator = "//div[@class='swt-TableSettingPopupPanel']/div/table/tbody/tr[1]";

      EasyMock.expect(browser.click(view)).andReturn(true).anyTimes();
      EasyMock.expect(browser.click(view + "-popupPanel")).andReturn(true).anyTimes();
      EasyMock.expect(browser.selectCheckBox(showColumnsLocator + "/td[1]/span/input")).andReturn(true)
            .anyTimes();
      EasyMock.expect(browser.exists(showColumnsLocator + "/td[1]/span")).andReturn(true).anyTimes();
      EasyMock.expect(browser.getText(showColumnsLocator + "/td[2]/div/div")).andReturn("Mock Item").anyTimes();
      EasyMock.expect(browser.selectCheckBox(showColumnsLocator + "/td[2]/span/input")).andReturn(false)
            .anyTimes();
      EasyMock.expect(browser.clearCheckBox(showColumnsLocator + "/td[1]/span/input")).andReturn(true).anyTimes();

      //Wait for Table to load
      EasyMock.expect(browser.exists(tableLocator + "/tbody/tr[1]/td[2]/div")).andReturn(true).once();
      EasyMock.expect(browser.exists(tableLocator + "/tbody/tr[1]/td[2]/div/a")).andReturn(true).once();
      EasyMock.expect(browser.waitForElement(tableLocator + "/tbody/tr[1]/td[2]/div/a")).andReturn(false).once();

      /*Radio Group actions*/

      EasyMock.expect(browser.exists("//div[@id='" + radioGroupLocator + "']/table")).andReturn(true).once();
      EasyMock.expect(browser.getAttribute("//div[@id='" + radioGroupLocator + "']/table", "class"))
            .andReturn("swt-HorizontalPanel").once();
      EasyMock.expect(browser.exists("//div[@id='" + radioGroupLocator + "']/table/tbody/tr/td[1]")).andReturn(true)
            .once();
      EasyMock.expect(browser.exists("//div[@id='" + radioGroupLocator + "']/table/tbody/tr/td[2]")).andReturn(true)
            .once();
      EasyMock.expect(browser.getText("//div[@id='" + radioGroupLocator + "']/table/tbody/tr/td[1]/span"))
            .andReturn("mockRadioButton").once();
      EasyMock.expect(browser.getText("//div[@id='" + radioGroupLocator + "']/table/tbody/tr/td[2]/span"))
            .andReturn("mockRadioButton1").once();
      EasyMock.expect(browser.getAttribute("//div[@id='" + radioGroupLocator + "']/table/tbody/tr/td[2]/span", "id"))
            .andReturn("mock_reportDetailSummary").once();
      EasyMock.expect(browser.selectCheckBox("//span[@id='mock_reportDetailSummary']/input"))
            .andReturn(true).anyTimes();

      EasyMock.expect(browser.exists("//div[@id='" + radioGroupLocator + "']/table")).andReturn(true).once();
      EasyMock.expect(browser.getAttribute("//div[@id='" + radioGroupLocator + "']/table", "class"))
            .andReturn("swt-HorizontalPanel").once();
      EasyMock.expect(browser.exists("//div[@id='" + radioGroupLocator + "']/table/tbody/tr/td[1]")).andReturn(true)
            .once();
      EasyMock.expect(browser.exists("//div[@id='" + radioGroupLocator + "']/table/tbody/tr/td[2]")).andReturn(true)
            .once();
      EasyMock.expect(browser.getText("//div[@id='" + radioGroupLocator + "']/table/tbody/tr/td[1]/span"))
            .andReturn("mockRadioButton").once();
      EasyMock.expect(browser.getText("//div[@id='" + radioGroupLocator + "']/table/tbody/tr/td[2]/span"))
            .andReturn("mockRadioButton2").once();
      EasyMock.expect(browser.getAttribute("//div[@id='" + radioGroupLocator + "']/table/tbody/tr/td[2]/span", "id"))
            .andReturn("mock_reportDetailSummary1").once();
      EasyMock.expect(browser.isSelected("//span[@id='mock_reportDetailSummary1']/input")).andReturn(true).anyTimes();

      return browser;
   }
}
