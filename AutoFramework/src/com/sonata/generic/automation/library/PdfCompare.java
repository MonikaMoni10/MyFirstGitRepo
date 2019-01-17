/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.library;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

/**
 * PDFHelper class to implement the methods that interact with Adobe Acrobat
 * (PDF) Files.
 * 
 */
public final class PdfCompare
{
   private final String m_utilityPath;
   private final String m_utilityExeName;

   /**
    * The constructor was made private so it can never be instantiated.
    * 
    * @param utilityPath
    *           path to the utility to compare with. Currently handles using the
    *           open source tool "diff-pdf"
    */
   public PdfCompare(final String utilityPath)
   {
      m_utilityPath = utilityPath + "\\diff-pdf";
      m_utilityExeName = "diff-pdf.exe";
   }

   /**
    * Compares the two PDF files. The comparison results file will get generated
    * to the user profiles, My documents folder. C:\Users\Joe.Smith\Documents as
    * an example.
    * 
    * @param expectedFile
    *           Path of expected PDF file
    * @param actualFile
    *           Path of actual PDF file
    * 
    * @return <li><code>True </code>if PDF files Compared successfully</li> <li>
    *         <code>False </code>otherwise</li>
    */
   public boolean compareTwoFiles(final String expectedFile, final String actualFile)
   {
      try
      {
         return compareTwoFiles(expectedFile, actualFile, System.getenv("USERPROFILE") + "\\Documents");
      }
      catch (Exception e)
      {
         e.printStackTrace();
         return false;
      }
   }

   /**
    * Compares the two PDF files. The comparison results file will get generated
    * to the specified directory.
    * 
    * @param expectedFile
    *           Path of expected PDF file
    * @param actualFile
    *           Path of actual PDF file
    * @param outputDirectory
    *           The folder to generate the comparison results as a PDF file.
    * 
    * @return <li><code>True </code>if PDF files Compared successfully</li> <li>
    *         <code>False </code>otherwise</li>
    */
   public boolean compareTwoFiles(final String expectedFile, final String actualFile, final String outputDirectory)
   {
      try
      {
         Runtime rt = Runtime.getRuntime();

         if (!FileHelper.fileExists(outputDirectory))
         {
            throw new IllegalArgumentException("Output directory '" + outputDirectory + "' could not be found.");
         }

         if (!FileHelper.fileExists(expectedFile))
         {
            throw new IllegalArgumentException("Expected file '" + expectedFile + "' could not be found.");
         }

         if (!FileHelper.fileExists(actualFile))
         {
            throw new IllegalArgumentException("Actual (newly generated) file '" + actualFile + "' could not be found.");
         }

         // Create a copy of the expected and the actual file.
         // With the copy the report generation date will be blacked out so it can be ignored during the compare.
         String stampedExpectedFile = blackoutReportGenerationDate(expectedFile);
         String stampedActualFile = blackoutReportGenerationDate(actualFile);

         // Set the output file as the User profile - my documents
         String command = m_utilityPath + "\\" + m_utilityExeName + " --output-diff=\"" + outputDirectory
               + "\\COMPARISION_RESULTS.pdf\" " + stampedExpectedFile + " " + stampedActualFile;

         Process pr = rt.exec(command);

         BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));

         String line = null;

         while ((line = input.readLine()) != null)
         {
         }

         int exitVal = pr.waitFor();

         if (exitVal == -1073741819)
            throw new IllegalArgumentException(
                  "You may have either the COMPARISON_RESULTS.pdf file open from a previous run of the same test OR you have the expected or actual files open.");

         if (exitVal != 0 && exitVal != 1)
            throw new IllegalArgumentException("Exited with error code " + exitVal);

         // Just some extra buffer time before deleting the temp files
         TimeDelay.doPause(3000);

         FileHelper.deleteFile(stampedExpectedFile);
         FileHelper.deleteFile(stampedActualFile);

         return (exitVal == 0);
      }
      catch (IOException e)
      {
         e.printStackTrace();
         return false;
      }
      catch (InterruptedException e)
      {
         e.printStackTrace();
         return false;
      }
   }

   /**
    * Create a copy of the PDF file that will blackout the report generation
    * date at the top left corner of each page, so that the "diff-pdf.exe"
    * compare will not detect the mismatching dates.
    * 
    * @param filename
    *           Path of a PDF file to replicate and "stamp"
    * 
    * @return the path and filename to the "stamped" (replicated) file.
    */
   private String blackoutReportGenerationDate(final String filename)
   {
      try
      {
         // Load existing PDF
         PdfReader pdfReader = new PdfReader(filename);

         if (filename.lastIndexOf(".pdf") == -1)
         {
            throw new IllegalArgumentException(filename + " does not a PDF file extension.");
         }

         String stampedFilename = filename.replace(".pdf", "_stamped.pdf");

         // Create shell for new version of the file with the Blackout stamping to clear out the report generation date
         PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(stampedFilename));

         // Set the image to be used to blackout the report generation date
         Image image = Image.getInstance(m_utilityPath + "\\overwriteReportGenerationDate.png");

         // float x = pdfReader.getPageSize(1).getWidth();
         float y = pdfReader.getPageSize(1).getHeight();

         // Note: Page numbers start from 1 to n (not 0 to n-1)
         for (int i = 1; i <= pdfReader.getNumberOfPages(); i++)
         {
            PdfContentByte content = pdfStamper.getUnderContent(i);

            // This position is over the report generation date of PDFs generated by Sage 300
            // This method takes two parameters: X and Y coordinate of the lower left corner of the image.
            // Also keep in mind, that the origin coordinate system in a PDF document is the lower left 
            // corner of the document. Not the uppper left corner, like on a screen. 
            image.setAbsolutePosition(0, y - 67);

            content.addImage(image);
         }

         pdfStamper.close();

         FileHelper.waitForFile(stampedFilename, TimeDelay.getTimeoutMassive(), TimeDelay.getDefaultInterval());

         //         // give a little extra time for the file to finish up
         //         TimeDelay.doPause(5000);

         return stampedFilename;

      }
      catch (IOException e)
      {
         e.printStackTrace();
         throw new IllegalArgumentException("Problem when creating a copy of the PDF report.");
      }
      catch (DocumentException e)
      {
         e.printStackTrace();
         throw new IllegalArgumentException("Problem when creating a copy of the PDF report.");
      }

   }
}
