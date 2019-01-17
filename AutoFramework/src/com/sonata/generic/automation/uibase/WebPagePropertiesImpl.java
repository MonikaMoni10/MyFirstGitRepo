/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.uibase;

import com.sonata.generic.automation.browser.TestMode;

/**
 * The <code>WebPagePropertiesImpl</code> class implements the
 * {@link WebPageProperties} interface based upon the properties passed to the
 * subclasses of {@link WebPage} that are part of this module.
 * 
 */
final class WebPagePropertiesImpl implements WebPageProperties
{
   private final UIProperties parent;
   private final String       signInValidationElement;

   /**
    * Constructs an instance of the {@link WebPagePropertiesImpl} class based
    * upon the {@link UIProperties} provided.
    * 
    * @param properties
    *           the properties available
    */
   protected WebPagePropertiesImpl(UIProperties properties)
   {
      parent = properties;
      signInValidationElement = properties.getSignInValidationElement();
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
      return parent.getURL(testMode);
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public String getApplication()
   {
      return parent.getApplication();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getApplicationfullname()
   {
      return parent.getApplicationfullname();
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public String getUiMenuName()
   {
      return parent.getUiMenuName();
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public String getCategory()
   {
      return parent.getCategory();
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public String getUIName()
   {
      return parent.getUIName();
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public String getIFrame()
   {
      return parent.getIFrame();
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public void setIFrame(String iframe)
   {
      parent.setIFrame(iframe);
   }
   
   
   
}
