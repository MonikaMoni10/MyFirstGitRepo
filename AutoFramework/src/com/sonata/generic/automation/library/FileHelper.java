package com.sonata.generic.automation.library;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

/**
 * File Helper.
 */
public final class FileHelper
{

   /**
    * The constructor was made private so it can never be instantiated.
    */
   private FileHelper()
   {

   }

   /**
    * Rename a file.
    * 
    * @param originalFileName
    *           The original file name which user wants to rename.
    * @param newFileName
    *           The new file name.
    * @return <li><code>True</code> if rename was successfully</li> <li>
    *         <code>False</code> otherwise</li>
    */
   public static boolean rename(final String originalFileName, final String newFileName)
   {
      File oldFile = new File(originalFileName);
      return oldFile.renameTo(new File(newFileName));
   }

   /**
    * get file names from a folder. we delete the folder files before call this
    * function
    * 
    * @param fileDirectoryValue
    *           the directory name
    * @return <li><code>filenames[0] only one file exists</code></li>
    */
   public static String getFileName(final String fileDirectoryValue)
   {
      String[] fileNames = null;
      File myDirectory = new File(fileDirectoryValue);

      if (myDirectory.isDirectory())
      {
         fileNames = myDirectory.list();

         for (int index = 0; index <= TimeDelay.getDefaultTimeout(); index += TimeDelay.getDefaultInterval())
         {
            if (fileNames[0] == null)
            {
               fileNames = myDirectory.list();
            }
            else
            {
               break;
            }
         }
      }
      return fileNames[0];

   }

   /**
    * Deletes all the files which is exist in the given directory.
    * 
    * @param fileDirectoryValue
    *           folder name
    * 
    * @return <li><code>True </code>if files are deleted. or if there is no
    *         directory</li> <li>
    *         <code>False </code>otherwise.</li>
    */
   public static boolean deleteFiles(final String fileDirectoryValue)
   {
      boolean result = false;
      boolean fileExistFlag = false;
      File[] fileNames = null;
      File myDirectory = new File(fileDirectoryValue);

      if (myDirectory.isDirectory())
      {
         fileNames = myDirectory.listFiles();
         for (File eachFile : fileNames)
         {
            fileExistFlag = true;
            result = eachFile.delete();
            //delete file failed
            if (!result)
            {
               break;
            }
         }
      }
      else
      {
         //if directory is not existing
         result = true;
      }
      //if directory exits, but no file is inside the folder
      if (!fileExistFlag)
      {
         result = true;
      }
      return result;
   }

   /**
    * get all file names from a folder.
    * 
    * @param fileDirectoryValue
    *           the directory name
    * @return <li><code>filenames exists</code></li>
    */
   public static String[] getFileNames(final String fileDirectoryValue)
   {
      String[] fileNames = null;
      File myDirectory = new File(fileDirectoryValue);

      if (myDirectory.isDirectory())
      {
         fileNames = myDirectory.list();

         for (int index = 0; index <= TimeDelay.getDefaultTimeout(); index += TimeDelay.getDefaultInterval())
         {
            if (fileNames[0] == null)
            {
               fileNames = myDirectory.list();
            }
            else
            {
               break;
            }
         }
      }
      return fileNames;
   }

   /**
    * get last modified file names from a folder.
    * 
    * @param fileDirectoryValue
    *           the directory name
    * @return <li><code>filenames exists</code></li>
    */
   public static String getLastModifiedFileName(final String fileDirectoryValue)
   {
      long lastMod = Long.MIN_VALUE;

      File resultFile = null;

      File[] fileNames = null;
      File myDirectory = new File(fileDirectoryValue);

      if (myDirectory.isDirectory())
      {
         fileNames = myDirectory.listFiles();
         for (File eachFile : fileNames)
         {
            if (eachFile.lastModified() > lastMod)
            {
               resultFile = eachFile;
               lastMod = eachFile.lastModified();
            }
         }
         return resultFile.getName();
      }
      return null;
   }

