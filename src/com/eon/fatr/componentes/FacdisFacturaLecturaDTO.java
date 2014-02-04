package com.eon.fatr.componentes;

public class FacdisFacturaLecturaDTO {
	
	private int id_fatr;
	private String id_magnitud_cne;
	private String id_periodo_cne;
	private String fecha_ini;
	private String fecha_fin;
	private String fecha_factura;
	private String tipoaparato;
	private String marca;
	private String numserie;
	private String codigodh;
	private double ctemult;
	private String nruedasent;
	private String nruedasdec;
	private double consumocalc;
	private String id_procedeini_cne;
	private String id_procedefin_cne;
	private double lecturaini;
	private double lecturafin;
	private String id_notivoajuste_cne;
	private double ajuste_integrador;
	private String id_anomalia_cne;
	private String f_maximetro;
	private String lecturavalida;
	private String dh_Equipo;
	
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

	public String getTipoaparato() {
		return tipoaparato;
	}
	public void setTipoaparato(String tipoaparato) {
		this.tipoaparato = tipoaparato;
	}

	public String getMarca() {
		return marca;
	}
	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getNumserie() {
		return numserie;
	}
	public void setNumserie(String numserie) {
		this.numserie = numserie;
	}

	public String getCodigodh() {
		return codigodh;
	}
	public void setCodigodh(String codigodh) {
		this.codigodh = codigodh;
	}

	public double getCtemult() {
		return ctemult;
	}
	public void setCtemult(double ctemult) {
		this.ctemult = ctemult;
	}

	public String getNruedasent() {
		return nruedasent;
	}
	public void setNruedasent(String nruedasent) {
		this.nruedasent = nruedasent;
	}

	public String getNruedasdec() {
		return nruedasdec;
	}
	public void setNruedasdec(String nruedasdec) {
		this.nruedasdec = nruedasdec;
	}

	public double getConsumocalc() {
		return consumocalc;
	}
	public void setConsumocalc(double consumocalc) {
		this.consumocalc = consumocalc;
	}

	public String getId_procedeini_cne() {
		return id_procedeini_cne;
	}
	public void setId_procedeini_cne(String id_procedeini_cne) {
		this.id_procedeini_cne = id_procedeini_cne;
	}

	public String getId_procedefin_cne() {
		return id_procedefin_cne;
	}
	public void setId_procedefin_cne(String id_procedefin_cne) {
		this.id_procedefin_cne = id_procedefin_cne;
	}

	public double getLecturaini() {
		return lecturaini;
	}
	public void setLecturaini(double lecturaini) {
		this.lecturaini = lecturaini;
	}

	public double getLecturafin() {
		return lecturafin;
	}
	public void setLecturafin(double lecturafin) {
		this.lecturafin = lecturafin;
	}

	public String getId_notivoajuste_cne() {
		return id_notivoajuste_cne;
	}
	public void setId_notivoajuste_cne(String id_notivoajuste_cne) {
		this.id_notivoajuste_cne = id_notivoajuste_cne;
	}

	public double getAjuste_integrador() {
		return ajuste_integrador;
	}
	public void setAjuste_integrador(double ajuste_integrador) {
		this.ajuste_integrador = ajuste_integrador;
	}

	public String getId_anomalia_cne() {
		return id_anomalia_cne;
	}
	public void setId_anomalia_cne(String id_anomalia_cne) {
		this.id_anomalia_cne = id_anomalia_cne;
	}

	public String getF_maximetro() {
		return f_maximetro;
	}
	public void setF_maximetro(String f_maximetro) {
		this.f_maximetro = f_maximetro;
	}

	public String getLecturavalida() {
		return lecturavalida;
	}
	public void setLecturavalida(String lecturavalida) {
		this.lecturavalida = lecturavalida;
	}

	public String getDh_Equipo() {
		return dh_Equipo;
	}
	public void setDh_Equipo(String dh_Equipo) {
		this.dh_Equipo = dh_Equipo;
	}

	public FacdisFacturaLecturaDTO(int id_fatr, String id_magnitud_cne,
			String id_periodo_cne, String fecha_ini, String fecha_fin,
			String fecha_factura, String tipoaparato, String marca,
			String numserie, String codigodh, double ctemult,
			String nruedasent, String nruedasdec, double consumocalc,
			String id_procedeini_cne, String id_procedefin_cne,
			double lecturaini, double lecturafin, String id_notivoajuste_cne,
			double ajuste_integrador, String id_anomalia_cne,
			String f_maximetro, String lecturavalida, String dh_Equipo) {
		super();
		this.id_fatr = id_fatr;
		this.id_magnitud_cne = id_magnitud_cne;
		this.id_periodo_cne = id_periodo_cne;
		this.fecha_ini = fecha_ini;
		this.fecha_fin = fecha_fin;
		this.fecha_factura = fecha_factura;
		this.tipoaparato = tipoaparato;
		this.marca = marca;
		this.numserie = numserie;
		this.codigodh = codigodh;
		this.ctemult = ctemult;
		this.nruedasent = nruedasent;
		this.nruedasdec = nruedasdec;
		this.consumocalc = consumocalc;
		this.id_procedeini_cne = id_procedeini_cne;
		this.id_procedefin_cne = id_procedefin_cne;
		this.lecturaini = lecturaini;
		this.lecturafin = lecturafin;
		this.id_notivoajuste_cne = id_notivoajuste_cne;
		this.ajuste_integrador = ajuste_integrador;
		this.id_anomalia_cne = id_anomalia_cne;
		this.f_maximetro = f_maximetro;
		this.lecturavalida = lecturavalida;
		this.dh_Equipo = dh_Equipo;
	}

	public FacdisFacturaLecturaDTO() {
		super();
		this.id_fatr = 0;
		this.id_magnitud_cne = null;
		this.id_periodo_cne = null;
		this.fecha_ini = null;
		this.fecha_fin = null;
		this.fecha_factura = null;
		this.tipoaparato = null;
		this.marca = null;
		this.numserie = null;
		this.codigodh = null;
		this.ctemult = 0;
		this.nruedasent = null;
		this.nruedasdec = null;
		this.consumocalc = 0;
		this.id_procedeini_cne = null;
		this.id_procedefin_cne = null;
		this.lecturaini = 0;
		this.lecturafin = 0;
		this.id_notivoajuste_cne = null;
		this.ajuste_integrador = 0;
		this.id_anomalia_cne = null;
		this.f_maximetro = null;
		this.lecturavalida = null;
		this.dh_Equipo = null;
	}
}