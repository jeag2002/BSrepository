package com.bs.analysis.cashanalysistool.ae;

import org.apache.log4j.Logger;

public class BeanATV{
	private String folderpath;
	private String dateini;
	private String datefin;
	private String dateinterval;
	private String inputformats;
	private String outputformats;
	private String outputpath;
	private String isformatted;
	private String outputdateformats;

	private BeanATVAdapter adapter;
	
	private Logger log = Logger.getLogger(BeanATV.class);
	
	public BeanATV(){
		this.folderpath="";
		this.dateini = "";
		this.datefin = "";
		this.dateinterval = "";
		this.inputformats = "";
		this.outputformats = "";
		this.outputpath = "";
		this.isformatted = "";
		this.outputdateformats = "";
	}
	
	public BeanATV(String folderpath, String dateini, String datefin){
		this.folderpath = folderpath;
		this.dateini = dateini;
		this.datefin = datefin;
		this.dateinterval = "";
		this.inputformats = "";
		this.outputformats = "";
		this.outputpath = "";
		this.isformatted = "";
		this.outputdateformats = "";
	}
	
	public BeanATV(String folderpath, String dateini, String datefin, String inputformats, String outputformats, String outputpath){
		this(folderpath, dateini, datefin);
		this.dateinterval = "";
		this.inputformats = inputformats;
		this.outputformats = outputformats;
		this.outputpath = outputpath;
		this.isformatted = "";
		this.outputdateformats = "";
	}
	
	public BeanATV(String folderpath, String dateini, String datefin, String inputformats, String outputformats, String outputpath, String formatted){
		this(folderpath, dateini, datefin);
		this.dateinterval = "";
		this.inputformats = inputformats;
		this.outputformats = outputformats;
		this.outputpath = outputpath;
		this.isformatted = formatted;
		this.outputdateformats = "";
	}
	
	public BeanATV(String folderpath, String dateini, String datefin, String inputformats, String outputformats, String outputpath, String formatted, String dateformatted){
		this(folderpath, dateini, datefin);
		this.dateinterval = "";
		this.inputformats = inputformats;
		this.outputformats = outputformats;
		this.outputpath = outputpath;
		this.isformatted = formatted;
		this.outputdateformats = dateformatted;
	}
	
	
	
	
	/*
	public BeanATV(String folderpath, String dateini, String datefin, String dateinterval, String inputformats, String outputformats, String outputpath){
		this(folderpath, dateini, datefin, inputformats, outputformats, outputpath);
		this.dateinterval = dateinterval;
		this.isformatted = "";
	}
	
	public BeanATV(String folderpath, String dateini, String datefin, String dateinterval, String inputformats, String outputformats, String outputpath, String formatted){
		this(folderpath,dateini,datefin,dateinterval,inputformats,outputformats,outputpath);
		this.isformatted = formatted;
	}
	*/
	
	public String getDatefin() {
		return datefin;
	}

	public void setDatefin(String datefin) {
		this.datefin = datefin;
	}

	public String getDateini() {
		return dateini;
	}

	public void setDateini(String dateini) {
		this.dateini = dateini;
	}

	public String getFolderpath() {
		return folderpath;
	}

	public void setFolderpath(String folderpath) {
		this.folderpath = folderpath;
	}
	
	public void validate() throws Exception{
		(new BeanATVAdapter(this.folderpath, this.dateini, this.datefin)).validate();
	}

	public String getInputformats() {
		return inputformats;
	}

	public void setInputformats(String inputformats) {
		this.inputformats = inputformats;
	}

	public String getOutputformats() {
		return outputformats;
	}

	public void setOutputformats(String outputformats) {
		this.outputformats = outputformats;
	}

	public String getOutputpath() {
		return outputpath;
	}

	public void setOutputpath(String outputpath) {
		this.outputpath = outputpath;
	}

	public String getDateinterval() {
		return dateinterval;
	}

	public void setDateinterval(String dateinterval) {
		this.dateinterval = dateinterval;
	}
	
	public String toString(){
		String cadena = "";
		cadena += "\nfolderpath = " + this.folderpath;
		cadena += "\ndateini = " + this.dateini; 
		cadena += "\ndatefin = " + this.datefin;
		cadena += "\ndateinterval = " + this.dateinterval;
		cadena += "\ninputformats = " + this.inputformats;
		cadena += "\noutputformats = " + this.outputformats;
		cadena += "\noutputpath = " + this.outputpath;
		cadena += "\nisformatted = " +  this.isformatted;
		cadena += "\ndateformatted = " + this.outputdateformats;
		return cadena;
	}

	public String getIsformatted() {
		return isformatted;
	}

	public void setIsformatted(String isformatted) {
		this.isformatted = isformatted;
	}

	public String getOutputdateformats() {
		return outputdateformats;
	}

	public void setOutputdateformats(String outputdateformats) {
		this.outputdateformats = outputdateformats;
	}


}
