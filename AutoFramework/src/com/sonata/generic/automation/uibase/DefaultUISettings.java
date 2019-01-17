/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.uibase;

import com.sonata.generic.automation.library.DateHelper;

/**
 * The <code>DefaultUISettings</code> class provides the {@link UISettings} for
 * default operation
 */
class DefaultUISettings implements UISettings
{
   /**
    * The name of the setting permitted in a specification to signify the
    * default Accpac user
    */
   private static final String USER         = "ADMIN";

   /**
    * The name of the setting permitted in a specification to signify the
    * default Accpac user password
    */
   private static final String PASSWORD     = "ADMIN";

   /**
    * The name of the setting permitted in a specification to signify the
    * default Accpac company
    */
   private static final String COMPANY      = "SAMINC";

   /**
    * The name of the setting permitted in a specification to signify the
    * default Accpac session date
    */
   private static final String SESSION_DATE = DateHelper.getTodaysDate("MM/dd/yyyy");

   /**
    * {@inheritDoc}
    */
   @Override
   public String getDefaultUser()
   {
      return USER;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getDefaultPassword()
   {
      return PASSWORD;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getDefaultCompany()
   {
      return COMPANY;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getDefaultSessionDate()
   {
      return SESSION_DATE;
   }

}
