/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.uibase;

import com.sonata.generic.automation.browser.TestMode;

/**
 * The <code>UIPropertiesImpl</code> class implements the {@link UIProperties}
 * interface based upon the properties passed to the subclasses of {@link UI}
 * that are part of this module.
 */
public class UIPropertiesImpl implements UIProperties
{
   private final String application;
   private final String applicationfullname;
   private final String category;
   private final String uiName;
   private final String uiMenuName;
   
   private final String signInValidationElement;
   
   private String iframe;

   /**
    * Constructs an instance of the {@link UIPropertiesImpl} class based upon
    * the {@link DefaultUIProperties} provided.
    * 
    * @param properties
    *           the properties available
    */
   protected UIPropertiesImpl(DefaultUIProperties properties)
   {
      signInValidationElement = properties.getSignInValidationElement();
      application = properties.getApplication();
      applicationfullname = properties.getApplicationfullname();
      category = properties.getCategory();
      uiName = properties.getUIName();
      uiMenuName = properties.getUiMenuName();
      iframe = properties.getIFrame();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getSignInValidationElement()
   {
      return signInValidationElement;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getURL(TestMode testMode)
   {
      return "payroll_employees";

      //      if (testMode == TestMode.ANT)
      //         return "/" + application + "/" + uiName + ".html";
      //      // return "/oe61a/" + uiName + ".html";
      //      else
      //         return "/SageERPAccpac/" + application + "/" + uiName + ".html";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getApplication()
   {
      return application;
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public String getApplicationfullname()
   {
      return applicationfullname;
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public String getCategory()
   {
      return category;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getUIName()
   {
      return uiName;
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public String getUiMenuName()
   {
      return uiMenuName;
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public String getIFrame()
   {
      return iframe;
   }
   
   
   /**
    * {@inheritDoc}
    */
   @Override
   public void setIFrame(String iframe)
   {
      this.iframe = iframe;
   }
}
