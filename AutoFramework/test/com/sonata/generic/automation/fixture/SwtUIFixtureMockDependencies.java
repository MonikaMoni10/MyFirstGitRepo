/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.fixture;

import java.io.File;

import org.easymock.EasyMock;
import org.w3c.dom.Document;

import com.sonata.generic.automation.browser.Browser;
import com.sonata.generic.automation.browser.TestMode;
import com.sonata.generic.automation.fixture.browser.BrowserTiming;
import com.sonata.generic.automation.fixture.configuration.ConfigurationParser;
import com.sonata.generic.automation.fixture.configuration.ConfigurationParserFactory;
import com.sonata.generic.automation.fixture.configuration.FixtureProperties;
import com.sonata.generic.automation.fixture.mock.MockUtils;
import com.sonata.generic.automation.fixture.widget.FixtureWidget;
import com.sonata.generic.automation.library.TimeDelay;

/**
 * Class containing mock dependencies for the {@link SwtUIFixture} under test.
 */
class SwtUIFixtureMockDependencies
{
   private final Browser                    mockBrowser;
   private final BrowserTiming              mockBrowserTiming;
   private final FixtureProperties          mockFixtureProperties;
   private final ConfigurationParser        mockParser;
   private final ConfigurationParserFactory mockParserFactory;

   /**
    * Constructs an object that has mock dependencies for a SWT UI fixture.
    */
   public SwtUIFixtureMockDependencies()
   {
      this.mockBrowser = createMockBrowser();
      this.mockBrowserTiming = createMockBrowserTiming();
      this.mockFixtureProperties = createMockFixtureProperties();
      this.mockParser = createMockParser();
      this.mockParserFactory = createMockParserFactory();
   }

   /**
    * Replays the mocks - this must be called right before exercising the class
    * under test.
    */
   public void replayMocks()
   {
      EasyMock.replay(mockBrowser, mockBrowserTiming, mockFixtureProperties, mockParser, mockParserFactory);
   }

   /**
    * Verifies the mocks - this should be called after exercising the class
    * under test.
    */
   public void verifyMocks()
   {
      EasyMock.verify(mockBrowser, mockBrowserTiming, mockFixtureProperties, mockParser, mockParserFactory);
   }

   /**
    * Gets the mock {@link Browser}.
    * 
    * @return The mock {@link Browser}.
    */
   public Browser getMockBrowser()
   {
      return mockBrowser;
   }

   /**
    * Gets the mock {@link BrowserTiming}.
    * 
    * @return The mock {@link BrowserTiming}.
    */
   public BrowserTiming getMockBrowserTiming()
   {
      return mockBrowserTiming;
   }

   /**
    * Gets the mock {@link FixtureProperties}.
    * 
    * @return The mock {@link FixtureProperties}.
    */
   public FixtureProperties getMockFixtureProperties()
   {
      return mockFixtureProperties;
   }

   /**
    * Gets the mock {@link ConfigurationParser}.
    * 
    * @return The mock {@link ConfigurationParser}.
    */
   public ConfigurationParser getMockParser()
   {
      return mockParser;
   }

   /**
    * Gets the mock {@link ConfigurationParserFactory}.
    * 
    * @return The mock {@link ConfigurationParserFactory}.
    */
   public ConfigurationParserFactory getMockParserFactory()
   {
      return mockParserFactory;
   }

   /**
    * Creates a mock {@link Browser}.
    * 
    * @return The newly created mock {@link Browser}.
    */
   private static Browser createMockBrowser()
   {
      Browser browser = EasyMock.createMock(Browser.class);
      return browser;
   }

   /**
    * Creates a mock {@link BrowserTiming}.
    * 
    * @return The newly created mock {@link BrowserTiming}.
    */
   private static BrowserTiming createMockBrowserTiming()
   {
      BrowserTiming browserTiming = EasyMock.createMock(BrowserTiming.class);
      EasyMock.expect(browserTiming.doExtraMaximumPause()).andReturn(true).anyTimes();
      EasyMock.expect(browserTiming.doLargePause()).andReturn(true).anyTimes();
      EasyMock.expect(browserTiming.doMaximumPause()).andReturn(true).anyTimes();
      EasyMock.expect(browserTiming.doMediumPause()).andReturn(true).anyTimes();
      EasyMock.expect(browserTiming.doMinimumPause()).andReturn(true).anyTimes();
      EasyMock.expect(browserTiming.doPause(EasyMock.anyInt())).andReturn(true).anyTimes();
      EasyMock.expect(browserTiming.doSmallPause()).andReturn(true).anyTimes();
      EasyMock.expect(browserTiming.getDefaultInterval()).andReturn(TimeDelay.getDefaultInterval()).anyTimes();
      EasyMock.expect(browserTiming.getDefaultTimeout()).andReturn(TimeDelay.getDefaultTimeout()).anyTimes();
      EasyMock.expect(browserTiming.getTimeoutExtraMaximum()).andReturn(TimeDelay.getTimeoutExtraMaximum()).anyTimes();
      EasyMock.expect(browserTiming.getTimeoutLarge()).andReturn(TimeDelay.getTimeoutLarge()).anyTimes();
      EasyMock.expect(browserTiming.getTimeoutMassive()).andReturn(TimeDelay.getTimeoutMassive()).anyTimes();
      EasyMock.expect(browserTiming.getTimeoutMaximum()).andReturn(TimeDelay.getTimeoutMaximum()).anyTimes();
      EasyMock.expect(browserTiming.getTimeoutMedium()).andReturn(TimeDelay.getTimeoutMedium()).anyTimes();
      EasyMock.expect(browserTiming.getTimeoutMinimum()).andReturn(TimeDelay.getTimeoutMinimum()).anyTimes();
      EasyMock.expect(browserTiming.getTimeoutSmall()).andReturn(TimeDelay.getTimeoutSmall()).anyTimes();
      return browserTiming;
   }

