package com.bs.analysis.cashanalysistool.driver;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.RandomAccessFile;
import java.io.IOException;

import java.net.*;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import java.util.regex.*;

import org.apache.commons.digester.*;
import org.apache.commons.digester.xmlrules.*;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import org.xml.sax.SAXException;


import com.bs.analysis.cashanalysistool.ae.ProcessSignals;
import com.bs.analysis.cashanalysistool.config.DataFileContainer;
import com.bs.analysis.cashanalysistool.driver.parser.*;
import com.bs.analysis.cashanalysistool.driver.provider.*;
import com.bs.analysis.cashanalysistool.driver.xml.*;
import com.bs.analysis.cashanalysistool.gui.validator.context.ViewScrnContext;
import com.bs.analysis.cashanalysistool.output.basic.*;
import com.bs.analysis.cashanalysistool.output.excel.*;


/**
 * User: EBA
 * Date: 22-jul-2005
 * Time: 16:52:18
 */

public class CashAnalysisTool extends AbstractCashAnalysisTool{
	
	private static String input_file;
	private static ViewScrnContext Context;
	private static String output_mode;
	private static String config_mode;
	private static String output_file;
	private static String formatted;
	private static String dateformatted;
	private static DataFileContainer conf;
	private static int config_id;
	
	
	
	private static int ID_INICIO = 1;
	private static int ID_ENCADEN = 2;
	private static int ID_NOINI = -1;
	private static int GENERAL = 3;
	
	
	
	private static boolean series_match_found = false;
	private static Logger log = Logger.getLogger(CashAnalysisTool.class);
		
	/**
	 * 
	 * @param config_path
	 * @return
	 * @throws Exception
	 */
	
	private static SamplePatternVec ParseConfigXMLData(String rules_path, String config_path) throws Exception{
		Digester dig = DigesterLoader.createDigester((new File(rules_path)).toURL());
		SamplePatternVec SPV = (SamplePatternVec)dig.parse(config_path);
		return SPV;
	}
	
	/**
	 * 
	 * @param rules_path
	 * @param config_path
	 * @return
	 * @throws Exception
	 */
	private static SamplePatternOpeVect ParseConfigXMLOpe(String rules_path, String config_path) throws Exception{
		Digester dig = DigesterLoader.createDigester((new File(rules_path)).toURL());
		SamplePatternOpeVect SPV = (SamplePatternOpeVect)dig.parse(config_path);
		return SPV;
	}
	
	/**
	 * 
	 * @param rules_path
	 * @param config_path
	 * @return
	 * @throws Exception
	 */
	private static SampleConfigVec ParseConfigSys(String rules_path, String config_path) throws Exception{
		Digester dig = DigesterLoader.createDigester((new File(rules_path)).toURL());
		SampleConfigVec SPV = (SampleConfigVec)dig.parse(config_path);
		return SPV;
	}
	
	private static void search_pattern(String cadena1, SamplePattern pat) throws Exception
	{	
		boolean isfinded = false;
		String pattern;
		String time_ant;
		String time;
		Pattern p;
		Matcher matcher;
		boolean search_ini = false;
		String last_element = "";
		//pattern to obtain the date pattern
		SimpleDateFormat dat1 = new SimpleDateFormat("dd MMM yyyy HH:mm:ss,SSS", new Locale(conf.getInfo("Locale")));
		SimpleDateFormat dat2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
	
		
		long timestamp = 0;
		long beftimestamp = 0;
		
		//detect final pattern
		pattern = pat.getBase() + pat.getFin();			
		p = Pattern.compile(pattern,Pattern.DOTALL|Pattern.MULTILINE);
		matcher = p.matcher(cadena1);
		if (matcher.find())
		{
			timestamp = dat1.parse(matcher.group(1)).getTime();
			time = dat2.format(dat1.parse(matcher.group(1)));
			CashAnalysisBean data_ant = CashAnalysisContainer.getInstance().getLastCashData(pat.getId());
			
				if (!data_ant.getData().equalsIgnoreCase("NOTFOUND")){
					time_ant = data_ant.getData();
					beftimestamp = data_ant.getTimestamp();	
					last_element = CashAnalysisContainer.getLastElement();
			
					if (!last_element.equals(pat.getId())){
						if (beftimestamp!=0){
						CashAnalysisProvider.getInstance().addElement(String.valueOf(beftimestamp), new SamplingDataParsed(pat.getId(),time_ant,"",0));
						}	
					}
					CashAnalysisProvider.getInstance().addElement(String.valueOf(timestamp), new SamplingDataParsed(pat.getId(), time_ant, time,(timestamp-beftimestamp)));
				}	
		}
		
		//detect begin pattern		
		pattern = pat.getBase() + pat.getIni();
		p = Pattern.compile(pattern, Pattern.DOTALL|Pattern.MULTILINE);
		matcher = p.matcher(cadena1);
		
		if (matcher.find())
		{
			timestamp = dat1.parse(matcher.group(1)).getTime();
			time = dat2.format(dat1.parse(matcher.group(1)));
			CashAnalysisContainer.getInstance().setreg(pat.getId(),time,timestamp);
			last_element = pat.getId();
			CashAnalysisContainer.setLastElement(last_element);
			CashAnalysisContainer.setLastData(time);
		}
	}
	
	
	
