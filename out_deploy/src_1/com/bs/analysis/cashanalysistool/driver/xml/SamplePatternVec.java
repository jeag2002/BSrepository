package com.bs.analysis.cashanalysistool.driver.xml;

import java.util.Vector;
import java.io.Serializable;

public class SamplePatternVec implements Serializable{
	private Vector SamplePatternVec;

	public SamplePatternVec()
	{
		SamplePatternVec = new Vector();
	}
	
	public Vector getSamplePatternVec() {
		return SamplePatternVec;
	}

	public void setSamplePatternVec(Vector samplePatternVec) {
		SamplePatternVec = samplePatternVec;
	}
	
	public void addSamplePattern(SamplePattern pat1)
	{
		if (pat1 != null)
			SamplePatternVec.add(pat1);
	}
	
	public int size()
	{
		return SamplePatternVec.size();
	}
		
}
