package com.bs.analysis.cashanalysistool.gui.validator.beans;

/**
 * Clase utilizada para validaciones de cliente.
 * @author alcaraz
 *
 */

import org.apache.commons.lang.builder.ToStringBuilder;

public class AnalysisToolViewBean{
	
	public static String NAME = "AnalysisToolView";
	
	private String folderpath;
	private String dateini;
	private String datefin;
	
	
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
	
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        builder.append(folderpath);
        builder.append(dateini);
        builder.append(datefin);
        return builder.toString();
    }
	
	
}