	/**
	 * 
	 * @param Data
	 * @param pattern
	 * @param ticket
	 */
	
	private static void SetData(String Data, String pattern_1, String ticket_1, String pattern_2, String ticket_2, boolean final_trace) throws Exception
	{
		
		SimpleDateFormat dat1 = new SimpleDateFormat("dd MMM yyyy HH:mm:ss,SSS",new Locale(conf.getInfo("Locale")));
		SimpleDateFormat dat2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
		
		String time = "";
		String beftime = "";
		
		
		Pattern p = Pattern.compile(pattern_1, Pattern.DOTALL|Pattern.MULTILINE);
		Matcher matcher = p.matcher(Data);		
		
		long timestamp = 0;
		long beftimestamp = 0;
		
		CashAnalysisBean be1 = new CashAnalysisBean();
		
		if (matcher.find())
		{			
			timestamp = dat1.parse(matcher.group(1)).getTime();
			time = dat2.format(dat1.parse(matcher.group(1)));
			
		
			
			if (final_trace)
			{
				be1 = CashAnalysisContainer.getInstance().getLastCashData("ini_serie");
				
				if (!be1.getData().equalsIgnoreCase("NOTFOUND")){
					beftimestamp = be1.getTimestamp();
					beftime = be1.getData();
					CashAnalysisProvider.getInstance().addElement(String.valueOf(timestamp), new SamplingDataParsed(ticket_1,beftime,time,(timestamp-beftimestamp)));
				}else{
					CashAnalysisProvider.getInstance().addElement(String.valueOf(timestamp), new SamplingDataParsed(ticket_1,"",time,0));	
				}
			}else{
				CashAnalysisContainer.getInstance().setreg("ini_serie",time,timestamp);
				CashAnalysisProvider.getInstance().addElement(String.valueOf(timestamp), new SamplingDataParsed(ticket_1,time,"",0));
			}
			
			
		}else{
			
			p = Pattern.compile(pattern_2, Pattern.DOTALL|Pattern.MULTILINE);
			matcher = p.matcher(Data);
			
			if (matcher.find())
			{
				timestamp = dat1.parse(matcher.group(1)).getTime();
				time = dat2.format(dat1.parse(matcher.group(1)));			
				if (final_trace)
				{
					be1 = CashAnalysisContainer.getInstance().getLastCashData("ini_serie");
					if (!be1.getData().equalsIgnoreCase("NOTFOUND")){
						beftimestamp = be1.getTimestamp();
						beftime = be1.getData();
						CashAnalysisProvider.getInstance().addElement(String.valueOf(timestamp), new SamplingDataParsed(ticket_2,beftime,time,(timestamp-beftimestamp)));
								
					}else{
						CashAnalysisProvider.getInstance().addElement(String.valueOf(timestamp), new SamplingDataParsed(ticket_2,"",time,0));	
					}
				}else{
					CashAnalysisContainer.getInstance().setreg("ini_serie",time,timestamp);
					CashAnalysisProvider.getInstance().addElement(String.valueOf(timestamp), new SamplingDataParsed(ticket_2,time,"",0));
				}
			}
			else{
				log.error("String \n" + Data + "\n no match");
			}
		}
	}
	
