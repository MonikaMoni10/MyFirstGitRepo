/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.browser;

/**
 * The <code>BrowserSettingsFactory</code> singleton class provides a factory
 * for creating instances of {@link BrowserSettings} for the
 * {@link BrowserType#createBrowser} to utilize when creating a {@link Browser}
 */
public class BrowserSettingsFactory
{
   /** The factory for this type */
   private static final BrowserSettingsFactory INSTANCE = new BrowserSettingsFactory();

   /**
    * gets an instance of this browser settings factory
    * 
    * @return {@link BrowserSettingsFactory} the instance of this browser
    *         settings factory
    */
   public static BrowserSettingsFactory getInst()
   {
      return INSTANCE;
   }

   /**
    * gets suitable default browser settings
    * 
    * @return {@link DefaultBrowserSettings} the default browser settings
    */
   public BrowserSettings getDefaultSettings()
   {
      return new DefaultBrowserSettings();
   }
}
