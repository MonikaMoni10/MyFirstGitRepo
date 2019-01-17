/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.browser;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.Keys;

/**
 * The <code>SpecialKeyCombo</code> enumeration provides values that represent
 * the combinations or simultaneous pressing of special keys, that are not
 * actually characters. They can be passed to
 * {@link Browser#type(String, SpecialKeyCombo)}.
 */
public enum SpecialKeyCombo
{
   // Remember that for binary compatibility we must not change the order of this list
   /** The ALT key + DELETE key */
   ALT__DELETE,
   /** The ALT key + BACK_SPACE key */
   ALT__BACK_SPACE,
   /** The SHIFT key + TAB key */
   SHIFT__TAB,
   /** The SHIFT key + F8 + LEFT key combo; used for highlighting text */
   SHIFT__F8__LEFT,
   /** The SHIFT key + F8 + HOME key combo; used for highlighting text */
   SHIFT__F8__HOME,
   /** The SHIFT key + F8 + END key combo; used for highlighting text */
   SHIFT__F8__END,
   /** The CONTROL key + SHIFT key + F8 + END key combo; used for highlighting text */
   CONTROL__SHIFT__F8__END,
   SHIFT__END;
   /** The SHIFT key + END key combo; used for highlighting text */
   /**
    * constructs an instance of {@link SpecialKeyCombo}.
    */
   private SpecialKeyCombo()
   {
   }

   /**
    * returns the Selenium {@link Keys} List equivalent to this one's key
    * components
    * 
    * @return the Selenium {@link Keys} List equivalent to this one's key
    *         components.
    */
   protected List<Keys> getKeys()
   {
      List<Keys> keysList = new ArrayList<Keys>();

      // Split the name of the SpecialKeyCombo to retrieve the individual Special Keys
      // Since BACK_SPACE contains an "_" have to split by "__" (2 underscores)
      String[] keys = this.name().split("__");
      for (String key : keys)
      {
         keysList.add(SpecialKey.valueOf(key).getKeys());
      }

      return keysList;
   }

}
