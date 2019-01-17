/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.widgets;

import static org.junit.Assert.assertTrue;

import org.easymock.EasyMock;
import org.junit.Test;

import com.sonata.generic.automation.browser.Browser;
import com.sonata.generic.automation.browser.SpecialKey;
import com.sonata.generic.automation.widgets.Widget;

/**
 * The <code>TestWidget</code> class provides the JUnit tests on the
 * {@link Widget} class
 */
public class TestWidget
{
   /** The locator must not be null */
   @Test(expected = IllegalArgumentException.class)
   public void widgetMustHavelocator()
   {
      new Widget(null, EasyMock.createNiceMock(Browser.class));
   }

   /** The browser must not be null */
   @Test(expected = IllegalArgumentException.class)
   public void widgetMustHaveBrowser()
   {
      new Widget("mock", null);
   }

   /** Widget is visible? */
   @Test
   public void widgetIsVisible()
   {
      final String locator = "mockWidget";
      final Browser browser = getBrowserExpectingVisibleWidget(locator);
      final Widget widget = new Widget(locator, browser);
      assertTrue(widget.isVisible());
      EasyMock.verify(browser);
   }

   /** Widget is editable? */
   @Test
   public void widgetIsEditable()
   {
      final String locator = "mockWidget";
      final Browser browser = getBrowserExpectingEditableWidget(locator);
      final Widget widget = new Widget(locator, browser);
      assertTrue(widget.isEditable());
      EasyMock.verify(browser);
   }

   /** Widget is disabled? */
   @Test
   public void widgetIsDisabled()
   {
      final String locator = "mockWidget";
      final Browser browser = getBrowserWithDisabledWidget(locator);
      final Widget widget = new Widget(locator, browser);
      assertTrue(widget.isDisabled());
      EasyMock.verify(browser);
   }

   /** Can wait for Widget? */
   @Test
   public void waitForWidget()
   {
      final String locator = "mockWidget";
      final Browser browser = getBrowserWaitForWidget(locator);
      final Widget widget = new Widget(locator, browser);
      assertTrue(widget.waitForElement());
      EasyMock.verify(browser);
   }

   /**
    * Can type a tab?
    */
   @Test
   public void canTypeATab()
   {
      final String locator = "mockWidget";
      final Browser browser = EasyMock.createMock(Browser.class);
      EasyMock.expect(browser.type(locator, SpecialKey.TAB)).andReturn(true).once();
      EasyMock.replay(browser);
      final Widget widget = new Widget(locator, browser);
      assertTrue(widget.pressTab());
      EasyMock.verify(browser);
   }

   // Private methods for mock
   /**
    * returns a mock browser that expects to have a visible widget
    * 
    * @param locator
    *           the locator of the widget
    * @return a {@link Browser} that expects to have a visible widget
    */
   private Browser getBrowserExpectingVisibleWidget(final String locator)
   {
      Browser browser = EasyMock.createMock(Browser.class);
      EasyMock.expect(browser.isVisible(locator)).andReturn(true).once();
      EasyMock.replay(browser);
      return browser;
   }

   /**
    * returns a mock browser that expects to have an editable widget
    * 
    * @param locator
    *           the locator of the widget
    * @return a {@link Browser} that expects to have an editable widget
    */
   private Browser getBrowserExpectingEditableWidget(final String locator)
   {
      Browser browser = EasyMock.createMock(Browser.class);
      EasyMock.expect(browser.isEditable(locator)).andReturn(true).once();
      EasyMock.replay(browser);
      return browser;
   }

   /**
    * returns a mock browser that expects to have a disabled widget
    * 
    * @param locator
    *           the locator of the widget
    * @return a {@link Browser} that expects to have a disabled widget
    */
   private Browser getBrowserWithDisabledWidget(final String locator)
   {
      Browser browser = EasyMock.createMock(Browser.class);
      EasyMock.expect(browser.isDisabled(locator)).andReturn(true).once();
      EasyMock.replay(browser);
      return browser;
   }

   /**
    * returns a mock browser that expects to wait for a widget
    * 
    * @param locator
    *           the locator of the widget
    * @return a {@link Browser} that expects to wait for a widget
    */
   private Browser getBrowserWaitForWidget(final String locator)
   {
      Browser browser = EasyMock.createMock(Browser.class);
      EasyMock.expect(browser.waitForElement(locator)).andReturn(true).once();
      EasyMock.replay(browser);
      return browser;
   }
}