	private static void SetOtherFinalTrace(String Data, String pattern_1, String ticket_1) throws Exception{
		
		SimpleDateFormat dat1 = new SimpleDateFormat("dd MMM yyyy HH:mm:ss,SSS",new Locale(conf.getInfo("Locale")));
		SimpleDateFormat dat2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
		
		
		long beftimestamp = 0;
		String beftime = "";
		
		Pattern p = Pattern.compile(pattern_1, Pattern.DOTALL|Pattern.MULTILINE);
		Matcher matcher = p.matcher(Data);		
		
		
		if (matcher.find())
		{			
			long timestamp = dat1.parse(matcher.group(1)).getTime();
			String time = dat2.format(dat1.parse(matcher.group(1)));
			
			CashAnalysisBean be1 = CashAnalysisContainer.getInstance().getLastCashData("ini_serie");
			
			if (!be1.getData().equalsIgnoreCase("NOTFOUND")){
				beftimestamp = be1.getTimestamp();
				beftime = be1.getData();
				String data = CashAnalysisContainer.getInstance().getLastData();
				
				long diff = timestamp-beftimestamp;
				long threshold = Long.parseLong(conf.getInfo("max_time"));
				
				if (diff > threshold){diff = threshold;}
				
				CashAnalysisProvider.getInstance().addElement(String.valueOf(timestamp), new SamplingDataParsed(ticket_1,beftime,data, diff));
			}else{
				CashAnalysisProvider.getInstance().addElement(String.valueOf(timestamp), new SamplingDataParsed(ticket_1,"",time,0));	
			}
			
			CashAnalysisContainer.getInstance().setLastData("");
		}
	}
	
	
	
	/**
	 * 
	 * @param match
	 * @param ActionPattern
	 */
	private static void KeepTraceToActionContainer(String match, Vector ActionPattern) throws Exception
	{
		Pattern pat = null;
		Matcher matcher = null;
		SamplePattern patron = null;
		String pat_rule = "";
		boolean encontrado = false;
	
	
		for(int i=0; ((i < ActionPattern.size()) & (!encontrado)); i++)
		{		
			patron = (SamplePattern)ActionPattern.get(i);
			pat_rule = patron.getBase() + "(" + patron.getIni() + "|" + patron.getFin() + ")";	
			pat  = Pattern.compile(pat_rule,Pattern.DOTALL|Pattern.MULTILINE);
			matcher = pat.matcher(match);
			if (matcher.find())
			{
				if (log.isDebugEnabled()){
					log.info("begin to process line match as " + patron.getId());
				}
				search_pattern(matcher.group(0),patron);
			}
		}
	}
	
	/**
	 * 
	 * @param sourceFile
	 * @return
	 */
	
	private static CharBuffer getSourceFile(String sourceFile) throws IOException
	{
		CharBuffer cadena;
		try{
			FileChannel fc = (new FileInputStream(sourceFile)).getChannel();
			ByteBuffer buf = fc.map(FileChannel.MapMode.READ_ONLY,0,fc.size());
			cadena = Charset.forName("ISO-8859-1").newDecoder().decode(buf);
		    buf.clear();
			fc.close();
		}catch (IOException e)
		{
			log.error("Error when try to open a file " + sourceFile + " " + e.getMessage(),e);
			throw e;
		}
		
		return cadena;
	}
	
