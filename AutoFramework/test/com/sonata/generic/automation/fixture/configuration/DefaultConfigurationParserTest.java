/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.fixture.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import com.sonata.generic.automation.browser.TestMode;
import com.sonata.generic.automation.fixture.configuration.DefaultConfigurationParser;
import com.sonata.generic.automation.fixture.configuration.FixtureProperties;
import com.sonata.generic.automation.fixture.mock.MockUtils;
import com.sonata.generic.automation.fixture.widget.FixtureWidget;

/**
 * Unit tests for {@link DefaultConfigurationParser}.
 */
public class DefaultConfigurationParserTest
{
   private DefaultConfigurationParserMockDependencies mockDependencies;
   private DefaultConfigurationParser                 parser;

   /**
    * Setup that happens before each test.
    */
   @Before
   public void setUp()
   {
      mockDependencies = new DefaultConfigurationParserMockDependencies();
      assertNotNull("'mockDependencies' should be non-null at the end of setup", mockDependencies);
      mockDependencies.replayMocks();

      parser = new DefaultConfigurationParser(mockDependencies.getMockWidgetFactory());
      assertNotNull("'parser' should be non-null at the end of setup", parser);
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

      parser = null;
      assertNull("'parser' should be null at the end of teardown", parser);
   }

   /**
    * Tests that we cannot construct a parser with a null fixture widget
    * factory.
    */
   @Test(expected = IllegalArgumentException.class)
   public void testCannotConstructParserWithNullFactory()
   {
      new DefaultConfigurationParser(null);
      fail("Should have thrown an IllegalArgumentException");
   }

   /**
    * Tests that we fail to parse the contents at a null configuration path.
    */
   @Test(expected = IllegalArgumentException.class)
   public void testParseNullConfigPathFails()
   {
      parser.parse((String)null);
      fail("Should have thrown an IllegalArgumentException");
   }

   /**
    * Tests that we fail to parse the contents at an empty configuration path.
    */
   @Test(expected = IllegalArgumentException.class)
   public void testParseEmptyConfigPathFails()
   {
      parser.parse("");
      fail("Should have thrown an IllegalArgumentException");
   }

   /**
    * Tests that we fail to parse the contents at an invalid configuration path.
    */
   @Test(expected = IllegalArgumentException.class)
   public void testParseInvalidConfigPathFails()
   {
      parser.parse("badConfigPath");
      fail("Should have thrown an IllegalArgumentException");
   }

   /**
    * Tests that we fail to parse a null file.
    */
   @Test(expected = IllegalArgumentException.class)
   public void testParseNullFileFails()
   {
      parser.parse((File)null);
      fail("Should have thrown an IllegalArgumentException");
   }

   /**
    * Tests that we fail to parse a null DOM.
    */
   @Test(expected = IllegalArgumentException.class)
   public void testParseNullDOMFails()
   {
      parser.parse((Document)null);
      fail("Should have thrown an IllegalArgumentException");
   }

   /**
    * Tests that we fail to parse an empty DOM.
    */
   @Test(expected = IllegalArgumentException.class)
   public void testParseEmptyDOMFails()
   {
      parser.parse(DefaultConfigurationParserMockDOMs.createEmptyDOM());
      fail("Should have thrown an IllegalArgumentException");
   }

   /**
    * Tests that we fail to parse an DOM whose root element is not the "ui"
    * element.
    */
   @Test(expected = IllegalArgumentException.class)
   public void testParseDOMNonUIRootElemFails()
   {
      parser.parse(DefaultConfigurationParserMockDOMs.createDOMNonUIRootElem());
      fail("Should have thrown an IllegalArgumentException");
   }

   /**
    * Tests that we fail to parse an DOM with a "ui" that has an empty "name"
    * attribute.
    */
   @Test(expected = IllegalArgumentException.class)
   public void testParseDOMUIElemEmptyNameFails()
   {
      parser.parse(DefaultConfigurationParserMockDOMs.createDOMUIElemEmptyName());
      fail("Should have thrown an IllegalArgumentException");
   }

