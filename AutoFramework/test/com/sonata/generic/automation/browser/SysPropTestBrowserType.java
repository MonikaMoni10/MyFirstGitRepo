/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.browser;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.sonata.generic.automation.browser.Browser;
import com.sonata.generic.automation.browser.BrowserType;
import com.sonata.generic.automation.browser.FireFox;

/**
 * The <code>SysPropTestBrowserType</code> class provides a special JUnit test
 * that requires a modified environment.
 * <p>
 * The Java VM arguments must include
 * '-Dcom.sage.swt.automation.browser.browser=firefox' (and FireFox must be
 * installed)
 */
public class SysPropTestBrowserType
{
   /**
    * In the system properties we can override the default and the environment
    */
   @Test
   public void browserTypeCanBeSpecifiedInTheSystemProperties()
   {
      Browser browser = BrowserType.createDefaultBrowser();
      assertTrue(browser instanceof FireFox);
      browser.close();
   }

}
