package com.bs.analysis.cashanalysistool.gui.help;

/*
 import javax.swing.text.html.*;
import javax.swing.text.*;
import javax.swing.event.*;
import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;


public class Browser {

 	protected static void setPage(JEditorPane jep, String url){
		try {
			jep.setPage(url);
		}
		catch (IOException e) {
			System.err.println(e);
			System.exit(-1);
		}

	}
	
	class backButtonListener implements ActionListener {
		protected JEditorPane jep;
		protected JLabel label;
		protected JButton backButton;
		protected Vector history;
		public backButtonListener(JEditorPane jep, JButton backButton, Vector history, JLabel label){
			this.jep = jep;
			this.backButton = backButton;
			this.history = history;
			this.label = label;
		}

		public void actionPerformed(ActionEvent e){
			try{
				//the current page is the last, remove it
				String curl = (String)history.lastElement();
				history.removeElement(curl);
					
				curl = (String)history.lastElement();
				System.out.println("Back to " + curl);
				setPage(jep,curl);
				label.setText("<html><b>URL:</b> "+ curl);
				if (history.size() == 1)
					backButton.setEnabled(false);
			}
			catch (Exception ex){
				System.out.println("Exception " + ex);
			}
		}
	}
	
	class LinkFollower implements HyperlinkListener {
		protected JEditorPane jep;
		protected JLabel label;
		protected JButton backButton;
		protected Vector history;
		public LinkFollower(JEditorPane jep, JButton backButton, Vector history, JLabel label){
			this.jep = jep;
			this.backButton = backButton; 
			this.history = history;
			this.label = label;
		}

		public void hyperlinkUpdate(HyperlinkEvent evt){
			if (evt.getEventType() == HyperlinkEvent.EventType.ACTIVATED){
				try {
					String currentURL = evt.getURL().toString();
					history.add(currentURL);
					backButton.setEnabled(true);
					System.out.println("Going to " + currentURL);
					setPage(jep,currentURL);
					label.setText("<html><b>URL:</b> "+ currentURL);
				}
				catch (Exception e) {
					System.out.println("ERROR: Trouble fetching url");
				}
			}
		}

	}

 	public Browser(String initialPage){

		Vector history = new Vector();
		history.add(initialPage);
		
		// set up the editor pane
		JEditorPane jep = new JEditorPane();
		jep.setEditable(false);
		setPage(jep, initialPage);

		// set up the window
		JScrollPane scrollPane = new JScrollPane(jep);     
		JFrame f = new JFrame("Simple Web Browser");
		f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		//Exit the program when user closes window.
		f.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e){
					System.exit(0);
				}
			});

		//Label where we show the url
		JLabel label = new JLabel("<html><b>URL:</b> "+ initialPage);

		
		JButton backButton = new JButton ("Back");
		backButton.setActionCommand("back");
		backButton.setToolTipText("Go to previous page");
		backButton.setEnabled(false);
		backButton.addActionListener(new backButtonListener(jep, backButton, history, label));

		JButton exitButton = new JButton ("Exit");
		exitButton.setActionCommand("exit");
		exitButton.setToolTipText("Quit this application");
		exitButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});

		//A toolbar to hold all our buttons
		JToolBar toolBar = new JToolBar();
		toolBar.add(backButton);
		toolBar.add(exitButton);


		jep.addHyperlinkListener(new LinkFollower(jep, backButton, history, label));

		//Set up the toolbar and scrollbar in the contentpane of the frame
		JPanel contentPane = (JPanel)f.getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.setPreferredSize(new Dimension(400, 100));
		contentPane.add(toolBar, BorderLayout.NORTH);
		contentPane.add(scrollPane, BorderLayout.CENTER);
		contentPane.add(label, BorderLayout.SOUTH);

		f.pack();
		f.setSize(640, 360);
		f.setVisible(true);


	}
	public static void main(String[] args) {
		String initialPage = new String("http://www.cse.sc.edu");

		if (args.length > 0) initialPage = args[0];

		Browser b = new Browser(initialPage);
	}
	
}
*/

/*
import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;

public class WebBrowser extends JFrame {

	public JPanel
		address_panel, window_panel;

	public JLabel
		address_label;

	public JTextField
		address_tf;

	public JEditorPane
		window_pane;

	public JScrollPane
		window_scroll;

	public JButton
		address_b;

	private Go go = new Go();

	public WebBrowser() throws IOException {

		// Define address bar
		address_label = new JLabel(" address: ", SwingConstants.CENTER);
		address_tf = new JTextField("http://www.yahoo.com");
		address_tf.addActionListener(go);
		address_b = new JButton("Go");
		address_b.addActionListener(go);

		window_pane = new JEditorPane("http://www.yahoo.com");
		window_pane.setContentType("text/html");
		window_pane.setEditable(false);

		address_panel = new JPanel(new BorderLayout());
		window_panel = new JPanel(new BorderLayout());

		address_panel.add(address_label, BorderLayout.WEST);
		address_panel.add(address_tf, BorderLayout.CENTER);
		address_panel.add(address_b, BorderLayout.EAST);

		window_scroll = new JScrollPane(window_pane);
		window_panel.add(window_scroll);

		Container pane = getContentPane();
		pane.setLayout(new BorderLayout());

		pane.add(address_panel, BorderLayout.NORTH);
		pane.add(window_panel, BorderLayout.CENTER);

		setTitle("web browser");
		setSize(800,600);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

	}

	public class Go implements ActionListener{

		public void actionPerformed(ActionEvent ae){

			try {

				window_pane.setPage(address_tf.getText());

			} catch (MalformedURLException e) {     // new URL() failed
				window_pane.setText("MalformedURLException: " + e);
			} catch (IOException e) {               // openConnection() failed
				window_pane.setText("IOException: " + e);
			}

		}

	}

	public static void main(String args[]) throws IOException {
		WebBrowser wb = new WebBrowser();
	}

}  
*/


public class browser {

}
