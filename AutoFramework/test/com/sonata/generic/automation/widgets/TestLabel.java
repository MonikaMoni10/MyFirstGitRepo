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
import com.sonata.generic.automation.widgets.Label;

/**
 * The <code>TestLabel</code> class provides the JUnit tests on the
 * {@link Label} class
 * 
 * V1 Story [B-12251]
 */
public class TestLabel
{
   /** The name must not be null */
   @Test(expected = IllegalArgumentException.class)
   public void labelMustHaveName()
   {
      new Label(null, EasyMock.createNiceMock(Browser.class));
   }

   /** The browser must not be null */
   @Test(expected = IllegalArgumentException.class)
   public void labelMustHaveBrowser()
   {
      new Label("mockLabel", null);
   }

   /** can get the expected Label text */
   @Test
   public void canGetLabelText()
   {
      final String name = "mockLabel";
      Browser browser = getBrowserExpectingLabelText(name);
      final Label label = new Label(name, browser);
      assertEquals("ABC", label.getText());
      EasyMock.verify(browser);
   }

   /** will not get incorrect Label text */
   @Test
   public void willNotGetIncorrectLabelText()
   {
      final String name = "mockLabel";
      Browser browser = getBrowserExpectingLabelText(name);
      final Label label = new Label(name, browser);
      assertNotSame("123", label.getText());
      EasyMock.verify(browser);
   }

   // Private methods to help the test

   /**
    * returns a mock browser that expects to return the text for a label.
    * 
    * @param name
    *           the name of the label
    * @return a {@link Browser} that expects to return the text for a label.
    */
   private Browser getBrowserExpectingLabelText(final String name)
   {
      Browser browser = EasyMock.createMock(Browser.class);
      EasyMock.expect(browser.getText(name)).andReturn("ABC");
      EasyMock.replay(browser);
      return browser;
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

      final Label label = new Label(locator, browser);
      assertTrue(label.waitForContent(content));

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

      final Label label = new Label(locator, browser);
      assertFalse(label.waitForContent(content));

      EasyMock.verify(browser);
   }
}
