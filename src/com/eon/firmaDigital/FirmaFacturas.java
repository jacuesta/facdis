package com.eon.firmaDigital;
import java.io.File; 
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;

import org.apache.log4j.Logger;

import com.ame.sign.Xades;
import com.eon.firmaDigital.gestion.general.utils.ConstantesCerts;
import com.eon.firmaDigital.gestion.general.utils.ConstantesFirma;
import com.eon.firmaDigital.gestion.general.utils.FileCopy;
import com.eon.firmaDigital.gestion.general.utils.FileUtils;
import com.eon.firmaDigital.gestion.general.utils.XMLUtils;


import es.mityc.firmaJava.configuracion.Configuracion;
import es.mityc.firmaJava.configuracion.ConstantesConfiguracion;

/**
 * v1.  RECOGE TODOS LOS XML EN FORMATO FACTURADUOS
 *    - LECTUARA DEL XML --> FACTURA PARA OBTENER LOS DATOS NECESARIOS PARA LA DB
 *    - FIRMA XADES DE LA FACTURA
 *    - TRAS TATAR TODOS LOS REGISTROS, COPIAMOS EL FICHERO AL DIRECTORIO DE TRATADOS O ERROR (CASO DE HABER ALGUN REGISTRO ERRONEO) 
 */

public class FirmaFacturas {

	static Logger log = Logger.getLogger(FirmaFacturas.class.getName());
	
	private static String crearRutaFecha(String dirBase, Timestamp fecha) {
		
		// Obtener un calendar con la fecha
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		
		// Extraer el dia, mes y año
		String day = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
		String month = Integer.toString(calendar.get(Calendar.MONTH)+1);
		String year = Integer.toString(calendar.get(Calendar.YEAR));
		
		// Crear el string con la ruta
		String dir = dirBase + 
			File.separator + year + 
			File.separator + month + 
			File.separator + day ; 
		
		// Crear los directorios
		FileUtils.createPath(dir);
		
		return dir;
	}


	private static boolean comprobarExisteDirectorio(String path) {
		boolean esDir=false;
		
		// crear el objeto file para obtener información sobre el
		if ( path != null && !"".equals(path)) {
			File fichero = new File(path);
			if ( fichero.exists() && fichero.isDirectory() ) {
				esDir = true;
			}
		}
		
		return esDir;
	}
	
