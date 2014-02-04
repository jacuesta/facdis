package com.eon.fatr.componentes;

public class FacdisFacturaDTO {
	
	private int id_fatr;
	private String id_distribuidora_cne;
	private String desc_distribuidora_cne;
	private String id_comercializadora_cne;
	private String desc_comercializadora_cne;
	private String cups;
	private String contrato;
	private String num_factura;
	private String id_tipofactrec_cne;
	private String desc_tipofactrec_cne;
	private String num_factura_rectificada;
	private String ticket_energetico;
	private String fecha_factura;
	private String fecha_grabacion;
	private String dir_suministro;
	private int municipio;
	private String documento;
	private String observaciones;
	private String id_tipofactper_cne;
	private String desc_tipofactper_cne;
	private String id_tipofactura_cne;
	private String desc_tipofactura_cne;
	private String id_tipo_identif_cne;
	private String cif;
	private String fecha_boe;
	private String modo_facturacion;
	private String validado;
	private String fecha_validado;
	private String fecha_envio_portal;
	private String documento_backup;
	private String fecha_firma;
	private String fecha_sappi;
	private String cod_solicitud;
	private String fecha_solicitud;
	private String tipo_moneda;
	private String fecha_valor;
	private String fecha_limite_pago;
	private String id_tarifa_cne;
	private String desc_tarifa_cne;
	private String fecha_ini;
	private String fecha_fin;
	private String cupspm;
	private Double num_meses;
	//private Integer num_meses;
	private String ind_at_med_bt;
	private Integer val_lec;
	private Integer val_atr_lec;
	private String ind_envio_csa;
	private String fecha_envio_csa;
	private Integer modo_control_potencia;
	private float total;
	private float saldo_factura;
	private float saldo_cobro;
	private float base_ie;
	private float coef_ie;
	private float pct_ie;
	private float ie;
	private float importe_alquileres;
	private float importe_intereses;
	private float base_ivaigic_n;
	private float pct_ivaigic_n;
	private float ivaigic_n;
	private float base_ivaigic_r;
	private float pct_ivaigic_r;
	private float ivaigic_r;
	private float base_ivaigic_n2;
	private float pct_ivaigic_n2;
	private float ivaigic_n2;
	private float base_ivaigic_r2;
	private float pct_ivaigic_r2;
	private float ivaigic_r2;
	private boolean existeXML;
	private String firmar;
	private Integer at;
	private String tipoOrigen;
	
	public String getDesc_tarifa_cne() {
		return desc_tarifa_cne;
	}
	public void setDesc_tarifa_cne(String desc_tarifa_cne) {
		this.desc_tarifa_cne = desc_tarifa_cne;
	}
	public String getTipoOrigen() {
		return tipoOrigen;
	}
	public void setTipoOrigen(String tipoOrigen) {
		this.tipoOrigen = tipoOrigen;
	}
	public Integer getAt() {
		return at;
	}
	public void setAt(Integer at) {
		this.at = at;
	}
	public String getDesc_tipofactper_cne() {
		return desc_tipofactper_cne;
	}
	public void setDesc_tipofactper_cne(String desc_tipofactper_cne) {
		this.desc_tipofactper_cne = desc_tipofactper_cne;
	}
	public String getDesc_distribuidora_cne() {
		return desc_distribuidora_cne;
	}
	public void setDesc_distribuidora_cne(String desc_distribuidora_cne) {
		this.desc_distribuidora_cne = desc_distribuidora_cne;
	}
	public String getDesc_comercializadora_cne() {
		return desc_comercializadora_cne;
	}
	public void setDesc_comercializadora_cne(String desc_comercializadora_cne) {
		this.desc_comercializadora_cne = desc_comercializadora_cne;
	}
	public String getDesc_tipofactrec_cne() {
		return desc_tipofactrec_cne;
	}
	public void setDesc_tipofactrec_cne(String desc_tipofactrec_cne) {
		this.desc_tipofactrec_cne = desc_tipofactrec_cne;
	}
	public String getDesc_tipofactura_cne() {
		return desc_tipofactura_cne;
	}
	public void setDesc_tipofactura_cne(String desc_tipofactura_cne) {
		this.desc_tipofactura_cne = desc_tipofactura_cne;
	}
	public int getId_fatr() {
		return id_fatr;
	}
	public void setId_fatr(int id_fatr) {
		this.id_fatr = id_fatr;
	}
	
