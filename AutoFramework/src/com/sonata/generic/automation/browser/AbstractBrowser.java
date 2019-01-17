/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */
package com.sonata.generic.automation.browser;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sonata.generic.automation.library.TimeDelay;

/**
 * The <code>AbstractBrowser</code> implements the core of the {@link Browser}
 * interface
 */
abstract class AbstractBrowser implements Browser
{
   /**
    * The instance of the test session.
    */
   private final WebDriver       driver;
   private final String          browserType;
   private final BrowserSettings settings;
   private final WebDriverWait   wait;
   
   private String                iframe;
   private String                homeUrl;
   private String                mainWindow;


   /**
    * constructs an instance of the class {link AbstractBrowser} based on the
    * given settings
    * 
    * @param settings
    *           the settings to apply
    * @param driver
    *           the driver to use
    * @param bannerSize
    *           the size of the banner
    */
   protected AbstractBrowser(String browserType, BrowserSettings settings, WebDriver driver, int bannerSize)
   {
      if (settings == null)
         throw new IllegalArgumentException("The browser settings must be supplied");
      this.settings = settings;
      this.driver = driver;
      this.browserType = browserType;
      this.wait = new WebDriverWait(getDriver(), 20);
      this.iframe = null;
   }

   /**
    * returns the underlying web driver
    * 
    * @return the driver
    */
   public WebDriver getDriver()
   {
      return driver;
   }


   @Override
   public BrowserSettings getBrowserSettings()
   {
      return this.settings;
   }

   /** 
    * Gets the browser version in the event we need it for logging purposes.
    * 
    * Note: This method is not inherited from {@link Browser} 
    */
   private String getBrowserVersion()
   {
	   return executeJavaScriptReturnString("return navigator.appVersion");
   }
   
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
   public boolean executeJavaScript(final String script)
   {
	  return Boolean.parseBoolean(((JavascriptExecutor)getDriver()).executeScript(script).toString());
   }

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
   public String executeJavaScriptReturnString(final String script)
   {	   
	   return (String)(((JavascriptExecutor)getDriver()).executeScript(script));
   }    
   
   /**
    * Opens a Web Page URL. This method will block until the page is loaded
    * based on mechanism used in the get() method.
    * 
    * This method automatically applies the server name. For example, if in
    * settings.java, SERVER_NAME is set to "localhost" and the parameter for url
    * is "SageERPAccpac/portal60a/portal.html", this method will open the web
    * page: http://localhost/SageERPAccpac
    * 
    * Note: This method is not inherited from {@link Browser} 
    * 
    * @param url
    *           Adds this string to the browserURL specified in the Connect()
    * @return <li><code>True</code> if the web page is loaded and the url is
    *         correct.</li> <li>
    *         <code>False</code> if the web page is loaded with incorrect url.</li>
    */
   private boolean openURL(final String url)
   {
      /* 
       * openURL would return a false when the URL is using port 80 because the browser 
       * will take off the port while the stored URL (completeURL) still refers to the URL with the port.
       * So first remove ":80" for the completeUrl. 
      */
      String completeUrl = settings.getBaseURL().replace(":80", "") + url;

      getDriver().get(completeUrl);
      int timeOut = 0;
      while (timeOut <= settings.getSmallTimeOut())
      {
         if (getDriver().getCurrentUrl().equalsIgnoreCase(completeUrl))
         {              
            return true;
         }
         else
         {
            TimeDelay.doPause(10);
            timeOut += 10;
         }
      }  
      return false;

   }
   
   /**
    * Opens a Web Page URL. This method will block until the page is loaded
    * based on mechanism used in the get() method.
    * 
    * This method automatically applies the server name. For example, if in
    * settings.java, SERVER_NAME is set to "localhost" and the parameter for url
    * is "SageERPAccpac/portal60a/portal.html", this method will open the web
    * page: http://localhost/SageERPAccpac
    * 
    * Note: This method is not inherited from {@link Browser} 
    * 
    * @param url
    *           Adds this string to the browserURL specified in the Connect()
    */
   public void openURLWithoutUrlValidation(final String url)
   {
      /* 
       * openURL would return a false when the URL is using port 80 because the browser 
       * will take off the port while the stored URL (completeURL) still refers to the URL with the port.
       * So first remove ":80" for the completeUrl. 
      */
      String completeUrl = settings.getBaseURL().replace(":80", "") + url;
      getDriver().get(completeUrl);
   }
   
   
   
   /**
    * Opens a UI by its full Url.
    * 
    * @param url
    *           Adds this string to the browserURL specified in the Connect()
    */
   public boolean openUiByFullUrl(String completeUrl)
   {
      getDriver().get(completeUrl);
      int timeOut = 0;
      while (timeOut <= settings.getSmallTimeOut())
      {
         if (getDriver().getCurrentUrl().equalsIgnoreCase(completeUrl))
         {              
            return true;
         }
         else
         {
            TimeDelay.doPause(10);
            timeOut += 10;
         }
      }  
      return false;
   }
   
   /** 
    * sign in to the application portal home page  
    * 
    * @param sageId
    *           The sageId for login
    * @param password
    *           The password of SageId for login                   
    * @return 
    *         <li><code>home url after sign in</code> </li> <li>     * 
   */
   public String signinToPortal(final String sageId, final String password)
   {
      String baseUrl = settings.getBaseURL().replace(":80", "");
      getDriver().get(baseUrl);
      
      WebDriverWait wait_login = new WebDriverWait(this.getDriver(), 30);                  
      try
      {
         wait_login.until(ExpectedConditions.elementToBeClickable(By.id("sso_Email")));
      }
      catch(Exception e)
      {
         System.out.println("Can't access sageId login page. Check if the url is correct. This ui will be closed directly");
         e.printStackTrace();
         this.close();
      }

      this.type("sso_Email", sageId);
      this.type("sso_Password", password);
      this.click("//input[@value='Sign In']");
            
      try
      {  
         wait.until(ExpectedConditions.elementToBeClickable(this.findElement("//ul[@id='menu']/li[1]/span")));   
      }
      catch(Exception e)
      {
         System.out.println("UI menu not show up, Maybe Unexpected error happens. This ui will be closed directly");
         e.printStackTrace();
         this.close();
      }
      
      return this.getDriver().getCurrentUrl();
   }
   
   /** 
    * sign in to the application portal home page, do session date update.
    * 
    * @param sageId
    *           The sageId for login
    * @param password
    *           The password of SageId for login
    * @param sessionDate
    *           The sessionDate to be updated after login.                   
    * @return 
    *         <li><code>home url after sign in</code> </li> <li>     * 
   */
   public String signinToPortalWithSessionDate(final String sageId, final String password, final String sessionDate)
   {
      this.signinToPortal(sageId, password);
      this.click("lnkEdit");
      this.type("datePicker", sessionDate);
      return this.getDriver().getCurrentUrl();
   }
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
    *           a full url with tenant, application and ui name
    * @return 
    *         <li><code>UI unique info: data-menuid and iframe</code> </li> <li> 
   */
   
   public String[] openSpecificUi(final String applicationfullname, final String category, 
         final String uiName, final String uiMenuName, final String completeUrl)
   {            
      String firstLevelMenuLocator = this.extendFirstLevelMenu(applicationfullname);
      if(firstLevelMenuLocator != null)
      {
         String secondLevelMenuLocator = this.extendSecondLevelMenu(firstLevelMenuLocator, category);
         if(secondLevelMenuLocator != null)
         {            
            // Before switch to the UI iframe, remember the mainWindow to switch back later
            this.mainWindow = this.getDriver().getWindowHandle();
            
            String uiMenuid = this.clickThirdLevelMenu(secondLevelMenuLocator, uiMenuName);
            String tenantAndUi = this.extractTenantAndUiFromFullUrl(completeUrl);
            
            // Wait until the iframe is displayed.
            this.waitForElement("//div[@id='screenLayout']/iframe[@src='" + tenantAndUi + "']" ); 
            String iframe = this.getIFrame(completeUrl);

            this.getDriver().switchTo().frame(findElement(iframe));            
            return new String[] {uiMenuid, iframe};
         }
         return null;
      }
      return null;
   }
   
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
   public String navigateToUi(final String applicationfullname, final String category, final String uiMenuName)
   {            

      String firstLevelMenuLocator = this.extendFirstLevelMenu(applicationfullname);

      if(firstLevelMenuLocator != null)
      {
         String secondLevelMenuLocator = this.extendSecondLevelMenu(firstLevelMenuLocator, category);
         if(secondLevelMenuLocator != null)
         {            
            // Before switch to the UI iframe, remember the mainWindow to switch back later
            this.mainWindow = this.getDriver().getWindowHandle();   
            this.waitForElement(secondLevelMenuLocator + "/div");
            
            return this.getUiLocatorInThridLevelMenu(secondLevelMenuLocator, uiMenuName);
         }
         return null;
      }
      return null;
   }
   
   
   /**
    * Extract the tenant and ui info from a complete Url.
    * a complete Ulr: https://columbus20nadev.sagenephos.com/DevTenant4/GL/BatchList
    * TenantAndUi: /DevTenant4/GL/BatchList
    * 
    * @param completeUrl
    *           a full url with tenant, application and ui name
    *           
    * @return 
    *         return the tenant and ui part present in a full url
    */
   private String extractTenantAndUiFromFullUrl(String completeUrl)
   {
      String[] tmp = completeUrl.split("/");
      return "/" + tmp[3] + "/" + tmp[4] + "/" + tmp[5];
   }

   /** 
    * find the first level menu per application and extended it
    * 
    * @param applicationfullname
    *           get from layoutmap ui.applicationfullname
    * @return 
    *         return the xpath locator of the first level Menu with text value set as application
   */
   private String extendFirstLevelMenu(final String applicationfullname)
   {  
      int firstLevelIdx = 1;
      String firstLevelMenuLocator = "//ul[@id='menu']/li[" + firstLevelIdx + "]"; 
      
      wait.until(ExpectedConditions.elementToBeClickable(this.findElement(firstLevelMenuLocator + "/span")));

      while(this.existsNoWait(firstLevelMenuLocator))
      {      
         if (this.getAttribute(firstLevelMenuLocator+"/span", "innerHTML").trim().equals(applicationfullname)) 
         {
            int i = firstLevelIdx-1;
            
            // Before when we use Kendo for portal menus, below way works stably 
            // After Dev discard kendo and use other way to implement the menus, it stops working
            // Selemium.moveToElement is not stable, sometimes it's blinking away for the first level menu
            // And it will stuck in second level menu and no items can be extended though the cursor is move the correct category
            // jquery.mousemover() does not work at all for all the levels .(tried mouseover(), hoven(), mouseenter())
            
            /*
            //if(this.browserType.equals("INTERNET_EXPLORER"))
            {
               Actions action = new Actions(getDriver());
               WebElement menu = this.findElement(firstLevelMenuLocator + "/span");          
               action.moveToElement(menu).build().perform();
            }
            else
            {
               String jquery = "$(\"ul#menu > li:eq(" + i  + ") > span\").mouseover();";
               System.out.println("jquery: " + jquery);
               executeJavaScriptReturnString(jquery);
            }
            */
            
            String jquery = "$(\"ul#menu > li:eq(" + i  + ") .std-menu\").show()";
            executeJavaScriptReturnString(jquery);
            
            return firstLevelMenuLocator;
         }
         firstLevelIdx++;
         firstLevelMenuLocator = "//ul[@id='menu']/li[" + firstLevelIdx + "]";
      }
      return null;
   }