   /**
    * get last modified file names of specific type(s) from a folder.
    * 
    * This method should only be used on file name with total length (including
    * extension) of at least 5 characters.
    * 
    * @param fileDirectoryValue
    *           the directory name
    * @param fileTypes
    *           the file types
    * @return <li><code>filenames exists</code></li>
    */
   public static String getLastModifiedFileName(final String fileDirectoryValue, final String[] fileTypes)
   {
      int extLgth = 5;
      long lastMod = Long.MIN_VALUE;

      File resultFile = null;

      File[] fileNames = null;
      File myDirectory = new File(fileDirectoryValue);

      if (myDirectory.isDirectory())
      {
         fileNames = myDirectory.listFiles();
         for (File eachFile : fileNames)
         {
            if (eachFile.lastModified() > lastMod)
            {
               String tempName = eachFile.getName().toLowerCase();
               int length = tempName.length();
               if (length < extLgth)
               {
                  length = extLgth;
               }

               //comparing each file types to the current file
               for (int i = 0; i < fileTypes.length; i++)
               {
                  String fileType = fileTypes[i];
                  if (tempName.substring(length - extLgth).contains(fileType.toLowerCase()))
                  {
                     resultFile = eachFile;
                  }
               }

               lastMod = eachFile.lastModified();
            }
         }
         return resultFile.getName();
      }
      return null;
   }

   /**
    * Reads the given file and check for the given Date values.
    * 
    * @param cSVFile
    *           Path of CSV file
    * @return <li><code>the value of last line of file </code>
    * 
    */
   private static String getLastLineOfFile(final String cSVFile)
   {
      String strLine = new String();

      try
      {
         FileInputStream fileStream = new FileInputStream(cSVFile);
         DataInputStream input = new DataInputStream(fileStream);

         BufferedReader reader = new BufferedReader(new InputStreamReader(input));

         while (reader.ready())
         {
            strLine = reader.readLine();
         }

         // Close file
         reader.close();

      }
      catch (Exception e)
      {
         e.printStackTrace();
         return null;
      }

      return strLine;
   }

   /**
    * get the inquiry value of the last line of specified CSV file.
    * 
    * @param inquiryFile
    *           Path of inquiry CSV file
    * @param searchKey
    *           search Key which user wants to find the value(case sensitive).
    * 
    * @return <li><code>the value of inquiry </code>
    */
   public static String getSearchValueOfLogFileLastLine(final String inquiryFile, final String searchKey)
   {
      String strLine = null;
      String actualValue = null;
      String elimiatedStr = null;
      int firstIndex = 0;
      //need to calculate ="
      int modifiedLength = searchKey.length() + 2;

      try
      {

         strLine = getLastLineOfFile(inquiryFile);

         //try to get the value of matching inquiry name
         firstIndex = strLine.indexOf(searchKey) - 1;

         elimiatedStr = strLine.substring(0, firstIndex + modifiedLength);

         //cut off the previous string plus searchKey + 2
         actualValue = strLine.substring(elimiatedStr.length());

         //cut off the first quote
         actualValue = actualValue.substring(1);

         firstIndex = actualValue.indexOf("\"");

         actualValue = actualValue.substring(0, firstIndex);

         return actualValue;

      }
      catch (Exception e)
      {
         e.printStackTrace();
         return null;
      }
   }

   /**
    * Deletes a file.
    * 
    * @param fileName
    *           full path + file name of the file to delete
    * 
    * @return <li><code>True </code>if file is deleted.</li> <li>
    *         <code>False </code>otherwise.</li>
    */
   public static boolean deleteFile(final String fileName)
   {
      File fileToDelete = new File(fileName);
      if (fileToDelete.exists())
      {
         return fileToDelete.delete();
      }
      else
      {
         return true;
      }
   }

   /**
    * Deletes a file.
    * 
    * @param fileDirectoryValue
    *           folder name
    * @param fileName
    *           file to delete from within the specified fileDirectory
    * 
    * @return <li><code>True </code>if file is deleted.</li> <li>
    *         <code>False </code>otherwise.</li>
    */
   public static boolean deleteFile(final String fileDirectoryValue, final String fileName)
   {
      File deleteFile = new File(fileDirectoryValue + fileName);
      if (deleteFile.exists())
      {
         return deleteFile.delete();
      }
      else
      {
         return true;
      }
   }

   /**
    * Creates a folder / directory .
    * 
    * @param fileDirectoryValue
    *           directory name
    * 
    * @return <li><code>True </code>if directory already exists or the directory
    *         was created successfully.</li> <li>
    *         <code>False </code>otherwise.</li>
    */
   public static boolean createDirectory(final String fileDirectoryValue)
   {
      File directory = new File(fileDirectoryValue);
      if (directory.exists())
      {
         return true;
      }
      else
      {
         return directory.mkdir();
      }
   }

