package com.bs.analysis.cashanalysistool.output.excel.beans;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

import org.apache.log4j.Logger;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFCell;


import com.bs.analysis.cashanalysistool.output.xml.SamplePatternTrace;

public class excelsheet{
	int rows = 0;
	int columns = 0;
	HSSFSheet datos;
	private Logger log = Logger.getLogger(excelsheet.class);
	/**
	 * Constructor
	 * @param data_info	sheet con la info de la página que se va a tratar
	 * @param rows	numero de filas
	 * @param columns numero de columnas
	 * @throws Exception
	 */
	
	public excelsheet(HSSFSheet data_info, Vector templates, int rows, int columns) throws Exception{	
		
		
		
		this.rows = rows+1;
		this.columns = columns+1;
		this.datos = data_info;
		
		HSSFRow row;
		HSSFCell cell;
		
		if (columns == 0){
			log.error("Not pattern traces for operation");
		}
		
		//se crea el espacio dentro del archivo EXCEL donde trabajar
		for(int i=0; i<this.rows; i++){
			//se crean las filas
			row = datos.getRow(i);
			if (row == null){
				row = datos.createRow((short)i);
			}
			//se crean las columnas
			for(int j=0; j<this.columns; j++){					
				if (i == 0){
					if (j == 0){
						cell = row.getCell((short)j);
						if (cell == null){
			    		cell = row.createCell((short)j);
						}		
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue("");
					}else{
						cell = row.getCell((short)j);
						if (cell == null){
			    		cell = row.createCell((short)j);
						}		
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue("serie"+j);
					}
				
				}//i==0
				else{
					if (j == 0){
						cell = row.getCell((short)j);
						if (cell == null){
							cell = row.createCell((short)j);
						}
						String dato = (String)templates.get(i-1);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue(dato);
					}else{
						cell = row.getCell((short)j);
						if (cell == null){
			    		cell = row.createCell((short)j);
						}		
					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					cell.setCellValue(0);
					}
				}//i!=0
			}//fin del bucle j 
		}//fin del bucle i
	}
	
	public excelsheet(HSSFSheet data_info,int rows, int col){
		
		HSSFRow row;
		HSSFCell cell;
		
		this.rows = rows+1;
		this.columns = col+1;
		this.datos = data_info;
		
		if (columns == 0){
			log.error("Not pattern traces for operation");
		}
		
		for(int i=0; i<this.rows; i++){
			//se crean las filas
			row = datos.getRow(i);
			if (row == null){
				row = datos.createRow((short)i);
			}
			//se crean las columnas
			for(int j=0; j<this.columns; j++){
				
				if (i==0){
					cell = row.getCell((short)j);
					if (cell == null){
						cell = row.createCell((short)j);
					}		
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					if (j<0){
						cell.setCellValue("Variables Globales");
					}else{
						cell.setCellValue("");
					}
					
				}else{
					
					if (j==0){
						cell = row.getCell((short)j);
						if (cell == null){
							cell = row.createCell((short)j);
						}		
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue("Param-"+i);
						
					}else{
						cell = row.getCell((short)j);
						if (cell == null){
							cell = row.createCell((short)j);
						}		
						cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
						cell.setCellValue(0);
					}
				}
			}//fin del bucle j 
		}//fin del bucle i
	}
	
	public void setData(int serieOrder, int traceOrder, long Data){
		HSSFRow row = datos.getRow((short)(traceOrder+1));
		HSSFCell cell = row.getCell((short)(serieOrder+1));
		cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		cell.setCellValue(Data);
	}
	
	public HSSFSheet getHSSFSheet(){
		return datos;
	}
	
}
