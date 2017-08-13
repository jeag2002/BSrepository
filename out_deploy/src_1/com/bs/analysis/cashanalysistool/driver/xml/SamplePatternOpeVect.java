package com.bs.analysis.cashanalysistool.driver.xml;

import java.util.Vector;
import java.io.Serializable;

public class SamplePatternOpeVect implements Serializable{
	private Vector SamplePatternOpeVec;
	
	public SamplePatternOpeVect(){
		SamplePatternOpeVec = new Vector();
	}

	public Vector getSamplePatternOpeVec() {
		return SamplePatternOpeVec;
	}

	public void setSamplePatternOpeVec(Vector samplePatternOpeVec) {
		SamplePatternOpeVec = samplePatternOpeVec;
	}
	
	public void addSamplePatternOpe(SamplePatternOpe pat1)
	{
		if (pat1 != null){
			SamplePatternOpeVec.add(pat1);
		}
	}
	
	public int size()
	{
		return SamplePatternOpeVec.size();
	}
	
}
