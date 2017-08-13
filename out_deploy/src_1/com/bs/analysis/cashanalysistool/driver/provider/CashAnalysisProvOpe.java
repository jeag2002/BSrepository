package com.bs.analysis.cashanalysistool.driver.provider;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.xmlrules.DigesterLoader;
import org.apache.log4j.Logger;


import com.bs.analysis.cashanalysistool.driver.parser.*;
import com.bs.analysis.cashanalysistool.output.xml.SamplePatternOut;
import com.bs.analysis.cashanalysistool.output.xml.SamplePatternOutVect;
import com.bs.analysis.cashanalysistool.config.DataFileContainer;

public class CashAnalysisProvOpe{
	
	private static Hashtable container;
	private static CashAnalysisProvOpe INSTANCE = null;
	private static DataFileContainer conf = null;
	private static Logger log = Logger.getLogger(CashAnalysisProvOpe.class);
	
	
    private CashAnalysisProvOpe() 
    {
    	container = new Hashtable();
    	try{
    	conf = DataFileContainer.getInstance();
    	}catch (Exception e){}
    }

    private synchronized static void createInstance() {
        if (INSTANCE == null) { 
            INSTANCE = new CashAnalysisProvOpe();
        }
    }
    
    public static CashAnalysisProvOpe getInstance() {
        if (INSTANCE == null) createInstance();
        return INSTANCE;
    }

	public Hashtable getContainer() {
		return container;
	}

	public static void setContainer(Hashtable container) {
		CashAnalysisProvOpe.container = container;
	}
	
	
	public SamplingOperation getOperation(String key) throws Exception
	{
		SamplingOperation ope = new SamplingOperation();
		if (container.containsKey(key))
		{
			Object obj1 =  container.get(key);
			if (obj1 instanceof SamplingOperation)
			{
				ope = (SamplingOperation)obj1;
			}
		}
		else
		{
			throw new Exception("Operation " + key + " not found!");
		}
		return ope;
	}
	
	
	public void setOperationSeries(String key, SamplingSerie serie)
	{	
		SamplingOperation ope1 = null;
		if (container.containsKey(key))
		{
			ope1 = (SamplingOperation)container.get(key);
		}
		else
		{
			ope1 = new SamplingOperation(key,new Vector());
		}
		
		SamplingDataOperationSeries serie1 = new SamplingDataOperationSeries(String.valueOf(ope1.getSize()),key,serie);	
		ope1.addSerie(serie1);
		container.put(key,ope1);
	}
	
	
	public String toString()
	{
		String cadena = "\n";
		
		Enumeration en = container.keys();
		
		while (en.hasMoreElements())
		{
			String keys = (String)en.nextElement();
			SamplingOperation ope1 = (SamplingOperation)container.get(keys);
			cadena += ope1.toString();
		}
		return cadena;
	}
	
	
	public void formatSeries() throws Exception{
		Enumeration en = container.keys();
		
		while (en.hasMoreElements())
		{
			String keys = (String)en.nextElement();
			SamplingOperation ope1 =  (SamplingOperation)container.get(keys);
			ope1.setSeries(processSeries(ope1.getSeries(), ope1.getOperation()));
		}
	}
	
	
    private Vector processSeries(Vector serie_operation, String idOperation) throws Exception{
		Vector series = new Vector();
		Vector sampling_data = new Vector();
		Vector pattern_base_ids = new Vector();
		SamplingDataOperationSeries SDOS;
		
		Vector serie_nobase = new Vector();

		if (serie_operation.size() > 0){
			
			for (int i=0; i<serie_operation.size(); i++){
			
				if (i==0){
					SDOS = (SamplingDataOperationSeries)serie_operation.get(i);
					pattern_base_ids = ((SamplingDataOperationSeries)serie_operation.get(i)).getContainer().getIds();
					SDOS.setType(idOperation+"_base");
					series.add(SDOS);
				}else{
					SDOS = (SamplingDataOperationSeries)serie_operation.get(i);
					if (pattern_base_ids.equals(SDOS.getContainer().getIds())){
						SDOS.setType(idOperation+"_base");
						series.add(SDOS);
					}
					//Solo se recogen las series que tienen este esquema
					/*
					else{
						SDOS.setType(idOperation+"_nobase");
						serie_nobase.add(SDOS);
					}
					*/
				}
			}
			
			if (serie_nobase.size() > 0){
				series.addAll(serie_nobase);
			}
			return series;
		}else{
			return serie_operation;
		}
    }
    
    
    
    
    
