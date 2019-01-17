/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.browser;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * The <code>Browser</code> interface contains methods for interacting with the
 * browser.
 */
public interface Browser
{	   
   /**
    * Opens a new Web Page.
    * 
    * @param url
    *           Adds this string to the browserURL specified in the Connect()
    *           method.
    */
   void openURLWithoutUrlValidation(final String url);
   
   /**
    * Sign in to the application portal page.
    * 
    * @param sageId
    *           The sageId for login
    * @param password
    *           The password of SageId for login  
    * 
    * @return 
    *         <li><code>home url after sign in</code> </li> <li> 
    */
   String signinToPortal(final String sageId, final String password);
   
   /**
    * Opens a UI by its full Url.
    * 
    * @param completeUrl
    *           the complete url of a url. 
    *           
    * @return <li><code>True </code>if the UI was opened</li> <li>
    *         <code>False </code>otherwise</li>
    */
   boolean openUiByFullUrl(String completeUrl);

   
   /** 
    * open the specific ui by navigating through the portal home page
    * 
    * @param applicationfullname
    *           get from layoutmap ui.applicationfullname         
    * @param category
    *           get from layoutmap ui.category
    * @param uiName
    *           get from layoutmap ui.name  
    * @param uiMenuName
    *           get from layoutmap ui.menuName   
    * @param completeUrl
    *           generated based on hompeUrl and ui.plication/ui.name from layoutmap
    * @return 
    *         <li><code>qunique data-menuid and iframe of the UI</code> </li> <li> 
   */
   String[] openSpecificUi(final String applicationfullname, final String category, 
         final String uiName, final String uiMenuName, final String completeUrl);
   
   /**
    * Navigate to a specific ui through portal home page by extending the menus.
    * 
    * @param applicationfullname
    *           the full name of an application, as shown on first level menu
    * @param category
    *           a category of an application, as shown on the second level menu
    * @param uiMenuName
    *           the full name of a ui, as shown on the third level menu                      
    *           
    * @return 
    *         return the xpath locator of a ui after the third level menu is extended
    */
   String navigateToUi(final String applicationfullname, final String category, final String uiMenuName);
   
   /**
    * Sign in to the application portal page.
    * 
    * @param sageId
    *           The sageId for login
    * @param password
    *           The password of SageId for login  
    * @param sessionDate
    *           The sessionDate to be updated after login.
    * 
    * @return 
    *         <li><code>home url after sign in</code> </li> <li> 
    */
   String signinToPortalWithSessionDate(final String sageId, final String password, final String sessionDate);
   
   /**
    * Get the iframe of an opened UI
    * 
    * @param completeUrl
    *           the full url of an UI, saved in src attribute
    *
    * @return 
    *         <li><code>data-menuid</code> if the menu with text value set as uiName is found,
    *         then click it</li><li> 
    *         <code>null</code> can't find the menu with text value set as uiName</li>          
    */
   String getIFrame(final String completeUrl);
   
   /**
    * Opens a new Web Page, and waits for a specified element to exist before
    * continuing.
    * 
    * @param url
    *           Adds this string to the browserURL specified in the Connect()
    *           method.
    * @param element
    *           The location of the element the function should wait for. The
    *           locator string can accept an XPath to the location or an HTML
    *           id.
    *
    * @return <li><code>True</code> if <code>url</code> was accessed, and
    *         <code>element</code> was located.</li> <li><code>False</code> if
    *         the <code>element</code> could not be located within the
    *         predefined Timeout period.</li>
    */
   boolean openURLAndWaitFor(final String url, final String element);

   /**
    * Quits this driver, closing every associated window.
    * 
    * @return <li><code>true</code> if the window was closed.</li> <li>
    *         <code>false</code> if the window was not closed.</li>
    */
   boolean close();

   /**
    * Quits the driver, closing every associated window, force the window to
    * close even with the Javascript popup
    * "are you sure you want to leave this window?".
    * 
    * @param force
    *           boolean, <li><code>true</code> if force the window to close with
    *           the "do you want to leave the screen" message.</li> <li>
    *           <code>false</code> if not force the window to close.</li>
    * 
    * @return <li><code>true</code> if the window was closed.</li> <li>
    *         <code>false</code> if the window was not closed.</li>
    */
   boolean close(boolean force);

   /**
    * Closes the currently opened window.
    * 
    * @return <li><code>true</code> if the window was closed.</li> <li>
    *         <code>false</code> if the window was not closed.</li>
    */
   boolean closeCurrentWindow();
   
   /**
    * Closes a UI window which is currently open.
    * 
    * @param uiMenuId
    *           The UI's unique menuId, can get from attribute "data-menuid"
    * 
    * @return <li><code>true</code> if the window was closed.</li> <li>
    *         <code>false</code> if the window was not closed.</li>
    */
   boolean closeUiWindow(String uiMenuId);
   
   /**
    * Checks to see if a location is visible.
    * 
    * @param locator
    *           The locator string can accept an XPath to the location or an
    *           HTML id.
    * @return <li><code>true</code> if the field was visible.</li> <li>
    *         <code>false</code> if the field was not visible.</li>
    * @throws org.openqa.selenium.NoSuchElementException
    *            if the element identified by the locator cannot be found
    */
   boolean isVisible(final String locator);

   /**
    * Checks to see if an element exists. It will return either true or false
    * 
    * @param locator
    *           The the element defined by locator string can be found in DOM.
    * @return <li><code>true</code> if the element was found.</li> <li>
    *         <code>false</code> if the element was not found.</li>
    */
   boolean exists(final String locator);
   
   /**
    * Checks to see if an element exists. It will return either true or false,
    * but not use implicit wait.
    * 
    * @param locator
    *           The the element defined by locator string can be found in DOM.
    * @return <li><code>true</code> if the element was found.</li> <li>
    *         <code>false</code> if the element was not found.</li>
    */
   boolean existsNoWait(final String locator);

   /**
    * Executes a JavaScript.
    * 
    * Note: This method is not inherited from {@link Browser} 
    * 
    * @param script
    *           The java script to be executed.
    * @return <li><code>True</code> if script was executed.</li> <li>
    *         <code>False</code> if script fail to execute.</li>
    * 
    */
   boolean executeJavaScript(final String script);

