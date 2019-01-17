/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.fixture.widget;

import org.easymock.EasyMock;

import com.sonata.generic.automation.browser.Browser;
import com.sonata.generic.automation.fixture.browser.BrowserFactory;
import com.sonata.generic.automation.fixture.widget.DefaultFixtureWidgetFactory;

/**
 * Mock dependencies for the {@link DefaultFixtureWidgetFactory} under test.
 */
final class DefaultFixtureWidgetFactoryMockDependencies
{
   private final Browser        mockDefaultBrowser;
   private final Browser        mockSpecifiedBrowser;
   private final BrowserFactory mockBrowserFactory;

   /**
    * Constructs an object that has mock dependencies for a default fixture
    * widget factory.
    */
   public DefaultFixtureWidgetFactoryMockDependencies()
   {
      this.mockDefaultBrowser = createMockBrowser();
      this.mockSpecifiedBrowser = createMockBrowser();
      this.mockBrowserFactory = createMockBrowserFactory();
   }

   /**
    * Replays the mocks - this must be called right before exercising the class
    * under test.
    */
   public void replayMocks()
   {
      EasyMock.replay(mockDefaultBrowser, mockSpecifiedBrowser, mockBrowserFactory);
   }

   /**
    * Verifies the mocks - this should be called after exercising the class
    * under test.
    */
   public void verifyMocks()
   {
      EasyMock.verify(mockDefaultBrowser, mockSpecifiedBrowser, mockBrowserFactory);
   }

   /**
    * Gets the mock default {@link Browser}.
    * 
    * @return The mock default {@link Browser}.
    */
   public Browser getMockDefaultBrowser()
   {
      return mockDefaultBrowser;
   }

   /**
    * Gets the mock setting-specified {@link Browser}.
    * 
    * @return The mock setting-specified {@link Browser}.
    */
   public Browser getMockSpecifiedBrowser()
   {
      return mockSpecifiedBrowser;
   }

   /**
    * Gets the mock {@link BrowserFactory}.
    * 
    * @return The mock {@link BrowserFactory}.
    */
   public BrowserFactory getMockBrowserFactory()
   {
      return mockBrowserFactory;
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
    * Creates a mock {@link BrowserFactory}.
    * 
    * @return The newly created mock {@link BrowserFactory}.
    */
   private BrowserFactory createMockBrowserFactory()
   {
      BrowserFactory factory = EasyMock.createNiceMock(BrowserFactory.class);
      EasyMock.expect(factory.createDefaultBrowser()).andReturn(mockDefaultBrowser).anyTimes();
      EasyMock.expect(factory.createSpecifiedBrowser(EasyMock.isA(String.class))).andReturn(mockSpecifiedBrowser)
            .anyTimes();
      return factory;
   }
}
