package com.bs.analysis.cashanalysistool.gui.help;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import com.bs.analysis.cashanalysistool.config.DataFileContainer;
import com.bs.analysis.cashanalysistool.driver.provider.CashAnalysisProvOpe;

import java.beans.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Random;

import org.apache.log4j.*;


public class help extends JFrame{
		
	public JPanel	address_panel, window_panel;
	public JLabel	address_label;
	public JEditorPane window_pane;
	public JScrollPane window_scroll;
	
	public help() throws Exception {
	//window_pane = new JEditorPane("file:///d:/produccion/EBPDCashTool/help/holamundo.html");
	//window_pane = new JEditorPane("help/holamundo.html");
    //http://172.30.35.1:7080/ebranchsite/jsp/TEXHome.jsp	
		
    super.setTitle("Ventana de Ayuda");
	DataFileContainer conf = DataFileContainer.getInstance();	
	window_pane = new JEditorPane(conf.getInfo("UrlHelp"));
	window_pane.setContentType("text/html");
	window_pane.setEditable(false);
	window_panel = new JPanel(new BorderLayout());

	window_scroll = new JScrollPane(window_pane);
	window_panel.add(window_scroll);

	Container pane = getContentPane();
	pane.setLayout(new BorderLayout());
	
	pane.add(window_panel, BorderLayout.CENTER);
	
	setSize(800,600);
	setVisible(true);
	setDefaultCloseOperation(EXIT_ON_CLOSE);

	}
	
	
	public static void main(String args[]) throws Exception {
		help wb = new help();
	}
	
}