   /**
    * Executes a JavaScript and returns the value fetched by the JavaScript call
    * 
    * Note: This method is not inherited from {@link Browser} 
    * 
    * @param script
    *           The java script to be executed.
    *          
    * @return a string representing the value returned by the JavaScript call
    * 
    */
   String executeJavaScriptReturnString(final String script);
   
   /**
    * Click on an element, and wait for an element to disappear (not existing in
    * DOM).
    * 
    * Most commonly, this can be used for dialog boxes, where a
    * <code>save</code> or <code>cancel</code> button is clicked, and the dialog
    * box needs time to process before it disappears.
    * 
    * @param locatorClick
    *           The element to click.
    * @param locatorDie
    *           The element to wait for to disappear.
    * @return <li><code>true</code> if the button was clicked, and the waited
    *         element finally disappeared (not existing in DOM).</li> <li>
    *         <code>false</code> if the waited element did not disappear (not
    *         exist in DOM) eventually.</li>
    */
   boolean clickAndWaitForDie(final String locatorClick, final String locatorDie);

   /**
    * Click on an element, and wait for a larger timeout for an element to
    * disappear (not existing in DOM).
    * 
    * Most commonly, this can be used for dialog boxes, where a <code>save
    * </code> or <code>cancel</code> button is clicked, and the dialog box needs
    * time to process.
    * 
    * @param locatorClick
    *           The element to click.
    * @param locatorDie
    *           The element to wait for to disappear.
    * @return <li><code>true</code> if the button was clicked, and the waited
    *         element finally disappeared (not existing in DOM).</li> <li>
    *         <code>false</code> if the waited element did not disappear (not
    *         exist in DOM) eventually.</li>
    */
   boolean clickAndWaitForDieLargeTimeout(final String locatorClick, final String locatorDie);


   /**
    * Waits for a certain element to exist and be visible after a click event.
    * 
    * Useful for clicks which cause pop-ups, or clicks which start calculations.
    * 
    * This function will timeout after 30 seconds, and check for the element
    * every 1 second.
    * 
    * @param locator
    *           The location of the element to be clicked. The locator string
    *           can accept an XPath to the location or an HTML id.
    * @param element
    *           The location of the element the function should wait for. The
    *           locator string can accept an XPath to the location or an HTML
    *           id.
    * @return <li><code>true</code> if <code>locator</code> is clicked, and
    *         <code>element</code> was located.</li> <li><code>false</code> if
    *         <code>locator</code> or <code>element</code> could not be located.
    *         </li>
    */
   boolean clickAndWaitFor(final String locator, final String element);

   /**
    * Waits for a certain element to exist and be visible for an extended period
    * of time after a click event.
    * 
    * Useful for clicks which cause pop-ups, or clicks which start calculations.
    * 
    * This function will timeout after 30 seconds, and check for the element
    * every 1 second.
    * 
    * @param locator
    *           The location of the element to be clicked. The locator string
    *           can accept an XPath to the location or an HTML id.
    * @param element
    *           The location of the element the function should wait for. The
    *           locator string can accept an XPath to the location or an HTML
    *           id.
    * @return <li><code>true</code> if <code>locator</code> is clicked, and
    *         <code>element</code> was located.</li> <li><code>false</code> if
    *         <code>locator</code> or <code>element</code> could not be located.
    *         </li>
    */
   boolean clickAndWaitForLargeTimeout(final String locator, final String element);

   /**
    * Waits for elementOne to exist after a click event.
    * 
    * If elementOne doesn't exist, it checks if elementTwo exists and if it
    * does, click elementTwo and wait for elementOne again.
    * 
    * Useful for clicks which may cause a confirmation pop-ups and it is
    * necessary to click on this popup's button to continue waiting for the
    * original element.
    * 
    * @param locator
    *           The location of the element to be clicked. The locator string
    *           can accept an XPath to the location or an HTML id.
    * @param elementOne
    *           The location of the element the function should wait for. The
    *           locator string can accept an XPath to the location or an HTML
    *           id.
    * @param elementTwo
    *           The location of the element that may pop up before elementOne is
    *           visible. The locator string can accept an XPath to the location
    *           or an HTML id.
    * @return <li><code>True</code> if <code>locator</code> is clicked, and
    *         <code>elementOne</code> was located.</li> <li><code>False</code>
    *         if <code>locator</code> or <code>elementOne</code> could not be
    *         found.</li>
    */
   boolean clickAndWaitForEither(final String locator, final String elementOne, final String elementTwo);

   /**
    * This method is public using default timeout and interval.
    * 
    * Click an item, and wait for a window to come up. Once the new window is
    * up, check for an item located in the new window. Note that this function
    * switches focus to the new window once complete.
    * 
    * @param locator
    *           The location of the element to be clicked. The locator string
    *           can accept an XPath to the location or an HTML id.
    * @param window
    *           The window name that needs to switch to.
    * @param element
    *           The location of the element the function should wait for. The
    *           locator string can accept an XPath to the location or an HTML
    *           id.
    * @return <li><code>true</code> if <code>locator</code> is clicked, and
    *         <code>element</code> was located in the new window.</li> <li>
    *         <code>false</code> if <code>locator</code> or <code>element</code>
    *         could not be located or the new window did not load.</li>
    */
   boolean clickAndWaitForWindow(final String locator, final String window, final String element);

   /**
    * Click an element, and wait for a frame to show up or refresh. Once the new
    * frame is showing, wait for an element to exist in the new frame.
    * 
    * Note that this function switches focus to the new frame once complete.
    * 
    * @param locator
    *           The location of the element to be clicked. The locator string
    *           can accept an XPath to the location or an HTML id.
    * @param element
    *           The location of the element the function should wait for. The
    *           locator string can accept an XPath to the location or an HTML
    *           id.
    * @param elementFrame
    *           The Frame name where the element to wait for can be found. This
    *           frame is switched to and focused on after clicking the locator.
    * @return <li><code>true</code> if <code>locator</code> is clicked, and
    *         <code>element</code> was located in the new frame.</li> <li>
    *         <code>false</code> if <code>locator</code> or <code>element</code>
    *         could not be located or the new frame did not load.</li>
    */
   boolean clickAndWaitForFrame(final String locator, final String element, final String elementFrame);

