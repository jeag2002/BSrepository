package com.bs.analysis.cashanalysistool.output.xml;

import java.util.Vector;

public class SamplePatternOut{
	
	private String id;
	private String type;
	private Vector detalles;
	private Vector funcionalidades;
	
	public SamplePatternOut(){
		this.id = "";
		this.type = "";
		detalles = new Vector();
		funcionalidades = new Vector();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Vector getDetalles() {
		return detalles;
	}

	public void setDetalles(Vector detalles) {
		this.detalles = detalles;
	}

	public Vector getFuncionalidades() {
		return funcionalidades;
	}

	public void setFuncionalidades(Vector funcionalidades) {
		this.funcionalidades = funcionalidades;
	}
	
	public void addDetalle(String detalle){
		detalles.add(detalle);
	}
	
	public void addFunc(String func){	
		funcionalidades.add(func);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
