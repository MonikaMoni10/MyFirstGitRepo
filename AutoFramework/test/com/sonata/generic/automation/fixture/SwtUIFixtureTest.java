/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.fixture;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sonata.generic.automation.fixture.GenericWebFixture;
import com.sonata.generic.automation.fixture.configuration.FixtureProperties;
import com.sonata.generic.automation.fixture.widget.FixtureWidget;

/**
 * Smoke tests for {@link SwtUIFixture}.
 */
public class SwtUIFixtureTest
{
   private SwtUIFixtureMockDependencies mockDependencies;

   /**
    * Setup that happens before each test.
    */
   @Before
   public void setUp()
   {
      mockDependencies = new SwtUIFixtureMockDependencies();
      assertNotNull("'mockDependencies' should be non-null at the end of setup", mockDependencies);
      mockDependencies.replayMocks();
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
   }

   /**
    * Tests that we can do "no-op" switches from the main UI to the main UI.
    */
   @Test
   public void canSwitchFromMainUIToMainUI()
   {
      GenericWebFixture fixture = new GenericWebFixture("some path", null, mockDependencies.getMockParserFactory(),
            mockDependencies.getMockBrowserTiming());
      assertNotNull("Fixture should be non-null", fixture);

      fixture.switchFormContext();
      fixture.switchFormContext("");
      fixture.switchFormContext(null);
   }

   /**
    * Tests that we can do "no-op" switches from a popup to the same popup.
    */
   @Test
   public void canSwitchFromPopupToSamePopup()
   {
	   GenericWebFixture fixture = new GenericWebFixture("some path", null, mockDependencies.getMockParserFactory(),
            mockDependencies.getMockBrowserTiming());
      assertNotNull("Fixture should be non-null", fixture);

      fixture.switchFormContext("mockPopupForm1");
      fixture.switchFormContext();
   }

   /**
    * Tests that we can switch between various forms (popup and main).
    */
   @Test
   public void canSwitchToDifferentPopupsAndMain()
   {
	   GenericWebFixture fixture = new GenericWebFixture("some path", null, mockDependencies.getMockParserFactory(),
            mockDependencies.getMockBrowserTiming());
      assertNotNull("Fixture should be non-null", fixture);

      fixture.switchFormContext("mockPopupForm1");
      fixture.switchFormContext();
      fixture.switchFormContext("mockPopupForm2");
      fixture.switchFormContext("mockPopupForm1");
   }

   /**
    * Tests that we cannot switch to a non-existent popup form.
    */
   @Test(expected = IllegalArgumentException.class)
   public void cannotSwitchToNonExistentPopup()
   {
	   GenericWebFixture fixture = new GenericWebFixture("some path", null, mockDependencies.getMockParserFactory(),
            mockDependencies.getMockBrowserTiming());
      assertNotNull("Fixture should be non-null", fixture);

      fixture.switchFormContext("badPopup");
      fail("Should have gotten an IllegalArgumentException for a non-existent form");
   }

   /**
    * Tests that we can clear widgets that exist on the form whose context we're
    * in.
    */
   @Test
   public void canClearExistingWidgets()
   {
	   GenericWebFixture fixture = new GenericWebFixture("some path", null, mockDependencies.getMockParserFactory(),
            mockDependencies.getMockBrowserTiming());
      assertNotNull("Fixture should be non-null", fixture);

      FixtureProperties mockProperties = mockDependencies.getMockFixtureProperties();
      FixtureWidget mockMainWidget1 = mockProperties.getFixtureWidget("", "mockMainWidget1");
      FixtureWidget mockPopup1Widget1 = mockProperties.getFixtureWidget("mockPopupForm1", "mockPopup1Widget1");

      assertEquals("mockMainWidget1.clear", mockMainWidget1.clear(), fixture.clear("mockMainWidget1"));
      fixture.switchFormContext("mockPopupForm1");
      assertEquals("mockPopup1Widget1.clear", mockPopup1Widget1.clear(), fixture.clear("mockPopup1Widget1"));
   }

   /**
    * Tests that we cannnot clear widgets that don't exist on the form whose
    * context we're in.
    */
   @Test
   public void cannotClearNonExistentWidgets()
   {
	   GenericWebFixture fixture = new GenericWebFixture("some path", null, mockDependencies.getMockParserFactory(),
            mockDependencies.getMockBrowserTiming());
      assertNotNull("Fixture should be non-null", fixture);

      try
      {
         fixture.clear("mockPopup1Widget1");
         fail("Should have gotten IllegalArgumentException for popup widget in main form context");
      }
      catch (IllegalArgumentException e)
      {
         // Ignore - we expect this exception.
      }

      fixture.switchFormContext("mockPopupForm1");

      try
      {
         fixture.clear("mockMainWidget1");
         fail("Should have gotten IllegalArgumentException for main widget in popup form context");
      }
      catch (IllegalArgumentException e)
      {
         // Ignore - we expect this exception.
      }
   }
}