   /**
    * Enters text at the specified location. The function will first clear the
    * previous value in the field.
    * 
    * It will also press "tab" key after it's finished.
    * 
    * @param locator
    *           The locator string can accept an xPath to the location or an
    *           HTML id.
    * @param value
    *           What is to be typed in the field.
    * @return <li><code>true</code> if the field was typed in, and the text was
    *         verified in the field.</li> <li><code>false</code> if the field
    *         was not found, or the field did not contain what was typed.</li>
    * @throws org.openqa.selenium.NoSuchElementException
    *            if unable to locate the element.
    */
   boolean type(final String locator, final String value);
   
   /**
    * Enters text at the specified location. The function will first clear the
    * previous value in the field.
    * Used when type() not working when clear() lose focus.
    * 
    * It will also press "tab" key after it's finished.
    * 
    * @param locator
    *           The locator string can accept an xPath to the location or an
    *           HTML id.
    * @param value
    *           What is to be typed in the field.
    * @return <li><code>true</code> if the field was typed in, and the text was
    *         verified in the field.</li> <li><code>false</code> if the field
    *         was not found, or the field did not contain what was typed.</li>
    * @throws org.openqa.selenium.NoSuchElementException
    *            if unable to locate the element.
    */
   boolean typeWithCtrlADel(final String locator, final String value);
   
   
   /**
    * Types the specified value into this widget. This action will clear any
    * existing value from this widget before typing in the new value.
    * Used for some special textbox for which type() not working because wait
    * for elementToBeClickable is not working, so bypass this wait condition
    * e.g.: the "page" text box for a table.
    * 
    * @param locator
    *           The locator string can accept an xPath to the location or an
    *           HTML id.
    * @param value
    *           What is to be typed in the field.
    * @return <li><code>true</code> if the field was typed in, and the text was
    *         verified in the field.</li> <li><code>false</code> if the field
    *         was not found, or the field did not contain what was typed.</li>
    * @throws org.openqa.selenium.NoSuchElementException
    *            if unable to locate the element.
    */
   boolean typeWithoutWaitForClicable(final String locator, final String value);
   
   /**
    * Enters text at the specified cell in a table. The function will first clear the
    * previous value in the field.
    * 
    * It will also press "tab" key after it's finished.
    * 
    * @param locator
    *           The locator string can accept an xPath to the location or an
    *           HTML id.
    * @param value
    *           What is to be typed in the field.
    * @return <li><code>true</code> if the field was typed in, and the text was
    *         verified in the field.</li> <li><code>false</code> if the field
    *         was not found, or the field did not contain what was typed.</li>
    * @throws org.openqa.selenium.NoSuchElementException
    *            if unable to locate the element.
    */
   boolean typeIntoCell(final String locator, final String value);

   
   /**
    * Enters text at the specified location. The function will first clear the
    * previous value in the field.
    * 
    * It may or may not do a tab press depending if the "tab" parameter is true
    * or false.
    * 
    * @param locator
    *           The locator string can accept an xPath to the location or an
    *           HTML id.
    * @param value
    *           What is to be typed in the field.
    * @param tab
    *           to do a tab or not
    * 
    * @return <li><code>true</code> if the field was typed in, and the text was
    *         verified in the field.</li> <li><code>false</code> if the field
    *         was not found, or the field did not contain what was typed.</li>
    * @throws org.openqa.selenium.NoSuchElementException
    *            if unable to locate the element.
    */
   boolean type(final String locator, final String value, final boolean tab);
   
   /**
    * Enters text at the specified location. The function will first clear the
    * previous value in the field.
    * 
    * @param locator
    *           The locator string can accept an xPath to the location or an
    *           HTML id.
    * @param value
    *           What is to be typed in the field.
    * 
    * @return <li><code>true</code> if the field was typed in, and the text was
    *         verified in the field.</li> <li><code>false</code> if the field
    *         was not found, or the field did not contain what was typed.</li>
    * @throws org.openqa.selenium.NoSuchElementException
    *            if unable to locate the element.
    */
   boolean typeByJavaScript(final String locator, final String value);
   
   /**
    * Enters text at the specified location. The function will first clear the
    * previous value in the field.
    * Used when type() not working for clear() lose focus.
    * 
    * It may or may not do a tab press depending if the "tab" parameter is true
    * or false.
    * 
    * @param locator
    *           The locator string can accept an xPath to the location or an
    *           HTML id.
    * @param value
    *           What is to be typed in the field.
    * @param tab
    *           to do a tab or not
    * 
    * @return <li><code>true</code> if the field was typed in, and the text was
    *         verified in the field.</li> <li><code>false</code> if the field
    *         was not found, or the field did not contain what was typed.</li>
    * @throws org.openqa.selenium.NoSuchElementException
    *            if unable to locate the element.
    */
   boolean typeWithCtrlADel(final String locator, final String value, final boolean tab);

   
   /**
    * Enters text at the specified cell in a table. The function will first clear the
    * previous value in the field.
    * 
    * It may or may not do a tab press depending if the "tab" parameter is true
    * or false.
    * 
    * @param locator
    *           The locator string can accept an xPath to the location or an
    *           HTML id.
    * @param value
    *           What is to be typed in the field.
    * @param tab
    *           to do a tab or not
    * 
    * @return <li><code>true</code> if the field was typed in, and the text was
    *         verified in the field.</li> <li><code>false</code> if the field
    *         was not found, or the field did not contain what was typed.</li>
    * @throws org.openqa.selenium.NoSuchElementException
    *            if unable to locate the element.
    */
   boolean typeIntoCell(final String locator, final String value, final boolean tab);
   
   
   /**
    * Types the specified value into this widget. This action will clear any
    * existing value from this widget before typing in the new value.
    * Used for some special textbox for which type() not working because wait
    * for elementToBeClickable is not working, so bypass this wait condition
    * e.g.: the "page" text box for a table.
    * 
    * @param locator
    *           The locator string can accept an xPath to the location or an
    *           HTML id.
    * @param value
    *           What is to be typed in the field.
    * @param tab
    *           to do a tab or not
    * @return <li><code>true</code> if the field was typed in, and the text was
    *         verified in the field.</li> <li><code>false</code> if the field
    *         was not found, or the field did not contain what was typed.</li>
    * @throws org.openqa.selenium.NoSuchElementException
    *            if unable to locate the element.
    */
   boolean typeWithoutWaitForClicable(final String locator, final String value, final boolean tab);

