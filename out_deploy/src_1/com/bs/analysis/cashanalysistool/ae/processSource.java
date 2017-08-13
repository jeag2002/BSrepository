package com.bs.analysis.cashanalysistool.ae;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.lang.Thread;

import com.bs.analysis.cashanalysistool.ae.*;
import com.bs.analysis.cashanalysistool.config.DataFileContainer;
import com.bs.analysis.cashanalysistool.gui.model.ATVModel;
import com.bs.analysis.cashanalysistool.gui.validator.context.*;


//definicion del Factory Method llamada al driver 
import com.bs.analysis.cashanalysistool.driver.*;
import com.bs.analysis.cashanalysistool.driver.xml.SampleConfig;
import com.bs.analysis.cashanalysistool.driver.xml.SampleConfigVec;


import org.apache.commons.digester.Digester;
import org.apache.commons.digester.xmlrules.DigesterLoader;
import org.apache.log4j.*;

class ExtensionComparator implements Comparator {
	  public int compare(Object o1, Object o2) {
	    if(o1 instanceof String && o2 instanceof String) {
	      String s1 = (String)o1;
	      String s2 = (String)o2;
	      int s11,s22;
	      s11 =0; s22 = 0;
	      
	      try{
	      	if (s1.lastIndexOf(".") != -1){
	      		s1 = s1.substring(s1.lastIndexOf(".")+1,s1.length());
	      		s11 = Integer.parseInt(s1);
	      	}
	      }catch(NumberFormatException e){s11 = -1;}
	      
	      try{
		      	if (s2.lastIndexOf(".") != -1){
		      		s2 = s2.substring(s2.lastIndexOf(".")+1,s2.length());
		      		s22 = Integer.parseInt(s2);
		      	}
		      }catch(NumberFormatException e){s22 = -1;}
	      if (s11 < s22){return 1;}
	      else{return -1;}	      
	    }else{
	    	return 0;
	    } 
	  }
}

class OnlyFile implements FilenameFilter
{
  String ext;
  public OnlyFile(String file)
  { this.ext = file; }
  public boolean accept(File dir, String name)
  { 
	  String patron = ext;
	  Pattern res = Pattern.compile(patron);
	  Matcher match = res.matcher(name);
	  return match.find(); 
  }
}

public class processSource extends Thread{
	
	private static Logger log = Logger.getLogger(processSource.class);
	private ATVModel AVT = null;
	private BeanATV AVTB = null;
	private int numfiles = 0;
	
	private AbstractFactory fact1 = null;
	private AbstractCashAnalysisTool ACAT = null;
	
	private static DataFileContainer conf = null; 
	
	private static String config_mode = "<no data>"; 		//parseo de los logs segun su procedencia (Produccion o desarrollo)
	
	private static String file = "<no data>";				//fichero o folder a parsear
	
	private static String format_mode = "<no data>";		//formato de salida (clasificación por operaciones o fechas)
															//OPE (operaciones)
															//DATA (fechas)	
	
	private static String normalize = "<no data>";			//tipo de patron con el que se filtran las salidas (los patrones son definidos en standardoutput.xml)
															//GENERAL
															//FUNCIONALES
	
	private static String date_normalize = "<no data>";		//normalizacion de funciones por fechas
	
	private static String output_mode = "<no data>";		//presentación de la info (
															//ALL (TODOS LOS OTROS), 
															//CONSOLE(pantalla), 
															//CSV(fichero texto formateado), 
															//EXCEL(fichero excel))
	
	private static String dateini = "01/01/1970 00:00:00";	//fecha de inicio
	private static String datefin = "01/01/1980 00:00:00";	//fecha final
	
	private static String datedv = "<no data>";				//intervalo 
															//TODAY(HOY), 
															//YESTERDAY(AYER), 
															//ALL(TODOS)
	
	private static String file_output = "<no data>";		//fichero de salida	
	
	private ViewScrnContext AVTContext = null;
	