   /**
    * Tests that we fail to parse an DOM with a "ui" that has no "name"
    * attribute.
    */
   @Test(expected = IllegalArgumentException.class)
   public void testParseDOMUIElemNoNameFails()
   {
      parser.parse(DefaultConfigurationParserMockDOMs.createDOMUIElemNoName());
      fail("Should have thrown an IllegalArgumentException");
   }

   /**
    * Tests that we fail to parse an DOM with a "ui" that has an empty
    * "application" attribute.
    */
   @Test(expected = IllegalArgumentException.class)
   public void testParseDOMUIElemEmptyApplicationFails()
   {
      parser.parse(DefaultConfigurationParserMockDOMs.createDOMUIElemEmptyApplication());
      fail("Should have thrown an IllegalArgumentException");
   }

   /**
    * Tests that we fail to parse an DOM with a "ui" that has no "application"
    * attribute.
    */
   @Test(expected = IllegalArgumentException.class)
   public void testParseDOMUIElemNoApplicationFails()
   {
      parser.parse(DefaultConfigurationParserMockDOMs.createDOMUIElemNoApplication());
      fail("Should have thrown an IllegalArgumentException");
   }

   /**
    * Tests that we fail to parse an DOM with only up to a valid "ui" element,
    * but no more than that.
    */
   @Test(expected = IllegalArgumentException.class)
   public void testParseDOMToUIElemOnlyFails()
   {
      parser.parse(DefaultConfigurationParserMockDOMs.createDOMToUIElemOnly());
      fail("Should have thrown an IllegalArgumentException");
   }

   /**
    * Tests that we fail to parse an DOM with a valid "ui" element but with a
    * popup "form" element that has an empty "name" attribute.
    */
   @Test(expected = IllegalArgumentException.class)
   public void testParseDOMFormElemPopupEmptyNameFails()
   {
      parser.parse(DefaultConfigurationParserMockDOMs.createDOMFormElemPopupEmptyName());
      fail("Should have thrown an IllegalArgumentException");
   }

   /**
    * Tests that we fail to parse an DOM with a valid "ui" element but with a
    * popup "form" element that has no "name" attribute.
    */
   @Test(expected = IllegalArgumentException.class)
   public void testParseDOMFormElemPopupNoNameFails()
   {
      parser.parse(DefaultConfigurationParserMockDOMs.createDOMFormElemPopupNoName());
      fail("Should have thrown an IllegalArgumentException");
   }

   /**
    * Tests that we fail to parse an DOM with a valid "ui" element but with a
    * (main) "form" element that has an empty "definitionID" attribute.
    */
   @Test(expected = IllegalArgumentException.class)
   public void testParseDOMFormElemEmptyDefinitionIDFails()
   {
      parser.parse(DefaultConfigurationParserMockDOMs.createDOMFormElemEmptyDefinitionID());
      fail("Should have thrown an IllegalArgumentException");
   }

   /**
    * Tests that we fail to parse an DOM with a valid "ui" element but with a
    * (main) "form" element that has no "definitionID" attribute.
    */
   @Test(expected = IllegalArgumentException.class)
   public void testParseDOMFormElemNoDefinitionIDFails()
   {
      parser.parse(DefaultConfigurationParserMockDOMs.createDOMFormElemNoDefinitionID());
      fail("Should have thrown an IllegalArgumentException");
   }

   /**
    * Tests that we fail to parse an DOM with a valid "ui" element but with a
    * (main) "form" element that has an empty "existenceValidationWidget"
    * attribute.
    */
   @Test(expected = IllegalArgumentException.class)
   public void testParseDOMFormElemEmptyExistenceValidationWidgetFails()
   {
      parser.parse(DefaultConfigurationParserMockDOMs.createDOMFormElemEmptyExistenceValidationWidget());
      fail("Should have thrown an IllegalArgumentException");
   }

   /**
    * Tests that we fail to parse an DOM with a valid "ui" element but with a
    * (main) "form" element that has no "existenceValidationWidget" attribute.
    */
   @Test(expected = IllegalArgumentException.class)
   public void testParseDOMFormElemNoExistenceValidationWidgetFails()
   {
      parser.parse(DefaultConfigurationParserMockDOMs.createDOMFormElemNoExistenceValidationWidget());
      fail("Should have thrown an IllegalArgumentException");
   }