   /**
    * Clear the content from a text box.
    * 
    * 
    * @param locator
    *           The locator string can accept an xPath to the location or an
    *           HTML id.
    * @return <li><code>true</code> if the field was cleared.</li> <li>
    *         <code>false</code> if the field was not found, or the field did
    *         not contain what was typed.</li>
    */
   boolean clearText(final String locator);
   
   /**
    * Clear the content from a text box.
    * Use Ctrl+A and Delete to select the text and clear
    * Used when clear() is not working for lose focus 
    * 
    * @param locator
    *           The locator string can accept an xPath to the location or an
    *           HTML id.
    * @return <li><code>true</code> if the field was cleared.</li> <li>
    *         <code>false</code> if the field was not found, or the field did
    *         not contain what was typed.</li>
    */
   boolean clearByCtrlADelete(final String locator);


   /**
    * Enters text at the specified location. The function was created when
    * debugging the Q2O grid and the Discount Percent field in Q2O as the
    * clear() causes the focus to be lost. Just to a straight type of the input
    * value
    * 
    * Before revision 2185 the findElement(locator).clear() was never used as it
    * causes loss of focus and problems entering into the grid. Also, tried
    * putting similar code as prior to that revision but that doesn't work
    * either so the implementation below works for now.
    * 
    * It will also press "tab" key after it's finished.
    * 
    * @param locator
    *           The locator string can accept an xPath to the location or an
    *           HTML id.
    * @param value
    *           What is to be typed in the field.
    * @return <li><code>true</code> if the field was typed in, and the text was
    *         verified in the field.</li> <li><code>false</code> if the field
    *         was not found, or the field did not contain what was typed.</li>
    * @throws org.openqa.selenium.NoSuchElementException
    *            if unable to locate the element.
    */
   boolean typeWithoutClear(final String locator, final String value);
   
   /**
    * Types the specified value into this widget. This action will clear any
    * existing value from this widget before typing in the new value.
    * Telephone number is specially treated with format like (123)-456-7890,
    * the text validation need follow this final format shown on the screen.
    * 
    * It will also press "tab" key after it's finished.
    * 
    * @param locator
    *           The locator string can accept an xPath to the location or an
    *           HTML id.
    * @param value
    *           What is to be typed in the field.
    * @return <li><code>true</code> if the field was typed in, and the text was
    *         verified in the field.</li> <li><code>false</code> if the field
    *         was not found, or the field did not contain what was typed.</li>
    * @throws org.openqa.selenium.NoSuchElementException
    *            if unable to locate the element.
    */
   boolean typeTelNumWithoutClear(final String locator, final String value);

   /**
    * Enters text at the specified location. The function was created when
    * debugging the Q2O grid and the Discount Percent field in Q2O as the
    * clear() causes the focus to be lost. Just to a straight type of the input
    * value
    * 
    * Before revision 2185 the findElement(locator).clear() was never used as it
    * causes loss of focus and problems entering into the grid. Also, tried
    * putting similar code as prior to that revision but that doesn't work
    * either so the implementation below works for now.
    * 
    * It will also press "tab" key after it's finished.
    * 
    * @param locator
    *           The locator string can accept an xPath to the location or an
    *           HTML id.
    * @param value
    *           What is to be typed in the field.
    * @param doTab
    *           <li><code>true</code> to tab after inputting the text.</li> <li>
    *           <code>false</code> to not tab after inputting the text.</li>
    * @return <li><code>true</code> if the field was typed in, and the text was
    *         verified in the field.</li> <li><code>false</code> if the field
    *         was not found, or the field did not contain what was typed.</li>
    * @throws org.openqa.selenium.NoSuchElementException
    *            if unable to locate the element.
    */
   boolean typeWithoutClear(final String locator, final String value, final boolean doTab);
   
   /**
    * Types the specified value into this widget. This action will clear any
    * existing value from this widget before typing in the new value.
    * Telephone number is specially treated with format like (123)-456-7890,
    * the text validation need follow this final format shown on the screen.
    * 
    * It will also press "tab" key after it's finished.
    * 
    * @param locator
    *           The locator string can accept an xPath to the location or an
    *           HTML id.
    * @param value
    *           What is to be typed in the field.
    * @param doTab
    *           <li><code>true</code> to tab after inputting the text.</li> <li>
    *           <code>false</code> to not tab after inputting the text.</li>
    * @return <li><code>true</code> if the field was typed in, and the text was
    *         verified in the field.</li> <li><code>false</code> if the field
    *         was not found, or the field did not contain what was typed.</li>
    * @throws org.openqa.selenium.NoSuchElementException
    *            if unable to locate the element.
    */
   boolean typeTelNumWithoutClear(final String locator, final String value, final boolean doTab);

   /**
    * Take a screenshot of the current state of the browser session.
    * Typically, this will be called by a Junit rule for when a test
    * (that follows that rule) fails, then take a screen capture.
    * 
    * @return A string representing the name of the file
    *         that is the screen capture.
    */ 
   String takeScreenshot();
   
   /**
    * Gives the ability to enter special keys, like ALT or CTRL.
    * 
    * @param locator
    *           The locator string can accept an XPath to the location or an
    *           HTML id.
    * @param key
    *           special key
    * @return <li><code>true</code> if the field was found and the key was
    *         entered.</li> <li><code>false</code> if the field was not
    *         available.</li>
    * @throws org.openqa.selenium.NoSuchElementException
    *            if unable to locate the element.
    */
   boolean type(final String locator, final SpecialKey key);