	/**
	 * 
	 * @param Patterns
	 * @param ticket
	 * @return
	 */
	private static int isPattern_ini_or_encaden(String match, String pattern_ini, String pattern_encaden) throws Exception
	{
		int res = -1;
		
		boolean found = false;
		
		SimpleDateFormat dat2 = new SimpleDateFormat("dd MMM yyyy HH:mm:ss,SSS",new Locale(conf.getInfo("Locale")));
		
		//pattern_ini
		Pattern pat_ini = Pattern.compile(pattern_ini,Pattern.DOTALL|Pattern.MULTILINE);
		Matcher match1 = pat_ini.matcher(match);
		
		
		Pattern pat_encaden = Pattern.compile(pattern_encaden,Pattern.DOTALL|Pattern.MULTILINE);
		Matcher match2 = pat_encaden.matcher(match);
		
			if (match1.find()){
				if (CashAnalysisTool.series_match_found){
					res = CashAnalysisTool.ID_NOINI;
				}
				else
				{
					res = CashAnalysisTool.ID_INICIO;
					String time = String.valueOf(dat2.parse(match1.group(1)).getTime());
					CashAnalysisContainer.getInstance().setRefData(time);
					CashAnalysisTool.series_match_found = true;
				}
			}
			//traza de encadenamiento
			else if (match2.find()){
				res = CashAnalysisTool.ID_ENCADEN;
				String time = String.valueOf(dat2.parse(match2.group(1)).getTime());
				CashAnalysisContainer.getInstance().setRefData(time);
				CashAnalysisTool.series_match_found = true;
				
			}
			//traza de inicio general
			else if (pattern_ini.equalsIgnoreCase("BOF")){
				
				
				if (CashAnalysisTool.series_match_found){
					res = CashAnalysisTool.ID_NOINI;
				}else{
					res = CashAnalysisTool.GENERAL;
					CashAnalysisTool.series_match_found = true;
				}
			}
		
		return res;
		
	}
	
	
	private static boolean is_other_final(String match_data, String pattern_ske) throws Exception{
		boolean res = false;
		
		Pattern pat_ini = Pattern.compile(pattern_ske,Pattern.DOTALL|Pattern.MULTILINE);
		Matcher match1 = pat_ini.matcher(match_data);
		
		SimpleDateFormat dat2 = new SimpleDateFormat("dd MMM yyyy HH:mm:ss,SSS",new Locale(conf.getInfo("Locale")));
		SimpleDateFormat dat1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
		
		
		long timestamp = 0;
		long beftimestamp = 0;
		long threshold = 0;
		
		
		if (match1.find()){
			try{
				//CashAnalysisBean be1 = CashAnalysisContainer.getInstance().getLastCashData("ini_serie");
				String beftimestampStr = CashAnalysisContainer.getInstance().getRefData();
					if (!beftimestampStr.equalsIgnoreCase("")){
						beftimestamp = Long.parseLong(beftimestampStr);
						timestamp = dat2.parse(match1.group(1)).getTime(); 		
						long diff = timestamp-beftimestamp;
						threshold = Long.parseLong(conf.getInfo("max_time"));
						if (diff > threshold){res=true;}
						else{res = false;}
					}
			}catch(Exception e){res = false;}
		}
		return res;
	}
	
	private static boolean is_pattern_final_ope(String match, String pattern_fin)
	{
		boolean res = false;
		
		//pattern_ini
		Pattern pat_ini = Pattern.compile(pattern_fin,Pattern.DOTALL|Pattern.MULTILINE);
		Matcher match1 = pat_ini.matcher(match);
		
		res = (match1.find());
		
		if (res){
			CashAnalysisTool.series_match_found = false;
		}
		
		return res;
	}
	
	/**
	 * 
	 * @param match
	 * @param Vect_Operation
	 * @return
	 */
	private static String searchIfPatternIdentifier(String match, Vector Vect_Operation)
	{
		String id_operation="DUMMY";
		boolean encontrado = false;
		Pattern pat = null;
		Matcher matcher = null;
		
		
		for(int i=0; i<Vect_Operation.size(); i++)
		{
			SamplePatternOpe ope = (SamplePatternOpe)Vect_Operation.get(i);
			pat = Pattern.compile(ope.getTraza(),Pattern.DOTALL|Pattern.MULTILINE);
			matcher = pat.matcher(match);
			encontrado = matcher.find();
			if (encontrado){
				id_operation = ope.getId();
			}
		}
		return id_operation;
	}
	