   /**
    * Creates a mock {@link FixtureProperties}.
    * 
    * @return The newly created mock {@link FixtureProperties}.
    */
   private FixtureProperties createMockFixtureProperties()
   {
      FixtureWidget mockMainExistenceWidget = MockUtils.createReadyToUseMockFixtureWidget();
      FixtureWidget mockPopup1ExistenceWidget = MockUtils.createReadyToUseMockFixtureWidget();
      FixtureWidget mockPopup2ExistenceWidget = MockUtils.createReadyToUseMockFixtureWidget();
      FixtureWidget mockMainWidget = MockUtils.createReadyToUseMockFixtureWidget();
      FixtureWidget mockPopup1Widget1 = MockUtils.createReadyToUseMockFixtureWidget();
      FixtureWidget mockPopup2Widget1 = MockUtils.createReadyToUseMockFixtureWidget();

      FixtureProperties properties = EasyMock.createNiceMock(FixtureProperties.class);
      EasyMock.expect(properties.getApplication()).andReturn("mockapp").anyTimes();
      EasyMock.expect(properties.getBrowser()).andReturn(mockBrowser).atLeastOnce();
      EasyMock.expect(properties.getExistenceValidationWidget("")).andReturn(mockMainExistenceWidget).anyTimes();
      EasyMock.expect(properties.getExistenceValidationWidget("mockPopupForm1")).andReturn(mockPopup1ExistenceWidget)
            .anyTimes();
      EasyMock.expect(properties.getExistenceValidationWidget("mockPopupForm2")).andReturn(mockPopup2ExistenceWidget)
            .anyTimes();
      EasyMock.expect(properties.getFixtureWidget("", "mockMainWidget1")).andReturn(mockMainWidget).anyTimes();
      EasyMock.expect(properties.getFixtureWidget("mockPopupForm1", "mockPopup1Widget1")).andReturn(mockPopup1Widget1)
            .anyTimes();
      EasyMock.expect(properties.getFixtureWidget("mockPopupForm2", "mockPopup2Widget1")).andReturn(mockPopup2Widget1)
            .anyTimes();
      EasyMock.expect(properties.getFixtureWidget(EasyMock.isA(String.class), EasyMock.isA(String.class)))
            .andStubReturn(null);
      EasyMock.expect(properties.getSignInValidationElement()).andReturn("mockSignInValidationElement").anyTimes();
      EasyMock.expect(properties.getUIName()).andReturn("mockui").anyTimes();
 //     EasyMock.expect(properties.getMessageBoxLocator()).andReturn("MessageDialog").anyTimes();
 //     EasyMock.expect(properties.getPopUpLocator()).andReturn("swt-DialogBox").anyTimes();
      EasyMock.expect(properties.getURL(EasyMock.isA(TestMode.class))).andReturn("mockurl").anyTimes();
      return properties;
   }

   /**
    * Creates a mock {@link ConfigurationParserFactory}.
    * 
    * @return The newly created mock {@link ConfigurationParserFactory}.
    */
   private ConfigurationParserFactory createMockParserFactory()
   {
      ConfigurationParserFactory factory = EasyMock.createMock(ConfigurationParserFactory.class);
      EasyMock.expect(factory.createConfigurationParser(EasyMock.isA(String.class))).andReturn(mockParser).anyTimes();
      EasyMock.expect(factory.createConfigurationParser(null)).andReturn(mockParser).anyTimes();
      return factory;
   }

   /**
    * Creates a mock {@link ConfigurationParser}.
    * 
    * @return The newly created mock {@link ConfigurationParser}.
    */
   private ConfigurationParser createMockParser()
   {
      ConfigurationParser parser = EasyMock.createMock(ConfigurationParser.class);
      EasyMock.expect(parser.parse(EasyMock.isA(Document.class))).andReturn(mockFixtureProperties).anyTimes();
      EasyMock.expect(parser.parse(EasyMock.isA(File.class))).andReturn(mockFixtureProperties).anyTimes();
      EasyMock.expect(parser.parse(EasyMock.isA(String.class))).andReturn(mockFixtureProperties).anyTimes();
      return parser;
   }
}