   /**
    * Gives the ability to enter synchronized special key combinations, like
    * ALT+INSERT. Most use cases will be a combination of 2 keys, very few with
    * 3 keys and almost never for any greater number of keys.
    * 
    * @param locator
    *           The locator string can accept an XPath to the location or an
    *           HTML id.
    * @param keyCombo
    *           special key combination
    * @return <li><code>true</code> if the field was found and the key was
    *         entered.</li> <li><code>false</code> if the field was not
    *         available.</li>
    * @throws org.openqa.selenium.NoSuchElementException
    *            if unable to locate the element.
    */
   boolean type(final String locator, final SpecialKeyCombo keyCombo);

   /**
    * Retrieves text from a specified location. The method will first check to
    * make sure the location is visible.
    * 
    * @param locator
    *           The locator string can accept an XPath to the location or an
    *           HTML id.
    * @return the text from the specified location
    * @throws org.openqa.selenium.NoSuchElementException
    *            if unable to locate the text field.
    */
   String getText(final String locator);
   
   /**
    * Retrieves placeholder text from a specified textBox
    *  
    * @param locator
    *           The locator string can accept an XPath to the location or an
    *           HTML id.
    * @return the text from the specific textBox
    * @throws org.openqa.selenium.NoSuchElementException
    *            if unable to locate the text field.
    */
   String getPlaceHolderText(final String locator);

   /**
    * Validates if the element is selected (checked) or not.
    * 
    * Applies to check boxes and radio buttons.
    * 
    * @param locator
    *           The locator string can accept an XPath to the location or an
    *           HTML id.
    * @return <li><code>True</code> if the value is checked / selected.</li> <li>
    *         <code>False</code> if the value is not checked / selected</li>
    * @throws org.openqa.selenium.NoSuchElementException
    *            if unable to find the check box /radio button.
    */
   boolean isSelected(final String locator);

   /**
    * Get source of the current page.
    * 
    * @return The text that represented the source of the current page.
    */
   String getPageSource();

   /**
    * Select (Check) a radio button.
    * 
    * @param locator
    *           The locator string can accept an XPath to the location or an
    *           HTML id.
    * @return <li><code>true</code> if select was successful.</li> <li>
    *         <code>false</code> if select was unsuccessful.</li>
    */
   boolean selectRadioButton(final String locator);
   /**
    * Select (Check) a check box button. The method will first check to
    * make sure the location is visible and editable.
    * 
    * @param locator
    *           The locator string can accept an XPath to the location or an
    *           HTML id.
    * @return <li><code>true</code> if select was successful.</li> <li>
    *         <code>false</code> if select was unsuccessful.</li>
    */
   boolean selectCheckBox(final String locator);

   /**
    * Clear (uncheck) a check box. The method will first check to make sure the
    * location is visible and editable.
    * 
    * @param locator
    *           The locator string can accept an XPath to the location or an
    *           HTML id.
    * @return <li><code>true</code> if clear was successful.</li> <li>
    *         <code>false</code> if clear was unsuccessful.</li>
    */
   boolean clearCheckBox(final String locator);

   /**
    * Selects an option from a combobox.
    * 
    * @param selectLocator
    *           Location of the combobox. The locator string can accept an XPath
    *           to the location or an HTML id.
    * @param optionLocator
    *           The text to be selected as a label. For example:
    *           "label=Customer Number"
    * @return <li><code>true</code> if the the selection was successful.</li>
    *         <li><code>false</code> if the selection was not successful.</li>
    */
   boolean selectComboBox(final String selectLocator, final String optionLocator);

   /**
    * Selects options by text. So far, it appears that this works for List
    * selections, but not comboBoxes
    * 
    * @param selectLocator
    *           Location of the selection list.
    * @param textLocator
    *           The text to be selected.
    * @return <li><code>true</code> if the the selection was successful.</li>
    *         <li><code>false</code> if the selection was not successful.</li>
    */
   boolean selectFromList(final String selectLocator, final String textLocator);
   
   /**
    * deSelects options by text. 
    * 
    * @param selectLocator
    *           Location of the deselection list.
    * @param textLocator
    *           The text to be deselected.
    * @return <li><code>true</code> if the the deselection was successful.</li>
    *         <li><code>false</code> if the deselection was not successful.</li>
    */
   boolean deselectFromList(final String selectLocator, final String textLocator);

   /**
    * Remove all selections from the set of selected options in a multi-select
    * element using an element locator.
    * 
    * @param selectLocator
    *           an element locator identifying a multi-select box
    * @return <li><code>True </code>if all unselected</li> <li>
    *         <code>False</code> otherwise</li>
    */
   boolean removeAllSelections(final String selectLocator);

   /**
    * Select all selections from the set of selected options in a multi-select
    * element using an element locator.
    * 
    * @param selectLocator
    *           an element locator identifying a multi-select box
    * @return <li><code>True </code>if all selected.</li> <li>
    *         <code>False</code> otherwise.</li>
    */
   boolean selectAllSelections(final String selectLocator);
   
   /**
    * Helper method to find elements on a page. It will automatically determine
    * if the locator passed to it uses an xPath or HTML ID.
    * 
    * Designed for elements which have definite locator, if can not be found in up to 
    * predefined seconds, exception will be threw out
    * 
    * If the following functions become too slow, we will look at improving the
    * performances: 1. mouseOver(). 2. type().
    * 
    * @param locator
    *           xPath or HTML ID used to uniquely identify an element.
    * @return WebElement being tracked by the locator.
    * @throws org.openqa.selenium.NoSuchElementException
    *            if the element identified by the locator cannot be found
    */
    WebElement findElement(final String locator);
  

   /**
    * Returns a string array, containing all the available options for a comboBox.
    * 
    * @param selectLocator
    *           Location of the comboBox. The locator string can accept an XPath
    *           to the location or an HTML id.
    * @return a String array containing all the available options. If the
    *         comboBox cannot be located, or if there are no values, the return
    *         value is <code>null</code>.
    * @throws org.openqa.selenium.NoSuchElementException
    *            if unable to find combobox.
    */
   String[] getAllOptions(final String selectLocator);

