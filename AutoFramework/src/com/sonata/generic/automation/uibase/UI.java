/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.uibase;

import java.io.IOException;

import com.sonata.generic.automation.browser.Browser;
import com.sonata.generic.automation.browser.StopTestException;
import com.sonata.generic.automation.library.CSVHelper;
import com.sonata.generic.automation.library.FileHelper;
import com.sonata.generic.automation.library.PdfCompare;
import com.sonata.generic.automation.library.TimeDelay;
import com.sonata.generic.automation.library.XmlCompare;
import com.sonata.generic.automation.widgets.CrystalReport;
import com.sonata.generic.automation.widgets.MessageBox;

/**
 * The <code>UI</code> interface provides the extension of the {@link WebPage}
 * class for the various sorts of User Interfaces to be met.
 */
public abstract class UI extends WebPage
{
   /**
    * Elements for common export dialog box; based on the old Crystal Runtime
    * implementation. This may co exist unless the previous Sage 300 ERP
    * versions will also incorporate the new Crystal Runtime 11 just as v6.1
    * will.
    */
   private static final String    EXPORT_WINDOW_TITLE_LOCATOR              = "//span[.='Export']";
   private static final String    EXPORT_WINDOW_EXPORT_BUTTON_LOCATOR      = "//a[.='Export']";
   
   //EXPORT_TEMP_LOCATION: a temporary location where the exported file is saved automatically  
   private static final String    EXPORT_TEMP_LOCATION      = "C:\\dev\\tmpReport";
   //PDF_DIFF_UTILITY_LOCATION: where the diff-pdf tool is located, used for Fitness RT
   //private static final String    PDF_DIFF_UTILITY_LOCATION ="C:\\AccpacFitnesseCNA2\\FitNesseExtras\\Utilities";
   
   //pdfDiffUtilityLocation: for Junit automation, copy the pdf_diff Utilities under the current workspace
   private String pdfDiffUtilityLocation = System.getProperty("user.dir") + "\\Utilities";
   /**
    * 
    */
   private MessageBox    messageBox;  
   private CrystalReport    crystalReport; 
   private String actualRpt;
   private String expectedRpt;
   private String actualPath;
   private String expectedPath;
   private String rptFileName;
   
   /**
    * Constructor
    * 
    * @param properties
    *           uiproperties
    * @param browser
    *           current browser
    */
   public UI(UIProperties properties, Browser browser)
   {
      super(new WebPagePropertiesImpl(properties), browser);
      messageBox = null;
   }

   /** Enforce no default constructor */
   private UI()
   {
      this(null, null);
   }

   /**
    * Closes the UI
    * 
    * @return <li><code>true</code> if UI closes</li> <li>
    *         <code>false</code> if UI fails to close</li>
    */
   public final boolean close()
   {
      return getBrowser().close();
   }

   /**
    * Closes the UI, disable the confirmation message.
    * 
    * @param force
    *           boolean to tell the Browser to close the window even if there is
    *           a Javascript to confirm leaving the window.
    * 
    * @return <li><code>true</code> if UI closes</li> <li>
    *         <code>false</code> if UI fails to close</li>
    */
   public final boolean close(boolean force)
   {
      return getBrowser().close(force);
   }
   
   /**
    * return {@link CrystalReport} representing the Crystal Report web page.
    * 
    * @param doCreate
    *    indicates to recreate the widget or not
    *    
    * @return {@link MessageBox} representing the Message Dialog Box.
    */
   public final CrystalReport getCrystalReport(final Boolean doCreate)
   {
      // Only create the Crystal Report widget if it hasn't been created earlier
      // for this instance of UI
      if (crystalReport == null || doCreate)
         crystalReport = new CrystalReport("CrystalReportViewerSage300", getBrowser());

      return crystalReport;
   }   
   
   /**
    * return {@link MessageBox} representing the Message Dialog Box.
    * 
    * @param messageType
    *    the locator to identify the Message Dialog in the DOM.
    *    "Confirmation", "Error", "Information", or "Warning" are acceptable
    *    
    * @param doCreate
    *    indicates to recreate the widget or not
    *    
    * @return {@link MessageBox} representing the Message Dialog Box.
    */
   public final MessageBox getMessageBox(final String messageType, final Boolean doCreate)
   {
      // Only create the MessageBox widget if it hasn't been created earlier
      // for this instance of UI
      if (messageBox == null || doCreate)
         messageBox = new MessageBox(messageType, getBrowser());

      return messageBox;
   }  
   
   /**
    * Select a file format from the comboBox to export a report.
    * 
    * The <id> for the crystal table includes a random value.
    * Get the table id first then locate other elements based on it  
    * 
    * @param fileFormat
    *           The file format option to export a report.
    * 
    * @return Whether or not the a file format is selected successful.
    */
   public final boolean selectExportFileFormat(final String fileFormat)
   {
      String crystalTableId = this.getBrowser().getAttribute("//body/table","id");
      String comboBoxId = "IconImg_Txt_iconMenu_icon_" + crystalTableId + "_combo";
      this.getBrowser().click(comboBoxId);
      this.getBrowser().click("//span[.='" + fileFormat +"']");         
      return (this.getBrowser().getText(comboBoxId).trim().equals(fileFormat)) ? true: false;         
   }
   
