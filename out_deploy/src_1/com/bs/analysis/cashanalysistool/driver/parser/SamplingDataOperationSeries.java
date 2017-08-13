package com.bs.analysis.cashanalysistool.driver.parser;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class SamplingDataOperationSeries{
	private String id;
	private String type;
	private String operation;
	private String dataini;
	private String datafin;
	private long diff;
	private SamplingSerie container;
	
	public SamplingDataOperationSeries(){
		this.id = "";
		this.type = "";
		this.operation = "";
		this.dataini = "";
		this.datafin = "";
		this.container = new SamplingSerie();
		this.diff = 0;
	}
	
	public SamplingDataOperationSeries(String id, String operation, SamplingSerie serie)
	{	
		this.id = id;
		this.operation = operation;
		this.container = serie;
		try{
			getDataFromSerie(serie);
		}catch(Exception e){}
	}
	
	public String BasicFormattedStringSerie(){
		String cadena = "";
		cadena += "\n" + this.operation + ";" + this.id + ";" + ((this.type!="")&&(this.type!=null)? this.type+";":"") + 
		this.dataini + ";" + this.datafin + ";" + this.diff + ";";
		cadena += container.BasicFormattedStringSerie();
		cadena += "\n;;;;";
 		return cadena;
		
	}
	
	public SamplingSerie getContainer() {
		return container;
	}

	public void setContainer(SamplingSerie container) {
		this.container = container;
	}

	public String getDatafin() {
		return datafin;
	}

	public void setDatafin(String datafin) {
		this.datafin = datafin;
	}

	public String getDataini() {
		return dataini;
	}

	public void setDataini(String dataini) {
		this.dataini = dataini;
	}
		
	public String toString()
	{
		String cadena = "\n";
		cadena += "[\n";
		cadena += " id      	::= " + this.id + "\n";
		cadena += " operation	::= " + this.operation + "\n";
		if ((type!=null) && (type!="")){
		cadena += " tipo        ::= " + this.type + "\n";		
		}
		cadena += " dataini 	::= " + this.dataini + "\n";
		cadena += " datafin 	::= " + this.datafin + "\n";
		cadena += " diff 	 	::= " + this.diff + "\n";
 		cadena += (container!=null? container.toString() : "");
  		cadena += "\n]";
		return cadena;	
	}

	public long getDiff() {
		return diff;
	}

	public void setDiff(long diff) {
		this.diff = diff;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
	private void getDataFromSerie(SamplingSerie serie) throws Exception{
		
		String ini = "";
		String fin = "";
		
		SimpleDateFormat dat2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
		
		if (serie != null){
			Hashtable hash = serie.getContainer();
			Vector vect = new Vector(hash.keySet());
			
			if (vect.size()!=0){
				Collections.sort(vect);
				ini = (String)vect.firstElement();
				fin = (String)vect.lastElement();
				SamplingDataParsed ini_data = (SamplingDataParsed)serie.getContainer().get(ini);
				SamplingDataParsed fin_data = (SamplingDataParsed)serie.getContainer().get(fin);
				this.dataini = (ini_data.getBegin().equals("")? ini_data.getEnd(): ini_data.getBegin());
				this.datafin = (fin_data.getEnd().equals("")? fin_data.getBegin(): fin_data.getEnd());
				this.diff = Long.parseLong(fin)-Long.parseLong(ini);
			}else{
				this.dataini = "01/01/1980 00:00:00.000";
				this.datafin = dat2.format(new Date());
				this.diff = dat2.parse(this.datafin).getTime() - dat2.parse(this.dataini).getTime();
			}
			

		}
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
}
