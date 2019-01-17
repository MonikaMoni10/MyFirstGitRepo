/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.widgets;

import static org.junit.Assert.assertTrue;

import org.easymock.EasyMock;
import org.junit.Test;

import com.sonata.generic.automation.browser.Browser;
import com.sonata.generic.automation.widgets.Button;

/**
 * The <code>TestButton</code> class provides the JUnit tests on the
 * {@link Button} class
 */
public class TestButton
{
   /** The locator must not be null */
   @Test(expected = IllegalArgumentException.class)
   public void buttonMustHaveLocator()
   {
      new Button(null, EasyMock.createNiceMock(Browser.class));
   }

   /** The browser must not be null */
   @Test(expected = IllegalArgumentException.class)
   public void buttonMustHaveBrowser()
   {
      new Button("mock", null);
   }

   /** Clicking the button merely passes it to the browser */
   @Test
   public void canClickButton()
   {
      final String locator = "mockButton";
      final Browser browser = getBrowserExpectingClick(locator);
      final Button button = new Button(locator, browser);
      assertTrue(button.click());
      EasyMock.verify(browser);
   }

   /** Can Click and wait for an element to appear */
   @Test
   public void canClickButtonAndWait()
   {
      final String locator = "mockButton";
      final String anElement = "mockElement";
      final Browser browser = getBrowserExpectingClickAndWait(locator, anElement);
      final Button button = new Button(locator, browser);
      assertTrue(button.clickAndWaitFor(anElement));
      EasyMock.verify(browser);
   }

   /** Can Click and wait for an element to appear, with large timeout */
   @Test
   public void canClickButtonAndWaitLargeTimeout()
   {
      final String locator = "mockButton";
      final String anElement = "mockElement";
      final Browser browser = getBrowserExpectingClickAndWaitLargeTimeout(locator, anElement);
      final Button button = new Button(locator, browser);
      assertTrue(button.clickAndWaitForLargeTimeout(anElement));
      EasyMock.verify(browser);
   }

   /** Can Click and wait for an element to disappear */
   @Test
   public void canClickButtonAndWaitForDie()
   {
      final String locator = "mockButton";
      final String anElement = "mockElement";
      final Browser browser = getBrowserExpectingClickAndWaitForDie(locator, anElement);
      final Button button = new Button(locator, browser);
      assertTrue(button.clickAndWaitForDie(anElement));
      EasyMock.verify(browser);
   }

   /** Can Click and wait for an element to disappear, with large timeout */
   @Test
   public void canClickButtonAndWaitForDieLargeTimeout()
   {
      final String locator = "mockButton";
      final String anElement = "mockElement";
      final Browser browser = getBrowserExpectingClickAndWaitForDieLargeTimeout(locator, anElement);
      final Button button = new Button(locator, browser);
      assertTrue(button.clickAndWaitForDieLargeTimeout(anElement));
      EasyMock.verify(browser);
   }

   /** Can Click and wait for a frame to appear */
   @Test
   public void canClickButtonAndWaitForFrame()
   {
      final String locator = "mockButton";
      final String anElement = "mockElement";
      final String aFrame = "mockFrame";
      final Browser browser = getBrowserExpectingClickAndWaitForFrame(locator, anElement, aFrame);
      final Button button = new Button(locator, browser);
      assertTrue(button.clickAndWaitForFrame(anElement, aFrame));
      EasyMock.verify(browser);
   }

   /** Can Click and wait for a window to appear */
   @Test
   public void canClickButtonAndWaitForWindow()
   {
      final String locator = "mockButton";
      final String anElement = "mockElement";
      final String aWindow = "mockWindow";
      final Browser browser = getBrowserExpectingClickAndWaitForWindow(locator, anElement, aWindow);
      final Button button = new Button(locator, browser);
      assertTrue(button.clickAndWaitForWindow(aWindow, anElement));
      EasyMock.verify(browser);
   }

   /**
    * Can Click and wait for either one of two elements to appear, if the 2nd
    * element appear, it clicks on that and continue waiting for element 1
    */
   @Test
   public void canClickButtonAndWaitForEither()
   {
      final String locator = "mockButton";
      final String elementOne = "mockElement1";
      final String elementTwo = "mockElement2";
      final Browser browser = getBrowserExpectingClickAndWaitForEither(locator, elementOne, elementTwo);
      final Button button = new Button(locator, browser);
      assertTrue(button.clickAndWaitForEither(elementOne, elementTwo));
      EasyMock.verify(browser);
   }

   // private helper methods
   /**
    * returns a mock browser that expects to be clicked
    * 
    * @param locator
    *           the locator of the button
    * @return a {@link Browser} that expects to be clicked
    */
   private Browser getBrowserExpectingClick(final String locator)
   {
      Browser browser = EasyMock.createMock(Browser.class);
      EasyMock.expect(browser.click(locator)).andReturn(true).once();
      EasyMock.replay(browser);
      return browser;
   }