   /** 
    * find the second level menu per category and extended it
    * 
    * @param firstLevelMenuLocator
    *           the xpath locator of the first level Menu
    * @param category
    *           get from layoutmap ui.category
    * @return 
    *         return the xpath locator of the second level Menu with text value set as category
   */
   private String extendSecondLevelMenu(final String firstLevelMenuLocator, final String category)
   {      
      int firstLevelIdx = Integer.parseInt(firstLevelMenuLocator.substring(firstLevelMenuLocator.length()-2, firstLevelMenuLocator.length()-1)) -1;
      
      int secondLevelIdx = 1;
      String secondLevelMenuLocator = firstLevelMenuLocator + "/ul/li[" + secondLevelIdx + "]"; 
      
      wait.until(ExpectedConditions.visibilityOf(this.findElement(secondLevelMenuLocator + "/span")));
      
      while(this.existsNoWait(secondLevelMenuLocator))
      {      
         int i = secondLevelIdx -1;
         
         // For some special items, they have another nested <span>, need remove it.
         String innnerHtml[] = this.getAttribute(secondLevelMenuLocator + "/span", "innerHTML").trim().split("<span");         
         if (innnerHtml[0].trim().equals(category)) 
         {
            
            // Before when we use Kendo for portal menus, below way works stably 
            // After Dev discard kendo and use other way to implement the menus, it stops working
            // Selemium.moveToElement is not stable, sometimes it's blinking away for the first level menu
            // And it will stuck in second level menu and no items can be extended though the cursor is move the correct category
            // jquery.mousemover() does not work at all for all the levels .(tried mouseover(), hoven(), mouseenter())
            /*
            if(this.browserType.equals("INTERNET_EXPLORER"))
            {
               Actions action = new Actions(getDriver());
               WebElement menu = this.findElement(secondLevelMenuLocator + "/span");          
               action.moveToElement(menu).build().perform();
            }
            else
            {
               String jquery = "$(\"ul#menu > li:eq(" + firstLevelIdx  + ") > ul > li:eq(" + i + ") > span\").mouseover();";
               System.out.println("jquery: " + jquery);
               executeJavaScriptReturnString(jquery);
            }
            */
            
            // As a workaround, use jquery to force the menu get active and show up 
            String jquery = "$(\"ul#menu > li:eq(" + firstLevelIdx  + ") > ul > li:eq(" + i + ") > span\").addClass(\"active\").next().show();";
            executeJavaScriptReturnString(jquery);
         
            return secondLevelMenuLocator;
         }
         secondLevelIdx++;
         secondLevelMenuLocator = firstLevelMenuLocator + "/ul/li[" + secondLevelIdx + "]";
      }
      return null;
   }

   /** 
    * find the third(final) level menu per uiName and click it
    * 
    * @param secondLevelMenuLocator
    *           the xpath locator of the second level Menu
    * @param uiMenuName
    *           get from layoutmap ui.menuName
    * @return 
    *         <li><code>data-menuid</code> if the menu with text value set as uiName is found,
    *         then click it</li><li> 
    *         <code>null</code> can't find the menu with text value set as uiName</li>
   */
   private String clickThirdLevelMenu(final String secondLevelMenuLocator, final String uiMenuName)
   {
      // the thridLevelIdx start from 2 since the first one is just a title without <a> 
      int thirdLevelIdx = 2;
      String thirdLevelMenuLocator = null;
      
      //this.waitForElement(secondLevelMenuLocator + "/div");
      
      thirdLevelMenuLocator = secondLevelMenuLocator + "/div/div/ul/li[" + thirdLevelIdx + "]/a"; 
      
      wait.until(ExpectedConditions.elementToBeClickable(this.findElement(thirdLevelMenuLocator)));
      
      while(this.existsNoWait(thirdLevelMenuLocator))
      {      
         if (this.getAttribute(thirdLevelMenuLocator, "innerHTML").trim().equals(uiMenuName)) 
         {            
            this.clickByJavaScript(thirdLevelMenuLocator);
            
            // Need explicitly hind the menu or else it will always there 
            // Because the menu is forced to be shown up by jquery instead of using morally trigger like mouseover() 
            String jquery = "$(\"ul#menu > li .std-menu\").css(\"display\", \"\")";
            ((JavascriptExecutor)this.getDriver()).executeScript(jquery);
            
            // Sometimes the screen validation element show too fast even before the menu disappear
            // At this point if try to click some element, the element might be still hidden behind the menu
            // to avoid this happen, wait for 500ms to make sure ui actions happens after the menu is gone.
            TimeDelay.doPause(500);
            
            return this.getAttribute(thirdLevelMenuLocator, "data-menuid").trim();
         }
         thirdLevelIdx++;
         // bypass the sub-heading item
         if(this.getAttribute(secondLevelMenuLocator + "/div/div/ul/li[" + thirdLevelIdx + "]", "class").equals("sub-heading")) 
            thirdLevelIdx++;
          thirdLevelMenuLocator = secondLevelMenuLocator + "/div/div/ul/li[" + thirdLevelIdx + "]/a";
      }
      return null;
   }
   
   /** 
    * Find the third(final) level menu per uiName
    * 
    * @param secondLevelMenuLocator
    *           the xpath locator of the second level Menu
    * @param uiMenuName
    *           get from layoutmap ui.menuName
    * @return 
    *         <li><code>thirdLevelMenuLocator</code> if the menu with text value set as uiMenuName is found,
    *         <code>null</code> can't find the menu with text value set as uiMenuName</li>
   */
   private String getUiLocatorInThridLevelMenu(final String secondLevelMenuLocator, final String uiMenuName)
   {
      // the thridLevelIdx start from 2 since the first one is just a title without <a> 
      int thirdLevelIdx = 2;
      String thirdLevelMenuLocator = null;
      
      thirdLevelMenuLocator = secondLevelMenuLocator + "/div/div/ul/li[" + thirdLevelIdx + "]/a"; 
      
      wait.until(ExpectedConditions.elementToBeClickable(this.findElement(thirdLevelMenuLocator)));
      
      while(this.existsNoWait(thirdLevelMenuLocator))
      {      
         if (this.getAttribute(thirdLevelMenuLocator, "innerHTML").trim().equals(uiMenuName))            
            return thirdLevelMenuLocator;
         thirdLevelIdx++;
         
         // bypass the sub-heading item
         if(this.getAttribute(secondLevelMenuLocator + "/div/div/ul/li[" + thirdLevelIdx + "]", "class").equals("sub-heading")) 
            thirdLevelIdx++;
         
         thirdLevelMenuLocator = secondLevelMenuLocator + "/div/div/ul/li[" + thirdLevelIdx + "]/a";
      }
      return null;
   }

   /**
    * Get the iframe of an opened UI
    * 
    * @param completeUrl
    *           the full url of an UI, saved in src attribute
    * @return 
    *         <li><code>iframe</code> iframe of a UI when it's found against it's complete url</li><li>
    *         <code>null</code> can't find the iframe for the UI</li>
   */
   public String getIFrame(String completeUrl)
   {      
      int iframeIdx = 1;
      String iframeLocator = "//div[@id='screenLayout']/iframe[" + iframeIdx + "]"; 
      
      while(this.existsNoWait(iframeLocator))
      {      
         if (this.getAttribute(iframeLocator, "src").trim().equals(completeUrl))
         {
            this.iframe = this.getAttribute(iframeLocator, "id").trim();
            return this.iframe;
         }
         iframeIdx ++;
         iframeLocator = "//div[@id='screenLayout']/iframe[" + iframeIdx + "]";
      }
      return null;
   }
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
    * @return <li><code>True</code> if <code>url</code> was accessed, and
    *         <code>element</code> was located.</li> <li><code>False</code> if
    *         the <code>element</code> could not be located within the
    *         predefined Timeout period.</li>
    */
   public boolean openURLAndWaitFor(final String url, final String element)
   {
      int timeOut = 0;
      
      if (openURL(url))
      {
         while (timeOut <= settings.getSmallTimeOut())
         {
            if (exists(element))
            {            	
               maximizeWindow();
               return true;
            }
            else
            {
               TimeDelay.doPause(settings.getDefaultInterval());
               timeOut += settings.getDefaultInterval();
            }
         }
         return false;
      }
      return false;
   }


   /**
    * Selects a tab by clicking it.
    * 
    * @param tabLocator
    *           the tab's locator
    * @return <li><code>true</code> if a tab was selected successfully.</li> <li>
    *         <code>false</code> if selecting a tab failed.</li>
    */
   public final boolean selectTab(final String tabLocator)
   {
      return click(tabLocator);
   }

   /**
    * Quits this driver, closing every associated window.
    * 
    * @return <li><code>true</code> if the window was closed.</li> <li>
    *         <code>false</code> if the window was not closed.</li>
    */
   public boolean close()
   {
      getDriver().quit();
      return true;
   }

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
   public boolean close(boolean force)
   {
      if (force)
      {
    	  executeJavaScript("window.onbeforeunload = function(e){};");
      }
      return this.close();
   }

   /**
    * Closes the currently opened window.
    * 
    * @return <li><code>true</code> if the window was closed.</li> <li>
    *         <code>false</code> if the window was not closed.</li>
    */
   public boolean closeCurrentWindow()
   {      
      getDriver().close();
      return true;
   }
   
