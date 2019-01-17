/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.fixture.configuration;

import com.sonata.generic.automation.fixture.widget.DefaultFixtureWidgetFactory;
import com.sonata.generic.automation.fixture.widget.FixtureWidgetFactory;

/**
 * Default implementation of a {@link ConfigurationParserFactory}.
 */
public final class DefaultConfigurationParserFactory implements ConfigurationParserFactory
{
   /**
    * {@inheritDoc}
    */
   @Override
   public ConfigurationParser createConfigurationParser(final String browserSettings)
   {
      // Using the specified browser settings, create a default fixture widget
      // factory and a default parser that will use that factory to create the
      // fixture widgets in a parse-generated fixture properties object.
      // NOTE: Browser settings can be null or empty (but some non-empty
      //       browser settings may be invalid and the fixture widget factory
      //       would throw an IllegalArgumentException).
      FixtureWidgetFactory factory = new DefaultFixtureWidgetFactory(browserSettings);
      ConfigurationParser parser = new DefaultConfigurationParser(factory);

      return parser;
   }
}
