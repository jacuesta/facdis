package com.eon.fatr.componentes;

public class FacdisFacturaPeriodosDTO {
	
	private int id_fatr;
	private String id_magnitud_cne;
	private String id_periodo_cne;
	private String fecha_ini; 
	private String fecha_fin; 
	private String fecha_factura; 
	private Double valor;
	private Double precio;
	private Double potcontratada; 
	private Double potmaxdeman; 
	private Double potafacturar; 
	private Double excpot; 
	private Double valor_periodo;
	private Double valor_facturado;
	private String penalizacionnoicp;
	
	
	public int getId_fatr() {
		return id_fatr;
	}
	public void setId_fatr(int id_fatr) {
		this.id_fatr = id_fatr;
	}
	public String getId_magnitud_cne() {
		return id_magnitud_cne;
	}
	public void setId_magnitud_cne(String id_magnitud_cne) {
		this.id_magnitud_cne = id_magnitud_cne;
	}
	public String getId_periodo_cne() {
		return id_periodo_cne;
	}
	public void setId_periodo_cne(String id_periodo_cne) {
		this.id_periodo_cne = id_periodo_cne;
	}
	public String getFecha_ini() {
		return fecha_ini;
	}
	public void setFecha_ini(String fecha_ini) {
		this.fecha_ini = fecha_ini;
	}
	public String getFecha_fin() {
		return fecha_fin;
	}
	public void setFecha_fin(String fecha_fin) {
		this.fecha_fin = fecha_fin;
	}
	public String getFecha_factura() {
		return fecha_factura;
	}
	public void setFecha_factura(String fecha_factura) {
		this.fecha_factura = fecha_factura;
	}
	public Double getValor() {
		return valor;
	}
	public void setValor(Double valor) {
		this.valor = valor;
	}
	public Double getPrecio() {
		return precio;
	}
	public void setPrecio(Double precio) {
		this.precio = precio;
	}
	
	public Double getPotcontratada() {
		return potcontratada;
	}
	public void setPotcontratada(Double potcontratada) {
		this.potcontratada = potcontratada;
	}
	
	public Double getPotmaxdeman() {
		return potmaxdeman;
	}
	public void setPotmaxdeman(Double potmaxdeman) {
		this.potmaxdeman = potmaxdeman;
	}
	
	public Double getPotafacturar() {
		return potafacturar;
	}
	public void setPotafacturar(Double potafacturar) {
		this.potafacturar = potafacturar;
	}
	
	public Double getExcpot() {
		return excpot;
	}
	public void setExcpot(Double excpot) {
		this.excpot = excpot;
	}
	public Double getValor_periodo() {
		return valor_periodo;
	}
	public void setValor_periodo(Double valor_periodo) {
		this.valor_periodo = valor_periodo;
	}
	public Double getValor_facturado() {
		return valor_facturado;
	}
	public void setValor_facturado(Double valor_facturado) {
		this.valor_facturado = valor_facturado;
	}
	public String getPenalizacionnoicp() {
		return penalizacionnoicp;
	}
	public void setPenalizacionnoicp(String penalizacionnoicp) {
		this.penalizacionnoicp = penalizacionnoicp;
	}
	
	public FacdisFacturaPeriodosDTO(int id_fatr, String id_magnitud_cne,
			String id_periodo_cne, String fecha_ini, String fecha_fin,
			String fecha_factura, double valor, double precio,
			double potcontratada, double potmaxdeman, double potafacturar,
			double excpot, double valor_periodo, double valor_facturado,
			String penalizacionnoicp) {
		super();
		this.id_fatr = id_fatr;
		this.id_magnitud_cne = id_magnitud_cne;
		this.id_periodo_cne = id_periodo_cne;
		this.fecha_ini = fecha_ini;
		this.fecha_fin = fecha_fin;
		this.fecha_factura = fecha_factura;
		this.valor = valor;
		this.precio = precio;
		this.potcontratada = potcontratada;
		this.potmaxdeman = potmaxdeman;
		this.potafacturar = potafacturar;
		this.excpot = excpot;
		this.valor_periodo = valor_periodo;
		this.valor_facturado = valor_facturado;
		this.penalizacionnoicp = penalizacionnoicp;
	}
	
	public FacdisFacturaPeriodosDTO() {
		
		this.id_fatr = 0;
		this.id_magnitud_cne = "";
		this.id_periodo_cne = "";
		this.fecha_ini = "";
		this.fecha_fin = "";
		this.fecha_factura = "";
		this.valor = null;
		this.precio = null;
		this.potcontratada = null;
		this.potmaxdeman = null;
		this.potafacturar = null;
		this.excpot = null;
		this.valor_periodo = null;
		this.valor_facturado = null;
		this.penalizacionnoicp = "";
	}
	
	
	
}
