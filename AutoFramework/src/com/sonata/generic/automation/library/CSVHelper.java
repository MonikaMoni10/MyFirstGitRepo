/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.library;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Date;

/**
 * CSVHelper class to implement the methods that interact with Comma-Separated
 * Value(CSV) Files.
 * 
 */
public final class CSVHelper
{
   /**
    * The constructor was made private so it can never be instantiated.
    */
   private CSVHelper()
   {
   }

   /**
    * Reads the given file and check for the given Dates values.
    * 
    * @param cSVFile
    *           Path of CSV file
    * @param columnName
    *           column name.
    * @param operator
    *           operator string value.
    * @param firstValue
    *           first date value which user wants to query "from".
    * @param secondValue
    *           second date value which user wants to query "to".
    * @return <li><code>True </code>if CSV files contains vales</li> <li>
    *         <code>False </code>otherwise</li>
    */
   private static boolean checkValuesInCSV(final String cSVFile, final String columnName, final String operator,
         final Date firstValue, final Date secondValue)
   {
      String strLine;
      int columnNameIndexInCSV = 1;
      int count = 0;
      try
      {
         FileInputStream fileStream = new FileInputStream(cSVFile);
         DataInputStream input = new DataInputStream(fileStream);

         BufferedReader reader = new BufferedReader(new InputStreamReader(input));
         String[] columnNamesInCSV = null;
         boolean firstRow = true;
         while ((strLine = reader.readLine()) != null)
         {
            String[] rowValues = strLine.split(",");
            // Get the column names from CSV file.
            if (firstRow)
            {
               columnNamesInCSV = rowValues;
               firstRow = false;
               // Get the index of the given column name.
               for (int rowIndex = 0; rowIndex <= rowValues.length - 1; rowIndex++)
               {
                  if (trimDoubleQuotes(columnNamesInCSV[rowIndex]).equals(columnName))
                  {
                     columnNameIndexInCSV = rowIndex;
                  }
               }
            }
            else
            {
               count++;
               // if the values have comma(,) then rearrange the values.
               if (columnNamesInCSV.length != rowValues.length)
               {
                  String[] newRowValues = new String[columnNamesInCSV.length];
                  int newRowIndex = 0;
                  for (int rowIndex = 0; rowIndex <= rowValues.length - 1; rowIndex++)
                  {
                     // Concatenate the values starts with double quote
                     // upto ends with double quotes
                     if (rowValues[rowIndex].startsWith("\"") && !rowValues[rowIndex].endsWith("\""))
                     {
                        // column value contain comma
                        int index = 1;
                        while (!rowValues[rowIndex + index].endsWith("\""))
                        {
                           index++;
                        }
                        // Concatenate the strings
                        String tempString = "";
                        for (int i = 0; i < index + 1; i++)
                        {
                           tempString = tempString + rowValues[rowIndex + i];
                        }
                        newRowValues[rowIndex] = tempString;
                        // increase the rowIndex
                        rowIndex = rowIndex + index;
                        newRowIndex++;
                     }
                     else
                     {
                        // column value does not contain comma
                        newRowValues[newRowIndex] = rowValues[rowIndex];
                        newRowIndex++;
                     }
                  }
                  // store the new values to the variable rowValues
                  rowValues = newRowValues;
               }
               // Get the value from the row and convert to date object
               String strDate = trimDoubleQuotes(rowValues[columnNameIndexInCSV]);
               Date expectedDate = DateHelper.getDate(strDate, "yyyy-MM-dd");
               // Check the value is equal
               compare(expectedDate, operator, firstValue, secondValue);
            }
         }
         // Close the input stream
         reader.close();
         input.close();

      }
      catch (Exception e)
      {
         e.printStackTrace();
         return false;
      }
      System.out.println(count + " - records is compared Successfully");
      return true;
   }

