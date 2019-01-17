/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.widgets;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * The <code>AllTests</code> class provides a JUnit test suite that includes all
 * the unit tests and smoke tests (but excludes any tests that require special
 * test environments). This suite should be run regularly and especially before
 * submitting code to the version control system.
 * <p>
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({CITests.class})
public class AllTests
{
}
