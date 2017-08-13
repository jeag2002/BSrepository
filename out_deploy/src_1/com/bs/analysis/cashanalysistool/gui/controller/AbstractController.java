package com.bs.analysis.cashanalysistool.gui.controller;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import java.lang.reflect.*;
import java.lang.reflect.Method;
import java.util.ArrayList;

import com.bs.analysis.cashanalysistool.gui.model.*;
import com.bs.analysis.cashanalysistool.gui.view.*;


import org.apache.log4j.*;

public abstract class AbstractController implements PropertyChangeListener
{
	private ArrayList regViews;
	private ArrayList regModels;
	private Logger log = Logger.getLogger(AbstractController.class);
	
	
	/**
	 * 
	 *
	 */
	public AbstractController()
	{
		regViews = new ArrayList();
		regModels = new ArrayList();
	}
	
	/**
	 * 
	 * @param model
	 */
	
    public void addModel(AbstractModel model) {
        regModels.add(model);
        model.addPropertyChangeListener(this);
    }

    /**
     * 
     * @param model
     */
    public void removeModel(AbstractModel model) {
        regModels.remove(model);
        model.removePropertyChangeListener(this);
    }
    
    /**
     * 
     * @param view
     */
    public void addView(AbstractViewPanel view) {
        regViews.add(view);
    }

    /**
     * 
     * @param view
     */
    public void removeView(AbstractViewPanel view) {
        regViews.remove(view);
    }
    
    
    public AbstractModel getModel(int index) throws Exception
    {
    	AbstractModel mod = null;
    	if (index < regModels.size())
    	{mod = (AbstractModel)regModels.get(index);}
    	else
    	{
    		throw new Exception("model with index " + index + " not found!");
    	}
    	return mod;
    }
    
    
    public AbstractModel getModel(String className) throws Exception
    {
    	AbstractModel mod = null;
    	Class modelo = Class.forName(className);
    	boolean encontrado = false;
    	
    	for(int i=0; i<regModels.size(); i++)
    	{
    		mod = (AbstractModel)regModels.get(i);
    		if (modelo.isInstance(mod)){
    			encontrado = true;
    		}else{
    			mod = null;
    		}
    	}
    	return mod;
    }
    
    /**
     * 
     */
    public void propertyChange(PropertyChangeEvent evt) {

    	for (int i=0; i<regViews.size(); i++)
    	{
    		AbstractViewPanel view = (AbstractViewPanel)regViews.get(i);
    		view.modelPropertyChange(evt);
    	}
    }
    
    protected void setModelProperty(String propertyName, Object newValue) {
    	for(int i=0; i<regModels.size(); i++)
    	{
    		try{
    			AbstractModel model = (AbstractModel)regModels.get(i);
    			Method method = model.getClass().getMethod("set"+propertyName, new Class[]{newValue.getClass()});
    			method.invoke(model, new Object[]{newValue});
    		}catch(Exception e)
    		{
    			log.error("Error al invocar metodo set "+ propertyName + " ::= " + newValue,e);
    		}
    	}
    }
     
  
    //Valida la clase
    protected void ValidateModel(String classforName) throws Exception
    {
    	Class modelo = Class.forName(classforName);
    	boolean encontrado = false;
    	for (int i=0; (i<regModels.size() && !encontrado); i++){    		
    		AbstractModel model = (AbstractModel)regModels.get(i);
    		if (modelo.isInstance(model)){
    			model.validate();
    			encontrado = true;
    		}else{
    			model = null;
    		}
    	}	
    	//if (encontrado){log.info("La clase " + classforName + " ha sido encontrada y validada correctamente!");}
    }
    
    //derivar al gestor de ventanas adonde tiene que ir cuando se llama a submit
    protected void Submit(String forward) throws Exception
    {
    }
}
