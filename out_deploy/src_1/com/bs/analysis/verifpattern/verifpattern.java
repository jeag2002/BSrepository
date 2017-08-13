package com.bs.analysis.verifpattern;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import java.util.*;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.WindowConstants;



import com.bs.analysis.verifpattern.view.*;

public class verifpattern{
	
	private static String country = "";
	private static String language = "";

	private static Locale currentLocale = null;
	private static ResourceBundle bundle = null;
	
	private static void createView() throws Exception
	{
	  
        currentLocale = new Locale(country,language);
    	bundle = ResourceBundle.getBundle("com.bs.analysis.verifpattern.properties.MessagesBundle",currentLocale);
    	
		String LookAndFeel  = UIManager.getSystemLookAndFeelClassName();
        
        UIManager.setLookAndFeel(LookAndFeel);
        JFrame DisplayFrame = new JFrame(bundle.getString("titulo1"));
        
        verifpatternview newView = new verifpatternview(DisplayFrame, bundle, country, language);
		
		DisplayFrame.setLocationRelativeTo(null);
		DisplayFrame.setSize(650,400);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		DisplayFrame.setLocation((int)dim.getWidth()/4,(int)dim.getHeight()/4);
		DisplayFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		DisplayFrame.getContentPane().add(newView);
		DisplayFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		DisplayFrame.setVisible(true);
	}
	
	
	
	private static void invokeView(){
    	javax.swing.SwingUtilities.invokeLater(
    			new Runnable()
    			{
    				public void run(){
   					try{
    						createView();
    					}catch (Exception e)
    					{}
    				}
    			}
        );  
	}
	
	public static void main(String args[]){
		
		if (args.length == 0){	
    		language = new String("es");
    		country = new String("ES");
		}
		else{	
			if (args.length != 2) { 	
        		language = new String("es");
        		country = new String("ES");
        	}else {
        		language = new String(args[1]);
        		country = new String(args[0]);
        	}
		}
    	invokeView();
		
	}
		
}