	public String getId_distribuidora_cne() {
		return id_distribuidora_cne;
	}
	public void setId_distribuidora_cne(String id_distribuidora_cne) {
		this.id_distribuidora_cne = id_distribuidora_cne;
	}

	public String getId_comercializadora_cne() {
		return id_comercializadora_cne;
	}
	public void setId_comercializadora_cne(String id_comercializadora_cne) {
		this.id_comercializadora_cne = id_comercializadora_cne;
	}

	public String getCups() {
		return cups;
	}
	public void setCups(String cups) {
		this.cups = cups;
	}
	
	public float getPct_ivaigic_r() {
		return pct_ivaigic_r;
	}
	public void setPct_ivaigic_r(float pct_ivaigic_r) {
		this.pct_ivaigic_r = pct_ivaigic_r;
	}

	public float getIvaigic_r() {
		return ivaigic_r;
	}
	public void setIvaigic_r(float ivaigic_r) {
		this.ivaigic_r = ivaigic_r;
	}

	public float getBase_ivaigic_n2() {
		return base_ivaigic_n2;
	}
	public void setBase_ivaigic_n2(float base_ivaigic_n2) {
		this.base_ivaigic_n2 = base_ivaigic_n2;
	}
	
	public float getPct_ivaigic_n2() {
		return pct_ivaigic_n2;
	}
	public void setPct_ivaigic_n2(float pct_ivaigic_n2) {
		this.pct_ivaigic_n2 = pct_ivaigic_n2;
	}
	
	public float getIvaigic_n2() {
		return ivaigic_n2;
	}
	public void setIvaigic_n2(float ivaigic_n2) {
		this.ivaigic_n2 = ivaigic_n2;
	}
	
	public float getBase_ivaigic_r2() {
		return base_ivaigic_r2;
	}
	public void setBase_ivaigic_r2(float base_ivaigic_r2) {
		this.base_ivaigic_r2 = base_ivaigic_r2;
	}
	
	public float getPct_ivaigic_r2() {
		return pct_ivaigic_r2;
	}
	public void setPct_ivaigic_r2(float pct_ivaigic_r2) {
		this.pct_ivaigic_r2 = pct_ivaigic_r2;
	}
	
	public float getIvaigic_r2() {
		return ivaigic_r2;
	}
	public void setIvaigic_r2(float ivaigic_r2) {
		this.ivaigic_r2 = ivaigic_r2;
	}
	
	public float getBase_ivaigic_r() {
		return base_ivaigic_r;
	}
	public void setBase_ivaigic_r(float base_ivaigic_r) {
		this.base_ivaigic_r = base_ivaigic_r;
	}
	
	public float getIvaigic_n() {
		return ivaigic_n;
	}
	public void setIvaigic_n(float ivaigic_n) {
		this.ivaigic_n = ivaigic_n;
	}
	
	public float getPct_ivaigic_n() {
		return pct_ivaigic_n;
	}
	public void setPct_ivaigic_n(float pct_ivaigic_n) {
		this.pct_ivaigic_n = pct_ivaigic_n;
	}
	
	public float getBase_ivaigic_n() {
		return base_ivaigic_n;
	}
	public void setBase_ivaigic_n(float base_ivaigic_n) {
		this.base_ivaigic_n = base_ivaigic_n;
	}
	
	public float getImporte_intereses() {
		return importe_intereses;
	}
	public void setImporte_intereses(float importe_intereses) {
		this.importe_intereses = importe_intereses;
	}
	
	public float getImporte_alquileres() {
		return importe_alquileres;
	}
	public void setImporte_alquileres(float importe_alquileres) {
		this.importe_alquileres = importe_alquileres;
	}
	
