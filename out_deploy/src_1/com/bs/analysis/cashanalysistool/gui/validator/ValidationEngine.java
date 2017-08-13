package com.bs.analysis.cashanalysistool.gui.validator;

import org.apache.commons.validator.*;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import java.text.MessageFormat;

import com.bs.analysis.cashanalysistool.gui.validator.beans.*;

/**
 * Motor Validaciones
 * @author EBA
 *
 */

public class ValidationEngine {
	
    private static ResourceBundle apps = ResourceBundle.getBundle("com.bs.analysis.cashanalysistool.gui.validator.applicationResources");
    public static boolean validate(String formName, AnalysisToolViewBean ATVB, String definitionXml) throws Exception {
        
    	FileInputStream definitionStream = new FileInputStream(new File(definitionXml));
        
        ValidatorResources resources;
        try {
            resources = new ValidatorResources(definitionStream);
        } catch (Exception e) {
            throw new Exception("Error al cargar las validaciones " + e.getMessage());
        } 
        
        Validator validator = new Validator(resources, ATVB.NAME);
        validator.setParameter(Validator.BEAN_PARAM, ATVB);

        ValidatorResults results;
        try {
            results = validator.validate();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        //return true;
        return parseResults(formName, ATVB, results, resources);
    }

    /**
     * Formats the validation result.
     */
    public static boolean parseResults(
        String formName,
        Object bean,
        ValidatorResults results,
        ValidatorResources resources) throws Exception {

        String errorMsg = new String();

        boolean success = true;

        // Start by getting the form for the current locale and Bean.
        Form form = resources.getForm(Locale.getDefault(), formName);

        // Iterate over each of the properties of the Bean which had messages.
        Iterator propertyNames = results.getPropertyNames().iterator();
        while (propertyNames.hasNext()) {
            String propertyName = (String) propertyNames.next();

            // Get the Field associated with that property in the Form
            Field field = form.getField(propertyName);

            // Look up the formatted name of the field from the Field arg0
            String prettyFieldName = apps.getString(field.getArg(0).getKey());

            // Get the result of validating the property.
            ValidatorResult result = results.getValidatorResult(propertyName);

            // Get all the actions run against the property, and iterate over their names.
            Iterator keys = result.getActions();
            while (keys.hasNext()) {
                String actName = (String) keys.next();
                ValidatorAction action = resources.getValidatorAction(actName);
        
                if (!result.isValid(actName)) {
                    success = false;
                    String message = apps.getString(action.getMsg());
                    Object[] args = { prettyFieldName };
                    throw new Exception(MessageFormat.format(message, args));
                }
            }
        }
        return success;
    }
    
    
    /**
     * Analizando un campo en concreto
     */
    public static boolean validateBean(String formName, AnalysisToolViewBean ATVB, String Field, String definitionXml) throws Exception {
    	boolean success = false;
    
    	FileInputStream definitionStream = new FileInputStream(new File(definitionXml));
        
        ValidatorResources resources;
        try {
            resources = new ValidatorResources(definitionStream);
        } catch (Exception e) {
            throw new Exception("Error al cargar las validaciones " + e.getMessage());
        } 
        
        Validator validator = new Validator(resources, ATVB.NAME);
        validator.setParameter(Validator.BEAN_PARAM, ATVB);

        ValidatorResults results;
        try {
            results = validator.validate();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        
        return parseResultsBean(formName, ATVB, Field, results, resources);    	
    }
    
    
    
    public static boolean parseResultsBean(
            String formName,
            Object bean,
            String fieldname,
            ValidatorResults results,
            ValidatorResources resources) throws Exception {

            String errorMsg = new String();

            boolean success = true;

            // Start by getting the form for the current locale and Bean.
            Form form = resources.getForm(Locale.getDefault(), formName);

            // Iterate over each of the properties of the Bean which had messages.
            Field field = form.getField(fieldname);

            // Look up the formatted name of the field from the Field arg0
            String prettyFieldName = apps.getString(field.getArg(0).getKey());

            // Get the result of validating the property.
            ValidatorResult result = results.getValidatorResult(fieldname);

            // Get all the actions run against the property, and iterate over their names.
            
            Iterator keys = result.getActions();
            while (keys.hasNext()) {
                    String actName = (String) keys.next();
                    ValidatorAction action = resources.getValidatorAction(actName);
            
                    if (!result.isValid(actName)) {
                        success = false;
                        String message = apps.getString(action.getMsg());
                        Object[] args = { prettyFieldName };
                        throw new Exception(MessageFormat.format(message, args));
                    }
            }
            return success;
        }
        
    
    
    
    
    
    
    
    
    
    
}

