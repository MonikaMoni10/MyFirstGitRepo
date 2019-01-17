/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.browser;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * The <code>BrowserPackageTestsSuite</code> class provides a suite for all the
 * unit tests in the package.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({TestAbstractBrowser.class, TestBrowser.class, TestBrowserSettings.class,
      TestBrowserSpecParser.class, TestBrowserType.class, TestDefautBrowserSettings.class, TestSpecialKey.class, TestTestMode.class})
public class BrowserPackageTestsSuite
{

}
