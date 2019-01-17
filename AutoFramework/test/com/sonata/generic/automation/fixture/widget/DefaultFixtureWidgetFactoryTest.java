/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.fixture.widget;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sonata.generic.automation.fixture.widget.AbstractFixtureWidget;
import com.sonata.generic.automation.fixture.widget.DefaultFixtureWidgetFactory;
import com.sonata.generic.automation.fixture.widget.FixtureButton;
import com.sonata.generic.automation.fixture.widget.FixtureCheckBox;
import com.sonata.generic.automation.fixture.widget.FixtureLabel;
import com.sonata.generic.automation.fixture.widget.FixtureListBox;
import com.sonata.generic.automation.fixture.widget.FixtureRadioButton;
import com.sonata.generic.automation.fixture.widget.FixtureTab;
import com.sonata.generic.automation.fixture.widget.FixtureTable;
import com.sonata.generic.automation.fixture.widget.FixtureTextBox;
import com.sonata.generic.automation.fixture.widget.FixtureWidgetFactory;
import com.sonata.generic.automation.fixture.widget.ModifiableFixtureWidget;

/**
 * Unit tests for {@link DefaultFixtureWidgetFactory}.
 */
public class DefaultFixtureWidgetFactoryTest
{
   private DefaultFixtureWidgetFactoryMockDependencies mockDependencies;
   private DefaultFixtureWidgetFactory                 factory;

   private static final String                         MOCK_BROWSER_SETTINGS = "mockBrowserSettings";
   private static final String                         MOCK_ID_BASE          = "mockUIName_";
   private static final String                         MOCK_LOCATOR          = "mockUIName_mockWidgetID";
   private static final String                         MOCK_WIDGET_ID        = "mockWidgetID";
   private static final String                         MOCK_WIDGET_NAME      = "mockWidgetName";

   /**
    * Setup that happens before each test.
    */
   @Before
   public void setUp()
   {
      mockDependencies = new DefaultFixtureWidgetFactoryMockDependencies();
      assertNotNull("'mockDependencies' should be non-null at the end of setup", mockDependencies);
      mockDependencies.replayMocks();

      // NOTE: This also tests that creating a factory with null browser
      //       settings will return a default browser.
      factory = new DefaultFixtureWidgetFactory(null, mockDependencies.getMockBrowserFactory());
      assertNotNull("'factory' should be non-null at the end of setup", factory);
      assertSame("fixture widget factory's browser", mockDependencies.getMockDefaultBrowser(), factory.getBrowser());
   }

   /**
    * Tear down that happens after each test.
    */
   @After
   public void tearDown()
   {
      mockDependencies.verifyMocks();
      mockDependencies = null;
      assertNull("'mockDependencies' should be null at the end of teardown", mockDependencies);

      factory = null;
      assertNull("'factory' should be null at the end of teardown", factory);
   }

   /**
    * Tests that we cannot construct an instance of the fixture widget factory
    * with a null browser factory.
    */
   @Test(expected = IllegalArgumentException.class)
   public void cannotConstructWithNullBrowserFactory()
   {
      new DefaultFixtureWidgetFactory("some settings", null);
      fail("Should have thrown an IllegalArgumentException");
   }

   /**
    * Tests that we can construct an instance of the fixture widget factory with
    * empty browser settings and that it should have created a default browser.
    * (Note that part of {@link #setUp() setUp} code already tests the case of
    * null browser settings.)
    */
   @Test
   public void constructWithEmptyBrowserSettingsGiveDefaultBrowser()
   {
      FixtureWidgetFactory fixtureWidgetFactory = new DefaultFixtureWidgetFactory("", mockDependencies
            .getMockBrowserFactory());
      assertNotNull("'fixtureWidgetFactory' should be non-null", fixtureWidgetFactory);
      assertSame("fixture widget factory's browser", mockDependencies.getMockDefaultBrowser(), fixtureWidgetFactory
            .getBrowser());
   }

   /**
    * Tests that we can construct an instance of the fixture widget factory with
    * non-empty browser settings and that it should have created a
    * setting-specified browser.
    */
   @Test
   public void constructWithEmptyBrowserSettingsGiveSettingSpecifiedBrowser()
   {
      FixtureWidgetFactory fixtureWidgetFactory = new DefaultFixtureWidgetFactory(MOCK_BROWSER_SETTINGS,
            mockDependencies.getMockBrowserFactory());
      assertNotNull("'fixtureWidgetFactory' should be non-null", fixtureWidgetFactory);
      assertSame("fixture widget factory's browser", mockDependencies.getMockSpecifiedBrowser(), fixtureWidgetFactory
            .getBrowser());
   }

