package com.eon.fatr.batch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;

import com.eon.fatr.batch.general.EjecucionesServicios;
import com.eon.fatr.componentes.FacdisFacturaDTO;
import com.eon.fatr.utilidades.LogProceso;
import com.eon.fatr.utilidades.PropiedadesAplicacion;
import com.eon.fatr.utilidades.TratarFicheros;
import com.eon.fatr.utilidades.UtilFTP;
import com.eon.firmaDigital.FirmaFacturas;
import com.eon.jaxb.MensajeFacturacion;
import com.ibatis.common.jdbc.SimpleDataSource;
import com.ibatis.sqlmap.client.SqlMapClient;

public class Batch_FACDIS_EnvioPortal {
	// Código de retorno para Control-M
	private static int controlM_ExitCode;
	
	private static boolean todoOK;
	
	private static String nombreServicio= "PROCESO_BATCH_ENVIOPORTAL";
	
	// Contadores para el resumen
	private static int deben;
	private static int noEncontrados;
	private static int enviadosPortal;
	private static int firmados;
	private static int modificadosAgrupacion;
	private static int noModificadosAgrupacion;
	
	private static SqlMapClient sqlMap;

	private static Log logger = LogFactory.getFactory().getInstance(Batch_FACDIS_EnvioPortal.class);
	
	public Batch_FACDIS_EnvioPortal(String nombreFichero) {
	}

	public static void main(String[] args) {

		controlM_ExitCode = 0;
		
    	BasicConfigurator.configure();
		
		try {
			logger.info("Inicio proceso ENVIO AL PORTAL DE FACTURAS ATR (FACDIS)");
	
			if (args.length < 3) {
				// Número de argumentos incorrectos
				logger.error("Es necesario incluir el nombre del fichero de configuracion como parametro");
				throw new Exception("NO EXISTEN FICHEROS DE PROPIEDADES PASADOS COMO PARAMETROS");
			}
			if(TratarFicheros.existeFichero(args[1])) {
				// Cambiamos el fichero de propiedades del log
				PropertyConfigurator.configure(args[1]);
			} else {
				logger.error("No se encuentra el fichero de propiedades de log");
			}

			sqlMap = PropiedadesAplicacion.LeerFicherosProperties(args[0], args[1], args[2]);

			try {
				todoOK = false; 

				LogProceso.Cabecera(nombreServicio);
				
				Batch_FACDIS_EnvioPortal envioPortal = new Batch_FACDIS_EnvioPortal(args[0]);
				
				//Recuperamos todas las facturas que se han validado (OK)
				List<FacdisFacturaDTO> datos = envioPortal.recuperaFacturas();
				
				// Generamos el zip con todas las facturas de la carpeta PendientePORTAL
				envioPortal.generarZip();
				
				// Comprobamos si se quiere enviar al Portal
				if (PropiedadesAplicacion.getInstance().getPropiedad("ENVIAR_PORTAL").equals("S")) {
					boolean bCorrecto = envioPortal.subirFicherosFTP();
					if (bCorrecto) {
						// si todo ha ido bien, actualizamos el campo en base de datos
						envioPortal.actualizarFacturas(datos);
					}
				} else {
					// No enviamos el zip al Portal, lo dejaremos en la carpeta
					// "PENDIENTEZIPPORTAL" y cuando se active el envío se enviarán
					// todo el contenido de esta carpeta.
					logger.info("Está deshabilitado el envío del zip al Portal.");
				}
				logger.info("FIN DEL PROCESO de ENVIO AL PORTAL DE FACTURAS ATR.");
				todoOK = true;
	            
	    	} catch (IOException ioe) {
	    		logger.error("Error IO: " + ioe.getMessage());
	    		controlM_ExitCode = 2;
	    	} catch (SQLException ioe) {
	    		logger.error("Error SQL: " + ioe.getMessage());
	    		controlM_ExitCode = 5;
	    	} catch (Exception e) {
	    		logger.debug("Error al procesar envio al portal en motor:" + e.getMessage());
	    		controlM_ExitCode = 1;
	    	}
		}
		catch (Exception e){
			logger.error("Error al cargar los ficheros de propiedades motor:" + e.getMessage());
			controlM_ExitCode = 1;
	    }
		finally {
			//Escribimos la fecha de la ejecucion correcta
			try	{
				if (sqlMap != null) {
					Date fechaHoy = new Date();
					SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
					String fechaejecucion = formato.format(fechaHoy);
					EjecucionesServicios.setEjecucion(sqlMap, "Batch_FACDIS_EnvioPortal", fechaejecucion, PropiedadesAplicacion.getRutaLog(), todoOK, "FACDIS");
				}
			} catch (Exception ex) {
				logger.error("No se ha guardado la fecha de ejecucion en la base de datos: " + ex.getMessage());
				controlM_ExitCode = 1;
		    }
			
			//Escribimos el resumen del proceso
			 EscribirResultado();
			
			//Escribimos el pie que muestra el tiempo que ha tardado el proceso
			LogProceso.Pie();
	    	logger.info("FINALIZACIÓN DEL PROCESO DE ENVIO AL PORTAL DE FACTURAS ATR (FACDIS)");
	    	
			//Cerramos todas las conexiones del pool
	    	if (sqlMap != null) {
				SimpleDataSource ds = (SimpleDataSource) sqlMap.getDataSource();
		        ds.forceCloseAll();
			}
	    	System.exit(controlM_ExitCode);
	    }
	}			