	public processSource(ATVModel AVT, ViewScrnContext AVTContext){
		
		SimpleDateFormat form = new SimpleDateFormat("dd/MM/yyyy");
		
		this.AVT = AVT;
		this.AVTContext = AVTContext;
		this.AVTB = new BeanATV();
	
		AVTB.setFolderpath(AVT.getFolderpath());
		AVTB.setDateini(AVT.getDateini());
		AVTB.setDatefin(AVT.getDatefin());
		AVTB.setDateinterval(AVT.getDateinterval());
		AVTB.setInputformats(AVT.getInputformats());
		AVTB.setOutputformats(AVT.getOutputformats());
		AVTB.setOutputpath(AVT.getOutputpath());
		AVTB.setIsformatted(AVT.getOutputoptions());
		AVTB.setOutputdateformats(AVT.getOutputdateformats());
				
		//modificacion de los tiempos en caso de que queden activos los intervalos
		if (!AVTB.getDateinterval().equalsIgnoreCase("NO")){
			if (AVTB.getDateinterval().equalsIgnoreCase("HOY")){
				AVTB.setDateini(form.format(new Date()).toString()+" 00:00:00");
				AVTB.setDatefin(form.format(new Date()).toString()+" 23:59:59");
			}
			else if (AVTB.getDateinterval().equalsIgnoreCase("AYER")){	
				Calendar cal_1 = Calendar.getInstance();
				cal_1.add(Calendar.DAY_OF_YEAR,-1);
				AVTB.setDateini(form.format(cal_1.getTime()).toString()+" 00:00:00");
				AVTB.setDatefin(form.format(cal_1.getTime()).toString()+" 23:59:59");
			}
			else if (AVTB.getDateinterval().equalsIgnoreCase("TODO")){
				AVTB.setDateini("01/01/1970 00:00:00");
				AVTB.setDatefin("01/01/1980 00:00:00");	
			}
		}
		
		log.debug("Procesando " + AVTB.toString());
		
		
		numfiles = 0;
	}
	
	public processSource(BeanATV AVTB)
	{
		this.AVTB = AVTB;
		numfiles = 0;
	}
	
	public processSource(String folderpath, String dateini, String datefin)
	{
		this.AVTB = new BeanATV(folderpath,dateini,datefin);
		numfiles = 0;
	}
	
	public processSource(String ConfigMode, String folderpath, String OutputMode, String dateini, String datefin)
	{
		this.AVTB = new BeanATV(folderpath,dateini,datefin, ConfigMode, OutputMode, "");
		numfiles = 0;
	}
	
	public processSource(String ConfigMode, String folderpath, String OutputMode, String dateini, String datefin, String output_file)
	{
		this.AVTB = new BeanATV(folderpath,dateini,datefin, ConfigMode, OutputMode, output_file);		
		numfiles = 0;
	}
	
	public processSource(String ConfigMode, String folderpath, String OutputMode, String dateini, String datefin, String output_file, String normalize)
	{
		this.AVTB = new BeanATV(folderpath,dateini,datefin, ConfigMode, OutputMode, output_file, normalize);		
		numfiles = 0;
	}
	
	
	private CharBuffer getSourceFile(String sourceFile) throws IOException
	{
		CharBuffer cadena = null;
		
		try{
			FileChannel fc = (new FileInputStream(sourceFile)).getChannel();
			ByteBuffer buf = fc.map(FileChannel.MapMode.READ_ONLY,0,fc.size());
			cadena = Charset.forName("ISO-8859-1").newDecoder().decode(buf);
		}catch (IOException e)
		{
			throw e;
		}
		
		return cadena;
	}

	private static SampleConfigVec ParseConfigSys(String rules_path, String config_path) throws Exception{
		Digester dig = DigesterLoader.createDigester((new File(rules_path)).toURL());
		SampleConfigVec SPV = (SampleConfigVec)dig.parse(config_path);
		return SPV;
	}
	
