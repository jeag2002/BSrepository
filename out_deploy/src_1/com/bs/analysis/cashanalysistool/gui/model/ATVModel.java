package com.bs.analysis.cashanalysistool.gui.model;

/**
 * Modelo especifico para la vista AnalysisToolView
 */

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.bs.analysis.cashanalysistool.gui.model.AbstractModel;
import com.bs.analysis.cashanalysistool.gui.validator.model.*;
import com.bs.analysis.cashanalysistool.config.*;

import org.apache.log4j.*;

public class ATVModel extends AbstractModel{
	
private String folderpath;
private String inputformats;
private String dateini;
private String datefin;
private String dateinterval;
private String outputformats;
private String outputdateformats;
private String outputpath;
private String outputoptions;

private Logger log = Logger.getLogger(ATVModel.class);


public ATVModel(){
	this.folderpath = "";
	this.inputformats = "";
	this.dateini = "";
	this.datefin = "";
	this.dateinterval = "";
	this.outputformats = "";
	this.outputpath = "";
	this.outputoptions = "";
	this.outputdateformats = "";
}


public ATVModel(String folderpath, String dateini, String datefin){
	this.folderpath = folderpath;
	this.inputformats = "";
	this.dateini = dateini;
	this.datefin = datefin;
	this.dateinterval = "";
	this.outputformats = "";
	this.outputpath = "";
	this.outputoptions = "";
}


public ATVModel(String folderpath, String inputformats, String dateini, String datefin, String outputformats, String outputpath) {
	this.folderpath = folderpath;
	this.inputformats = inputformats;
	this.dateini = dateini;
	this.datefin = datefin;
	this.dateinterval = "";
	this.outputformats = outputformats;
	this.outputpath = outputpath;
	this.outputoptions = "";
	
}


public ATVModel(String folderpath, String inputformats, String dateini, String datefin, String dateinterval, String outputformats, String outputpath) {
	this(folderpath, inputformats, dateini, datefin, outputformats, outputpath);
	this.dateinterval = dateinterval;
	this.outputoptions = "";
}


public ATVModel(String folderpath, String inputformats, String dateini, String datefin, String dateinterval, String outputformats, String outputpath, String outputoptions) {
	this(folderpath, inputformats, dateini, datefin, outputformats, outputpath, dateinterval);
	this.outputoptions = outputoptions;
}

public ATVModel(String folderpath, String inputformats, String dateini, String datefin, String dateinterval, String outputformats, String outputpath, String outputoptions, String outputdateoptions) {
	this(folderpath, inputformats, dateini, datefin, outputformats, outputpath, dateinterval,outputoptions);
	this.outputdateformats = outputdateoptions;
	//this.outputoptions = outputoptions;
}


public void initDefault() throws Exception{
	
	SimpleDateFormat form = new SimpleDateFormat("dd/MM/yyyy");
	DataFileContainer conf = DataFileContainer.getInstance();
	conf.getInstance().setDefaultConfigFile();
	
	setFolderpath(conf.getInfo("DefaultFolder"));
	setDateini(form.format(new Date()).toString()+" 00:00:00");
	setDatefin(form.format(new Date()).toString()+" 23:59:59");
	
	String[] conf_list = conf.getInfoList("ConfigFormats");
	if (conf_list.length == 0){
		this.setInputformats("NONE");
	}else{
		this.setInputformats(conf_list[0]);
	}
	
	
	conf_list = conf.getInfoList("DataFormats");
	if (conf_list.length == 0){
		this.setDateinterval("NONE");
	}else{
		this.setDateinterval(conf_list[0]);
	}

	conf_list = conf.getInfoList("OutputFormats");
	if (conf_list.length == 0){
		this.setOutputformats("NONE");
	}else{
		this.setOutputformats(conf_list[0]);
	}
	
	this.setOutputpath(conf.getInfo("OutputBasicFile"));
	this.setOutputoptions("NO");
}

public String getDatefin() {
	return datefin;
}

public void setDatefin(String datefin){
	String olddatefin = this.datefin;	
	this.datefin = datefin;
	log.debug("datefin::= " + datefin);
	firePropertyChange(ATVController.DATEFIN_PROPERTY,olddatefin,datefin);
}

public String getDateini() {
	return dateini;
}

public void setDateini(String dateini){
	String olddateini = this.dateini;
	this.dateini = dateini;
	log.debug("dateini::= " + dateini);
	firePropertyChange(ATVController.DATEINI_PROPERTY,olddateini,dateini);	
}

public String getFolderpath() {
	return folderpath;
}

public void setFolderpath(String folderpath) {
	String oldfolderpath = this.folderpath;
	this.folderpath = folderpath;
	log.debug("folderpath::= " + folderpath);
	firePropertyChange(ATVController.FOLDERPATH_PROPERTY,oldfolderpath,folderpath);
}


public String toString(){
	String cadena = "";
	cadena += "\nfolderpath = " + this.folderpath;
	cadena += "\ndateini = " + this.dateini;
	cadena += "\ndatefin = " + this.datefin;
	cadena += "\ndateinterval = " + this.dateinterval;
	cadena += "\ninputformat = " + this.inputformats;
	cadena += "\noutputformat = " + this.outputformats;
	cadena += "\noutputpath = " + this.outputpath;
	cadena += "\noutputoptions = " + this.outputoptions;
	cadena += "\noutputdateoptions = " + this.outputdateformats;
	return cadena;
}

public void validate() throws Exception
{
	ATVModelValidate.execute(this);
}


public String getInputformats() {
	return inputformats;
}


public void setInputformats(String inputformats) {
	String oldInputFormat = this.inputformats;
	this.inputformats = inputformats;
	log.debug("inputformats ::= " + inputformats);
	firePropertyChange(ATVController.INPUTFORMAT_PROPERTY,oldInputFormat,this.inputformats);
}


public String getOutputformats() {
	return outputformats;
}


public void setOutputformats(String outputformats) {
	String oldOutputFormat = this.outputformats;
	this.outputformats = outputformats;
	log.debug("outputformats ::= " + outputformats);
	firePropertyChange(ATVController.OUTPUTFORMAT_PROPERTY,oldOutputFormat,this.outputformats);	
}


public String getOutputpath() {
	return outputpath;
}


public void setOutputpath(String outputpath) {
	String oldOutputPath = this.outputpath;
	this.outputpath = outputpath;
	log.debug("outputpath ::= " + outputpath);
	firePropertyChange(ATVController.OUTPUTPATH_PROPERTY,oldOutputPath,this.outputpath);
}


public String getDateinterval() {
	return dateinterval;
}


public void setDateinterval(String dateinterval) {
	String oldDateInterval = this.dateinterval;
	this.dateinterval = dateinterval;
	log.debug("dateinterval ::= " + dateinterval);
	firePropertyChange(ATVController.DATEINTERVAL_PROPERTY,oldDateInterval,this.dateinterval);
	
}


public String getOutputoptions() {
	return outputoptions;
}


public void setOutputoptions(String outputoptions) {
	String oldoutputoptions = this.outputoptions;
	this.outputoptions = outputoptions;
	log.debug("outputoptions ::= " + outputoptions);
	firePropertyChange(ATVController.OUTPUTOPTIONS_PROPERTY,oldoutputoptions,this.outputoptions);
	
	
}


public String getOutputdateformats() {
	return outputdateformats;
}


public void setOutputdateformats(String outputdateformats) {
	String oldoutputdateformats = this.outputdateformats;
	this.outputdateformats = outputdateformats;
	log.debug("outputdateformat ::= " + this.outputdateformats);
	firePropertyChange(ATVController.OUTPUTDATEFORMAT_PROPERTY,oldoutputdateformats,this.outputdateformats);
}

}
