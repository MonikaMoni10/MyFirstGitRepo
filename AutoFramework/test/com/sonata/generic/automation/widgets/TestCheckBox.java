/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.widgets;

import static org.junit.Assert.assertTrue;

import org.easymock.EasyMock;
import org.junit.Test;

import com.sonata.generic.automation.browser.Browser;
import com.sonata.generic.automation.widgets.CheckBox;

/**
 * The <code>TestCheckBox</code> class provides the JUnit tests on the
 * {@link CheckBox} class
 */
public class TestCheckBox
{
   /** The locator must not be null */
   //   @Test(expected = IllegalArgumentException.class)
   //   public void checkBoxMustHaveLocator()
   //   {
   //      new CheckBox(null, EasyMock.createNiceMock(Browser.class));
   //   }

   /** The browser must not be null */
   @Test(expected = IllegalArgumentException.class)
   public void checkBoxMustHaveBrowser()
   {
      new CheckBox("mock", null);
   }

   /** Select (Check) the check box passes it to the browser */
   @Test
   public void canSelectCheckBox()
   {
      final String locator = "mockCheckBox";
      final Browser browser = getBrowserExpectingSelectCheckBox(locator);
      final CheckBox checkBox = new CheckBox(locator, browser);
      assertTrue(checkBox.select());
      EasyMock.verify(browser);
   }

   /** Clear (Uncheck) the check box passes it to the browser */
   @Test
   public void canClearCheckBox()
   {
      final String locator = "mockCheckBox";
      final Browser browser = getBrowserExpectingClearCheckBox(locator);
      final CheckBox checkBox = new CheckBox(locator, browser);
      assertTrue(checkBox.clear());
      EasyMock.verify(browser);
   }

   /**
    * Validates if the check box is selected (checked) or not, it returns a
    * browser as well
    */
   @Test
   public void canValidateCheckBoxState()
   {
      final String locator = "mockCheckBox";
      final Browser browser = getBrowserWithCheckBoxState(locator);
      final CheckBox checkBox = new CheckBox(locator, browser);
      assertTrue(checkBox.isSelected());
      EasyMock.verify(browser);
   }

   // Private helper functions

   /**
    * returns a mock browser that expects to have check box selected
    * 
    * @param locator
    *           the locator of the check box
    * @return a {@link Browser} that expects to have check box selected
    */
   private Browser getBrowserExpectingSelectCheckBox(final String locator)
   {
      Browser browser = EasyMock.createMock(Browser.class);
      EasyMock.expect(browser.selectCheckBox("//span[@id='" + locator + "']/input")).andReturn(true)
            .once();
      EasyMock.replay(browser);
      return browser;
   }

   /**
    * returns a mock browser that expects to have check box cleared
    * 
    * @param locator
    *           the locator of the check box
    * @return a {@link Browser} that expects to have check box cleared
    */
   private Browser getBrowserExpectingClearCheckBox(final String locator)
   {
      Browser browser = EasyMock.createMock(Browser.class);
      EasyMock.expect(browser.clearCheckBox("//span[@id='" + locator + "']/input")).andReturn(true).once();
      EasyMock.replay(browser);
      return browser;
   }

   /**
    * returns a mock browser with validation if a check box is selected
    * 
    * @param locator
    *           the locator of the check box
    * @return a {@link Browser} with validation if a check box is selected
    */
   private Browser getBrowserWithCheckBoxState(final String locator)
   {
      Browser browser = EasyMock.createMock(Browser.class);
      EasyMock.expect(browser.isSelected("//span[@id='" + locator + "']/input")).andReturn(true).once();
      EasyMock.replay(browser);
      return browser;
   }

}