	private String getPatternSke(String configmode) throws Exception{
		SampleConfigVec vect_sys = ParseConfigSys(conf.getInfo("RulSys"), conf.getInfo("FilePatSys"));
		int get_config = 0;
		String pattern = "";
		
		if (vect_sys.size() == 0){
			throw new Exception("Not found any Config Pattern");
		}
		
    	if ((configmode.equalsIgnoreCase("PRODDETALLE")) || (configmode.equalsIgnoreCase("PRODFUNC"))){
    		get_config = 1;
    	}else if (configmode.equalsIgnoreCase("GENERAL")){
    		get_config = 2;
    	}else{
    		get_config = 0;
    	}
		
    	try{
    		SampleConfig Config_data = (SampleConfig)vect_sys.getSampleConfigVec().get(get_config);
    		pattern = Config_data.getSke_pattern();
    	}catch(Exception e)
    	{
    		throw e;
    	}
		return pattern;	
	}
	
	
	/**
	 * 
	 * @param path
	 * @param AVTB
	 * @param bf
	 * @throws Exception
	 */
	private void MergeFilesInOne(String path, BeanATV AVTB, BufferedWriter bf) throws Exception{
		
				CharBuffer sourcefile = getSourceFile(path);	
				String pattern_ske = getPatternSke(AVTB.getInputformats());
				Pattern pat = Pattern.compile(pattern_ske,Pattern.DOTALL|Pattern.MULTILINE);
				Matcher match = pat.matcher(sourcefile);
				String line = "";
				
				SimpleDateFormat dat1 = new SimpleDateFormat("dd MMM yyyy HH:mm:ss,SSS",new Locale(conf.getInfo("Locale")));
				SimpleDateFormat dat2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    	 	
				while (match.find()){
					try{
						String fecha = match.group(1);
						Date line_date = dat1.parse(fecha);

						line = match.group(0);
					
						if (AVTB.getDateini().equalsIgnoreCase("01/01/1970 00:00:00") || AVTB.getDatefin().equalsIgnoreCase("01/01/1980 00:00:00")){
								bf.write(line+"\n");
								bf.flush();
								numfiles ++;
						}
						else if (!AVTB.getDateini().equalsIgnoreCase("01/01/1970 00:00:00") && !AVTB.getDatefin().equalsIgnoreCase("01/01/1980 00:00:00")){			
								Date limit_inf = dat2.parse(AVTB.getDateini());
								Date limit_sup = dat2.parse(AVTB.getDatefin());
							
								if (line_date.after(limit_inf) && line_date.before(limit_sup)){
									bf.write(line+"\n");
									bf.flush();
									numfiles ++;
								}
						}
					}catch(Exception e){
						log.error("Error when try to process line " + line + " message " + e.getMessage() );
					}
			}
	}
	
	/**
	 * 
	 * @param FileData
	 * @param AVTContext
	 * @throws Exception
	 */
	private void processData(AbstractFactory fact1, String FileData, ViewScrnContext AVTContext, String OutputMode, String ConfigMode, String fileOutput, String formatted, String dateformatted) throws Exception
	{
		ACAT = fact1.createCashAnalysisTool(FileData,AVTContext, OutputMode, ConfigMode, fileOutput, formatted, dateformatted);
		ACAT.processFile();
	}
	
