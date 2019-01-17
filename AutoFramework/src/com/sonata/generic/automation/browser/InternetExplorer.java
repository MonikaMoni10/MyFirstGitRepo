/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.browser;

import org.openqa.selenium.ie.InternetExplorerDriver;

/**
 * The <code>InternetExplorer</code> class provides the {@link AbstractBrowser}
 * for the Microsoft Internet Explorer browser
 * */
class InternetExplorer extends AbstractBrowser
{
   /**
    * Constructor to specify the Internet Explorer Driver.
    * 
    * @param settings
    *           the settings to apply, which must not be null
    */
   InternetExplorer(BrowserSettings settings)
   {
      super("INTERNET_EXPLORER", settings, new InternetExplorerDriver(), 60);
   }   
}
