package com.bs.analysis.cashanalysistool.gui.model;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public abstract class AbstractModel{
	
	protected PropertyChangeSupport propertyChangeSupport;
	
    public AbstractModel()
    {
        propertyChangeSupport = new PropertyChangeSupport(this);
    }
	
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }
    
    public abstract void validate() throws Exception;
}