   /**
    * Closes a opened UI by it's window Id in <div>.
    * <div id="dviFramewMenu1" class="rcbox">
    * 
    * @param uiWindowId
    *           The UI's unique menuId, can get from attribute "data-menuid"
    * 
    * @return <li><code>true</code> if the window was closed.</li> <li>
    *         <code>false</code> if the window was not closed.</li>
    */
   public boolean closeUiWindow(String uiWindowId)
   {      
      //this.getDriver().switchTo().window(this.mainWindow); 
      this.getDriver().switchTo().defaultContent();
      
      // For IE, jquery.mouseover() not working, but selenium.moveToElement works
      if(this.browserType.equals("INTERNET_EXPLORER"))
      {
         Actions action = new Actions(getDriver());
         WebElement menu = this.findElement("//div[@id='draggable']/div[2]");          
         action.moveToElement(menu).click().build().perform();
      }
      // For Firefox and Chrome, use jquery.mouseOver can reliablely extended the logout menu.
      else
      {
         String jquery = "$(\"div#draggable > div:eq(1) > span\").mouseover();";
         ((JavascriptExecutor)this.getDriver()).executeScript(jquery);
      }
      
      String uiWindowLocator = "//div[@id='" + uiWindowId + "']/span[2]";   
      wait.until(ExpectedConditions.elementToBeClickable(this.findElement(uiWindowLocator)));
      this.findElement(uiWindowLocator).click();
      
      return waitForNoElement(uiWindowLocator);
   }

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
   public boolean isVisible(final String locator)
   {
      
      WebElement element = findElement(locator);
      ((JavascriptExecutor)getDriver()).executeScript("arguments[0].style.opacity=1", element);      
      return findElement(locator).isDisplayed();
   }

   /**
    * Checks to see if an element exists. It will return either true or false,
    * but will not throw an exception.
    * 
    * @param locator
    *           The the element defined by locator string can be found in DOM.
    * @return <li><code>true</code> if the element was found.</li> <li>
    *         <code>false</code> if the element was not found.</li>
    */
   public boolean exists(final String locator)
   {
      try
      {
         findElement(locator);
         return true;
      }
      catch(org.openqa.selenium.TimeoutException e)
      {
         return false;
      }
   }
   
   /**
    * Checks to see if an element exists without implicit waits. It will return 
    * either true or false.
    * 
    * @param locator
    *           The the element defined by locator string can be found in DOM.
    * @return <li><code>true</code> if the element was found.</li> <li>
    *         <code>false</code> if the element was not found.</li>
    */
   public boolean existsNoWait(final String locator)
   {
      try
      {
         findElementNoWait(locator);
         return true;
      }
      catch (org.openqa.selenium.NoSuchElementException e)
      {
         return false;
      }
   }

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
   public boolean clickAndWaitForDie(final String locatorClick, final String locatorDie)
   {
      return clickAndWaitForDie(locatorClick, locatorDie, settings.getDefaultTimeOut(), settings.getDefaultInterval());
   }

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
   public boolean clickAndWaitForDieLargeTimeout(final String locatorClick, final String locatorDie)
   {
      return clickAndWaitForDie(locatorClick, locatorDie, settings.getLargeTimeOut(), settings.getDefaultInterval());
   }

   /**
    * This is a private method that to be only used by other methods in current
    * class.
    * 
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
    * @param timeout
    *           The timeout in milliseconds
    * @param interval
    *           The default interval in milliseconds
    * @return <li><code>true</code> if the button was clicked, and the waited
    *         element finally disappeared (not existing in DOM).</li> <li>
    *         <code>false</code> if the waited element did not disappear (not
    *         exist in DOM) eventually.</li>
    */
   private boolean clickAndWaitForDie(final String locatorClick, final String locatorDie, final int timeout,
         final int interval)
   {
      if (click(locatorClick))
      {
         int count = 0;
         while (count <= timeout)
         {
            if (exists(locatorDie))
            {
               TimeDelay.doPause(interval);
               count += interval;
               continue;
            }
            else
            {
               return true;
            }
         }
      }
      return false;
   }


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
   public boolean clickAndWaitFor(final String locator, final String element)
   {
      return clickAndWaitFor(locator, element, settings.getDefaultTimeOut());
   }

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
   public boolean clickAndWaitForLargeTimeout(final String locator, final String element)
   {
      return clickAndWaitFor(locator, element, settings.getLargeTimeOut());
   }

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
   public boolean clickAndWaitForEither(final String locator, final String elementOne, final String elementTwo)
   {
      // click locator
      if (click(locator))
      {
         for (int index = 0; index <= settings.getDefaultTimeOut(); index += settings.getDefaultInterval())
         {
            // check if elementOne is visible
            if (exists(elementOne))
            {
               return true;
            }
            else
            {
               // if elementTwo is visible, click on it, then wait for
               // elementOne again.
               TimeDelay.doPause(settings.getDefaultInterval());
               if (exists(elementTwo))
               {
                  click(elementTwo);
               }
            }
         }
      }
      // if click locator failed or timed out, return false
      return false;
   }

   /**
    * This method is private, another public method will be calling this one
    * using default timeout and interval.
    * 
    * Click an element, and wait for a new window to open. Once the new window
    * is opened, check for an element located in the new window.
    * 
    * This method will catch the exceptions thrown when waiting for the windows.
    * 
    * Note that this function switches focus to the new window once complete.
    * 
    * @param locator
    *           The location of the element to be clicked. The locator string
    *           can accept an XPath to the location or an HTML id.
    * @param window
    *           The window handle that needs to switch to.
    * @param element
    *           The location of the element the function should wait for. The
    *           locator string can accept an XPath to the location or an HTML
    *           id.
    * @param timeout
    *           The amount of time to wait before giving up.
    * @return <li><code>true</code> if <code>locator</code> is clicked, and
    *         <code>element</code> was located in the new window.</li> <li>
    *         <code>false</code> if <code>locator</code> or <code>element</code>
    *         could not be located or the new window did not load.</li>
    * 
    */
   private boolean clickAndWaitForWindow(final String locator, final String window, final String element,
         final int timeout)
   {
      long endTime = System.currentTimeMillis() + timeout;

      if (click(locator))
      {
         for (;;)
         {
            try
            {
               driver.switchTo().window(window);
               for (;;)
               {
                  if (exists(element))
                  {
                     return true;
                  }
                  if (System.currentTimeMillis() > endTime)
                  {
                     return false;
                  }
               }
            }
            catch (org.openqa.selenium.NoSuchWindowException e)
            {
               //Suppress the No window exception because we are still waiting for it.
               //System.out.println("Waiting for window...");
            }
            if (System.currentTimeMillis() > endTime)
            {
               return false;
            }
         }
      }
      else
      {
         return false;
      }
   }

   /**
    * This method is public using default timeout and interval.
    * 
    * Click an element, and wait for a new window to open. Once the new window
    * is opened, check for an element located in the new window.
    * 
    * Note that this function switches focus to the new window once complete.
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
   public boolean clickAndWaitForWindow(final String locator, final String window, final String element)
   {
      return clickAndWaitForWindow(locator, window, element, settings.getDefaultTimeOut());
   }

   /**
    * Click an element, and wait for a frame to show up or refresh.
    * 
    * Once the new frame is showing, wait for an element to exist in the new
    * frame.
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
   public boolean clickAndWaitForFrame(final String locator, final String element, final String elementFrame)
   {
      if (click(locator))
      {
         try
         {
            switchToFrame(elementFrame);
            int timeOut = 0;
            while (timeOut <= settings.getDefaultTimeOut())
            {
               if (exists(element))
               {
                  return true;
               }
               else
               {
                  TimeDelay.doPause(settings.getDefaultInterval());
                  timeOut += settings.getDefaultInterval();
                  switchToFrame(elementFrame);
               }
            }
         }
         catch (org.openqa.selenium.NoSuchFrameException e)
         {
            //Suppress the No frame exception because we are still waiting for it.
            //System.out.println("Waiting for frame...");
         }
      }
      return false;
   }

     
   
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
   public boolean type(final String locator, final String value)
   {
      // by default the type will do a proceeding tab after the input
      return type(locator, value, true);
   }
   
   /**
    * Enters text at the specified location. The function will first clear the
    * previous value in the field.
    * Used when type() not working for clear() lose focus.
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
   public boolean typeWithCtrlADel(final String locator, final String value)
   {
      // by default the type will do a proceeding tab after the input
      return typeWithCtrlADel(locator, value, true);
   }
   
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
   public boolean typeIntoCell(final String locator, final String value)
   {
      // by default the type will do a proceeding tab after the input
      return typeIntoCell(locator, value, true);
   }
   
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
   public boolean typeWithoutWaitForClicable(final String locator, final String value)
   {
      // by default the type will do a proceeding tab after the input
      return typeWithoutWaitForClicable(locator, value, true);
   }

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
   public boolean type(final String locator, final String value, final boolean tab)
   {
      WebElement element = findElement(locator);
      //although findElement use implicit wait condition "presenceOfElementLocated"
      //it's possible that the element is not editable at this point
      //e.g.:gl2100: click("btnCreateBatch"), then type("batchDescription", "New Batch")) without explicit wait
      //error happens like: invalid element state: Element is not currently interactable and may not be manipulated
      //So add further implicit wait condition
      //no condition like elementToBeEditable, use elementToBeClickable as an alternative  
      wait.until(ExpectedConditions.elementToBeClickable(By.id(locator)));
      
      // 01/052015: element.clear() will lose focus for some textBox like portal->sessionDate
      // Change to use speical key "ctrl+A" and  then "Delete" to clear the existing text 
      element.sendKeys(Keys.chord(Keys.chord(Keys.CONTROL, "a")));
      element.sendKeys(SpecialKey.DELETE.getKeys());
      
      element.sendKeys(value);  
      boolean success = getText(locator).equalsIgnoreCase(value);        
      if (tab)
      {
         element.sendKeys(Keys.TAB);
      }
      
      return success;
   }
   
   /**
    * Enters text at the specified location. The function will first clear the
    * previous value in the field.
    * 
    * Used in special case when type() are not working. type() uses webdriver's 
    * clear() and sendKeys(), which sometimes not work well on FireFox and Chrome. 
    * An example is : gl2100-> type("00001") for BatchNumber -> Reverse...-> 
    * type("00003") for BatchNumber(id="Data_ReverseBatchId"), error happens at this
    * point, input value keep original "000000" instead of expected "00003".
    * Case: testGL2100_ReversePostedBatchesToPosted()
    * This function extended type by using JavaScript for some special case handling.
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
   public boolean typeByJavaScript(final String locator, final String value)
   {
      wait.until(ExpectedConditions.elementToBeClickable(By.id(locator)));
      
      // If just use javascript to set the value, the value can be set but no related event is triggered
      // Use jQuery to trigger the event on change
      String javascript = "$('input#" + locator + "').val('" + value + "').trigger('change');";
      this.executeJavaScriptReturnString(javascript);
   
      return getText(locator).equalsIgnoreCase(value);        
   }
   
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
   public boolean typeWithCtrlADel(final String locator, final String value, final boolean tab)
   {
      WebElement element = findElement(locator);

      wait.until(ExpectedConditions.elementToBeClickable(By.id(locator)));
      element.click(); //Needed for FIREFOX, but not mandatory for CHROME
      
      this.clearByCtrlADelete(locator);
      element.sendKeys(value);                  
      boolean success = getText(locator).equalsIgnoreCase(value);  
      
      if (tab)
      {
         element.sendKeys(Keys.TAB);
      }
      return success;
   }
   
   /**
    * Enters text at the specified cell location. The function will first clear the
    * previous value in the field.
    * 
    * Clear() of a textbox in a table causes loss of focus so it needs a different implementation
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
    * @return <li><code>true</code> if the field was typed in, and the text was
    *         verified in the field.</li> <li><code>false</code> if the field
    *         was not found, or the field did not contain what was typed.</li>
    * @throws org.openqa.selenium.NoSuchElementException
    *            if unable to locate the element.
    */
   public boolean typeIntoCell(final String locator, final String value, final boolean tab)
   {
      WebElement element = findElement(locator);
      boolean success = false;
      try
      {
         // Before input new value, need select existing value then delete 
         
         // Special case treatment: Number type cell
         // e.g.: GL/JournalEntry-> SourceDebit cell in JournalBatchDetailsGrid
         // Selecting original value: "Ctrl+A" not working, but "Shift+End" works
         // If the original value has decimal, like 100.00, "Shift+End" will only select the last two digits
         // Use "HOME" before "Shift+End" to force it focus at the beginning of the value so as 
         // to select the whole value
         
         if (locator.contains("input[2]") && element.getAttribute("data-role").equals("numerictextbox"))
         {
            element.sendKeys(SpecialKey.HOME.getKeys());
            List<Keys> keys = SpecialKeyCombo.SHIFT__END.getKeys();
            element.sendKeys(Keys.chord(keys.get(0), keys.get(1)));
         }
         // Special case treatment: Date type cell
         // e.g.: GL/JournalEntry-> Date cell in JournalBatchDetailsGrid
         // Selecting original value: "Ctrl+A", "Shift+End", "double click", all failed to select the whole value
         // because some special key is restricted to be input for this date type
         // As a workaround, use BACK_SPACE to delete each character one by one
         // Use "END: key to always make it focus at the end of value and get ready for "BACK_SPACE"
         if(locator.endsWith("/span/span/input"))
         {
            for(int i=1; i<11; i++)
            {
               element.sendKeys(SpecialKey.END.getKeys());
               element.sendKeys(SpecialKey.BACK_SPACE.getKeys());
            }
         }
         // For all other type of cell, use "Ctrl+A" to select the original value
         else
         {
            element.sendKeys(Keys.chord(Keys.chord(Keys.CONTROL, "a")));
         }
         // Delete the existing value
         element.sendKeys(SpecialKey.DELETE.getKeys());
         
         // Input new value
         element.sendKeys(value);     
         /*
         String inputBoxId = element.getAttribute("id");
         this.typeByJavaScript(inputBoxId, value);
         */
         
         // For cell with column title "No. of Days" in the grid of AR/TermsCode
         // If using element.sendKeys(value), the expected value is not input there and also the focus is lost.
         // After Changed to use typeByJavasScript(), "No. of Days" in AR/TermsCode can be successfully type with value.
         // But Some other problem happens like Cell WeightUnitsOfMeasure in IC/WeightUnitOfMeasure not working
         // Because after typeByJavasScript(), seems a tab is implicitly done so the cursor is focus to next cell
         // this will cause text verification failure. 
         // So just change back to orignal one. Need special treatment for "No. of Days" in the grid of AR/TermsCode
      }
      // When a textbox is disabled, WebDriverException with "can not focus element exception" would be caught 
      // Here return false, in test scripts using assertFalse to verify if a cell is not editable. 
      catch(org.openqa.selenium.WebDriverException e)
      {
         return false;
      }
      
      success = getText(locator).equalsIgnoreCase(value); 
      
      if (tab)
      {
         element.sendKeys(Keys.TAB);
      }
      
      return success;
   }
   
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
   public boolean typeWithoutWaitForClicable(final String locator, final String value, final boolean tab)
   {
      WebElement element = findElement(locator);
      element.click();
      element.sendKeys(Keys.chord(Keys.chord(Keys.CONTROL, "a")));
      element.sendKeys(SpecialKey.DELETE.getKeys());
      element.sendKeys(value);
      
      boolean success = getText(locator).equalsIgnoreCase(value); 

      if (tab)
      {
         element.sendKeys(Keys.TAB);
      }
      return success;
   }

