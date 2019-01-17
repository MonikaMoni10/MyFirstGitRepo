/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.widgets;

import org.sikuli.script.FindFailed;
import org.sikuli.script.Region;
import org.sikuli.script.Screen;

import com.sonata.generic.automation.browser.Browser;

/**
 * The <code>Button</code> class represents a button
 */
public class Button extends Widget
{
   /**
    * constructs an instance of the {@link Button} class.
    * 
    * @param locator
    *           the locator of the button as it appears in the DOM on the
    *           browser
    * @param browser
    *           the browser that will be used to access the text box
    */
   public Button(final String locator, final Browser browser)
   {
      super(locator, browser);
   }

   /**
    * clicks the button
    * 
    * @return <li><code>true</code> if the button is clicked successfully</li>
    *         <li><code>false</code> if the button could not be located.</li>
    */
   public boolean click()
   {
      return click(true);
   }

   /**
    * clicks the button by sending special key "RETRUN"
    * 
    * @return <li><code>true</code> if the button is clicked successfully</li>
    *         <li><code>false</code> if the button could not be located.</li>
    */
   public boolean clickByReturn()
   {
      return clickByReturn(true);
   }
   
   /**
    * clicks the button. This implementation will be called by performance tests
    * to control the Accpac Request checking, basically to not do the check.
    * 
    * @param doAccpacRequest
    *           Indicates whether or not to do Accpac Request checking within
    *           the click or not.
    * @return <li><code>true</code> if the button is clicked successfully</li>
    *         <li><code>false</code> if the button could not be located.</li>
    */
   public boolean click(final boolean doAccpacRequest)
   {
      if (doAccpacRequest)
         return this.getBrowser().click(this.getLocator());
      else
         return this.getBrowser().click(this.getLocator(), doAccpacRequest);
   }

   /**
    * clicks the button by sending special key "RETURN". This implementation 
    * will be called by performance tests to control the Accpac Request checking, 
    * basically to not do the check.
    * 
    * @param doAccpacRequest
    *           Indicates whether or not to do Accpac Request checking within
    *           the click or not.
    * @return <li><code>true</code> if the button is clicked successfully</li>
    *         <li><code>false</code> if the button could not be located.</li>
    */
   public boolean clickByReturn(final boolean doAccpacRequest)
   {
      if (doAccpacRequest)
         return this.getBrowser().clickByReturn(this.getLocator());
      else
         return this.getBrowser().clickByReturn(this.getLocator(), doAccpacRequest);
   }   
   
   /**
    * clicks the button and then waits for a certain element to exist and be
    * visible after the clicking event.
    * 
    * Useful for clicks which cause pop-ups, or clicks which start calculations.
    * 
    * This function will timeout after default timeout specified in
    * DefaultBrowserSettings class.
    * 
    * @param element
    *           The location of the element the function should wait for. The
    *           locator string can accept an XPath to the location or an HTML
    *           id.
    * @return <li><code>true</code> if the button is clicked, and
    *         <code>element</code> was located.</li> <li><code>false</code> if
    *         the button or <code>element</code> could not be located.</li>
    */
   public boolean clickAndWaitFor(final String element)
   {
      return this.getBrowser().clickAndWaitFor(this.getLocator(), element);
   }

   /**
    * clicks the button and then waits for an extended period of time for a
    * certain element to exist and be visible after the clicking event.
    * 
    * Useful for clicks which cause pop-ups, or clicks which start calculations.
    * 
    * This function will timeout after default timeout specified in
    * DefaultBrowserSettings class.
    * 
    * @param element
    *           The location of the element the function should wait for. The
    *           locator string can accept an XPath to the location or an HTML
    *           id.
    * @return <li><code>true</code> if the button is clicked, and
    *         <code>element</code> was located.</li> <li><code>false</code> if
    *         the button or <code>element</code> could not be located.</li>
    */
   public boolean clickAndWaitForLargeTimeout(final String element)
   {
      return this.getBrowser().clickAndWaitForLargeTimeout(this.getLocator(), element);
   }

   /**
    * Click a button, and wait for an element to disappear.
    * 
    * Most commonly, this can be used for dialog boxes. When a <code>save</code>
    * or <code>cancel</code> button is clicked, the dialog box may needs time to
    * process before disappearing.
    * 
    * @param locatorDie
    *           The element to wait for to disappear.
    * @return <li><code>true</code> if the button was clicked, and the
    *         locatorDie disappeared.</li> <li>
    *         <code>false</code> if the button could not be located.</li>
    */
   public boolean clickAndWaitForDie(final String locatorDie)
   {
      return this.getBrowser().clickAndWaitForDie(this.getLocator(), locatorDie);
   }

   /**
    * Click a button, and wait for an extended period of time for an element to
    * disappear.
    * 
    * Most commonly, this can be used for dialog boxes. When a <code>save</code>
    * or <code>cancel</code> button is clicked, the dialog box may needs time to
    * process before disappearing.
    * 
    * @param locatorDie
    *           The element to wait for to disappear.
    * @return <li><code>true</code> if the button was clicked, and the
    *         locatorDie disappeared.</li> <li>
    *         <code>false</code> if the button could not be located.</li>
    */
   public boolean clickAndWaitForDieLargeTimeout(final String locatorDie)
   {
      return this.getBrowser().clickAndWaitForDieLargeTimeout(this.getLocator(), locatorDie);
   }

