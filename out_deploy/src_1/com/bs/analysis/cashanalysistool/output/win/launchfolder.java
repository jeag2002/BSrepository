package com.bs.analysis.cashanalysistool.output.win;

import java.io.File;
import java.lang.*;
import org.apache.log4j.*;


public class launchfolder{
	
	private static launchfolder INSTANCE;
	private static Logger log = Logger.getLogger(launchfolder.class);
	
	public launchfolder(){
	}
	
    private synchronized static void createInstance() {
        if (INSTANCE == null) { 
            INSTANCE = new launchfolder();
        }
    }
    
    public static launchfolder getInstance() {
        if (INSTANCE == null) createInstance();
        return INSTANCE;
    }
	
	public void execute(){
	    
		try{
			java.lang.Runtime rt = java.lang.Runtime.getRuntime(); 
			String commandline[] = new String[4];
			commandline[0] = "CMD";
			commandline[1] = "/c";
			commandline[2] = "explorer";
			commandline[3] = System.getProperty("user.dir")+File.separatorChar+"out";
			Process proc = rt.exec(commandline);
			log.debug("Win32 process trap " + commandline.toString() + " works fine");
		}catch(Exception e){
			log.error("Have a problem when try to show the content of " + System.getProperty("user.dir")+File.separatorChar+"out" + " " + e.getMessage(),e);
		}   
	}
	
	public void execute(String path){
		try{
			java.lang.Runtime rt = java.lang.Runtime.getRuntime(); 
			String commandline[] = new String[4];
			//this function works with WinXP/2k/NT 4.0 . older versions should change commandline[0] = "CMD" with commandline[0] = "COMMAND"
			commandline[0] = "CMD";
			commandline[1] = "/c";
			commandline[2] = "explorer";
			commandline[3] = path;
			Process proc = rt.exec(commandline);
			log.debug("Win32 process trap " + commandline.toString() + " works fine");
		}catch(Exception e){
			log.error("Have a problem when try to show the content of " + path + " " + e.getMessage(),e);
		}   
	}
	
	
	
	
}