/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.fixture.configuration;

import com.sonata.generic.automation.fixture.configuration.DefaultConfigurationParser;

/**
 * Constants for the mocks (such as valid and invalid values) for
 * {@link DefaultConfigurationParser} tests.
 */
final class DefaultConfigurationParserMockConstants
{
   /**
    * Don't create instances.
    */
   private DefaultConfigurationParserMockConstants()
   {
   }

   // "Invalid" values for the mock objects
   public static final String BAD_WIDGET_TYPE                  = "badWidgetType";

   // "Proper" values for the mock objects
   public static final String MOCK_APPLICATION                 = "mockApplication";
   public static final String MOCK_DEFINITION_ID               = "mockDefinitionID";
   public static final String MOCK_EXISTENCE_VALIDATION_WIDGET = "mockExistenceValidationWidget";
   public static final String MOCK_POPUP1_NAME                 = "mockPopup1";
   public static final String MOCK_POPUP2_NAME                 = "mockPopup2";
   public static final String MOCK_UI_NAME                     = "mockUIName";
   public static final String MOCK_URL_ANT                     = "/mockApplication/mockUIName.html";
   public static final String MOCK_URL_DEPLOYED                = "/Sage300ERP/mockApplication/mockUIName.html";
   public static final String MOCK_WIDGET_ID1                  = "mockWidgetID1";
   public static final String MOCK_WIDGET_ID2                  = "mockWidgetID2";
   public static final String MOCK_WIDGET_ID_EXISTENCE         = "mockWidgetIDExistence";
   public static final String MOCK_WIDGET_NAME1                = "mockWidgetName1";
   public static final String MOCK_WIDGET_NAME2                = "mockWidgetName2";
   public static final String MOCK_WIDGET_TYPE                 = "mockWidgetType";
}