	public float getIe() {
		return ie;
	}
	public void setIe(float ie) {
		this.ie = ie;
	}
	
	public float getPct_ie() {
		return pct_ie;
	}
	public void setPct_ie(float pct_ie) {
		this.pct_ie = pct_ie;
	}
	
	public float getCoef_ie() {
		return coef_ie;
	}
	public void setCoef_ie(float coef_ie) {
		this.coef_ie = coef_ie;
	}
	
	public float getBase_ie() {
		return base_ie;
	}
	public void setBase_ie(float base_ie) {
		this.base_ie = base_ie;
	}
	
	public float getSaldo_cobro() {
		return saldo_cobro;
	}
	public void setSaldo_cobro(float saldo_cobro) {
		this.saldo_cobro = saldo_cobro;
	}
	
	public float getSaldo_factura() {
		return saldo_factura;
	}
	public void setSaldo_factura(float saldo_factura) {
		this.saldo_factura = saldo_factura;
	}
	
	public float getTotal() {
		return total;
	}
	public void setTotal(float total) {
		this.total = total;
	}

	
	public Double getNum_meses() {
		return num_meses;
	}
	public void setNum_meses(Double num_meses) {
		this.num_meses = num_meses;
	}
	public String getContrato() {
		return contrato;
	}
	public void setContrato(String contrato) {
		this.contrato = contrato;
	}

	public String getNum_factura() {
		return num_factura;
	}
	public void setNum_factura(String num_factura) {
		this.num_factura = num_factura;
	}

	public String getId_tipofactrec_cne() {
		return id_tipofactrec_cne;
	}
	public void setId_tipofactrec_cne(String id_tipofactrec_cne) {
		this.id_tipofactrec_cne = id_tipofactrec_cne;
	}

	public String getNum_factura_rectificada() {
		return num_factura_rectificada;
	}
	public void setNum_factura_rectificada(String num_factura_rectificada) {
		this.num_factura_rectificada = num_factura_rectificada;
	}

	public String getTicket_energetico() {
		return ticket_energetico;
	}
	public void setTicket_energetico(String ticket_energetico) {
		this.ticket_energetico = ticket_energetico;
	}

	public String getFecha_factura() {
		return fecha_factura;
	}
	public void setFecha_factura(String fecha_factura) {
		this.fecha_factura = fecha_factura;
	}

	public String getFecha_grabacion() {
		return fecha_grabacion;
	}
	public void setFecha_grabacion(String fecha_grabacion) {
		this.fecha_grabacion = fecha_grabacion;
	}

	public String getDir_suministro() {
		return dir_suministro;
	}
	public void setDir_suministro(String dir_suministro) {
		this.dir_suministro = dir_suministro;
	}

	public int getMunicipio() {
		return municipio;
	}
	public void setMunicipio(int municipio) {
		this.municipio = municipio;
	}

	public String getDocumento() {
		return documento;
	}
	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getId_tipofactper_cne() {
		return id_tipofactper_cne;
	}
	public void setId_tipofactper_cne(String id_tipofactper_cne) {
		this.id_tipofactper_cne = id_tipofactper_cne;
	}

	public String getId_tipofactura_cne() {
		return id_tipofactura_cne;
	}
	public void setId_tipofactura_cne(String id_tipofactura_cne) {
		this.id_tipofactura_cne = id_tipofactura_cne;
	}

	public String getId_tipo_identif_cne() {
		return id_tipo_identif_cne;
	}
	public void setId_tipo_identif_cne(String id_tipo_identif_cne) {
		this.id_tipo_identif_cne = id_tipo_identif_cne;
	}

	public String getCif() {
		return cif;
	}
	public void setCif(String cif) {
		this.cif = cif;
	}

	public String getFecha_boe() {
		return fecha_boe;
	}
	public void setFecha_boe(String fecha_boe) {
		this.fecha_boe = fecha_boe;
	}

	public String getModo_facturacion() {
		return modo_facturacion;
	}
	public void setModo_facturacion(String modo_facturacion) {
		this.modo_facturacion = modo_facturacion;
	}

