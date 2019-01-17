/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.widgets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.easymock.EasyMock;
import org.junit.Test;

import com.sonata.generic.automation.browser.Browser;
import com.sonata.generic.automation.widgets.TextBox;

/**
 * The <code>TestTextBox</code> class provides the JUnit tests on the
 * {@link TextBox} class
 */
public class TestTextBox
{
   /** The locator must not be null */
   @Test(expected = IllegalArgumentException.class)
   public void textBoxMustHaveLocator()
   {
      new TextBox(null, EasyMock.createNiceMock(Browser.class));
   }

   /** The browser must not be null */
   @Test(expected = IllegalArgumentException.class)
   public void textBoxMustHaveBrowser()
   {
      new TextBox("mockTextBox", null);
   }

   /** Clearing the Text Box returns true when successful */
   @Test
   public void canClearTextBox()
   {
      final String locator = "mockTextBox";
      final Browser browser = getBrowserExpectingClearTextBox("//div[@id='" + locator + "']/input");
      final TextBox textBox = new TextBox(locator, browser);
      assertTrue(textBox.clear());
      EasyMock.verify(browser);
   }

   /** Clearing and validating the Text Box returns true when successful */
   @Test
   public void canClearAndValidateTextBox()
   {
      final String locator = "mockTextBox";
      final String validationString = "ABC";
      final Browser browser = getBrowserExpectingClearAndValidateTextBox("//div[@id='" + locator + "']/input",
            validationString);
      final TextBox textBox = new TextBox(locator, browser);
      assertTrue(textBox.clearAndValidate("ABC"));
      EasyMock.verify(browser);
   }

   /** can get the expected text from text box */
   @Test
   public void canGetTextBoxText()
   {
      final String locator = "mockText";
      Browser browser = getBrowserExpectingText("//div[@id='" + locator + "']/input");
      final TextBox textBox = new TextBox(locator, browser);
      assertEquals("ABC", textBox.getText());
      EasyMock.verify(browser);
   }

   /** will not get incorrect text box text */
   @Test
   public void willNotGetIncorrectTextBoxText()
   {
      final String locator = "mockText";
      Browser browser = getBrowserExpectingText("//div[@id='" + locator + "']/input");
      final TextBox textBox = new TextBox(locator, browser);
      assertNotSame("123", textBox.getText());
      EasyMock.verify(browser);
   }

   /** Typing in the Text Box returns true when successful */
   @Test
   public void canTypeInTextBox()
   {
      final String locator = "mockTextBox";
      final String contents = "ABC";
      final Browser browser = getBrowserExpectingTypeInTextBox("//div[@id='" + locator + "']/input", contents);
      final TextBox textBox = new TextBox(locator, browser);
      assertTrue(textBox.type(contents));
      EasyMock.verify(browser);
   }

   /** Typing in the Text Box without clearing returns true when successful */
   @Test
   public void canTypeInTextBoxWithoutClearing()
   {
      final String locator = "mockTextBox";
      final String contents = "ABC";
      final boolean doTab = true;
      final Browser browser = getBrowserExpectingTypeInTextBoxWithoutClearing("//div[@id='" + locator + "']/input",
            contents, doTab);
      final TextBox textBox = new TextBox(locator, browser);
      assertTrue(textBox.typeWithoutClear(contents, doTab));
      EasyMock.verify(browser);
   }

   /** Typing in the Text Box without clearing returns true when successful */
   @Test
   public void canTypeInTextBoxWithoutTabbing()
   {
      final String locator = "mockTextBox";
      final String contents = "ABC";
      final Browser browser = getBrowserExpectingTypeInTextBoxWithoutTabbing("//div[@id='" + locator + "']/input",
            contents);
      final TextBox textBox = new TextBox(locator, browser);
      assertTrue(textBox.type(contents, false));
      EasyMock.verify(browser);
   }

   /** Waiting for content returns true */
   @Test
   public void waitForContentIsTrue()
   {
      final String locator = "mockTextBox";
      final String content = "mockSomeContent";

      Browser browser = EasyMock.createMock(Browser.class);
      EasyMock.expect(browser.waitForContent("//div[@id='" + locator + "']/input", content)).andReturn(true).once();
      EasyMock.replay(browser);

      final TextBox textBox = new TextBox(locator, browser);
      assertTrue(textBox.waitForContent(content));

      EasyMock.verify(browser);
   }

