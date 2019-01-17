/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.fixture.mock;

import org.easymock.EasyMock;

import com.sonata.generic.automation.fixture.widget.ModifiableFixtureWidget;

/**
 * Utility class for mock object support.
 */
public final class MockUtils
{
   /**
    * Don't create instances.
    */
   private MockUtils()
   {
   }

   public static final String MOCK_WAIT_TARGET_LOCATOR = "mockWaitTargetLocator";

   /**
    * Creates a mock modifiable fixture widget that gives default answers to all
    * method calls (null, 0, or false, unless otherwise specified) and that is
    * set to replay mode.
    * 
    * @return The newly created mock modifiable fixture widget that gives
    *         default answers to all method calls and that is set to replay
    *         mode.
    */
   public static ModifiableFixtureWidget createReadyToUseMockFixtureWidget()
   {
      ModifiableFixtureWidget fixtureWidget = EasyMock.createNiceMock(ModifiableFixtureWidget.class);
      EasyMock.expect(fixtureWidget.getWaitTargetLocator()).andReturn(MOCK_WAIT_TARGET_LOCATOR).anyTimes();
      EasyMock.replay(fixtureWidget);
      return fixtureWidget;
   }
}