	public String getValidado() {
		return validado;
	}
	public void setValidado(String validado) {
		this.validado = validado;
	}

	public String getFecha_validado() {
		return fecha_validado;
	}
	public void setFecha_validado(String fecha_validado) {
		this.fecha_validado = fecha_validado;
	}

	public String getFecha_envio_portal() {
		return fecha_envio_portal;
	}
	public void setFecha_envio_portal(String fecha_envio_portal) {
		this.fecha_envio_portal = fecha_envio_portal;
	}

	public String getDocumento_backup() {
		return documento_backup;
	}
	public void setDocumento_backup(String documento_backup) {
		this.documento_backup = documento_backup;
	}

	public String getFecha_firma() {
		return fecha_firma;
	}
	public void setFecha_firma(String fecha_firma) {
		this.fecha_firma = fecha_firma;
	}

	public String getFecha_sappi() {
		return fecha_sappi;
	}
	public void setFecha_sappi(String fecha_sappi) {
		this.fecha_sappi = fecha_sappi;
	}

	public String getCod_solicitud() {
		return cod_solicitud;
	}
	public void setCod_solicitud(String cod_solicitud) {
		this.cod_solicitud = cod_solicitud;
	}

	public String getFecha_solicitud() {
		return fecha_solicitud;
	}
	public void setFecha_solicitud( String fecha_solicitud) {
		this.fecha_solicitud = fecha_solicitud;
	}

	public String getTipo_moneda() {
		return tipo_moneda;
	}
	public void setTipo_moneda(String tipo_moneda) {
		this.tipo_moneda = tipo_moneda;
	}

	public String getFecha_valor() {
		return fecha_valor;
	}
	public void setFecha_valor(String fecha_valor) {
		this.fecha_valor = fecha_valor;
	}

	public String getFecha_limite_pago() {
		return fecha_limite_pago;
	}
	public void setFecha_limite_pago(String fecha_limite_pago) {
		this.fecha_limite_pago = fecha_limite_pago;
	}

