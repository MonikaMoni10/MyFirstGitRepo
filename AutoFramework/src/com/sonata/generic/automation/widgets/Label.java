/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.widgets;

import org.sikuli.basics.Settings;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Region;
import org.sikuli.script.Screen;

import com.sonata.generic.automation.browser.Browser;

/**
 * The <code>Label</code> class represents a text label
 */
public class Label extends Widget
{
   /**
    * constructs an instance of the {@link Label} class.
    * 
    * @param name
    *           the name of the label as it appears in the DOM on the browser
    * @param browser
    *           the browser that will be used to access the label
    */
   public Label(final String name, final Browser browser)
   {
      super(name, browser);
   }

   /**
    * Retrieves text from a specified label. The method will first check to make
    * sure the location is visible.
    * 
    * @return the text from the specified label, or null if the label cannot be
    *         located.
    */
   public String getText()
   {
      try
      {
         if( this.getBrowser().getText(this.getLocator()).equals("") || this.getBrowser().getText(this.getLocator()).equals(null))
               return this.getBrowser().getAttribute(this.getLocator(), "innerHTML");
         else
            return this.getBrowser().getText(this.getLocator());
      }
      catch (Exception e)
      {
         return null;
      }
   }

   /**
    * clicks the label, this would not do anything.
    * 
    * @return <li><code>true</code> if the label is clicked successfully</li>
    *         <li><code>false</code> if the label could not be located.</li>
    */
   public boolean click()
   {
      return this.getBrowser().click(this.getLocator());
   }

   /**
    * Waits for the specified content
    * 
    * @param content
    *           the expected content to wait for
    * 
    * @return <li><code>true</code> if the content was displayed</li> <li>
    *         <code>false</code> if the timeout was reached.</li>
    */
   public boolean waitForContent(final String content)
   {
      return this.getBrowser().waitForContent(this.getLocator(), content);
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
           screen.wait(image, 60);
           screen.exists(image).highlight(5).click();
           return true; 
           
         }
         catch (FindFailed e) 
         {
           e.printStackTrace();
           return false;
         }
   }  
   
   /**
    * Select menu item the image.  Uses the Sikuli tool.
    * 
    * @param menuTitle
    *    The name of the menu where the menu item resides.
    *    
    * @param menuItem
    *    The name of the menu item to select.
    * 
    * @return Whether the action was successful or not
    * 
    * @throws IllegalArgumentException
    *            This widget does not support this action.
    */  
   public boolean selectMenuItemFromImage(String menuTitle, String menuItem)
   {
         Settings.OcrTextSearch = true;
         Screen screen = new Screen();
         
         try 
         {
           String image =  this.getLocator();
           Match toolbar = screen.wait(image, 60);
           
           toolbar.click(menuTitle);
           
           Region dropDownRegion = new Region(toolbar.getLastMatch().getX(), toolbar.getLastMatch().getY(), 200, 200);
           
           dropDownRegion.highlight(5);
           
           dropDownRegion.mouseDown(org.sikuli.script.Button.LEFT);
           dropDownRegion.mouseMove(menuItem);
           dropDownRegion.mouseUp(org.sikuli.script.Button.LEFT);
           
           return true; 
           
         }
         catch (FindFailed e) 
         {
           e.printStackTrace();
           return false;
         }     
   }      
}
