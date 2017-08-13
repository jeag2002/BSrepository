package com.bs.analysis.cashanalysistool.output.xml;

import java.util.Vector;
import java.io.Serializable;

import com.bs.analysis.cashanalysistool.output.xml.*;

public class SamplePatternOutVect implements Serializable{
	
	private Vector SamplePatternOutVec;
	
	public SamplePatternOutVect()
	{
		SamplePatternOutVec = new Vector();
	}
	
	public void addSamplePatternOut(SamplePatternOut pat1)
	{
		if (pat1 != null)
			SamplePatternOutVec.add(pat1);
	}
	
	public int size()
	{
		return SamplePatternOutVec.size();
	}

	public Vector getSamplePatternOutVec() {
		return SamplePatternOutVec;
	}

	public void setSamplePatternOutVec(Vector samplePatternOutVec) {
		SamplePatternOutVec = samplePatternOutVec;
	}
	
	
	public SamplePatternOut getSamplePatternOut(String idoperation, String type){
		SamplePatternOut pat1 = null;
		boolean encontrado = false;
			for(int i=0; i<SamplePatternOutVec.size() && !encontrado; i++){
				pat1 = (SamplePatternOut)SamplePatternOutVec.get(i);
					if (pat1.getId().equalsIgnoreCase(idoperation) && pat1.getType().equalsIgnoreCase(type)){
						encontrado = true;
					}else{
						pat1 = null;
					}
			}
		return pat1;
	}
	
	
	
}