	public String getId_tarifa_cne() {
		return id_tarifa_cne;
	}
	public void setId_tarifa_cne(String id_tarifa_cne) {
		this.id_tarifa_cne = id_tarifa_cne;
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

	public String getCupspm() {
		return cupspm;
	}
	public void setCupspm(String cupspm) {
		this.cupspm = cupspm;
	}
	
	public String getInd_at_med_bt() {
		return ind_at_med_bt;
	}
	public void setInd_at_med_bt(String ind_at_med_bt) {
		this.ind_at_med_bt = ind_at_med_bt;
	}
	public Integer getVal_lec() {
		return val_lec;
	}
	public void setVal_lec(Integer val_lec) {
		this.val_lec = val_lec;
	}
	public Integer getVal_atr_lec() {
		return val_atr_lec;
	}
	public void setVal_atr_lec(Integer val_atr_lec) {
		this.val_atr_lec = val_atr_lec;
	}
	public String getInd_envio_csa() {
		return ind_envio_csa;
	}
	public void setInd_envio_csa(String ind_envio_csa) {
		this.ind_envio_csa = ind_envio_csa;
	}

	public String getFecha_envio_csa() {
		return fecha_envio_csa;
	}
	public void setFecha_envio_csa(String fecha_envio_csa) {
		this.fecha_envio_csa = fecha_envio_csa;
	}

	public Integer getModo_control_potencia() {
		return modo_control_potencia;
	}
	public void setModo_control_potencia(Integer modo_control_potencia) {
		this.modo_control_potencia = modo_control_potencia;
	}
	
	public boolean isExisteXML() {
		return existeXML;
	}
	public void setExisteXML(boolean existeXML) {
		this.existeXML = existeXML;
	}
	
	public String getFirmar() {
		return firmar;
	}
	public void setFirmar(String firmar) {
		this.firmar = firmar;
	}
	
	
	public FacdisFacturaDTO(int id_fatr, String id_distribuidora_cne,
			String desc_distribuidora_cne, String id_comercializadora_cne,
			String desc_comercializadora_cne, String cups, String contrato,
			String num_factura, String id_tipofactrec_cne,
			String desc_tipofactrec_cne, String num_factura_rectificada,
			String ticket_energetico, String fecha_factura,
			String fecha_grabacion, String dir_suministro, int municipio,
			String documento, String observaciones, String id_tipofactper_cne,
			String desc_tipofactper_cne, String id_tipofactura_cne,
			String desc_tipofactura_cne, String id_tipo_identif_cne,
			String cif, String fecha_boe, String modo_facturacion,
			String validado, String fecha_validado, String fecha_envio_portal,
			String documento_backup, String fecha_firma, String fecha_sappi,
			String cod_solicitud, String fecha_solicitud, String tipo_moneda,
			String fecha_valor, String fecha_limite_pago, String id_tarifa_cne,
			String desc_tarifa_cne, String fecha_ini, String fecha_fin,
			String cupspm, Double num_meses, String ind_at_med_bt,
			Integer val_lec, Integer val_atr_lec, String ind_envio_csa,
			String fecha_envio_csa, Integer modo_control_potencia, float total,
			float saldo_factura, float saldo_cobro, float base_ie,
			float coef_ie, float pct_ie, float ie, float importe_alquileres,
			float importe_intereses, float base_ivaigic_n, float pct_ivaigic_n,
			float ivaigic_n, float base_ivaigic_r, float pct_ivaigic_r,
			float ivaigic_r, float base_ivaigic_n2, float pct_ivaigic_n2,
			float ivaigic_n2, float base_ivaigic_r2, float pct_ivaigic_r2,
			float ivaigic_r2, boolean existeXML, String firmar, Integer at,
			String tipoOrigen) {
		super();
		this.id_fatr = id_fatr;
		this.id_distribuidora_cne = id_distribuidora_cne;
		this.desc_distribuidora_cne = desc_distribuidora_cne;
		this.id_comercializadora_cne = id_comercializadora_cne;
		this.desc_comercializadora_cne = desc_comercializadora_cne;
		this.cups = cups;
		this.contrato = contrato;
		this.num_factura = num_factura;
		this.id_tipofactrec_cne = id_tipofactrec_cne;
		this.desc_tipofactrec_cne = desc_tipofactrec_cne;
		this.num_factura_rectificada = num_factura_rectificada;
		this.ticket_energetico = ticket_energetico;
		this.fecha_factura = fecha_factura;
		this.fecha_grabacion = fecha_grabacion;
		this.dir_suministro = dir_suministro;
		this.municipio = municipio;
		this.documento = documento;
		this.observaciones = observaciones;
		this.id_tipofactper_cne = id_tipofactper_cne;
		this.desc_tipofactper_cne = desc_tipofactper_cne;
		this.id_tipofactura_cne = id_tipofactura_cne;
		this.desc_tipofactura_cne = desc_tipofactura_cne;
		this.id_tipo_identif_cne = id_tipo_identif_cne;
		this.cif = cif;
		this.fecha_boe = fecha_boe;
		this.modo_facturacion = modo_facturacion;
		this.validado = validado;
		this.fecha_validado = fecha_validado;
		this.fecha_envio_portal = fecha_envio_portal;
		this.documento_backup = documento_backup;
		this.fecha_firma = fecha_firma;
		this.fecha_sappi = fecha_sappi;
		this.cod_solicitud = cod_solicitud;
		this.fecha_solicitud = fecha_solicitud;
		this.tipo_moneda = tipo_moneda;
		this.fecha_valor = fecha_valor;
		this.fecha_limite_pago = fecha_limite_pago;
		this.id_tarifa_cne = id_tarifa_cne;
		this.desc_tarifa_cne = desc_tarifa_cne;
		this.fecha_ini = fecha_ini;
		this.fecha_fin = fecha_fin;
		this.cupspm = cupspm;
		this.num_meses = num_meses;
		this.ind_at_med_bt = ind_at_med_bt;
		this.val_lec = val_lec;
		this.val_atr_lec = val_atr_lec;
		this.ind_envio_csa = ind_envio_csa;
		this.fecha_envio_csa = fecha_envio_csa;
		this.modo_control_potencia = modo_control_potencia;
		this.total = total;
		this.saldo_factura = saldo_factura;
		this.saldo_cobro = saldo_cobro;
		this.base_ie = base_ie;
		this.coef_ie = coef_ie;
		this.pct_ie = pct_ie;
		this.ie = ie;
		this.importe_alquileres = importe_alquileres;
		this.importe_intereses = importe_intereses;
		this.base_ivaigic_n = base_ivaigic_n;
		this.pct_ivaigic_n = pct_ivaigic_n;
		this.ivaigic_n = ivaigic_n;
		this.base_ivaigic_r = base_ivaigic_r;
		this.pct_ivaigic_r = pct_ivaigic_r;
		this.ivaigic_r = ivaigic_r;
		this.base_ivaigic_n2 = base_ivaigic_n2;
		this.pct_ivaigic_n2 = pct_ivaigic_n2;
		this.ivaigic_n2 = ivaigic_n2;
		this.base_ivaigic_r2 = base_ivaigic_r2;
		this.pct_ivaigic_r2 = pct_ivaigic_r2;
		this.ivaigic_r2 = ivaigic_r2;
		this.existeXML = existeXML;
		this.firmar = firmar;
		this.at = at;
		this.tipoOrigen = tipoOrigen;
	}
	
	public FacdisFacturaDTO() {
		super();
		this.id_fatr = 0;
		this.id_distribuidora_cne = null;
		this.desc_distribuidora_cne = null;
		this.id_comercializadora_cne = null;
		this.desc_comercializadora_cne = null;
		this.cups = null;
		this.contrato = null;
		this.num_factura = null;
		this.id_tipofactrec_cne = null;
		this.desc_tipofactrec_cne = null;
		this.num_factura_rectificada = null;
		this.ticket_energetico = null;
		this.fecha_factura = null;
		this.fecha_grabacion = null;
		this.dir_suministro = null;
		this.municipio = 0;
		this.documento = null;
		this.observaciones = null;
		this.id_tipofactper_cne = null;
		this.desc_tipofactper_cne = null;
		this.id_tipofactura_cne = null;
		this.desc_tipofactura_cne = null;
		this.id_tipo_identif_cne = null;
		this.cif = null;
		this.fecha_boe = null;
		this.modo_facturacion = null;
		this.validado = null;
		this.fecha_validado = null;
		this.fecha_envio_portal = null;
		this.documento_backup = null;
		this.fecha_firma = null;
		this.fecha_sappi = null;
		this.cod_solicitud = null;
		this.fecha_solicitud = null;
		this.tipo_moneda = null;
		this.fecha_valor = null;
		this.fecha_limite_pago = null;
		this.id_tarifa_cne = null;
		this.desc_tarifa_cne=null;
		this.fecha_ini = null;
		this.fecha_fin = null;
		this.cupspm = null;
		this.num_meses = null;
		this.ind_at_med_bt = null;
		this.val_lec = null;
		this.val_atr_lec = null;
		this.ind_envio_csa = "0";
		this.fecha_envio_csa = null;
		this.modo_control_potencia = null;
		this.total = 0;
		this.saldo_factura = 0;
		this.saldo_cobro = 0;
		this.base_ie = 0;
		this.coef_ie = 0;
		this.pct_ie = 0;
		this.ie = 0;
		this.importe_alquileres = 0;
		this.importe_intereses = 0;
		this.base_ivaigic_n = 0;
		this.pct_ivaigic_n = 0;
		this.ivaigic_n = 0;
		this.base_ivaigic_r = 0;
		this.pct_ivaigic_r = 0;
		this.ivaigic_r = 0;
		this.base_ivaigic_n2 = 0;
		this.pct_ivaigic_n2 = 0;
		this.ivaigic_n2 = 0;
		this.base_ivaigic_r2 = 0;
		this.pct_ivaigic_r2 = 0;
		this.ivaigic_r2 = 0;
		this.existeXML = false;
		this.firmar = null;
		this.at = null;
		this.tipoOrigen = null;
	}
}