   /** Waiting for content returns false */
   @Test
   public void waitForContentIsFalse()
   {
      final String locator = "mockTextBox";
      final String content = "mockSomeContent";

      Browser browser = EasyMock.createMock(Browser.class);
      EasyMock.expect(browser.waitForContent("//div[@id='" + locator + "']/input", content)).andReturn(false).once();
      EasyMock.replay(browser);

      final TextBox textBox = new TextBox(locator, browser);
      assertFalse(textBox.waitForContent(content));

      EasyMock.verify(browser);
   }

   // private helper methods
   /**
    * returns a mock browser that expects to have a text box cleared.
    * 
    * @param locator
    *           the locator of the text box
    * @return a {@link Browser} that expects to have a text box cleared
    */
   private Browser getBrowserExpectingClearTextBox(final String locator)
   {
      Browser browser = EasyMock.createMock(Browser.class);
      EasyMock.expect(browser.clearText(locator)).andReturn(true).once();
      EasyMock.replay(browser);
      return browser;
   }

   /**
    * returns a mock browser that expects to have a text box cleared and
    * validated.
    * 
    * @param locator
    *           the locator of the text box
    * @param validationString
    *           the String that the method is validating the text box's default
    *           value against
    * @return a {@link Browser} that expects to have a text box cleared and
    *         validated.
    */
   private Browser getBrowserExpectingClearAndValidateTextBox(final String locator, final String validationString)
   {
      Browser browser = EasyMock.createMock(Browser.class);
      EasyMock.expect(browser.clearAndValidateField(locator, validationString)).andReturn(true);
      EasyMock.replay(browser);
      return browser;
   }

   /**
    * returns a mock browser that expects to return the text for a text box.
    * 
    * @param locator
    *           the locator of the label
    * @return a {@link Browser} with texts from a text box
    */
   private Browser getBrowserExpectingText(final String locator)
   {
      Browser browser = EasyMock.createMock(Browser.class);
      EasyMock.expect(browser.exists(locator)).andReturn(true);
      EasyMock.expect(browser.getText(locator)).andReturn("ABC");
      EasyMock.replay(browser);
      return browser;
   }

   /**
    * returns a mock browser that expects to have a text box typed in.
    * 
    * @param locator
    *           the locator of the text box
    * @param contents
    *           the content to be typed in the text box
    * @return a {@link Browser} that expects to have a text box typed in some
    *         texts
    */
   private Browser getBrowserExpectingTypeInTextBox(final String locator, final String contents)
   {
      Browser browser = EasyMock.createMock(Browser.class);
      EasyMock.expect(browser.type(locator, contents, true)).andReturn(true).once();
      EasyMock.replay(browser);
      return browser;
   }

   /**
    * returns a mock browser that expects to have a text box typed in without
    * clearing first.
    * 
    * @param locator
    *           the locator of the text box
    * @param contents
    *           the content to be typed in the text box
    * @param doTab
    *           whether or not hitting the tab key after the typing.
    * @return a {@link Browser} that expects to have a text box typed in some
    *         texts without clearing first
    */
   private Browser getBrowserExpectingTypeInTextBoxWithoutClearing(final String locator, final String contents,
         final boolean doTab)
   {
      Browser browser = EasyMock.createMock(Browser.class);
      EasyMock.expect(browser.typeWithoutClear(locator, contents, doTab)).andReturn(true).once();
      EasyMock.replay(browser);
      return browser;
   }

   /**
    * returns a mock browser that expects to have a text box typed in without
    * tabbing after inputting the text.
    * 
    * @param locator
    *           the locator of the text box
    * @param contents
    *           the content to be typed in the text box
    * @return a {@link Browser} that expects to have a text box typed in some
    *         texts without clearing first
    */
   private Browser getBrowserExpectingTypeInTextBoxWithoutTabbing(final String locator, final String contents)
   {
      Browser browser = EasyMock.createMock(Browser.class);
      EasyMock.expect(browser.type(locator, contents, false)).andReturn(true).once();
      EasyMock.replay(browser);
      return browser;
   }
}
