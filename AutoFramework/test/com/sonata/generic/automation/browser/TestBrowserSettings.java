/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.browser;

import org.junit.Test;

import com.sonata.generic.automation.browser.BrowserSettings;

/**
 * The <code>TestBrowserSettings</code> class provides the JUnit tests on the
 * {@link BrowserSettings} interface
 */
public class TestBrowserSettings
{
   /**
    * Confirms things about the public names, such as ensuring collision
    * prevention, not exporting hidden words and concepts etc.
    */
   @Test
   public void publicNamesAreAcceptable()
   {
      Utility.confirmPublicNamesAreAcceptable(BrowserSettings.class);
   }
}
