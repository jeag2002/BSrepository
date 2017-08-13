package com.bs.analysis.cashanalysistool.gui.monitor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EtchedBorder;

import com.bs.analysis.cashanalysistool.driver.provider.CashAnalysisProvOpe;


import java.beans.*;
import java.util.Random;

import org.apache.log4j.*;


public class monitor{

	private JFrame frame = null;
	private static Logger log = Logger.getLogger(monitor.class);
	private boolean isclose;
	
    
	private void createAndShowGUI() throws Exception{

        String LookAndFeel  = UIManager.getSystemLookAndFeelClassName();
        UIManager.setLookAndFeel(LookAndFeel);
        frame = new JFrame("monitor");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize(550,500);
		frame.setLocation((int)dim.getWidth()/4,(int)dim.getHeight()/4);
		PanelMonitor newView = new PanelMonitor();
		frame.getContentPane().add(newView);
		frame.setVisible(true);
		
		
        frame.addWindowListener(new WindowListener() {
            public void windowClosed(WindowEvent arg0) {
            	log.debug("Ventana cerrada");
            	isclose = true;
            }
            public void windowActivated(WindowEvent arg0) {
            	log.debug("Ventana activada");
            	isclose = false;
            }
            public void windowClosing(WindowEvent arg0) {
            	log.debug("Ventana cerrandose");
            	isclose = true;
            }
            public void windowDeactivated(WindowEvent arg0) {
            	log.debug("Ventana desactivandose");
            	isclose = false;
            }
            public void windowDeiconified(WindowEvent arg0) {
            	log.debug("Ventana sin icono");
            	isclose = false;
            }
            public void windowIconified(WindowEvent arg0) {
            	log.debug("Ventana con icono");
            	isclose = false;
            }
            public void windowOpened(WindowEvent arg0) {
            	log.debug("Ventana abriendose");
            	isclose = false;
            }
        });
    }

    public void setMessage(String msg) throws Exception{
    	PanelMonitor nView = (PanelMonitor)frame.getContentPane().getComponent(0);
    	nView.setMsg(msg);
    }

    public void eraseMessage() throws Exception{
    	try{
    		PanelMonitor nView = (PanelMonitor)frame.getContentPane().getComponent(0);
    		nView.eraseMsg();
    	}catch(Exception e)
    	{
    	}
    }

    public boolean isClose(){
    	return isclose;
    }
    
    public monitor(){
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

class PanelMonitor extends JPanel{
	JTextArea TA;

	public PanelMonitor(){
		super(new BorderLayout());
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),"Output Data"));
        TA = new JTextArea(5, 20);
        TA.setMargin(new Insets(5,5,5,5));
        TA.setEditable(false);
        JScrollPane pScroll = new JScrollPane(TA, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        add(pScroll,BorderLayout.CENTER);
	}

	public void setMsg(String msg){
		TA.append(msg);
	}

	public void eraseMsg(){
		TA.setText("");
	}

}
