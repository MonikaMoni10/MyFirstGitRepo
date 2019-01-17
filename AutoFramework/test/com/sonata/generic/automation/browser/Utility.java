/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.browser;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Locale;

/**
 * The <code>Utility</code> class provides utilities for the testing code in
 * this module
 */
public final class Utility
{
   /** Utility classes hide their constructors */
   private Utility()
   {
   }

   /**
    * checks to see that the given name does not contain any names we want to
    * keep hidden. For example 'Orion' is a code name and should not appear in
    * the public API and this package is a facade on the Selenium package, in
    * which case that should be hidden.
    * 
    * @param nameToTest
    *           the name to check
    * @return true if the name does not contain any of the forbidden names
    */
   private static boolean containsNoForbiddenNames(final String nameToTest)
   {
      final String[] lowerCaseForbiddenNames = {"orion", "selenium"};
      final String lcNameToTest = nameToTest.toLowerCase(Locale.ENGLISH);
      for (String forbiddenName : lowerCaseForbiddenNames)
      {
         if (lcNameToTest.contains(forbiddenName))
         {
            return false;
         }
      }
      return true;
   }

   /**
    * Confirms that the public names of the given class meet naming conventions
    * etc.
    * <p>
    * This method uses JUnit assertions to report if the requirements are not
    * met.
    * 
    * @param thisClass
    *           the class to check
    */
   public static void confirmPublicNamesAreAcceptable(Class<? extends Object> thisClass)
   {
      final String comDotSageDot = "com.sage.";
      final String canonicalClassName = thisClass.getCanonicalName();

      assertTrue(canonicalClassName.startsWith(comDotSageDot));
      final String namePastSageDot = canonicalClassName.substring(comDotSageDot.length());
      assertTrue(namePastSageDot.startsWith("swt.") || namePastSageDot.startsWith("accpac."));

      assertTrue("The class name '" + canonicalClassName + "' contains a forbidden term",
            Utility.containsNoForbiddenNames(canonicalClassName));
      for (Method m : thisClass.getDeclaredMethods())
      {
         if (Modifier.isPublic(m.getModifiers()))
         {
            // Wow.  We can test the name, the parameters and the return type in one operation!
            assertTrue("The method '" + m.getName() + "' contains a forbidden term",
                  Utility.containsNoForbiddenNames(m.toGenericString()));
         }
      }
   }

}
