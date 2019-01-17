/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.widgets;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sonata.generic.automation.browser.Browser;

/**
 * The <code>MessageBox</code> class represents a Message Dialog Box that
 * consist of Text Messages ,Yes , No & Cancel Buttons.
 * 
 * This Method will replace the need of Webpage.java methods (Handling Message
 * Dialog Box) in future based on the official confirmation.
 */
public class MessageBox extends Widget
{
   private String messageType;
   private final WebDriverWait wait;
   
   /**
    * constructs an instance of the {@link MessageBox} class.
    * 
    * @param locator
    *           the locator of the MessageBox
    * @param browser
    *           the browser that will be used to access the control
    */
   public MessageBox(String locator, Browser browser)
   {
      // Find the <span> element with the locator matching the Message Dialog title.
      // Then traverse up to get the parent <div> element that is the entire dialog
      super(locator, browser);

      messageType = locator;
      this.wait = new WebDriverWait(this.getBrowser().getDriver(), 10);
   }

   /**
    * clicks the Message Box, this will invoke specific button to interact with.
    * 
    * @param responseButton
    *           The message dialog response, which is either {@code "no"},
    *           {@code "ok"}, {@code "yes"}, {@code "cancel"}, or
    *           {@code "close"} .
    * @return <li><code>true</code> if the Message Box's button is clicked successfully</li>
    *         <li><code>false</code> if the Message Box's button could not be located.</li>
    */
   public boolean clickResponseButton(final String responseButton)
   {
      // Since the messageBox locator itself is an XPATH, we can assume so when we get the locator of it
      // Each time when click the delete button, a new confirmation DialogBox popup, the dialogues are using the same
      // id, but the previous ones have "display: none", only the last one is the visible one. So if just locate it 
      // by "//button[.='" + responseButton + "']", it will failed from the second time since it will always be located
      // as the first dialogue which is disabled at this time.
      // Check from the source code, the visible dialogue box always follows a special element as below
      // "div class="k-overlay" style="display: block; z-index: 10020; opacity: 0.5;"></div>" 
      // so use above element to locate the current visible confirmation dialogue
      
      // Change code to adapt to the new html structure for confirmation box.
      
      String confirmationLocator;

      if(responseButton.equalsIgnoreCase("Ok") || responseButton.equalsIgnoreCase("Yes")
            || responseButton.equalsIgnoreCase("Delete") || responseButton.equalsIgnoreCase("Print"))
         confirmationLocator = "//div[@class='k-overlay']/following-sibling::*[1]//input[@id='kendoConfirmationAcceptButton']";
      else if (responseButton.equalsIgnoreCase("Cancel") || responseButton.equalsIgnoreCase("No"))
         confirmationLocator = "//div[@class='k-overlay']/following-sibling::*[1]//input[@id='kendoConfirmationCancelButton']";
      else 
         return false;
      
      if (!(this.getBrowser().exists(confirmationLocator)))
            throw new IllegalArgumentException("Cannot find the button pattern with the Message Dialog.");
      wait.until(ExpectedConditions.elementToBeClickable(By.xpath(confirmationLocator)));
      
      return this.getBrowser().clickByReturn(confirmationLocator);
   } 
   
   /**
    * returns the Message Type
    * 
    * This is the Getter method for the property MessageType
    * 
    * @return {@link String} representing the Message Type
    * 
    */
   public String getMessageType()
   {
      return messageType;
   }

   /**
    * returns {@link String} representing the Message Description text
    * 
    * @return {@link Label} representing the Message Description Field
    */
   public String getMessageDescription()
   {
      // Should get the visible confirmation dialogue box instead of the first one
      
      //return this.getBrowser().getText("//div[@id='body-text']"); 
      String msgLocator = "//div[@class='k-overlay']/following-sibling::*[1]//div[@id='body-text']";
      return this.getBrowser().getText(msgLocator);
   }
}