   /**
    * Tests that the factory will return null (and not throw an exception) when
    * trying to create a fixture widget of a null widget type.
    */
   @Test
   public void createNullTypeReturnsNull()
   {
      ModifiableFixtureWidget fixtureWidget = factory.createFixtureWidget(MOCK_WIDGET_NAME, MOCK_WIDGET_NAME, null,
            MOCK_ID_BASE);
      assertNull("'fixtureWidget' should be null for a null widget type", fixtureWidget);
   }

   /**
    * Tests that the factory will return null (and not throw an exception) when
    * trying to create a fixture widget of an empty widget type.
    */
   @Test
   public void createEmptyTypeReturnsNull()
   {
      ModifiableFixtureWidget fixtureWidget = factory.createFixtureWidget(MOCK_WIDGET_NAME, MOCK_WIDGET_NAME, "",
            MOCK_ID_BASE);
      assertNull("'fixtureWidget' should be null for an empty widget type", fixtureWidget);
   }

   /**
    * Tests that the factory will return null (and not throw an exception) when
    * trying to create a fixture widget of an invalid widget type.
    */
   @Test
   public void createBadTypeReturnsNull()
   {
      ModifiableFixtureWidget fixtureWidget = factory.createFixtureWidget(MOCK_WIDGET_NAME, MOCK_WIDGET_NAME,
            "badWidgetType", MOCK_ID_BASE);
      assertNull("'fixtureWidget' should be null for an invalid widget type", fixtureWidget);
   }

   /**
    * Tests that the factory can create a {@link FixtureButton}.
    */
   @Test
   public void canCreateFixtureButton()
   {
      ModifiableFixtureWidget fixtureWidget = createAndVerifyFixtureWidget("swt:SwtButton");
      assertTrue("'fixtureWidget' should be a FixtureButton", fixtureWidget instanceof FixtureButton);
   }

   /**
    * Tests that the factory can create a {@link FixtureCheckBox}.
    */
   @Test
   public void canCreateFixtureCheckBox()
   {
      ModifiableFixtureWidget fixtureWidget = createAndVerifyFixtureWidget("swt:SwtCheckBox");
      assertTrue("'fixtureWidget' should be a FixtureCheckBox", fixtureWidget instanceof FixtureCheckBox);
   }

   /**
    * Tests that the factory can create a {@link FixtureLabel}.
    */
   @Test
   public void canCreateFixtureLabel()
   {
      ModifiableFixtureWidget fixtureWidget = createAndVerifyFixtureWidget("swt:SwtLabel");
      assertTrue("'fixtureWidget' should be a FixtureLabel", fixtureWidget instanceof FixtureLabel);
   }

   /**
    * Tests that the factory can create a {@link FixtureListBox}.
    */
   @Test
   public void canCreateFixtureListBox()
   {
      ModifiableFixtureWidget fixtureWidget = createAndVerifyFixtureWidget("swt:SwtListBox");
      assertTrue("'fixtureWidget' should be a FixtureListBox", fixtureWidget instanceof FixtureListBox);
   }

   /**
    * Tests that the factory can create a {@link FixtureRadioButton}.
    */
   @Test
   public void canCreateFixtureRadioButton()
   {
      ModifiableFixtureWidget fixtureWidget = createAndVerifyFixtureWidget("swt:SwtRadioButton");
      assertTrue("'fixtureWidget' should be a FixtureRadioButton", fixtureWidget instanceof FixtureRadioButton);
   }

   /**
    * Tests that the factory can create a {@link FixtureTab}.
    */
   @Test
   public void canCreateFixtureTab()
   {
      ModifiableFixtureWidget fixtureWidget = createAndVerifyFixtureWidget("swt:SwtTab");
      assertTrue("'fixtureWidget' should be a FixtureTab", fixtureWidget instanceof FixtureTab);
   }

   /**
    * Tests that the factory can create a {@link FixtureTable}.
    */
   @Test
   public void canCreateFixtureTable()
   {
      ModifiableFixtureWidget fixtureWidget = createAndVerifyFixtureWidget("swt:SwtTable");
      assertTrue("'fixtureWidget' should be a FixtureTable", fixtureWidget instanceof FixtureTable);
   }