   /**
    * Enters text at the specified location. In some cases we do not need to 
    * clear out the field first as it may not be possible or if this method
    * is used when selecting an option for a "drop down list".
    *  
    * The drop down list (the <select> element) of CNA2.0 does not work as a 
    * normal drop down list, one way to select is by keying in the value desired.
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
   public boolean typeWithoutClear(final String locator, final String value)
   {
      return typeWithoutClear(locator, value, true);
   }
   
   /**
    * Enters text at the specified location. In some cases we do not need to 
    * clear out the field first as it may not be possible or if this method
    * is used when selecting an option for a "drop down list".
    * 
    * Telephone number is specially treated with format like (123)-456-7890,
    * the text validation need follow this final format shown on the screen.
    *  
    * The drop down list (the <select> element) of CNA2.0 does not work as a 
    * normal drop down list, one way to select is by keying in the value desired.
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
   public boolean typeTelNumWithoutClear(final String locator, final String value)
   {
      return typeTelNumWithoutClear(locator, value, true);
   }

   /**
    * Enters or "selects" text at the specified location. In some cases we do not need to 
    * clear out the field first as it may not be possible or if this 
    * method is used when selecting an option for a "drop down list".
    *   
    * The drop down list (the <select> element) of CNA2.0 does not work as a 
    * normal drop down list, one way to select is by keying in the value desired.
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
   public boolean typeWithoutClear(final String locator, final String value, final boolean doTab)
   {
      findElement(locator).sendKeys(value);
      
      boolean success = getText(locator).equalsIgnoreCase(value); 
      
      if (doTab)
      {
         findElement(locator).sendKeys(Keys.TAB);
      }      
      
      return success;
   }
   
   /**
    * Enters or "selects" text at the specified location. In some cases we do not need to 
    * clear out the field first as it may not be possible or if this 
    * method is used when selecting an option for a "drop down list".
    * 
    * Telephone number is specially treated with format like (123)-456-7890,
    * the text validation need follow this final format shown on the screen.
    *  
    * The drop down list (the <select> element) of CNA2.0 does not work as a 
    * normal drop down list, one way to select is by keying in the value desired.
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
   public boolean typeTelNumWithoutClear(final String locator, final String value, final boolean doTab)
   {
      findElement(locator).sendKeys(value);
      
      if (doTab)
      {
         findElement(locator).sendKeys(Keys.TAB);
      }
      String expectedString = null;
      if(value.length()<=3)
      {
         expectedString = "(" + value;
      }
      else if(value.length()>3 && value.length() <= 6)
      {
         expectedString = "(" + value.substring(0, 3) + ") " + value.substring(3);
      }
      else if(value.length()>6)
      {
         expectedString = "(" + value.substring(0, 3) + ") " + value.substring(3, 6) + "-" + value.substring(6);
      }
      
      boolean success = getText(locator).equalsIgnoreCase(expectedString);       
      
      return success;
   }

   /**
    * Take a screenshot of the current state of the browser session.
    * Typically, this will be called by a Junit rule for when a test
    * (that follows that rule) fails, then take a screen capture.
    * 
    * @return A string representing the name of the file
    *         that is the screen capture.
    */   
   public String takeScreenshot()
   {    
      File scrFile = ((TakesScreenshot)getDriver()).getScreenshotAs(OutputType.FILE);
      
      try
      {
         Date date = new Date();
         DateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
         String newFile = "c:\\tmp\\screenshot" + df.format(date).toString() + ".png";
         FileUtils.copyFile(scrFile, new File(newFile));
         return newFile;
      }
      catch (IOException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
         return "";
      }
   }
   
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
   public boolean type(final String locator, final SpecialKey key)
   {
      findElement(locator).sendKeys(key.getKeys());
      return true;
   }

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
   public boolean type(final String locator, final SpecialKeyCombo keyCombo)
   {
      List<Keys> keys = keyCombo.getKeys();

      switch(keys.size())
      {
         case 2:
         {
            findElement(locator).sendKeys(Keys.chord(keys.get(0), keys.get(1)));
            break;
         }
         case 3:
         {
            findElement(locator).sendKeys(Keys.chord(keys.get(0), keys.get(1), keys.get(2)));
            break;
         }
         case 4:
         {
            findElement(locator).sendKeys(Keys.chord(keys.get(0), keys.get(1), keys.get(2), keys.get(3)));
            break;
         }
         default:
            break;
      }
      return true;
   }

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
    * @throws org.openqa.selenium.NoSuchElementException
    *            if unable to locate the text field.
    */
   public boolean clearText(final String locator)
   {
      findElement(locator).clear();
      return true;
   }
   
   /**
    * Clear the content from a text box.
    * Use Ctrl+A and Delete to select the text and Delete.
    * Used when clear() not working due to losing focus.
    * 
    * @param locator
    *           The locator string can accept an xPath to the location or an
    *           HTML id.
    * @return <li><code>true</code> if the field was cleared.</li> <li>
    *         <code>false</code> if the field was not found, or the field did
    *         not contain what was typed.</li>
    * @throws org.openqa.selenium.NoSuchElementException
    *            if unable to locate the text field.
    */
   public boolean clearByCtrlADelete(final String locator)
   {
      findElement(locator).sendKeys(Keys.chord(Keys.chord(Keys.CONTROL, "a")));
      findElement(locator).sendKeys(SpecialKey.DELETE.getKeys());
      return true;
   }

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
   public String getText(final String locator)
   {
      try
      {
         WebElement element = findElement(locator);

         //outerHTML not working when getting text on TextBoxes returns null
         //String attribute = findElement(locator).getAttribute("outerHTML");
         String attribute = element.getAttribute("innerHTML");

         // If the attribute isEmpty, then we are working with a textBox widget.
         // Else if the attribute starts with "<OPTION ", we are working with a comboBox widget.
         // Else anything else
         if (attribute.isEmpty())
         {
            // implemented at time of new first column of the table where the header is empty
            String valueAttribute = element.getAttribute("value");
            if (valueAttribute == null)
            {
               return "";
            }

            return valueAttribute.trim();
         }
         else if (attribute.toLowerCase().startsWith("<option "))
         {
            return new Select(element).getFirstSelectedOption().getText().trim();
         }
         else if (attribute.contains(">select<"))
         {
            // For a "select" element (drop down list in CNA2) trim off the extra "\nselect" from the innertext
            // of the span element.  Span element is used since the Select element is not interactable.
            return element.getText().replace("\nselect","").trim();
         }
         else if(locator.contains("/thead/"))
         {
            //getText return "" from thead="Source Credit"
            //Can't use getText to locate the thead correctly
            //So for a table's thead element, use the innerHTML
            return attribute.trim();
         }
         else
         {  //for a normal element, just use the getText() to retrieve the value
            return element.getText().trim();
         }
      }
      catch (org.openqa.selenium.NoSuchElementException e)
      {
         return "";
      }
   }
   
