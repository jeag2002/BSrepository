package com.bs.analysis.cashanalysistool.driver.provider;

import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.bs.analysis.cashanalysistool.driver.provider.*;

public class CashAnalysisContainer{
	
	private static Hashtable reg;
	private static CashAnalysisContainer INSTANCE;
	private static String lastElement;
	private static String lastData;
	private static String refData;
	private static Logger log = Logger.getLogger(CashAnalysisContainer.class);
	
	///////////////////////////////////////////////////////////////////////
    private CashAnalysisContainer(){
    	reg = new Hashtable();
    }

    private synchronized static void createInstance() {
        if (INSTANCE == null) { 
            INSTANCE = new CashAnalysisContainer();
        }
    }
    
    public static CashAnalysisContainer getInstance() {
        if (INSTANCE == null) createInstance();
        return INSTANCE;
    }
	 
    //////////////////////////////////////////////////////////////////////
    public void setreg(String Method, String data, long timestamp)
    {
    	Vector vect = new Vector();
    	CashAnalysisBean be1 = new CashAnalysisBean(data,timestamp);
    	
    	if (!reg.containsKey(Method))
    	{
    		vect.add(be1);
    		reg.put(Method,vect);
    	}
    	else
    	{
    		vect = (Vector)reg.get(Method);
    		vect.add(be1);
    		reg.remove(Method);
    		reg.put(Method,vect);
    	}
    }
    
    
    
	public CashAnalysisBean getLastCashData(String Method)
	{
	    CashAnalysisBean res = new CashAnalysisBean("NOTFOUND",0);
		if (reg.containsKey(Method))
		{
			Vector vect = (Vector)reg.get(Method);
			if (vect.size() > 0)
			{res = (CashAnalysisBean)vect.lastElement();
			vect.removeElement(vect.lastElement());
			reg.remove(Method);
			reg.put(Method,vect);
			}
		}
		return res;
	}
	
	public static void setContainer(Hashtable reg){
		CashAnalysisContainer.reg = reg;
	}

	public static String getLastElement() {
		return lastElement;
	}

	public static void setLastElement(String lastElement) {
		CashAnalysisContainer.lastElement = lastElement;
	}

	public static String getLastData() {
		return lastData;
	}

	public static void setLastData(String lastData) {
		CashAnalysisContainer.lastData = lastData;
	}

	public static String getRefData() {
		return refData;
	}

	public static void setRefData(String refData) {
		CashAnalysisContainer.refData = refData;
	}
	
	
	
	
}
