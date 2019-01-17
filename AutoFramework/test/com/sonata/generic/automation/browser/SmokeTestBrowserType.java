/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.browser;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.sonata.generic.automation.browser.Browser;
import com.sonata.generic.automation.browser.BrowserType;
import com.sonata.generic.automation.browser.FireFox;
import com.sonata.generic.automation.browser.InternetExplorer;

/**
 * The <code>SmokeTestBrowserType</code> provides smoke tests on the
 * {@link BrowserType} class.
 */
public class SmokeTestBrowserType
{
   /**
    * Confirm that we can create a browser of each type and it will get some
    * default settings. This does actually create the browser so it takes a
    * little time.
    */
   @Test
   public void canCreateWithDefaultSettings()
   {
      for (BrowserType type : BrowserType.values())
      {
         Browser browser = type.createBrowser();
         browser.close();
      }
   }

   /**
    * By default we will get the first browser type
    */
   @Test
   public void canCreateDefaultBrowser()
   {
      Browser browser = BrowserType.createDefaultBrowser();
      assertTrue(browser instanceof InternetExplorer);
      browser.close();
   }

   /**
    * We can override settings in a string
    * 
    */
   @Test
   public void canCreateNonDefaultBrowserBySpecification()
   {
      Browser browser = BrowserType.createSpecifiedBrowser("browser is firefox, port is 7800 and server is bcraccqa04");
      assertTrue(browser instanceof FireFox);
      browser.close();

   }
}
