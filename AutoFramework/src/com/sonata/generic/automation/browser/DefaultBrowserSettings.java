/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.browser;

import java.util.HashMap;
import java.util.Map;

/**
 * The <code>DefaultBrowserSettings</code> class provides the
 * {@link BrowserSettings} for default operation
 */
class DefaultBrowserSettings implements BrowserSettings
{
   /** The name of the setting permitted in a specification to signify the port */
   static final String      PORT_SETTING          = "port";
   /**
    * The name of the setting permitted in a specification to signify the server
    */
   static final String      SERVER_SETTING        = "server";

   /**
    * Constants for Default Time Out in milliseconds.
    * 
    * 300000 ms = 5 minutes
    */
   private static final int DEFAULT_TIMEOUT       = 300000;

   /**
    * Constants for Small Time Out in milliseconds, used for waitForElement.
    * 
    * 30000 ms = 30 seconds
    */
   private static final int DEFAULT_TIMEOUT_SMALL = 30000;

   /**
    * Constants for Large Time Out in milliseconds.
    * 
    * 3600000 ms = 1 hour
    */
   private static final int DEFAULT_TIMEOUT_LARGE = 3600000;

   /**
    * Default interval period to check for an element existing.
    */
   private static final int DEFAULT_INTERVAL      = 50;

   /** Remember the URL */
   private final String     baseURL;

   /** Remember Test Mode */
   private final TestMode   testMode;

   /**
    * Constructs a {@link BrowserSettings} based on the given URL
    * 
    * @param baseURL
    *           the base URL to use.
    * @param testMode
    *           the Test Mode to use
    */
   private DefaultBrowserSettings(final String baseURL, TestMode testMode)
   {
      this.baseURL = baseURL;
      this.testMode = testMode;
   }

   /**
    * Constructs a {@link BrowserSettings} based on the normal defaults.
    * <p>
    * This is non-private only to allow unit testing
    */
   DefaultBrowserSettings()
   {
      this(getDefaultedBaseURL(new HashMap<String, String>()), getDefaultedTestMode(new HashMap<String, String>()));
   }

   /**
    * constructs a {@link BrowserSettings} by mixing the given settings onto the
    * default ones
    * 
    * @param settings
    *           the settings to superimpose
    * @return a {@link BrowserSettings} that includes the given settings
    */
   public static BrowserSettings getBrowserSettings(Map<String, String> settings)
   {
      return new DefaultBrowserSettings(getDefaultedBaseURL(settings), getDefaultedTestMode(settings));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getBaseURL()
   {
      return this.baseURL;
   }

   /**
    * Performs all the defaulting to figure out the base URL to use
    * 
    * @param specSettings
    *           a map of settings to superimpose
    * 
    * @return the base URL, taking everything into account
    */
   private static String getDefaultedBaseURL(Map<String, String> specSettings)
   {
      final String server = getServer(specSettings);
      final String port = getPort(specSettings);
      return getURLOfPortAndServer(server, port);
   }

   /**
    * Performs all the defaulting to figure out the Test Mode to use
    * 
    * @param specSettings
    *           a map of settings to superimpose
    * 
    * @return the Test Mode to be used
    */
   private static TestMode getDefaultedTestMode(Map<String, String> specSettings)
   {
      final String server = getServer(specSettings);
      final String port = getPort(specSettings);
      return (isLocalWithNonDefaultPort(server, port) || isRemoteWithNonDefaultPort(server, port)) ? TestMode.ANT
            : TestMode.DEPLOYED;
   }

   /**
    * return if this is the local server with non default port
    * 
    * @param server
    *           the server
    * @param port
    *           the port (may be null)
    * @return if this is the local server with non default port
    */
   private static boolean isLocalWithNonDefaultPort(final String server, final String port)
   {
      return ("localhost".equalsIgnoreCase(server) || "127.0.0.1".equals(server))
            && (port != null && !"443".equals(port) && !"80".equals(port));
   }

   /**
    * return if this is the remote server with non default port
    * 
    * @param server
    *           the server
    * @param port
    *           the port (may be null)
    * @return if this is the remote server with non default port
    */
   private static boolean isRemoteWithNonDefaultPort(final String server, final String port)
   {
      return (!"localhost".equalsIgnoreCase(server) || !"127.0.0.1".equals(server))
            && (port != null && !"443".equals(port) && !"80".equals(port));
   }

   /**
    * gets the port including checking environment, etc.
    * 
    * @param specSettings
    *           a map of settings to superimpose
    * @return the port including checking environment, etc.
    */
   private static String getPort(Map<String, String> specSettings)
   {
      final String specPort = specSettings.get(PORT_SETTING);
      final String envPort = System.getenv("SWT_AUTOMATION_PORT");
      final String syspropPort = System.getProperty("com.sage.swt.automation.browser.port");
      final String port = StringUtils.getFirstNonNull(specPort, syspropPort, envPort);
      return port;
   }

   /**
    * gets the server including checking environment, etc.
    * 
    * @param specSettings
    *           a map of settings to superimpose
    * @return the server including checking environment, etc.
    */
   private static String getServer(Map<String, String> specSettings)
   {
      final String specServer = specSettings.get(SERVER_SETTING);
      final String envServer = System.getenv("SWT_AUTOMATION_SERVER");
      final String syspropServer = System.getProperty("com.sage.swt.automation.browser.server");
      final String server = StringUtils.getFirstNonNull(specServer, syspropServer, envServer, "localhost");
      return server;
   }

   /**
    * gets a URL using port and server
    * 
    * @param server
    *           the server to be used
    * @param port
    *           port on server (may be null)
    * @return the URL for the port and server
    */
   private static String getURLOfPortAndServer(final String server, final String port)
   {
      final boolean secure = "443".equals(port);
      final StringBuilder sb = new StringBuilder();
      sb.append("http");
      if (secure)
      {
         sb.append('s');
      }
      sb.append("://");
      sb.append(server);
      return sb.toString();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public int getDefaultTimeOut()
   {
      return DEFAULT_TIMEOUT;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public int getDefaultInterval()
   {
      return DEFAULT_INTERVAL;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public int getLargeTimeOut()
   {
      return DEFAULT_TIMEOUT_LARGE;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public int getSmallTimeOut()
   {
      return DEFAULT_TIMEOUT_SMALL;
   }

   @Override
   public TestMode getTestMode()
   {
      return this.testMode;
   }

}
