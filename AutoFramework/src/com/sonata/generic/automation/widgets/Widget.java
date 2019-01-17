/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.widgets;

import java.awt.event.KeyEvent;

import org.sikuli.script.FindFailed;
import org.sikuli.script.Region;
import org.sikuli.script.Screen;

import com.sonata.generic.automation.browser.Browser;
import com.sonata.generic.automation.browser.SpecialKey;
import com.sonata.generic.automation.library.TimeDelay;

/**
 * The <code>Widget</code> class provides common methods for UI widget. If no
 * specific classes are defined for an UI element, it will at least have some
 * methods to work with.
 */
public class Widget implements WidgetInterface
{
   private final Browser browser;
   private final String  locator;
   
   /**
    * constructs an instance of the {@link Widget} class.
    * 
    * @param locator
    *           the locator (ID) of the widget as it appears in the DOM on the
    *           browser.
    * @param browser
    *           the browser that will be used to access the button
    */
   public Widget(final String locator, final Browser browser)
   {
      if (locator == null || locator.isEmpty())
         throw new IllegalArgumentException("The locator of a widget must be specified");
      if (browser == null)
         throw new IllegalArgumentException("The browser must be specified");
      this.locator = locator;
      this.browser = browser;
   }

   /**
    * public method to get locator
    * 
    * @return locator the locator of the class.
    */
   public String getLocator()
   {
      return this.locator;
   }

   /**
    * public method to get browser
    * 
    * @return browser the browser of the class.
    */
   public Browser getBrowser()
   {
      return this.browser;
   }

   /**
    * Checks to see if a widget is visible.
    * 
    * @return <li><code>true</code> if the widget was visible.</li> <li>
    *         <code>false</code> if the widget was not visible.</li>
    */
   public boolean isVisible()
   {
      return browser.isVisible(this.locator);
   }

   /**
    * Checks to see if a widget exists in DOM.
    * 
    * @return <li><code>true</code> if the widget exists.</li> <li>
    *         <code>false</code> if the widget does not exists.</li>
    */
   public boolean exists()
   {
      return browser.exists(this.locator);
   }

   /**
    * Verify if a widget is disabled or not.
    * 
    * @return <li><code>true</code> if widget is disabled</li> <li>
    *         <code>false</code> otherwise</li>
    */
   public boolean isDisabled()
   {
      return browser.isDisabled(this.locator);
   }

   /**
    * Verify if a widget is editable or not.
    * 
    * This method should be different for combo box, will need to override in
    * the subclass.
    * 
    * This function also tests if the element is visible, if not, it should not
    * be editable to begin with.
    * 
    * @return <li><code>true</code> if the widget is editable</li> <li>
    *         <code>false</code> otherwise</li>
    */
   public boolean isEditable()
   {
      return browser.isEditable(this.locator);
   }

   /**
    * Move mouse to an element and hover over it.
    */
   public void hover()
   {
      browser.hover(this.locator);
   }
   
   /**
    * Waits for an element to exist in DOM
    * 
    * @return <li><code>true</code> if the item was found within the timeout.</li>
    *         <li><code>false</code> if the timeout was reached.</li>
    */
   public boolean waitForElement()
   {
      return browser.waitForElement(this.locator);
   }

   /**
    * Waits for an element, that is in a different window, to exist in DOM
    * 
    * @param window
    *           the window name where the element to wait for resides
    * 
    * @return <li><code>true</code> if the item was found within the timeout.</li>
    *         <li><code>false</code> if the timeout was reached.</li>
    */
   public boolean waitForElementInDifferentWindow(final String window)
   {
      return this.getBrowser().waitForElementInDifferentWindow(this.locator, window);
   }

