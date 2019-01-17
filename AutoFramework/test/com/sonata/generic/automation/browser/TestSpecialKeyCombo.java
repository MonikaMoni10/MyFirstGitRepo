/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.browser;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.openqa.selenium.Keys;

import com.sonata.generic.automation.browser.SpecialKeyCombo;

/**
 * The <code>TestSpecialKeyCombo</code> class provides JUnit testing of the
 * {@link SpecialKeyCombo} class.
 * 
 */
public class TestSpecialKeyCombo
{
   /**
    * Confirms things about the public names, such as ensuring collision
    * prevention, not exporting hidden words and concepts etc.
    */
   @Test
   public void publicNamesAreAcceptable()
   {
      Utility.confirmPublicNamesAreAcceptable(SpecialKeyCombo.class);
   }

   /**
    * The SpecialKeyCombo expands on SpecialKey to enable simultaneous pressing
    * of multiple Selenium special keys.
    */
   @Test
   public void eachSpecialKeyComboRepresentsACombinationOfSeleniumKeys()
   {
      for (SpecialKeyCombo keyCombo : SpecialKeyCombo.values())
      {
         List<Keys> seleniumKeysList = keyCombo.getKeys();
         for (Keys seleniumKey : seleniumKeysList)
         {
            assertNotNull(seleniumKey);
         }
      }
   }

}
