/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.fixture.configuration;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * This class provides a JUnit test suite for all the JUnit tests in the
 * package.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses( {DefaultConfigurationParserTest.class, DefaultFixturePropertiesTest.class})
public class ConfigurationPackageTestSuite
{

}
