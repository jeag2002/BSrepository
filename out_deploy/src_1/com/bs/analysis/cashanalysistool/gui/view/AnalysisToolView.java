package com.bs.analysis.cashanalysistool.gui.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;


import org.apache.log4j.*;

import com.bs.analysis.cashanalysistool.config.DataFileContainer;
import com.bs.analysis.cashanalysistool.gui.help.help;
import com.bs.analysis.cashanalysistool.gui.help.monitorhelp;
import com.bs.analysis.cashanalysistool.gui.model.ATVController;
import com.bs.analysis.cashanalysistool.gui.monitor.*;
import com.bs.analysis.cashanalysistool.gui.controller.*;
import com.bs.analysis.cashanalysistool.gui.validator.context.*;
import com.bs.analysis.cashanalysistool.gui.validator.forms.ATVFormValidate;
import com.bs.analysis.cashanalysistool.output.win.*;

public class AnalysisToolView extends AbstractViewPanel implements PropertyChangeListener{
	
	
	private JFrame parentFrame;
	
	private JTextField folderpath;
	private JTextField dateini;
	private JTextField datefin;
	private JTextField outputpath;
	
	/*ComboBox con las opciones de parseo, intervalos de fecha y seleccion de trazas*/
	private JComboBox selTypProcess;
	private JComboBox selTypData;
	private JComboBox selTypOutput;
	
	/*Checkbox*/
	private JCheckBox selTypFormat; //-->ordena los patrones por tipos de patrones
	private JCheckBox checkTypDate;	//-->ordena los patrones por orden de aparicion
	
	
	private JPanel pan0;
	private JPanel panFechas;
	
	
	private JLabel namefolderpath;
	private JLabel nameformat;
	private JLabel namedateini;
	private JLabel namedatefin;
	private JLabel namedatemode;
	private JLabel nameoutputmode;
	private JLabel nameoutputfile;
	
	private JLabel nametypeformat;
	private JLabel nametypedate;
	
	private JLabel evolution;
	private JLabel errserver;
	private JLabel progress;
	
	private JButton siguiente;
	private JButton cancel;
	private JButton searchfolderpath;
	private JButton terminar;
	private JButton help;
	
	private JProgressBar progressbar;
	private monitor mot1;
	private help help1;
	
	
	private ATVController control;
	private ViewScrnContext context;
	
	private boolean validateFP;
	private boolean validateIF;
	private boolean validateDF;
	
	private int numtraces;
	
	private DataFileContainer conf;
	
	
	private static String antPath="";
	private static int valor =0;
	
	private final JFileChooser fc = new JFileChooser();
    private Logger log = Logger.getLogger(AnalysisToolView.class);
    