   /**
    * Retrieves placeholder text from a specified textBox like "Batch number" in 
    * "Batch List" and "Journal Entry" screen. 
    * 
    * @param locator
    *           The locator string can accept an XPath to the location or an
    *           HTML id.
    * @return the placeholder text from the specific textBox
    * @throws org.openqa.selenium.NoSuchElementException
    *            if unable to locate the text field.
    */
   public String getPlaceHolderText(final String locator)
   {
      WebElement element = findElement(locator);
      return element.getAttribute("placeholder");
   }


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
   public boolean isSelected(final String locator)
   {
      return findElement(locator).isSelected();
   }

   /**
    * Get source of the current page.
    * 
    * @return The text that represented the source of the current page.
    */
   public String getPageSource()
   {
      return getDriver().getPageSource();
   }

   /**
    * Select (Check) a radio button.
    * 
    * When use previous click() provided by selenium, tests fails on IE
    * e.g.: testGL2100_AddJournalEntry()-> fixture.select("normal")
    *       Previous click() works well for Firefox and Chrome, but not on IE
    * Changed to using clickByJavaScript(), it's more reliable for all browsers.
    * 
    * For radio button, don't check whether it's selected, just click it to
    * make sure it's clicked and selected.
    * 
    * Reason for split it from previous selectCheckBoxOrRadioButton():
    * In gl2100, if input "JE" for Source Code with the normal type() which use tab
    * after sendKeys, the real value will be changed to "J" or sometimes just empty.
    * If using typeNoTab(), the input "JE" will always be there and it's more stable,
    * But need to click some other element to move the mouse out of it.
    * Normally the next step is to select the "Normal" radio button, but by default 
    * "Normal" is already selected, so according to original selectCheckBoxOrRadioButton()
    * if it's already selected, no click action, so the focus is still stay at Source Code
    * Which result in the "Add Line" button keep in disabled, so later actions to the 
    * grid is hung there
    * 
    * As a workaround, for radio button, no matter the previous value is selected or not
    * Just click it, no results different, but it's helpful to move the focus out of the 
    * previous element.   
    *   
    * 
    * @param locator
    *           The locator string can accept an XPath to the location or an
    *           HTML id.
    * @return <li><code>true</code> if select was successful.</li> <li>
    *         <code>false</code> if select was unsuccessful.</li>
    * @throws org.openqa.selenium.NoSuchElementException
    *            if unable to find the check box /radio button.
    */
   public boolean selectRadioButton(final String locator)
   {        
      clickByJavaScript(locator);
      return isSelected(locator);
   }
   
   /**
    * Originally it's named as selectCheckBoxOrRadioButton()
    * Since selectRadioButton is separated from it, just rename it to
    * make it just for check box
    * 
    * Select (Check) a check box button.
    * 
    * The method will first check to make sure the location is visible and
    * editable.
    * 
    * @param locator
    *           The locator string can accept an XPath to the location or an
    *           HTML id.
    * @return <li><code>true</code> if select was successful.</li> <li>
    *         <code>false</code> if select was unsuccessful.</li>
    * @throws org.openqa.selenium.NoSuchElementException
    *            if unable to find the check box /radio button.
    */
   public boolean selectCheckBox(final String locator)
   {

      if (!isSelected(locator))
      {         
         // On IE, "click(locator)" not working for check box;
         this.clickByJavaScript(locator);
      }
      return isSelected(locator);
   }

   /**
    * Clear (uncheck) a check box.
    * 
    * The method will first check to make sure the location is visible and
    * editable.
    * 
    * @param locator
    *           The locator string can accept an XPath to the location or an
    *           HTML id.
    * @return <li><code>true</code> if clear was successful.</li> <li>
    *         <code>false</code> if clear was unsuccessful.</li>
    * @throws org.openqa.selenium.NoSuchElementException
    *            if unable to find the check box.
    */
   public boolean clearCheckBox(final String locator)
   {
      if (isSelected(locator))
      {
         // On IE, "click(locator)" not working for check box;
         this.clickByJavaScript(locator);
         
      }
      return !isSelected(locator);
   }

   /**
    * Selects an option from a combo box.
    * 
    * @param selectLocator
    *           Location of the combo box. The locator string can accept an
    *           XPath to the location or an HTML id.
    * @param optionValue
    *           The text to be selected as a label. For example:
    *           "label=Customer Number"
    * @return <li><code>true</code> if the the selection was successful.</li>
    *         <li><code>false</code> if the selection was not successful.</li>
    */
   public boolean selectComboBox(final String selectLocator, final String optionValue)
   {
      // by default the type will do a proceeding tab after the input
      return selectComboBox(selectLocator, optionValue, true);
   }

   /**
    * Selects an option from a combo box with a choice to tab or not.
    * 
    * @param selectLocator
    *           Location of the combo box. The locator string can accept an
    *           XPath to the location or an HTML id.
    * @param optionValue
    *           The text to be selected as a label. For example:
    *           "label=Customer Number"
    * @param tab
    *           Indicates whether or not to do tab and verification after
    *           selection of an item
    * @return <li><code>true</code> if the the selection was successful.</li>
    *         <li><code>false</code> if the selection was not successful.</li>
    */
   public boolean selectComboBox(final String selectLocator, final String optionValue, final boolean tab)
   {
      try
      {
         boolean success = true;

         new Select(findElement(selectLocator)).selectByVisibleText(optionValue);

         // do this twice to give it a bit of time to set up the requests if that time is needed
         if (tab)
         {
            success = getText(selectLocator).equalsIgnoreCase(optionValue);

            findElement(selectLocator).sendKeys(Keys.TAB);

            // do this twice to give it a bit of time to set up the requests if that time is needed
            waitForNoRequests();
            waitForNoRequests();
         }

         return success;
      }
      catch (org.openqa.selenium.NoSuchElementException e)
      {
         throw new NoSuchElementException("Either the locator '" + selectLocator + "' or the combo box item '"
               + optionValue + "' doesn't exist");
      }
   }

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
   public boolean selectFromList(final String selectLocator, final String textLocator)
   {
      new Select(findElement(selectLocator)).selectByVisibleText(textLocator);
      String[] selectedValue = getSelectedOptionsFromListBox(selectLocator);
      for (String singleValue : selectedValue)
      {
         if (singleValue.equalsIgnoreCase(textLocator))
         {
            return true;
         }
      }
      return false;
   }
   
   /**
    * deSelects options by text.
    * 
    * @param selectLocator
    *           Location of the selection list.
    * @param textLocator
    *           The text to be deselected.
    * @return <li><code>true</code> if the the deselection was successful.</li>
    *         <li><code>false</code> if the deselection was not successful.</li>
    */
   public boolean deselectFromList(final String selectLocator, final String textLocator)
   {
      Select select = new Select(findElement(selectLocator));
      String[] selectedValue = getSelectedOptionsFromListBox(selectLocator);
      for (String singleValue : selectedValue)
      {
         if (singleValue.equalsIgnoreCase(textLocator))
         {
            select.deselectByVisibleText(textLocator);
            break;
         }
      }
      
      String[] selectedValueAfterDeselect = getSelectedOptionsFromListBox(selectLocator);
      for (String singleValue : selectedValueAfterDeselect)
      {
         if (singleValue.equalsIgnoreCase(textLocator))
         {
            return false;
         }
      }
      return true;
   }

   /**
    * Remove all selections from the set of selected options in a select list
    * element using an element locator.
    * 
    * @param selectLocator
    *           an element locator identifying a multi-select box
    * @return <li><code>True </code>if all options are unselected</li> <li>
    *         <code>False</code> otherwise</li>
    */
   public boolean removeAllSelections(final String selectLocator)
   {
      Select selectionList = new Select(findElement(selectLocator));
      String[] selectedValue = getSelectedOptionsFromListBox(selectLocator);
      for (String singleValue : selectedValue)
      {
         selectionList.deselectByVisibleText(singleValue);
      }
      return (getSelectedOptionsFromListBox(selectLocator).length == 0);
   }

   /**
    * Select all selections from the set of selected options in a select list
    * using an element locator.
    * 
    * This function works even if the select list is not a true multi-select
    * list (when you can select or de-select by simply click an option).
    * 
    * @param selectLocator
    *           an element locator identifying a select list box
    * @return <li><code>True </code>if all options are selected.</li> <li>
    *         <code>False</code> otherwise.</li>
    */
   public boolean selectAllSelections(final String selectLocator)
   {
      Select selectionList = new Select(findElement(selectLocator));
      String[] allOptions = getAllOptions(selectLocator);
      int selectedNumber = 0;
      for (String singleValue : allOptions)
      {
         selectionList.selectByVisibleText(singleValue);
         selectedNumber++;
      }
      return (getSelectedOptionsFromListBox(selectLocator).length == selectedNumber);
   }

   /**
    * Returns a string array, containing all the available options for a comboBox.
    * 
    * @param selectLocator
    *           Location of the combo box. The locator string can accept an
    *           XPath to the location or an HTML id.
    * @return a String array containing all the available options. If the combo
    *         box cannot be located, or if there are no values, the return value
    *         is <code>null</code>.
    * @throws org.openqa.selenium.NoSuchElementException
    *            if unable to find combo box.
    */
   public String[] getAllOptions(final String selectLocator)
   {
      Select comboBox = new Select(findElement(selectLocator));
      int numberOfOptions = comboBox.getOptions().size();
      Iterator<WebElement> options = comboBox.getOptions().iterator();
      String[] optionValues = new String[numberOfOptions];

      for (int index = 0; index < numberOfOptions; index++)
      {
         optionValues[index] = options.next().getAttribute("textContent");
      }

      return optionValues;
   }

