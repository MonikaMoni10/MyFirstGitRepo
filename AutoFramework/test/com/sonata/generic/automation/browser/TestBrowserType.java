/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.browser;

import org.junit.Test;

import com.sonata.generic.automation.browser.BrowserType;

/**
 * The <code>TestBrowserType</code> class provides the unit tests on the
 * {@link BrowserType} enumeration
 */
public class TestBrowserType
{
   /**
    * Confirms things about the public names, such as ensuring collision
    * prevention, not exporting hidden words and concepts etc.
    */
   @Test
   public void publicNamesAreAcceptable()
   {
      Utility.confirmPublicNamesAreAcceptable(BrowserType.class);
   }

   /**
    * If the browser comes from a specification then an invalid browser is a bad
    * argument
    */
   @Test(expected = IllegalArgumentException.class)
   public void throwsExceptionIfInvalidBrowserSpecified()
   {
      BrowserType.createSpecifiedBrowser("browser is mock");
   }

   /**
    * If the browser does not come from a specification then an invalid browser
    * is a configuration error (e.g. environment or system property) and so we
    * want an error
    */
   @Test(expected = Error.class)
   public void throwsErrorIfInvalidBrowserInEnvironment()
   {
      final String propertyName = "com.sage.swt.automation.browser.browser";
      System.setProperty(propertyName, "mock");
      try
      {
         BrowserType.createDefaultBrowser();
      }
      finally
      {
         System.getProperties().remove(propertyName);
      }
   }

}
