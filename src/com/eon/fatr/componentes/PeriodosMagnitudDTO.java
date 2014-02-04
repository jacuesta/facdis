package com.eon.fatr.componentes;

public class PeriodosMagnitudDTO {

	public String idFactura;
	public String magnitud;
	public String idTipoPeriodo;
	public String valor;
	public String potContratada;
	public String potMaxDemandada;
	public String excesoPotencia;
	public String nemonico;
	
	
	public PeriodosMagnitudDTO(String idFactura, String magnitud, String idTipoPeriodo, String valor, String potContratada, String potMaxDemandada, String excesoPotencia, String nemonico) {
		this.idFactura = idFactura;
		this.magnitud = magnitud;
		this.idTipoPeriodo = idTipoPeriodo;
		this.valor = valor;
		this.potContratada = potContratada;
		this.potMaxDemandada = potMaxDemandada;
		this.excesoPotencia = excesoPotencia;
		this.nemonico = nemonico;
	}
	public PeriodosMagnitudDTO() {
		this.idFactura = "";
		this.magnitud = "";
		this.idTipoPeriodo = "";
		this.valor = "";
		this.potContratada = "";
		this.potMaxDemandada = "";
		this.excesoPotencia = "";
		this.nemonico = "";
	}
	public String getExcesoPotencia() {
		return excesoPotencia;
	}
	public void setExcesoPotencia(String excesoPotencia) {
		this.excesoPotencia = excesoPotencia;
	}
	public String getIdFactura() {
		return idFactura;
	}
	public void setIdFactura(String idFactura) {
		this.idFactura = idFactura;
	}
	public String getIdTipoPeriodo() {
		return idTipoPeriodo;
	}
	public void setIdTipoPeriodo(String idTipoPeriodo) {
		this.idTipoPeriodo = idTipoPeriodo;
	}
	public String getMagnitud() {
		return magnitud;
	}
	public void setMagnitud(String magnitud) {
		this.magnitud = magnitud;
	}
	public String getNemonico() {
		return nemonico;
	}
	public void setNemonico(String nemonico) {
		this.nemonico = nemonico;
	}
	public String getPotContratada() {
		return potContratada;
	}
	public void setPotContratada(String potContratada) {
		this.potContratada = potContratada;
	}
	public String getPotMaxDemandada() {
		return potMaxDemandada;
	}
	public void setPotMaxDemandada(String potMaxDemandada) {
		this.potMaxDemandada = potMaxDemandada;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	 
	
	
	
	
}