   /**
    * Returns a string array, containing all the available options for a listBox.
    * 
    * @param selectLocator
    *           Location of the combo box. The locator string can accept an
    *           XPath to the location or an HTML id.
    * @return a String array containing all the available options. If the listBox 
    *           cannot be located, or if there are no values, the return value
    *           is <code>null</code>.
    * @throws org.openqa.selenium.NoSuchElementException
    *            if unable to find combo box.
    */
   public String[] getAllOptionsFromListBox(final String selectLocator)
   {     
      
      int optionIdx = 1;
      ArrayList<String> lstAllOptions = new ArrayList<String>();
      String optionLocator = "//div[@id='" + selectLocator + "']/div[1]";  
      while(this.existsNoWait(optionLocator))
      {      
         lstAllOptions.add(this.getAttribute(optionLocator, "innerHTML"));        
         optionIdx++;
         optionLocator = "//div[@id='" + selectLocator + "']/div[" + optionIdx +"]";
      }
      
      String[] allOptions = new String[lstAllOptions.size()];
      allOptions = lstAllOptions.toArray(allOptions);
      
      return allOptions;
   }    

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
   public String[] getSelectedOptionsFromListBox(final String selectLocator)
   {
      int optionIdx = 1;
      ArrayList<String> lstSelectedOptions = new ArrayList<String>();

      String optionLocator = "//div[@id='" + selectLocator + "']/div[1]";     
      while(this.existsNoWait(optionLocator))
      {      
         if(this.getAttribute(optionLocator, "aria-selected").equals("true"))
         {
            lstSelectedOptions.add(this.getAttribute(optionLocator, "innerHTML"));
         }
              
         optionIdx++;
         optionLocator = "//div[@id='" + selectLocator + "']/div[" + optionIdx +"]";
      }
      
      String[] selectedOptions = new String[lstSelectedOptions.size()];
      selectedOptions = lstSelectedOptions.toArray(selectedOptions);
      
      return selectedOptions;
   }   

   /**
    * Returns the string which was selected in a combo box.
    * 
    * @param selectLocator
    *           Location of the combobox. The locator string can accept an XPath
    *           to the location or an HTML id.
    * @return the value of that combo box if combobox cannot be located, or if
    *         there are no values, the return value is <code>null</code>.
    * @throws IllegalArgumentException
    */
//   public String getSelectedValue(final String selectLocator)
//   {
//      if (selectLocator == null)
//      {
//         throw new IllegalArgumentException("select Locator ID = " + selectLocator);
//      }
//      return getText(selectLocator);
//   }

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
   public boolean verifyElement(final String expectedSource, final String locator)
   {
      return (findElement(locator).getAttribute("src")).contains(expectedSource);
   }

   /**
    * Retrieves the handle of the window most recently opened.
    * 
    * @return string containing the unique ID of the window.
    */
   public String getCurrentWindowHandle()
   {
      return getDriver().getWindowHandle();
   }

   /**
    * Switch to the last window opened.
    * Try every default interval until the window is changed. 
    * 
    * @return <li><code>true</code> if the window switch was done successfully.</li>
    *         <li><code>false</code> if the system failed to find or switch to
    *         the specified window.</li>
    */
   public boolean switchToNewWindow()
   {
      String currentWinHandle = driver.getWindowHandle();
      
      int timeout = TimeDelay.getDefaultTimeout();
      int interval = TimeDelay.getDefaultInterval();
      
      for (int index = 0; index < timeout; index += interval)
      {        
         try
         {
            for(String winHandle : driver.getWindowHandles()){
               driver.switchTo().window(winHandle);
           }
            if (! driver.getWindowHandle().equals(currentWinHandle))
               return true;
            TimeDelay.doPause(interval);
         }
         catch (org.openqa.selenium.NoSuchWindowException e)
         {
            //Suppress the No Window exception because we are still waiting for it.
            return false;
         }
      }
      return false;
   } 
   
   /**
    * Switch to the new opened Crystal Report Viewer iFrame.
    * When open a screen from portal to print reports, should not open other screens
    * before clicking "Print" button on this screen. Framework assumes the iFrame Id of the
    * crystal report viewer is just the next one following its parent screen. 
    * e.g. when you open AP7201(A/P Batch Listing Report), say its iFrameID is "iFrameMenu2",
    * then if you click the "Print" button, the crystal report viewer will be opened with a 
    * iFrameID equals "iFrameMenu3". But if you open another screen like GL5100, this new screen 
    * will take "iFrameMenu3", later when you go back to AP7201 and do Print again, the crystal 
    * report viewer may take "iFrameMenu4" or other values which depends how many other screens
    * you've opened. In this case framework can't locate its exact iFrameId.
    * 
    * @return <li><code>true</code> if the iFrame switch was done successfully.</li>
    *         <li><code>false</code> if the system failed to find or switch to
    *         the specified iFrame.</li>
    */
   public boolean switchToCrystalReportIFrameFromPortal()
   {
      this.waitForSpinnerGone();
      
      // Can't switch from one iFrame to another if they are siblings
      // Need switch back to parent first
      this.getDriver().switchTo().defaultContent();
      
      // Get current screen's iFrameId
      // Generate crystal report viewer's iFrameId, suppose it just follows its parent.
      String currentIframeIdx = this.iframe.substring(10, this.iframe.length());
      int crystalReportIframeIdx = Integer.parseInt(currentIframeIdx) + 1;
      String crystalReportIframe = "iFrameMenu" + crystalReportIframeIdx;
      
      // Wait until the crystal report iFrame is displayed.
      this.waitForElement("//div[@id='screenLayout']/iframe[@id='" + crystalReportIframe + "']" ); 

      if(this.getAttribute(crystalReportIframe, "src").contains("ReportViewer"))
      {
         this.getDriver().switchTo().frame(findElement(crystalReportIframe));
         return true;
      }
      System.out.println("Can't locate Crystal Report Viewer iFrame with ID set to " + crystalReportIframe);
      
      return false;
   }
   
   
   /**
    * Switch to a window specified by its handle provided.
    * 
    * @param windowHandle
    *           string containing the unique ID of the window.
    * @return <li><code>true</code> if the window switch was done successfully.</li>
    *         <li><code>false</code> if the system failed to find or switch to
    *         the specified window.</li>
    */
   public boolean switchToWindow(final String windowHandle)
   {
      try
      {
         for(String winHandle : driver.getWindowHandles()){
            driver.switchTo().window(winHandle);
        }
                 
         //getDriver().switchTo().window(windowHandle);
         return true;
      }
      catch (org.openqa.selenium.NoSuchWindowException e)
      {
         //Suppress the No Window exception because we are still waiting for it.
         //System.out.println("Waiting for window...");
         return false;
      }
   }

   /**
    * Switch to a specified frame by its name provided.
    * 
    * @param frameName
    *           name of the frame specified in the HTML tag.
    * @return <li><code>true</code> if the frame switch was done successfully.</li>
    *         <li><code>false</code> if the system failed to find or switch to
    *         the frame.</li>
    */
   public boolean switchToFrame(final String frameName)
   {
      try
      {
         switchToDefaultContent();

         // To handle frames (children) within a frame (parent) look for a
         // "." in the frameName, if it splits into 2 then make a call
         // to switch to the parent frame first then the children
         // This may be a temporary work around as normally the
         // one call is enough for Parent.Child
         if (frameName.contains("."))
         {
            String[] frames = frameName.split("\\.");
            for (String frame : frames)
            {
               getDriver().switchTo().frame(frame);
            }
         }
         else
         {
            getDriver().switchTo().frame(frameName);
         }
      }
      catch (org.openqa.selenium.NoSuchFrameException e)
      {
         //Suppress the No frame exception because we are still waiting for it.
         //System.out.println("Waiting for frame...");
      }
      return true;
   }

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
   public boolean switchToDefaultContent()
   {
      getDriver().switchTo().defaultContent();
      return true;
   }

   /**
    * Retrieve the title of the current window.
    * 
    * @return string with the current window title.
    */
   public String getCurrentWindowTitle()
   {
      return getDriver().getTitle();
   }

   /**
    * Switch to the original window that first opened.
    * 
    * This method will catch the NoWindow exception during the switching.
    * 
    * This method would keep popping the window stack until finding the first
    * window handler and then switch to that window using the window handler.
    * 
    * @return <li><code>true</code> if the window switching was successful.</li>
    *         <li><code>false</code> if the window switching failed.</li>
    */
   public boolean switchToDefaultWindow()
   {
      try
      {
         String defaultHandle;
         Iterator<String> windowHandles = getDriver().getWindowHandles().iterator();
         for (;;)
         {
            defaultHandle = windowHandles.next();
            if (!windowHandles.hasNext())
            {
               break;
            }
         }
         switchToWindow(defaultHandle);
      }
      catch (org.openqa.selenium.NoSuchWindowException e)
      {
         //Suppress the No window exception because we are still waiting for it.
         //System.out.println("Waiting for window...");
      }
      return true;
   }

   /**
    * Maximizes the window to screen.
    */
   public void maximizeWindow()
   {
	   getDriver().manage().window().maximize();
   }


   
   /**
    * Gets the attribute of a CSS Property.
    * 
    * This method and ({@link getCssProperty}) needs to be looked at and see
    * which one is necessary.
    * 
    * 
    * @param locator
    *           Element locator
    * @param property
    *           CSS Property which value will be returned
    * @return CSS Property value
    * @throws org.openqa.selenium.NoSuchElementException
    *            if unable to locate the element.
    */
   public String getAttribute(final String locator, final String property)
   {
      return findElement(locator).getAttribute(property);
   }

   /**
    * Gets the value of a CSS Property.
    * 
    * @param locator
    *           Element locator
    * @param property
    *           CSS Property which value will be returned
    * @return CSS Property value
    * @throws org.openqa.selenium.NoSuchElementException
    *            if unable to locate the element.
    */
   public String getCssProperty(final String locator, final String property)
   {
      return findElement(locator).getCssValue(property);
   }

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
    * @throws org.openqa.selenium.NoSuchElementException
    *            if unable to locate the element.
    */
   public boolean isEditable(final String locator)
   {
      // if element is not visible or disabled, return false right away
      if (!isVisible(locator) || findElement(locator).getAttribute("isDisabled").contentEquals("true"))
      {
         return false;
      }

      // if element is List box or Check box or Radio then return true
      // right away
      if (findElement(locator).getAttribute("class").contains("ListBox")
            || findElement(locator).getAttribute("type").contains("checkbox")
            || findElement(locator).getAttribute("type").contains("radio"))
      {
         // Return true since the element is enabled and visible
         return true;
      }

      // now test any other element, such as text box and label
      // Note: label will throw and exception in the section above because
      // it does not have a "type" attribute.
      return findElement(locator).getAttribute("isContentEditable").contentEquals("true");
   }

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
    * @throws org.openqa.selenium.NoSuchElementException
    *            if unable to locate the element.
    */
   public boolean isDisabled(final String locator)
   {
	  if (findElement(locator).getAttribute("disabled") != null) 
		  return findElement(locator).getAttribute("disabled").contentEquals("true");
	  else
		  return false;

   }
   
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
   public boolean isDisabledForTab(final String locator)
   {
      if (findElement(locator).getAttribute("class") != null) 
        return findElement(locator).getAttribute("class").contains("k-state-disabled");
     else
        return false;
   }

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
   public boolean isDisabledByClass(final String locator)
   {
      return findElement(locator).getAttribute("class").contains("disabled");

   }

