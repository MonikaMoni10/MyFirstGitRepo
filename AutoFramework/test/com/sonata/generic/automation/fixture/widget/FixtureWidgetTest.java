/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.fixture.widget;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sonata.generic.automation.fixture.widget.FixtureButton;
import com.sonata.generic.automation.fixture.widget.FixtureLabel;
import com.sonata.generic.automation.fixture.widget.FixturePasswordTextBox;
import com.sonata.generic.automation.fixture.widget.FixtureTable;
import com.sonata.generic.automation.fixture.widget.FixtureTextBox;
import com.sonata.generic.automation.fixture.widget.FixtureWidget;
import com.sonata.generic.automation.fixture.widget.FixtureWidgetCreator;
import com.sonata.generic.automation.fixture.widget.ModifiableFixtureWidget;

/**
 * Smoke tests for various kinds of {@link FixtureWidget} objects.
 */
public class FixtureWidgetTest
{
   private FixtureWidgetMockDependencies mockDependencies;

   // messages
   private static String                 badLocator = "Bad locator!";
   private static String                 noMatch    = "The returned value does not match!";

   private static String                 mockUIName = "mockUIName";

   /**
    * Setup that happens before each test.
    */
   @Before
   public void setUp()
   {
      mockDependencies = new FixtureWidgetMockDependencies();
      assertNotNull("'mockDependencies' should be non-null at the end of setup", mockDependencies);
      mockDependencies.replayMocks();
   }

   /**
    * Tear down that happens after each test.
    */
   @After
   public void tearDown()
   {
      // mockDependencies.verifyMocks();
      mockDependencies = null;
      assertNull("'mockDependencies' should be null at the end of teardown", mockDependencies);
   }

   /**
    * Tests a button actions.
    */
   @Test(expected = IllegalArgumentException.class)
   public void buttonTests()
   {
      final String mockButtonID = "mockButtonID";

      assertEquals("FixtureButton.getSwtWidgetType", "swt:SwtButton", FixtureButton.getSwtWidgetType());

      FixtureWidgetCreator creator = FixtureButton.getFixtureWidgetCreator();
      assertNotNull("FixtureButton's creator should be non-null", creator);

      ModifiableFixtureWidget mfwButton = creator.createFixtureWidget(mockButtonID, mockButtonID, mockUIName,
            mockDependencies.getMockBrowser());
      assertNotNull("'mfwButton' should be non-null", mfwButton);
      assertTrue("'mfwButton' should be a FixtureButton", mfwButton instanceof FixtureButton);

      FixtureButton button = (FixtureButton)mfwButton;

      assertEquals(badLocator, button.getWaitTargetLocator(), mockUIName + mockButtonID);
      assertTrue(button.isVisible());
      assertTrue(button.click());

      // This is a failed case.
      button.type("Cannot type in a button!");
   }

