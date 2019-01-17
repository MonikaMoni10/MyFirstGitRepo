/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.widgets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.easymock.EasyMock;
import org.junit.Test;

import com.sonata.generic.automation.browser.Browser;
import com.sonata.generic.automation.widgets.ComboBox;

/**
 * The <code>TestComboBox</code> class provides the JUnit tests on the
 * {@link ComboBox} class
 */
public class TestComboBox
{
   private static String DEFAULT_COMBOBOX_LOCATOR = "defaultComboBoxLocator";
   private static String DEFAULT_COMBOBOX_VALUE   = "defaultComboBoxValue";

   /** The name must not be null */
   @Test(expected = IllegalArgumentException.class)
   public void comboBoxMustHaveName()
   {
      new ComboBox(null, EasyMock.createNiceMock(Browser.class));
   }

   /** The browser must not be null */
   @Test(expected = IllegalArgumentException.class)
   public void comboBoxMustHaveBrowser()
   {
      new ComboBox("mockComboBox", null);
   }

   /**
    * can returns value from comboBox.
    * 
    */
   @Test
   public void canGetComboBoxSelectedValue()
   {
      Browser browser = getBrowserComboBoxSelectedValue();
      final ComboBox comboBox = new ComboBox(DEFAULT_COMBOBOX_LOCATOR, browser);
      assertEquals(DEFAULT_COMBOBOX_VALUE, comboBox.getSelectedValue());
      EasyMock.verify(browser);
   }

   /**
    * returns a mock browser that expects to be selected
    * 
    * @return a {@link Browser} that expects to have comboBox selected
    */
   private Browser makeBrowserSelectedComboBoxDefaultValue()
   {
      Browser browser = EasyMock.createMock(Browser.class);
      EasyMock.expect(browser.selectComboBox(DEFAULT_COMBOBOX_LOCATOR, DEFAULT_COMBOBOX_VALUE)).andReturn(true).once();
      EasyMock.replay(browser);
      return browser;
   }

   /**
    * returns a mock browser that expects return text from comboBox default
    * value value
    * 
    * @return a {@link Browser} that expects to have comboBox selected
    */
   private Browser getBrowserComboBoxSelectedValue()
   {
      Browser browser = EasyMock.createMock(Browser.class);
      EasyMock.expect(browser.getText(DEFAULT_COMBOBOX_LOCATOR)).andReturn("defaultComboBoxValue");
      EasyMock.replay(browser);
      return browser;
   }

   /**
    * can returns value from comboBox.
    * 
    */
   @Test
   public void canSelectComboBoxValue()
   {
      Browser browser = makeBrowserSelectedComboBoxDefaultValue();
      ComboBox comboBox = new ComboBox(DEFAULT_COMBOBOX_LOCATOR, browser);
      assertTrue(comboBox.selectComboBox(DEFAULT_COMBOBOX_VALUE, true));
      EasyMock.verify(browser);
   }

   /** Waiting for content returns true */
   @Test
   public void waitForContentIsTrue()
   {
      final String locator = "mockTextBox";
      final String content = "mockSomeContent";

      Browser browser = EasyMock.createMock(Browser.class);
      EasyMock.expect(browser.waitForContent(locator, content)).andReturn(true).once();
      EasyMock.replay(browser);

      final ComboBox comboBox = new ComboBox(locator, browser);
      assertTrue(comboBox.waitForContent(content));

      EasyMock.verify(browser);
   }

   /** Waiting for content returns false */
   @Test
   public void waitForContentIsFalse()
   {
      final String locator = "mockTextBox";
      final String content = "mockSomeContent";

      Browser browser = EasyMock.createMock(Browser.class);
      EasyMock.expect(browser.waitForContent(locator, content)).andReturn(false).once();
      EasyMock.replay(browser);

      final ComboBox comboBox = new ComboBox(locator, browser);
      assertFalse(comboBox.waitForContent(content));

      EasyMock.verify(browser);
   }
}
