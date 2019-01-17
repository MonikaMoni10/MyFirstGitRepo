package com.sonata.generic.automation.library;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Used for performance related functions, such as timing.
 * 
 * @author Garrick Tom
 * 
 */
public class Performance
{
   /**
    * The product being tested.
    */
   private static final String PRODUCT      = "Sage 300 ERP";

   /**
    * The version of the product.
    */
   private static final String VERSION      = "6.1";

   /**
    * The build number being tested. We are currently utilizing this for the
    * sprint number.
    */
   private static final String BUILD_NUMBER = "00507935";         //Nightly Build 2011.10.10__507935

   /**
    * Test type.
    */
   private static final String TYPE         = "Normal";

   /**
    * Normally the value of this will not change.
    */
   private static final String NETWORK      = "LAN";

   /**
    * Fix part of the directory path to check for the test results file.
    */
   private static final String FIXPATH      = "C:\\performance\\";

   /**
    * Complete directory path to check for the test results file.
    */
   private String              completePath;

   /**
    * File to store the test results.
    */
   private String              fileName;

   /**
    * Name of the company under test.
    */
   private String              company;

   /**
    * Server storing the data.
    */
   private String              dataLocation;

   /**
    * Database engine type.
    */
   private String              engine;

   /**
    * Operating system running the tests.
    */
   private String              operatingSystem;

   /**
    * Number of machines running the tests.
    */
   private String              numberMachines;

   /**
    * Name of the machine running the test.
    */
   private String              machineName;

   /**
    * Number of the run, typically starting at 1 and being incremented up.
    */
   private int                 measureName;

   /**
    * Test result, seconds.
    */
   private double              measure;

   /**
    * Start time of the test in milliseconds.
    */
   private long                startTime;

   /**
    * Total time of the test in milliseconds.
    */
   private long                elapsedTime;

   /**
    * Creates a performance object, used to store performance data.
    * 
    * @param company
    *           Name of the company under test.
    * @param dataLocation
    *           Server storing the data.
    * @param engine
    *           Database engine type.
    * @param operatingSystem
    *           Operating system running the tests.
    * @param numberMachines
    *           Number of machines running the tests.
    */
   public Performance(final String company, final String dataLocation, final String engine,
         final String operatingSystem, final String numberMachines)
   {
      this.company = company;
      this.dataLocation = dataLocation;
      this.engine = engine;
      this.operatingSystem = operatingSystem;
      this.numberMachines = numberMachines;
   }

   /**
    * Start the timer just before the function you want to test. Note that the
    * file created will auto-generate the file name using the testName and the
    * the iteration.
    * 
    * @param testName
    *           Name of the test.
    * @param iteration
    *           Used if running the same test multiple times.
    * @param notes
    *           Any extra notes to add.
    * @param machineName
    *           Name of the current machine.
    */
   public final void startTimer(final String testName, final String iteration, final String notes,
         final String machineName)
   {
      //  Full path to folder with results
      this.completePath = FIXPATH + machineName + "\\";

      // Create directory if it doesn't exist
      File directory = new File(this.completePath);
      directory.mkdirs();

      // Auto-Generate a filename.
      fileName = testName.replace(" ", "") + "-" + iteration + ".csv";
      this.machineName = machineName;

      try
      {
         // If the file exists already, read the last line to get the
         // measureName.
         FileInputStream in = new FileInputStream(completePath + fileName);
         BufferedReader br = new BufferedReader(new InputStreamReader(in));

         String strLine = null, tmp;

         while ((tmp = br.readLine()) != null)
         {
            strLine = tmp;
         }

         measureName = Integer.parseInt(strLine.split(",")[1]);

         in.close();
      }
      catch (Exception e)
      {
         // Create the file and header if it doesn't already exist.
         addHeader(testName, iteration, notes);
      }

      startTime = System.currentTimeMillis();
   }

   /**
    * Stop the timer just after the function you're testing. This also writes to
    * the text file.
    */
   public final void endTimer()
   {
      final double secondsInMilliseconds = 0.001;

      elapsedTime = System.currentTimeMillis() - startTime;
      measure = elapsedTime * secondsInMilliseconds;
      measureName++;
      addData();
   }

   /**
    * Adds the [HEADER] section in the text file, as well as starts the [DATA].
    * 
    * @param testName
    *           Name of the test.
    * @param iteration
    *           Used if running the same test multiple times.
    * @param notes
    *           Any extra notes to add.
    */
   private void addHeader(final String testName, final String iteration, final String notes)
   {
      try
      {
         BufferedWriter writer = new BufferedWriter(new FileWriter(completePath + fileName, true));
         writer.write("[HEADER]" + "\r\n");
         writer.write(testName + "," + TYPE + "," + VERSION + "," + BUILD_NUMBER + "," + PRODUCT + "," + company + ","
               + dataLocation + "," + engine + "," + NETWORK + "," + operatingSystem + "," + numberMachines + ","
               + iteration + ",,,," + notes + "\r\n");
         writer.write("[DATA]" + "\r\n");
         writer.close();
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }

   }

   /**
    * Adds an entry into [DATA].
    */
   private void addData()
   {
      try
      {
         BufferedWriter writer = new BufferedWriter(new FileWriter(completePath + fileName, true));
         writer.write(machineName + "," + measureName + "," + measure + "\r\n");
         writer.close();
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }

}