   /**
    * Tests a text box's actions
    */
   @Test
   public void textBoxTests()
   {
      final String mockTextBoxID = "mockTextBoxID";

      assertEquals("FixtureTextBox.getSwtWidgetType", "swt:SwtTextBox", FixtureTextBox.getSwtWidgetType());

      FixtureWidgetCreator creator = FixtureTextBox.getFixtureWidgetCreator();
      assertNotNull("FixtureTextBox creator should be non-null", creator);

      ModifiableFixtureWidget mfwTextBox = creator.createFixtureWidget(mockTextBoxID, mockTextBoxID, mockUIName,
            mockDependencies.getMockBrowser());
      assertNotNull("'mfwTextBox' should be non-null", mfwTextBox);
      assertTrue("'mfwTextBox' should be a FixtureTextBox", mfwTextBox instanceof FixtureTextBox);

      FixtureTextBox textBox = (FixtureTextBox)mfwTextBox;

      assertEquals(badLocator, textBox.getWaitTargetLocator(), mockUIName + mockTextBoxID);
      assertTrue(textBox.isVisible());
      assertTrue(textBox.type("This is a text box."));
      assertTrue(textBox.typeWithoutTab("This is a text box, input it but do not tab out after"));
      assertEquals(noMatch, textBox.getText(), "This is a text box.");
   }

 
   /**
    * Tests a password text box's actions
    */
   @Test
   public void passwordTextBoxTests()
   {
      final String mockPasswordTextBoxID = "mockPasswordTextBoxID";

      assertEquals("FixturePasswordTextBox.getSwtWidgetType", "swt:SwtPasswordTextBox",
            FixturePasswordTextBox.getSwtWidgetType());

      FixtureWidgetCreator creator = FixturePasswordTextBox.getFixtureWidgetCreator();
      assertNotNull("FixturePasswordTextBox creator should be non-null", creator);

      ModifiableFixtureWidget mfwPasswordTextBox = creator.createFixtureWidget(mockPasswordTextBoxID,
            mockPasswordTextBoxID, mockUIName, mockDependencies.getMockBrowser());
      assertNotNull("'mfwPasswordTextBox' should be non-null", mfwPasswordTextBox);
      assertTrue("'mfwPasswordTextBox' should be a FixturePasswordTextBox",
            mfwPasswordTextBox instanceof FixturePasswordTextBox);

      FixturePasswordTextBox passwordTextBox = (FixturePasswordTextBox)mfwPasswordTextBox;

      assertEquals(badLocator, passwordTextBox.getWaitTargetLocator(), mockUIName + mockPasswordTextBoxID);
      assertTrue(passwordTextBox.isVisible());
      assertTrue(passwordTextBox.type("my password"));
      assertTrue(passwordTextBox.clear());
      assertTrue(passwordTextBox.typeWithoutClearAndWithoutTab("my password"));

      // The password text box should throw an illegal argument exception on
      // an attempt at getText.
      try
      {
         passwordTextBox.getText();
         fail("Should have thrown an IllegalArgumentException for passwordTextBox.getText");
      }
      catch (IllegalArgumentException e)
      {
         // This is exactly what we expect.
      }

      // The password text box should throw an illegal argument exception on
      // an attempt at clearAndValidate.
      try
      {
         passwordTextBox.clearAndValidate("ignored default value");
         fail("Should have thrown an IllegalArgumentException for passwordTextBox.clearAndValidate");
      }
      catch (IllegalArgumentException e)
      {
         // This is exactly what we expect.
      }
   }


   /**
    * Tests a label's actions
    */
   @Test
   public void labelTests()
   {
      final String mockLabelID = "mockLabelID";

      assertEquals("FixtureLabel.getSwtWidgetType", "swt:SwtLabel", FixtureLabel.getSwtWidgetType());

      FixtureWidgetCreator creator = FixtureLabel.getFixtureWidgetCreator();
      assertNotNull("FixtureLabel creator should be non-null", creator);

      ModifiableFixtureWidget mfwLabel = creator.createFixtureWidget(mockLabelID, mockLabelID, mockUIName,
            mockDependencies.getMockBrowser());
      assertNotNull("'mfwLabel' should be non-null", mfwLabel);
      assertTrue("'mfwLabel' should be a FixtureLabel", mfwLabel instanceof FixtureLabel);

      FixtureLabel label = (FixtureLabel)mfwLabel;

      assertEquals(badLocator, label.getWaitTargetLocator(), mockUIName + mockLabelID);
      assertTrue(label.isVisible());
      assertEquals(noMatch, label.getText(), "This is a label!");
   }