   /**
    * Checks if a window with name windowTitle exists.
    * 
    * @param windowTitle
    *           Title located in the browser tab
    * @return <li><code>True </code>if window with title windowTitle exist</li>
    *         <li><code>False </code>otherwise</li>
    */
   public boolean isWindowTitleExist(final String windowTitle)
   {
      boolean result = false;
      String currentHandle = getDriver().getWindowHandle();

      Set<String> handles = getDriver().getWindowHandles();

      for (String handle : handles)
      {
         getDriver().switchTo().window(handle);

         if (getDriver().getTitle().contentEquals(windowTitle))
         {
            result = true;
         }
      }

      getDriver().switchTo().window(currentHandle);
      return result;
   }

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
   public WebElement findElement(final String locator)
   {
      
      this.waitForSpinnerGone();
      
      final By by;
      if (locator.startsWith("//"))
      {
         by = By.xpath(locator);
      }
      else if (locator.startsWith("css="))
      {
         by = By.cssSelector(locator.replace("css=", ""));  
      }
      else
      {
         by = By.id(locator);
      }
      
      return wait.until(ExpectedConditions.presenceOfElementLocated(by));
   }
   /**
    * Helper method to find elements on a page. It will automatically determine
    * if the locator passed to it uses an xPath or HTML ID. No implicit wait
    * 
    * Implicit wait will throw Exception when an element can't be located 
    * by a given locator. For elements which need try with different locators until 
    * they are located will be failed to be found if using findElement() which supports 
    * implicit wait. So this method will remove the implicit 
    * wait and users can add explicit wait time by pauseForSeconds  
    * 
    * Applicable for an element which has indefinite locator, needs to try with the 
    * possible locators one by one until it is found.
    * 
    * e.g., tables has two types of structure, and textBox in a table has 3 types of 
    * structure
    * 
    * @param locator
    *           xPath or HTML ID used to uniquely identify an element.
    * @return WebElement being tracked by the locator.
    * @throws org.openqa.selenium.NoSuchElementException
    *            if the element identified by the locator cannot be found
    */
   protected WebElement findElementNoWait(final String locator)
   {
      this.waitForSpinnerGone();
      
      final By by;
      if (locator.startsWith("//"))
      {
         by = By.xpath(locator);
      }
      else if (locator.startsWith("css="))
      {
         by = By.cssSelector(locator.replace("css=", ""));  
      }
      else
      {
         by = By.id(locator);
      }
      return getDriver().findElement(by);
   }
   /**
    * This function is called after a method in the Browser class fails. It
    * should be used by most, if not all public Browser functions.
    * 
    * @param e
    *           Exception thrown by the failing function.
    * @return Should always return false.
    */
   protected boolean functionFail(final Exception e)
   {
      e.printStackTrace();
      return false;
   }

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
   public boolean scrollWithEnd(final String locator)
   {
      findElement(locator).sendKeys(Keys.END);
      return true;
   }

   /**
    * Scrolls vertically when passed a vertical scrollbar.
    * 
    * @param divID
    *           locator for the vertical scrollbar.
    * @return <li><code>true</code> if the script executed successfully.</li>
    *         <li><code>false</code> if the script failed to execute.</li>
    */
   public boolean scrollVertical(final String divID)
   {
      final String script = "var objDiv = document.getElementById(\"" + divID
            + "\");objDiv.scrollTop = objDiv.scrollHeight;";

      return executeJavaScript(script);
   }

   /**
    * Determines if the browser window has a scrollbar of the specified
    * orientation. Note: Uses javascript to do the verification.
    * 
    * @param orientation
    *           the scrollbar to check. Either <code>horizontal</code> or
    *           <code>vertical</code>
    * 
    * @return <li><code>true</code> if the window has a horizontal scrollbar</li>
    *         <li><code>false</code> if the window does not have a horizontal
    *         scrollbar</li>
    */
   public boolean isScrollbarPresent(final String orientation)
   {
      String script = (orientation.toLowerCase().equalsIgnoreCase("horizontal")) ? "if (document.body.scrollWidth > document.body.clientWidth)"
            : "if (document.body.scrollHeight > document.body.clientHeight)";

      script += "{";
      script += "return true;";
      script += "}";
      script += "else";
      script += "{";
      script += "return false;";
      script += "}";

      return executeJavaScript(script);
   }

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
   public boolean scrollWithArrowDown(final String locator)
   {
      findElement(locator).sendKeys(Keys.ARROW_DOWN);
      return true;
   }

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
   public boolean scrollWithPageDown(final String locator)
   {
      findElement(locator).sendKeys(Keys.PAGE_DOWN);
      return true;
   }

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
   public boolean scrollWithPageUp(final String locator)
   {
      findElement(locator).sendKeys(Keys.PAGE_UP);
      return true;
   }

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
   public boolean scrollWithArrowRight(final String locator)
   {
      findElement(locator).sendKeys(Keys.ENTER);
      return true;
   }

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
   public boolean scrollWithArrowLeft(final String locator)
   {
      findElement(locator).sendKeys(Keys.chord(Keys.SHIFT, Keys.ARROW_LEFT));
      return true;
   }

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
   public boolean scrollWithHome(final String locator)
   {

      findElement(locator).sendKeys(Keys.HOME);
      return true;
   }

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
   public boolean scrollWithArrowUp(final String locator)
   {
      findElement(locator).sendKeys(Keys.ARROW_UP);
      return true;
   }

   /**
    * Generic vertical scroll to the 0, pixelToScrollTo position.
    * 
    * @param pixelToScrollTo
    *           position to scroll to, in pixels
    * @return <li><code>true</code> if the scroll was successful.</li> <li>
    *         <code>false</code> if the scroll failed.</li>
    */
   public boolean scrollWindowVertically(final int pixelToScrollTo)
   {
      final String script = "if (window.screen) {window.scroll(0, " + pixelToScrollTo + ");};";

      return executeJavaScript(script);
   }

   /**
    * Generic horizontal scroll to the pixelToScrollTo, 0 position.
    * 
    * @param pixelToScrollTo
    *           position to scroll to, in pixels
    * @return <li><code>true</code> if the scroll was successful.</li> <li>
    *         <code>false</code> if the scroll failed.</li>
    */
   public boolean scrollWindowHorizontally(final int pixelToScrollTo)
   {
      final String script = "if (window.screen) {window.scroll(" + pixelToScrollTo + ",0);};";

      return executeJavaScript(script);
   }

   /**
    * Waits for an element to exist in DOM.
    * 
    * Currently it uses a small timeout (1 min).
    * 
    * @param locator
    *           locator of the element to wait for.
    * @return <li><code>true</code> if the item was found within the timeout.</li>
    *         <li><code>false</code> if the timeout was reached.</li>
    */
   public boolean waitForElement(final String locator)
   {
      return waitForElement(locator, 30, 1000);
   }

   /**
    * Waits for an element to appear before proceeding. Allows for the
    * specification of the timeout period as well as the interval in which to
    * check for the element.
    * 
    * @param locator
    *           locator of the element to wait for.
    * @param timeout
    *           The amount of time to wait before giving up.
    * @param interval
    *           The frequency which to poll to check if <code>locator</code> is
    *           available.
    * @return <li><code>true</code> if the item was found within the timeout.</li>
    *         <li><code>false</code> if the timeout was reached.</li>
    */
   private boolean waitForElement(final String locator, final int timeout, final int interval)
   {
      for (int index = 0; index < timeout; index ++)
      {
         if (existsNoWait(locator) && isVisible(locator))
         {
            return true;
         }
         TimeDelay.doPause(interval);
      }
      return false;
   }

   /**
    * Waits for an element to exist in DOM.
    * 
    * Currently it uses a small timeout (1 min).
    * 
    * @param locator
    *           locator of the element to wait for.
    * 
    * @param window
    *           window where the element to wait for resides.
    * 
    * @return <li><code>true</code> if the item was found within the timeout.</li>
    *         <li><code>false</code> if the timeout was reached.</li>
    */
   public boolean waitForElementInDifferentWindow(final String locator, final String window)
   {
      return waitForElementInDifferentWindow(locator, window, settings.getSmallTimeOut(), settings.getDefaultInterval());
   }

   /**
    * Waits for an element to appear before proceeding. Allows for the
    * specification of the timeout period as well as the interval in which to
    * check for the element.
    * 
    * @param locator
    *           locator of the element to wait for.
    * @param window
    *           window where the element to wait for resides.
    * @param timeout
    *           The amount of time to wait before giving up.
    * @param interval
    *           The frequency which to poll to check if <code>locator</code> is
    *           available.
    * @return <li><code>true</code> if the item was found within the timeout.</li>
    *         <li><code>false</code> if the timeout was reached.</li>
    */
   private boolean waitForElementInDifferentWindow(final String locator, final String window, final int timeout,
         final int interval)
   {
      for (int index = 0; index < timeout; index += interval)
      {
         if (!switchToWindow(window))
         {
            TimeDelay.doPause(interval);
         }
         else
         {
            if (exists(locator))
            {
               return true;
            }
         }
      }

      return false;
   }

   /**
    * Waits for an element to NOT exist in DOM.
    * 
    * Currently it uses a small timeout (1 min).
    * 
    * @param locator
    *           locator of the element to wait for.
    * @return <li><code>true</code> if the item was found within the timeout.</li>
    *         <li><code>false</code> if the timeout was reached.</li>
    */
   public boolean waitForNoElement(final String locator)
   {
      return waitForNoElement(locator, 10, 1000);
   }

   /**
    * Waits for an element to disappear before proceeding. Allows for the
    * specification of the timeout period as well as the interval in which to
    * check for the element.
    * 
    * @param locator
    *           locator of the element to wait until gone.
    * @param timeout
    *           The amount of time to wait before giving up.
    * @param interval
    *           The frequency which to poll to check if <code>locator</code> is
    *           available.
    * @return <li><code>true</code> if the item was found within the timeout.</li>
    *         <li><code>false</code> if the timeout was reached.</li>
    */
   private boolean waitForNoElement(final String locator, final int timeout, final int interval)
   {
      for (int index = 0; index < timeout; index += interval)
      {
         if ((!existsNoWait(locator) || !isVisible(locator)))
         {
            return true;
         }
         TimeDelay.doPause(interval);
      }
      return false;
   }

   /**
    * Clears a field and validates its default value after clearing.
    * 
    * @param locator
    *           An element locator pointing to an input element.
    * @param defaultValue
    *           the default value of the field after clearing.
    * @return <li><code>True </code>if Cleared and Validated</li> <li>
    *         <code>False</code> otherwise</li>
    * @throws org.openqa.selenium.NoSuchElementException
    *            if unable to locate the text field.
    */
   public boolean clearAndValidateField(final String locator, final String defaultValue)
   {
      findElement(locator).clear();
      return getText(locator).equalsIgnoreCase(defaultValue);
   }