   /**
    * returns a mock browser that expects to be clicked, with an element that
    * can be verified.
    * 
    * @param locator
    *           the locator of the button
    * @param anElement
    *           locator of the element that can be verified.
    * @return a {@link Browser} that expects to be clicked
    */
   private Browser getBrowserExpectingClickAndWait(final String locator, final String anElement)
   {
      Browser browser = EasyMock.createMock(Browser.class);
      EasyMock.expect(browser.clickAndWaitFor(locator, anElement)).andReturn(true).once();
      EasyMock.replay(browser);
      return browser;
   }

   /**
    * returns a mock browser that expects to be clicked, with an element that
    * can be verified, after a large timeout.
    * 
    * @param locator
    *           the locator of the button
    * @param anElement
    *           locator of the element that can be verified.
    * @return a {@link Browser} that expects to be clicked
    */
   private Browser getBrowserExpectingClickAndWaitLargeTimeout(final String locator, final String anElement)
   {
      Browser browser = EasyMock.createMock(Browser.class);
      EasyMock.expect(browser.clickAndWaitForLargeTimeout(locator, anElement)).andReturn(true).once();
      EasyMock.replay(browser);
      return browser;
   }

   /**
    * returns a mock browser that expects to be clicked, with an element that
    * should disappear.
    * 
    * @param locator
    *           the locator of the button
    * @param anElement
    *           locator of the element that will disappear.
    * @return a {@link Browser} that expects to be clicked
    */
   private Browser getBrowserExpectingClickAndWaitForDie(final String locator, final String anElement)
   {
      Browser browser = EasyMock.createMock(Browser.class);
      EasyMock.expect(browser.clickAndWaitForDie(locator, anElement)).andReturn(true).once();
      EasyMock.replay(browser);
      return browser;
   }

   /**
    * returns a mock browser that expects to be clicked, with an element that
    * should disappear, with large timeout.
    * 
    * @param locator
    *           the locator of the button
    * @param anElement
    *           locator of the element that will disappear.
    * @return a {@link Browser} that expects to be clicked
    */
   private Browser getBrowserExpectingClickAndWaitForDieLargeTimeout(final String locator, final String anElement)
   {
      Browser browser = EasyMock.createMock(Browser.class);
      EasyMock.expect(browser.clickAndWaitForDieLargeTimeout(locator, anElement)).andReturn(true).once();
      EasyMock.replay(browser);
      return browser;
   }

   /**
    * returns a mock browser that expects to be clicked, with an element in a
    * new frame that can be verified.
    * 
    * @param locator
    *           the locator of the button
    * @param anElement
    *           locator of the element that can be verified.
    * @param aFrame
    *           locator of the new frame where the element is.
    * @return a {@link Browser} that expects to be clicked
    */
   private Browser getBrowserExpectingClickAndWaitForFrame(final String locator, final String anElement,
         final String aFrame)
   {
      Browser browser = EasyMock.createMock(Browser.class);
      EasyMock.expect(browser.clickAndWaitForFrame(locator, anElement, aFrame)).andReturn(true).once();
      EasyMock.replay(browser);
      return browser;
   }

   /**
    * returns a mock browser that expects to be clicked, with an element in a
    * new window that can be verified.
    * 
    * @param locator
    *           the locator of the button
    * @param anElement
    *           locator of the element that can be verified.
    * @param aWindow
    *           locator of the new window where the element is.
    * @return a {@link Browser} that expects to be clicked
    */
   private Browser getBrowserExpectingClickAndWaitForWindow(final String locator, final String anElement,
         final String aWindow)
   {
      Browser browser = EasyMock.createMock(Browser.class);
      EasyMock.expect(browser.clickAndWaitForWindow(locator, aWindow, anElement)).andReturn(true).once();
      EasyMock.replay(browser);
      return browser;
   }

   /**
    * returns a mock browser that expects to be clicked, and have elementTwo
    * appear, then elementOne after clicking on elementTwo
    * 
    * @param locator
    *           the locator of the button
    * @param elementOne
    *           locator of the element that needs to be verified.
    * @param elementTwo
    *           locator of another element that needs to be verified and
    *           clicked.
    * @return a {@link Browser} that expects to be clicked
    */
   private Browser getBrowserExpectingClickAndWaitForEither(final String locator, final String elementOne,
         final String elementTwo)
   {
      Browser browser = EasyMock.createMock(Browser.class);
      EasyMock.expect(browser.clickAndWaitForEither(locator, elementOne, elementTwo)).andReturn(true).once();
      EasyMock.replay(browser);
      return browser;
   }
}
