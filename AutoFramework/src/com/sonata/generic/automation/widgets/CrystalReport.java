/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.widgets;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sonata.generic.automation.browser.Browser;

/**
 * The <code>CrystalReport</code> class represents a Crystal Report webpage.
 */
public class CrystalReport extends Widget
{
   /**
    * constructs an instance of the {@link CrystalReport} class.
    * 
    * @param locator
    *           the locator of the CrystalReport
    * @param browser
    *           the browser that will be used to access the control
    */
   public CrystalReport(String locator, Browser browser)
   {
      super(locator, browser);
   }
   
   /**
    * Click the export button. Need wait for a long time for crystal viewer to load the report page.
    * 
    * @return {@link Button} representing the Export Button of the report
    */
   public Boolean clickExportButton()
   {      
      WebDriverWait wait = new WebDriverWait(this.getBrowser().getDriver(), 60);
      
      WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("IconImg_CrystalReportViewerSage300_toptoolbar_export")));
      if(element !=null)
      {
         element.click();
         return true;
      }
      else
         return false;
   }


   /**
    * Gets the export button.
    * 
    * @return {@link Button} representing the Export Button of the report
    */
   public Button getExportButton()
   {
      return new Button("IconImg_CrystalReportViewerSage300_toptoolbar_export", this.getBrowser());  
   } 

   /**
    * returns {@link Label} representing the Company Name field of the report
    * 
    * @return {@link Label} representing the Company Name field of the report
    */
   public Label getCompanyNameLabel()
   {
      // The text of the message is contained within the div with an id "Field2"     
      return new Label("//div[@id='Field2']", this.getBrowser());      
   }
}