   /**
    * Returns a string array, containing all the available options for a
    * listBox.
    * 
    * @param selectLocator
    *           Location of the listBox. The locator string can accept an XPath
    *           to the location or an HTML id.
    * @return a String array containing all the available options. If the
    *         listBox cannot be located, or if there are no values, the return
    *         value is <code>null</code>.
    * @throws org.openqa.selenium.NoSuchElementException
    *            if unable to find listBox.
    */
   String[] getAllOptionsFromListBox(final String selectLocator);
   
   /**
    * Returns a string array, containing all the selected options from a listBox
    * 
    * @param selectLocator
    *           Location of the list box. The locator string can accept an
    *           XPath to the location or an HTML id.
    * @return a String array containing all the selected options. If the list
    *         box cannot be located, or if there are no values, the return value
    *         is <code>null</code>.
    * @throws org.openqa.selenium.NoSuchElementException
    *            if unable to find list box.
    */
   String[] getSelectedOptionsFromListBox(final String selectLocator);

   /**
    * Compares the source of an HTML element with what's expected.
    * 
    * @param expectedSource
    *           the expected html path of the element to be verified.
    * @param locator
    *           the locator attribute of the element to be checked.
    * @return <li><code>true</code> if the actual source contains the expected
    *         source.</li> <li><code>false</code> if the actual source does not
    *         have the expected source.</li>
    * @throws org.openqa.selenium.NoSuchElementException
    *            if the element cannot be found.
    */
   boolean verifyElement(final String expectedSource, final String locator);

   /**
    * Retrieves the handle of the window most recently opened.
    * 
    * @return string containing the unique ID of the window.
    */
   String getCurrentWindowHandle();

   /**
    * Switch to the last window opened.
    * 
    * @return <li><code>true</code> if the window switch was done successfully.</li>
    *         <li><code>false</code> if the system failed to find or switch to
    *         the specified window.</li>
    */
   boolean switchToNewWindow();
   
   /**
    * Switch to the new opened crystal report viewer iFrame.
    * 
    * @return <li><code>true</code> if the iFrame switch was done successfully.</li>
    *         <li><code>false</code> if the system failed to find or switch to
    *         the specified iFrame.</li>
    */
   boolean switchToCrystalReportIFrameFromPortal();
   
   /**
    * Switch to a window specified by its handle provided.
    * 
    * @param windowHandle
    *           string containing the unique ID of the window.
    * @return <li><code>true</code> if the window switch was done successfully.</li>
    *         <li><code>false</code> if the system failed to find or switch to
    *         the specified window.</li>
    */
   boolean switchToWindow(final String windowHandle);

   /**
    * Switch to a specified frame by its name provided.
    * 
    * @param frameName
    *           name of the frame specified in the HTML tag.
    * @return <li><code>true</code> if the frame switch was done successfully.</li>
    *         <li><code>false</code> if the system failed to find or switch to
    *         the frame.</li>
    */
   boolean switchToFrame(final String frameName);

   /**
    * Selects a tab by clicking it.
    * 
    * @param tabLocator
    *           the tab's locator
    * @return <li><code>true</code> if a tab was selected successfully.</li> <li>
    *         <code>false</code> if selecting a tab failed.</li>
    */
   boolean selectTab(final String tabLocator);

   /**
    * Switches to the default content inside the window (default frame in most
    * case).
    * 
    * This would allow switching to a different frame in the same window.
    * 
    * This method will throw exceptions is the frame is not found.
    * 
    * @return <li><code>true</code> if switching to default was successful.</li>
    *         <li>
    *         <code>false</code> if the switching failed.</li>
    * @throws org.openqa.selenium.NoSuchFrameException
    *            if the default iframe is not found
    */
   boolean switchToDefaultContent();

   /**
    * Retrieve the title of the current window.
    * 
    * @return string with the current window title.
    */
   String getCurrentWindowTitle();

   /**
    * Switch to the original window.
    * 
    * This method would keep popping the window stack until finding the first
    * window handler and then switch to that window using the window handler.
    * 
    * @return <li><code>true</code> if the window switching was successful.</li>
    *         <li><code>false</code> if the window switching failed.</li>
    */
   boolean switchToDefaultWindow();

   /**
    * Maximizes the window to screen.
    * 
    * @return nothing
    */
   void maximizeWindow();

   /**
    * Gets the value of a CSS Property.
    * 
    * @param locator
    *           Element locator
    * @param property
    *           CSS Property which value will be returned
    * @return CSS Property value
    */
   String getCssProperty(final String locator, final String property);

   /**
    * Gets the attribute of a CSS Property.(this is temp replacement of above
    * method).
    * 
    * @param locator
    *           Element locator
    * @param property
    *           CSS Property which value will be returned
    * @return CSS Property value
    */
   String getAttribute(final String locator, final String property);

   /**
    * Verify if locator is editable or not. This locator cannot be a drop down
    * combo box.
    * 
    * A drop down combo box is enditable when it is visible and is NOT disabled,
    * but not necessarily have an attribute of "isContentEditable".
    * 
    * This function also tests if the element is visible, if not, it should not
    * be editable either.
    * 
    * @param locator
    *           locator xpath.
    * @return <li><code>true</code>if locator is editable</li> <li>
    *         <code>false</code>otherwise</li>
    */
   boolean isEditable(final String locator);

   /**
    * Verify if locator is disabled or not.
    * 
    * Note that this method will check if the attribute "disabled" exists for an
    * element indicated by the locator.
    * 
    * A drop box is enabled if and only if it is visible AND NOT disabled
    * (disabled as that it has the property 'disabled').
    * 
    * @param locator
    *           locator xpath.
    * @return <li><code>true</code>if locator is disabled</li> <li>
    *         <code>false</code>otherwise</li>
    */
   boolean isDisabled(final String locator);

