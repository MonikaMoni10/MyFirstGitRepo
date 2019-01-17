/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.browser;

import java.io.File;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

/**
 * The <code>FireFox</code> class provides the {@link AbstractBrowser} for the
 * Mozilla FireFox browser
 */
class FireFox extends AbstractBrowser
{
   private static final String    EXPORT_TEMP_LOCATION      = "c:\\dev\\tmpReport\\";
   /**
    * Constructor for the FireFox Driver.
    * 
    * @param settings
    *           the {@link BrowserSettings} to apply, which must not be null
    */
   FireFox(BrowserSettings settings)
   {
      super("FIREFOX", settings, new FirefoxDriver(getFireFoxDefaultProfile()), 40);
   }

   /**
    * Obtain the default Firefox profile of the user invoking the driver. This
    * was added as of Webdriver 2.16.1 where in Firefox 9, an anonymous profile
    * was created by default, which then caused browser click problems. The user
    * can turn on and off to the default profile and has no control over the
    * anonymous profile.
    * 
    * @return the default Firefox profile if found, otherwise null.
    */
   static FirefoxProfile getFireFoxDefaultProfile()
   {
      String path = System.getenv("APPDATA") + "\\Mozilla\\Firefox\\Profiles\\";

      File folder = new File(path);
      File[] listOfFiles = folder.listFiles();

      for (int i = 0; i < listOfFiles.length; i++)
      {
         if (listOfFiles[i].isDirectory())
         {
            if (listOfFiles[i].getName().contains(".default"))
            {
               FirefoxProfile profile = new FirefoxProfile(new File(listOfFiles[i].getPath()));
               profile.setAcceptUntrustedCertificates(true);
               profile.setPreference("browser.download.folderList",2);
               profile.setPreference("browser.download.dir",EXPORT_TEMP_LOCATION);
               profile.setPreference("browser.helperApps.neverAsk.saveToDisk","text/download,application/x-xml,application/pdf");
               profile.setAcceptUntrustedCertificates(true);
               return profile;
            }
         }
      }

      return null;

   }

}
