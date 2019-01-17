/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.widgets;

import static org.junit.Assert.assertTrue;

import org.easymock.EasyMock;
import org.junit.Ignore;
import org.junit.Test;

import com.sonata.generic.automation.browser.Browser;
import com.sonata.generic.automation.widgets.Tab;

/**
 * The <code>Tab</code> class provides the JUnit tests on the {@link Tab} class
 */
public class TestTab
{

   /**
    * Can not do this test any longer with the change to the constructor of Tab.
    * The locator must not be null
    */
   @Ignore
   @Test(expected = IllegalArgumentException.class)
   public void tabMustHaveLocator()
   {
      new Tab(null, EasyMock.createNiceMock(Browser.class));
   }

   /** The browser must not be null */
   @Test(expected = IllegalArgumentException.class)
   public void tabMustHaveBrowser()
   {
      new Tab("mock", null);
   }

   /** Select the Tab and pass it to the browser */
   @Test
   public void canSelectTab()
   {
      final String locator = "mockTab";
      final Browser browser = getBrowserExpectingSelectTab(locator);
      final Tab tab = new Tab(locator, browser);
      assertTrue(tab.select());
      EasyMock.verify(browser);
   }

   /**
    * Validates if the tab is selected or not, it returns a browser as well
    */
   @Test
   public void canValidateTabOpened()
   {
      final String locator = "mockTab";
      final Browser browser = getBrowserExpectingTabSelectedState(locator);
      final Tab tab = new Tab(locator, browser);
      assertTrue(tab.isSelected());
      EasyMock.verify(browser);
   }

   /**
    * returns a mock browser with validation if a tab is opened
    * 
    * @param locator
    *           the tab label locator
    
    * @return a {@link Browser} with validation if a tab is opened
    */
   private Browser getBrowserExpectingTabSelectedState(String locator)
   {
      Browser browser = EasyMock.createMock(Browser.class);
      EasyMock.expect(browser.getAttribute(locator,"aria-controls")).andReturn("tabIndex1").once();
            
      // then use that value as the id of the <div> element that contains the aria-expanded (true or false) attribute 
      EasyMock.expect(browser.getAttribute("tabIndex1","aria-expanded")).andReturn("true").once();
      
      EasyMock.replay(browser);
      return browser;
   }

   /**
    * returns a mock browser with validation if a tab is opened
    * 
    * @param locator
    *           the tab label locator
    * @return a {@link Browser} with validation if a tab panel is opened
    */
   private Browser getBrowserExpectingSelectTab(String locator)
   {
      Browser browser = EasyMock.createMock(Browser.class);
      EasyMock.expect(browser.selectTab(locator)).andReturn(true).once();
      EasyMock.replay(browser);
      return browser;
   }
}
