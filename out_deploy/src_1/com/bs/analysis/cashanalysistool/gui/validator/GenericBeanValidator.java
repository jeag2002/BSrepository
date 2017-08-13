package com.bs.analysis.cashanalysistool.gui.validator;

import java.util.Locale;

import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.Field;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.util.ValidatorUtils;

/**
 * GenericBeanValidator (POJO)
 * @author EBA
 *
 */
public class GenericBeanValidator {
	    public static boolean validateRequired(Object bean, Field field) {
	       String value = ValidatorUtils.getValueAsString(bean, field.getProperty());
	       return !GenericValidator.isBlankOrNull(value);
	    }
	    
	    public static boolean validateTime(Object bean, Field field) {
	        String value = ValidatorUtils.getValueAsString(bean, field.getProperty());
//	        String sProperty2 = field.getVarValue("secondProperty");
//	        String pattern = ValidatorUtils.getValueAsString(bean, sProperty2);
//	        System.out.println(pattern);
	        return GenericValidator.isDate(value,"dd/MM/yyyy HH:mm:ss",true);
	     } 
}