   /**
    * Clicks a Locator and waits for an Element to appear (exist and visible)
    * until a predefined timeout.
    * 
    * @param locator
    *           Locator for UI element that is going to be clicked.
    * @param element
    *           UI element to wait for.
    * @param timeOut
    *           Time out value
    * @return <li><code>True </code> locator was clicked and element exists and
    *         isVisible</li> <li>
    *         <code>False</code> otherwise</li>
    */
   private boolean clickAndWaitFor(final String locator, final String element, final int timeOut)
   {
      // Did not time out while waiting for element?
      boolean notTimedOut = false; // Assume it will time out

      // Click the specified Locator
      boolean wasClicked = click(locator);

      // Sleep until the specified Element is visible
      long end = System.currentTimeMillis() + timeOut;

      if (wasClicked)
      {
         while (System.currentTimeMillis() < end)
         {
            // If Element exists and isVisible, then we are done
            if (exists(element) && isVisible(element))
            {
               notTimedOut = true; // It didn't time out
               break;
            }
         }
      }

      // True if not timed out and locator was clicked
      return (wasClicked && notTimedOut);
   }

   /**
    * Clicks an element.
    * 
    * Uses scroll and then clicks.
    * 
    * @param locator
    *           Locator for the UI elements that needs focus and clicked
    * @return <li><code>True </code>if clicking the element is successfully</li>
    *         <li>
    *         <code>False</code> never. (this needs to be looked at)</li>
    * @throws org.openqa.selenium.NoSuchElementException
    *            if unable to locate the element.
    * @throws org.openqa.selenium.ElementNotVisibleException
    *            if the element exists but not visible, thus cannot be interact
    *            with
    */
   public boolean click(final String locator)
   {
      return click(locator, true);
   }
   
   /**
    * Clicks an element by sending special key "RETURN" as a alternative.
    * e.g.: For gl2100, click button "btnCreateBatch" with click() not working
    * 
    * 
    * @param locator
    *           Locator for the UI elements that needs focus and clicked
    * @return <li><code>True </code>if clicking the element is successfully</li>
    *         <li>
    *         <code>False</code> never. (this needs to be looked at)</li>
    * @throws org.openqa.selenium.NoSuchElementException
    *            if unable to locate the element.
    * @throws org.openqa.selenium.ElementNotVisibleException
    *            if the element exists but not visible, thus cannot be interact
    *            with
    */
   public boolean clickByReturn(final String locator)
   {
      return clickByReturn(locator, true);
   }

   /**
    * Move the mouse to an element and hover over it.
    * 
    * Only support using element id to locate it.
    * 
    * @param locator
    *           Locator for the UI elements that to be hover over
    */
   public void hover(final String locator)
   { 
      //if(this.browserType.equalsIgnoreCase("INTERNET_EXPLORER"))
      {
         Actions action = new Actions(getDriver());
         WebElement menu = this.findElement(locator);          
         action.moveToElement(menu).build().perform();
      }
      /*else
      {       
    	  System.out.println("HIHI :"+getDriver().findElement(By.xpath(locator)).getText());
         String jquery = "$(\"#" + locator + "\").mouseover();";
         executeJavaScriptReturnString(jquery);
      }*/
   }
   
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
    * @throws org.openqa.selenium.NoSuchElementException
    *            if unable to locate the element.
    * @throws org.openqa.selenium.ElementNotVisibleException
    *            if the element exists but not visible, thus cannot be interact
    *            with
    */
   public boolean click(final String locator, final boolean doAccpacRequestChecking)
   {
      //Locatable located = (Locatable)findElement(locator);
      //located.getLocationOnScreenOnceScrolledIntoView();
      //mouseOver(locator);
      //wait.until(ExpectedConditions.elementToBeClickable(By.id(locator)));
      findElement(locator).click();
      //
      //      if (doAccpacRequestChecking)
      //      {
      //         // do this twice to give it a bit of time to set up the requests if that time is needed
      //         waitForZeroAccpacRequests();
      //         waitForZeroAccpacRequests();
      //      }

      return true;
   }
   
   /**
    * Click an element by javascript. Used when click() provided by selenium
    * not working.
    * 
    * @param locator
    *           Locator for the UI elements that needs focus and clicked
    * 
    * @return <li><code>True </code>if clicking the element is successfully</li>
    *         <li>
    *         <code>False</code> never. (this needs to be looked at)</li>
    * @throws org.openqa.selenium.NoSuchElementException
    *            if unable to locate the element.
    * @throws org.openqa.selenium.ElementNotVisibleException
    *            if the element exists but not visible, thus cannot be interact
    *            with
    */
   public boolean clickByJavaScript(final String locator)
   {
      this.waitForSpinnerGone();
      
      // Use findElement to wait until the element is present
      this.findElement(locator);
      
      JavascriptExecutor js = (JavascriptExecutor) driver;
      
      String javascript = null;
      if (locator.startsWith("//"))
         javascript = "return document.evaluate(\"" + locator + "\", document, null, 9, null).singleNodeValue.click();";
      else
         javascript = "return document.getElementById(\"" + locator + "\").click()";

      js.executeScript(javascript);

      return true;
   }
   
   /**
    * Clicks an element and will do an Accpac Request check in the Fitnesse
    * tests. However, because our performance tests need to start a timer before
    * doing the checking, this method is to handle so we can just call click
    * without checking and then call the check separately.
    * 
    * This method is not exposed to Fitnesse tests, only click() that take one
    * parameter.
    * 
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
    * @throws org.openqa.selenium.NoSuchElementException
    *            if unable to locate the element.
    * @throws org.openqa.selenium.ElementNotVisibleException
    *            if the element exists but not visible, thus cannot be interact
    *            with
    */
   public boolean clickByReturn(final String locator, final boolean doAccpacRequestChecking)
   {
      //mouseOver(locator);
      findElement(locator).sendKeys(Keys.RETURN);
      return true;
   }

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
    *         <code>False</code> if Robot false to execute</li>
    */
   public boolean mouseOver(final String locator)
   {
      try
      {
         Point location = findElement(locator).getLocation();

         int x = location.getX() + Integer.parseInt(findElement(locator).getAttribute("clientWidth")) / 2;
         int y = location.getY() + Integer.parseInt(findElement(locator).getAttribute("clientHeight")) / 2;
         //y = y + Integer.parseInt(findElement(locator).getAttribute("clientHeight")) / 2 + bannerSize;

         Robot mouse = new Robot();
         mouse.mouseMove(x, y);

         return true;
      }
      catch (AWTException e)
      {
         e.printStackTrace();
         return false;
      }
   }


   @Override
   public int getTableItemCount(final String locator)
   {
      WebElement table = getDriver().findElement(By.id(locator));
      List<WebElement> rows = table.findElements(By.tagName("tr"));
      return rows.size();
   }

   @Override
   public boolean selectTableItemByName(final String locator, final String itemName)
   {
      WebElement table = getDriver().findElement(By.id(locator));
      List<WebElement> rows = table.findElements(By.tagName("tr"));
      for (WebElement row : rows)
      {
         List<WebElement> cells = row.findElements(By.tagName("td"));
         for (WebElement cell : cells)
         {
            if (itemName.contentEquals(cell.getText()))
            {
               cell.click();
               return true;
            }
         }
      }

      // If reach here then no Item was found
      return false;
   }

   @Override
   public boolean selectTableItemByIndex(final String locator, final int index)
   {
      WebElement table = getDriver().findElement(By.id(locator));

      List<WebElement> rows = table.findElements(By.tagName("tr"));

      WebElement row = rows.get(index - 1);

      List<WebElement> cells = row.findElements(By.tagName("td"));

      WebElement cell = cells.get(0);

      cell.click();

      return true;
   }


   /**
    * 
    * {@inheritDoc}
    */
   public boolean waitForContent(final String locator, final String content)
   {
      return waitForContent(locator, content, settings.getSmallTimeOut(), settings.getDefaultInterval());
   }

   /**
    * 
    * @param locator
    *           locator of the element to get content from
    * @param content
    *           the expected content to be displayed by the element represented
    *           by locator
    * @param timeout
    *           The amount of time to wait before giving up.
    * @param interval
    *           The frequency which to poll to check if <code>content</code> is
    *           displayed by locator.
    * @return <li><code>true</code> if the content was displayed by element
    *         within the timeout.</li> <li><code>false</code> if the timeout was
    *         reached.</li>
    */
   private boolean waitForContent(final String locator, final String content, final int timeout, final int interval)
   {
      for (int index = 0; index < timeout; index += interval)
      {
         if (getText(locator).contentEquals(content))
         {
            return true;
         }
         TimeDelay.doPause(interval);
      }
      return false;
   }

   /**
    * Wait until all requests have been fulfilled. This method will
    * help in the timing of the Selenium WebDriver and should reduce
    * the need for time delay pauses.
    * 
    * 
    * @return <li><code>true</code> if the poll of no requests completed
    *         within the timeout.</li> <li><code>false</code> if the timeout was
    *         reached.</li> 
    */
   public boolean waitForNoRequests()
   {
      try
      {
    	  int interval = settings.getDefaultInterval();
    	  
          for (int waitingTime = 0; waitingTime < settings.getLargeTimeOut(); waitingTime += interval)
          {    	  
              if (executeJavaScript("return window.jQuery != undefined && jQuery.active === 0"))
              {
                 return true;
              }
              TimeDelay.doPause(interval);
           }
           return false; 	  
      }
      catch (Exception e)
      {
    	  return false;
      }      
   } 
   
   /**
    * <p>
    * Waits for the loading Spinner imagine to be gone.
    * </p>
    * 
    * @return Whether or not the loadingImage(Spinner) disappear before the 
    *         default timeout was reached.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget.
    */   
   private boolean waitForSpinnerGone()
   {
      try
      {
         this.getDriver().findElement(By.id("ajaxSpinner"));
      }
      catch(Exception e)
      {
         // For UI like Crystal Print screen, no Spinner is defined
         // So just ignore the waiting  
         return true;
      }      
      for (int index = 0; index < 120; index ++)
      {    
         if ((!this.getDriver().findElement(By.id("ajaxSpinner")).isDisplayed()))
         {
            return true;
         }
         TimeDelay.doPause(1000);
      }
      return false;
   }
   
   /**
    * <p>
    * Waits for a ui is ready.
    * First wait until the spinner is gone, then wait for the validation element present .
    * </p>
    * 
    * @param locator
    *           Locator for the UI elements to wait for
    * @return Whether or not the ui is fully loaded and ready for use.
    * 
    * @throws IllegalArgumentException
    *            The UI does not contain such a widget.
    */   
   public boolean waitForUiReady(String locator)
   {
      if(this.waitForSpinnerGone())
         return this.waitForElement(locator);
      else
         return false;      
   }

}