	/**
	 * 
	 * @param sourceFile
	 * @param Patterns
	 * @param PatternsOpe
	 * @return
	 * @throws Exception
	 */
	private static boolean exchangePatternsInFile(String sourceFile, int config_id, Vector ConfigSys, Vector Patterns, Vector PatternsOpe) throws Exception
	{
		if ((Patterns == null) || (PatternsOpe == null))
		      return false;
		
		    CharBuffer parseFile = null;
		    String pattern_ske = "";
		    
		    String target_ini = "";
		    String pattern_ini = "";
		    
		    String target_enc = "";
		    String pattern_encaden = "";
		    
		    String target_fin_enc = "";
		    String pattern_fin_enc = "";
		    
		    String target_fin = "";
		    String pattern_fin = "";
		    
		    String operIdentifier = "DUMMY";
		    String aux = "";
		    int get_config = 0;
		    
		    boolean no_more_traces = true;
		    
		    
		    parseFile = getSourceFile(sourceFile);
		    SamplingOperation ope1 = null;
		    
		    	try{
		    		SampleConfig Config_data = (SampleConfig)ConfigSys.get(config_id);
		    		
		    		pattern_ske = Config_data.getSke_pattern();
		    		target_ini = Config_data.getId_ini();
		    		pattern_ini = Config_data.getIni_pattern();
		    		target_enc = Config_data.getId_enc();
		    		pattern_encaden = Config_data.getEnc_pattern();
		    		target_fin_enc = Config_data.getId_fin_enc();
		    		pattern_fin_enc = Config_data.getFin_enc_pattern();
		    		target_fin = Config_data.getId_fin();
		    		pattern_fin = Config_data.getFin_pattern();
		    		
		    	}catch(Exception e)
		    	{
		    		throw e;
		    	}
		    
		    	Pattern pat = Pattern.compile(pattern_ske,Pattern.DOTALL|Pattern.MULTILINE);
		    	Matcher match = pat.matcher(parseFile);
		    	
		    	int num_operation = 0;			    	
		    	CashAnalysisTool.series_match_found = false;
		    	CashAnalysisContainer.setLastElement("");

		    	
		    	int is_ini_or_enc = -1;
		    	
		    	while (match.find())
		    	{	
		    		is_ini_or_enc = CashAnalysisTool.ID_NOINI;
		    		//is a begin of operation?
		    		
		    		if (log.isDebugEnabled()){
		    			log.debug("linea a procesar \n" + match.group(0));
		    		}
		    		
		    		
		    		if ((is_ini_or_enc = isPattern_ini_or_encaden(match.group(0),pattern_ini,pattern_encaden)) > 0)
		    		{
		    			log.info("Find begin of new operation \n" + match.group(0));
		    			if (Context!=null){
		    				Context.getRegScrn().ChangeMessageProgressBar("\nDetecta comienzo " + (is_ini_or_enc == CashAnalysisTool.ID_INICIO? "nueva operacion" : "operacion encadenada"));
		    			}
		    			
		    			if (operIdentifier!="DUMMY")
		    			{
		    				log.info("Process operation (" + operIdentifier + ")");
		    				if (Context!=null){Context.getRegScrn().ChangeMessageProgressBar("\nProcesa operacion ["+operIdentifier+ "]");}
		    				    				
		    				if (is_ini_or_enc == CashAnalysisTool.ID_ENCADEN){
				    			if (Context!=null){
				    				Context.getRegScrn().ChangeMessageProgressBar("\nDetecta fin operacion encadenada");
				    			}
				    			
				    			if (!config_mode.equalsIgnoreCase("GENERAL")){
				    				SetData(match.group(0),pattern_fin_enc, target_fin_enc,"","",true);
				    			}
		    				}
		    				
		    				SamplingSerie SS = CashAnalysisProvider.getContainer();
		    				CashAnalysisProvOpe.getInstance().setOperationSeries(operIdentifier,SS);
		    				CashAnalysisContainer.getInstance().setContainer(new Hashtable());
		    				CashAnalysisProvider.setContainer(new SamplingSerie());
		    				
		    				
		    			}else{
		    				//si no se ha detectado Operación, la info recogida se desprecia
		    				CashAnalysisContainer.getInstance().setContainer(new Hashtable());
		    				CashAnalysisProvider.setContainer(new SamplingSerie());
		    			}
		    			
		    			if (!config_mode.equalsIgnoreCase("GENERAL")){
		    				SetData(match.group(0),pattern_ini,target_ini,pattern_encaden,target_enc,false);
		    			}
		    			
		    			no_more_traces = false; 
		    			operIdentifier="DUMMY";
		    		}
		    		
		    		else if (is_pattern_final_ope(match.group(0), pattern_fin)){
		    			if (Context!=null){
		    				Context.getRegScrn().ChangeMessageProgressBar("\nFin operacion actual [" + (!operIdentifier.equalsIgnoreCase("DUMMY")? operIdentifier : "No Identificada") + "]");
		    			}
		    			log.info("Detect final of current operation");
		    			if (!no_more_traces){
		    				if (!config_mode.equalsIgnoreCase("GENERAL")){
		    					SetData(match.group(0),pattern_fin, target_fin,"","",true);
		    				}	
		    			}	
		    			CashAnalysisContainer.getInstance().setRefData("");
		    			no_more_traces = true;
		    		}
		    		
		    		//detect other possible finals as diffs of 5 minutes or similars
		    		
		    		else if (is_other_final(match.group(0), pattern_ske)){
		    			if (Context!=null){
		    				Context.getRegScrn().ChangeMessageProgressBar("\nFin operacion actual [" + (!operIdentifier.equalsIgnoreCase("DUMMY")? operIdentifier : "No Identificada") + "]");
		    			}
		    			log.info("Detect final (Delay) of current operation");
		    			if (!no_more_traces){
		    				if (!config_mode.equalsIgnoreCase("GENERAL")){
		    					SetOtherFinalTrace(match.group(0), pattern_ske, target_fin);
		    				}
		    			}
		    			CashAnalysisContainer.getInstance().setRefData("");
		    			no_more_traces = true;
		    		}
		    		
		    		//Does the user push the cancel button?
		    		else if (ProcessSignals.getInstance().getFinishSignal().equalsIgnoreCase("SI")){
		    			throw new Exception("Operacion cancelada a petición del usuario");
		    		}
		    		
		    		else
		    		{
		    			//is an identifier operation pattern?
		    		    aux = searchIfPatternIdentifier(match.group(0),PatternsOpe);
		    		    
		    		    if (aux != "DUMMY"){
		    		    	log.info("Detect new operation (" + aux +")\n " + match.group(0));
		    		    }
		    		    
		    		    if (!no_more_traces){
		    		    	if (aux != "DUMMY"){
		    		    		operIdentifier = aux;
		    		    	}
		    		    }
		    		}
		    		
		    		if (!no_more_traces){KeepTraceToActionContainer(match.group(0),Patterns);}
		    		if (Context != null){Context.getRegScrn().IncActProgressBar();}
		    	}
		    	
		    	//keep the last data into an auxiliary container
		    	
		    	if (operIdentifier!="DUMMY")
    			{
    				log.info("Process the last one operation (" + operIdentifier + ")");
    				if (Context!=null){Context.getRegScrn().ChangeMessageProgressBar("\nProcesa la última operación(" + operIdentifier + ")");}	    				
    				SamplingSerie SS = CashAnalysisProvider.getContainer();
    				if (SS!=null){
    					CashAnalysisProvOpe.getInstance().setOperationSeries(operIdentifier,SS);
    					CashAnalysisProvider.setContainer(new SamplingSerie());
    					CashAnalysisContainer.getInstance().setContainer(new Hashtable());
    					CashAnalysisProvider.setContainer(new SamplingSerie());
    				}
    			}
		    	else if (pattern_fin.equalsIgnoreCase("EOF")){
    				log.info("Process all the analysis ");
    				if (Context!=null){Context.getRegScrn().ChangeMessageProgressBar("\nProcesa todo el análisis");}
    				SamplingSerie SS = CashAnalysisProvider.getContainer();
    				if (SS!=null){
    					CashAnalysisProvOpe.getInstance().setOperationSeries(operIdentifier,SS);
    					CashAnalysisProvider.setContainer(new SamplingSerie());
    					CashAnalysisContainer.getInstance().setContainer(new Hashtable());
    					CashAnalysisProvider.setContainer(new SamplingSerie());
    				}
    			}
		    return true;
	 }
	
	 
	 public CashAnalysisTool(String FileName, ViewScrnContext ScrnContext, String OutputMode, String ConfigMode, String fileOutput, String formatted, String dateformatted){
		 this.input_file = FileName;
		 this.Context = ScrnContext;
		 this.output_mode = OutputMode;
		 this.config_mode = ConfigMode;
		 this.output_file = fileOutput;
		 
		 if ((formatted == "") || (formatted == "NO"))
		 {formatted = "<no data>";}
		 this.formatted = formatted;
		 
		 if ((dateformatted == "") || (dateformatted == "NO"))
		 {dateformatted = "<no data>";}
		 this.dateformatted = dateformatted;
	 }
	 
