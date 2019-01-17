package com.sonata.generic.automation.fixture.widget;

import com.sonata.generic.automation.browser.Browser;
import com.sonata.generic.automation.widgets.TextBox;
import com.sonata.generic.automation.widgets.Widget;

/**
 * Base class for a {@link FixtureWidget} that wraps the underlying automation
 * {@link TextBox} widget, but that may represent different SWT widgets (such as
 * password text box, text box and text area).
 */
public abstract class AbstractFixtureTextBox extends AbstractFixtureWidget
{
   private final TextBox textBox;

   /**
    * Base class constructor for a fixture-friendly representation of a widget
    * that wraps the underlying automation {@link TextBox} widget (since more
    * than one type of SWT widget works with that type of automation widget).
    * 
    * @param widgetName
    *           The descriptive name of the widget, as used by callers of the
    *           fixture (such as the FitNesse test page) to identify the widget
    *           on which an action should be performed.
    * @param widgetID
    *           The underlying widget ID (as specified in the declarative UI
    *           definition) that is used to calculate the widget's automation
    *           locator.
    * @param idBase
    *           The debug ID prefix that is used to calculate the widget's
    *           automation locator.
    * @param browser
    *           The automation {@link Browser} object that the widget will run
    *           in.
    * 
    * @throws IllegalArgumentException
    *            One or more arguments are null or empty.
    */
   protected AbstractFixtureTextBox(final String widgetName, final String widgetID, final String idBase,
         final Browser browser)
   {
      super(widgetName, widgetID, idBase, browser);
      this.textBox = new TextBox(getLocator(), browser);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected Widget getAutomationWidget()
   {
      return getAutomationTextBox();
   }

   /**
    * Gets the underlying automation {@link TextBox} that this fixture widget is
    * wrapping.
    * 
    * @return The underlying automation {@link TextBox} that this fixture widget
    *         is wrapping.
    */
   protected TextBox getAutomationTextBox()
   {
      return textBox;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean clear()
   {
      return textBox.clear();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean click()
   {
      return textBox.click();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getText()
   {
      return textBox.getText();
   }   
   
   /**
    * {@inheritDoc}
    */
   @Override
   public String getPlaceHolderText()
   {
      return textBox.getPlaceHolderText();
   }
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean type(final String value)
   {
      return textBox.type(value);
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean typeByJavaScript(final String value)
   {
      return textBox.typeByJavaScript(value);
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean typeNoTab(final String value)
   {
      return textBox.typeNoTab(value);
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean typeWithCtrlADel(final String value)
   {
      return textBox.typeWithCtrlADel(value);
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean typeWithoutWaitForClicable(final String value)
   {
      return textBox.typeWithoutWaitForClicable(value);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean typeWithoutTab(final String value)
   {
      return textBox.type(value, false);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean typeWithoutClear(final String value)
   {
      return textBox.typeWithoutClear(value);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean typeWithoutClearAndWithoutTab(final String value)
   {
      return textBox.typeWithoutClear(value, false);
   }
   
   /**
    * ================================================================
    * Sikuli related actions
    * 
    */
   
   @Override
   public boolean typeIntoImage(final String value)
   {
      return textBox.typeIntoImage(value);
   }   
}