	private void validate() throws Exception{
		AVTB.validate();
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	private void execute() throws Exception{
		
		File file = new File(AVTB.getFolderpath());
		String[] dir = null;
		String path="";
		
		if (file.isDirectory()){
			dir = file.list(new OnlyFile(conf.getInfo("FilePattern")));
			path = file.getAbsolutePath() + File.separatorChar;
		}
		
		else if (file.isFile()){
			dir = new String[1];
			dir[0] = file.getAbsolutePath();
		}
		
		java.util.Arrays.sort(dir, new ExtensionComparator());
		
		if (this.AVTContext!=null){AVTContext.getRegScrn().setIniprogressBar();}
		
		File file_1  = File.createTempFile("output",".tmp",new File(".\\logs"));
		BufferedWriter wf = new BufferedWriter(new FileWriter(file_1));
		
		fact1 = new CashAnalysisFactory();
		for(int i=0; i<dir.length; i++)
		{						
				log.info("Process file " + dir[i]);		
				//processData(fact1, path.concat(dir[i]), AVTContext, AVTB.getOutputformats(), AVTB.getInputformats(), AVTB.getOutputpath(), AVTB.getIsformatted());
				MergeFilesInOne(path.concat(dir[i]),AVTB,wf);
		}
		
		wf.flush();
		wf.close();
		
		if (this.AVTContext!=null)
		{
			AVTContext.getRegScrn().setSuccesfulMsg("");
			AVTContext.getRegScrn().setNumtraces(this.numfiles);
			AVTContext.getRegScrn().setIniOutputConsole();
		}	
		
		//llamada al metodo procesaInfo del driver (fichero de entrada, AVTcontexto)!
		fact1 = new CashAnalysisFactory();
		processData(fact1, file_1.getPath(), AVTContext, AVTB.getOutputformats(), AVTB.getInputformats(), AVTB.getOutputpath(), AVTB.getIsformatted(), AVTB.getOutputdateformats());
		System.gc();
		if (file_1.exists()){file_1.delete();}
	}
	
	public void run(){
		try{
			
			conf = DataFileContainer.getInstance();
			conf.setDefaultConfigFile();
			validate();
			execute();
			finalize();
		}catch(Exception e)
		{
			if (AVTContext!=null){
				AVTContext.getRegScrn().setErrorMsg(e.getMessage());
				AVTContext.getRegScrn().setKOprogressBar();
				
				if (ProcessSignals.getInstance().getFinishSignal().equalsIgnoreCase("SI")){
					AVTContext.getRegScrn().ChangeMessageProgressBar("Operacion detenida por el usuario!");
				}
			}
		}finally{
			finalize();
		}
	}
	
	/**
	 * 
	 *
	 */
	public static void cleanTemp(){
		File file = new File(".\\logs");
		File[] dir = file.listFiles(new OnlyFile("tmp"));
		log.debug("detected " + dir.length + " temp files for removing");
		for(int i=0; i<dir.length; i++){
				boolean is_remove = dir[i].delete();
				log.debug ("file " + dir[i].getAbsolutePath() + " is removed? " + is_remove);
		}
	}
	
	/**
	 * 
	 */
	public void finalize(){
		try{
			cleanTemp();
		}catch(Exception e){
			log.error("Error when try to close the operation " + e.getMessage(),e);
		}
	}
	
	public static void main(String args[]) throws Exception
	{
		PropertyConfigurator.configure(".\\config\\log4j.properties");
		SimpleDateFormat form = new SimpleDateFormat("dd/MM/yyyy");
		
			try{
					if (args.length < 2){
						log.info("CashTool -cf (PRODDETALLE|DETALLE|PRODFUNC|FUNC|GENERAL) -if folder -nmr (GENERAL|FUNCIONAL) -fm (TODO|CONSOLA|CSV|EXCEL) (-di dateini -do dateout| -dv [NO|HOY|AYER|TODO] ) -of outputfile");
						System.exit(1);
					}else{
							
							boolean is_config = false;
							boolean is_folder = false;
							boolean is_omode = false;
							boolean is_fmode = false;
							boolean is_di = false;
							boolean is_do = false;
							boolean is_dv = false;
							boolean is_of = false; 
							boolean is_nmr = false;
							
							conf = DataFileContainer.getInstance();
							conf.setConfigFile(".\\config\\cashtool.properties");
							
							
							for (int i=0; i< args.length; i++){
								if (args[i].equalsIgnoreCase("-cf")){
									
									is_config = true;
									is_folder = false;
									is_omode = false;
									is_fmode = false;
									is_di = false;
									is_do = false;
									is_dv = false;
									is_of = false;
									is_nmr = false;
								}
								else if (args[i].equalsIgnoreCase("-if")){
									
									is_config = false;
									is_folder = true;
									is_omode = false;
									is_fmode = false;
									is_di = false;
									is_do = false;
									is_dv = false;
									is_of = false;
									is_nmr = false;
								}
								
								else if (args[i].equalsIgnoreCase("-fm")){
									
									is_config = false;
									is_folder = false;
									is_omode = false;
									is_fmode = true;
									is_di = false;
									is_do = false;
									is_dv = false;
									is_of = false;
									is_nmr = false;
								}
								
								
								else if (args[i].equalsIgnoreCase("-di")){
									
									is_config = false;
									is_folder = false;
									is_omode = false;
									is_fmode = false;
									is_di = true;
									is_do = false;
									is_dv = false;
									is_of = false;
									is_nmr = false;
								
								}
								else if (args[i].equalsIgnoreCase("-do")){
									
									is_config = false;
									is_folder = false;
									is_omode = false;
									is_fmode = false;
									is_di = false;
									is_do = true;
									is_dv = false;
									is_of = false;
									is_nmr = false;
								}
								
								else if (args[i].equalsIgnoreCase("-dv")){
									
									is_config = false;
									is_folder = false;
									is_omode = false;
									is_fmode = false;
									is_di = false;
									is_do = false;
									is_dv = true;
									is_of = false;
									is_nmr = false;
								}
								
								else if (args[i].equalsIgnoreCase("-of")){
									
									is_config = false;
									is_folder = false;
									is_omode = false;
									is_fmode = false;
									is_di = false;
									is_do = false;
									is_dv = false;
									is_of = true;
									is_nmr = false;
								}
								
								/*
								else if (args[i].equalsIgnoreCase("-nmr")){
									
									is_config = false;
									is_folder = false;
									is_omode = false;
									is_fmode = false;
									is_di = false;
									is_do = false;
									is_dv = false;
									is_of = false;
									is_nmr = true;
								}
								*/
								
								else{
									if (is_config){config_mode = args[i]; is_config = false;}
									else if (is_folder){file = args[i]; is_folder = false;}
									else if (is_fmode){format_mode  = args[i]; is_fmode = false;}
									else if (is_omode){output_mode = args[i]; is_omode = false;}
									else if (is_di){dateini = args[i]; is_di = false;}
									else if (is_do){datefin = args[i]; is_do = false;}
									else if (is_dv){datedv = args[i]; is_dv = false;}
									else if (is_of){file_output = args[i]; is_of = false;}
									//else if (is_nmr){normalize = args[i]; is_nmr = false;}
								}
							}
							
							//
							if (config_mode.equalsIgnoreCase("")){
								config_mode = "DETALLE";
							}
						    
							if (file.equalsIgnoreCase("")){
								file = conf.getInfo("DefaultFolder");
							}
							
							//opciones de fechas
							if (dateini.equalsIgnoreCase("") && datefin.equalsIgnoreCase("")){
								dateini = "01/01/1970 00:00:00";
								datefin = "01/01/1980 00:00:00";	
							}
							else if (!dateini.equalsIgnoreCase("") && datefin.equalsIgnoreCase("")){	
								datefin = form.format(new Date()).toString()+" 23:59:59";
							
							}
							else if (dateini.equalsIgnoreCase("") && !datefin.equalsIgnoreCase("")){
								dateini = form.format(new Date()).toString()+" 00:00:00";
							}
							
							//opciones de fecha. Intervalos
							if (!datedv.equalsIgnoreCase("")){
								if (datedv.equalsIgnoreCase("HOY")){
									dateini = form.format(new Date()).toString()+" 00:00:00";
									datefin = form.format(new Date()).toString()+" 23:59:59";
								}
								else if (datedv.equalsIgnoreCase("AYER")){	
									Calendar cal_1 = Calendar.getInstance();
									cal_1.add(Calendar.DAY_OF_YEAR,-1);
									dateini = form.format(cal_1.getTime()).toString()+" 00:00:00";
									datefin = form.format(cal_1.getTime()).toString()+" 23:59:59";
								}
								else if (datedv.equalsIgnoreCase("TODO")){
									dateini = "01/01/1970 00:00:00";
									datefin = "01/01/1980 00:00:00";	
								}
							}
							
							if (file_output.equalsIgnoreCase("")){
								file_output = conf.getInfo("OutputBasicFile");
							}
					
							if (format_mode.equalsIgnoreCase("") || format_mode.equalsIgnoreCase("<no data>")){
								format_mode = conf.getInfo("DefaultFormat");
							}
							
							
							if (datefin.equalsIgnoreCase("01/01/1980 00:00:00")){
								log.info("launch processSource -cf " + config_mode + " -if "+ file + " -fm " + format_mode + " -nmr " + normalize + " all traces  -of" + file_output);
							}else{
								log.info("launch processSource -cf " + config_mode + " -if "+ file + " -fm " + format_mode + " -nmr " + normalize + " -di " + dateini + " -df " + datefin + " -of" + file_output);
							}

							processSource src = new processSource(config_mode,file,format_mode,dateini,datefin,file_output,normalize);
							src.validate();
							src.execute();
							src.finalize();
							System.exit(0);
					}
			}catch(Exception e){
				log.error("Captured Error when process folder " + e.getMessage(),e);
				processSource.cleanTemp();
				System.exit(1);
			}
	}
}
