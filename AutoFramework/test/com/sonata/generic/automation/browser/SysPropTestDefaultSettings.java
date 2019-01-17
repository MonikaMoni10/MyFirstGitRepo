/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.browser;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.sonata.generic.automation.browser.DefaultBrowserSettings;

/**
 * The <code>SysPropTestDefaultSettings</code> class provides a special JUnit
 * test that requires a modified environment.
 * <p>
 * The Java VM arguments must include
 * '-Dcom.sage.swt.automation.browser.server=bcraccqa02' and
 * '-Dcom.sage.swt.automation.browser.port=7890'
 */
public class SysPropTestDefaultSettings
{
   /**
    * Confirm that the default settings picks up settings from the system
    * properties on the command line
    */
   @Test
   public void hostCanBeSpecifiedInTheSystemProperties()
   {
      DefaultBrowserSettings settings = new DefaultBrowserSettings();
      assertEquals("bcraccqa02:7890", settings.getBaseURL());
   }
}
