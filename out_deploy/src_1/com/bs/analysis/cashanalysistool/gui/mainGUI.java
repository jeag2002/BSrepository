package com.bs.analysis.cashanalysistool.gui;

import com.bs.analysis.cashanalysistool.gui.view.*;
import com.bs.analysis.cashanalysistool.gui.model.*;
import com.bs.analysis.cashanalysistool.gui.controller.*;

import com.bs.analysis.cashanalysistool.config.DataFileContainer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;



public class mainGUI{
	
	private static Logger log = Logger.getLogger(mainGUI.class);
	
	public static void main(String[] args) throws Exception
	{
		invokeView();
	}
	
	private static void createView() throws Exception
	{
		ATVModel mod1 = new ATVModel();
		ATVController control1 = new ATVController();
	
		//carga vista definida
		mod1.initDefault();
		
        String LookAndFeel  = UIManager.getSystemLookAndFeelClassName();
        //String LookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
        UIManager.setLookAndFeel(LookAndFeel);
        
		//UIManager.setLookAndFeel(new SubstanceLookAndFeel());
		//UIManager.setLookAndFeel("org.jvnet.substance.SubstanceLookAndFeel"); 
		JFrame DisplayFrame = new JFrame("CashAnalisisTool");
		
		AnalysisToolView newView = new AnalysisToolView(DisplayFrame,control1);
		control1.addView(newView);
		control1.addModel(mod1);
		
		DisplayFrame.setLocationRelativeTo(null);
		DisplayFrame.setSize(600,500);//600,500
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		DisplayFrame.setLocation((int)dim.getWidth()/4,(int)dim.getHeight()/4);
		DisplayFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		DisplayFrame.getContentPane().add(newView);
		DisplayFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		DisplayFrame.setVisible(true);
	}
	
	private static void invokeView() throws Exception
	{
    			
		PropertyConfigurator.configure("./config/log4j.properties");
    	log.info("######################################################");
    	log.info("                                                      ");
    	log.info("           BEGIN CASHANALYSISTOOL PROCESS             ");
    	log.info("                                                      ");
    	log.info("######################################################");
    	
    	javax.swing.SwingUtilities.invokeLater(
    			new Runnable()
    			{
    				Logger log = Logger.getLogger("Runnable");
    				public void run(){
   					try{
    						createView();
    					}catch (Exception e)
    					{log.error("Exception when try to create the view " + e.getMessage(),e);}
    				}
    			}
        );   	
	}	
}
