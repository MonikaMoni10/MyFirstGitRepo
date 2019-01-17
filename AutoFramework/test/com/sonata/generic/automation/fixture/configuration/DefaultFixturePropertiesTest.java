/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.fixture.configuration;

import static org.junit.Assert.fail;

import org.junit.Test;

import com.sonata.generic.automation.fixture.configuration.DefaultFixtureProperties;

/**
 * Unit tests for {@link DefaultFixtureProperties}.
 * 
 */
public class DefaultFixturePropertiesTest
{
   /**
    * Tests that if we construct a {@link DefaultFixtureProperties} object with
    * all null arguments we should get an exception.
    */
   @Test(expected=IllegalArgumentException.class)
   public void constructWithAllNullArgumentsShouldFail()
   {
      new DefaultFixtureProperties(null, null, null, null, null, null, null, null);
      fail("Should have gotten an IllegalArgumentException");
   }
}
