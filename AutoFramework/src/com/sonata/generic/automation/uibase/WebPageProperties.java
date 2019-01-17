/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.uibase;

import com.sonata.generic.automation.browser.TestMode;

/**
 * The <code>WebPageProperties</code> interface defines which properties the
 * subclass must provide to a {@link WebPage} at construction time.
 */
public interface WebPageProperties
{
   /**
    * the element that can be used to check that Sign-In is complete.
    * 
    * @return the debug id of an element that will appear after signing in
    */
   String getSignInValidationElement();

   /**
    * the partial URL that must be specified to open the {@link WebPage}.
    * 
    * @param testMode
    *           the test mode that the test is running in, which may have an
    *           effect on what URL is used
    * @return the URL that must be specified.
    */
   String getURL(TestMode testMode);
   
   /**
    * Gets the application (e.g. GL) of the UI
    * 
    * @return the application (e.g. GL) of the UI
    */
   String getApplication();
   
   /**
    * Gets the applicationfullname (e.g. General Ledger) of the UI
    * 
    * @return the applicationfullname (e.g. General Ledger) of the UI
    */
   String getApplicationfullname();

   /**
    * Gets the category (e.g. G/L Setup)
    * 
    * @return the category (e.g. G/L Setup)
    */
   String getCategory();
   
   /**
    * Gets the uiMenuName (e.g. Batch List) of the UI
    * 
    * @return the uiMenuName (e.g. uiMenuName) of the UI
    */
   String getUiMenuName();

   /**
    * Gets the name of the UI shown in url (e.g. BatchList)
    * 
    * @return the name of the UI (e.g. BatchList)
    */
   String getUIName();
   
   /**
    * Gets the iframe of the UI (e.g. iFrameMenu1)
    * 
    * @return the iframe of the UI (e.g. iFrameMenu1)
    */
   String getIFrame();
   
   /**
    * Sets the iframe of the UI (e.g. iFrameMenu1)
    */
   void setIFrame(String iframe);

}