	public boolean firmarXML(String sNombreFichero, String sDirOrigen, String sDirSalida, String sDirPropiedades) {
		boolean bCorrecto = true;
		//log.debug("Inicio firma ...");
		try { 
			// comprobar que existen las rutas de entrada
			boolean existeDirBase = comprobarExisteDirectorio(sDirOrigen);
			boolean existeDirBaseRepository = comprobarExisteDirectorio(sDirSalida);
			boolean existeDirBasePropiedades = comprobarExisteDirectorio(sDirSalida);
			
			if (!existeDirBase) {
				log.error("No existe el directorio Origen: '" + sDirOrigen + "'" );
				bCorrecto = false;
			}
			if (!existeDirBaseRepository) {
				log.error("No existe el directorio destino '" + sDirSalida + "'" );				
				bCorrecto = false;
			}
			if (!existeDirBasePropiedades) {
				log.error("No existe el directorio de los Certificados '" + sDirPropiedades + "'" );				
				bCorrecto = false;
			}
			
		//Si existen las rutas.
			if(sNombreFichero.contains(".F1.") && bCorrecto){
				File fileXml = new File (sDirOrigen + File.separator + sNombreFichero);
				String nombreXML = fileXml.getName();
				String dirXML = fileXml.getParent();
						
				try {
//					if ( XMLUtils.ValidaXml(fileXml.getAbsolutePath(), sDirPropiedades + File.separator , ConstantesXSD.XSDS) ) {									
						/** Se cargan las propiedades del repositorio y del certificado a utilizar en función de la empresa comercializadora**/
						//String carpetaComercializadora;
						String empresa;
						String rutaContenedorCertificados;
						String pwdContenedor;
						String tipoContenedor;
						String aliasCertificado;
							
						String[] arrayNombreXML = nombreXML.split("\\.");
						empresa = arrayNombreXML[0];
							
						if (empresa.equals(ConstantesFirma.EMPRESA_EON)){
							//carpetaComercializadora=ConstantesFtp.DIR_FIRMADOS_ML;
							rutaContenedorCertificados=ConstantesCerts.RUTA_CONTENEDOR_CERTIFICADOS_EON;
							pwdContenedor=ConstantesCerts.PWD_CONTENEDOR_EON;
							tipoContenedor=ConstantesCerts.TIPO_CONTENEDOR_EON;
							aliasCertificado=ConstantesCerts.ALIAS_CERTIFICADO_EON;											
						} else if (empresa.equals(ConstantesFirma.EMPRESA_BEG)){
							//carpetaComercializadora=ConstantesFtp.DIR_FIRMADOS_TUR;
							rutaContenedorCertificados=ConstantesCerts.RUTA_CONTENEDOR_CERTIFICADOS_BEG;
							pwdContenedor=ConstantesCerts.PWD_CONTENEDOR_BEG;
							tipoContenedor=ConstantesCerts.TIPO_CONTENEDOR_BEG;
							aliasCertificado=ConstantesCerts.ALIAS_CERTIFICADO_BEG;	
						} else {
							throw new Exception("La Comercializadora de la factura no se corresponde con una de las requeridas");
						}
								
						/** Formatear contenido del fichero xml **/
						XMLUtils.format(fileXml.getAbsolutePath(), ConstantesFirma.ENCODING_XML);									
		
						 /** Generamos el fichero de salida **/
						String nombreXMLFirmado = nombreXML.toUpperCase().replace(".XML",".xmf");
		
						/** A continuacion la firmamos */
						log.debug("Inicio de firma");
						//log.debug(Configuracion.VALOR_ENCODING_XML);
						//log.debug(Configuracion.ENCODING_XML);
						boolean firmaOk = Xades.sign(dirXML, nombreXML, nombreXMLFirmado, sDirPropiedades + File.separator + rutaContenedorCertificados, tipoContenedor, pwdContenedor, aliasCertificado);
						log.debug("Distribuidora: " + empresa + " Certificado: " + rutaContenedorCertificados);
						log.debug("Fin de firma");
								
						if (firmaOk) {

							/*
							//Renombramos el xml original como opi (Original del PI)
							File fFileOriginal = new File(sDirOrigen,fileXml.getName().toUpperCase().replace(".XML",".opi"));
							boolean renombradoOriginal = fileXml.renameTo(fFileOriginal);
							*/
							boolean ddd = fileXml.delete();
							if (!ddd) {
								bCorrecto = false;
								log.error("ERROR al borrar el xml");
							}

							/** obtener el directorio final del archivo firmado **/
							File dirXmlFirmado = new File(sDirSalida);
							if(!dirXmlFirmado.exists()){
								dirXmlFirmado.mkdirs();
							}
							File firmado = new File(dirXML,nombreXMLFirmado);
							File destino= new File(sDirSalida, nombreXMLFirmado);
							if(destino.exists()) {
								destino.delete();
							}
							
							//boolean renombrado = firmado.renameTo(destino);
							//if(!renombrado){
								log.info("Intentamos copiar: "+firmado.getPath()); 
								try {
									FileCopy.copy(firmado.getPath(), sDirSalida);
								} catch (IOException e) {
									log.error("ERROR EN COPIADO a la otra carpeta " + e.getMessage());
								}
								// no lo borramos del origen
								// firmado.deleteOnExit();
							//}

							//Renombramos el xmf firmado como xml							
							File fFileFirmado = new File(sDirOrigen,fileXml.getName().toUpperCase().replace(".XMF",".xml"));
							boolean aaa = firmado.renameTo(fFileFirmado);
							if (!aaa) {
								bCorrecto = false;
								log.error("ERROR al renombrar archivo en carpeta 1 (carpeta procesado)");
							}
							File fFileRenombrado = new File(sDirSalida,fileXml.getName().toUpperCase().replace(".XMF",".xml"));
							boolean bbb = destino.renameTo(fFileRenombrado);
							if (!bbb) {
								bCorrecto = false;
								log.error("ERROR al renombrar archivo en carpeta 2 (carpeta pendiente portal)");
							}
							//log.debug("firmadoOk");
							//miFactura.setUrlXades(destino.getName());
							log.info("Factura "+nombreXMLFirmado+" firmada");
						} else {
							log.error("Factura ["+nombreXML+"] no ha podido ser firmada ["+nombreXMLFirmado+"]");
							File firmado = new File(dirXML,nombreXMLFirmado);
							firmado.delete();
							bCorrecto = false;
						}			
				} catch (Exception e) {
					bCorrecto = false;
					//log.error("Error en la factura con número factura: " + miFactura.getNumFactura());
					log.error(e.getMessage());
				}
			} else {
				bCorrecto = false;
				log.error("Directorio inexistente o inaccesible :" + sDirSalida);
			}
	
			// Registro del resumen
			//log.debug("****Fin del proceso de Generacion de Facturas**********");
			//log.debug("ficheros procesados : " + sNombreFichero);
//			log.info("ficheros correctos : " + numTratadosOk);
			
//			log.debug("****FIN DEL PROCESO**********");
		} catch (Exception e) {
			bCorrecto = false;
			log.error("ERROR EN PROCESO " + e.getMessage());
		}
		return bCorrecto;
	}
}