   /**
    * Verify if a tab is disabled or not.
    * 
    * @param locator
    *           locator xpath.
    * @return <li><code>true</code>if locator is disabled</li> <li>
    *         <code>false</code>otherwise</li>
    * @throws org.openqa.selenium.NoSuchElementException
    *            if unable to locate the element.
    */
   boolean isDisabledForTab(final String locator);   
   /**
    * Verify if locator is disabled or not, specific for table row action menu
    * so far.
    * 
    * Note that this method will check if the attribute "class" contains the
    * text "disabled" for an element indicated by the locator.
    * 
    * A drop box is enabled if and only if it is visible AND NOT disabled
    * (disabled as that it has the property 'disabled').
    * 
    * @param locator
    *           locator xpath.
    * @return <li><code>true</code>if locator is disabled</li> <li>
    *         <code>false</code>otherwise</li>
    * @throws org.openqa.selenium.NoSuchElementException
    *            if unable to locate the element.
    */
   boolean isDisabledByClass(final String locator);

   /**
    * Checks if a window with name windowTitle exists.
    * 
    * @param windowTitle
    *           Title located in the browser tab
    * @return <li><code>True </code>if window with title windowTitle exist</li>
    *         <li><code>False </code>otherwise</li>
    */
   boolean isWindowTitleExist(final String windowTitle);

   /**
    * Scroll to the bottom of a table.
    * 
    * Uses the End key.
    * 
    * @param locator
    *           locator for a cell within a table.
    * @return <li><code>true</code> if the scroll was successful.</li> <li>
    *         <code>false</code> if the scroll failed.</li>
    */
   boolean scrollWithEnd(final String locator);

   /**
    * Scrolls vertically when passed a vertical scrollbar.
    * 
    * @param divID
    *           locator for the vertical scrollbar.
    * @return <li><code>true</code> if the script executed successfully.</li>
    *         <li><code>false</code> if the script failed to execute.</li>
    */
   boolean scrollVertical(final String divID);

   /**
    * Determines if the browser window has a scrollbar of the specified
    * orientation. Note: Uses javascript to do the verification.
    * 
    * @param orientation
    *           the scrollbar to check. Either <code>horizontal</code> or
    *           <code>vertical</code>\
    * 
    * @return <li><code>true</code> if the window has a horizontal scrollbar</li>
    *         <li><code>false</code> if the window does not have a horizontal
    *         scrollbar</li>
    */
   boolean isScrollbarPresent(final String orientation);

   /**
    * Scrolls one page down of a table.
    * 
    * Uses the Arrow Down key.
    * 
    * @param locator
    *           locator for a cell within a table.
    * @return <li><code>true</code> if the scroll was successful.</li> <li>
    *         <code>false</code> if the scroll failed.</li>
    */
   boolean scrollWithArrowDown(final String locator);

   /**
    * Scrolls one page down of a table.
    * 
    * Uses the Page Down key.
    * 
    * @param locator
    *           locator for a cell within a table.
    * @return <li><code>true</code> if the scroll was successful.</li> <li>
    *         <code>false</code> if the scroll failed.</li>
    */
   boolean scrollWithPageDown(final String locator);

   /**
    * Scrolls one page up of a table.
    * 
    * Uses the Page Up key.
    * 
    * @param locator
    *           locator for a cell within a table.
    * @return <li><code>true</code> if the scroll was successful.</li> <li>
    *         <code>false</code> if the scroll failed.</li>
    */
   boolean scrollWithPageUp(final String locator);

   /**
    * Scrolls to the right of a table.
    * 
    * Uses the Arrow Right key.
    * 
    * @param locator
    *           locator for a cell within a table.
    * @return <li><code>true</code> if the scroll was successful.</li> <li>
    *         <code>false</code> if the scroll failed.</li>
    */
   boolean scrollWithArrowRight(final String locator);

   /**
    * Scrolls to the left of a table.
    * 
    * Uses the Arrow Left key.
    * 
    * @param locator
    *           locator for a cell within a table.
    * @return <li><code>true</code> if the scroll was successful.</li> <li>
    *         <code>false</code> if the scroll failed.</li>
    */
   boolean scrollWithArrowLeft(final String locator);

   /**
    * Scroll to the beginning of a table.
    * 
    * Uses the Home key.
    * 
    * @param locator
    *           locator for a cell within a table.
    * @return <li><code>true</code> if the scroll was successful.</li> <li>
    *         <code>false</code> if the scroll failed.</li>
    */
   boolean scrollWithHome(final String locator);

   /**
    * Scrolls one page up of a table.
    * 
    * Uses the Arrow Up Key.
    * 
    * @param locator
    *           locator for a cell.
    * @return <li><code>true</code> if the scroll was successful.</li> <li>
    *         <code>false</code> if the scroll failed.</li>
    */
   boolean scrollWithArrowUp(final String locator);

   /**
    * Generic vertical scroll to the 0, pixelToScrollTo position.
    * 
    * @param pixelToScrollTo
    *           position to scroll to, in pixels
    * @return <li><code>true</code> if the scroll was successful.</li> <li>
    *         <code>false</code> if the scroll failed.</li>
    */
   boolean scrollWindowVertically(final int pixelToScrollTo);

   /**
    * Generic horizontal scroll to the pixelToScrollTo, 0 position.
    * 
    * @param pixelToScrollTo
    *           position to scroll to, in pixels
    * @return <li><code>true</code> if the scroll was successful.</li> <li>
    *         <code>false</code> if the scroll failed.</li>
    */
   boolean scrollWindowHorizontally(final int pixelToScrollTo);

   /**
    * Waits for an element to appear before proceeding.
    * 
    * @param locator
    *           locator of the element to wait for.
    * @return <li><code>true</code> if the item was found within the timeout.</li>
    *         <li><code>false</code> if the timeout was reached.</li>
    */
   boolean waitForElement(final String locator);

   /**
    * Waits for an element, that exists in a different window, to appear before
    * proceeding.
    * 
    * @param locator
    *           locator of the element to wait for.
    * @return <li><code>true</code> if the item was found within the timeout.</li>
    *         <li><code>false</code> if the timeout was reached.</li>
    */
   boolean waitForElementInDifferentWindow(final String locator, final String window);

   /**
    * Waits for an element to disappear before proceeding.
    * 
    * @param locator
    *           locator of the element to wait until gone.
    * @return <li><code>true</code> if the item was found within the timeout.</li>
    *         <li><code>false</code> if the timeout was reached.</li>
    */
   boolean waitForNoElement(final String locator);

