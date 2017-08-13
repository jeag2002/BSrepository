package com.bs.analysis.cashanalysistool.output.excel.beans;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

import org.apache.log4j.Logger;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFCell;

import com.bs.analysis.cashanalysistool.output.xml.SamplePatternTrace;

public class excelsheetdummy{
	int rows = 0;
	int columns = 0;
	HSSFSheet datos;
	Object[][] info;
	
	
	/**
	 * Constructor
	 * @param data_info	sheet con la info de la página que se va a tratar
	 * @param rows	numero de filas
	 * @param columns numero de columnas
	 * @throws Exception
	 */
	
	public excelsheetdummy(HSSFSheet data_info, Vector templates, int rows, int columns) throws Exception{	
		
		Logger log = Logger.getLogger(excelsheet.class);
		
		this.rows = rows;
		this.columns = columns+1;
		this.datos = data_info;
		
		info = new Object[this.rows][this.columns];
		
		
		if (columns == 0){
			throw new Exception("don't exist series for proccessing");
		}
		
		if (rows == 0){
			throw new Exception("don't exist pattern traces for formatting");
		}
		
		//se crea el espacio dentro del archivo EXCEL donde trabajar
		for(int i=0; i<this.rows; i++){
			for(int j=0; j<this.columns; j++){
				if (j==0){
					info[i][j] = (String)templates.get(i);
				}
				else{
					info[i][j] = "0";
				}
				
			}//fin bucle
		}//fin del bucle
	}
	
	
	public void setData(int serieOrder, int traceOrder, long Data){
		
		if ((traceOrder < this.rows) && ((serieOrder+1) < this.columns)){
			info[traceOrder][serieOrder+1] = String.valueOf(Data);
		}
	}
	
	public HSSFSheet getHSSFSheet(){
		return datos;
	}
	
	
	public String toString(){
		String cadena = "\n";
		
		for (int j=0; j<this.columns; j++){
			cadena += "[ serie " + j + " : ";
			
			for(int i=0; i<this.rows; i++){
				cadena += info[i][j] +  " ";
			}
			
			cadena += "]\n";
		}
		return cadena;

	}
	
}
