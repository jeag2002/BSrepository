package com.bs.analysis.cashanalysistool.gui.validator.forms;

import com.bs.analysis.cashanalysistool.gui.validator.*;
import com.bs.analysis.cashanalysistool.gui.validator.beans.*;
import com.bs.analysis.cashanalysistool.gui.validator.context.*;
import com.bs.analysis.cashanalysistool.gui.view.AnalysisToolView;

import com.bs.analysis.cashanalysistool.config.*;

import javax.swing.JComponent;

import org.apache.log4j.*;


/**
 * Validaciones parte cliente (es vacio?, fecha correcta?,...)
 * @author EBA
 */
public class ATVFormValidate{
	
	private static Logger log = Logger.getLogger(ATVFormValidate.class);

	
    public static boolean execute(ViewScrnContext context){
    	AnalysisToolView regScrn = context.getRegScrn();
        
    	regScrn.setErrorMsg("");
    	
    	AnalysisToolViewBean reg = new AnalysisToolViewBean();
        reg.setFolderpath(regScrn.getFolderpath());
        reg.setDateini(regScrn.getDateini());
        reg.setDatefin(regScrn.getDatefin());
        
        
        
        boolean success = false;  
        try {
        	DataFileContainer cont = DataFileContainer.getInstance();
        	cont.setDefaultConfigFile();
            if(ValidationEngine.validate(reg.NAME, reg, cont.getInfo("ValForm1"))){ 
            	success = true;
            }
            else{
            	success = false;
            }
        }catch (Exception e) {
        	regScrn.setCliErrMsg(e.getMessage());
        	success = false;
        }
        return success;
    }
    
    public static boolean validateBean(ViewScrnContext context, String Field){
    	boolean success = false;
    	
    	AnalysisToolView regScrn = context.getRegScrn();
    	regScrn.setErrorMsg("");
    	
    	AnalysisToolViewBean reg = new AnalysisToolViewBean();
        reg.setFolderpath(regScrn.getFolderpath());
        reg.setDateini(regScrn.getDateini());
        reg.setDatefin(regScrn.getDatefin());
    
        log.debug("Analizando campo " + Field);
        
    	try {
    		DataFileContainer cont = DataFileContainer.getInstance();
    		cont.setDefaultConfigFile();
            if(ValidationEngine.validateBean(reg.NAME, reg, Field, cont.getInfo("ValForm1"))){ 
            	success = true;
            }
            else{
            	success = false;
            }
        }catch (Exception e) {
        	regScrn.setCliErrMsg(e.getMessage());
        	success = false;
        }
    	return success;	
    }
    
    
}
