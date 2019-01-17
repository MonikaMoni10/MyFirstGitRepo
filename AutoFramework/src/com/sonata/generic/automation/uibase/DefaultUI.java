/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.uibase;

import com.sonata.generic.automation.browser.Browser;

/**
 * The <code>DefaultUI</code> represents a UI that is not of any other specific
 * type
 */
public class DefaultUI extends UI
{
   private final DefaultUIProperties properties;
   private final Browser             browser;

   /**
    * constructor.
    * 
    * @param properties
    *           DefaultUIProperties
    * @param browser
    *           current browser
    */
   protected DefaultUI(DefaultUIProperties properties, Browser browser)
   {
      super(new UIPropertiesImpl(properties), browser);
      this.properties = properties;
      this.browser = browser;
   }

   /**
    * constructor
    * 
    * @param browser
    *           current browser
    */
   protected DefaultUI(Browser browser)
   {
      this(null, browser);
   }

   /**
    * @return the browser
    */
   public Browser getBrowser()
   {
      return browser;
   }

   /**
    * @return the properties
    */
   protected DefaultUIProperties getProperties()
   {
      return properties;
   }
}
