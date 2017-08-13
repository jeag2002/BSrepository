package com.bs.analysis.cashanalysistool.output.basic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Hashtable;

import org.apache.log4j.Logger;

import com.bs.analysis.cashanalysistool.driver.provider.*;
import com.bs.analysis.cashanalysistool.gui.validator.context.ViewScrnContext;

public class basicOutput{
	
	private static basicOutput INSTANCE = null;
	private File file_1 = null;
	private CashAnalysisProvOpe DataOpe = null;
	
	private Logger log = Logger.getLogger(basicOutput.class);
    private basicOutput(){
    	file_1 = null;
    	DataOpe = null;
    }

    private synchronized static void createInstance() {
        if (INSTANCE == null) { 
            INSTANCE = new basicOutput();
        }
    }
    
    public static basicOutput getInstance() {
        if (INSTANCE == null) createInstance();
        return INSTANCE;
    }
    
    public void processFile(String filename, CashAnalysisProvOpe DataOpe, String ConfigMode, String is_formatted, String dateformatted, ViewScrnContext Context) throws Exception{
    	if (DataOpe.getContainer().size() > 0){		
    			file_1 = new File(filename);
		    	BufferedWriter wf = new BufferedWriter(new FileWriter(file_1));
		    	if (!ConfigMode.equalsIgnoreCase("GENERAL")){
		    		wf.write(DataOpe.DataFormattedString("CSV", ConfigMode ,is_formatted, dateformatted));
		    	}else{
		    		wf.write(DataOpe.BasicFormattedString(ConfigMode, "<no data>"));
		    	}
				wf.flush();
		    	wf.close();
    	}else{
    		log.warn("\nNo data to output to csv file!");
    		if (Context != null){
    			Context.getRegScrn().ChangeMessageProgressBar("\nNo data to output to csv file!");
    		}
    	}
    }

}

