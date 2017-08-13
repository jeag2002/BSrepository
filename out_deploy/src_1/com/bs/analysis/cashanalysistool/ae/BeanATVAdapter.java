package com.bs.analysis.cashanalysistool.ae;

import com.bs.analysis.cashanalysistool.gui.model.*;

public class BeanATVAdapter implements IBeanATVAdaptor{
	
	ATVModel mod1 = null;
	
	public BeanATVAdapter(){
		mod1 = new ATVModel();
	}
	
	public BeanATVAdapter(String data1, String data2, String data3){
		mod1 = new ATVModel(data1,data2,data3);
	}
	
	public void validate() throws Exception{	
		mod1.validate();
	}
}
