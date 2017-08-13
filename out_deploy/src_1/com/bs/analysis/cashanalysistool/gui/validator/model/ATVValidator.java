package com.bs.analysis.cashanalysistool.gui.validator.model;

import java.io.File;
import java.text.SimpleDateFormat;

import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.Field;
import org.apache.commons.validator.util.ValidatorUtils;

import org.apache.log4j.*;

//import sun.util.logging.resources.logging;

public class ATVValidator {
	
	static Logger log = Logger.getLogger(ATVValidator.class);
	
    public static boolean validateTimeInterval (Object bean, ValidatorAction va, Field field) 
    {
        boolean res = true;
    	String value = ValidatorUtils.getValueAsString(bean, field.getProperty());
        String sProperty2 = field.getVarValue("secondProperty");
        String value2 = ValidatorUtils.getValueAsString(bean, sProperty2);
        
        SimpleDateFormat ff = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        
        try{
        	if ((value!=null) && (value2!=null)){	
        		if (ff.parse(value).before(ff.parse(value2))){
        			res=false;
        		}else{
        			res=true;
        		}
        	}else
        	{
        		res=false;
        	}
        }catch(Exception e){
        	log.error("Exception caught when try to validate time interval " + e.getMessage(),e);
        	res = false;
        }
        return res;
    }
        
    public static boolean validateExistFile(Object bean, Field field){
    	boolean res = false;
    	String value = ValidatorUtils.getValueAsString(bean, field.getProperty());
    	File file = new File(value);
    	res = file.exists();
    	return res;
    }
    
    
}