   /**
    * Perform the comparing of a report that is in form of a CSV file. Assumes
    * the filename specified exists in both the "Expected" and the "Actual"
    * folders.
    * 
    * @param fileExtension
    *           The file extension, the type of file, to save as; which is
    *           either {@code "csv"}, {@code "pdf"}.
    * 
    * @return <li><code>true</code> if the format CSV could not be selected</li>
    *         <li><code>false</code>otherwise</li>
    */
   
   public final boolean compareReport(final String fileExtension)
   {
      if (fileExtension.toLowerCase().equals("csv"))
      {
         boolean compareTwoCSVFile = CSVHelper.compareTwoCSVFile(expectedRpt, actualRpt);
         if (! compareTwoCSVFile)
            //throw new IllegalArgumentException("Exported report does not match the expected report!");
            System.out.println("Error: Exported csv report does not match the expected report!");
         return compareTwoCSVFile;
      }
      if (fileExtension.toLowerCase().equals("xml"))
      {
         boolean compareTwoXMLFile = XmlCompare.compareTwoXMLFile(expectedRpt, actualRpt);
         if ( ! compareTwoXMLFile)
            //throw new IllegalArgumentException("Exported xml report does not match the expected report!");
            System.out.println("Error: Exported xml report does not match the expected report!");
         return compareTwoXMLFile;
      }
      if (fileExtension.toLowerCase().equals("pdf"))
      {
         PdfCompare compareUtility = new PdfCompare(pdfDiffUtilityLocation);
         boolean compareTwoPDFFile = compareUtility.compareTwoFiles(expectedRpt, actualRpt);
         if (! compareTwoPDFFile)
            //throw new IllegalArgumentException("Exported pdf report does not match the expected report!");
            System.out.println("Error: Exported pdf report does not match the expected report!");
         return compareTwoPDFFile;
      }
      return false;
   }

   /**
    * Perform the exporting of a report, assumes the export window has been
    * invoked and opened.
    * 
    * @param path
    *           The path to the FitNesse test page.
    * 
    * @param caseName
    *           The filename to save as.
    * 
    * @param fileExtension
    *           The file extension, the type of file, to save as; which is
    *           either {@code "csv"}, {@code "pdf"}, {@code "xml"}.
    * 
    * @return <li><code>true</code> if the format CSV could not be selected</li>
    *         <li><code>false</code>otherwise</li>
    */
   public final boolean exportReport(final String path, final String caseName, final String fileExtension) 
   {
   // Delete the previous actual file if one exists, so file existence
      // Check is accurate as to indicate that the process is complete.
      
      actualPath = path + "\\" + caseName + "\\Actual";
      expectedPath = path + "\\" + caseName + "\\Expected";
            
      FileHelper.cleanDir(EXPORT_TEMP_LOCATION);
      FileHelper.createDir(actualPath);
      FileHelper.createDir(expectedPath);
            
      // Handle the export dialog
      if (!this.getBrowser().waitForElement(EXPORT_WINDOW_TITLE_LOCATOR))
      {
         throw new StopTestException("Cannot find the export window title with the XPATH: " + EXPORT_WINDOW_TITLE_LOCATOR);
      }
      
      if (!this.getBrowser().click(EXPORT_WINDOW_EXPORT_BUTTON_LOCATOR))
      {
         throw new StopTestException("Cannot click the export button with the XPATH: " + EXPORT_WINDOW_TITLE_LOCATOR);
      }
      
      rptFileName = FileHelper.waitForRpt(EXPORT_TEMP_LOCATION, TimeDelay.getTimeoutMassive(), TimeDelay.getDefaultInterval());
      
      if( rptFileName!= null)
      {
         String srcFile = EXPORT_TEMP_LOCATION + "\\" + rptFileName;
         actualRpt = actualPath + "\\" + rptFileName;
         
         try
         {
            FileHelper.copyFileBySteam(srcFile, actualRpt);
         }
         catch (IOException e)
         {
            e.printStackTrace();
         }
         //For FRT the expected report files are saved with name like glbtch01.csv 
         //While in CNA2.0 the report file is named like CrystalReportViewerSage300.csv by default
         //So just keep the existing expected report file name during comparison for FRT
         String existingRpt = FileHelper.getExpectedRpt(expectedPath, fileExtension);
         if(existingRpt != null)
         {
            expectedRpt = expectedPath + "\\" + existingRpt;
            return true;
         }else
         {
            expectedRpt = expectedPath + "\\" + rptFileName;         
            //If no expected report file exists, automatically copy the real one to expected dir for the first time run
            if( ! FileHelper.fileExists(expectedRpt))
            {
               try
               {
                  FileHelper.copyFileBySteam(srcFile, expectedRpt);
               }
               catch (IOException e)
               {
                  e.printStackTrace();
               }
            }    
            return true;
         }
      }
      return false;
   }
}
