package com.bs.analysis.cashanalysistool.driver.parser;

import java.util.Vector;

public class SamplingOperation{
	private String Operation;
	private Vector Series;
	
	public SamplingOperation()
	{
		this.Operation = "";
		this.Series = new Vector();
	}
	
	public SamplingOperation(String Operation, Vector Series)
	{
		this.Operation = Operation;
		this.Series = Series;
	}

	public String getOperation() {
		return Operation;
	}

	public void setOperation(String operation) {
		Operation = operation;
	}

	public Vector getSeries() {
		return Series;
	}

	public void setSeries(Vector series) {
		Series = series;
	}
	
	public void addSerie(Object Serie){
		Series.add(Serie);
	}
	
	public int getSize(){
		return Series.size();
	}
	
	public String toString()
	{
		String cadena = "\n";
		
		cadena += "[\n";
		cadena += " id       ::= " + this.Operation + "\n";
		cadena += " numseries::= " + this.Series.size() + "\n";
		
		for(int i=0; i<Series.size(); i++)
		{
			SamplingDataOperationSeries ser1 = (SamplingDataOperationSeries)Series.get(i);
			cadena += ser1.toString();
		}
		cadena += "\n]";
		return cadena;
	}
	
	
	public String BasicFormattedStringOpe(){
		
		String cadena = "";
		
		for(int i=0; i<Series.size(); i++){
			SamplingDataOperationSeries ser1 = (SamplingDataOperationSeries)Series.get(i);
			cadena += ser1.BasicFormattedStringSerie();
			
		}
		
		return cadena;
	}
	
}
