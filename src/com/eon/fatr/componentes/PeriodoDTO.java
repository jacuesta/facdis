package com.eon.fatr.componentes;

public class PeriodoDTO {

	private String idTipoPeriodo;
	private String nemonico;

	public PeriodoDTO(String idTipoPeriodo, String nemonico) {
		super();
		this.idTipoPeriodo = idTipoPeriodo;
		this.nemonico = nemonico;
	}
	public PeriodoDTO() {
		this.idTipoPeriodo		= "";
		this.nemonico			= "";
	}

	public String getIdTipoPeriodo() {
		return idTipoPeriodo;
	}
	public void setIdTipoPeriodo(String idTipoPeriodo) {
		this.idTipoPeriodo = idTipoPeriodo;
	}

	public String getNemonico() {
		return nemonico;
	}
	public void setNemonico(String nemonico) {
		this.nemonico = nemonico;
	}
}