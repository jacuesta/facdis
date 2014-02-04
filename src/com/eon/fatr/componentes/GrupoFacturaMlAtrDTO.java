package com.eon.fatr.componentes;

public class GrupoFacturaMlAtrDTO {

	private String idGrupoFatr;
	private String codReeEmpEmisora;
	private String codReeEmpDestino;
	private String codProceso;
	private String codPaso;
	private String codSolicitud;
	private String secuencialSolicitud;
	private String codigo;
	private String fechaSolicitud;
	private String version;
	private double importeTotal;
	private double saldoTotalFact;
	private double saldoTotalCobro;
	private double totalRecibos;
	private String tipoMoneda;
	private String fechaValor;
	private String fechaLimitePago;
	private String banco;
	private String sucursal;
	private String dc;
	private String cuenta;
	private String idRemesa;
	
	public String getBanco() {
		return banco;
	}

	public void setBanco(String banco) {
		this.banco = banco;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getCodPaso() {
		return codPaso;
	}

	public void setCodPaso(String codPaso) {
		this.codPaso = codPaso;
	}

	public String getCodProceso() {
		return codProceso;
	}

	public void setCodProceso(String codProceso) {
		this.codProceso = codProceso;
	}

	public String getCodReeEmpDestino() {
		return codReeEmpDestino;
	}

	public void setCodReeEmpDestino(String codReeEmpDestino) {
		this.codReeEmpDestino = codReeEmpDestino;
	}

	public String getCodReeEmpEmisora() {
		return codReeEmpEmisora;
	}

	public void setCodReeEmpEmisora(String codReeEmpEmisora) {
		this.codReeEmpEmisora = codReeEmpEmisora;
	}

	public String getCodSolicitud() {
		return codSolicitud;
	}

	public void setCodSolicitud(String codSolicitud) {
		this.codSolicitud = codSolicitud;
	}

	public String getCuenta() {
		return cuenta;
	}

	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}

	public String getDc() {
		return dc;
	}

	public void setDc(String dc) {
		this.dc = dc;
	}

	public String getFechaLimitePago() {
		return fechaLimitePago;
	}

	public void setFechaLimitePago(String fechaLimitePago) {
		this.fechaLimitePago = fechaLimitePago;
	}

	public String getFechaSolicitud() {
		return fechaSolicitud;
	}

	public void setFechaSolicitud(String fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}

	public String getFechaValor() {
		return fechaValor;
	}

	public void setFechaValor(String fechaValor) {
		this.fechaValor = fechaValor;
	}

	public String getIdGrupoFatr() {
		return idGrupoFatr;
	}

	public void setIdGrupoFatr(String idGrupoFatr) {
		this.idGrupoFatr = idGrupoFatr;
	}

	public String getIdRemesa() {
		return idRemesa;
	}

	public void setIdRemesa(String idRemesa) {
		this.idRemesa = idRemesa;
	}

	public double getImporteTotal() {
		return importeTotal;
	}

	public void setImporteTotal(double importeTotal) {
		this.importeTotal = importeTotal;
	}

	public double getSaldoTotalCobro() {
		return saldoTotalCobro;
	}

	public void setSaldoTotalCobro(double saldoTotalCobro) {
		this.saldoTotalCobro = saldoTotalCobro;
	}

	public double getSaldoTotalFact() {
		return saldoTotalFact;
	}

	public void setSaldoTotalFact(double saldoTotalFact) {
		this.saldoTotalFact = saldoTotalFact;
	}

	public String getSecuencialSolicitud() {
		return secuencialSolicitud;
	}

	public void setSecuencialSolicitud(String secuencialSolicitud) {
		this.secuencialSolicitud = secuencialSolicitud;
	}

	public String getSucursal() {
		return sucursal;
	}

	public void setSucursal(String sucursal) {
		this.sucursal = sucursal;
	}

	public String getTipoMoneda() {
		return tipoMoneda;
	}

	public void setTipoMoneda(String tipoMoneda) {
		this.tipoMoneda = tipoMoneda;
	}

	public double getTotalRecibos() {
		return totalRecibos;
	}

	public void setTotalRecibos(double totalRecibos) {
		this.totalRecibos = totalRecibos;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public GrupoFacturaMlAtrDTO() {
		super();
		this.idGrupoFatr = "";
		this.codReeEmpEmisora = "";
		this.codReeEmpDestino = "";
		this.codProceso = "";
		this.codPaso = "";
		this.codSolicitud = "";
		this.secuencialSolicitud = "";
		this.codigo = "";
		this.fechaSolicitud = "";
		this.version = "";
		this.importeTotal = 0;
		this.saldoTotalFact = 0;
		this.saldoTotalCobro = 0;
		this.totalRecibos = 0;
		this.tipoMoneda = "";
		this.fechaValor = "";
		this.fechaLimitePago = "";
		this.banco = "";
		this.sucursal = "";
		this.dc = "";
		this.cuenta = "";
		this.idRemesa = "";
	}

	public GrupoFacturaMlAtrDTO(String idGrupoFatr, String codReeEmpEmisora, String codReeEmpDestino, String codProceso, String codPaso, String codSolicitud, String secuencialSolicitud, String codigo, String fechaSolicitud, String version, double importeTotal, double saldoTotalFact, double saldoTotalCobro, double totalRecibos, String tipoMoneda, String fechaValor, String fechaLimitePago, String banco, String sucursal, String dc, String cuenta, String idRemesa) {
		super();
		this.idGrupoFatr = idGrupoFatr;
		this.codReeEmpEmisora = codReeEmpEmisora;
		this.codReeEmpDestino = codReeEmpDestino;
		this.codProceso = codProceso;
		this.codPaso = codPaso;
		this.codSolicitud = codSolicitud;
		this.secuencialSolicitud = secuencialSolicitud;
		this.codigo = codigo;
		this.fechaSolicitud = fechaSolicitud;
		this.version = version;
		this.importeTotal = importeTotal;
		this.saldoTotalFact = saldoTotalFact;
		this.saldoTotalCobro = saldoTotalCobro;
		this.totalRecibos = totalRecibos;
		this.tipoMoneda = tipoMoneda;
		this.fechaValor = fechaValor;
		this.fechaLimitePago = fechaLimitePago;
		this.banco = banco;
		this.sucursal = sucursal;
		this.dc = dc;
		this.cuenta = cuenta;
		this.idRemesa = idRemesa;
	}
}