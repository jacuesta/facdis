package com.eon.fatr.componentes;

public class FacdisTarifaMagnitudDTO {
	
	private String id_tarifa_cne;
	private String id_magnitud_cne;
	private int num_periodos_xml;
	private int periodo;
	private int posicion;
	
	public String getId_tarifa_cne() {
		return id_tarifa_cne;
	}
	public void setId_tarifa_cne(String id_tarifa_cne) {
		this.id_tarifa_cne = id_tarifa_cne;
	}
	public String getId_magnitud_cne() {
		return id_magnitud_cne;
	}
	public void setId_magnitud_cne(String id_magnitud_cne) {
		this.id_magnitud_cne = id_magnitud_cne;
	}
	public int getNum_periodos_xml() {
		return num_periodos_xml;
	}
	public void setNum_periodos_xml(int num_periodos_xml) {
		this.num_periodos_xml = num_periodos_xml;
	}
	public int getPeriodo() {
		return periodo;
	}
	public void setPeriodo(int periodo) {
		this.periodo = periodo;
	}
	public int getPosicion() {
		return posicion;
	}
	public void setPosicion(int posicion) {
		this.posicion = posicion;
	}
}