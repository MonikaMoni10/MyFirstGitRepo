/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.browser;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * The <code>AllTests</code> class provides a JUnit test suite that includes all
 * the unit tests and smoke tests (but excludes a couple of tests that require
 * special test environments). This suite should be run regularly and especially
 * before submitting code to the version control system.
 * <p>
 * The following require special conditions:
 * <ul>
 * <li>{@link EnvTestBrowserType} requires a modified 'Environment'</li>
 * <li>{@link EnvTestDefaultSettings} requires a modified 'Environment'</li>
 * <li>{@link SysPropTestBrowserType} requires a modified set of Java VM
 * arguments</li>
 * <li>{@link SysPropTestDefaultSettings} requires a modified set of Java VM
 * arguments</li>
 * </ul>
 * See those tests for further details
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({CITests.class, SmokeTestBrowserType.class})
public class AllTests
{
}