   /**
    * Tests that we fail to parse an DOM with a valid "ui" element but with a
    * (main) "form" element that has an empty "type" attribute.
    */
   @Test(expected = IllegalArgumentException.class)
   public void testParseDOMFormElemEmptyTypeFails()
   {
      parser.parse(DefaultConfigurationParserMockDOMs.createDOMFormElemEmptyType());
      fail("Should have thrown an IllegalArgumentException");
   }

   /**
    * Tests that we fail to parse an DOM with a valid "ui" element but with a
    * (main) "form" element that has no "type" attribute.
    */
   @Test(expected = IllegalArgumentException.class)
   public void testParseDOMFormElemNoTypeFails()
   {
      parser.parse(DefaultConfigurationParserMockDOMs.createDOMFormElemNoType());
      fail("Should have thrown an IllegalArgumentException");
   }

   /**
    * Tests that we fail to parse an DOM with a valid "ui" element but with a
    * (main) "form" element that has an invalid "type" attribute.
    */
   @Test(expected = IllegalArgumentException.class)
   public void testParseDOMFormElemInvalidTypeFails()
   {
      parser.parse(DefaultConfigurationParserMockDOMs.createDOMFormElemInvalidType());
      fail("Should have thrown an IllegalArgumentException");
   }

   /**
    * Tests that we fail to parse an DOM with a valid "ui" element and a valid
    * main "form" element under it (a valid main form does not need a name), but
    * no more than that.
    */
   @Test(expected = IllegalArgumentException.class)
   public void testParseDOMToFormElemOnlyFails()
   {
      parser.parse(DefaultConfigurationParserMockDOMs.createDOMToFormElemOnly());
      fail("Should have thrown an IllegalArgumentException");
   }

   /**
    * Tests that we fail to parse an DOM up to a valid "form" element but with a
    * "widget" element that has an empty "name" attribute.
    */
   @Test(expected = IllegalArgumentException.class)
   public void testParseDOMWidgetElemEmptyNameFails()
   {
      parser.parse(DefaultConfigurationParserMockDOMs.createDOMWidgetElemEmptyName());
      fail("Should have thrown an IllegalArgumentException");
   }

   /**
    * Tests that we fail to parse an DOM up to a valid "form" element but with a
    * "widget" element that has no "name" attribute.
    */
   @Test(expected = IllegalArgumentException.class)
   public void testParseDOMWidgetElemNoNameFails()
   {
      parser.parse(DefaultConfigurationParserMockDOMs.createDOMWidgetElemNoName());
      fail("Should have thrown an IllegalArgumentException");
   }

   /**
    * Tests that we fail to parse an DOM up to a valid "form" element but with
    * two "widget" element that have the same "name" attribute.
    */
   @Test(expected = IllegalArgumentException.class)
   public void testParseDOMWidgetElemsSameNameFails()
   {
      parser.parse(DefaultConfigurationParserMockDOMs.createDOMWidgetElemsSameName());
      fail("Should have thrown an IllegalArgumentException");
   }

   /**
    * Tests that we fail to parse an DOM up to a valid "form" element but with a
    * "widget" element that has an empty "id" attribute.
    */
   @Test(expected = IllegalArgumentException.class)
   public void testParseDOMWidgetElemEmptyIDFails()
   {
      parser.parse(DefaultConfigurationParserMockDOMs.createDOMWidgetElemEmptyID());
      fail("Should have thrown an IllegalArgumentException");
   }

   /**
    * Tests that we fail to parse an DOM up to a valid "form" element but with a
    * "widget" element that has no "id" attribute.
    */
   @Test(expected = IllegalArgumentException.class)
   public void testParseDOMWidgetElemNoIDFails()
   {
      parser.parse(DefaultConfigurationParserMockDOMs.createDOMWidgetElemNoID());
      fail("Should have thrown an IllegalArgumentException");
   }

   /**
    * Tests that we fail to parse an DOM up to a valid "form" element but with
    * only one "widget" element that has an empty "type" attribute.
    */
   @Test(expected = IllegalArgumentException.class)
   public void testParseDOMWidgetElemEmptyTypeFails()
   {
      parser.parse(DefaultConfigurationParserMockDOMs.createDOMWidgetElemEmptyType());
      fail("Should have thrown an IllegalArgumentException");
   }