	 private static void outputTo(String output_formats[], String fileOutput, String ConfigMode, ViewScrnContext Context, SampleConfigVec config_vector) throws Exception{
		 String options = "";
		 String file_output = "";
		 if (output_formats.length != 0)
		 {
			 for(int i=0; i<output_formats.length; i++)
			 {
				 options = output_formats[i];
				 
				 //se obtiene por pantalla 
				 if (options.equalsIgnoreCase("CONSOLA") || options.equalsIgnoreCase("TODO") ) {
					 log.info("Output data to stdout");
					  
					 if (CashAnalysisProvOpe.getInstance().getContainer().size() > 0){
							 if (!ConfigMode.equalsIgnoreCase("GENERAL")){
								 log.info("Data parsed obtained " +  CashAnalysisProvOpe.getInstance().DataFormattedString("CONSOLA", ConfigMode, CashAnalysisTool.formatted, CashAnalysisTool.dateformatted));
								 if (Context != null){
									 Context.getRegScrn().ChangeMessageProgressBar("\nData parsed obtained " +  CashAnalysisProvOpe.getInstance().DataFormattedString("CONSOLA", ConfigMode, CashAnalysisTool.formatted, CashAnalysisTool.dateformatted));
								 }
							 }else{
								 log.info("Data parsed obtained " +  CashAnalysisProvOpe.getInstance().toString());
								 if (Context != null){
									 Context.getRegScrn().ChangeMessageProgressBar("\nData parsed obtained " +  CashAnalysisProvOpe.getInstance().toString());
								 }
							 }
					 }else{
						 log.warn("No data to output to stdout");
						 if (Context != null){
							 Context.getRegScrn().ChangeMessageProgressBar("\n No data to output to stdout");
						 }
					 }
			     }
				 
				 //se vuelca a un fichero de texto
				 if (options.equalsIgnoreCase("CSV") || options.equalsIgnoreCase("TODO")){
					 
					 if (fileOutput.equalsIgnoreCase("")){
						 file_output = conf.getInfo("OutputBasicFile");
					 }else{
						 file_output = fileOutput;
					 }
					 
					 log.info("Output data to CSV file: " + file_output); 
					 
					 if (Context != null){
						 Context.getRegScrn().ChangeMessageProgressBar("\nOutput data to CSV file: " + file_output);
					 }
					 
					 basicOutput.getInstance().processFile(file_output,CashAnalysisProvOpe.getInstance(),ConfigMode, CashAnalysisTool.formatted, CashAnalysisTool.dateformatted, Context);	
				 }
				 
				 //se vuelca a un fichero excel
				 if (options.equalsIgnoreCase("EXCEL") || options.equalsIgnoreCase("TODO")){
					 log.info("Output data to EXCEL");
					 //process(String templatePath, String skillsID, CashAnalysisProvOpe container)
					 SampleConfig sys_info = (SampleConfig)config_vector.getSampleConfigVec().get(CashAnalysisTool.config_id);
					 if (Context != null){
						 Context.getRegScrn().ChangeMessageProgressBar("\nOutput data to EXCEL");
					 }
					 excelOutput.getInstance().process(conf.getInfo("OutputCSVTemp"), ConfigMode, CashAnalysisProvOpe.getInstance(), sys_info, CashAnalysisTool.formatted, CashAnalysisTool.dateformatted, Context);				 
				 }		 
			 }
		 }else{
			 log.info("No output formats configured!");
		 }
	 }
	 
