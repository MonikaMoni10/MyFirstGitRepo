/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.browser;

/**
 * The <code>StringUtils</code> provides a couple of useful utilities required
 * in more than one class
 * 
 */
final class StringUtils
{
   /** No instantiation */
   private StringUtils()
   {
   }

   /**
    * Returns the first non-null value in the list, or null if they are all null
    * 
    * @param strings
    *           the strings to check
    * @return the first string that is not null, or null
    */
   static String getFirstNonNull(String... strings)
   {
      for (String string : strings)
      {
         if (string != null)
         {
            return string;
         }
      }
      return null;
   }

}