   /**
    * Waits for an element to exist in DOM
    * 
    * @return <li><code>true</code> if the item was found within the timeout.</li>
    *         <li><code>false</code> if the timeout was reached.</li>
    */
   public boolean waitForNoElement()
   {
      return browser.waitForNoElement(this.locator);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean clear()
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("clear"));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean clearAndValidate(final String defaultValue)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("clearAndValidate"));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean click()
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("click"));
   }

   /**
    * 
    * {@inheritDoc}
    */
   @Override
   public boolean closeCurrentWindow()
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("closeCurrentWindow"));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String[] getAllOptions()
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("getOptions"));
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public String[] getSelectedOptions()
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("getSelectedOptions"));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getText()
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("getText"));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean isSelected()
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("isSelected"));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean select()
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("select"));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean selectByLabel()
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("selectByLabel"));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean selectComboBox(final String optionValue, boolean textValidation)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("selectComboBox"));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean deselectFromListBox(final String optionValue)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("deselect"));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean deselectAllFromListBox()
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("deselectAll"));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean selectAllFromListBox()
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("selectAll"));
   }

   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean type(final String value)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("type"));
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean typeTelephoneNumber(final String value)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("typeTelephoneNumber"));
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean typeByJavaScript(final String value)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("typeByJavaScript"));
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean typeNoTab(final String value)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("typeNoTab"));
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean typeWithCtrlADel(final String value)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("typeWithCtrlADel"));
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean typeIntoCellByColumnIdx(final String value)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("typeIntoCell"));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean typeWithoutWaitForClicable(final String value)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("typeWithoutWaitForClicable"));
   }
   
   /**
    * type a TAB.
    * 
    * @return <li><code>true</code> if the widget exists.</li> <li>
    *         <code>false</code> if the widget does not exists.</li>
    */
   public boolean pressTab()
   {
      return browser.type(locator, SpecialKey.TAB);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean type(final String value, final boolean tab)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("type"));
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean typeWithCtrlADel(final String value, final boolean tab)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("typeWithCtrlADel"));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean typeIntoCell(final String value, final boolean tab)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("typeIntoCell"));
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean typeWithoutWaitForClicable(final String value, final boolean tab)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("typeWithoutWaitForClicable"));
   }

   
   /**
    * {@inheritDoc}
    */
   @Override
   public boolean typeWithoutClear(final String value)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("typeWithoutClear"));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean typeWithoutClear(final String value, final boolean doTab)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("typeWithoutClear"));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getSelectedValue()
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("getSelectedValue"));
   }

   /**
    * Builds the exception message stating that this widget does not support the
    * specified action.
    * 
    * @param action
    *           The friendly name of the action that was attempted on the
    *           widget.
    * @return The exception message stating that this widget does not support
    *         the specified action.
    */
   private String buildActionExceptionMessage(final String action)
   {
      StringBuilder sbExceptionMsg = new StringBuilder();
      sbExceptionMsg.append("'");
      sbExceptionMsg.append(this.locator);
      sbExceptionMsg.append("' does not support '");
      sbExceptionMsg.append(action);
      sbExceptionMsg.append("'");

      return sbExceptionMsg.toString();
   }

   /**
    * Wait until a widget becomes enabled before the timeout is reached.
    * 
    * @return <li><code>true</code> if widget becomes disabled</li> <li>
    *         <code>false</code> otherwise</li>
    */
   public boolean waitUntilDisabled()
   {
      for (int index = 0; index < 15; index++)
      {
         if (isDisabled())
         {
            return true;
         }
         TimeDelay.doPause(1000);
      }
      return false;
   }

   /**
    * Wait until a widget becomes disabled before the timeout is reached.
    * 
    * @return <li><code>true</code> if widget becomes enabled</li> <li>
    *         <code>false</code> otherwise</li>
    */
   public boolean waitUntilEnabled()
   {
      for (int index = 0; index < 15; index++)
      {
         if (!isDisabled())
         {
            return true;
         }
         TimeDelay.doPause(1000);
      }
      return false;
   }

   @Override
   public boolean waitForContent(String content)
   {
      throw new IllegalArgumentException(buildActionExceptionMessage("waitForContent"));
   }

   
   /**
    * ==============================================================
    * Sikuli related actions
    * 
    */
   
   /**
    * Wat for an image, using the Sikuli tool.
    *    
    * @return boolean whether action was success or not.
    */   
   public boolean waitForImage() 
   {
         Screen screen = new Screen();
         
         try 
         {
           String image =  this.getLocator();
           screen.wait(image, 60);
           screen.exists(image).highlight(5);
           return true; 
           
         }
         catch (FindFailed e) 
         {
           e.printStackTrace();
           return false;
         }
   }
   /**
    * ==============================================================
    * Sikuli related actions
    * 
    */
   
   /**
    * Wait for an image, using the Sikuli tool.
    * 
    * @param  timeout, in seconds, for the image to show up
    *    
    * @return boolean whether action was success or not.
    */   
   public boolean waitForImage(int timeout) 
   {
         Screen screen = new Screen();
         
         try 
         {
           String image =  this.getLocator();
           screen.wait(image, timeout);
           screen.exists(image).highlight(5);
           return true; 
           
         }
         catch (FindFailed e) 
         {
           e.printStackTrace();
           return false;
         }
   }
   /**
    * Pressing a series of keys, such as Ctrl+X, Ctrl+V, or Del. 
    * 
    * @param  keyToHold, the key to press while entering another key. Accept 'CONTROL', 'SHIFT', 'DELETE' and 'F2'.
    *         This is not required and can be left empty.  If left empty, it will just enter the sequence
    *         of letters in charToPress.
    *         
    * @param  charToPress, the sequence of characters to enter while holding the key 'keyToHold'.  This is 
    *         optional.  If it is empty, it will simply press the single key in keyToHold. e.g. Delete key. 
    * 
    * @return <li><code>true</code> if the region is found and the keys are pressed successfully.</li> <li>
    *         <code>false</code> if the widget does not exists, keyToHold is not recognized,
    *          or issues with pressing the keys.</li>
    */
   public boolean pressKeySequence(String keyToHold, String charToPress)
   {
      Screen screen = new Screen();
      Region myIcon;
      int keyToHoldEvent;
      
      try {
      
         // Decide what type of action to take      
         if (keyToHold !=""){
            if (keyToHold.equalsIgnoreCase("CONTROL")){
               keyToHoldEvent =  KeyEvent.VK_CONTROL;
            } else if (keyToHold.equalsIgnoreCase("ALT")){
               keyToHoldEvent = KeyEvent.VK_ALT;
            } else if (keyToHold.equalsIgnoreCase("SHIFT")){
               keyToHoldEvent = KeyEvent.VK_SHIFT;
            } else if (keyToHold.equalsIgnoreCase("DELETE")){
               keyToHoldEvent = KeyEvent.VK_DELETE;
            } else if (keyToHold.equalsIgnoreCase("F2")){
               keyToHoldEvent = KeyEvent.VK_F2;
            } else {            
               throw new Exception ("Not recognizing which key to hold: " + keyToHold);         
            }
         } else {
            keyToHoldEvent = 0;
         }
         
        
         String image =  this.getLocator();
         myIcon = screen.wait(image, 30);
         
         // Do not click on the image itself but highlight it only. Confirming
         // that we have it in focus and carrying out the keypress based on its existence.
         // This provide more flexibility in certain scenerio. e.g. if a folder is already in focus/
         // clicked/in focus, sending a click here will edit its text instead of trying to do
         // what we are trying to do e.g. Ctrl+C the folder itself.      

         screen.exists(image).highlight(5);
         
         // The following will mimic holding onto a specific key while typing other characters
         if (keyToHoldEvent !=0)
         {
            myIcon.keyDown(keyToHoldEvent);
         }
         if (charToPress !="")
         {
            myIcon.type(charToPress);
         }
         if (keyToHoldEvent !=0)
         {
            myIcon.keyUp(keyToHoldEvent);
         }
         return true;

      }
      catch (FindFailed e)
      {
        e.printStackTrace();        
        return false;
      }
      catch (Exception f)
      {
        f.printStackTrace();
        return false;
      }
   }
   
}
