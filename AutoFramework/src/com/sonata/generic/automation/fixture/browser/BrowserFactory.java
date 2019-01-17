/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.fixture.browser;

import com.sonata.generic.automation.browser.Browser;

/**
 * Interface for a factory that creates automation {@link Browser} objects.
 */
public interface BrowserFactory
{
   /**
    * Creates an automation {@link Browser} object using default browser
    * settings.
    * 
    * @return The newly created automation {@link Browser} object that was
    *         created using default browser settings.
    */
   Browser createDefaultBrowser();

   /**
    * Creates an automation {@link Browser} object using the specified browser
    * settings.
    * 
    * @param browserSettings
    *           The settings used to create a specific kind of automation
    *           {@link Browser} object.
    * 
    * @return The newly created automation {@link Browser} object that was
    *         created using the specified browser settings.
    */
   Browser createSpecifiedBrowser(String browserSettings);
}