	 private void processInformation() throws Exception{
		 	
		    String path_action = "";
		 	config_id = 0;
		    
		 	
		    log.info("CashAnalysisTool-Core Process begins"); 
		    
		    //inicializa los containers
			CashAnalysisProvOpe.setContainer(new Hashtable());
			CashAnalysisProvider.setContainer(new SamplingSerie());
			CashAnalysisContainer.setContainer(new Hashtable());
		   
			
		    path_action = conf.getInfo(this.config_mode+"_file");
		    config_id = Integer.parseInt(conf.getInfo(this.config_mode+"_id"));
		    
		    log.info("path_action " + path_action + " config_id " + String.valueOf(config_id));

		    
			SampleConfigVec vect_sys = ParseConfigSys(conf.getInfo("RulSys"), conf.getInfo("FilePatSys"));
			if (vect_sys.size() == 0){
				throw new Exception("Not found any Config Pattern");
			}
			else{
				log.info("Patterns Config charged correctly! from file " + conf.getInfo("FilePatSys") + " size() = " + vect_sys.size());
			}
			
		    SamplePatternVec vect_patterns = ParseConfigXMLData(conf.getInfo("RulAction"), path_action);
			
			if (vect_patterns.size() == 0){
				throw new Exception("Not found any Action Pattern");
			}
			else{
				log.info("Patterns Data charged correctly! from file " + path_action + " size() = " + vect_patterns.size());
			}
			
			SamplePatternOpeVect vect_ope = ParseConfigXMLOpe(conf.getInfo("RulOpe"),path_action);
			
			if (vect_ope.size() == 0){
				throw new Exception("Not found any Ope Pattern");
			}
			else{
				log.info("Patterns Ope charged correctly! from file " + path_action + " size() = " + vect_ope.size());
			}
			
			
			
			
			
			log.debug("Process file " + this.input_file);
			
			exchangePatternsInFile(this.input_file, config_id, vect_sys.getSampleConfigVec(), vect_patterns.getSamplePatternVec(), vect_ope.getSamplePatternOpeVec());
			
			//fuerza el volcado de información a un canal de salida
			if (!this.output_mode.equals("")){
				outputTo(new String[]{output_mode},this.output_file,config_mode, Context,vect_sys);
			}
			else{
				outputTo(new String[]{"TODO"},this.output_file,config_mode, Context, vect_sys);
			}
		
			if (Context!=null)
			{
				
				if (!this.output_mode.equals("")){
					if (!this.output_mode.equalsIgnoreCase("NO") && !this.output_mode.equalsIgnoreCase("CONSOLA")){
						if (conf.getInfo("visibleFolder").equalsIgnoreCase("1")){
							
							if (this.output_mode.equalsIgnoreCase("CSV")){
								String path = this.output_file;
								if (path.lastIndexOf(File.separatorChar) != -1){
									path = path.substring(0,path.lastIndexOf(File.separatorChar));
									Context.getRegScrn().launchWindows(path);
								}else{
									Context.getRegScrn().launchWindows();
								}
							}
							else{
								Context.getRegScrn().launchWindows();
							}
						}
						
					}
				}
				Context.getRegScrn().setSuccesfulMsg("Proceso acabado correctamente!");
				Context.getRegScrn().finishActions();
			}
			log.info("Current CashAnalysisTool-Core Process finnished correctly!");
	 }
	 
