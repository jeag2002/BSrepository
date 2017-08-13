package com.bs.analysis.cashanalysistool.output.excel;


import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;


public class processExcelPOI{
	public static void main(String args[]) throws Exception
	{
		
		POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream("./res/plantilla.xls"));
	    HSSFWorkbook wb = new HSSFWorkbook(fs);
	    HSSFSheet sheet = wb.getSheetAt(0);
	    
	    //como prueba, se insertarán tres filas en el fichero de datos (primera hoja).
	   
	    /** Primera linea: paso-1 15 */
	    HSSFRow row = sheet.getRow(1);
	    if (row == null)
	    	row = sheet.createRow((short)1);
	    
	    HSSFCell cell = row.getCell((short)0);
	    	if (cell == null)
	    		cell = row.createCell((short)0);
	    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	    cell.setCellValue("paso-1");
	    
	    cell = row.getCell((short)1);
    	if (cell == null)
    		cell = row.createCell((short)1);
    	cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
    	cell.setCellValue(15);
	    
	    
	    /** Segunda linea: paso-2 11 */
	    
    	row = sheet.getRow(2);
	    if (row == null)
	    	row = sheet.createRow((short)2);
	    
	    cell = row.getCell((short)0);
	    	if (cell == null)
	    		cell = row.createCell((short)0);
	    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	    cell.setCellValue("paso-2");
	    
	    cell = row.getCell((short)1);
    	if (cell == null)
    		cell = row.createCell((short)1);
    	cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
    	cell.setCellValue(11);
    	
    	/** tercera linea: paso-3 20*/
    	
    	row = sheet.getRow(3);
	    if (row == null)
	    	row = sheet.createRow((short)3);
	    
	    cell = row.getCell((short)0);
	    	if (cell == null)
	    		cell = row.createCell((short)0);
	    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	    cell.setCellValue("paso-3");
	    
	    cell = row.getCell((short)1);
    	if (cell == null)
    		cell = row.createCell((short)1);
    	cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
    	cell.setCellValue(20);
    	
    	
    	sheet = wb.getSheetAt(2);
    	
    	/** Primera linea paso-1  30 25 12 */
    	row = sheet.getRow(1);
	    if (row == null)
	    	row = sheet.createRow((short)1);
	    cell = row.getCell((short)0);
	    	if (cell == null)
	    		cell = row.createCell((short)0);
	    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	    cell.setCellValue("paso-1");
	    
	    cell = row.getCell((short)1);
    	if (cell == null)
    		cell = row.createCell((short)1);
    	cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
    	cell.setCellValue(30);
    	
	    cell = row.getCell((short)2);
    	if (cell == null)
    		cell = row.createCell((short)2);
    	cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
    	cell.setCellValue(25);
    	
	    cell = row.getCell((short)3);
    	if (cell == null)
    		cell = row.createCell((short)3);
    	cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
    	cell.setCellValue(12);
	    
	    /** Segunda linea paso-2  32 30	20 */
    	
    	row = sheet.getRow(2);
	    if (row == null)
	    	row = sheet.createRow((short)2);
	    
	    cell = row.getCell((short)0);
	    	if (cell == null)
	    		cell = row.createCell((short)0);
	    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	    cell.setCellValue("paso-2");
	    
	    cell = row.getCell((short)1);
    	if (cell == null)
    		cell = row.createCell((short)1);
    	cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
    	cell.setCellValue(32);
    	
	    cell = row.getCell((short)2);
    	if (cell == null)
    		cell = row.createCell((short)2);
    	cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
    	cell.setCellValue(30);
    	
	    cell = row.getCell((short)3);
    	if (cell == null)
    		cell = row.createCell((short)3);
    	cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
    	cell.setCellValue(20);
    	
    	/** Tercera linea paso-3  33 33 30 */
    	
    	row = sheet.getRow(3);
	    if (row == null)
	    	row = sheet.createRow((short)3);
	    
	    cell = row.getCell((short)0);
	    	if (cell == null)
	    		cell = row.createCell((short)0);
	    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	    cell.setCellValue("paso-3");
	    
	    cell = row.getCell((short)1);
    	if (cell == null)
    		cell = row.createCell((short)1);
    	cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
    	cell.setCellValue(33);
    	
	    cell = row.getCell((short)2);
    	if (cell == null)
    		cell = row.createCell((short)2);
    	cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
    	cell.setCellValue(33);
    	
	    cell = row.getCell((short)3);
    	if (cell == null)
    		cell = row.createCell((short)3);
    	cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
    	cell.setCellValue(30);
    	
    
	    // Write the output to a file
	    FileOutputStream fileOut = new FileOutputStream("./out/output.xls");
	    wb.write(fileOut);
	    fileOut.close();
	    
	}
}