	//Método que lee el fichero y recupera los datos en BBDD.
	private List<FacdisFacturaDTO> recuperaFacturas() {

		logger.info("COMIENZA LA RECUPERACIÓN DE FACTURAS...");
		
		String rutaFechaFichero = "";
		String nombreFichero = "";
		boolean bFicheroXML = true;
		noEncontrados = 0;
		List<FacdisFacturaDTO> datos = null;
		try {
			HashMap map = new HashMap();
			try {
				map.put("DIAS_NO_DOMICILIACION", PropiedadesAplicacion.getInstance().getPropiedad("DIAS_NO_DOMICILIACION"));
				map.put("DIAS_DOMICILIACION", PropiedadesAplicacion.getInstance().getPropiedad("DIAS_DOMICILIACION"));
			} catch (Exception e1) {
				logger.error("Error " + e1.getMessage());
			}
			datos = sqlMap.queryForList("recuperaFacturas", map);
			
			deben = datos.size();
			
			if (datos.size() > 0) {
				// Recorremos el listado y recuperamos el documento xml
				ListIterator<FacdisFacturaDTO> listado = datos.listIterator();
				while (listado.hasNext()) {
					
					FacdisFacturaDTO factura = listado.next();
					// Pasamos la ruta y el nombre del documento
					if (factura.getDocumento() != null) {
						try {
							rutaFechaFichero = factura.getDocumento().substring(0, factura.getDocumento().lastIndexOf("/"));
							nombreFichero = factura.getDocumento().substring(factura.getDocumento().lastIndexOf("/") + 1, factura.getDocumento().length());

							String FTPMircom = PropiedadesAplicacion.getInstance().getPropiedad("FTP_MIRCOM");;
							
							if (FTPMircom.equals("S")) {
								// Si hay que recuperar los xmls por ftp, no esta funcionando.
								// bFicheroXML = procesaDirectorioFTP(ruta, nombreFichero);
							} else {
								bFicheroXML = procesaDirectorio(rutaFechaFichero, nombreFichero, factura);
							}
							// Si hemos podido recuperar el fichero, lo indicamos
							if (bFicheroXML) {
								factura.setExisteXML(true);
								enviadosPortal++;
							} else {
								factura.setExisteXML(false);
								noEncontrados++;
							}
						} catch (Exception e) {
							logger.error("Error al acceder al documento de la factura // Id_fatr " + factura.getNum_factura() + " //" + factura.getId_fatr() + ". Error: " + e.getMessage());
						}
					} else {
						logger.error("Para el numero de factura // Id_fatr " + factura.getNum_factura() + " //" + factura.getId_fatr() + " no existe documento xml.");
					}
				}
			} else {
				logger.info("NO EXISTEN FACTURAS PARA ENVIAR...");
			}
	
		} catch (SQLException e) {
			logger.error("Error al recuperar las facturas validas: " + e.getMessage());
		}
		logger.info("FIN DE LA RECUPERACIÓN DE FACTURAS.");
		return datos;
	}

