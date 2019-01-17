/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.uibase;

/**
 * The <code>UISettings</code> interface provides the settings that any
 * {@link UI} typically needs to know to operate.
 */
public interface UISettings
{
   /**
    * gets the default Accpac User to use for signin
    * 
    * @return the user to utilize when signing into Accpac
    */
   String getDefaultUser();

   /**
    * gets the default Accpac Password to use for signin
    * 
    * @return the user password to utilize when signing into Accpac
    */
   String getDefaultPassword();

   /**
    * gets the default Accpac company to use for signin
    * 
    * @return the company to utilize when signing into Accpac
    */
   String getDefaultCompany();

   /**
    * gets the default Accpac Session Date to use for signin
    * 
    * @return the session date to utilize when signing into Accpac
    */
   String getDefaultSessionDate();

}
