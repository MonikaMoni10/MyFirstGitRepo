/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.browser;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.sonata.generic.automation.browser.BrowserSettings;
import com.sonata.generic.automation.browser.DefaultBrowserSettings;
import com.sonata.generic.automation.browser.TestMode;

/**
 * The <code>TestDefautBrowserSettings</code> provides unit tests on the
 * {@link DefaultBrowserSettings} class.
 * <p>
 * Note that this is not a public class. However it is not currently possible to
 * unit test the use of this class so it is easier to test as it is.
 */
public class TestDefautBrowserSettings
{
   /** By default the values are hard-coded */
   @Test
   public void trueDefaultsAreHardCoded()
   {
      BrowserSettings settings = new DefaultBrowserSettings();
      checkSettings(settings, "http://localhost", 50, 5 * 60 * 1000, 60 * 60 * 1000);
   }

   /** Test that the settings can be overridden */
   @Test
   public void canOverrideSettings()
   {
      Map<String, String> testSettings = new HashMap<String, String>();
      testSettings.put("server", "bcraccqa04");
      testSettings.put("port", "8080");
      BrowserSettings settings = DefaultBrowserSettings.getBrowserSettings(testSettings);
      checkSettings(settings, "http://bcraccqa04:8080", 50, 5 * 60 * 1000, 60 * 60 * 1000);
   }

   /**
    * At least for now we will assume that the protocol is http unless the port
    * is 443
    */
   @Test
   public void protocolForSecureIsByPort()
   {
      Map<String, String> testSettings = new HashMap<String, String>();
      testSettings.put("server", "bcraccqa04");
      testSettings.put("port", "443");
      BrowserSettings settings = DefaultBrowserSettings.getBrowserSettings(testSettings);
      checkSettings(settings, "https://bcraccqa04", 50, 5 * 60 * 1000, 60 * 60 * 1000);
   }

   /**
    * If nothing else is said, Test Mode is Deployed
    */
   @Test
   public void defaultTestModeIsDeployed()
   {
      BrowserSettings settings = new DefaultBrowserSettings();
      assertEquals(TestMode.DEPLOYED, settings.getTestMode());
   }

   /**
    * If local server and not default port it is probably Ant mode.
    */
   @Test
   public void testModeIsAnt()
   {
      Map<String, String> testSettings = new HashMap<String, String>();
      testSettings.put("port", "7800");
      BrowserSettings settings = DefaultBrowserSettings.getBrowserSettings(testSettings);

      assertEquals(TestMode.ANT, settings.getTestMode());
   }

   /**
    * 127.0.0.1 is the same as localhost.
    */
   @Test
   public void testModeIsAntEvenNumberedServer()
   {
      Map<String, String> testSettings = new HashMap<String, String>();
      testSettings.put("server", "127.0.0.1");
      testSettings.put("port", "7800");
      BrowserSettings settings = DefaultBrowserSettings.getBrowserSettings(testSettings);

      assertEquals(TestMode.ANT, settings.getTestMode());
   }

   /**
    * localhost can have any capitalisation
    */
   @Test
   public void testModeIsAntEvenInCaseInsensitive()
   {
      Map<String, String> testSettings = new HashMap<String, String>();
      testSettings.put("server", "LoCalHost");
      testSettings.put("port", "7800");
      BrowserSettings settings = DefaultBrowserSettings.getBrowserSettings(testSettings);

      assertEquals(TestMode.ANT, settings.getTestMode());
   }

   /**
    * test mode is not fooled by the HTTPS
    */
   @Test
   public void testModeIsNotFooledByHTTPS()
   {
      Map<String, String> testSettings = new HashMap<String, String>();
      testSettings.put("port", "443");
      BrowserSettings settings = DefaultBrowserSettings.getBrowserSettings(testSettings);

      assertEquals(TestMode.DEPLOYED, settings.getTestMode());
   }

   /**
    * test local ORF installation is Ant mode
    */
   @Test
   public void testLocalORFIsAntMode()
   {
      Map<String, String> testSettings = new HashMap<String, String>();
      testSettings.put("server", "localhost");
      testSettings.put("port", "29000");
      BrowserSettings settings = DefaultBrowserSettings.getBrowserSettings(testSettings);
      checkSettings(settings, "http://localhost:29000", 50, 5 * 60 * 1000, 60 * 60 * 1000);

      assertEquals(TestMode.ANT, settings.getTestMode());
   }

   /**
    * test remote ORF installation is Ant mode
    */
   @Test
   public void testRemoteORFIsAntMode()
   {
      Map<String, String> testSettings = new HashMap<String, String>();
      testSettings.put("server", "bcraccqa04");
      testSettings.put("port", "29000");
      BrowserSettings settings = DefaultBrowserSettings.getBrowserSettings(testSettings);
      checkSettings(settings, "http://bcraccqa04:29000", 50, 5 * 60 * 1000, 60 * 60 * 1000);

      assertEquals(TestMode.ANT, settings.getTestMode());
   }

   /**
    * Check that the settings are as specified
    * 
    * @param settings
    *           the settings to check
    * @param url
    *           the expected URL
    * @param interval
    *           the expected interval
    * @param timeout
    *           the expected timeout
    * @param longTimeout
    *           the expected long timeout
    */
   private static void checkSettings(BrowserSettings settings, String url, int interval, int timeout, int longTimeout)
   {
      assertEquals(url, settings.getBaseURL());
      assertEquals(interval, settings.getDefaultInterval());
      assertEquals(timeout, settings.getDefaultTimeOut());
      assertEquals(longTimeout, settings.getLargeTimeOut());
   }
}