   /**
    * 
    * @param locator
    *           locator of the element to get content from
    * @param content
    *           the expected content to be displayed by the element represented
    *           by locator
    * @return <li><code>true</code> if the content was displayed by the element
    *         within the timeout.</li> <li><code>false</code> if the timeout was
    *         reached.</li>
    */
   boolean waitForContent(final String locator, final String content);

   /**
    * Clears a field and validates its default value after clearing.
    * 
    * @param locator
    *           An element locator pointing to an input element.
    * @param defaultValue
    *           the default value of the field after clearing.
    * @return <li><code>True </code>if Cleared and Validated</li> <li>
    *         <code>False</code> otherwise</li>
    */
   boolean clearAndValidateField(final String locator, final String defaultValue);

   /**
    * Clicks an element.
    * 
    * Uses scroll and then clicks.
    * 
    * @param locator
    *           Locator for the UI elements that needs focus and clicked
    * @return <li><code>True </code>if clicking the element is successfully</li>
    *         <li>
    *         <code>False</code> otherwise</li>
    */
   boolean click(final String locator);

   /**
    * Clicks an element by sending speical key "RETURN"
    * As an alternative when click() is not reliable when clicking a button.
    * 
    * @param locator
    *           Locator for the UI elements that needs focus and clicked
    * @return <li><code>True </code>if clicking the element is successfully</li>
    *         <li>
    *         <code>False</code> otherwise</li>
    */
   boolean clickByReturn(final String locator);
   /**
    * Clicks an element and will do an Accpac Request check in the Fitnesse
    * tests. However, because our performance tests need to start a timer before
    * doing the checking, this method is to handle so we can just call click
    * without checking and then call the check separately.
    * 
    * This method is not exposed to Fitnesse tests, only click() that take one
    * parameter.
    * 
    * Uses scroll and then clicks.
    * 
    * @param locator
    *           Locator for the UI elements that needs focus and clicked
    * 
    * @param doAccpacRequestChecking
    *           Indicates whether or not to do Accpac Request checking to make
    *           the framework more reliable and consistent.
    * 
    * @return <li><code>True </code>if clicking the element is successfully</li>
    *         <li>
    *         <code>False</code> never. (this needs to be looked at)</li>
    */
   boolean click(final String locator, final boolean doAccpacRequestChecking);
   
   /**
    * Clicks an element by "RETRUN" and will do an Accpac Request check in the 
    * Fitnesse tests. However, because our performance tests need to start a 
    * timer before doing the checking, this method is to handle so we can just 
    * call clickByReturn without checking and then call the check separately.
    * 
    * This method is not exposed to Fitnesse tests, only clickByReturn() that 
    * take one parameter.
    * 
    * @param locator
    *           Locator for the UI elements that needs focus and clicked
    * 
    * @param doAccpacRequestChecking
    *           Indicates whether or not to do Accpac Request checking to make
    *           the framework more reliable and consistent.
    * 
    * @return <li><code>True </code>if clicking the element is successfully</li>
    *         <li>
    *         <code>False</code> never. (this needs to be looked at)</li>
    */
   boolean clickByReturn(final String locator, final boolean doAccpacRequestChecking);
   
   /**
    * Clicks an element by javascripts.
    * Used when click() not working in some special case
    * E.g: when use click() to extend a cell with comboBox, it fails on Firefox
    * clickByJavaScript() is more stable in some speical case.
    * 
    * @param locator
    *           Locator for the UI elements that needs focus and clicked
    * 
    * @return <li><code>True </code>if clicking the element is successfully</li>
    *         <li>
    *         <code>False</code> never. (this needs to be looked at)</li>
    */
   boolean clickByJavaScript(final String locator);

   /**
    * Move the mouse to an element and hover over it.
    * 
    * @param locator
    *           Locator for the UI elements that to be hover over
    */
   void hover(final String locator);
   
   /**
    * Move the mouse over an element.
    * 
    * this method catches AWTException (used by Robot), prints it out, and
    * returns false.
    * 
    * NOTE: This method does not seem to provoke pop-up balloons for some
    * reason.
    * 
    * @param locator
    *           Locator for the UI elements that to be mouse over
    * @return <li><code>True </code>if execute successfully</li> <li>
    *         * <code>False</code> if Robot false to execute</li>
    */
   boolean mouseOver(final String locator);

   /**
    * Gets the settings from this browser.
    * 
    * @return the settings from this browser.
    */
   BrowserSettings getBrowserSettings();
   
   /**
    * returns the underlying web driver
    * 
    * @return the driver
    */
   WebDriver getDriver();

   /**
    * Counts the number of items in a table.
    * 
    * @param locator
    *           Locator for the table
    * @return the number of items in a table.
    */
   int getTableItemCount(final String locator);

   /**
    * Selects an item from a table using its name.
    * 
    * @param locator
    *           Locator for the table
    * @param itemName
    *           The name of the item to be selected
    * @return <li><code>True </code>if item is selected successfully</li> <li>
    *         * <code>False</code> if item is not selected</li>
    */
   boolean selectTableItemByName(final String locator, final String itemName);

   /**
    * Selects an item from a table using its index.
    * 
    * @param locator
    *           Locator for the table
    * @param index
    *           The index of the item to be selected. Indexes start at 1.
    * @return <li><code>True </code>if item is selected successfully</li> <li>
    *         * <code>False</code> if item is not selected</li>
    */
   boolean selectTableItemByIndex(final String locator, final int index);

   /**
    * Wait until all requests for Accpac have been fulfilled. This method will
    * help in the timing of the Selenium - Accpac integration and should reduce
    * the need for time delay pauses.
    * 
    * First check if the function exists, as it does not exist until the first
    * stateful request is made. If it does exist then make the call to check the
    * queue.
    */
   boolean waitForNoRequests();
   
   /**
    * Waits for a UI to be ready for use.
    * First wait until the spinner is gone, then wait for the validation element present.
    * 
    * @param locator
    *           Locator for the UI elements to wait for
    * @return Whether or not the ui is fully loaded and ready for use.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget.
    */   
   boolean waitForUiReady(String locator);
}
