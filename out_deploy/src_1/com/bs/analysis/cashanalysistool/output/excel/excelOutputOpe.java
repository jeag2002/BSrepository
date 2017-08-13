package com.bs.analysis.cashanalysistool.output.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.bs.analysis.cashanalysistool.driver.parser.SamplingDataOperationSeries;
import com.bs.analysis.cashanalysistool.driver.parser.SamplingDataParsed;
import com.bs.analysis.cashanalysistool.driver.parser.SamplingOperation;
import com.bs.analysis.cashanalysistool.driver.parser.SamplingSerie;
import com.bs.analysis.cashanalysistool.driver.xml.SampleConfig;
import com.bs.analysis.cashanalysistool.driver.xml.SamplePatternVec;


import com.bs.analysis.cashanalysistool.output.xml.*;
import com.bs.analysis.cashanalysistool.output.excel.beans.excelsheet;


import org.apache.commons.digester.Digester;
import org.apache.commons.digester.xmlrules.DigesterLoader;

import org.apache.log4j.*;

import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;



public class excelOutputOpe{
	
	private SamplingOperation ope;	
	private Vector skilltemplate;
	private String templatePath;
	private POIFSFileSystem excelFile;
	private SampleConfig sysconf;
	
	private Logger log = Logger.getLogger(excelOutputOpe.class);
	
	//ope, skillstemplate, templatePath
	public excelOutputOpe(SamplingOperation ope1, Vector skilltemplate, String templatePath, SampleConfig sysconf) throws Exception {
		this.ope = ope1;
		this.skilltemplate = skilltemplate;
		this.excelFile = new POIFSFileSystem(new FileInputStream(templatePath));
		this.sysconf = sysconf;
	}

	public void processOperation() throws Exception{
		HSSFWorkbook wb = new HSSFWorkbook(excelFile);
	    processOperationTimes(wb.getSheet("Hoja1"), skilltemplate, ope.getSeries());
	    processOperationMedia(wb.getSheet("Hoja3"), skilltemplate, ope.getSeries());
	    setGeneralVariables(wb.getSheet("Hoja5"),skilltemplate,ope.getSeries());
	    FileOutputStream fileOut = new FileOutputStream("./out/"+ope.getOperation()+".xls");
	    wb.write(fileOut);
	    fileOut.close();
	    log.info("Generated new file ./out/"+ope.getOperation()+".xls");
	}
	
	
	
	private int search_trace(int find_id, String ID, excelsheet xlssheet, int serie, String data_fin, String data_ini, Vector skilltemplate) throws Exception{
		int i = -1;
		int j=0;
		boolean encontrado = false;
		
		SimpleDateFormat form1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
		
		for(j=find_id; (j<skilltemplate.size() && (!encontrado)); j++){
			encontrado = skilltemplate.get(j).equals(ID);
			if (encontrado){break;}
			else{
				if (log.isDebugEnabled()){
					log.debug("Act element " + skilltemplate.get(j) + "order " + j +  " element to compare " + ID + " diff " +  (form1.parse(data_fin).getTime() - form1.parse(data_ini).getTime()) );
				}
				xlssheet.setData(serie,j,form1.parse(data_fin).getTime() - form1.parse(data_ini).getTime());
			}
		}
		
		if (encontrado){i=j;}
		
		return i;
	}
	
	
	private int search_trace_media(int find_id, String ID, Vector skilltemplate) throws Exception{
		int i = -1;
		int j=0;
		boolean encontrado = false;
		
		
		for(j=find_id; (j<skilltemplate.size() && (!encontrado)); j++){
			encontrado = skilltemplate.get(j).equals(ID);
			if (encontrado){break;}
			//en este caso, no hace falta ningun tratamiento especial. solo encontrar si existe
			//el elemento buscado. Si no existe, res se deja a 0
		}
		
		if (encontrado){i=j;}
		
		return i;
	}
	
	private void setGeneralVariables(HSSFSheet sheet1, Vector skilltemplate, Vector series) throws Exception{
		excelsheet xlssheet = new excelsheet(sheet1, 2, 1);
		xlssheet.setData(0,0,skilltemplate.size());
		xlssheet.setData(0,1,series.size());
	}
	
