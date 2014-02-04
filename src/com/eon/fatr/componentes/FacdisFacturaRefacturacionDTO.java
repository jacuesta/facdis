package com.eon.fatr.componentes;

public class FacdisFacturaRefacturacionDTO {
	private String idFatr;
	private String idTConcepto;
	private double importeConceptoIeIva;		
	private String observacionesIeIva;		
	private double importeConceptoIva;		
	private String observacionesIva;		
	private String refFechaDesde;		
	private String refFechaHasta;
	private double refConsumo;		
	private double importeTotal;		
	private String observaciones;		

	public String getIdFatr() {
		return idFatr;
	}
	public void setIdFatr(String idFatr) {
		this.idFatr = idFatr;
	}

	public String getIdTConcepto() {
		return idTConcepto;
	}
	public void setIdTConcepto(String idTConcepto) {
		this.idTConcepto = idTConcepto;
	}
	
	public double getImporteConceptoIeIva() {
		return importeConceptoIeIva;
	}
	public void setImporteConceptoIeIva(double importeConceptoIeIva) {
		this.importeConceptoIeIva = importeConceptoIeIva;
	}

	public String getObservacionesIeIva() {
		return observacionesIeIva;
	}
	public void setObservacionesIeIva(String observacionesIeIva) {
		this.observacionesIeIva = observacionesIeIva;
	}

	public double getImporteConceptoIva() {
		return importeConceptoIva;
	}
	public void setImporteConceptoIva(double importeConceptoIva) {
		this.importeConceptoIva = importeConceptoIva;
	}

	public String getObservacionesIva() {
		return observacionesIva;
	}
	public void setObservacionesIva(String observacionesIva) {
		this.observacionesIva = observacionesIva;
	}
	
	public String getRefFechaDesde() {
		return refFechaDesde;
	}
	public void setRefFechaDesde(String refFechaDesde) {
		this.refFechaDesde = refFechaDesde;
	}

	public String getRefFechaHasta() {
		return refFechaHasta;
	}
	public void setRefFechaHasta(String refFechaHasta) {
		this.refFechaHasta = refFechaHasta;
	}

	public double getRefConsumo() {
		return refConsumo;
	}
	public void setRefConsumo(double refConsumo) {
		this.refConsumo = refConsumo;
	}

	public double getImporteTotal() {
		return importeTotal;
	}
	public void setImporteTotal(double importeTotal) {
		this.importeTotal = importeTotal;
	}

	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	public FacdisFacturaRefacturacionDTO() {
		super();
		this.idFatr = "";
		this.idTConcepto = "";
		this.importeConceptoIeIva = 0;	
		this.observacionesIeIva = "";
		this.importeConceptoIva = 0;
		this.observacionesIva = "";
		this.refFechaDesde = "";
		this.refFechaHasta = "";
		this.refConsumo = 0;
		this.importeTotal = 0;
		this.observaciones = "";
	}

	public FacdisFacturaRefacturacionDTO(String idFatr, String idTConcepto, double importeConceptoIeIva, String observacionesIeIva, double importeConceptoIva, String observacionesIva, String refFechaDesde, String refFechaHasta, double refConsumo, double importeTotal, String observaciones) {
		super();
		this.idFatr = idFatr;
		this.idTConcepto = idTConcepto;
		this.importeConceptoIeIva = importeConceptoIeIva;	
		this.observacionesIeIva = observacionesIeIva;
		this.importeConceptoIva = importeConceptoIva;
		this.observacionesIva = observacionesIva;
		this.refFechaDesde = refFechaDesde;
		this.refFechaHasta = refFechaHasta;
		this.refConsumo = refConsumo;
		this.importeTotal = importeTotal;
		this.observaciones = observaciones;
	}
}