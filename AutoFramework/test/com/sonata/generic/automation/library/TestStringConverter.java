/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.library;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.sonata.generic.automation.library.StringConverter;

/**
 * The <code>TestStringConverter</code> class provides JUnit tests on the
 * {@link StringConverter} class.
 * 
 */
public class TestStringConverter
{
   /** A small allowance for when testing equality of two doubles */
   private static final double EPSILON = 1.0e-7;

   @Test
   public void negativeCanBeInBraces()
   {
      assertEquals(-1.2, StringConverter.convertToDouble("(1.2)"), EPSILON);
   }

   @Test
   public void allowsMissingCloseBrace()
   {
      assertEquals(-1.2, StringConverter.convertToDouble("(1.2"), EPSILON);
   }

   @Test
   public void allowsCommasInNonNegative()
   {
      assertEquals(1234.56, StringConverter.convertToDouble("1,234.56"), EPSILON);
   }

   @Test
   public void commasCanBeAnywhere()
   {
      assertEquals(1234.56, StringConverter.convertToDouble("12,3,4.56"), EPSILON);
   }

}