   /**
    * Tests a Table's actions
    */
   @Test
   public void tableTests()
   {
      final String mockTableID = "mockTableID";

      assertEquals("FixtureTable.getSwtWidgetType", "swt:SwtTable", FixtureTable.getSwtWidgetType());

      FixtureWidgetCreator creator = FixtureTable.getFixtureWidgetCreator();
      assertNotNull("FixtureTable creator should be non-null", creator);

      ModifiableFixtureWidget mfwTable = creator.createFixtureWidget(mockTableID, mockTableID, mockUIName,
            mockDependencies.getMockBrowser());
      assertNotNull("'mfwTable' should be non-null", mfwTable);
      assertTrue("'mfwTable' should be a FixtureTable", mfwTable instanceof FixtureTable);

      FixtureTable table = (FixtureTable)mfwTable;

      assertEquals(badLocator, table.getWaitTargetLocator(), mockUIName + mockTableID);
      assertTrue(table.clickCell("Second Column", 2));
      assertEquals("Text: column 2, row 2", table.getCellText("Second Column", 2));
      //assertEquals(table.getRowCount(), 2);
      assertTrue(table.isCellDataPresent(1, 1));
      assertTrue(table.typeIntoCell("First Column", 2, "Text: column 1, row 2"));
      assertTrue(table.pressInRow("alt delete", 4));
      assertTrue(table.pressInRow("insert", 5));
      assertTrue(table.pressInRow("alt backspace", 6));
      assertTrue(table.pressInCell("escape", "Second Column", 7));

      //table navigation widget
      assertTrue(table.goToPage("1"));
      assertEquals(table.getCurrentPage(), "1");
      assertEquals(table.getTotalPages(), "2");

      //verify wait for table to load
      assertNotNull("Failed to wait for Table to load", table.waitForTableToLoad());

      //table get selected cell text
      assertEquals("Text: row 2, column 3", table.getSelectedCell("Third Column", 2));

      //is Cell Editable
      assertFalse("The specified Cell in the is Editable", table.isCellEditable("First Column", 1));
      assertTrue("The specified Cell in the is not Editable", table.isCellEditable("Second Column", 2));

   }


   /**
    * Tests a Table's Finder actions
    */
   @Test
   public void tableFinderTests()
   {
      final String mockTableID = "mockTableID";

      assertEquals("FixtureTable.getSwtWidgetType", "swt:SwtTable", FixtureTable.getSwtWidgetType());

      FixtureWidgetCreator creator = FixtureTable.getFixtureWidgetCreator();
      assertNotNull("FixtureTable creator should be non-null", creator);

      ModifiableFixtureWidget mfwTable = creator.createFixtureWidget(mockTableID, mockTableID, mockUIName,
            mockDependencies.getMockBrowser());
      assertNotNull("'mfwTable' should be non-null", mfwTable);
      assertTrue("'mfwTable' should be a FixtureTable", mfwTable instanceof FixtureTable);

      FixtureTable table = (FixtureTable)mfwTable;

      assertEquals(badLocator, table.getWaitTargetLocator(), mockUIName + mockTableID);

      //Table Finder Widget
      assertTrue(table.clickCell("Second Column", 2));
   }

   /**
    * Tests to validate Table Show/Hide Columns Item
    */
   @Test
   public void tableShowHideColumnsTests()
   {
      final String mockTableID = "mockTableID";

      assertEquals("FixtureTable.getSwtWidgetType", "swt:SwtTable", FixtureTable.getSwtWidgetType());

      FixtureWidgetCreator creator = FixtureTable.getFixtureWidgetCreator();
      assertNotNull("FixtureTable creator should be non-null", creator);

      ModifiableFixtureWidget mfwTable = creator.createFixtureWidget(mockTableID, mockTableID, mockUIName,
            mockDependencies.getMockBrowser());
      assertNotNull("'mfwTable' should be non-null", mfwTable);
      assertTrue("'mfwTable' should be a FixtureTable", mfwTable instanceof FixtureTable);

      FixtureTable table = (FixtureTable)mfwTable;

      assertEquals(badLocator, table.getWaitTargetLocator(), mockUIName + mockTableID);

   }

 
   /**
    * Tests selectFromTable method.
    * 
    * This test is returning a IndexOutOfBoundsException. I believe it is caused
    * by the expected mock results from other test.
    */
   @Test
   public void selectFromTableTest()
   {
      final String mockTableID = "mockTableID";
      FixtureWidgetCreator creator = FixtureTable.getFixtureWidgetCreator();
      ModifiableFixtureWidget mfwTable = creator.createFixtureWidget(mockTableID, mockTableID, mockUIName,
            mockDependencies.getMockBrowser());
      FixtureTable table = (FixtureTable)mfwTable;

      assertEquals(badLocator, mockUIName + mockTableID, table.getWaitTargetLocator());
      assertTrue("Failed to select from Table cell", table.selectFromCell("Third Column", 2, "Item"));
   }


}
