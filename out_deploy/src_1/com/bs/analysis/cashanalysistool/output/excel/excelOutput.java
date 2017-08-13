 package com.bs.analysis.cashanalysistool.output.excel;

import java.io.File;
import java.util.Enumeration;
import java.util.Vector;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.xmlrules.DigesterLoader;
import org.apache.log4j.Logger;


import com.bs.analysis.cashanalysistool.config.DataFileContainer;
import com.bs.analysis.cashanalysistool.driver.parser.SamplingOperation;
import com.bs.analysis.cashanalysistool.driver.provider.CashAnalysisProvOpe;
import com.bs.analysis.cashanalysistool.driver.xml.SampleConfig;
import com.bs.analysis.cashanalysistool.gui.validator.context.ViewScrnContext;
import com.bs.analysis.cashanalysistool.output.basic.basicOutput;
import com.bs.analysis.cashanalysistool.output.xml.SamplePatternOut;
import com.bs.analysis.cashanalysistool.output.xml.SamplePatternOutVect;


public class excelOutput{
	
	private static excelOutput INSTANCE;
	private String templatePath;
	private CashAnalysisProvOpe DataOpe;
	private excelOutputOpe xlsOpe;
	private static DataFileContainer conf;
	private static Logger log = Logger.getLogger(excelOutput.class);
	
	private excelOutput(){
		templatePath = "";
		DataOpe = null;
		try{
			conf = DataFileContainer.getInstance();
			conf.setConfigFile(".\\config\\cashtool.properties");
		}catch(Exception e){}
	}
	
    private synchronized static void createInstance() {
        if (INSTANCE == null) {INSTANCE = new excelOutput();}
    }
    
    public static excelOutput getInstance() {
        if (INSTANCE == null) createInstance();
        return INSTANCE;
    }
    
    
    /**
     * 
     * @param rules_path
     * @param config_path
     * @return
     * @throws Exception
     */
    private SamplePatternOutVect ParseOutputXMLData(String rules_path, String config_path) throws Exception{
		Digester dig = DigesterLoader.createDigester((new File(rules_path)).toURL());
		SamplePatternOutVect SPOV = (SamplePatternOutVect)dig.parse(config_path);
		return SPOV;
	} 
    
    
    
    
    /**
     * Genera un fichero excel por operacion
     * @param templatePath
     * @param container
     */
    public void process(String templatePath, String skillsID, CashAnalysisProvOpe container, SampleConfig sys_conf, String formatted, String formattedate,ViewScrnContext Context) throws Exception{
    	   //parseo de todas las operaciones
    	   SamplePatternOutVect SPOV = ParseOutputXMLData(DataFileContainer.getInstance().getInfo("RulOutput"), DataFileContainer.getInstance().getInfo("FileOutput"));
    	   SamplePatternOut out = new SamplePatternOut();
    	   Vector skillstemplate = new Vector();
    	
    	
    	   	//si se selecciona, se hace filtrado. Si no, se cogen todas las trazas que tengan el mismo comportamiento
    		if (!formatted.equalsIgnoreCase("<no data>"))
    		{
    			container.formatSeries();
    		}
    	
    		if (container.getContainer().size() > 0){
		    		    Enumeration keys = container.getContainer().keys();
		    			while(keys.hasMoreElements()){
				    			String id_operation = (String)keys.nextElement();
				    			SamplingOperation ope = container.getOperation(id_operation);	
				    			out = SPOV.getSamplePatternOut(id_operation,"EXCEL");
				    			
				    			String skillIDConfig = conf.getInfo(skillsID+"_xls");
				    			
				    			if (skillIDConfig.equalsIgnoreCase("DETALLES")){
				    				skillstemplate = out.getDetalles();
				    			}else if (skillIDConfig.equalsIgnoreCase("FUNC")){
				    				skillstemplate = out.getFuncionalidades();
				    			}else{
				    				skillstemplate = new Vector();
				    			}
				    			
				    			
				    			if (out!=null){
				    					if (skillstemplate.size() > 0){
						    					xlsOpe = new excelOutputOpe(ope, skillstemplate, templatePath, sys_conf);
						    					try{
						    						xlsOpe.processOperation();
						    						if (Context != null){
						    							Context.getRegScrn().ChangeMessageProgressBar("\nGenerate file " + id_operation + ".xls");
						    						}
						    					}catch(Exception e){
						    						log.warn("Cannot generate the output excel for this operation. Reason " + e.getMessage(),e);
						    					}
				    					}
				    			}
		    		  }
    		}else{
    			log.warn("\nNo data to output to xls file!");
				if (Context != null){
					Context.getRegScrn().ChangeMessageProgressBar("\nNo data to output to xls file!");
				}
    			
    		}
    }
    
}