	private void processSerieOperationTime(excelsheet xlssheet, int serieOrder, String dataini, SamplingSerie ser1, Vector skilltemplate) throws Exception{
		Hashtable serie = ser1.getContainer();
		SamplingDataParsed SPD = null;
		
		SimpleDateFormat form1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
		
		Vector vect = new Vector(serie.keySet());
		Collections.sort(vect);
		Enumeration en = vect.elements();
		String keys = "";
		
		String ID = "";
		
		String data_begin = "";
		String data_ini = "";
		String data_old = "";
		
		boolean encontrado = false;
		
		int i=0;
		int j=0;
		int k=0;
		
		int find_id = 0;
		int before_skill = 0;
		
		long diff = 0;
		
		data_begin = dataini;
		data_old = data_begin;
		
		long[] reg_buffer = new long[skilltemplate.size()];
		
		for (k=0; k<reg_buffer.length; k++){reg_buffer[k] = 0;}
		
			while (en.hasMoreElements())
			{
				keys = (String)en.nextElement();
				SPD = (SamplingDataParsed)serie.get(keys);
				//solo nos interesa parsear las operaciones de fin
				diff = SPD.getDiff();	
				
				if (diff !=0){
					ID = SPD.getTit();
					data_ini = SPD.getEnd();
					
					before_skill = find_id;
					find_id = search_trace(find_id,ID, xlssheet, serieOrder, data_old,data_begin,skilltemplate);
					
						if ((find_id!=-1) && (find_id < skilltemplate.size())){
							if (log.isDebugEnabled()){	
								log.debug("find element ID " + ID + " order " + find_id + " begin time " + data_ini + " ref time " + data_begin + " diff " + (form1.parse(data_ini).getTime() - form1.parse(data_begin).getTime()));
							}
							reg_buffer[find_id] = form1.parse(data_ini).getTime() - form1.parse(data_begin).getTime();	
							data_old = data_ini;
						}else{
							find_id = before_skill; 
						}
				}
			}
		
			
		long val_ant = reg_buffer[0];	
		
		for(k=0;k<reg_buffer.length;k++){
			log.debug("valor obtenido "+reg_buffer[k]);
			
			if (reg_buffer[k] == (long)0){
				xlssheet.setData(serieOrder,k,val_ant);
			}else{
				xlssheet.setData(serieOrder,k,reg_buffer[k]);
				val_ant = reg_buffer[k];
			}
		}
			
			
			
	}
	
	private void processOperationTimes(HSSFSheet sheet1, Vector skilltemplate, Vector series) throws Exception{
		//se crea el objeto excel necesario
		//HSSFSheet data_info, Vector skills, int rows, int columns
		
		excelsheet xlssheet = new excelsheet(sheet1, skilltemplate, skilltemplate.size(), series.size());
		//regla: se guia por los skills recogidos del fichero de configuracion. Si estos estan, estos se presentan
		
		String operation = "";
		String data_ini = "";
		for (int i=0; i<series.size(); i++){
			SamplingDataOperationSeries ser1 = (SamplingDataOperationSeries) series.get(i);
			SamplingSerie SS = ser1.getContainer();
			processSerieOperationTime(xlssheet,i,ser1.getDataini(),SS,skilltemplate);
		}
		
		log.info("sheet - (operation time) processed for operation " + ope.getOperation() );		
	}
	
	
	private void processSerieOperationMedia(excelsheet xlssheet, int i, SamplingSerie SS, Vector skilltemplate) throws Exception{
		
		Hashtable serie = SS.getContainer();
		SamplingDataParsed SPD = null;
		
		SimpleDateFormat form1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
		
		Vector vect = new Vector(serie.keySet());
		Collections.sort(vect);
		Enumeration en = vect.elements();
		String keys = "";
		
		String ID = "";
		long diff = 0;
		
		boolean encontrado = false;
		int j=0;
		
		int find_id = 0;
		int before_skill = 0;
	
		
			while (en.hasMoreElements())
			{
				keys = (String)en.nextElement();
				SPD = (SamplingDataParsed)serie.get(keys);
				ID = SPD.getTit(); diff = SPD.getDiff();		
				
				if (!ID.equalsIgnoreCase(sysconf.getId_ini()) && !ID.equalsIgnoreCase(sysconf.getId_enc()) && !ID.equalsIgnoreCase(sysconf.getId_fin()) && !ID.equalsIgnoreCase(sysconf.getId_fin_enc())){
					if (diff!=0){
						before_skill = find_id;
						find_id = search_trace_media(find_id,ID,skilltemplate);	
					
						if ((find_id!=-1) && (find_id < skilltemplate.size())){
							xlssheet.setData(i,find_id,diff);
						}
						else{
							find_id = before_skill;
						}
					}
				}
				
			}	
	}
	
	
	private void processOperationMedia(HSSFSheet sheet1, Vector skilltemplate, Vector series) throws Exception{
		excelsheet xlssheet = new excelsheet(sheet1, skilltemplate, skilltemplate.size(), series.size());
		//regla: se guia por los skills recogidos del fichero de configuracion. Si estos estan, estos se presentan
		//en caso contrario, el fichero no se genera.
		
		String operation = "";
		String data_ini = "";
		for (int i=0; i<series.size(); i++){
			SamplingDataOperationSeries ser1 = (SamplingDataOperationSeries) series.get(i);
			SamplingSerie SS = ser1.getContainer();
			processSerieOperationMedia(xlssheet,i,SS,skilltemplate);
		}
		
		log.info("sheet - (operation media) processed for operation " + ope.getOperation() );
	
	}
	
	
	
}
