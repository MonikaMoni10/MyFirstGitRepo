/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.uibase;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sonata.generic.automation.browser.Browser;
import com.sonata.generic.automation.library.TimeDelay;
import com.sonata.generic.automation.widgets.Button;
import com.sonata.generic.automation.widgets.PasswordTextBox;
import com.sonata.generic.automation.widgets.TextBox;

/**
 * The <code>WebPage</code> class is a generic name for a web page.
 * 
 */
public abstract class WebPage
{
   /**
    * User ID text box locator.
    */
   private static final String     USER_ID_TEXTBOX   = "AccpacSignonPage_userIDTextBox";

   /**
    * Password text box locator.
    */
   private static final String     PASSWORD_TEXTBOX  = "AccpacSignonPage_passwordTextBox";

   /**
    * Sign In button locator.
    */
   protected static final String   SIGN_IN_BUTTON    = "AccpacSignonPage_signinButton";

   /**
    * Elements for common error message dialog box. common id between
    * confirmation and error message dialog so renamed the constants to be
    * generic for all messages
    */
   protected static final String   MSG_WINDOW        = "MessageDialog";
   protected static final String   MSG_TEXT_LOCATOR  = "MessageDialog-messageLabel";
   protected static final String   MSG_OK_BUTTON     = "MessageDialog-okButton";
   protected static final String   MSG_YES_BUTTON    = "MessageDialog-yesButton";
   protected static final String   MSG_NO_BUTTON     = "MessageDialog-noButton";
   protected static final String   MSG_CANCEL_BUTTON = "MessageDialog-cancelButton";

   protected static final int       UIMENUID         = 0;
   protected static final int       UIIFRAME         = 1;
      
   private final Button            signInButton;
   private final TextBox           userTextBox;
   private final PasswordTextBox   passwordTextBox;

   private final WebPageProperties properties;

   private final Browser           browser;
   
   private String uiMenuid;
   private String iframe;
   private String homeUrl;

   /**
    * constructor
    * 
    * @param properties
    *           webpageproperties
    * @param browser
    *           current browser
    */
   public WebPage(final WebPageProperties properties, final Browser browser)
   {
      this.properties = properties;
      this.browser = browser;
      this.signInButton = new Button(SIGN_IN_BUTTON, browser);
      this.userTextBox = new TextBox(USER_ID_TEXTBOX, browser);
      this.passwordTextBox = new PasswordTextBox(PASSWORD_TEXTBOX, browser);
   }

   /**
    * returns the browser that was provided when the object was created.
    * 
    * @return the {@link Browser} that was passed to the contructor
    */
   public Browser getBrowser()
   {
      return browser;
   }
   
   /**
    * Opens the Portal home page at its URL
    * @param sageId
    *           The sageId used during login to Web app.
    * @param password
    *           The password for the sageId used for login.
    *           
    * @return <li><code>True </code>if portal home page was opened</li> <li>
    *         <code>False </code>otherwise</li>
    */
   public final boolean openPortal(final String sageId, final String password)
   {
      this.homeUrl = browser.signinToPortal(sageId, password);
      String url = properties.getURL(browser.getBrowserSettings().getTestMode());
      return this.getBrowser().getDriver().getCurrentUrl().contains(url);
   }



   /**
    * Sign in first, then open a ui by navigating through portal menus. 
    * After sign in to portal, use the full url of the ui to open it, like below.
    * https://columbus20nadev.sagedatacloud.com/048f1adc-bab1-42af-b189-427d201f214d/GL/Options
    * 
    * @param sageId
    *           The sageId used during login to Web app.
    * @param password
    *           The password for the sageId used for login.
    *           
    * @return <li><code>True </code>if the UI was opened</li> <li>
    *         <code>False </code>otherwise</li>
    */
   public final boolean openUiByUrl(final String sageId, final String password)
   {
      this.homeUrl = browser.signinToPortal(sageId, password);
         
      return this.openUiAfterSignByUrl();
   }
   
   /**
    * Sign in first, change session data, then open a ui by changing the url. 
    * 
    * @param sageId
    *           The sageId used during login to Web app.
    * @param password
    *           The password for the sageId used for login.
    * @param sessionDate
    *           The sessionDate is going to update to 
    *           
    * @return <li><code>True </code>if the UI was opened</li> <li>
    *         <code>False </code>otherwise</li>
    */
   public final boolean openUiByUrlWithSessionDate(final String sageId, final String password, final String sessionDate)
   {
      this.homeUrl = browser.signinToPortalWithSessionDate(sageId, password, sessionDate);
      return this.openUiAfterSignByUrl();   
   }
   
