package com.bs.analysis.cashanalysistool.config;

import org.apache.commons.configuration.*;


public class DataFileContainer{
	
	private static DataFileContainer INSTANCE;
	
	private static CompositeConfiguration config;
	
    private DataFileContainer() throws Exception{
    	config = new CompositeConfiguration();
    	config.addConfiguration(new SystemConfiguration());
    }
    

    private synchronized static void createInstance() throws Exception {
        if (INSTANCE == null) { 
            INSTANCE = new DataFileContainer();
        }
    }
    
    public static DataFileContainer getInstance() throws Exception {
        if (INSTANCE == null) createInstance();
        return INSTANCE;
    }
    
    public static void  setConfigFile(String file) throws Exception{	
    	config.addConfiguration(new PropertiesConfiguration(file));
    }
    
    public static void setDefaultConfigFile() throws Exception{
    	config.addConfiguration(new PropertiesConfiguration("./config/cashtool.properties"));
    }
    
    public String getInfo(String ticket) throws Exception{
    	return config.getString(ticket);
    }
    
    public String[] getInfoList(String ticket) throws Exception{
    	return config.getStringArray(ticket);
    }
    
    
}
