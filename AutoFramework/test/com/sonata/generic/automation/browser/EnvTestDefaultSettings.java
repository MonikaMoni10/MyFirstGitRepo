/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.browser;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.sonata.generic.automation.browser.DefaultBrowserSettings;

/**
 * The <code>EnvTestDefaultSettings</code> class provides a special JUnit test
 * that requires a modified environment.
 * <p>
 * The Operating System environment must include
 * 'SWT_AUTOMATION_SERVER=bcraccqa04' and 'SWT_AUTOMATION_PORT=8080'.
 */
public class EnvTestDefaultSettings
{
   /**
    * Confirm that the default settings picks up settings from the environment
    */
   @Test
   public void hostServerAndPortCanBeSpecifiedInTheEnvironment()
   {
      DefaultBrowserSettings settings = new DefaultBrowserSettings();
      assertEquals("http://bcraccqa04:8080", settings.getBaseURL());
   }

   /**
    * Confirm that the default settings picks up settings from the environment
    * even when Ant mode is requested The Operating System environment must
    * include SWT_AUTOMATION_SERVER=localhost' and 'SWT_AUTOMATION_PORT=7800'.
    */
   @Test
   public void hostServerAndPortCanBeSpecifiedInTheEnvironmentInAntMode()
   {
      DefaultBrowserSettings settings = new DefaultBrowserSettings();
      assertEquals("http://localhost:7800", settings.getBaseURL());
   }

}