   /**
    * Creates a small text file and writes the content into the file from a
    * string.
    * 
    * @param fileName
    *           the absolute path and name of the file.
    * @param content
    *           the content of file.
    * @return true if the file has been created successfully.
    */
   public static boolean createSmallTextFile(final String fileName, final String content)
   {
      Writer output = null;
      File file = new File(fileName);
      try
      {
         output = new BufferedWriter(new FileWriter(file));
         output.write(content);
         output.close();
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
      return fileExists(fileName);
   }

   /**
    * Waits for a file to exist in the file system.
    * 
    * @param fileName
    *           file name
    * @param timeout
    *           The amount of time to wait before giving up.
    * @param interval
    *           The frequency which to poll to check if file exists.
    * 
    * @return <li><code>True </code>if file exists within the timeout period</li>
    *         <li>
    *         <code>False </code>otherwise.</li>
    */
   public static boolean waitForFile(final String fileName, final int timeout, final int interval)
   {
      File fileToVerify = new File(fileName);

      for (int index = 0; index < timeout; index += interval)
      {
         // do a quick verify that the file by that name exists, 
         // then verify it has a size greater than 0, if it does then the file has likely been created 
         // (assuming that the file only gets updated once when the process that created it saves it at one time)
         if (fileToVerify.exists())
         {
            if (fileToVerify.length() > 0)
               return true;
         }
         TimeDelay.doPause(interval);
      }
      return false;
   }

   /**
    * Validates that a file or directory exists in the file system.
    * 
    * @param fileDirectoryValue
    *           directory name
    * 
    * @return <li><code>True </code>if directory exists</li> <li>
    *         <code>False </code>otherwise.</li>
    */
   public static boolean fileExists(final String fileDirectoryValue)
   {
      File directory = new File(fileDirectoryValue);

      return directory.exists();

   }
   
   /**
    * If a directory exists, clean it.
    * If not exist, create it.
    * 
    * @param path
    *           directory path to be cleaned
    * @throws IOException
    *            
    */
   public static void cleanDir(final String path)
   {
      try {      
         File f = new File(path);
         if (f.exists() && f.isDirectory()) {
            FileUtils.cleanDirectory(f); 
         }else
         {
            createDir(path);
         }
      } catch (IOException e) {
         e.printStackTrace();
      } 
   }
   
   /**
    * If a directory does not exist, create it.
    * 
    * @param path
    *           directory path to be created
    * @throws IOException
    *            
    */
   public static void createDir(final String path)
   {
      try {
         File f = new File(path);
         if(!(f.exists() && f.isDirectory())) {
            
            FileUtils.forceMkdir(f);
         }
      } catch (IOException e) {
         e.printStackTrace();
      } 
   }
   
   /**
    * Copy a file from the original location to a destination location.
    * FileUtils.copyFile Works for pdf, but for csv and xml, the copied 
    * files are deleted finally, so use copyFileBySteam
    * @param src
    *           src file with full path
    * @param dest
    *           dest file with full path   
    *           
    * @throws IOException
    *            
    */
   public static void copyFile(final String src, final String dest)
   {
      File srcFile = new File(src);
      File destFile = new File(dest);
      
      destFile.deleteOnExit();
      
      try
      {
         FileUtils.copyFile(srcFile, destFile);
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }
   
   /**
    * Copy a file from the original location to a destination location.
    * 
    * @param src
    *           src file with full path
    * @param src
    *           dest file with full path   
    *           
    * @throws IOException
    *            
    */
   public static void copyFileBySteam(final String src, final String dest) throws IOException   
   {
      File srcFile = new File(src);
      File destFile = new File(dest);
      
      InputStream is = null;
      OutputStream os = null;
      try {
          is = new FileInputStream (srcFile);
          os = new FileOutputStream(destFile);
          byte[] buffer = new byte[1024];
          int length;
          while ((length = is.read(buffer)) > 0) {
              os.write(buffer, 0, length);
          }
      }finally {
         is.close();
         os.close();
      } 
   }
   
   /**
    * Check if a directory exists.
    * 
    * @param directory
    *           dir path to be checked
 
    * @return <li><code>True </code>if directory exists</li> <li>
    *         <code>False </code>otherwise.</li>
    *            
    */
   public static boolean isDirNotExist(final String directory)
   {
      File dir = new File(directory);
      if(!(dir.exists() && dir.isDirectory()))
      {
         return true;
      }
      else
      {
         return false;
      }
   }
   
   /**
    * Wait for a file generated under a given directory and return the file name.
    * 
    * @param fileDir
    *           dir path where to wait for a file to be generated
    *    
    * @param timeout
    *           The amount of time to wait before giving up.
    * @param interval
    *           The frequency which to poll to check if file exists.
 
    * @return <li><code>the generated file name</code></li>
    *            
    */ 
   public static String waitForRpt(final String fileDir, final int timeout, final int interval)
   {
      File dirToVerify = new File(fileDir);
      for (int index = 0; index < timeout; index += interval)
      {
         if(dirToVerify.list().length>0)
         {
            File[] listOfFiles = dirToVerify.listFiles();
            if (listOfFiles[0].exists())
            {
               //For Chrome, a temp file like 90F1.tmp will be generated first
               //Later when the file totally generated, it's changed to the name like CrystalReportViewerSage300.pdf
               //so need enhance to wait until the real file is generated
               if ((listOfFiles[0].length() > 0) && ( (listOfFiles[0].getName().endsWith(".pdf"))
                    || (listOfFiles[0].getName().endsWith(".csv")) || (listOfFiles[0].getName().endsWith(".xml"))))
                  
                  return listOfFiles[0].getName();
            }
         }
         TimeDelay.doPause(interval);
      }
      return null;
   }

   /**
    * Returns the value that matches the Key and Type provided.
    * 
    * @param logFile
    *           The log file where a search for a Type and Key is performed.
    * @param startingLine
    *           Whether the test is from the start or from the end line
    * @param offsetLine
    *           The number of lines to go up or down in order to select the
    *           required line
    * @param type
    *           The type this line belongs to (i.e. User, Hotfix, Database,
    *           License, etc)
    * @param key
    *           The key in this line which value is required
    * @return <li><code>The value that matches the Key and Type provided</code></li>
    */
   public static String getValueForTypeAndKey(final String logFile, final String startingLine, final int offsetLine,
         final String type, final String key)
   {
      String result = "";
      String line = "";

      // By default we assume the starting point is the "last" line
      boolean first = false;
      boolean last = true;

      // Change default values if starting point is the "first" line
      if (startingLine.equalsIgnoreCase("first"))
      {
         first = true;
         last = false;
      }

      File file = new File(logFile);

      // Return NULL if file doesn`t exist
      if (file.exists())
      {
         // All lines of the SAME type are gathered
         ArrayList<String> types = getAllLinesWithType(file, type);

         // Get the specific line from all lines of the same type
         if (!types.isEmpty())
         {
            if (last)
            {
               int index = types.size() - 1 - offsetLine;
               if (index >= 0)
               {
                  line = types.get(index);
               }
            }

            if (first)
            {
               if (offsetLine < types.size())
               {
                  line = types.get(offsetLine);
               }
            }
         }

         // Get the value from line searching for a key
         result = getValueForKey(line, key);
      }
      else
      {
         return null;
      }

      return result;
   }

   /**
    * Verifies if a file was created less than a number of days
    * 
    * @param fullFileName
    *           File name with path
    * @param numberOfDays
    *           The number of days old to verify
    * 
    * @return <li><code>True if file was created less than numberOfDays</li>
    *         </code> <li>
    *         <code>False otherwise</li></code>
    */
   public static boolean isFileLessThanDays(final String fullFileName, final int numberOfDays)
   {
      Boolean result = false;
      String dateFormat = "MM/dd/yyyy";
      SimpleDateFormat format = new SimpleDateFormat(dateFormat);

      // This is the date the file was created
      String creationDate = getCreationDate(fullFileName);

      try
      {
         // Create the dates
         Date fileCreationDate = format.parse(creationDate);
         Date today = new Date();

         // Zero out the Time in Dates
         fileCreationDate = getZeroTimeDate(fileCreationDate);
         today = getZeroTimeDate(today);

         // Start calendar is the creation date 
         Calendar startCalendar = Calendar.getInstance();
         startCalendar.setTime(fileCreationDate);

         // End calendar is today's date
         Calendar endCalendar = Calendar.getInstance();
         endCalendar.setTime(today);

         // Get a date that is today minus numberOfDays
         int daysBetween = getDaysBetween(startCalendar, endCalendar);

         if (daysBetween < numberOfDays)
         {
            result = true;
         }

      }
      catch (ParseException e)
      {
         e.printStackTrace();
      }

      return result;
   }

   /**
    * Verifies if a file creation date is greater than a number of days
    * 
    * @param fullFileName
    *           File name with path
    * @param numberOfDays
    *           The number of days old to verify
    * 
    * @return <li>
    *         <code>True if a file creation date is greater than numberOfDays</li>
    *         </code> <li>
    *         <code>False otherwise</li></code>
    */
   public static boolean isFileGreaterThanDays(final String fullFileName, final int numberOfDays)
   {
      Boolean result = false;
      String dateFormat = "MM/dd/yyyy";
      SimpleDateFormat format = new SimpleDateFormat(dateFormat);

      // This is the date the file was created
      String creationDate = getCreationDate(fullFileName);

      try
      {
         // Create the dates
         Date fileCreationDate = format.parse(creationDate);
         Date today = new Date();

         // Zero out the Time in Dates
         fileCreationDate = getZeroTimeDate(fileCreationDate);
         today = getZeroTimeDate(today);

         // Start calendar is the creation date 
         Calendar startCalendar = Calendar.getInstance();
         startCalendar.setTime(fileCreationDate);

         // End calendar is today's date
         Calendar endCalendar = Calendar.getInstance();
         endCalendar.setTime(today);

         // Get a date that is today minus numberOfDays
         int daysBetween = getDaysBetween(startCalendar, endCalendar);

         if (daysBetween > numberOfDays)
         {
            result = true;
         }

      }
      catch (ParseException e)
      {
         e.printStackTrace();
      }

      return result;
   }

   /**
    * Verifies if a File is exactly a number of days old.
    * 
    * @param fullFileName
    *           File name with path
    * @param numberOfDays
    *           The number of days old to verify
    * @return <li><code>True if file is exactly numberOfDays old</li></code> <li>
    *         <code>False otherwise</li></code>
    */
   public static boolean isFileDaysOld(final String fullFileName, final int numberOfDays)
   {
      boolean result = false;

      String dateFormat = "MM/dd/yyyy";
      SimpleDateFormat format = new SimpleDateFormat(dateFormat);

      // This is the date the file was created
      String creationDate = getCreationDate(fullFileName);

      try
      {
         // Create the dates
         Date fileCreationDate = format.parse(creationDate);
         Date today = new Date();

         // Zero out the Time in Dates
         fileCreationDate = getZeroTimeDate(fileCreationDate);
         today = getZeroTimeDate(today);

         // Get a date that is today minus numberOfDays
         Date daysOld = rollBackDays(today, numberOfDays);

         if ((!daysOld.after(fileCreationDate)) && (!daysOld.before(fileCreationDate)))
         {
            result = true;
         }

      }
      catch (ParseException e)
      {
         e.printStackTrace();
      }

      return result;
   }

   /**
    * Get the number of days between two Calendar dates
    * 
    * @param startDate
    *           The starting date
    * @param endDate
    *           The end date
    * @return <li><code>the number of days between two Calendar dates</li>
    *         </code>
    */
   private static int getDaysBetween(final Calendar startDate, final Calendar endDate)
   {
      Calendar date = (Calendar)startDate.clone();
      int daysBetween = 0;

      while (date.before(endDate))
      {
         date.add(Calendar.DAY_OF_MONTH, 1);
         daysBetween++;
      }

      return daysBetween;
   }

   /**
    * Returns a Date after subtracting a number of days.
    * 
    * @param date
    *           The date that will be used
    * @param days
    *           The number of days to subtracting
    * @return <li><code>A Date after subtracting a number of days</li></code>
    */
   private static Date rollBackDays(final Date date, final int days)
   {
      Calendar calendar = Calendar.getInstance();

      calendar.setTime(date);
      calendar.add(Calendar.DATE, ((-1) * days));

      return calendar.getTime();
   }

   /**
    * Zeroes out the time (hour, minute, second, millisecond) in a given Date
    * 
    * @param date
    *           The date to zero out the time
    * 
    * @return <li><code>The Date with the Time zeroed out</li></code>
    */
   private static Date getZeroTimeDate(final Date date)
   {
      Date res = date;
      Calendar calendar = Calendar.getInstance();

      calendar.setTime(date);
      calendar.set(Calendar.HOUR_OF_DAY, 0);
      calendar.set(Calendar.MINUTE, 0);
      calendar.set(Calendar.SECOND, 0);
      calendar.set(Calendar.MILLISECOND, 0);

      res = calendar.getTime();

      return res;
   }

   /**
    * Gets the file creation date
    * 
    * @param fullFileName
    *           complete file name with path
    * @return <li><code>the file creation date</code></li>
    */
   private static String getCreationDate(final String fullFileName)
   {
      String creationDate = "";

      try
      {

         // get runtime environment and execute child process
         Runtime systemShell = Runtime.getRuntime();
         Process output = systemShell.exec("cmd /c dir \"" + fullFileName + "\" /tc");

         // open reader to get output from process
         BufferedReader reader = new BufferedReader(new InputStreamReader(output.getInputStream()));

         String out = "";
         String line = null;

         int step = 1;
         while ((line = reader.readLine()) != null)
         {
            if (step == 6)
            {
               out = line;
            }
            step++;
         }

         // display process output
         try
         {
            out = out.replaceAll(" ", "");
            creationDate = out.substring(0, 10);
         }
         catch (StringIndexOutOfBoundsException se)
         {
            System.out.println("File not found");
         }
      }
      catch (IOException ioe)
      {
         System.err.println(ioe);
      }
      catch (Throwable t)
      {
         t.printStackTrace();
      }

      return creationDate;
   }

   /**
    * Gets a value for the specified key from a line
    * 
    * @param line
    *           The line that contains a pair of key-value
    * @param key
    *           The key to search in line
    * @return <li><code>A value for the specified key from a line</code></li>
    */
   private static String getValueForKey(String line, String key)
   {
      String result = "";

      // Using a regular expression that will find strings like 
      // AppID="GL" in a line and return GL
      String regex = key + "=\"(.*?)\"";

      Pattern pattern = Pattern.compile(regex);

      Matcher matcher = pattern.matcher(line);

      if (matcher.find())
      {
         result = matcher.group(1);
      }

      return result;
   }

   /**
    * Gets a list of all lines that belong to a specific Type
    * 
    * @param file
    *           The log file
    * @param type
    *           The Type of lines that are requested
    * @return <li>
    *         <code>A list of all lines that belong to a specific Type</code></li>
    */
   private static ArrayList<String> getAllLinesWithType(File file, String type)
   {
      ArrayList<String> types = new ArrayList<String>();
      String line;

      try
      {
         FileReader fileReader = new FileReader(file);
         BufferedReader reader = new BufferedReader(fileReader);

         while ((line = reader.readLine()) != null)
         {
            if (line.startsWith(type))
            {
               types.add(line);
            }
         }

         // Close file
         reader.close();

      }
      catch (Exception e)
      {
         e.printStackTrace();
         return null;
      }

      return types;
   }

   /**
    * Decrypts a file using an executable program.
    * 
    * @param executable
    *           The name of the program that decrypts
    * @param fileName
    *           The encrypted file that is going to be encrypted
    * @return <li>
    *         <code>True if fileName was decrypted</code></li> <li>
    *         <code>False otherwise</code></li>
    */
   public static boolean decrypt(final String executable, final String fileName)
   {
      File encryptedFile = new File(fileName);
      if (!encryptedFile.exists())
      {
         System.out.println("File " + fileName + " not found");
         return false;
      }

      return Runner.run(executable, fileName);
   }

   /**
    * Decrypts a log file using a known executable program.
    * 
    * @param fitnesseRoot
    *           FitNesse root. FitNesse users can get it using:
    *           ${ACCPAC_FITNESSE}
    * @param logFilePath
    *           FitNesse log file path. FitNesse users can get it using:
    *           ${LOGFILEPATH}
    * @param logFileName
    *           The log file name (with extension) that is encrypted
    * @return <li><code>True if logFileName was decrypted</code></li> <li>
    *         <code>False otherwise</code></li>
    */
   public static boolean decrypt(final String fitnesseRoot, final String logFilePath, final String logFileName)
   {
      String executable = fitnesseRoot + "\\FitNesseExtras\\Utilities\\DecryptEnv.exe";
      String logFile = logFilePath + "\\" + logFileName;

      return decrypt(executable, logFile);
   }
   
   /**
    * Get the existing report file saved under Expected Dir.
    * 
    * @param fileDir
    *           Expected dir path:
    * @param fileExtension
    *           Supported file extension: pdf, csv, xml
    * @return <li><code>the existing expected report file name</code></li> 
    */
   public static String getExpectedRpt(final String fileDir, final String fileExtension)
   {
      File dirToVerify = new File(fileDir);
      File[] listOfFiles = dirToVerify.listFiles();
      if(listOfFiles.length == 0)
         return null;
      else
      {
         for (int i=0; i < listOfFiles.length; i++ )
         {
            if (listOfFiles[i].getName().endsWith("." + fileExtension))
            {
               return listOfFiles[i].getName(); 
            }
         }
         return null;
      }
   }
}