   /**
    * Reads the given file and check for the given Date values.
    * 
    * @param cSVFile
    *           Path of CSV file
    * @param columnName
    *           column name.
    * @param operator
    *           operator string value.
    * @param firstValue
    *           first Value which user wants to query "from".
    * @param secondValue
    *           second Value which user wants to query "to".
    * @return <li><code>True </code>if CSV files contains vales</li> <li>
    *         <code>False </code>otherwise</li>
    */
   private static boolean checkValuesInCSV(final String cSVFile, final String columnName, final String operator,
         final String firstValue, final String secondValue)
   {
      String strLine;
      int columnNameIndexInCSV = 1;
      int count = 0;
      try
      {
         FileInputStream fileStream = new FileInputStream(cSVFile);
         DataInputStream input = new DataInputStream(fileStream);

         BufferedReader reader = new BufferedReader(new InputStreamReader(input));
         String[] columnNamesInCSV = null;
         boolean firstRow = true;
         while ((strLine = reader.readLine()) != null)
         {
            String[] rowValues = strLine.split(",");
            // Get the column names from CSV file.
            if (firstRow)
            {
               columnNamesInCSV = rowValues;
               firstRow = false;
               // Get the index of the given column name.
               for (int rowIndex = 0; rowIndex <= rowValues.length - 1; rowIndex++)
               {
                  if (trimDoubleQuotes(columnNamesInCSV[rowIndex]).equals(columnName))
                  {
                     columnNameIndexInCSV = rowIndex;
                  }
               }
            }
            else
            {
               count++;
               // if the values have comma(,) then rearrange the values.
               if (columnNamesInCSV.length != rowValues.length)
               {
                  String[] newRowValues = new String[columnNamesInCSV.length];
                  int newRowIndex = 0;
                  for (int rowIndex = 0; rowIndex <= rowValues.length - 1; rowIndex++)
                  {
                     // Concatenate the values starts with double quote
                     // upto ends with double quotes
                     if (rowValues[rowIndex].startsWith("\"") && !rowValues[rowIndex].endsWith("\""))
                     {
                        // column value contain comma
                        int index = 1;
                        while (!rowValues[rowIndex + index].endsWith("\""))
                        {
                           index++;
                        }
                        // Concatenate the strings
                        String tempString = "";
                        for (int i = 0; i < index + 1; i++)
                        {
                           tempString = tempString + rowValues[rowIndex + i];
                        }
                        newRowValues[rowIndex] = tempString;
                        // increase the rowIndex
                        rowIndex = rowIndex + index;
                        newRowIndex++;
                     }
                     else
                     {
                        // column value does not contain comma
                        newRowValues[newRowIndex] = rowValues[rowIndex];
                        newRowIndex++;
                     }
                  }
                  // store the new values to the variable rowValues
                  rowValues = newRowValues;
               }
               // Get the value from the row
               String strRowValue = trimDoubleQuotes(rowValues[columnNameIndexInCSV]);
               // Check the value is equal
               if (operator.equals("Is"))
               {
                  if (!strRowValue.equals(firstValue))
                  {
                     return false;
                  }
               }
            }
         }
         // Close the input stream
         reader.close();
         input.close();

      }
      catch (Exception e)
      {
         e.printStackTrace();
         return false;
      }
      return true;
   }

   /**
    * Compare the date values between the given date.
    * 
    * @param expectedDate
    *           expected Date value
    * @param operator
    *           operator string value.
    * @param firstValue
    *           first Value which user wants to query "from".
    * @param secondValue
    *           second Value which user wants to query "to".
    * 
    * @return <li><code>True </code>if CSV files contains date as expected date</li>
    *         <li>
    *         <code>False </code>otherwise</li>
    */
   private static boolean compare(final Date expectedDate, final String operator, final Date firstValue,
         final Date secondValue)
   {
      if (operator.equals("Is"))
      {
         if (!expectedDate.equals(firstValue))
         {
            return false;
         }
      }
      else if (operator.equals("Between"))
      {
         if (!(expectedDate.after(firstValue) && expectedDate.before(secondValue)))
         {
            if (!(expectedDate.equals(firstValue) || expectedDate.equals(secondValue)))
            {
               return false;
            }
         }
      }
      return true;

   }

   /**
    * Reads the given file and check for the given values is exist.
    * 
    * @param cSVFile
    *           Path of CSV file
    * @param columnName
    *           column name.
    * @param isColumnValue
    *           column value to be checked.
    * @return <li><code>True </code>if CSV files contains vales</li> <li>
    *         <code>False </code>otherwise</li>
    */
   public static boolean checkIsValuesInCSV(final String cSVFile, final String columnName, final Date isColumnValue)
   {
      return checkValuesInCSV(cSVFile, columnName, "Is", isColumnValue, null);
   }

   /**
    * Reads the given file and check for the given column has value between
    * given range.
    * 
    * @param cSVFile
    *           Path of CSV file
    * @param columnName
    *           column name.
    * @param fromDate
    *           fromDate to be checked.
    * @param toDate
    *           toDate to be checked.
    * @return <li><code>True </code>if CSV files contains vales</li> <li>
    *         <code>False </code>otherwise</li>
    */
   public static boolean checkBetweenValuesInCSV(final String cSVFile, final String columnName, final Date fromDate,
         final Date toDate)
   {
      return checkValuesInCSV(cSVFile, columnName, "Between", fromDate, toDate);
   }

