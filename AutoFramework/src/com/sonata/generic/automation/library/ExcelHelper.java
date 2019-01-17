/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.library;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellReference;

/**
 * ExcelHelper class helps to implement the methods that help working with Excel
 * Files.
 * 
 * @author Raghunath
 */
public final class ExcelHelper {

	/**
	 * The constructor was made private so it can never be instantiated.
	 */
	private ExcelHelper() {

	}

	/**
	 * Reads the given file and get the summation result.
	 * 
	 * @param excelFile
	 *            Path of Excel file
	 * @param summationCellLocation
	 *            the cell where summation result is present
	 * @return the summation value from the specified location
	 */
	public static double getSummationResult(final String excelFile,
			final String summationCellLocation) {

		InputStream myxls;
		HSSFWorkbook wb;
		double summationResult = 0;
		try {
			myxls = new FileInputStream(excelFile);
			wb = new HSSFWorkbook(myxls);
			CellReference cellReference = new CellReference(
					summationCellLocation);
			HSSFSheet sheet = wb.getSheetAt(0);
			HSSFRow row = sheet.getRow(cellReference.getRow());
			HSSFCell cell = row.getCell((int) cellReference.getCol());
			HSSFFormulaEvaluator evaluator = new HSSFFormulaEvaluator(wb);
			if (evaluator.evaluateInCell(cell).getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
				summationResult = cell.getNumericCellValue();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return summationResult;
	}

	/**
	 * Summation process of the specified cells.
	 * 
	 * @param excelName
	 *            path of excel file
	 * @param tempFile
	 *            the temporary file
	 * @param startingCellLocation
	 *            starting cell location for summation
	 * @param endingCellLocation
	 *            ending cell location for summation
	 * @param summationCellLocation
	 *            summation cell location where summation of
	 *            startingCellLocation and endingCellLocation done
	 * @return <li><code>True </code>if summation takes place</li> <li>
	 *         <code>False </code>otherwise</li>
	 */
	public static boolean summationOfCells(final String excelName,
			final String tempFile, final String startingCellLocation,
			final String endingCellLocation, final String summationCellLocation) {
		FileOutputStream fileOut;
		InputStream myxls;
		HSSFWorkbook wb;
		try {
			myxls = new FileInputStream(excelName);
			fileOut = new FileOutputStream(tempFile);
			wb = new HSSFWorkbook(myxls);
			CellReference cellReference = new CellReference(
					summationCellLocation);
			HSSFSheet sheet = wb.getSheetAt(0);
			HSSFRow row = sheet.getRow(cellReference.getRow());
			HSSFCell cell = row.createCell((int) cellReference.getCol());
			String semicolon = ":";
			String formulaString = "SUM(" + startingCellLocation + semicolon
					+ endingCellLocation + ")";
			if (cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
				cell.setCellFormula(formulaString);
			}
			wb.write(fileOut);
			fileOut.flush();
			fileOut.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Reads the given file and get the numeric data from the specified cell.
	 * 
	 * @param excelName
	 *            Path of Excel file
	 * @param cellLocation
	 *            the data to be retrieved from this cell location
	 * @return the numeric data
	 */
	public static Double getNumericDataFromCell(final String excelName,
			final String cellLocation) {

		InputStream inputStream;
		HSSFWorkbook wb;
		double value = 0;
		try {
			inputStream = new FileInputStream(excelName);
			wb = new HSSFWorkbook(inputStream);
			CellReference cellReference = new CellReference(cellLocation);
			HSSFSheet sheet = wb.getSheetAt(0);
			HSSFRow row = sheet.getRow(cellReference.getRow());
			HSSFCell cell = row.getCell((int) cellReference.getCol());
			value = cell.getNumericCellValue();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * Reads the given file and checks the cell format.
	 * 
	 * @param excelName
	 *            Path of Excel file
	 * @param cellLocation
	 *            the cell location where format of cell to retrieve
	 * @return the format of cell
	 */
	public static String checkCellFormat(final String excelName,
			final String cellLocation) {

		InputStream inputStream;
		HSSFWorkbook wb;
		try {
			inputStream = new FileInputStream(excelName);
			wb = new HSSFWorkbook(inputStream);
			CellReference cellReference = new CellReference(cellLocation);
			HSSFSheet sheet = wb.getSheetAt(0);
			HSSFRow row = sheet.getRow(cellReference.getRow());
			HSSFCell cell = row.getCell((int) cellReference.getCol());
			if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
				return "NUMERIC";
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Delete the file.
	 * 
	 * @param tempFilelName
	 *            path of the file to delete
	 * 
	 * @return <li><code>True </code>if deletion takes place</li> <li>
	 *         <code>False </code>otherwise</li>
	 */
	public static boolean deleteFile(final String tempFilelName) {
		File tempFile = new File(tempFilelName);
		return tempFile.delete();
	}

	/**
	 * Checks excel file is readable.
	 * 
	 * @param tempFilelName
	 *            path of the file to read
	 * 
	 * @return <li><code>True </code>if file is readable</li> <li>
	 *         <code>False </code>otherwise</li>
	 */
	public static boolean verifyExcelFileIsReadable(final String tempFilelName) {
		File tempFile = new File(tempFilelName);
		return tempFile.canRead();
	}

	/**
	 * Reads the given excel file and get the last row number.
	 * 
	 * @param excelName
	 *            Path of Excel file
	 * @return the lastRowNumber
	 */
	public static String getLastRowNumberFromExcel(final String excelName) {

		InputStream inputStream;
		HSSFWorkbook wb;
		String value = "";
		try {
			inputStream = new FileInputStream(excelName);
			wb = new HSSFWorkbook(inputStream);
			HSSFSheet sheet = wb.getSheetAt(0);
			// Apache POI counts Row starting from 0. So the result has been
			// added with 1.
			int lastRowNumber = sheet.getLastRowNum() + 1;
			value = lastRowNumber + "";
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return value;
	}
}
