/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.browser;

import static org.junit.Assert.assertTrue;

import org.easymock.EasyMock;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import com.sonata.generic.automation.browser.AbstractBrowser;
import com.sonata.generic.automation.browser.Browser;
import com.sonata.generic.automation.browser.DefaultBrowserSettings;

/**
 * The <code>TestAbstractBrowser</code> class provides the JUnit tests on the
 * {@link AbstractBrowser} class.
 */
public class TestAbstractBrowser
{
   /** Mainly a test to get us started */
   @Ignore
   @Test
   public void canClose()
   {
      final WebDriver closingDriver = getClosingDriver(true);
      Browser browser = new MockAbstractBrowser(closingDriver);
      assertTrue(browser.close());
      EasyMock.verify(closingDriver);
   }

   /** Tests that we currently squash exceptions. I want to change that */
   @Ignore
   @Test
   public void closingDoesNotSquashExceptions()
   {
      final WebDriver closingDriver = getClosingDriver(false);
      Browser browser = new MockAbstractBrowser(closingDriver);
      try
      {
         browser.close();
      }
      catch (IllegalStateException e)
      {
         //Caught Expected exception!
      }
      EasyMock.verify(closingDriver);
   }

   /**
    * creates a mock {@link WebDriver} that handles the close call.
    * 
    * @param returnValue
    *           the value to return
    * @return a WebDriver
    */
   private WebDriver getClosingDriver(boolean returnValue)
   {
      WebDriver driver = EasyMock.createNiceMock(WebDriver.class);
      driver.quit();
      if (!returnValue)
      {
         EasyMock.expectLastCall().andThrow(new IllegalStateException("Can quit fail?"));
      }
      EasyMock.replay(driver);
      return driver;
   }

   /**
    * The <code>MockAbstractBrowser</code> class provides a mock extension of
    * the {@link AbstractBrowser}, to support testing it.
    */
   private static class MockAbstractBrowser extends AbstractBrowser
   {
      /**
       * Constructs an instance of the {@link MockAbstractBrowser} class.
       * 
       * @param driver
       *           the web driver to place under the browser
       */
      MockAbstractBrowser(WebDriver driver)
      {
         super("FIREFOX", new DefaultBrowserSettings(), driver, 40);
      }
   }

}