   /**
    * Tests that the factory can create a {@link FixtureTextBox}.
    */
   @Test
   public void canCreateFixtureTextBox()
   {
      ModifiableFixtureWidget fixtureWidget = createAndVerifyFixtureWidget("swt:SwtTextBox");
      assertTrue("'fixtureWidget' should be a FixtureTextBox", fixtureWidget instanceof FixtureTextBox);
   }

   /**
    * Uses the {@link DefaultFixtureWidgetFactory} under test to create a
    * {@link ModifiableFixtureWidget} of the specified non-empty widget type.
    * Also does some preliminary verifications on that fixture widget. In this
    * case, the locators are the default ones (as provided in the abstract
    * class).
    * 
    * @param widgetType
    *           The non-empty SWT widget type.
    * 
    * @return The newly created modifiable fixture widget that has also been
    *         verified (including an assertion that it's non-null).
    * 
    * @throws IllegalArgumentException
    *            The widget type is null or empty (in other words, don't call
    *            this helper method when testing those cases).
    */
   private ModifiableFixtureWidget createAndVerifyFixtureWidget(final String widgetType)
   {
      return createAndVerifyFixtureWidget(widgetType, MOCK_LOCATOR, MOCK_LOCATOR);
   }

   /**
    * Uses the {@link DefaultFixtureWidgetFactory} under test to create a
    * {@link ModifiableFixtureWidget} of the specified non-empty widget type.
    * Also does some preliminary verifications on that fixture widget, including
    * checking that the locators are the expected ones specified. In this case,
    * the locators are the default ones (as provided in the abstract class).
    * 
    * @param widgetType
    *           The non-empty SWT widget type.
    * @param expectedLocator
    *           The non-null expected locator of the underlying widget.
    * @param expectedWaitTargetLocator
    *           The non-null expected wait target locator of the underlying
    *           widget.
    * 
    * @return The newly created modifiable fixture widget that has also been
    *         verified (including an assertion that it's non-null).
    * 
    * @throws IllegalArgumentException
    *            The widget type and/or locators are null or empty (in other
    *            words, don't call this helper method when testing those cases).
    */
   private ModifiableFixtureWidget createAndVerifyFixtureWidget(final String widgetType, final String expectedLocator,
         final String expectedWaitTargetLocator)
   {
      if ((null == widgetType) || widgetType.isEmpty())
         throw new IllegalArgumentException("'widgetType' must be non-empty.");
      if ((null == expectedLocator) || expectedLocator.isEmpty())
         throw new IllegalArgumentException("'expectedLocator' must be non-empty.");
      if ((null == expectedWaitTargetLocator) || expectedWaitTargetLocator.isEmpty())
         throw new IllegalArgumentException("'expectedWaitTargetLocator' must be non-empty.");

      ModifiableFixtureWidget modifiableFixtureWidget = factory.createFixtureWidget(MOCK_WIDGET_NAME, MOCK_WIDGET_ID,
            widgetType, MOCK_ID_BASE);
      assertNotNull("'modifiableFixtureWidget' should be non-null for type '" + widgetType + "'",
            modifiableFixtureWidget);

      assertTrue("'modifiableFixtureWidget' should be an AbstractFixtureWidget",
            modifiableFixtureWidget instanceof AbstractFixtureWidget);

      AbstractFixtureWidget abstractFixtureWidget = (AbstractFixtureWidget)modifiableFixtureWidget;
      assertNotNull("underlying automation widget should be non-null", abstractFixtureWidget.getAutomationWidget());

      String friendlyWidgetType = abstractFixtureWidget.getFriendlyWidgetType();
      assertNotNull("'friendlyWidgetType' should be non-null", friendlyWidgetType);
      assertFalse("'friendlyWidgetType' should be non-empty", friendlyWidgetType.isEmpty());

      assertEquals("getLocator", expectedLocator, abstractFixtureWidget.getLocator());
      assertEquals("getWaitTargetLocator", expectedWaitTargetLocator, abstractFixtureWidget.getWaitTargetLocator());
      assertEquals("getWidgetName", MOCK_WIDGET_NAME, abstractFixtureWidget.getWidgetName());

      return modifiableFixtureWidget;
   }
}
