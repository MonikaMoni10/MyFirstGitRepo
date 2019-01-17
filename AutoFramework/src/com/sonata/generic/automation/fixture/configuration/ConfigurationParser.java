/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.fixture.configuration;

import java.io.File;

import org.w3c.dom.Document;

/**
 * Interface for a parser that transforms fixture configuration data into a
 * {@link FixtureProperties} object that the fixture can use easily.
 */
public interface ConfigurationParser
{
   /**
    * Parses a UI's fixture configuration data, as found in the file at the
    * specified configuration path, into a {@link FixtureProperties} object that
    * is easily consumable by the fixture.
    * 
    * @param configurationPath
    *           The path to the file containing fixture configuration
    *           information for the UI (for example,
    *           "c:\fitnesse\fixtureconfig\ar5104_widgetinfo.xml").
    * 
    * @return The {@link FixtureProperties} containing configuration information
    *         for the fixture.
    * 
    * @throws IllegalArgumentException
    *            The configuration path is null or invalid.
    */
   FixtureProperties parse(String configurationPath);

   /**
    * Parses a UI's fixture configuration data, as found in the specified
    * configuration file, into a {@link FixtureProperties} object that is easily
    * consumable by the fixture.
    * 
    * @param configurationFile
    *           The file containing fixture configuration information for the
    *           UI.
    * 
    * @return The {@link FixtureProperties} containing configuration information
    *         for the fixture.
    * 
    * @throws IllegalArgumentException
    *            The configuration file is null or invalid.
    */
   FixtureProperties parse(File configurationFile);

   /**
    * Parses a UI's fixture configuration data, as found in the specified DOM
    * document, into a {@link FixtureProperties} object that is easily
    * consumable by the fixture.
    * 
    * @param configurationDoc
    *           The DOM document containing the fixture configuration data.
    * 
    * @return The {@link FixtureProperties} containing configuration information
    *         for the fixture.
    * 
    * @throws IllegalArgumentException
    *            The configuration DOM document is null or does not contain
    *            valid fixture configuration data.
    */
   FixtureProperties parse(Document configurationDoc);
}