   /**
    * Reads the given file and check for the given values is exist.
    * 
    * @param cSVFile
    *           Path of CSV file
    * @param columnName
    *           column name.
    * @param isColumnValue
    *           column value to be checked.
    * @return <li><code>True </code>if CSV files contains vales</li> <li>
    *         <code>False </code>otherwise</li>
    */
   public static boolean checkIsValuesInCSV(final String cSVFile, final String columnName, final String isColumnValue)
   {
      return checkValuesInCSV(cSVFile, columnName, "Is", isColumnValue, null);
   }

   /**
    * Trim the double quotes from the given string.
    * 
    * @param text
    *           the string to be trimmed.
    * @return the trimmed string
    */
   private static String trimDoubleQuotes(final String text)
   {
      int len = text.length();
      if (!text.isEmpty() && text.substring(0, 1).equals("\"") && text.substring(len - 1, len).equals("\""))
      {
         return text.substring(1, len - 1);
      }
      return text;

   }

   /**
    * Reads the given file and check for count exist for all fields and also
    * checks for Sum and Avg.
    * 
    * @param cSVFile
    *           Path of CSV file
    * @return <li><code>True </code>if CSV files contains Count, Sum and Avg</li>
    *         <li>
    *         <code>False </code>otherwise</li>
    * 
    */
   public static boolean checkCountExistInCSV(final String cSVFile)
   {
      String strLine;
      boolean isAvgAvailable = false;
      boolean isSumAvailable = false;
      try
      {
         FileInputStream fileStream = new FileInputStream(cSVFile);
         DataInputStream input = new DataInputStream(fileStream);

         BufferedReader reader = new BufferedReader(new InputStreamReader(input));
         String[] columnNames = null;
         boolean firstRow = true;
         while ((strLine = reader.readLine()) != null)
         {
            String[] rowValues = strLine.split(",");
            if (firstRow)
            {
               columnNames = rowValues;
               firstRow = false;
            }
            if (rowValues[0].contains("Count"))
            {
               for (int i = 1; i <= rowValues.length - 1; i++)
               {
                  // Checks for Count should not have " (Double quotes)
                  if (rowValues[i].contains("\""))
                  {
                     System.out.println("Count has double quote \" for" + columnNames[i]);
                     return false;
                  }
                  // Checks for Count must exist for all fields
                  if (rowValues[i].isEmpty())
                  {
                     System.out.println("Count does not exist for" + columnNames[i]);
                     return false;
                  }
               }
            }
            if (rowValues[0].contains("Avg"))
            {
               isAvgAvailable = true;
            }
            if (rowValues[0].contains("Sum"))
            {
               isSumAvailable = true;
            }
         }
         // Close the input stream
         reader.close();
         input.close();

      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
      if (!isSumAvailable)
      {
         // Checks for Sum exists in the given CSV Files
         System.out.println("Sum does not exist");
         return false;
      }
      if (!isAvgAvailable)
      {
         // Checks for Avg exists in the given CSV Files
         System.out.println("Avg does not exist");
         return false;
      }
      return true;
   }

   /**
    * Reads the given file and check for the given vaules are exist.
    * 
    * @param cSVFile
    *           Path of CSV file
    * @param summaryTypes
    *           summary type to be checked
    * @param verifyColNameAndValue
    *           column name and value to be verified in the summary.
    * @param filterColNameAndValue
    *           column name and value to be checked.
    * @return <li><code>True </code>if CSV files contains vales</li> <li>
    *         <code>False </code>otherwise</li>
    */
   public static boolean checkSummaryValuesInCSV(final String cSVFile, final String[] summaryTypes,
         final String[] verifyColNameAndValue, final String[] filterColNameAndValue)
   {
      String strLine;
      int success = 0;
      try
      {
         FileInputStream fileStream = new FileInputStream(cSVFile);
         DataInputStream input = new DataInputStream(fileStream);

         BufferedReader reader = new BufferedReader(new InputStreamReader(input));
         String[] columnNames = null;
         boolean firstRow = true;
         boolean checkSummary = false;
         while ((strLine = reader.readLine()) != null)
         {
            String[] rowValues = strLine.split(",");
            if (firstRow)
            {
               columnNames = rowValues;
               firstRow = false;
            }
            if (filterColNameAndValue != null && !checkSummary)
            {
               for (int rowIndex = 1; rowIndex <= rowValues.length - 1; rowIndex++)
               {
                  if (trimDoubleQuotes(columnNames[rowIndex]).equals(filterColNameAndValue[0]))
                  {
                     if (trimDoubleQuotes(rowValues[rowIndex]).equals(filterColNameAndValue[1]))
                     {
                        checkSummary = true;
                        break;
                     }
                  }
               }
            }
            for (int summaryIndex = 0; summaryIndex <= summaryTypes.length - 1; summaryIndex++)
            {
               if (rowValues[0].contains(summaryTypes[summaryIndex])
                     && (checkSummary || (filterColNameAndValue == null)))
               {
                  for (int i = 1; i <= rowValues.length - 1; i++)
                  {
                     if (trimDoubleQuotes(columnNames[i]).equals(verifyColNameAndValue[0]))
                     {
                        System.out
                              .println(summaryTypes[summaryIndex] + " is " + rowValues[i] + " in " + columnNames[i]);
                        String value = (verifyColNameAndValue[summaryIndex + 1] == null) ? ""
                              : verifyColNameAndValue[summaryIndex + 1];

                        if (value.equals(trimDoubleQuotes(rowValues[i])))
                        {
                           success++;
                           if (success == summaryTypes.length)
                           {
                              return true;
                           }
                        }
                        else
                        {
                           System.out.println("Error: " + summaryTypes[summaryIndex] + " is "
                                 + verifyColNameAndValue[summaryIndex + 1] + " in " + columnNames[i]);
                           return false;
                        }
                     }
                  }
               }
            }
         }
         // Close the input stream
         reader.close();
         input.close();

      }
      catch (Exception e)
      {
         e.printStackTrace();
         return false;
      }
      if (success != summaryTypes.length)
      {
         System.out.println("Given summary is not Matched with CSV");
         return false;
      }
      return true;
   }

   /**
    * Compares the two CSV files.
    * 
    * @param expectedFile
    *           Path of expected CSV file
    * @param actualFile
    *           Path of actual CSV file
    * 
    * @return <li><code>True </code>if CSV files Compared successfully</li> <li>
    *         <code>False </code>otherwise</li>
    */
   public static boolean compareTwoCSVFile(final String expectedFile, final String actualFile)
   {
      String[] rowValuesOfExpectedFile = null;
      String[] rowValuesOfactualFile = null;
      String strLineExpectedFile = null;
      String strLineActualFile = null;
     
      try
      {

         FileInputStream fileStream1 = new FileInputStream(expectedFile);
         DataInputStream input1 = new DataInputStream(fileStream1);
         BufferedReader reader1 = new BufferedReader(new InputStreamReader(input1));

         FileInputStream fileStream2 = new FileInputStream(actualFile);
         DataInputStream input2 = new DataInputStream(fileStream2);
         BufferedReader reader2 = new BufferedReader(new InputStreamReader(input2));
         int rowCount = 0;
         boolean flag = false;
         while (((strLineExpectedFile = reader1.readLine()) != null)
               && ((strLineActualFile = reader2.readLine()) != null))
         {
            int columnReportGenerationDate = 0;

            rowValuesOfExpectedFile = strLineExpectedFile.split(",");
            rowValuesOfactualFile = strLineActualFile.split(",");

            // ignore the report generation date which is the 1st column of the 1st row
            // sometimes it is the 2nd column (ie. PO credit details report)

            if (rowValuesOfExpectedFile[0] == "")
            
            {
               columnReportGenerationDate = 1;
            }

            for (int i = columnReportGenerationDate + 1; i <= rowValuesOfExpectedFile.length - 1; i++)
            {
             
               //  The below code will ignore date and time which is in any column 
               if (!(rowValuesOfExpectedFile[i].contains("/")))
               {
                  if (!(rowValuesOfExpectedFile[i].contains(":")))
                  {
               
               if (!(rowValuesOfExpectedFile[i].equalsIgnoreCase(rowValuesOfactualFile[i])))
               {
                  System.out.println("There is difference in Row:" + rowCount);
                  System.out.println("Expected File:" + rowValuesOfExpectedFile[i]);
                  System.out.println("Actual File:" + rowValuesOfactualFile[i]);
                  flag = false;
                  input1.close();
                  reader1.close();
                  fileStream1.close();
                  
                  input2.close();
                  reader2.close();
                  fileStream2.close();
                  return false;
               }
               else
               {
                  flag = true;
               }
              }
            }
         }
            System.out.println("Row: " + rowCount
                  + " Compared successfully.  Report generation date column has been ignored.");

            rowCount++;
         }

         // Close the input stream
         input1.close();
         reader1.close();
         fileStream1.close();
         
         input2.close();
         reader2.close();
         fileStream2.close();
         
         return flag;
      }
      catch (Exception e)
      {
         e.printStackTrace();
         return false;
      }
   }

}
