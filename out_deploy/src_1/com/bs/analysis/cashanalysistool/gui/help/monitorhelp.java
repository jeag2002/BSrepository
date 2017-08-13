package com.bs.analysis.cashanalysistool.gui.help;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.UIManager;

import org.apache.log4j.Logger;


public class monitorhelp {

		private help helpframe = null;
		private static Logger log = Logger.getLogger(monitorhelp.class);
		private boolean isclose;
		
	    
		private void createAndShowGUI() throws Exception{

	        String LookAndFeel  = UIManager.getSystemLookAndFeelClassName();
	        UIManager.setLookAndFeel(LookAndFeel);
	        helpframe = new help();
	        helpframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			helpframe.setSize(550,500);
			helpframe.setLocation((int)dim.getWidth()/4,(int)dim.getHeight()/4);
			helpframe.setVisible(true);
	    }


	    public boolean isClose(){
	    	return isclose;
	    }
	    
	    public monitorhelp(){
	    	this.isclose = false;
	        javax.swing.SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	            	try{
	            		createAndShowGUI();
	            	}catch(Exception e){}
	            }
	        });
	    }

	
	
	
	
	
	
	

}