    private static ResourceBundle apps = ResourceBundle.getBundle("com.bs.analysis.cashanalysistool.gui.validator.applicationResources");
    
    
    private void panelErrores(JPanel panCentral){
    	errserver = new JLabel("",JLabel.LEFT);
    	JPanel panErr = new JPanel();
    	panErr.setLayout(new FlowLayout());
    	panErr.add(errserver);
    	panCentral.add(panErr);
    }
    
    
    private void panelDirectorio(JPanel panCentral){
    	pan0 = new JPanel();
    	pan0.setLayout(new FlowLayout());
    	pan0.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),"Seleccion ficheros"));
    	
    	
    	JLabel labfolderpath = new JLabel(apps.getString("AnalysisToolView.folderpath.displayname")+":");
    	folderpath = new JTextField(50);
    	
    	
    	
    	folderpath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	folderpathActionPerformed(evt);
            }
        });
    	
    	folderpath.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
            	folderpathFocusLost(evt);
            }
        });
    	
    	
    	searchfolderpath = new JButton("...");
    	searchfolderpath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	 searchButtonActionPerformed(evt);
            }
        });
    	
    	pan0.add(labfolderpath);
    	pan0.add(folderpath);
    	pan0.add(searchfolderpath);
    	panCentral.add(pan0);
    }
    
    private void panelFechas(JPanel panCentral) throws Exception{
    	
    	panFechas = new JPanel();
    	panFechas.setLayout(new BoxLayout(panFechas, BoxLayout.Y_AXIS));
    	
    	panFechas.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),"Parámetros logs"));
      	
    	JPanel panFlowLayoutFechas = new JPanel();
    	panFlowLayoutFechas.setLayout(new FlowLayout());
    	
    	
    	JPanel panDataFechas = new JPanel();
    	panDataFechas.setLayout(new FlowLayout());
    	
    	
    	this.selTypProcess = new JComboBox(conf.getInfoList("ConfigFormats"));
    	
    	selTypProcess.addActionListener(new java.awt.event.ActionListener(){
            	public void actionPerformed(java.awt.event.ActionEvent evt) {
            		selTypProcessActionPerformed(evt);
            	}
    		}
    	);
    	
    	selTypProcess.addFocusListener(new FocusAdapter(){
        		public void FocusLost(java.awt.event.FocusEvent evt) {
        			selTypProcessFocusLost(evt);
        		}
			}
    	);
    	
    	
    	this.selTypFormat = new JCheckBox("",false);
    	
    	
    	selTypFormat.addActionListener(new java.awt.event.ActionListener(){
        		public void actionPerformed(java.awt.event.ActionEvent evt) {
        			selTypFormatActionPerformed(evt);
        		}
			}
    	);
	
    	selTypFormat.addFocusListener(new FocusAdapter(){
    			public void FocusLost(java.awt.event.FocusEvent evt) {
    				selTypFormatFocusLost(evt);
    			}
			}
    	);
    	
    	this.checkTypDate = new JCheckBox("",false);
    	
    	checkTypDate.addActionListener(new java.awt.event.ActionListener(){
    		public void actionPerformed(java.awt.event.ActionEvent evt) {
    			checkTypDateActionPerformed(evt);
    		}
		  }
	    );

	    checkTypDate.addFocusListener(new FocusAdapter(){
			public void FocusLost(java.awt.event.FocusEvent evt) {
				checkTypDateFocusLost(evt);
			}
		  }
	    );
    	
    	
    	
    	
    	//Create a regular text field
    	dateini = new JTextField(30);
    	
    	dateini.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	datainiActionPerformed(evt);
            }
        });
    	
    	
    	dateini.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
            	datainiFocusLost(evt);
            }
        });
        
    	
    	datefin = new JTextField(30);
    
    	datefin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	datafinActionPerformed(evt);
            }
        });
    		
    	datefin.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
            	datafinFocusLost(evt);
            }
        });
    	
    	
        this.selTypOutput = new JComboBox(conf.getInfoList("OutputFormats"));
    	
        selTypOutput.addActionListener(new java.awt.event.ActionListener(){
        		public void actionPerformed(java.awt.event.ActionEvent evt) {
        			selTypOutputActionPerformed(evt);
        		}
			}
    	);
	
        selTypOutput.addFocusListener(new FocusAdapter(){
    			public void FocusLost(java.awt.event.FocusEvent evt) {
    				selTypOutputFocusLost(evt);
    			}
			}
    	);
	
        
        this.outputpath = new JTextField(30);
    	
        outputpath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	outputpathActionPerformed(evt);
            }
        });
    		
    	outputpath.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
            	outputpathFocusLost(evt);
            }
        });
    	
    	selTypData = new JComboBox(conf.getInfoList("DataFormats"));
    	
        selTypData.addActionListener(new java.awt.event.ActionListener(){
    			public void actionPerformed(java.awt.event.ActionEvent evt) {
    				selTypDataActionPerformed(evt);
    			}
			}
        );
        
        selTypData.addFocusListener(new java.awt.event.FocusAdapter() {
            	public void focusLost(java.awt.event.FocusEvent evt) {
            		selTypDataFocusLost(evt);
            	}	
        	}
    	);
       
    	nameformat = new JLabel(apps.getString("AnalysisToolView.inputformat.displayname")+":");
    	namedateini = new JLabel(apps.getString("AnalysisToolView.dateini.displayname")+":");
    	namedatefin = new JLabel(apps.getString("AnalysisToolView.datefin.displayname")+":");
    	namedatemode = new JLabel(apps.getString("AnalysisToolView.datainterval.displayname")+":");
    	nameoutputmode = new JLabel(apps.getString("AnalysisToolView.outputformat.displayname")+":");
    	nameoutputfile = new JLabel(apps.getString("AnalysisToolView.outputpath.displayname")+":");
    	nametypeformat = new JLabel(apps.getString("AnalysisToolView.typeformat.displayname")+":");
    	nametypedate = new JLabel(apps.getString("AnalysisToolView.typedate.displayname")+":");
    	
    	
    	
    	
    	JPanel panEtiqData = new JPanel();
    	panEtiqData.setLayout(new GridLayout(8,1,10,12));
    	
    	panEtiqData.add(nameformat);
    	panEtiqData.add(namedateini);
    	panEtiqData.add(namedatefin);
    	panEtiqData.add(namedatemode);
    	panEtiqData.add(nametypeformat);
    	panEtiqData.add(nametypedate);
    	panEtiqData.add(nameoutputmode);
    	panEtiqData.add(nameoutputfile);
    	
    	JPanel panFechaData = new JPanel();
    	panFechaData.setLayout(new GridLayout(8,1,10,5));
    	
    	panFechaData.add(selTypProcess);
    	panFechaData.add(dateini);
    	panFechaData.add(datefin);
    	panFechaData.add(selTypData);
    	panFechaData.add(selTypFormat);
    	panFechaData.add(checkTypDate);
    	panFechaData.add(selTypOutput);
    	panFechaData.add(outputpath);
    	
    	
    	panDataFechas.add(panEtiqData); 
    	panDataFechas.add(panFechaData);
    	
    	JPanel panFlowLayoutAviso = new JPanel();
    	panFlowLayoutAviso.setLayout(new FlowLayout());
    	
    	JLabel aviso = new JLabel(apps.getString("AnalysisToolView.avisoformato.displayname"), JLabel.CENTER);
    	
    	panFlowLayoutAviso.add(aviso);
    	
    	panFlowLayoutFechas.add(panDataFechas);
    	
    	panFechas.add(panFlowLayoutFechas);
    	panFechas.add(panFlowLayoutAviso);
    	
    	panCentral.add(panFechas);
    }
    
    private void panelEvolucion(JPanel panCentral){
    	
    	JPanel panEvol = new JPanel();
    	panEvol.setLayout(new BoxLayout(panEvol, BoxLayout.Y_AXIS));
    	panEvol.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)));
    	
    	JPanel panProgressBar = new JPanel();	
    	panProgressBar.setLayout(new FlowLayout());
    	
    	progressbar = new JProgressBar();
    	progressbar.setValue(0);
    	progressbar.setStringPainted(true);
    	progress = new JLabel("");
    	panProgressBar.add(progressbar);
    	
    	JPanel panTitEvol = new JPanel();
    	panTitEvol.setLayout(new FlowLayout());
    	panTitEvol.add(progress);
    
    	panEvol.add(panTitEvol, BorderLayout.NORTH);
    	panEvol.add(panProgressBar, BorderLayout.CENTER);
    	panCentral.add(panEvol);
    }
    
    private void panelBotonera(JPanel panCentral){
    	JPanel pan4 = new JPanel();
    	pan4.setLayout(new BoxLayout(pan4,BoxLayout.X_AXIS));
    	
    	ImageIcon icon = new ImageIcon("images/PENUSA.DLL-4030-01-16x16x16.GIF");
    	help = new JButton(icon);
    	
        help.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	try{
            		monitorhelp help1  = new monitorhelp();
            	}catch(Exception uncaughException){}
            }
        });
    	
    	JPanel pan3 = new JPanel();
    	pan3.setLayout(new FlowLayout());
    
    	//siguiente	
    	siguiente = new JButton("Continuar");
        siguiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            		if (ATVFormValidate.execute(context)){
            			//2-verifica datos del modelo.Si todo OK carga CORE
            			pan0.setEnabled(false); panFechas.setEnabled(false);
            			control.Submit(context);
            			pan0.setEnabled(true); panFechas.setEnabled(true); 
            		}
            }
        });
    	
    	//cancel
        cancel = new JButton("Cancelar");
        cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            		cancelButtonActionPerformed(evt);
            }
        });
        
        //finish
        terminar = new JButton("Terminar");
        terminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            		finishButtonActionPerformed(evt);
            }
        });
        
        pan3.add(cancel);
        pan3.add(siguiente);
        pan3.add(terminar);
        
        pan4.add(help);
        pan4.add(pan3);
        
        
        panCentral.add(pan4);
    }
    
    
    private void initGUI() throws Exception
    {
    	setLayout(new BorderLayout());
    	
    	JPanel panCentral = new JPanel();
    	panCentral.setLayout(new BoxLayout(panCentral,BoxLayout.Y_AXIS));
    	
    	//////////////////////PANEL ERRORES SERVIDOR ////////////////////////////
    	panelErrores(panCentral);
    	//////////////////////PANEL SELECCION FICHERO Y/O DIRECTORIO ////////////
    	panelDirectorio(panCentral);
    	//////////////////////PANEL DATOS FECHA ////////////////////////////////
    	panelFechas(panCentral);
    	//////////////////PANEL EVOLUCION SISTEMA////////////////////////////////
    	panelEvolucion(panCentral);
    	/////////////////////PANEL BOTONERA /////////////////////////////////////
    	panelBotonera(panCentral);
        add(panCentral);
    }
        
   
    public String getFolderpath()
    {return folderpath.getText();}
    
    public String getDateini()
    {return dateini.getText();}
    
    public String getDatefin()
    {return datefin.getText();}
    
    public void setSuccesfulMsg(String errmsg)
    {
    	if ((errmsg != null) && (errmsg!=""))
    		JOptionPane.showMessageDialog(this.parentFrame, errmsg, "aviso!", JOptionPane.INFORMATION_MESSAGE);
    } 
    
    //mensaje error de servidor
    public void setErrorMsg(String errmsg)
    {
    	if ((errmsg != null) && (errmsg!=""))
    		JOptionPane.showMessageDialog(this.parentFrame, errmsg, "error", JOptionPane.ERROR_MESSAGE);
    }
    
    //mensaje error de cliente
    public void setCliErrMsg(String errmsg){
    	JOptionPane.showMessageDialog(this.parentFrame, errmsg, "error", JOptionPane.ERROR_MESSAGE);
    }
    
    //lanza la ventana windows con los ficheros solicitados
    public void launchWindows(){
			if (System.getProperty("os.name").indexOf("Windows") != -1){
				launchfolder lf = launchfolder.getInstance();	
				lf.execute();
			}
    }
    
    //lanza la ventana windows en el path indicado
    public void launchWindows(String path){
		if (System.getProperty("os.name").indexOf("Windows") != -1){
			launchfolder lf = launchfolder.getInstance();	
			lf.execute(path);
		}
    }
    
   
    //determina el límite superior de la progressbar
    public void setNumtraces(int numtraces) {
    	this.numtraces = numtraces;
    	this.progress.setText("Recogida de informacion con exito!");
    	this.progressbar.setMinimum(0);
    	this.progressbar.setMaximum(numtraces);
    	this.progressbar.setValue(0);
    	this.progressbar.setIndeterminate(false);
    }
    
    
    public void setMsgProgressBar(String msg){
    	this.progress.setText(msg);
    }
    
    
    //determina el estado de la barra cuando ha pasado algo
    public void setKOprogressBar(){
    	this.progressbar.setValue(0);
    	this.progress.setText("Error al procesar la información!");
    	this.progressbar.setIndeterminate(false);
    }
    
    //determina el estado de la barra cuando empieza todo el proceso
    public void setIniprogressBar(){
    	this.numtraces = 0;
    	this.progressbar.setValue(0);
    	this.progress.setText("Recogiendo información de las fuentes");
    	this.progressbar.setIndeterminate(true);
    }
    
    
    //determina el estado de la barra
    public void setBeginProgressBar(){
    	this.numtraces = 0;
    	this.progress.setText("");
    	this.progressbar.setValue(0);
    	this.progressbar.setIndeterminate(false);
    }
    
    //inicializa consola de salida
    public void setIniOutputConsole() throws Exception{
    	if (conf.getInfo("visibleMonitor").equalsIgnoreCase("1")){
    		//gestion del frame de monitoreo del sistema.
    		if (mot1 == null){
    			mot1 = new monitor();
    		}else if (mot1.isClose()){
    			mot1 = null;
    			mot1 = new monitor();
    		}else{
    			mot1.eraseMessage();
    		}
    	}
    }
    
    //inicializa la monitorizacion
    public void eraseMessage() throws Exception{
    	this.progressbar.setValue(0);
    	if (conf.getInfo("visibleMonitor").equalsIgnoreCase("1")){
    		mot1.eraseMessage();
    	}
    }
    
    
    //incrementa en uno la barra de estado
    public void IncActProgressBar(){
    	int value = progressbar.getValue();	    	
    	value += 1;
        this.progressbar.setValue(value);
    }
    
    public void ChangeMessageProgressBar(String msg){
    	try{
    		if (conf.getInfo("visibleMonitor").equalsIgnoreCase("1")){
    			mot1.setMessage(msg);
    		}
    	}catch(Exception e){}
    }
    
    public void finishActions(){
    }
    
    public void searchButtonActionPerformed(ActionEvent e){
    	
       fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
       fc.addPropertyChangeListener(this);
       int returnVal = fc.showOpenDialog(this);
       if (returnVal == JFileChooser.APPROVE_OPTION){
    	 
       if (!antPath.equalsIgnoreCase("") && (valor > 0)){ 
    		folderpath.setText(antPath);
    		valor = 0;
    		antPath = "";
       }else{
    		folderpath.setText(fc.getSelectedFile().getPath());
       }
       control.changeElementFolderpath(folderpath.getText());
       }
   }
   
   //procedimiento que tiene como objetivo eliminar posibles repeticiones de directorios 
    public void propertyChange(PropertyChangeEvent e) {
        boolean update = false;
        String prop = e.getPropertyName();
        
        String oldPath = "";
        String newPath = "";
        String selectPath = "";
        
        if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(prop)){
        	
        	if (e.getOldValue() != null){
        		oldPath = e.getOldValue().toString();
        	}
        	
        	if (e.getNewValue()!= null){
        		newPath = e.getNewValue().toString();
        	}        	
        	
        	if (fc.getSelectedFile().getName()!=null){
        		selectPath = fc.getSelectedFile().getName();
        	}
        	
        	if (newPath.equalsIgnoreCase(oldPath+File.separatorChar+selectPath)){
        		log.warn("Warning! duplicate folder! change from " + e.getNewValue().toString() + " to " + e.getOldValue().toString());
        		valor++;
        		antPath =  oldPath;
        	}	
        }
    }
    
    
   public void cancelButtonActionPerformed(ActionEvent e){
	   int value = 0;
	   value = JOptionPane.showConfirmDialog(null, "Desea cancelar la aplicación? (Si/No)", "Cancel", JOptionPane.YES_NO_OPTION);
	   if (value==0)
	   {
	    	log.info("         END CASHANALYSISTOOL PROCESS         ");
		    control.Stop(context);

	   }
   }
    
   
   public void finishButtonActionPerformed(ActionEvent e){
	   int value = 0;
	   value = JOptionPane.showConfirmDialog(null, "Desea salir realmente de la aplicación? (Si/No)", "Cancel", JOptionPane.YES_NO_OPTION);
	   if (value==0)
	   {
	    	log.info("         END CASHANALYSISTOOL PROCESS         ");
		    parentFrame.dispose();
		    parentFrame = null;
		    System.gc();
		    System.exit(0);
	   }
   }
   
   
   public void modelPropertyChange(PropertyChangeEvent evt)
   {   
		String new_value = evt.getNewValue().toString();
		if (evt.getPropertyName().equals(ATVController.FOLDERPATH_PROPERTY)) {		
			folderpath.setText(new_value);
			if (!ATVFormValidate.validateBean(context,"folderpath"))
			{
				folderpath.requestFocus();
				folderpath.selectAll();
			}
        }

        else if (evt.getPropertyName().equals(ATVController.DATEINI_PROPERTY)){
        	dateini.setText(new_value);
			if (!ATVFormValidate.validateBean(context,"dateini"))
			{
				dateini.requestFocus();
				dateini.selectAll();
			}
        }
		
        else if (evt.getPropertyName().equals(ATVController.DATEFIN_PROPERTY)){
        	datefin.setText(new_value);
			if (!ATVFormValidate.validateBean(context,"datefin"))
			{
				datefin.requestFocus();
				datefin.selectAll();
			}
        }
        
        else if (evt.getPropertyName().equals(ATVController.OUTPUTPATH_PROPERTY)) { 	
        	this.outputpath.setText(new_value);
        }
		
        else if (evt.getPropertyName().equals(ATVController.INPUTFORMAT_PROPERTY)) {
        }
		
        else if (evt.getPropertyName().equals(ATVController.OUTPUTFORMAT_PROPERTY)) { 	
        }
		
        else if (evt.getPropertyName().equals(ATVController.DATEINTERVAL_PROPERTY)) { 	
        }
		
        else if (evt.getPropertyName().equals(ATVController.OUTPUTOPTIONS_PROPERTY)){
        }
		
		setBeginProgressBar();
	}

   
	private void localInitialitation() throws Exception{
		SimpleDateFormat form = new SimpleDateFormat("dd/MM/yyyy");
		folderpath.setText(conf.getInfo("DefaultFolder"));
		dateini.setText(form.format(new Date()).toString()+" 00:00:00");
		datefin.setText(form.format(new Date()).toString()+" 23:59:59");
		outputpath.setText(conf.getInfo("OutputBasicFile"));
	}
	
	
	/////////////// ACTION PERFORMED ////////////////////////////////////////////////////////////////////////////////

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private void folderpathActionPerformed(java.awt.event.ActionEvent evt){
        try {
            control.changeElementFolderpath(folderpath.getText());
        } catch (Exception e) {
        	log.error("Error processing folder data" + e.getMessage(),e);
        }
	}
	

	private void datainiActionPerformed(java.awt.event.ActionEvent evt){
        try {
        	control.changeElementDateini(dateini.getText());
        } catch (Exception e) {
        	log.error("Error processing ini data " + e.getMessage(),e);
        }
	}
	
	
	private void selTypProcessActionPerformed(java.awt.event.ActionEvent evt){
        try {
        	control.changeElementInputformat((String)selTypProcess.getSelectedItem());
        } catch (Exception e) {
        	log.error("Error processing ini data " + e.getMessage(),e);
        }
	}
	
	/**
	 * 
	 * @param evt
	 */
	private void datafinActionPerformed(java.awt.event.ActionEvent evt){
        try {
        	control.changeElementDatefin(datefin.getText());
        } catch (Exception e) {
        	log.error("Error processing ini data " + e.getMessage(),e);
        }
	}
	
	/**
	 * 
	 * @param evt
	 */
	private void outputpathActionPerformed(java.awt.event.ActionEvent evt){
	       try {
	        	control.changeElementOutputpath(outputpath.getText());
	        } catch (Exception e) {
	        	log.error("Error processing fin data " + e.getMessage(),e);
	        }
	}
	
	
	private void selTypDataActionPerformed(java.awt.event.ActionEvent evt){
			try {
	        	
				if (((String)selTypData.getSelectedItem()).equalsIgnoreCase("NO")){
					dateini.setEnabled(true);
					datefin.setEnabled(true);
				}else{
					dateini.setEnabled(false);
					datefin.setEnabled(false);
				}
				
	        } catch (Exception e) {
	        	log.error("Error processing fin data " + e.getMessage(),e);
	        }
	}
	
	private void selTypOutputActionPerformed(java.awt.event.ActionEvent evt){
	       try {
	        		if ((((String)selTypOutput.getSelectedItem()).equalsIgnoreCase("EXCEL")) || 
	        		   (((String)selTypOutput.getSelectedItem()).equalsIgnoreCase("CONSOLA"))) 
	        		   {
	        				outputpath.setEnabled(false);
	        		   }else{
	        			   	outputpath.setEnabled(true);
	        		   }
	        	control.changeElementOutputformat((String)selTypOutput.getSelectedItem());
	        	
	       }catch (Exception e) {
	        	log.error("Error processing fin data " + e.getMessage(),e);
	       }
		
	}
	
	private void selTypFormatActionPerformed(java.awt.event.ActionEvent evt){
		try{
			if (selTypFormat.isSelected()){
				control.changeElementOutputOptions("CONFIG");
			}else{
				control.changeElementOutputOptions("NO");
			}
		}catch(Exception e){
			log.error("Error processing fin data " + e.getMessage(),e);
		}	
	}
	
	
	
	private void checkTypDateActionPerformed(java.awt.event.ActionEvent evt){
		try{
			if (checkTypDate.isSelected()){
				control.changeElementOutputDateOptions("DATE");
			}else{
				control.changeElementOutputDateOptions("NO");
			}
		}catch(Exception e){
			log.error("Error processing fin data " + e.getMessage(),e);
		}	
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	//////////////////////// LOST FOCUS EVENT ////////////////////////////////////////////////////////////////////////
	
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 
	 * @param evt
	 */
	private void folderpathFocusLost(java.awt.event.FocusEvent evt) {
        try {
            control.changeElementFolderpath(folderpath.getText());
        } catch (Exception e) {
        	log.error("Error processing folder data" + e.getMessage(),e);
        }
    }
	
	
	/**
	 * 
	 * @param evt
	 */
	private void datainiFocusLost(java.awt.event.FocusEvent evt) {
        try {
        	
        	control.changeElementDateini(dateini.getText());
        } catch (Exception e) {
        	log.error("Error processing ini data " + e.getMessage(),e);
        }
 
    }
	
	/**
	 * 
	 * @param evt
	 */
	private void datafinFocusLost(java.awt.event.FocusEvent evt) {

        try {
        	control.changeElementDatefin(datefin.getText());
        } catch (Exception e) {
        	log.error("Error processing fin data " + e.getMessage(),e);
        }
 
    }
	
	/**
	 * 
	 * @param evt
	 */
	private void outputpathFocusLost(java.awt.event.FocusEvent evt){
	       try {
	        	control.changeElementOutputpath(outputpath.getText());
	        } catch (Exception e) {
	        	log.error("Error processing fin data " + e.getMessage(),e);
	        }
	}
	
	/**
	 * 
	 * @param evt
	 */
	private void selTypProcessFocusLost(java.awt.event.FocusEvent evt){
	       try {
	        	control.changeElementInputformat(outputpath.getText());
	        } catch (Exception e) {
	        	log.error("Error processing fin data " + e.getMessage(),e);
	        }	
	}
	
	
	
	private void selTypOutputFocusLost(java.awt.event.FocusEvent evt){
	       try {
	        	control.changeElementOutputformat((String)selTypOutput.getSelectedItem());
	        } catch (Exception e) {
	        	log.error("Error processing fin data " + e.getMessage(),e);
	        }
		
	}
	
	
	private void selTypDataFocusLost(java.awt.event.FocusEvent evt){
			try{
				control.changeElementDateInterval((String)selTypData.getSelectedItem());
			}catch(Exception e){
	        	log.error("Error processing fin data " + e.getMessage(),e);
			}
	}
	
	private void selTypFormatFocusLost(java.awt.event.FocusEvent evt){
			try{
				control.changeElementOutputOptions("CONFIG");
			}catch(Exception e){
				log.error("Error processing fin data " + e.getMessage(),e);
			}
	}
	
	
	private void checkTypDateFocusLost(java.awt.event.FocusEvent evt){
		try{
			if (checkTypDate.isSelected()){
				control.changeElementOutputDateOptions("DATE");
			}else{
				control.changeElementOutputDateOptions("NO");
			}
		}catch(Exception e){
			log.error("Error processing fin data " + e.getMessage(),e);
		}	
	}
	
	
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	

   //verificaciones a partir de "submit" de formulario
   private void createContext() {
       context = new ViewScrnContext();
       context.setRegScrn(this);
   }
   
   public AnalysisToolView(JFrame parentFrame, AbstractController control) throws Exception{
    	this.parentFrame = parentFrame;
    	this.control = (ATVController)control;	
    	this.numtraces = 0; 
    	
    	//se llama al fichero de configuracion
		conf = DataFileContainer.getInstance();
		conf.setConfigFile(".\\config\\cashtool.properties");
		
		
    	initGUI();
    	localInitialitation();
    	createContext();
   }
   
}
