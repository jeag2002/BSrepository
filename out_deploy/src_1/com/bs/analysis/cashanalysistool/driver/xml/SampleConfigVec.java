package com.bs.analysis.cashanalysistool.driver.xml;

import java.util.Vector;
import java.io.Serializable;


import org.apache.log4j.*;

public class SampleConfigVec implements Serializable{
	private Vector SampleConfigVec;
	
	private Logger log = Logger.getLogger(SampleConfigVec.class);

	public SampleConfigVec()
	{
		SampleConfigVec = new Vector();
	}
	
	public Vector getSampleConfigVec() {
		return SampleConfigVec;
	}

	public void setSampleConfigVec(Vector sampleConfigVec) {
		SampleConfigVec = sampleConfigVec;
	}
	
	public void addConfigPattern(SampleConfig pat1)
	{
		if (pat1 != null){
			//log.debug(pat1.toString());
			SampleConfigVec.add(pat1);
		}
	}
	
	public int size()
	{
		return SampleConfigVec.size();
	}
		
}