	 public void processFile(){
		 try{
			 
			 conf = DataFileContainer.getInstance();
			 processInformation();
		 }catch(Exception e){
			 log.error("Error to process the files " + e.getMessage(),e);
			 Context.getRegScrn().setErrorMsg(e.getMessage());
			 if (ProcessSignals.getInstance().getFinishSignal().equalsIgnoreCase("SI")){
					Context.getRegScrn().ChangeMessageProgressBar("\nOperacion detenida por el usuario!");
					Context.getRegScrn().setMsgProgressBar("Operacion detenida por el usuario!");
			 }
		 }
	 }
	 
	 
	
	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	
	public static void main(String args[]) throws Exception{
		
		String config_file = "";		/*fichero de configuración con la info del sistema*/
		String input_file = "";			/*fichero de entrada*/
		
		try{		
					PropertyConfigurator.configure("./res/log4j.properties");
					if (args.length < 2){
						log.info("less than two elements");
						System.exit(1);
					}
					else{	
							if (args[0]!=null){config_file = args[0];}
							if (args[1]!=null){input_file = args[1];}
							
							log.info("launch CashAnalysisTool " + config_file + " " + input_file);
							
							conf = DataFileContainer.getInstance();
							conf.setConfigFile(config_file);
							
							
							SampleConfigVec vect_sys = ParseConfigSys(conf.getInfo("RulSys"), conf.getInfo("FilePatSys"));
							if (vect_sys.size() == 0){
								throw new Exception("Not found any Config Pattern");
							}
							else{
								log.info("Patterns Config charged correctly! size() = " + vect_sys.size());
							}
							
							SamplePatternVec vect_patterns = ParseConfigXMLData(conf.getInfo("RulAction"),conf.getInfo("FilePatAction"));
							if (vect_patterns.size() == 0){
								throw new Exception("Not found any Action Pattern");
							}	
							else{
								log.info("Patterns Data charged correctly!");
							}
					
							SamplePatternOpeVect vect_ope = ParseConfigXMLOpe(conf.getInfo("RulOpe"),conf.getInfo("FilePatAction"));
					
							if (vect_ope.size() == 0){
								throw new Exception("Not found any Ope Pattern");
							}
							else{
								log.info("Patterns Ope charged correctly!");
							}
							
							
							exchangePatternsInFile(input_file, 0, vect_sys.getSampleConfigVec(), vect_patterns.getSamplePatternVec(), vect_ope.getSamplePatternOpeVec());
							outputTo(conf.getInfoList("OutputFormats"),conf.getInfo("OutputBasicFile"),"",null,vect_sys);
							log.info("Application finnished succesfully");	
							System.exit(0);
					   }
		}catch(Exception e)
		{
			log.error("Exception captured while process are executed " + e.getMessage(),e);
			System.exit(1);
		}
	}

}
