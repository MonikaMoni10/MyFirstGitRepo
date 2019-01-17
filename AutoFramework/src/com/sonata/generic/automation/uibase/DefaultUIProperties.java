/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.uibase;

/**
 * The <code>DefaultUIProperties</code> interface defines which properties the
 * subclass must provide to a {@link DefaultUI} at construction time.
 */
public interface DefaultUIProperties extends Properties
{
   /**
    * the element that can be used to check that Sign-In is complete.
    * 
    * @return the debug id of an element that will appear after signing in
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
    * Gets the name of the UI (e.g. Options)
    * 
    * @return the name of the UI (e.g. Options)
    */
   String getUIName();
   
   /**
    * Gets the name of the UI shown in the menu (e.g. Batch List)
    * 
    * @return the name of the UI (e.g. Batch List)
    */
   String getUiMenuName();
   
   /**
    * get the ID of the iFrame which hold an screen
    * 
    * @return A String containing the iFrame ID
    */
   String getIFrame();
   
   /**
    * set the ID of the iFrame which hold an screen when open it
    * 
    * @param iFrame
    *           the iFrame of a ui
    */
   void setIFrame(String iFrame);

   /**
    * @return id prefix
    */
   String getIDBase();
}
