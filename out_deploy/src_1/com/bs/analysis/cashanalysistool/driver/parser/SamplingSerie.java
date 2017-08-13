package com.bs.analysis.cashanalysistool.driver.parser;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.bs.analysis.cashanalysistool.driver.parser.SamplingDataParsed;

/**
 * User: EBA
 * Date: 22-jul-2005
 * Time: 16:52:18
 */

public class SamplingSerie{
	
	private Hashtable container;

	public SamplingSerie(){
		container = new Hashtable();
	}

	public Hashtable getContainer() {
		Hashtable container1 = (Hashtable)container.clone();
		return container1;
	}

	public void setContainer(Hashtable container) {
		this.container = container;
	}
    
	public void addElement(String key, Object value)
	{
		long new_key = 0;
		if (container.containsKey(key))
		{
			SamplingDataParsed par1 = (SamplingDataParsed)value;	
				if (par1.getTit().indexOf("MDCS")!=-1)
				{this.container.put(String.valueOf(Long.parseLong(key)-1),value);}
				else
				{this.container.put(String.valueOf(Long.parseLong(key)+1),value);}
		}
		else
		{this.container.put(key,value);}
	}
	
	public  void removeElement(String key) throws Exception
	{
		if (container.isEmpty())
		{throw new Exception("Container has not any element!");}
		else if (!container.containsKey(key))
		{throw new Exception("Container has not any element with " + key + " key ");}
		else
		{container.remove(key);}
		
	}
	
	public void removeAllElements()
	{
		container.clear();
	}
    
	public String toString()
	{
		String cadena = "\n";
		String keys = "";
		String car = "";
		
		
		Vector vect = new Vector(container.keySet());
		Collections.sort(vect);
		Enumeration en = vect.elements();
		
		while (en.hasMoreElements())
		{
			keys = (String)en.nextElement();
			car = ((SamplingDataParsed)container.get(keys)).toString();
			cadena += "[ " + keys + ", { " + car  + "}]\n";
		}
		
		return cadena;
	}	
	
	public String BasicFormattedStringSerie(){
		String cadena = "";
		String keys = "";
		String car = "";
		
		Vector vect = new Vector(container.keySet());
		Collections.sort(vect);
		Enumeration en = vect.elements();
		
		while (en.hasMoreElements())
		{
			keys = (String)en.nextElement();
			car = ((SamplingDataParsed)container.get(keys)).toString();
			cadena += "\n" + car;
		}
		return cadena;
	}
	
	
	public boolean equals(Object arg1){
		
		boolean res = false;
		
		Hashtable data_comparable = (Hashtable)arg1;
		
		Vector vect1 = new Vector(container.keySet());
		Vector vect2 = new Vector(data_comparable.keySet());
	
		if (vect1.size()!=vect2.size()){res = false;}
		else{
			vect1.clear(); vect2.clear();
			vect1.add(container.values());
			vect2.add(data_comparable.values());
			res = vect1.equals(vect2);
		}
		return res;
	}
	
	public Vector getIds(){
		Vector idkeys = new Vector();
		String keys = "";
		String car = "";
		
		Vector vect = new Vector(container.keySet());
		Collections.sort(vect);
		
		Enumeration en = vect.elements();
			
			while (en.hasMoreElements())
			{
				keys = (String)en.nextElement();
				car = ((SamplingDataParsed)container.get(keys)).getTit();
				idkeys.add(car);
			}
		
		return idkeys;
	}
	
	
}
