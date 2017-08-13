package com.bs.analysis.cashanalysistool.driver.provider;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;


import com.bs.analysis.cashanalysistool.driver.parser.SamplingDataParsed;
import com.bs.analysis.cashanalysistool.driver.parser.SamplingSerie;


/**
 * User: EBA
 * Date: 22-jul-2005
 * Time: 16:52:18
 */

public class CashAnalysisProvider{
	
	private static SamplingSerie container;	
    private static CashAnalysisProvider INSTANCE = null;

    private CashAnalysisProvider() 
    {
    	container = new SamplingSerie();
    }

    private synchronized static void createInstance() {
        if (INSTANCE == null) { 
            INSTANCE = new CashAnalysisProvider();
        }
    }
    
    public static CashAnalysisProvider getInstance() {
        if (INSTANCE == null) createInstance();
        return INSTANCE;
    }

	public static SamplingSerie getContainer() {
		
		return container;
	}

	public static void setContainer(SamplingSerie container) {
		CashAnalysisProvider.container = container;
	}
    
	public static void addElement(String key, Object value)
	{
		container.addElement(key,value);
	}
	
	public static void removeElement(String key) throws Exception
	{
		container.removeElement(key);
	}
	
	public void removeAllElements()
	{
		container.removeAllElements();
	}
    
	public String toString()
	{
		return container.toString();
	}
	
}

	
	