   /**
    * Clicks the button, then wait for elementOne to exist and be visible after
    * a click event. If elementOne doesn't exist, it checks if elementTwo exists
    * and if it does, then clicks on elementTwo and again wait for elementOne.
    * 
    * Useful for clicks which may cause a confirmation pop-ups and it is
    * necessary to click on this popup's button to continue waiting for the
    * original element.
    * 
    * @param elementOne
    *           The location of the element the function should wait for. The
    *           locator string can accept an XPath to the location or an HTML
    *           id.
    * @param elementTwo
    *           The location of the element that may pop up before elementOne is
    *           visible. The locator string can accept an XPath to the location
    *           or an HTML id.
    * @return <li><code>True</code> if the button is clicked, and
    *         <code>elementOne</code> was located.</li> <li><code>False</code>
    *         if the button or <code>elementOne</code> could not be located.</li>
    */
   public boolean clickAndWaitForEither(final String elementOne, final String elementTwo)
   {
      return this.getBrowser().clickAndWaitForEither(this.getLocator(), elementOne, elementTwo);
   }

   /**
    * Click the button, and wait for a frame to show up or refresh.
    * 
    * Once the new frame is showing, check for an element located in the new
    * frame.
    * 
    * Note: This function switches focus to the new frame once complete.
    * 
    * @param element
    *           The location of the element the function should wait for. The
    *           locator string can accept an XPath to the location or an HTML
    *           id.
    * @param elementFrame
    *           The Frame locator where the element to wait for can be found.
    *           This frame is switched to and focused on after clicking the
    *           locator.
    * @return <li><code>true</code> if the button is clicked, and
    *         <code>element</code> was located in the new frame.</li> <li>
    *         <code>false</code> if the button or <code>element</code> could not
    *         be located or the new frame did not load.</li>
    */
   public boolean clickAndWaitForFrame(final String element, final String elementFrame)
   {
      return this.getBrowser().clickAndWaitForFrame(this.getLocator(), element, elementFrame);
   }

   /**
    * Click the button and wait for a window to come up. Once the new window is
    * up, check for an item located on the new window. It waits for the element
    * to appear in the new window
    * 
    * Note that this function switches focus to the new window once complete.
    * 
    * @param window
    *           The window handler that needs to switch to.
    * @param element
    *           The location of the element the function should wait for. The
    *           locator string can accept an XPath to the location or an HTML
    *           id.
    * @return <li><code>true</code> if the button is clicked, and
    *         <code>element</code> was located in the new window.</li> <li>
    *         <code>false</code> if the button or <code>element</code> could not
    *         be located or the new window did not load.</li>
    */
   public boolean clickAndWaitForWindow(final String window, final String element)
   {
      return this.getBrowser().clickAndWaitForWindow(this.getLocator(), window, element);
   }

   /**
    * Overwritten as CheckBox locator based in is not actually the element that
    * has the disabled attribute
    * 
    * @return <li><code>True</code> if the value is disabled.</li> <li>
    *         <code>False</code> if the value is not disabled</li>
    */
   public boolean isDisabled()
   {
      return this.getBrowser().isDisabled(this.getLocator());
   }
   
   /**
    * Retrieves text value from a button. The method will first check to make
    * sure the location is visible.
    * 
    * @return the text from the button, or null if the button cannot be
    *         located.
    */
   public String getText()
   {
      try
      {
         return this.getBrowser().getText(this.getLocator());
      }
      catch (Exception e)
      {
         return null;
      }
   }
   
   /**
    * ==============================================================
    * Sikuli related actions
    * 
    */
   
   /**
    * Clicks on an image, using the Sikuli tool.
    * 
    * @return boolean whether action was success or not.
    */
   public boolean clickImage() 
   {
      Screen screen = new Screen();
         
       try 
       {
         String image =  this.getLocator();
         Region button = screen.wait(image, 60).highlight();
         button.click();
         button.click();
         
         return true;           
       }
       catch (FindFailed e) 
       {
         e.printStackTrace();
       }
         
      return false;
   }  
   
   
   /**
    * ==============================================================
    * Sikuli related actions
    * 
    */
   
   /**
    * Double-Click on an image, using the Sikuli tool.
    * 
    * @return boolean whether action was success or not.
    */
   public boolean doubleClickImage() 
   {
      Screen screen = new Screen();
         
       try 
       {
         String image =  this.getLocator();
         Region button = screen.wait(image, 30).highlight();
         button.doubleClick();
         button.doubleClick();
         
         return true;           
       }
       catch (FindFailed e) 
       {
         e.printStackTrace();
       }
         
      return false;
   }
   
   /**
    * ==============================================================
    * Sikuli related actions
    * 
    */
   
   /**
    * Right-Click on an image, using the Sikuli tool.
    * 
    * @return boolean whether action was success or not.
    */
   public boolean rightClickImage() 
   {
      Screen screen = new Screen();
         
       try 
       {
         String image =  this.getLocator();
         Region button = screen.wait(image, 30).highlight();
         button.rightClick();
         button.rightClick();
         
         return true;           
       }
       catch (FindFailed e) 
       {
         e.printStackTrace();
       }
         
      return false;
   }
}