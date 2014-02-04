package com.eon.fatr.componentes;

public class FacdisFacturaConceptoDTO {

	private int idFatr;
	private String idConceptoCne;
	private double unidades;
	private double precio;
	private double importe;
	private Double pctIva;
	private Double pctIe;
	
	

	public String getIdConceptoCne() {
		return idConceptoCne;
	}

	public void setIdConceptoCne(String idConceptoCne) {
		this.idConceptoCne = idConceptoCne;
	}

	
	public int getIdFatr() {
		return idFatr;
	}

	public void setIdFatr(int idFatr) {
		this.idFatr = idFatr;
	}

	public double getImporte() {
		return importe;
	}

	public void setImporte(double importe) {
		this.importe = importe;
	}

	

	public Double getPctIva() {
		return pctIva;
	}

	public void setPctIva(Double pctIva) {
		this.pctIva = pctIva;
	}

	public Double getPctIe() {
		return pctIe;
	}

	public void setPctIe(Double pctIe) {
		this.pctIe = pctIe;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public double getUnidades() {
		return unidades;
	}

	public void setUnidades(double unidades) {
		this.unidades = unidades;
	}

	public FacdisFacturaConceptoDTO() {
		super();
		this.idFatr = 0;
		this.idConceptoCne = "";
		this.unidades = 0;
		this.precio = 0;
		this.importe = 0;
		this.pctIva = null;
		this.pctIe = null;
	}

}