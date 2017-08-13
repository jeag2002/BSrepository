package com.bs.analysis.cashanalysistool.driver.xml;

public class SampleConfig{
	private String id_ini;
	private String ini_pattern;
	private String id_ske;
	private String ske_pattern;
	private String id_enc;
	private String enc_pattern;
	private String id_fin;
	private String fin_pattern;
	private String id_fin_enc;
	private String fin_enc_pattern;
	
	
	public String getEnc_pattern() {
		return enc_pattern;
	}
	public void setEnc_pattern(String enc_pattern) {
		this.enc_pattern = enc_pattern;
	}
	public String getFin_pattern() {
		return fin_pattern;
	}
	public void setFin_pattern(String fin_pattern) {
		this.fin_pattern = fin_pattern;
	}
	public String getId_enc() {
		return id_enc;
	}
	public void setId_enc(String id_enc) {
		this.id_enc = id_enc;
	}
	public String getId_fin() {
		return id_fin;
	}
	public void setId_fin(String id_fin) {
		this.id_fin = id_fin;
	}
	public String getId_ini() {
		return id_ini;
	}
	public void setId_ini(String id_ini) {
		this.id_ini = id_ini;
	}
	public String getId_ske() {
		return id_ske;
	}
	public void setId_ske(String id_ske) {
		this.id_ske = id_ske;
	}
	public String getIni_pattern() {
		return ini_pattern;
	}
	public void setIni_pattern(String ini_pattern) {
		this.ini_pattern = ini_pattern;
	}
	public String getSke_pattern() {
		return ske_pattern;
	}
	public void setSke_pattern(String ske_pattern) {
		this.ske_pattern = ske_pattern;
	}	
	
	public String toString(){
		String cadena = "\n";
		cadena += this.id_ini + ", " + this.ini_pattern + "\n";
		cadena += this.id_enc + ", " + this.enc_pattern + "\n";
		cadena += this.id_ske + ", " + this.ske_pattern + "\n";
	    cadena += this.id_fin + ", " + this.fin_pattern + "\n";
		return cadena; 
	}
	public String getFin_enc_pattern() {
		return fin_enc_pattern;
	}
	public void setFin_enc_pattern(String fin_enc_pattern) {
		this.fin_enc_pattern = fin_enc_pattern;
	}
	public String getId_fin_enc() {
		return id_fin_enc;
	}
	public void setId_fin_enc(String id_fin_enc) {
		this.id_fin_enc = id_fin_enc;
	}
	
	
}