   /**
    * Opens a UI by changing the url after sign in. 
    * 
    * @param sageId
    *           The sageId used during login to Web app.
    * @param password
    *           The password for the sageId used for login.
    *           
    * @return <li><code>True </code>if the UI was opened</li> <li>
    *         <code>False </code>otherwise</li>
    */
   public final boolean openUiAfterSignByUrl()
   {         
      browser.openUiByFullUrl(this.getCompleteUrl());
   
      // If the validation Element is not found and in current url any "Error" is shown up, normally an unexpected error occurs 
      // Close unexpected Error page directly
      boolean result = browser.waitForUiReady(properties.getSignInValidationElement()); 
      if (!result  && browser.getDriver().getCurrentUrl().contains("Error"))
      {
         System.out.println("Unexpected error happens. This ui will be closed directly");
         getBrowser().close();
      }
      
      return result;
   }
   
   /**
    * Sign in first, then opens a UI by navigating through portal menus
    * @param sageId
    *           The sageId used during login to Web app.
    * @param password
    *           The password for the sageId used for login.
    *           
    * @return <li><code>True </code>if ui was successfully opened</li> <li>
    *         <code>False </code>otherwise</li>
    */
   public final boolean openUiByMenus(final String sageId, final String password)
   {
      this.homeUrl = browser.signinToPortal(sageId, password);  
      return this.openUiAfterSigninByMenu();
   }
   
   /**
    * Sign in first, update session date, then open a UI by navigating through portal menus
    * @param sageId
    *           The sageId used during login to Web app.
    * @param password
    *           The password for the sageId used for login.
    * @param sessionDate
    *           The sessionDate is going to update to          
    *           
    * @return <li><code>True </code>if ui was successfully opened</li> <li>
    *         <code>False </code>otherwise</li>
    */
   public final boolean openUiByMenusWithSessionDate(final String sageId, final String password, final String sessionDate)
   {
      this.homeUrl = browser.signinToPortalWithSessionDate(sageId, password, sessionDate);
      
      return this.openUiAfterSigninByMenu();
   }
   
   /**
    * Opens a UI by navigating through portal menus after sign in
    * The property has already be changed to the new layoutmap.
    *           
    * @return <li><code>True </code>if page was opened</li> <li>
    *         <code>False </code>otherwise</li>
    */
   public final boolean openUiAfterSigninByMenu()
   {
      String applicationfullname = properties.getApplicationfullname();
      String category = properties.getCategory();
      String uiName = properties.getUIName();
      String uiMenuName = properties.getUiMenuName();
      
      String comepletUrl = this.getCompleteUrl();
      browser.getDriver().switchTo().defaultContent();
      
      String[] uiInfo = browser.openSpecificUi(applicationfullname, category, uiName, uiMenuName, comepletUrl); 
      this.uiMenuid = uiInfo[UIMENUID];
      this.iframe = uiInfo[UIIFRAME];
      
      properties.setIFrame(this.iframe);

      return browser.waitForUiReady(properties.getSignInValidationElement());
   }
   
   /**
    * Opens the WebPage at its URL
    **/
   public final void openPage()
   {
      String url = properties.getURL(browser.getBrowserSettings().getTestMode());
      browser.openURLWithoutUrlValidation(url);
   }
   
   /**
    * Opens the WebPage at its URL
    * 
    * @return <li><code>True </code>if page was opened</li> <li>
    *         <code>False </code>otherwise</li>
    */
   public final boolean open()
   {
      String url = properties.getURL(browser.getBrowserSettings().getTestMode());
      browser.openURLWithoutUrlValidation(url);
      
      return browser.waitForElement(properties.getSignInValidationElement());
   }

   /**
    * Opens the WebPage at its URL
    * 
    * @param urlParameters
    *    additional text to add to the end of a URL
    * @return <li><code>True </code>if page was opened</li> <li>
    *         <code>False </code>otherwise</li>
    */
   public final boolean open(final String urlParameters)
   {
      return browser.openURLAndWaitFor(properties.getURL(browser.getBrowserSettings().getTestMode()) + urlParameters,
            SIGN_IN_BUTTON);
   } 
   
