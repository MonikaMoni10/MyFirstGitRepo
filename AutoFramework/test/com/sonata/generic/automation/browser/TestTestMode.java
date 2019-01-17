/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.browser;

import org.junit.Test;

import com.sonata.generic.automation.browser.TestMode;

/**
 * The <code>TestTestMode</code> class provides the unit tests on the
 * {@link TestMode} enumeration
 */
public class TestTestMode
{
   /**
    * Confirms things about the public names, such as ensuring collision
    * prevention, not exporting hidden words and concepts etc.
    */
   @Test
   public void publicNamesAreAcceptable()
   {
      Utility.confirmPublicNamesAreAcceptable(TestMode.class);
   }

}