   /**
    * Tests that we fail to parse an DOM up to a valid "form" element but with
    * only one "widget" element that has no "type" attribute.
    */
   @Test(expected = IllegalArgumentException.class)
   public void testParseDOMWidgetElemNoTypeFails()
   {
      parser.parse(DefaultConfigurationParserMockDOMs.createDOMWidgetElemNoType());
      fail("Should have thrown an IllegalArgumentException");
   }

   /**
    * Tests that we fail to parse an DOM up to a valid "form" element but with
    * only one "widget" element that has an invalid "type" attribute.
    */
   @Test(expected = IllegalArgumentException.class)
   public void testParseDOMWidgetElemInvalidTypeFails()
   {
      parser.parse(DefaultConfigurationParserMockDOMs.createDOMWidgetElemInvalidType());
      fail("Should have thrown an IllegalArgumentException");
   }

   /**
    * Tests that we fail to parse a DOM up to a valid main "form" element with
    * one "widget" element but it is not the existence validation widget.
    */
   @Test(expected = IllegalArgumentException.class)
   public void testParseDOMToOneWidgetNoExistenceValidationFails()
   {
      parser.parse(DefaultConfigurationParserMockDOMs.createDOMToOneWidgetNoExistenceValidation());
      fail("Should have thrown an IllegalArgumentException");
   }

   /**
    * Tests that we can parse a DOM that is valid and includes a main "form"
    * element with one "widget" element that is the existence validation widget.
    */
   @Test
   public void testParseDOMToExistenceValidationWidgetOnlySucceeds()
   {
      FixtureProperties properties = parser.parse(DefaultConfigurationParserMockDOMs
            .createDOMToExistenceValidationWidgetOnly());
      verifyCommonPropertiesAssertions(properties);
   }

   /**
    * Tests that we can parse a DOM that is valid and includes a main "form"
    * element with one "widget" element that is the existence validation widget
    * as well as an element with a different tag name (which gets ignored).
    */
   @Test
   public void testParseDOMToExistenceValidationWidgetPlusIgnoredElemSucceeds()
   {
      FixtureProperties properties = parser.parse(DefaultConfigurationParserMockDOMs
            .createDOMToExistenceValidationWidgetPlusIgnoredElem());
      verifyCommonPropertiesAssertions(properties);
   }

   /**
    * Tests that we can parse a DOM that is valid and includes a main "form"
    * element with a parent "widget" element (the existence validation widget)
    * of a valid type and a child "widget" element of an invalid type (which
    * gets ignored).
    */
   @Test
   public void testParseDOMParentWidgetValidTypeChildWidgetBadTypeSucceeds()
   {
      FixtureProperties properties = parser.parse(DefaultConfigurationParserMockDOMs
            .createDOMParentWidgetValidTypeChildWidgetBadType());
      verifyCommonPropertiesAssertions(properties);

      FixtureWidget parentWidget = properties.getFixtureWidget(null,
            DefaultConfigurationParserMockConstants.MOCK_EXISTENCE_VALIDATION_WIDGET);
      assertNotNull("parentWidget should be non-null", parentWidget);

      FixtureWidget childWidget = properties.getFixtureWidget(null,
            DefaultConfigurationParserMockConstants.MOCK_WIDGET_NAME1);
      assertNull("childWidget should be null because its element had an invalid type", childWidget);

      // TODO: We may want to improve the mock object so we can do some sort
      // of getChildren/getParent tests to see that addChild/setParent was
      // called.  However, a code coverage report will show that they are
      // indeed called.
   }

   /**
    * Tests that we can parse a DOM that is valid and includes a main "form"
    * element with a parent "widget" element of an invalid type (which gets
    * ignored) and a child "widget" element (the existence validation widget) of
    * a valid type (which gets placed at the parent level).
    */
   @Test
   public void testParseDOMParentWidgetBadTypeChildWidgetValidTypeSucceeds()
   {
      FixtureProperties properties = parser.parse(DefaultConfigurationParserMockDOMs
            .createDOMParentWidgetBadTypeChildWidgetValidType());
      verifyCommonPropertiesAssertions(properties);

      FixtureWidget parentWidget = properties.getFixtureWidget(null,
            DefaultConfigurationParserMockConstants.MOCK_WIDGET_NAME1);
      assertNull("parentWidget should be null because its element had an invalid type", parentWidget);

      FixtureWidget childWidget = properties.getFixtureWidget(null,
            DefaultConfigurationParserMockConstants.MOCK_EXISTENCE_VALIDATION_WIDGET);
      assertNotNull("childWidget should be non-null", childWidget);
   }

