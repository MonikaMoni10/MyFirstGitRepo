/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.fixture;

/**
 * Constants relating to the SwtUIFixture.
 */
public final class GenericWebFixtureConstants
{
   /**
    * Don't create instances.
    */
   private GenericWebFixtureConstants()
   {
   }

   /**
    * The various responses (buttons) that a message dialog could possibly have.
    */
   public enum MessageDialogResponses {No,OK,Yes,Cancel,Print,Delete}
   
   /**
    * Possible file extensions to save as when exporting report data.
    */
   public static final class FileExtensions
   {
      /**
       * Don't create instances.
       */
      private FileExtensions()
      {
      }

      public static final String CSV = "csv";
      public static final String PDF = "pdf";
      public static final String XML = "xml";
   }   
}
