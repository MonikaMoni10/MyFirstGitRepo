/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.fixture.configuration;

/**
 * Constants relating to fixture configuration data file contents.
 */
public final class ConfigurationConstants
{
   /**
    * Don't create instances.
    */
   private ConfigurationConstants()
   {
   }

   /**
    * XML attribute names in fixture configuration data.
    */
   public static final class Attributes
   {
      /**
       * Don't create instances.
       */
      private Attributes()
      {
      }

      public static final String APPLICATION                 = "application";
      public static final String APPLICATIONFULLNAME         = "applicationfullname";
      public static final String CATEGORY                    = "category";
      public static final String DEFINITION_ID               = "definitionID";
      public static final String EXISTENCE_VALIDATION_WIDGET = "existenceValidationWidget";
      public static final String ID                          = "id";
      public static final String NAME                        = "name";
      public static final String MENUNAME                    = "menuName";
      public static final String TYPE                        = "type";
   }

   /**
    * XML element tag names in fixture configuration data.
    */
   public static final class Tags
   {
      /**
       * Don't create instances.
       */
      private Tags()
      {
      }

      public static final String FORM   = "form";
      public static final String UI     = "ui";
      public static final String WIDGET = "widget";
   }

   /**
    * Possible types of forms, as listed in the value of a "form" element's
    * "type" attribute. (Note that we don't bother with the extra code of an
    * enumeration since we are only doing one string comparison per "form"
    * element encountered and since we would have to do that same string
    * comparison anyway in order to create the appropriate enum item from the
    * string representation in the XML file).
    */
   public static final class FormType
   {
      /**
       * Don't create instances.
       */
      private FormType()
      {
      }

      public static final String MAIN  = "main";
      public static final String POPUP = "popup";
   }
}
