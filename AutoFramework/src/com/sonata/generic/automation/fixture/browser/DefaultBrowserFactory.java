/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.fixture.browser;

import com.sonata.generic.automation.browser.Browser;
import com.sonata.generic.automation.browser.BrowserType;

/**
 * Default implementation of a {@link BrowserFactory}.
 */
public final class DefaultBrowserFactory implements BrowserFactory
{
   /**
    * {@inheritDoc}
    */
   @Override
   public Browser createDefaultBrowser()
   {
      return BrowserType.createDefaultBrowser();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Browser createSpecifiedBrowser(final String browserSettings)
   {
      return BrowserType.createSpecifiedBrowser(browserSettings);
   }
}
