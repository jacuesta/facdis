package com.eon.firmaDigital.gestion.general.utils;


import java.util.ResourceBundle;

public class ConstantesCerts {
    // constantes para el uso de certificado digital de usuario
    private static ResourceBundle recursos = ResourceBundle.getBundle("properties.cert");

	public static final String RUTA_CONTENEDOR_CERTIFICADOS_EON = recursos.getString("rutaContenedorCertificados_EON");
	public static final String PWD_CONTENEDOR_EON = recursos.getString("pwdContenedor_EON");
	public static final String TIPO_CONTENEDOR_EON = recursos.getString("tipoContenedor_EON");
	public static final String  ALIAS_CERTIFICADO_EON = recursos.getString("aliasCerificado_EON");
	
	public static final String RUTA_CONTENEDOR_CERTIFICADOS_BEG = recursos.getString("rutaContenedorCertificados_BEG");
	public static final String PWD_CONTENEDOR_BEG = recursos.getString("pwdContenedor_BEG");
	public static final String TIPO_CONTENEDOR_BEG = recursos.getString("tipoContenedor_BEG");
	public static final String  ALIAS_CERTIFICADO_BEG  = recursos.getString("aliasCerificado_BEG");
}
