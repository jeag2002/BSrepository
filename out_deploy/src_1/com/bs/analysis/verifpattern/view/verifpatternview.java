package com.bs.analysis.verifpattern.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;


import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;



public class verifpatternview extends JPanel{
	
	private JFrame parentFrame;
	private JTextArea textAreaIni;
	private JTextArea pattern;
	private JTextArea textAreaOut;
	
	private JLabel etiqTextAreaIni;
	private JLabel etiqPattern;
	private JLabel etiqTextAreaOut;
	
	private JButton captura_info;
	private JButton traducir;
	private JButton volcar_info;
	private JButton borrar;
	private JButton salir;
	
	private ResourceBundle bundle;
	
	private String country;
	private String language;
	
	private Locale currentLocale;
		
	private final JFileChooser fc = new JFileChooser();
	
	
	
	private void panelTexto (JPanel panCentral){
		
		JPanel panTextos = new JPanel();
    	panTextos.setLayout(new GridLayout(3,1));
		
		JPanel panTextoIni = new JPanel();
		panTextoIni.setLayout(new BoxLayout(panTextoIni, BoxLayout.Y_AXIS));
	
		
    	panTextoIni.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),bundle.getString("texto_entrada")));

		JPanel panTextoPat = new JPanel();
		panTextoPat.setLayout(new BoxLayout(panTextoPat, BoxLayout.Y_AXIS));
    	panTextoPat.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),bundle.getString("texto_patron")));
    	
    	JPanel panSubTextoPat = new JPanel();
    	panSubTextoPat.setLayout(new BoxLayout(panSubTextoPat, BoxLayout.Y_AXIS));
    	
		JPanel panTextoOut = new JPanel();
		panTextoOut.setLayout(new BoxLayout(panTextoOut, BoxLayout.Y_AXIS));
    	panTextoOut.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),bundle.getString("texto_salida")));

    	
		textAreaIni = new JTextArea("",5,50);
		JScrollPane scrollAreaIni = new JScrollPane(textAreaIni);
		
		
		pattern = new JTextArea("",5,50);
		JScrollPane scrollAreaPat = new JScrollPane(pattern);
		
		
		textAreaOut = new JTextArea("",5,50);
		JScrollPane scrollAreaOut = new JScrollPane(textAreaOut);
		
		panTextoIni.add(scrollAreaIni);
		panSubTextoPat.add(scrollAreaPat);
		
		panTextoPat.add(panSubTextoPat);
		panTextoOut.add(scrollAreaOut);

		panTextos.add(panTextoIni);
		panTextos.add(panTextoPat);
		panTextos.add(panTextoOut);
		
		panCentral.add(panTextos,BorderLayout.CENTER);
	}
	
	private void panelBotonera (JPanel panCentral){
    	JPanel panBoton = new JPanel();
    	panBoton.setLayout(new FlowLayout());
		
		JPanel pan3 = new JPanel();
    	pan3.setLayout(new BoxLayout(pan3,BoxLayout.X_AXIS));
    	
    	//aplica el patron
    	traducir = new JButton(bundle.getString("boton_analiza"));
        traducir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	traduce();
            }
        });
    	
    	//captura la info de un fichero
    	captura_info = new JButton(bundle.getString("boton_captura"));
    	captura_info.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	volca_texto();
            }
        });
    	
        //volcar la info de un fichero
        volcar_info = new JButton(bundle.getString("boton_volcado"));
        volcar_info.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	volca_info();
            }
        });
        
        //borrar info del sistema
        borrar = new JButton(bundle.getString("boton_borrado"));
        borrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	borra_info();
            }
        });
        
       
    	salir = new JButton(bundle.getString("boton_salir"));
        salir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	salirButtonActionPerformed(evt);   	
            }
        });
    	
        pan3.add(traducir);
    	pan3.add(captura_info);
    	pan3.add(volcar_info);
    	pan3.add(borrar);
    	pan3.add(salir);
    	panBoton.add(pan3);
        panCentral.add(panBoton,BorderLayout.SOUTH);	
	}
	
	
	private void borra_info(){
		int value = JOptionPane.showConfirmDialog(null, bundle.getString("msg_conf_borrado"),bundle.getString("msg_tit_conf_borrado"), JOptionPane.YES_NO_OPTION);
		if (value == 0){
			this.textAreaIni.setText("");
			this.pattern.setText("");
			this.textAreaOut.setText("");
		}
	}
	
	private void volca_texto(){
			int returnVal = fc.showOpenDialog(this);
			try{
				if (returnVal == JFileChooser.APPROVE_OPTION){
					String filepath = fc.getSelectedFile().getPath();
					volca_info_prog(filepath);
				}
			}catch(Exception ignored_exception)
			{
				JOptionPane.showMessageDialog(null,bundle.getString("msg_err_volc") + ignored_exception.getMessage());
				ignored_exception.printStackTrace();
			}
			
	}
	
	
	private void volca_info_prog(String filepath)throws Exception{
		FileChannel fc = (new FileInputStream(filepath)).getChannel();
		ByteBuffer buf = fc.map(FileChannel.MapMode.READ_ONLY,0,fc.size());
		String cadena = Charset.forName("ISO-8859-1").newDecoder().decode(buf).toString();
		this.textAreaIni.setText(cadena);
		
//		BufferedWriter wf = new BufferedWriter(new FileWriter(fil1));
		
	}
	
	
	private void volca_info(){
		int value = JOptionPane.showConfirmDialog(null, bundle.getString("msg_conf_volc"), bundle.getString("msg_tit_conf_volc"), JOptionPane.YES_NO_OPTION);
		if (value == 0){
			int returnVal = fc.showSaveDialog(this);
			try{
				if (returnVal == JFileChooser.APPROVE_OPTION){
					String filepath = fc.getSelectedFile().getPath();
					volca_info_fichero(filepath);
					JOptionPane.showMessageDialog(null,bundle.getString("msg_final_volcado"));
				}
			}catch(Exception ignored_ex){}
			
		}
	}
	
	private void volca_info_fichero(String Path) throws Exception{
		File fil1 = new File(Path);
		BufferedWriter wf = new BufferedWriter(new FileWriter(fil1));
		wf.write("////////////////TEXTO DE ENTRADA A ANALIZAR ////////////////////////////\n");
		wf.flush();
		wf.write(this.textAreaIni.getText()+"\n\n");
		wf.flush();
		wf.write("////////////////PATRON ////////////////////////////\n");
		wf.flush();
		wf.write(this.pattern.getText()+"\n\n");
		wf.flush();
		wf.write("////////////////TEXTO DE SALIDA ////////////////////////////\n");
		wf.flush();
		wf.write(this.textAreaOut.getText()+"\n\n");
		wf.flush();
		wf.close();
		
	}
	
	
	private void traduce(){
		
		Pattern p;
		Matcher matcher;
		
		String textini = "";
		String patron = "";
		String textoutput = "";
		
		textini = textAreaIni.getText();
		patron = pattern.getText();
		
		textAreaOut.setFont(new Font("SansSerif", Font.BOLD, 12));
		textAreaOut.setLineWrap(true);
		textAreaOut.setWrapStyleWord(true);
		textAreaOut.setForeground(Color.BLUE);
		
		
		p = Pattern.compile(patron,Pattern.DOTALL|Pattern.MULTILINE);
		matcher = p.matcher(textini);
		while(matcher.find())
		{
			textoutput += bundle.getString("msg_tit")+"\n";
			int subgroups = matcher.groupCount();
			
			textoutput += bundle.getString("msg_texto_1") + " " + matcher.group(0) + " " + bundle.getString("msg_texto_2") + " " + matcher.start() + "\n";
			
			if (subgroups > 0){
				textoutput += bundle.getString("msg_texto_3")+": \n";
				for(int i=0; i<subgroups; i++){
					textoutput += "match(" + i + ") " + matcher.group(i) + "\n"; 
				}
			}
			
			textoutput += "\n\n";
			
		}
		
		//si no se ha encontrado nada, mensaje en rojo
		
		if (textoutput.equalsIgnoreCase("")){
			textAreaOut.setForeground(Color.RED);
			 textoutput = bundle.getString("msg_no_coincidencia");
		}
		
		this.textAreaOut.setText(textoutput);		
	}
	
    private void salirButtonActionPerformed(ActionEvent e){
	 	   int value = 0;
		   value = JOptionPane.showConfirmDialog(null, bundle.getString("msg_conf_sal"), "Cancel", JOptionPane.YES_NO_OPTION);
		   if (value==0)
		   {
			    parentFrame.dispose();
			    parentFrame = null;
			    System.gc();
			    System.exit(0);
		   }
	}
	    
	public void initGUI(){
	
    setLayout(new BorderLayout());
    JPanel panCentral = new JPanel();
    panCentral.setLayout(new BorderLayout());
    //panCentral.setLayout(new BoxLayout(panCentral,BoxLayout.Y_AXIS));
		
    //////////////////////PANEL TEXTO ////////////////////////////
    panelTexto(panCentral);
    //////////////////////PANEL BOTONERA ////////////
    panelBotonera(panCentral);
    //////////////////////INSERCION PANELES EN PANEL CENTRAL///////
    add(panCentral,BorderLayout.CENTER);
		
	}
	
	public verifpatternview(JFrame parentFrame, ResourceBundle bundle, String country, String language){
		
		
		this.parentFrame = parentFrame;	
		this.country = country;
		this.language = language;
		this.bundle = bundle;
		
		initGUI();
	}
	
}