   /**
    * Tests that we can parse a DOM that is valid and includes a main "form"
    * element with a parent "widget" element and children "widget" elements, all
    * of valid types.
    */
   @Test
   public void testParseDOMParentChildrenWidgetsValidTypesSucceeds()
   {
      FixtureProperties properties = parser.parse(DefaultConfigurationParserMockDOMs
            .createDOMParentChildrenWidgetsValidTypes());
      verifyCommonPropertiesAssertions(properties);

      FixtureWidget parentWidget = properties.getFixtureWidget(null,
            DefaultConfigurationParserMockConstants.MOCK_WIDGET_NAME1);
      assertNotNull("parentWidget should be non-null", parentWidget);

      FixtureWidget childWidget1 = properties.getFixtureWidget(null,
            DefaultConfigurationParserMockConstants.MOCK_WIDGET_NAME2);
      assertNotNull("childWidget1 should be non-null", childWidget1);

      FixtureWidget childWidget2 = properties.getFixtureWidget(null,
            DefaultConfigurationParserMockConstants.MOCK_EXISTENCE_VALIDATION_WIDGET);
      assertNotNull("childWidget2 should be non-null", childWidget2);
   }

   /**
    * Tests that we fail to parse a DOM that includes a popup "form" element and
    * the existence widget for that form, but is missing a main "form" element.
    */
   @Test(expected = IllegalArgumentException.class)
   public void testParseDOMPopupFormOnlyFails()
   {
      parser.parse(DefaultConfigurationParserMockDOMs.createDOMPopupFormOnly());
      fail("Should have thrown an IllegalArgumentException");
   }

   /**
    * Tests that we fail to parse a DOM that includes a valid main "form"
    * element and a popup "form" element whose child widgets do not include an
    * existence validation widget.
    */
   @Test(expected = IllegalArgumentException.class)
   public void testParseDOMMainOKPopupNoExistenceValidationFails()
   {
      parser.parse(DefaultConfigurationParserMockDOMs.createDOMMainOKPopupNoExistenceValidation());
      fail("Should have thrown an IllegalArgumentException");
   }

   /**
    * Tests that we can parse a DOM that is valid and includes a main "form"
    * element followed by two popup "form" elements, where all three forms have
    * existence validation "widget" elements within them.
    */
   @Test
   public void testParseDOMMainPopup1Popup2Succeeds()
   {
      FixtureProperties properties = parser.parse(DefaultConfigurationParserMockDOMs.createDOMMainPopup1Popup2());
      verifyCommonPropertiesAssertions(properties);
      verifyThreeFormsPropertiesAssertions(properties);
   }

   /**
    * Tests that we can parse a DOM that is valid and includes a popup 1 "form"
    * element, followed by the main "form" element, and followed by a popup 2
    * "form" element, where all three forms have existence validation "widget"
    * elements within them.
    */
   @Test
   public void testParseDOMPopup1MainPopup2Succeeds()
   {
      FixtureProperties properties = parser.parse(DefaultConfigurationParserMockDOMs.createDOMPopup1MainPopup2());
      verifyCommonPropertiesAssertions(properties);
      verifyThreeFormsPropertiesAssertions(properties);
   }

   /**
    * Tests that we can parse a DOM that is valid and includes two popup "form"
    * elements followed by the main "form" element, where all three forms have
    * existence validation "widget" elements within them.
    */
   @Test
   public void testParseDOMPopup1Popup2MainSucceeds()
   {
      FixtureProperties properties = parser.parse(DefaultConfigurationParserMockDOMs.createDOMPopup1Popup2Main());
      verifyCommonPropertiesAssertions(properties);
      verifyThreeFormsPropertiesAssertions(properties);
   }

