/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.browser;

import org.openqa.selenium.Keys;

/**
 * The <code>SpecialKey</code> enumeration provides values that represent the
 * special keys that can be pressed, that are not actually characters. They can
 * be passed to {@link Browser#type(String, SpecialKey)}.
 */
public enum SpecialKey
{
   // This list is currently basically as for Selenium and it is not clear exactly
   // what many of them mean. For example what is the distinction between Enter and
   // Return, or Help and F1

   // Although it is tempting to tie the key directly to its Selenium key during construction that
   // results in EasyMock depending on Selenium when it tries to mock the {@link Browser} interface,
   // so instead we add a layer of indirection by insisting that the names must agree (see getKeys)

   // Remember that for binary compatibility we must not change the order of this list
   /** The backspace key */
   BACK_SPACE,
   /** The Tab key */
   TAB,
   /** The Enter key */
   ENTER,
   /** One (unspecified) of the Shift keys */
   SHIFT,
   /** One (unspecified) of the Ctrl keys */
   CONTROL,
   /** One (unspecified) of the Alt keys */
   ALT,
   /** The Escape key */
   ESCAPE,
   /** The space bar */
   SPACE,
   /** One (unspecified) of the Page Up keys */
   PAGE_UP,
   /** One (unspecified) of the Page Down keys */
   PAGE_DOWN,
   /** One (unspecified) of the End keys */
   END,
   /** One (unspecified) of the Home keys */
   HOME,
   /** One (unspecified) of the Left keys */
   LEFT,
   /** One (unspecified) of the Up keys */
   UP,
   /** One (unspecified) of the Right keys */
   RIGHT,
   /** One (unspecified) of the Down keys */
   DOWN,
   /** One (unspecified) of the Insert keys */
   INSERT,
   /** One (unspecified) of the Delete keys */
   DELETE,
   /** The F1 function key */
   F1,
   /** The F1 function key */
   F2,
   /** The F3 function key */
   F3,
   /** The F4 function key */
   F4,
   /** The F5 function key */
   F5,
   /** The F6 function key */
   F6,
   /** The F7 function key */
   F7,
   /** The F8 function key */
   F8,
   /** The F9 function key */
   F9,
   /** The F10 function key */
   F10,
   /** The F11 function key */
   F11,
   /** The F12 function key */
   F12;

   /**
    * constructs an instance of {@link SpecialKey}.
    */
   private SpecialKey()
   {
   }

   /**
    * returns the Selenium {@link Keys} value equivalent to this one.
    * 
    * @return the Selenium {@link Keys} value equivalent to this one.
    */
   protected Keys getKeys()
   {
      return Keys.valueOf(this.name());
   }

}