	//Método que lee el fichero y recupera los datos en BBDD.
	private void actualizarFacturas(List<FacdisFacturaDTO> datos) {

		logger.info("COMIENZA LA ACTUALIZACIÓN DE fecha_envio_portal PARA LAS FACTURAS...");

		try {
			if (datos.size()>0) {
				// Recorremos el listado y recuperamos el documento xml
				ListIterator<FacdisFacturaDTO> listado = datos.listIterator();
				while (listado.hasNext()) {
					FacdisFacturaDTO factura = listado.next();
					// Si hemos podido descargar el documento XML del FTP de MIRCOM
					// actualizamos la fecha
					if (factura.isExisteXML()) {
						sqlMap.update("actualizaFechaEnvioPortal", "" + factura.getId_fatr());
						logger.info("Id_fatr " + factura.getId_fatr() + " actualizado el campo fecha_envio_portal.");
					} else {
						logger.info("Id_fatr " + factura.getId_fatr() + " NO actualizado el campo fecha_envio_portal.");
					}
				}
			} else {
				logger.info("NO EXISTEN FACTURAS PARA ENVIAR...");
			}

		} catch (SQLException e) {
			logger.error("Error al actualizar el campo fecha_envio_portal para la factura:: " + e.getMessage());
		}
		logger.info("FIN DE LA ACTUALIZACIÓN fecha_envio_portal DE FACTURAS.");
	}

	/**
	 * A partir del directorio de Procesado, localizamos el fichero xml para entregar al Portal
	 * @param DirectorioFTP
	 * @param nombreFichero
	 * @return
	 */
	private boolean procesaDirectorio(String rutaFechaFichero, String nombreFichero, FacdisFacturaDTO factura) {

		boolean bCorrecto = true;
		
		try {
			String directorioProcesado = PropiedadesAplicacion.getInstance().getPropiedad("DIRECTORIOPROCESADO");
			String directorioPendientePORTAL = PropiedadesAplicacion.getInstance().getPropiedad("DIRECTORIOPENDIENTEPORTAL");

			/* Guardar original como .opi */
			String nombreFicheroOpi = nombreFichero.substring(0, nombreFichero.length()-4) + ".opi";
			File origen 	= new File(directorioProcesado + rutaFechaFichero + File.separator + nombreFichero);
			File destinoOpi = new File(directorioProcesado + rutaFechaFichero + File.separator + nombreFicheroOpi);
			
			boolean bCorrectoOpi = TratarFicheros.copyFile(origen, destinoOpi);
			/* Fin Guardar original como .opi */
			
			if (bCorrectoOpi) {
				// Comprobar si se debe agrupar
				boolean bModificaXML = false;
				
				GregorianCalendar gcff = (GregorianCalendar) GregorianCalendar.getInstance();
				SimpleDateFormat sdff= new SimpleDateFormat("yyyy-MM-dd"); 										
				Date fechaff = sdff.parse(factura.getFecha_factura());
				gcff.setTime(fechaff);

				GregorianCalendar gcfda = (GregorianCalendar) GregorianCalendar.getInstance();
				SimpleDateFormat sdfda= new SimpleDateFormat("dd/MM/yyyy"); 										
				Date fechafda = sdfda.parse(PropiedadesAplicacion.getInstance().getPropiedad("FECHADESDEAGRUPAR"));
				gcfda.setTime(fechafda);
				
				// Si la fecha de factura es mayor o igual que la del properties se agrupan, sino no.
				if (gcff.compareTo(gcfda) >= 0) {
					// Calcular Agrupación Modificar XML y Modificar BBDD
					bModificaXML = modificaXML(directorioProcesado + rutaFechaFichero + File.separator + nombreFichero, factura);
				} else {
					bModificaXML = true;
					noModificadosAgrupacion++;
				}
				
				if (bModificaXML) {
					
					File destino = new File(directorioPendientePORTAL + File.separator + nombreFichero);
					
					String[] arrayNombreFichero = nombreFichero.split("\\.");
					String distribuidora = arrayNombreFichero[0];
		
					// Comprobamos si se quieren firmar los xml por distribuidora
					if ( (PropiedadesAplicacion.getInstance().getPropiedad("FIRMAR_EON").equals("S") && distribuidora.equals("0027")) ||
						(PropiedadesAplicacion.getInstance().getPropiedad("FIRMAR_BEG").equals("S") && distribuidora.equals("0033")) ) {
						// Comprobamos si se quieren firmar los xml por comercializadora
						if (factura.getFirmar().equals("S")) {
							// Comprobamos si ya está firmado para no volverlo a firmar
							if (factura.getFecha_firma()==null || factura.getFecha_firma().equals("")) {
								try {
									FirmaFacturas gestion = new FirmaFacturas();
									String sDirOrigen = directorioProcesado + rutaFechaFichero;
									String sDirSalida = directorioPendientePORTAL;
									String sDirPropiedades = PropiedadesAplicacion.getInstance().getPropiedad("DIRECTORIOCERTIFICADOS");
				
									boolean bFirmado = false;
										bFirmado = gestion.firmarXML(nombreFichero, sDirOrigen, sDirSalida, sDirPropiedades);
										logger.debug("Fichero ["+nombreFichero+"] firmado correcto ["+bFirmado+"]");
										if (bFirmado) {
											// si todo ha ido bien, actualizamos el campo en base de datos
											sqlMap.update("actualizaFechaFirma", "" + factura.getId_fatr());
											firmados++;
										}
								} catch (Exception e){
									logger.error("ERROR AL FIRMAR. " + e.getMessage());
								}
							} else {
								bCorrecto = TratarFicheros.copyFile(origen, destino);
								logger.info("El xml ya está firmado, se copia el ya firmado");
							}
						} else {
							bCorrecto = TratarFicheros.copyFile(origen, destino);
							logger.info("Comercializadora sin firma digital, se copia sin firmar");
						}
					} else {
						bCorrecto = TratarFicheros.copyFile(origen, destino);
						logger.info("Distribuidora sin firma digital, se copia sin firmar " + distribuidora);
					}
				} else {
					bCorrecto = false;
					logger.error("No se ha podido modificar la agrupación de " + nombreFichero);
				}
			} else {
				bCorrecto = false;
				logger.error("No se ha podido guardar el original " + nombreFichero);
			}
		} catch (Exception e) {
			bCorrecto = false;			
			logger.error("Error " + e.getMessage());
		}
		return bCorrecto;
	}
	
