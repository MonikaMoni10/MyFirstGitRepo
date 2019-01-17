/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.browser;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.sonata.generic.automation.browser.Browser;
import com.sonata.generic.automation.browser.BrowserType;
import com.sonata.generic.automation.browser.Chrome;

/**
 * The <code>EnvTestBrowserType</code> class provides a special JUnit test that
 * requires a modified environment.
 * <p>
 * The Operating System environment must include 'SWT_AUTOMATION_BROWSER=chrome'
 */
public class EnvTestBrowserType
{
   /**
    * In the environment we can override the default
    */
   @Test
   public void defaultBrowserCanBeSpecifiedInTheEnvironment()
   {
      Browser browser = BrowserType.createDefaultBrowser();
      assertTrue(browser instanceof Chrome);
      browser.close();
   }
}
