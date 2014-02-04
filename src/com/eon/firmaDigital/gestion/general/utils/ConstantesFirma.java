package com.eon.firmaDigital.gestion.general.utils;


import java.util.ResourceBundle;

public class ConstantesFirma {
    // constantes para el uso de certificado digital de usuario
    private static ResourceBundle recursos = ResourceBundle.getBundle("properties.SignXML");

    public static final String VALOR_ENCODING_XML = recursos.getString("VALOR_ENCODING_XML");
    public static final String ENCODING_XML = recursos.getString("encodingXML");
    public static final String EMPRESA_EON = recursos.getString("empresa_EON");
    public static final String EMPRESA_BEG = recursos.getString("empresa_BEG");
}
