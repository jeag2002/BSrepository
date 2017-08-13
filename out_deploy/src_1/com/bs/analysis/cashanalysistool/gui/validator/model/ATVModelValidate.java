package com.bs.analysis.cashanalysistool.gui.validator.model;

import com.bs.analysis.cashanalysistool.gui.model.*;
import com.bs.analysis.cashanalysistool.gui.validator.ValidationEngine;
import com.bs.analysis.cashanalysistool.gui.validator.beans.AnalysisToolViewBean;
import com.bs.analysis.cashanalysistool.gui.view.AnalysisToolView;

import com.bs.analysis.cashanalysistool.config.*;
import java.io.*;
import java.util.*;

import java.text.SimpleDateFormat;


/**
 * Una vez comprobada que se cumplen las reglas de cliente, se miran
 * las reglas de servidor. Estas son:
 * -Existencia del folder donde están los logs
 * -Comparación entre fechas (dateini < datefin)
 */

public class ATVModelValidate{
	public static void execute(ATVModel ATV) throws Exception
	{
        AnalysisToolViewBean reg = new AnalysisToolViewBean();
        reg.setFolderpath(ATV.getFolderpath());
        reg.setDateini(ATV.getDateini());
        reg.setDatefin(ATV.getDatefin());
        
        boolean success = false;  
        try {
        	DataFileContainer conf = DataFileContainer.getInstance();
        	//conf.setDefaultConfigFile();
            if(ValidationEngine.validate(reg.NAME, reg, conf.getInfo("ValMod1"))){ 
            	success = true;
            }
            else{
            	success = false;
            }
        }catch (Exception e) {
        	throw new Exception(e.getMessage());
        }
	}
}
