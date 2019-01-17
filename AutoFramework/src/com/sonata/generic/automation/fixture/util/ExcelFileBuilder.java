/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.fixture.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;



/**
 * The <code>ExcelFileBuilder</code> provide interfaces to interact with excel file
 * to access the configuration or test data, and write back to file for validation or 
 * other cases' reference.
 */
public class ExcelFileBuilder
{
   private static String PATH = new File("").getAbsolutePath();
   private int rowNum = 0;
   private int colNum = 0;
   private String[][] data;
   
   /**
    * Open the excel file, read and put the data to a cache for later access.
    * @param file 
    *            The excel(.xls) file where the configuration or test data is saved.
    * @param sheetIdx 
    *            The index of the excel sheet, begin from "0".           
    * @throws IllegalStateException
    *            The document builder could not be created with to satisfy the
    *            default configuration.
    */
   public ExcelFileBuilder(final String file, final int sheetIdx)
   {
      try
      {
         String fullFilePath = PATH + "//" + file; 
         
         FileInputStream excel = new FileInputStream(new File(fullFilePath));
         HSSFWorkbook workbook = new HSSFWorkbook(excel);
         HSSFSheet ws = workbook.getSheetAt(sheetIdx);
         FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
          
         rowNum = ws.getLastRowNum() + 1;
         colNum = ws.getRow(0).getLastCellNum();
         
         data = new String[rowNum][colNum];

         for (int i=0; i<rowNum; i++)
         {
            HSSFRow row = ws.getRow(i);
            for (int j=0; j<colNum; j++)
            {
                HSSFCell cell = row.getCell(j);
                String value = cellToString(cell, evaluator);
                data[i][j] = value;
            }
         }
         excel.close();
      }catch (FileNotFoundException e)
      {
         e.printStackTrace();
      }catch (IOException e)
      {
         e.printStackTrace();
      }
   }

   /**
    * Get data from excel cell.
    * @param cell 
    *            A cell in excel sheet.
    * @param evaluator 
    *            used to get the shown value instead of the formula.    
    * @return a String value of the cell.
    *        
    * @throws IllegalArgumentException
    *            Throw exception when the type is not supported like Error, Formula.
    */     
   public static String cellToString (HSSFCell cell, FormulaEvaluator evaluator)
   {
      int type;
      Object result;
      
      if(cell == null)
         return "";
      
      type = evaluator.evaluateInCell(cell).getCellType(); 
      switch(type) 
      {
         case Cell.CELL_TYPE_STRING:
            result = cell.getStringCellValue();
            break;
         case Cell.CELL_TYPE_NUMERIC:
            if(DateUtil.isCellDateFormatted(cell)){
               SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
               result = dateFormat.format(cell.getDateCellValue());
            }else{
               result = cell.getNumericCellValue();
            }
            break;
         case Cell.CELL_TYPE_BOOLEAN:
            result = cell.getBooleanCellValue ();
            break;
         case Cell.CELL_TYPE_BLANK:
            result = "";
            break;     
         default:
            throw new IllegalArgumentException("The data type is not supported.");
      }
      return result.toString();
   }
   
   /**
    * Get the element Value configured in excel file keyed by elementName in 
    * the first column.
    * 
    * @param elementName
    *           The element name, suggest same as the widget name in LayOutMap.
    * @param columnIdx
    *           The column index where the element value is set.                
    * @return The element value set in excel file.
    * 
    * @throws IllegalArgumentException
    *            Occurred when the element can't be found.
    */
   public String getData(final String elementName, final int columnIdx)
   {
      for (int i=0; i<rowNum; i++)
      {
         if (data[i][0].equals(elementName))
         {
            return data[i][columnIdx];
         }
      }
      throw new IllegalArgumentException("Can't find element '" + elementName + "'");
   }
   
   /**
    * Set Value to an element in cache temporarily
    * 
    * @param elementName
    *           The element name, suggest same as the widget name in LayOutMap.
    * @param columnIdx
    *           The column index where the element value is set.                               
    * @param value
    *           The value to be set to the element.
    * @throws IllegalArgumentException
    *            Occurred when the element can't be found.
    */
   public void setData(final String elementName, final int columnIdx, final String value)
   {
      for (int i=0; i<rowNum; i++)
      {
         if (data[i][0].equals(elementName))
         {
            data[i][columnIdx] = value;
            return;
         }
      }
      throw new IllegalArgumentException("Can't find element '" + elementName + "'");
   }
   
   /**
    * Open the excel file, update the value.
    * @param file 
    *            The excel(.xls) file where the value to be updated.
    * @param sheetIdx 
    *            The index of the excel sheet, begin from "0".           
    * @throws IllegalStateException
    *            The document builder could not be created with to satisfy the
    *            default configuration.
    */
   public void updateExcel(final String file, final int sheetIdx)
   {
      try
      {
         String fullFilePath = PATH + "//" + file; 
         
         FileInputStream excel = new FileInputStream(new File(fullFilePath));
         HSSFWorkbook workbook = new HSSFWorkbook(excel);
         
         HSSFCellStyle style = workbook.createCellStyle();
         
         HSSFFont font = workbook.createFont();
         font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
         style.setFont(font);

         style.setFillForegroundColor(new HSSFColor.YELLOW().getIndex());
         style.setFillPattern(CellStyle.SOLID_FOREGROUND);
         
         HSSFSheet ws = workbook.getSheetAt(sheetIdx);
         
         FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
        
         for (int i=1; i<rowNum; i++)
         {
            HSSFRow row = ws.getRow(i);
            for (int j=1; j<colNum; j++)
            {
                HSSFCell cell = row.getCell(j);
                String value = cellToString(cell, evaluator); 
                if (!data[i][j].equals(value))
                {
                   if(cell == null)
                   {
                      cell = row.createCell(j);
                   }
                   cell.setCellStyle(style);
                   cell.setCellValue(data[i][j]);
                }
            }
         }
         excel.close();
         
         FileOutputStream outFile =new FileOutputStream(new File(fullFilePath));
         workbook.write(outFile);
         outFile.close();
      }catch (FileNotFoundException e)
      {
         e.printStackTrace();
      }catch (IOException e)
      {
         e.printStackTrace();
      }
   }
}
