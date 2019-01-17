/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.uibase;

import com.sonata.generic.automation.browser.TestMode;

/**
 * The <code>UIProperties</code> interface is all those properties that an
 * actual UI must provide to allow testing.
 */
public interface UIProperties extends Properties
{
   /**
    * provides the name of the element that can be used to validate sign-in
    * 
    * @return A String containing the name of the widget
    */
   String getSignInValidationElement();

   /**
    * Gets the application (e.g. GL) of the UI
    * 
    * @return the application (e.g. GL) of the UI
    */
   String getApplication();
   
   /**
    * Gets the applicationfullname (e.g. General Ledger) of the UI
    * 
    * @return the application (e.g. General Ledger) of the UI
    */
   String getApplicationfullname();
   
   /**
    * Gets the the category (e.g. G/L Setup)
    * 
    * @return the category (e.g. G/L Setup)
    */
   String getCategory();

   /**
    * Gets the name of the UI shown in url (e.g. BatchList)
    * 
    * @return the name of the UI (e.g. BatchList)
    */
   String getUIName();
   
   /**
    * Gets the name of the UI shown in the menu (e.g. Batch List)
    * 
    * @return the name of the UI (e.g. Batch List)
    */
   String getUiMenuName();
   
   /**
    * provides the URL that is to be used to access the UI
    * 
    * @param testMode
    *           the mode that the test is being run in
    * @return A String containing the URL
    */
   String getURL(TestMode testMode);
   
   /**
    * get the ID of the iFrame which hold an screen
    * 
    * @return A String containing the iFrame ID
    */
   String getIFrame();
   
   /**
    * set the ID of the iFrame which hold an screen when open it
    * 
    */
   void setIFrame(String iFrame);

}
