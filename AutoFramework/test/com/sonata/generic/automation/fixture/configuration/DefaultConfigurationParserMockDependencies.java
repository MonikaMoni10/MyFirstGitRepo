/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.fixture.configuration;

import org.easymock.EasyMock;
import org.easymock.IAnswer;

import com.sonata.generic.automation.browser.Browser;
import com.sonata.generic.automation.fixture.configuration.DefaultConfigurationParser;
import com.sonata.generic.automation.fixture.mock.MockUtils;
import com.sonata.generic.automation.fixture.widget.FixtureWidgetFactory;
import com.sonata.generic.automation.fixture.widget.ModifiableFixtureWidget;

/**
 * Mock dependencies for the {@link DefaultConfigurationParser} under test.
 */
final class DefaultConfigurationParserMockDependencies
{
   private final Browser                          mockBrowser;
   private final FixtureWidgetFactory             mockWidgetFactory;
   private static final AnswerCreateFixtureWidget ANSWER_CREATE_FIXTURE_WIDGET = new AnswerCreateFixtureWidget();

   /**
    * Constructs an object that has mock dependencies for a default
    * configuration parser.
    */
   public DefaultConfigurationParserMockDependencies()
   {
      this.mockBrowser = createMockBrowser();
      this.mockWidgetFactory = createMockWidgetFactory();
   }

   /**
    * Replays the mocks - this must be called right before exercising the class
    * under test.
    */
   public void replayMocks()
   {
      EasyMock.replay(mockBrowser, mockWidgetFactory);
   }

   /**
    * Verifies the mocks - this should be called after exercising the class
    * under test.
    */
   public void verifyMocks()
   {
      EasyMock.verify(mockBrowser, mockWidgetFactory);
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
    * Gets the mock {@link FixtureWidgetFactory}.
    * 
    * @return The mock {@link FixtureWidgetFactory}.
    */
   public FixtureWidgetFactory getMockWidgetFactory()
   {
      return mockWidgetFactory;
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
    * Creates a mock {@link FixtureWidgetFactory}.
    * 
    * @return The newly created mock {@link FixtureWidgetFactory}.
    */
   private FixtureWidgetFactory createMockWidgetFactory()
   {
      FixtureWidgetFactory factory = EasyMock.createNiceMock(FixtureWidgetFactory.class);
      EasyMock.expect(
            factory.createFixtureWidget(EasyMock.isA(String.class), EasyMock.isA(String.class), EasyMock
                  .isA(String.class), EasyMock.isA(String.class))).andAnswer(ANSWER_CREATE_FIXTURE_WIDGET).anyTimes();
      EasyMock.expect(factory.getBrowser()).andReturn(mockBrowser).anyTimes();
      return factory;
   }

   /**
    * Class for holding the arguments of a call to createFixtureWidget via
    * EasyMock.
    */
   private static class ArgsCreateFixtureWidget
   {
      private final String widgetName;
      private final String widgetID;
      private final String widgetType;
      private final String idBase;

      /**
       * Creates an instance of a more usable class for holding the arguments of
       * a call to createFixtureWidget (as opposed to just dealing with
       * Object[]).
       * 
       * @throws IllegalStateException
       *            Something was wrong with EasyMock's getCurrentArguments.
       */
      public ArgsCreateFixtureWidget()
      {
         Object[] args = EasyMock.getCurrentArguments();
         if ((null == args) || (args.length != 4))
            throw new IllegalStateException("createFixtureWidget must have 4 arguments");

         Object arg0 = args[0];
         if (null == arg0)
            this.widgetName = null;
         else if (arg0 instanceof String)
            this.widgetName = (String)arg0;
         else
            throw new IllegalArgumentException("The 2nd argument to createFixtureWidget must be a string or null");

         Object arg1 = args[1];
         if (null == arg1)
            this.widgetID = null;
         else if (arg1 instanceof String)
            this.widgetID = (String)arg1;
         else
            throw new IllegalArgumentException("The 2nd argument to createFixtureWidget must be a string or null");

         Object arg2 = args[2];
         if (null == arg2)
            this.widgetType = null;
         else if (arg2 instanceof String)
            this.widgetType = (String)arg2;
         else
            throw new IllegalArgumentException("The 3rd argument to createFixtureWidget must be a string or null");

         Object arg3 = args[3];
         if (null == arg3)
            this.idBase = null;
         else if (arg3 instanceof String)
            this.idBase = (String)arg3;
         else
            throw new IllegalArgumentException("The 4th argument to createFixtureWidget must be a string or null");
      }

      /**
       * Gets the widgetName argument.
       * 
       * @return The widgetName argument.
       */
      @SuppressWarnings("unused")
      public String getWidgetName()
      {
         return widgetName;
      }

      /**
       * Gets the widgetID argument.
       * 
       * @return The widgetID argument.
       */
      @SuppressWarnings("unused")
      public String getWidgetID()
      {
         return widgetID;
      }

      /**
       * Gets the widgetType argument.
       * 
       * @return The widgetType argument.
       */
      public String getWidgetType()
      {
         return widgetType;
      }

      /**
       * Gets the idBase argument.
       * 
       * @return The idBase argument.
       */
      @SuppressWarnings("unused")
      public String getIDBase()
      {
         return idBase;
      }
   }

   /**
    * 
    * Class for producing an answer to a call to createFixtureWidget via
    * EasyMock.
    */
   private static class AnswerCreateFixtureWidget implements IAnswer<ModifiableFixtureWidget>
   {
      @Override
      public ModifiableFixtureWidget answer() throws Throwable
      {
         ArgsCreateFixtureWidget args = new ArgsCreateFixtureWidget(); // gets current Easymock arguments.
         if (args.getWidgetType().equals(DefaultConfigurationParserMockConstants.MOCK_WIDGET_TYPE))
            return MockUtils.createReadyToUseMockFixtureWidget();
         else
            return null;
      }
   }
}