   /**
    * Get the complete url of a specified ui according to the configuration in layoutmap
    * 
    * First get the front fixed part including home ulr and tenant info according to current url
    * e.g.: https://columbus20nadev.sagedatacloud.com/048f1adc-bab1-42af-b189-427d201f214d/
    * then append it with sub url of the UI. like GL/Options
    *           
    * @return <li><code>completeUrl </code>UI complete url</li> <li>
    */
   private String getCompleteUrl()
   {
      String url = this.properties.getURL(browser.getBrowserSettings().getTestMode());
      String currentUrl = browser.getDriver().getCurrentUrl();
      
      String[] tmp = currentUrl.split("/");
      String completeUrl = tmp[0] + "//" + tmp[2] + "/" + tmp[3] + url;
      
      return completeUrl;
   }

   /**
    * Signs In once a WebPage URL has been opened.
    * 
    * It uses default values from the {@link DefaultUISettings} class and
    * today's date
    * 
    * @return <li><code>True </code>if sign in is success</li> <li>
    *         <code>False </code>otherwise</li>
    */
   public final boolean signIn()
   {
      DefaultUISettings settings = new DefaultUISettings();

      return signIn(settings.getDefaultUser(), settings.getDefaultPassword());
   }

   /**
    * Signs In using a UserID, Password.
    * 
    * @param userID
    *           UserId used for sign in
    * @param password
    *           Password used for sign in
    * @return <li><code>True </code>if sign in is success</li> <li>
    *         <code>False </code>otherwise</li>
    */
   public final boolean signIn(final String userID, final String password)
   {
      this.userTextBox.type(userID);

      this.passwordTextBox.type(password);

      return browser.clickAndWaitForEither(SIGN_IN_BUTTON, properties.getSignInValidationElement(),
        "");
   }

   /**
    * Sets the UserID field.
    * 
    * @param userID
    *           User id to be set
    * @return <li><code>True </code>if field is set</li> <li>
    *         <code>False </code>otherwise</li>
    */
   public final boolean setUserID(final String userID)
   {
      return this.userTextBox.type(userID);
   }

   /**
    * Sets the Password field.
    * 
    * @param password
    *           Password to be set
    * @return <li><code>True </code>if field is set</li> <li>
    *         <code>False </code>otherwise</li>
    */
   public final boolean setPassword(final String password)
   {
            return this.passwordTextBox.type(password);
   }

   /**
    * Verifies if the sign-in validation element is visible.
    * 
    * An element could exist in the DOM but not visible because of HTML
    * properties.
    * 
    * @return <li><code>true</code> if the sign-in validation element is visible
    *         <li><code>false</code> otherwise
    */
   public final boolean isSignInValidationElementVisible()
   {
      return browser.isVisible(properties.getSignInValidationElement());
   }

   /**
    * Verifies if the sign-in validation element exists in the DOM.
    * 
    * This verification is used in waitForSignInValidationElement method.
    * 
    * @return <li><code>true</code> if the sign-in validation element exists in
    *         DOM <li><code>false</code> otherwise
    */
   public final boolean doesSignInValidationElementExists()
   {
      return browser.exists(properties.getSignInValidationElement());
   }

   /**
    * Waits for the sign-in validation element until it exists in DOM or
    * timeout.
    * 
    * @return <li><code>true</code> if the test is successful <li>
    *         <code>false</code> otherwise
    */
   public final boolean waitForSignInValidationElement()
   {
      int timeOut = 0;
      int defaultTimeOut = TimeDelay.getTimeoutExtraMaximum();
      while (!doesSignInValidationElementExists() && timeOut <= defaultTimeOut)
      {
         timeOut++;
      }
      
      return (timeOut <= defaultTimeOut) ? true: false;
   }

   /**
    * Get the SignIn Button Widget
    * 
    * @return <code>Button</code> Sign In Button Object
    */
   public final Button getSignInButton()
   {
      return this.signInButton;
   }
   
   /**
    * Close the opened Ui window from portal
    * 
    * @param uiWindowId
    *        the id of <div> which hold the window close icon   
    *           
    * @return <li><code>True </code>if ui was closed</li> <li>
    *         <code>False </code>otherwise</li>
    */
   public final boolean closeUiWindow(String uiWindowId)
   {   
      return browser.closeUiWindow(uiWindowId);
   }
}
