/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.browser;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * The <code>Chrome</code> class provides the {@link AbstractBrowser} for the
 * Google Chrome browser
 */
class Chrome extends AbstractBrowser
{

   /**
    * Constructor for the Chrome Driver.
    * 
    * @param settings
    *           the {@link BrowserSettings} to apply, which must not be null
    */
   Chrome(BrowserSettings settings)
   {
      //super(settings, new ChromeDriver(), 40);
      super("CHROME", settings, new ChromeDriver(setChromCapabilities()), 40);
   }
   
   /**
    * set the Capabilities used for creating a Chrome WebDriver.
    * 
    * @return DesiredCapabilities
    *           the Capabilities used for creating a Chrome WebDriver.
    *           including where to save the download files, etc.
    */
   static DesiredCapabilities setChromCapabilities()
   {
      Map<String, Object> prefs = new HashMap<String, Object>();
      
      prefs.put("profile.default_content_settings.popups", 1);
      prefs.put("download.default_directory", "C:\\dev\\tmpReport");
         
      //System.setProperty("webdriver.chrome.logfile", "c:\\chromedriver\\chromedriver.log");
            
      ChromeOptions options = new ChromeOptions();
      options.setExperimentalOption("prefs", prefs);
      options.addArguments("chrome.switches","--disable-extensions");
      //options.addArguments("chrome.switches","--disable-application-cache");
      options.addArguments("--start-maximized");
         
      DesiredCapabilities capabilities = DesiredCapabilities.chrome();
      capabilities.setCapability(ChromeOptions.CAPABILITY,options);
      return capabilities;
   }
}


