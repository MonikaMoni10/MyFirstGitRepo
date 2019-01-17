package com.sonata.generic.automation.widgets;

import org.easymock.EasyMock;
import org.junit.Test;

import com.sonata.generic.automation.browser.Browser;
import com.sonata.generic.automation.widgets.RadioButton;

/**
 * The <code>TestRadioButton</code> class provides the JUnit tests on the
 * {@link RadioButton} class
 */
public class TestRadioButton
{
   /** The id must not be null */
   @Test(expected = IllegalArgumentException.class)
   public void radioButtonMustHaveID()
   {
      new RadioButton(null, EasyMock.createNiceMock(Browser.class));
   }

   /** The browser must not be null */
   @Test(expected = IllegalArgumentException.class)
   public void radioButtonMustHaveBrowser()
   {
      new RadioButton("mock", null);
   }

   /** Select (Check) the radio button passes it to the browser */
   @Test
   public void canSelectRadioButton()
   {
      final String id = "mockRadioButton";
      final Browser browser = getBrowserExpectingSelectRadioButton(id);
      final RadioButton radioButton = new RadioButton(id, browser);
      radioButton.select();
      EasyMock.verify(browser);
   }

   /**
    * Validates if the radio button is selected (checked) or not, it returns a
    * browser as well
    */
   @Test
   public void canValidateRadioButtonState()
   {
      final String id = "mockRadioButton";
      final Browser browser = getBrowserWithRadioButtonState(id);
      final RadioButton radioButton = new RadioButton(id, browser);
      radioButton.isSelected();
      EasyMock.verify(browser);
   }

   /**
    * returns a mock browser that expects to have radio button selected
    * 
    * @param id
    *           the id of the Radio Button
    * @return a {@link Browser} that expects to have Radio Button selected
    */
   private Browser getBrowserExpectingSelectRadioButton(final String id)
   {
      Browser browser = EasyMock.createMock(Browser.class);
      EasyMock.expect(browser.selectCheckBox("//span[@id='" + id + "']/input")).andReturn(true).once();
      EasyMock.replay(browser);
      return browser;
   }

   /**
    * returns a mock browser with validation if a Radio Button is selected
    * 
    * @param id
    *           the id of the Radio Button
    * @return a {@link Browser} with validation if a Radio Button is selected
    */
   private Browser getBrowserWithRadioButtonState(final String id)
   {
      Browser browser = EasyMock.createMock(Browser.class);
      EasyMock.expect(browser.isSelected("//span[@id='" + id + "']/input")).andReturn(true).once();
      EasyMock.replay(browser);
      return browser;
   }
}
