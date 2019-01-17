/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.browser;

import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * The <code>BrowserType</code> enumerates the sorts of browsers and provides a
 * method for creating instances of the {@link Browser} interface for them.
 */
public enum BrowserType
{

   /** Microsoft Internet Explorer */
   INTERNET_EXPLORER(new ExplorerFactory()),

   /** Mozilla FireFox */
   FIREFOX(new FireFoxFactory()),

   /** Google Chrome */
   CHROME(new ChromeFactory());

   /** The factory for this type */
   private final BrowserFactory factory;

   /**
    * Constructs an instance of the {@link BrowserType} enumeration with the
    * given factory
    * 
    * @param factory
    *           the factory that can create instances of this type
    */
   private BrowserType(final BrowserFactory factory)
   {
      this.factory = factory;
   }

   /**
    * returns a {@link Browser} of this type, with default settings
    * 
    * @return a corresponding {@link Browser}.
    */
   public Browser createBrowser()
   {
      return factory.createBrowser(new DefaultBrowserSettings());
   }

   /**
    * returns a {@link Browser} of this type with the given settings
    * 
    * @param settings
    *           the settings to apply
    * @return a corresponding {@link Browser}.
    */
   public Browser createBrowser(BrowserSettings settings)
   {
      if (settings == null)
      {
         throw new IllegalArgumentException("The settings you provide must not be null");
      }
      return factory.createBrowser(settings);
   }

   /** The interface for the factory */
   private static interface BrowserFactory
   {
      /**
       * Creates an instance of the {@link Browser} interface
       * 
       * @param settings
       *           the general settings to apply
       * @return a {@link Browser}
       */
      Browser createBrowser(BrowserSettings settings);
   }

   /** A factory for Internet Explorer browsers */
   private static class ExplorerFactory implements BrowserFactory
   {
      @Override
      public Browser createBrowser(BrowserSettings settings)
      {
    	  // Added for supporting the newer Webdriver versions 2.26+
    	 System.setProperty("webdriver.ie.driver","c:\\iedriver\\IEDriverServer.exe"); 
         return new InternetExplorer(settings);
      }
   }

   /** A factory for FireFoxe browsers */
   private static class FireFoxFactory implements BrowserFactory
   {
      @Override
      public Browser createBrowser(BrowserSettings settings)
      {
         return new FireFox(settings);
      }
   }

   /** a factory for Chrome browsers */
   private static class ChromeFactory implements BrowserFactory
   {
      @Override
      public Browser createBrowser(BrowserSettings settings)
      {
    	  // Added for supporting the newer Webdriver versions 2.26+
    	 System.setProperty("webdriver.chrome.driver","c:\\chromedriver\\chromedriver.exe");     	  
         return new Chrome(settings);
      }
   }

   /**
    * returns whatever the default browser is
    * 
    * @return a browser
    */
   public static Browser createDefaultBrowser()
   {
      return createBrowserOfSpecifiedType(getBrowserType(null), new DefaultBrowserSettings(), true);
   }

   /**
    * Create a browser of the specified type
    * 
    * @param browserType
    *           the name of the type of browser to create
    * @param settings
    *           the settings to use
    * @param typeIsFromEnvironment
    *           true if the browser type is from the environment and false if it
    *           is from a call parameter. this affects whether an error or an
    *           exception is thrown.
    * @return a {@link Browser} of the given type with the given properties
    * @throws Error
    *            if there is no matching browser type and the type is obtained
    *            from a configuration option rather than a specification
    * @throws IllegalArgumentException
    *            if there is no matching browser type and the type is obtained
    *            from a specification rather than a configuration option
    */
   private static Browser createBrowserOfSpecifiedType(final String browserType, BrowserSettings settings,
         boolean typeIsFromEnvironment) throws Error, IllegalArgumentException
   {
      assert (browserType != null);
      assert (settings != null);
      try
      {
         return BrowserType.valueOf(browserType.toUpperCase(Locale.ENGLISH)).createBrowser(settings);
      }
      catch (IllegalArgumentException e)
      {
         final String errorMessage = "There is no browser of type '" + browserType;
         if (typeIsFromEnvironment)
         {
            throw new Error(errorMessage);
         }
         else
         {
            throw new IllegalArgumentException(errorMessage);
         }
      }
   }

   /**
    * creates a browser by specification
    * 
    * @param specification
    *           the specification
    * @return a browser matching the specification
    */
   public static Browser createSpecifiedBrowser(String specification)
   {
      final String browserSetting = "browser";
      final String serverSetting = DefaultBrowserSettings.SERVER_SETTING;
      final String portsetting = DefaultBrowserSettings.PORT_SETTING;
      Map<String, String> settings = BrowserSpecParser.getInst().parse(specification,
            getNormalSettings(browserSetting, serverSetting, portsetting));
      final String browser = getBrowserType(settings.get(browserSetting));
      BrowserSettings browserSettings = DefaultBrowserSettings.getBrowserSettings(settings);
      return createBrowserOfSpecifiedType(browser, browserSettings, browser == null);
   }

   /**
    * gets the default browser type based on all possible settings
    * 
    * @param specified
    *           the browser type specified in the string
    * @return the evaluated browser type
    */
   private static String getBrowserType(String specified)
   {
      return StringUtils.getFirstNonNull(specified, System.getProperty("com.sage.swt.automation.browser.browser"),
            System.getenv("SWT_AUTOMATION_BROWSER"), "INTERNET_EXPLORER");
   }

   /**
    * gets the normal permitted settings
    * 
    * @param settings
    *           the list of strings to include
    * @return a Set containing the normal settings
    */
   private static Set<String> getNormalSettings(String... settings)
   {
      Set<String> result = new HashSet<String>();
      for (String setting : settings)
      {
         result.add(setting);
      }
      return Collections.unmodifiableSet(result);
   }

}