   /**
    * Verifies common assertions on the {@link FixtureProperties} object
    * generated by parsing the DOMs that have a main form and two popup forms.
    * The DOMs have the same set of elements in them, but the order in which the
    * forms are appended is different. However, we can apply the same assertions
    * on the fixture properties object.
    * 
    * @param properties
    *           The {@link FixtureProperties} object generated by the parser.
    */
   private void verifyThreeFormsPropertiesAssertions(FixtureProperties properties)
   {
      FixtureWidget mainWidget = properties.getFixtureWidget(null,
            DefaultConfigurationParserMockConstants.MOCK_EXISTENCE_VALIDATION_WIDGET);
      assertNotNull("mainWidget should be non-null", mainWidget);
      FixtureWidget notInMainWidget = properties.getFixtureWidget(null,
            DefaultConfigurationParserMockConstants.MOCK_WIDGET_NAME2);
      assertNull("notInMainWidget should be null as there is no widget by this name on the main form", notInMainWidget);

      FixtureWidget popup1Widget = properties.getFixtureWidget(
            DefaultConfigurationParserMockConstants.MOCK_POPUP1_NAME,
            DefaultConfigurationParserMockConstants.MOCK_EXISTENCE_VALIDATION_WIDGET);
      assertNotNull("popup1Widget should be non-null", popup1Widget);
      FixtureWidget notInPopup1Widget = properties.getFixtureWidget(
            DefaultConfigurationParserMockConstants.MOCK_POPUP1_NAME,
            DefaultConfigurationParserMockConstants.MOCK_WIDGET_NAME2);
      assertNull("notInPopup1Widget should be null as there is no widget by this name on popup 1", notInPopup1Widget);

      FixtureWidget popup2Widget = properties.getFixtureWidget(
            DefaultConfigurationParserMockConstants.MOCK_POPUP2_NAME,
            DefaultConfigurationParserMockConstants.MOCK_WIDGET_NAME2);
      assertNotNull("popup2Widget should be non-null", popup2Widget);
      FixtureWidget notInPopup2Widget = properties.getFixtureWidget(
            DefaultConfigurationParserMockConstants.MOCK_POPUP2_NAME,
            DefaultConfigurationParserMockConstants.MOCK_EXISTENCE_VALIDATION_WIDGET);
      assertNull("notInPopup2Widget should be null as there is no widget by this name on popup 2", notInPopup2Widget);
   }

   /**
    * Verifies common assertions on the {@link FixtureProperties} object
    * generated by the parser.
    * 
    * @param properties
    *           The {@link FixtureProperties} object generated by the parser.
    */
   private void verifyCommonPropertiesAssertions(FixtureProperties properties)
   {
      assertNotNull("Fixture properties should be non-null", properties);

      assertEquals("Application name", DefaultConfigurationParserMockConstants.MOCK_APPLICATION, properties
            .getApplication());
      assertSame("Browser", mockDependencies.getMockBrowser(), properties.getBrowser());

      FixtureWidget expectedExistenceValidationWidget = properties.getFixtureWidget(null,
            DefaultConfigurationParserMockConstants.MOCK_EXISTENCE_VALIDATION_WIDGET);
      assertNotNull("Actual existence validation widget should be non-null", expectedExistenceValidationWidget);

      FixtureWidget actualExistenceValidationWidget = properties.getExistenceValidationWidget(null);
      assertNotNull("Actual existence validation widget should be non-null", actualExistenceValidationWidget);

      assertSame("Existence validation widget", expectedExistenceValidationWidget, actualExistenceValidationWidget);

      // TODO: We may need to redesign the mock widget if we want a more
      // intelligent test for the sign-in validation element (or perhaps that
      // test may be better covered when testing other classes such as the
      // default fixture properties or the fixture widgets).
      assertEquals("Sign-in validation element locator", MockUtils.MOCK_WAIT_TARGET_LOCATOR, properties
            .getSignInValidationElement());

      assertEquals("UI name", DefaultConfigurationParserMockConstants.MOCK_UI_NAME, properties.getUIName());
      assertEquals("Ant mode URL", DefaultConfigurationParserMockConstants.MOCK_URL_ANT, properties
            .getURL(TestMode.ANT));
      assertEquals("Deployed mode URL", DefaultConfigurationParserMockConstants.MOCK_URL_DEPLOYED, properties
            .getURL(TestMode.DEPLOYED));
   }
}
