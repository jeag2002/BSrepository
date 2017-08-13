package com.bs.analysis.cashanalysistool.gui.view;

import javax.swing.JPanel;
import java.beans.PropertyChangeEvent;

public abstract class AbstractViewPanel extends JPanel{
	public abstract void modelPropertyChange(PropertyChangeEvent evt);
}
