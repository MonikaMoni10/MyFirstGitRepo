/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.browser;

import org.junit.Test;

import com.sonata.generic.automation.browser.Browser;

/**
 * The <code>TestBrowser</code> class provides the JUnit tests on the
 * {@link Browser} interface.
 */
public class TestBrowser
{
   /**
    * Confirms things about the public names, such as ensuring collision
    * prevention, not exporting hidden words and concepts etc.
    */
   @Test
   public void publicNamesAreAcceptable()
   {
      Utility.confirmPublicNamesAreAcceptable(Browser.class);
   }

}
