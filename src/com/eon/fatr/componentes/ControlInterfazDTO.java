package com.eon.fatr.componentes;

public class ControlInterfazDTO {

	
	public String nombreInterfaz;
	public String fecha;
	public String indEnvResp;
	public String esquema;
	
	
	
	public String getEsquema() {
		return esquema;
	}



	public void setEsquema(String esquema) {
		this.esquema = esquema;
	}



	public String getFecha() {
		return fecha;
	}



	public void setFecha(String fecha) {
		this.fecha = fecha;
	}



	public String getIndEnvResp() {
		return indEnvResp;
	}



	public void setIndEnvResp(String indEnvResp) {
		this.indEnvResp = indEnvResp;
	}



	public String getNombreInterfaz() {
		return nombreInterfaz;
	}



	public void setNombreInterfaz(String nombreInterfaz) {
		this.nombreInterfaz = nombreInterfaz;
	}



	public ControlInterfazDTO(String nombreInterfaz, String fecha, String indEnvResp, String esquema) {
		super();
		// TODO Auto-generated constructor stub
		this.nombreInterfaz = nombreInterfaz;
		this.fecha = fecha;
		this.indEnvResp = indEnvResp;
		this.esquema = esquema;
	}
	
	
}
