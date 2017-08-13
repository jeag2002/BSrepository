package com.bs.analysis.cashanalysistool.driver.parser;

import java.util.Date;
import java.text.SimpleDateFormat;

import org.apache.log4j.*;


/**
 * User: 	EBA
 * Date: 	22-jul-2005
 * Time: 	16:52:18
 * Comment:	
 */

public class SamplingDataParsed {
	
	private String tit;
	private String begin;
	private String end;
	private long diff;
	
	private Logger log = Logger.getLogger(SamplingDataParsed.class);
	
    public SamplingDataParsed()
    {
    	this.tit = "";
    	this.begin = "";
    	this.end = "";
    	this.diff = 0;
    }
    
    public SamplingDataParsed(String tit, String begin, String end, long diff)
    {
    	this.tit = tit;
    	this.begin = begin;
    	this.end = end;
    	this.diff = diff;
    }

	public String getBegin() {
		return begin;
	}

	public void setBegin(String begin) {
		this.begin = begin;
	}

	public long getDiff() {
		return diff;
	}

	public void setDiff(long diff) {
		this.diff = diff;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getTit() {
		return tit;
	}

	public void setTit(String tit) {
		this.tit = tit;
	}
	
	public String toString()
	{
		String res = "";
		SimpleDateFormat befdat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
		SimpleDateFormat dat = new SimpleDateFormat("HH:mm:ss.SSS");
		try{
			res = this.tit + "; " + (!this.begin.equals("")? dat.format(befdat.parse(this.begin)):"") + "; " + (!this.end.equals("")? dat.format(befdat.parse(this.end)):"") + "; " + this.diff + "; ";
		}catch(Exception e)
		{
			log.error("Error at present the action data " + e.getMessage(),e);
			res = "NO_DATA";
		}
		return res;
	}
	
	public boolean equals(Object arg1){
		boolean res = false;
		SamplingDataParsed SDP = (SamplingDataParsed)arg1;
		res = this.tit.equalsIgnoreCase(SDP.getTit());
		return res;
	}
}