	//Método que lee el fichero y recupera los datos en BBDD.
	private void generarZip() {

		logger.info("Comienza la generación del ZIP...");
		String outFilename = null;
		String nombreZipDestino = null;

		try {
			String directorioPendientePORTAL = PropiedadesAplicacion.getInstance().getPropiedad("DIRECTORIOPENDIENTEPORTAL");
			String nombreZIP = PropiedadesAplicacion.getInstance().getPropiedad("NOMBREZIP");
			
			File directorio = new File(directorioPendientePORTAL);

			String [] ficheros = directorio.list();
			try {
				
				if (ficheros.length > 0) {
					nombreZipDestino = nombreZIP + TratarFicheros.recuperarFecha() + ".zip";
					
					String directorioPendienteZIPPORTAL = PropiedadesAplicacion.getInstance().getPropiedad("DIRECTORIOPENDIENTEZIPPORTAL");
					
					outFilename = directorioPendienteZIPPORTAL + File.separator + nombreZipDestino; // nombre del zip generado
		            FileInputStream in = null;
		            byte[] buf = new byte[1024];
		            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFilename));
		             
		            for(int i =0; i < ficheros.length; i++){
		          	  // recorre los nombres de los ficheros para insertarlos en el zip
		            	if (!ficheros[i].equals("")){
		            		String path = directorioPendientePORTAL + File.separator + ficheros[i]; //nombre del fichero
		                    in = new FileInputStream(path);
		                    out.putNextEntry(new ZipEntry((String) ficheros[i]));
		                    int len;
		                    while ((len = in.read(buf)) > 0) {
		                    	out.write(buf, 0, len);
		                    }
		                    //cierra la escritura de la entrada del fichero
		                    out.closeEntry();
		                    in.close();
		            	}
		            }
		            // cierra la escritura del zip
		            out.close();
		            
		            for(int i =0; i < ficheros.length; i++){
			          	// recorre los nombres de los ficheros de nuevo para borrarlos
			            if (!ficheros[i].equals("")){
		                    //borra el fichero descargado de local
		                    File f = new File(directorioPendientePORTAL + File.separator + ficheros[i]);
		                    logger.debug("borrar fichero " + f.getAbsolutePath());
		                    boolean borrarParaZip = f.delete();
							if (!borrarParaZip) {
								logger.error("ERROR al borrar archivo " + f.getName() + " para ZIP ");
							}
		          	   	}
		            }
				} else {
					logger.info("No existen ficheros para generar el zip.");
				}
			} catch (Exception e) {
				logger.error("Error " + e.getMessage());
			}
		} catch (Exception e) {
			logger.error("Error " + e.getMessage());
		} finally {
			logger.info("Fin de la generación del zip.");
		}
	}
	
	/**
	 * Funcion que escribe el resumen del proceso en el fichero de log
	 */
	private static void EscribirResultado(){
		LogProceso.mensaje(" ");
		LogProceso.mensaje("PROCESO DE ENVIO PORTAL, FIRMA Y ENVIO PI:");
		LogProceso.mensaje("  -----------------------------------------------  ");
		LogProceso.mensaje(" ");
		
		LogProceso.registro("Ficheros que deben enviarse al Portal", deben);
		LogProceso.registro("Ficheros no encontrados", noEncontrados);
		LogProceso.registro("Ficheros Modificados con agrupación", modificadosAgrupacion);
		LogProceso.registro("Ficheros NO Modificados con agrupación", noModificadosAgrupacion);
		LogProceso.registro("Ficheros Firmados", firmados);
		LogProceso.registro("Ficheros Correctamente Enviados al Portal", enviadosPortal);

		LogProceso.mensaje(" ");
	}
	
	// Función que coge todos los ficheros de un directorio local y los
	// sube a un FTP.
	private boolean subirFicherosFTP() {
		boolean vCorrecto=true;
		File fOrigen;
		File fDestino;

		String directorioPendienteZIPPORTAL;
		try {
			directorioPendienteZIPPORTAL = PropiedadesAplicacion.getInstance().getPropiedad("DIRECTORIOPENDIENTEZIPPORTAL");
			
			File directorio = new File(directorioPendienteZIPPORTAL);
			
			String [] ficheros = directorio.list();

			try {

				String PROTOCOLO_SEGURO_PORTAL = PropiedadesAplicacion.getInstance().getPropiedad("PROTOCOLO_SEGURO_PORTAL");
				String TIPO_TRANSFERENCIA_PORTAL = PropiedadesAplicacion.getInstance().getPropiedad("TIPO_TRANSFERENCIA_PORTAL");
				String ipFTP_PORTAL = PropiedadesAplicacion.getInstance().getPropiedad("IP_FTP_PORTAL");
				String Usuario_FTP_PORTAL = PropiedadesAplicacion.getInstance().getPropiedad("USUARIO_FTP_PORTAL");
				String Password_FTP_PORTAL = PropiedadesAplicacion.getInstance().getPropiedad("CLAVE_FTP_PORTAL");
				int Timeout_FTP_PORTAL = Integer.parseInt(PropiedadesAplicacion.getInstance().getPropiedad("TIMEOUT_FTP_PORTAL"));
				String Directorio_FTP_PORTAL = PropiedadesAplicacion.getInstance().getPropiedad("DIRECTORIO_FTP_PORTAL");
				
				UtilFTP ftp = new UtilFTP(ipFTP_PORTAL, Usuario_FTP_PORTAL, Password_FTP_PORTAL, PROTOCOLO_SEGURO_PORTAL, TIPO_TRANSFERENCIA_PORTAL);
				
	  			//Se asigna el timeout
				ftp.setTimeout(Timeout_FTP_PORTAL);
				
				for(int i =0; i < ficheros.length; i++) {
				
					boolean bEnviar = ftp.enviarFTP(directorioPendienteZIPPORTAL + File.separator + ficheros[i], Directorio_FTP_PORTAL, ficheros[i]);
					
					if(bEnviar){
						String directorioEnviosPORTAL = PropiedadesAplicacion.getInstance().getPropiedad("DIRECTORIOENVIOSPORTAL");

						//Se mueve el fichero al directorio destino
						fOrigen = new File(directorioPendienteZIPPORTAL + File.separator + ficheros[i]);
						fDestino = new File(directorioEnviosPORTAL + File.separator + ficheros[i]);
						TratarFicheros.desplazar(fOrigen, fDestino);
						
					} else {
						logger.error("No se ha copiado el fichero " + ficheros[i] + " en el directorio del FTP " );
						vCorrecto=false;
					}
				}
			} catch (Exception e) {
				logger.error("ERROR CONEXION AL FTP." + e.getMessage());
				vCorrecto=false;
			}
		} catch (Exception e) {
			logger.error("Error " + e.getMessage());
			vCorrecto=false;
		}
		return vCorrecto;
	}

	private boolean modificaXML(String nombreFichero, FacdisFacturaDTO factura) {
		boolean correcto = false;
		
		logger.debug("Al " + nombreFichero + " le cambiaremos los datos a: " + factura.getObservaciones() + "-" + factura.getCod_solicitud() + "-" + factura.getFecha_valor() + "-" + factura.getFecha_limite_pago());
		
		File ficheroAModificar = new File(nombreFichero);
		try {
			JAXBContext jc = JAXBContext.newInstance("com.eon.jaxb");
			Unmarshaller u = jc.createUnmarshaller();
			MensajeFacturacion facturaJaxb = (MensajeFacturacion) u.unmarshal(ficheroAModificar);

			//Observaciones
			for(int i=0;i < facturaJaxb.getFacturas().getFacturaATR().size();i++) {
				facturaJaxb.getFacturas().getFacturaATR().get(i).getDatosGeneralesFacturaATR().
					getDatosGeneralesFactura().setObservaciones(factura.getObservaciones());
			}
			for(int i=0;i < facturaJaxb.getFacturas().getOtrasFacturas().size();i++) {
				facturaJaxb.getFacturas().getOtrasFacturas().get(i).getDatosGeneralesOtrasFacturas().
					getDatosGeneralesFactura().setObservaciones(factura.getObservaciones());
			}
			//Firmado
			if (factura.getFirmar().equals("S")) {
				facturaJaxb.setFirmar(factura.getFirmar());
			} else {
				facturaJaxb.setFirmar(null);
			}

			//Código de Solicitud
			facturaJaxb.getCabecera().setCodigoDeSolicitud(factura.getCod_solicitud());
			
			//Fecha Valor
			GregorianCalendar gcfv = (GregorianCalendar) GregorianCalendar.getInstance();
			SimpleDateFormat sdfv= new SimpleDateFormat("yyyy-MM-dd"); 										
			Date fechav = sdfv.parse(factura.getFecha_valor());
			gcfv.setTime(fechav);
			XMLGregorianCalendar xmlFQDMaxfv = DatatypeFactory.newInstance().newXMLGregorianCalendar();
			xmlFQDMaxfv.setDay(gcfv.get(GregorianCalendar.DAY_OF_MONTH));
			xmlFQDMaxfv.setMonth(gcfv.get(GregorianCalendar.MONTH) + 1);
			xmlFQDMaxfv.setYear(gcfv.get(GregorianCalendar.YEAR));
			facturaJaxb.getFacturas().getRegistroFin().setFechaValor(xmlFQDMaxfv);

			//Fecha Limite de Pago
			GregorianCalendar gcfl = (GregorianCalendar) GregorianCalendar.getInstance();
			SimpleDateFormat sdfl= new SimpleDateFormat("yyyy-MM-dd"); 										
			Date fechal = sdfl.parse(factura.getFecha_limite_pago());
			gcfl.setTime(fechal);
			XMLGregorianCalendar xmlFQDMaxfl = DatatypeFactory.newInstance().newXMLGregorianCalendar();
			xmlFQDMaxfl.setDay(gcfl.get(GregorianCalendar.DAY_OF_MONTH));
			xmlFQDMaxfl.setMonth(gcfl.get(GregorianCalendar.MONTH) + 1);
			xmlFQDMaxfl.setYear(gcfl.get(GregorianCalendar.YEAR));
			facturaJaxb.getFacturas().getRegistroFin().setFechaLimitePago(xmlFQDMaxfl);
				
			//Se crea el xml acutalizado
			Marshaller m = jc.createMarshaller();
			//Se añaden propiedades de formato
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,new Boolean(true));
//			Error 13/11/2013 - Cortar direcciones y cambiar caracteres
			m.setProperty(Marshaller.JAXB_ENCODING,"ISO-8859-1");
//			m.setProperty(Marshaller.JAXB_ENCODING,"UTF-8");
			m.marshal(facturaJaxb, ficheroAModificar);

			sqlMap.update("actualizaAgrupacionFacturas", factura);
			logger.info("Id_fatr " + factura.getId_fatr() + " actualizado agrupación.");
			modificadosAgrupacion++;
			correcto=true;

		} catch (Exception e) {
			logger.error("Error " + e.getMessage());
			correcto=false;
		}
		return correcto;
	}
}