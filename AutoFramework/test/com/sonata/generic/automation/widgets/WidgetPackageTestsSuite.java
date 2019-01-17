/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.widgets;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * The <code>WidgetPackageTestsSuite</code> class provides a JUnit test suite
 * for all the JUnit tests in the package
 */
@RunWith(Suite.class)
@Suite.SuiteClasses( {TestButton.class, TestTextBox.class, TestCheckBox.class,
      TestWidget.class, TestLabel.class, TestComboBox.class, TestRadioButton.class, TestTab.class})
public class WidgetPackageTestsSuite
{

}
