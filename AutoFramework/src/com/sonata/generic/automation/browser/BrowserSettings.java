/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.browser;

/**
 * The <code>BrowserSettings</code> interface provides the settings that a
 * {@link Browser} typically needs to know to operate.
 */
public interface BrowserSettings
{
   /**
    * gets the base URL that the browser will be addressing, including protocol,
    * address and port but not including the trailing forward slash
    * 
    * @return the base URL from the protocol up to but not including the first
    *         path separator
    */
   String getBaseURL();

   /**
    * gets the default timeout for an operation, in milliseconds
    * 
    * @return the number of milliseconds to wait for operations
    */
   int getDefaultTimeOut();

   /**
    * gets the default polling interval used when polling for changes
    * 
    * @return the number of milliseconds to pause before re-checking something
    */
   int getDefaultInterval();

   /**
    * gets the large timeout used for long operations
    * 
    * @return the number of milliseconds to allow for long operations
    */
   int getLargeTimeOut();

   /**
    * gets the small timeout used for short waiting
    * 
    * @return the number of milliseconds to allow for short waiting
    */
   int getSmallTimeOut();

   /**
    * gets the mode that the test is being run in.
    * 
    * @return the mode that the test is being run in.
    */
   TestMode getTestMode();

}