    /*
	private Vector processSeries(Vector serie_operation, String idOperation, String func, String type) throws Exception{
		Vector series = new Vector();
		Vector sampling_data = new Vector();
		SamplePatternOut schema = new SamplePatternOut();
		Vector pattern = new Vector();

		//se comparará cada uno de las series con los patrones definidos en standardoutput.
		SamplePatternOutVect SPOV = ParseOutputXMLData(conf.getInfo("RulOutput"), conf.getInfo("FileOutput"));
		schema = SPOV.getSamplePatternOut(idOperation, type);
		//si la serie se corresponde, entonces es valido. Si no, se descrimina
					
		if (schema!=null){
				if (func.indexOf("FUNC")!=-1){
					pattern = schema.getFuncionalidades();
				}else{
					pattern = schema.getDetalles();
				}
				
				for (int i=0; i<serie_operation.size(); i++){
					sampling_data = ((SamplingDataOperationSeries)serie_operation.get(i)).getContainer().getIds();
					if (pattern.equals(sampling_data)){
						series.add(serie_operation.get(i));
					}
				}
		}
		else{
				log.warn("Schema not found. Put all the series as default");
				series = serie_operation;
		}
		return series;
	}
	*/
	
	/**
	 * Procesa todas las series de una operacion
	 */
	
	public Object[] formatSeriesFilterByPattern(String ConfigMode, String formatted) throws Exception{
		
		//obtiene toda la info de la operacion
		Vector vect_series = new Vector();
		Object[] selection_series = null;
		
		Enumeration en = container.keys();
		
		while (en.hasMoreElements())
		{
			String keys = (String)en.nextElement();
			SamplingOperation ope1 =  (SamplingOperation)container.get(keys);
			if (!formatted.equalsIgnoreCase("<no data>")){
				ope1.setSeries(processSeries(ope1.getSeries(), ope1.getOperation()));
			}
			vect_series.addAll(ope1.getSeries());
		}
		selection_series = vect_series.toArray();
		return selection_series;
	}
	
	
	/**
	 * Formateado de la estructura clasificada por fechas
	 * @return
	 */
	
	public String DataFormattedString(String Output, String ConfigMode, String formatted, String formatteddate) throws Exception{
		String cadena = "";
		boolean insert_ok = true;
		
		
		Object[] series = new Object[]{};		
		series = formatSeriesFilterByPattern(ConfigMode, formatted);
		
		
		//ordena por fechas
		if (!formatteddate.equalsIgnoreCase("<no data>")){
			java.util.Arrays.sort(series, new SeriesComparator());
		}
		

		//procesa la salida
		for (int i=0; i<series.length; i++){
			SamplingDataOperationSeries SDOS = (SamplingDataOperationSeries)series[i];
			if (Output.equalsIgnoreCase("CONSOLA")){
				cadena += SDOS.toString();
			}else{
				cadena += SDOS.BasicFormattedStringSerie();
			}
			
		}
		return cadena;
	}
	
	
	/**
	 * Formateado de la estructura clasificada por objetos
	 * @return
	 */
	
	public String BasicFormattedString(String ConfigMode, String formatted) throws Exception
	{	
		String cadena = "";
		Enumeration en = container.keys();
		
		if (!formatted.equalsIgnoreCase("<no data>")){
			formatSeriesFilterByPattern(ConfigMode, formatted);
		}
		
		while (en.hasMoreElements())
		{
			String keys = (String)en.nextElement();
			SamplingOperation ope1 = (SamplingOperation)container.get(keys);
			cadena += ope1.BasicFormattedStringOpe();
		}
		return cadena;
	}
	
}


class SeriesComparator implements Comparator {
	  Logger log = Logger.getLogger(SeriesComparator.class);
	  
	  public int compare(Object o1, Object o2){
		int i = 0;	  
			if(o1 instanceof SamplingDataOperationSeries && o2 instanceof SamplingDataOperationSeries) {
				SamplingDataOperationSeries SDOS1 = (SamplingDataOperationSeries)o1;
				SamplingDataOperationSeries SDOS2 = (SamplingDataOperationSeries)o2;
				
				SimpleDateFormat dateform = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSSS");
				
				String date1 = SDOS1.getDataini();
				String date2 = SDOS2.getDataini();
				
				try{	
					if (dateform.parse(date1).after(dateform.parse(date2))){i = 1;}
					else if (dateform.parse(date1).before(dateform.parse(date2))){i = -1;}
					else{i = 0;}
				}catch(Exception e){
					log.error("Cannot classify this object " + e.getMessage(),e);
					i = 0;
				}
			}
		return i;
	  }
}



