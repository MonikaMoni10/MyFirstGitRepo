/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.browser;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.sonata.generic.automation.browser.BrowserSpecParser;
import com.sonata.generic.automation.browser.DefaultBrowserSettings;

/**
 * The <code>TestBrowserSpecParser</code> class provides JUnit tests on the
 * (non-public) {@link BrowserSpecParser} class, used in constructing browsers.
 */
public class TestBrowserSpecParser
{
   private static final String PORT_SETTING    = DefaultBrowserSettings.PORT_SETTING;
   private static final String SERVER_SETTING  = DefaultBrowserSettings.SERVER_SETTING;
   private static final String BROWSER_SETTING = "browser";

   /** Simple regular case */
   @Test
   public void canHandleCommaSeparaterEqualsSettings()
   {
      checkResult(BrowserSpecParser.getInst().parse("browser=chrome,server=bcraccqa04,port=8080", getNormalSettings()));
   }

   /** Simple regular case except spaces */
   @Test
   public void canHandleCommaSeparaterEqualsSettingsWithSpaces()
   {
      checkResult(BrowserSpecParser.getInst().parse("browser = chrome,  server = bcraccqa04, port = 8080",
            getNormalSettings()));
   }

   /** Verbose regular case */
   @Test
   public void canHandleVerbose()
   {
      checkResult(BrowserSpecParser.getInst().parse("browser is chrome,  server is bcraccqa04 and port is 8080",
            getNormalSettings()));
   }

   /**
    * The specification must be valid
    */
   @Test(expected = IllegalArgumentException.class)
   public void throwsIfInvalidSetting()
   {
      BrowserSpecParser.getInst().parse("browsir is chrome,  server is bcraccqa04 and port is 8080",
            getNormalSettings());
   }

   /**
    * checks the parsed results against the default expected results
    * 
    * @param parsed
    *           the parsed results
    */
   private void checkResult(Map<String, String> parsed)
   {
      final String browser = parsed.get(BROWSER_SETTING);
      final String server = parsed.get(SERVER_SETTING);
      final String port = parsed.get(PORT_SETTING);
      assertEquals("chrome", browser);
      assertEquals("bcraccqa04", server);
      assertEquals("8080", port);
   }

   /**
    * gets the normal permitted settings
    * 
    * @return a Set containing the normal settings
    */
   private static Set<String> getNormalSettings()
   {
      Set<String> result = new HashSet<String>();
      result.add(BROWSER_SETTING);
      result.add(SERVER_SETTING);
      result.add(PORT_SETTING);
      return Collections.unmodifiableSet(result);
   }

}
