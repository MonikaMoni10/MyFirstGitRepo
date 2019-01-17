/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.widgets;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * The <code>CITests</code> provides a test suite containing all the Continuous
 * Integration (CI) tests of this module. These tests are intended for the CI
 * system to run, to check that the system is still robust. As such, they are
 * all unit tests that do not need specialized environments etc.
 * <p>
 * Developers should not use this suite except as a quick check. Before
 * submitting code the developer should run the {@link AllTests} suite, which
 * includes smoke tests, and any other tests mentioned there.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({WidgetPackageTestsSuite.class})
public class CITests
{
}
