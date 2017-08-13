package com.bs.analysis.cashanalysistool.gui.model;

/**
 * Controlador específico para la vista AnalysisToolView
 */

import com.bs.analysis.cashanalysistool.gui.controller.AbstractController;
import com.bs.analysis.cashanalysistool.gui.validator.context.*;
import java.lang.ThreadGroup;



import com.bs.analysis.cashanalysistool.ae.*;

import org.apache.log4j.*;

public class ATVController extends AbstractController{
	public static final String FOLDERPATH_PROPERTY = "Folderpath";
	public static final String DATEINI_PROPERTY = "Dateini";
	public static final String DATEFIN_PROPERTY = "Datefin";
	public static final String DATEINTERVAL_PROPERTY = "Dateinterval";
	public static final String INPUTFORMAT_PROPERTY = "Inputformats"; 
	public static final String OUTPUTFORMAT_PROPERTY = "Outputformats";
	public static final String OUTPUTDATEFORMAT_PROPERTY = "Outputdateformats";
	public static final String OUTPUTPATH_PROPERTY = "Outputpath";
	public static final String OUTPUTOPTIONS_PROPERTY = "Outputoptions";
	
	private Logger log = Logger.getLogger(ATVController.class);
	
	private processSource process;
	
    public void changeElementFolderpath(String newFolderpath) {
        super.setModelProperty(FOLDERPATH_PROPERTY, newFolderpath);
    }
    
    public void changeElementDateini(String newdateini) {
        super.setModelProperty(DATEINI_PROPERTY, newdateini);
    }
    
    public void changeElementDatefin(String newdatefin) {
        super.setModelProperty(DATEFIN_PROPERTY, newdatefin);
    }
    
    public void changeElementOutputpath(String newoutputpath) {
        super.setModelProperty(this.OUTPUTPATH_PROPERTY, newoutputpath);
    }
    
    public void changeElementInputformat(String newinputformat) {
        super.setModelProperty(this.INPUTFORMAT_PROPERTY, newinputformat);
    }
    
    public void changeElementOutputformat(String newoutputformat) {
        super.setModelProperty(this.OUTPUTFORMAT_PROPERTY, newoutputformat);
    }
    
    
    public void changeElementDateInterval(String newdateinterval) {
        super.setModelProperty(this.DATEINTERVAL_PROPERTY, newdateinterval);
    }
    
    public void changeElementOutputOptions(String newOutputOption) {
        super.setModelProperty(this.OUTPUTOPTIONS_PROPERTY, newOutputOption);
    }
    
    
    public void changeElementOutputDateOptions(String newOutputDateOption){
    	super.setModelProperty(this.OUTPUTDATEFORMAT_PROPERTY,newOutputDateOption);
    	
    }
    
    public void Submit(ViewScrnContext context){
    	context.getRegScrn().setSuccesfulMsg("Comienza todo el proceso!");
    	ProcessSignals.getInstance().setFinishSignal("NO");
    	try{
    		super.ValidateModel("com.bs.analysis.cashanalysistool.gui.model.ATVModel");
    		ATVModel mod1 = (ATVModel)super.getModel("com.bs.analysis.cashanalysistool.gui.model.ATVModel");
    		log.info("Submit data  mod1 = (" + mod1.toString() + ") to AE driver");
    		process = new processSource(mod1, context);
    		process.setPriority(Thread.MAX_PRIORITY);
    		process.start();
    		
    	}catch(Exception e){
    		context.getRegScrn().setErrorMsg(e.getMessage());
    		context.getRegScrn().setKOprogressBar();
    	}
    }
    
    public synchronized void Stop(ViewScrnContext context){
    	try{
    		if ((process!=null) && (process.isAlive())){
    			ProcessSignals.getInstance().setFinishSignal("SI");
    		}
    		
    	}catch(Exception e){
    		context.getRegScrn().setErrorMsg(e.getMessage());
    		context.getRegScrn().setKOprogressBar();
    	}
    }
    
    
    
}
