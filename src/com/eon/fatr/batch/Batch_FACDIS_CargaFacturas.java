package com.eon.fatr.batch;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;

import com.eon.fatr.batch.cargaFacturas.Ajustes;
import com.eon.fatr.batch.general.EjecucionesServicios;
import com.eon.fatr.componentes.FacdisFacturaConceptoDAO;
import com.eon.fatr.componentes.FacdisFacturaConceptoDTO;
import com.eon.fatr.componentes.FacdisFacturaDAO;
import com.eon.fatr.componentes.FacdisFacturaDTO;
import com.eon.fatr.componentes.FacdisFacturaLecturaDAO;
import com.eon.fatr.componentes.FacdisFacturaLecturaDTO;
import com.eon.fatr.componentes.FacdisFacturaPeriodosDAO;
import com.eon.fatr.componentes.FacdisFacturaPeriodosDTO;
import com.eon.fatr.componentes.FacdisFacturaRefacturacionDAO;
import com.eon.fatr.componentes.FacdisFacturaRefacturacionDTO;
import com.eon.fatr.componentes.FacdisTarifaMagnitudDAO;
import com.eon.fatr.componentes.FacdisTarifaMagnitudDTO;
import com.eon.fatr.utilidades.LogProceso;
import com.eon.fatr.utilidades.PropiedadesAplicacion;
import com.eon.fatr.utilidades.SchemaValidator;
import com.eon.fatr.utilidades.TratarFicheros;
import com.eon.fatr.utilidades.Util;
import com.eon.fatr.utilidades.UtilFTP;
import com.eon.jaxb.Facturas.FacturaATR;
import com.eon.jaxb.Facturas.FacturaATR.Alquileres;
import com.eon.jaxb.Facturas.FacturaATR.ConceptoIEIVA;
import com.eon.jaxb.Facturas.FacturaATR.ConceptoIVA;
import com.eon.jaxb.Facturas.FacturaATR.EnergiaActiva;
import com.eon.jaxb.Facturas.FacturaATR.EnergiaActiva.TerminoEnergiaActiva;
import com.eon.jaxb.Facturas.FacturaATR.EnergiaReactiva;
import com.eon.jaxb.Facturas.FacturaATR.EnergiaReactiva.TerminoEnergiaReactiva;
import com.eon.jaxb.Facturas.FacturaATR.ExcesoPotencia;
import com.eon.jaxb.Facturas.FacturaATR.ImpuestoElectrico;
import com.eon.jaxb.Facturas.FacturaATR.Medidas;
import com.eon.jaxb.Facturas.FacturaATR.Medidas.Aparato;
import com.eon.jaxb.Facturas.FacturaATR.Medidas.Aparato.Integrador;
import com.eon.jaxb.Facturas.FacturaATR.Potencia;
import com.eon.jaxb.Facturas.FacturaATR.Potencia.TerminoPotencia;
import com.eon.jaxb.Facturas.FacturaATR.Potencia.TerminoPotencia.Periodo;
import com.eon.jaxb.Facturas.FacturaATR.Refacturaciones;
import com.eon.jaxb.Facturas.FacturaATR.Refacturaciones.Refacturacion;
import com.eon.jaxb.Facturas.OtrasFacturas;
import com.eon.jaxb.Facturas.OtrasFacturas.Concepto;
import com.eon.jaxb.MensajeFacturacion;
import com.eon.jaxb.TipoIVAIGIC;
import com.ibatis.common.jdbc.SimpleDataSource;
import com.ibatis.sqlmap.client.SqlMapClient;

public class Batch_FACDIS_CargaFacturas {
	//variables static
	private static int controlM_ExitCode;
	private static boolean todoOK;
	private static String magnitudActual="";
	private static String nombreServicio = "PROCESO_BATCH_CARGAFACTURAS";
	private static SqlMapClient sqlMap;
	private static Log logger = LogFactory.getFactory().getInstance(
			Batch_FACDIS_CargaFacturas.class);

	// Contadores para el resumen
	private static int totalFacturas = 0;
	private static int totalProcesadas = 0;
	private static int totalSustituidas = 0;
	private static int totalErroneas = 0;
	private static int totalDuplicadas = 0;

	// A revisar
	private String cupsPm;
	private static boolean hayDatos = true;

	static // bCorrecto (0 - correcto, 1 - no valida esquema o error sql, 2 - factura
	// duplicada)
	String bCorrecto = "0";
	String ticketEnergetico = "";
	String sNombreFichero = "";

	// boolean bConceptos16_17_18 = false;

	// Variables del Fichero de configuración.
	private String directorioErrores;
	private String directorioDuplicadas;
	private String directorioProcesado;
	private String directorioPendiente;
	private String directorioEsquemas;
	private String directorioZipProcesado;
	private String directorioZipErrores;
	private String fechaDia;

	private String NombreSchema = "";
	private String blnFTP;
	private String ipFTP;
	private String Usuario_FTP;
	private String Password_FTP;
	private String Directorio_FTP;
	private String protocoloSeguro_FTP;
	private String tipoTransferencia_FTP;
	
	//Duos
	private String enviarDUOS;
	private String directorioPendienteDUOS;
	private String directorioErroresDUOS;
	private String directorioEnviosDUOS;
	private String ipFTP_DUOS;
	private String Usuario_FTP_DUOS;
	private String Password_FTP_DUOS;
	private String DirecotrioFTP_DUOS;
	private String protocoloSeguro_FTP_DUOS;
	private String tipoTransferencia_FTP_DUOS;

	// Secuencias.
	private static int SeqIdFacDis;

	FacdisFacturaPeriodosDAO periodosDao = new FacdisFacturaPeriodosDAO();

	private static double importeTotalEA;
	private static double importeTotalER;
	private static double importeTotalTP;
	private static double importeTotalEX;

	// Datos Generales Factura ATR- DireccionSuministro
	private ArrayList<String> CupsSum = new ArrayList<String>();
	private ArrayList<String> Municipio = new ArrayList<String>();
	private ArrayList<String> DirSuministro = new ArrayList<String>();

	// Impuesto Eléctrico
	private ArrayList BaseImponible = new ArrayList();
	private ArrayList Coeficiente = new ArrayList();
	private ArrayList Porcentaje = new ArrayList();
	private ArrayList Importe = new ArrayList();

	// Alquileres
	private ArrayList ImporteFacturacionAlquileres = new ArrayList();

	// Importe Intereses
	private ArrayList ImporteIntereses = new ArrayList();

	// Concepto IEIVA IVA
	private ArrayList TipoConceptoIEIVA = new ArrayList();
	private ArrayList ImporteConceptoIEIVA = new ArrayList();
	private ArrayList ObservacionesIEIVA = new ArrayList();
	private ArrayList TipoConceptoIVA = new ArrayList();
	private ArrayList ImporteConceptoIVA = new ArrayList();
	private ArrayList ObservacionesIVA = new ArrayList();

	// Refacturaciones
	private ArrayList TipoRefacturaciones = new ArrayList();
	private ArrayList ImporteConceptoIEIVARefacturaciones = new ArrayList();
	private ArrayList ObservacionesIEIVARefacturaciones = new ArrayList();
	private ArrayList ImporteConceptoIVARefacturaciones = new ArrayList();
	private ArrayList ObservacionesIVARefacturaciones = new ArrayList();
	private ArrayList RFechaDesdeRefacturaciones = new ArrayList();
	private ArrayList RFechaHastaRefacturaciones = new ArrayList();
	private ArrayList RConsumoRefacturaciones = new ArrayList();
	private ArrayList ImporteTotalRefacturaciones = new ArrayList();
	private ArrayList ObservacionesRefacturaciones = new ArrayList();

	// Totales
	private ArrayList TotalPeriodosExcesoPotencia = new ArrayList();
	private ArrayList TotalTerminosEA = new ArrayList();
	private ArrayList TotalPeriodosEA = new ArrayList();
	private ArrayList TotalTerminosER = new ArrayList();
	private ArrayList TotalPeriodosER = new ArrayList();
	private ArrayList TotalTerminosRE = new ArrayList();
	private ArrayList TotalTerminosIVA = new ArrayList();
	private ArrayList TotalTerminosIVAReducido = new ArrayList();
	private ArrayList TotalMedidas = new ArrayList();

	public static List<FacdisTarifaMagnitudDTO> listaTodos;
	
	// objeto para manejar ficheros
	TratarFicheros trataFichero = new TratarFicheros();

	public static void main(String[] args) {

		controlM_ExitCode = 0;

		BasicConfigurator.configure();

		try {
			logger.info("Inicio proceso CARGA DE FACTURAS ATR (FACDIS)");

			if (args.length < 3) {
				// Número de argumentos incorrectos
				logger.error("Es necesario incluir el nombre del fichero de configuracion como parametro");
				throw new Exception(
						"NO EXISTEN FICHEROS DE PROPIEDADES PASADOS COMO PARAMETROS");
			}
			if (TratarFicheros.existeFichero(args[1])) {
				// Cambiamos el fichero de propiedades del log
				PropertyConfigurator.configure(args[1]);
			} else {
				logger.error("No se encuentra el fichero de propiedades de log");
			}

			sqlMap = PropiedadesAplicacion.LeerFicherosProperties(args[0], args[1], args[2]);

			listaTodos = recuperaTodosDatosTarifaMagnitud();
			
			try {
				todoOK = false;

				LogProceso.Cabecera(nombreServicio);
				
				// Se va a buscar ficheros al ftp si es necesario
				Batch_FACDIS_CargaFacturas cargaFacturas = new Batch_FACDIS_CargaFacturas();
				cargaFacturas.obtenerFicheros();
				cargaFacturas.cargaDatos();

				if (hayDatos == true) { 
					Ajustes.actualizarDatos(sqlMap);
				} else {
					logger.debug("NO SE EJECUTARÁN LOS AJUSTES, NO HAY FICHEROS PARA PROCESAR.");
				}

				logger.info("FIN DEL PROCESO de CARGA DE FACTURAS ATR.");
				todoOK = true;

			} catch (Exception e) {
				logger.debug("Error al procesar envio al portal en motor:"
						+ e.getMessage());
				controlM_ExitCode = 1;
			}
		} catch (Exception e) {
			logger.error("Error al cargar los ficheros de propiedades motor:"
					+ e.getMessage());
			controlM_ExitCode = 1;
		} finally {
			// Escribimos la fecha de la ejecucion correcta
			try {
				if (sqlMap != null) {
					Date fechaHoy = new Date();
					SimpleDateFormat formato = new SimpleDateFormat(
							"dd/MM/yyyy HH:mm:ss");
					String fechaejecucion = formato.format(fechaHoy);
					EjecucionesServicios.setEjecucion(sqlMap,
							"Batch_FACDIS_CargaFacturas", fechaejecucion,
							PropiedadesAplicacion.getRutaLog(), todoOK,
							"FACDIS");
				}
			} catch (Exception ex) {
				logger.error("No se ha guardado la fecha de ejecucion en la base de datos: "
						+ ex.getMessage());
				controlM_ExitCode = 1;
			}

			// Escribimos el resumen del proceso
			EscribirResultado();

			// Escribimos el pie que muestra el tiempo que ha tardado el proceso
			LogProceso.Pie();
			logger.info("FINALIZACIÓN DEL PROCESO DE CARGA DE FACTURAS ATR (FACDIS)");

			// Cerramos todas las conexiones del pool
			if (sqlMap != null) {
				SimpleDataSource ds = (SimpleDataSource) sqlMap.getDataSource();
				ds.forceCloseAll();
			}
			System.exit(controlM_ExitCode);
		}
	}
	
	public static List <FacdisTarifaMagnitudDTO> recuperaTodosDatosTarifaMagnitud() throws Exception {
		FacdisTarifaMagnitudDAO tarifaMagnitud=new FacdisTarifaMagnitudDAO();
		List<FacdisTarifaMagnitudDTO> datos = null;
		try {
			datos = tarifaMagnitud.recuperarTodosTarifaMagnitud(sqlMap);
		} catch (Exception e) {
			logger.error("recuperaTodosDatosTarifaMagnitud--FALLO AL RECUPERAR DATOS. "	+ e.getMessage());
			throw e;
		}
		return datos;
	}
	
	public Batch_FACDIS_CargaFacturas() {
		try {
			this.directorioProcesado = PropiedadesAplicacion.getInstance().getPropiedad("DIRECTORIOPROCESADO");
			this.directorioPendiente = PropiedadesAplicacion.getInstance().getPropiedad("DIRECTORIOPENDIENTE");
			this.directorioDuplicadas = PropiedadesAplicacion.getInstance().getPropiedad("DIRECTORIODUPLICADAS");
			this.directorioErrores = PropiedadesAplicacion.getInstance().getPropiedad("DIRECTORIOERRORES");
			this.directorioZipProcesado = PropiedadesAplicacion.getInstance().getPropiedad("DIRECTORIOZIPPROCESADO");
			this.directorioZipErrores = PropiedadesAplicacion.getInstance().getPropiedad("DIRECTORIOZIPERRORES");
			this.directorioEsquemas = PropiedadesAplicacion.getInstance().getPropiedad("DIRECTORIOESQUEMAS");
			this.blnFTP = PropiedadesAplicacion.getInstance().getPropiedad("FTP");
			this.ipFTP = PropiedadesAplicacion.getInstance().getPropiedad("IP_FTP");
			this.Usuario_FTP = PropiedadesAplicacion.getInstance().getPropiedad("USUARIO_FTP");
			this.Password_FTP = PropiedadesAplicacion.getInstance().getPropiedad("CLAVE_FTP");
			this.protocoloSeguro_FTP = PropiedadesAplicacion.getInstance().getPropiedad("PROTOCOLO_SEGURO");
			this.tipoTransferencia_FTP = PropiedadesAplicacion.getInstance().getPropiedad("TIPO_TRANSFERENCIA");
			this.Directorio_FTP = PropiedadesAplicacion.getInstance().getPropiedad("DIRECTORIO_FTP");

			this.protocoloSeguro_FTP_DUOS = PropiedadesAplicacion.getInstance().getPropiedad("PROTOCOLO_SEGURO_DUOS");
			this.tipoTransferencia_FTP_DUOS = PropiedadesAplicacion.getInstance().getPropiedad("TIPO_TRANSFERENCIA_DUOS");
			this.directorioPendienteDUOS = PropiedadesAplicacion.getInstance().getPropiedad("DIRECTORIOPENDIENTEDUOS");
			this.directorioErroresDUOS = PropiedadesAplicacion.getInstance().getPropiedad("DIRECTORIOERRORESDUOS");
			this.directorioEnviosDUOS = PropiedadesAplicacion.getInstance().getPropiedad("DIRECTORIOENVIOSDUOS");
			this.enviarDUOS = PropiedadesAplicacion.getInstance().getPropiedad("ENVIAR_DUOS");
			this.ipFTP_DUOS = PropiedadesAplicacion.getInstance().getPropiedad("IP_FTP_DUOS");
			this.Usuario_FTP_DUOS = PropiedadesAplicacion.getInstance().getPropiedad("USUARIO_FTP_DUOS");
			this.Password_FTP_DUOS =  PropiedadesAplicacion.getInstance().getPropiedad("CLAVE_FTP_DUOS");
			this.DirecotrioFTP_DUOS = PropiedadesAplicacion.getInstance().getPropiedad("DIRECTORIO_FTP_DUOS");
		} catch (Exception e) {
			logger.error("ERROR Cargando properties " + e.getMessage());
		}

		logger.info("COMIENZA CARGA FACTURAS ATR");
		// Despues de leer el ini creamos las carpetas de procesamiento
		Calendar now = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		fechaDia = formatter.format(now.getTime());
		// Creamos la carpeta y renombramos los directorios de trabajo
		File diaDirectorioProcesado = new File(directorioProcesado + "/" + fechaDia + "/");
		File diaDirectorioErrores = new File(directorioErrores + "/" + fechaDia + "/");
		File diaDirectorioDuplicadas = new File(directorioDuplicadas + "/" + fechaDia + "/");
		File diaDirectorioZipErrores = new File(directorioZipErrores + "/" + fechaDia + "/");
		File diaDirectorioZipProcesado = new File(directorioZipProcesado + "/" + fechaDia + "/");

		if (diaDirectorioProcesado.mkdir()) {
			logger.debug("Se ha creado el directorio para el procesado " + directorioProcesado + "/" + fechaDia + "/");
		}
		directorioProcesado = directorioProcesado + "/" + fechaDia + "/";

		if (diaDirectorioErrores.mkdir()) {
			logger.debug("Se ha creado el directorio para el procesado " + directorioErrores + "/" + fechaDia + "/");
		}
		directorioErrores = directorioErrores + "/" + fechaDia + "/";

		if (diaDirectorioDuplicadas.mkdir()) {
			logger.debug("Se ha creado el directorio para el procesado " + diaDirectorioDuplicadas + "/" + fechaDia + "/");
		}
		directorioDuplicadas = directorioDuplicadas + "/" + fechaDia + "/";

		if (diaDirectorioZipErrores.mkdir()) {
			logger.debug("Se ha creado el directorio para el procesado " + directorioZipErrores + "/" + fechaDia + "/");
		}
		directorioZipErrores = directorioZipErrores + "/" + fechaDia + "/";

		if (diaDirectorioZipProcesado.mkdir()) {
			logger.debug("Se ha creado el directorio para el procesado"	+ directorioZipProcesado + "/" + fechaDia + "/");
		}
		directorioZipProcesado = directorioZipProcesado + "/" + fechaDia + "/";
	}

	private void obtenerFicheros() {
		if (blnFTP.equals("S")) {
			try {
				UtilFTP ftp = new UtilFTP(this.ipFTP, this.Usuario_FTP,
						this.Password_FTP, this.protocoloSeguro_FTP,
						this.tipoTransferencia_FTP);
				ftp.recuperarDirectorioFTP(this.Directorio_FTP, this.directorioPendiente);
			} catch (Exception e) {
				logger.error("Error en la configuración ftp remoto: " + e.getMessage());
			}
		}
	}

	private void cargaDatos() {

		boolean bEsquema = true;
		String txtMensa = "";

		File dirPrueba = new File(directorioPendiente);
		if (!dirPrueba.isDirectory()) {
			logger.error("EL DIRECTORIO NO ES VÁLIDO");
		} else {
			// Comprobamos si existen zips, en ese daso primero hacemos la
			// descompresión
			fnDescromprimirZips(directorioPendiente);
			// Sólo procesamos los XML
			File[] ficheros = new File(directorioPendiente)
					.listFiles(new FilenameFilter() {
						public boolean accept(File dir, String name) {
							String Extension = name.substring(
									name.length() - 3, name.length())
									.toUpperCase();
							if ((Extension.equals("XML"))) {
								return true;

							} else
								return false;
						}
					});

			if (ficheros.length == 0) {
				logger.info("NO HAY FICHEROS PARA PROCESAR");
				hayDatos = false;
			} else {
				if (enviarDUOS.equals("S")) {
					logger.info("DUOS - INICIO DE CREACIÓN Y ENVÍO DE FICHERO ZIP");
					
					//incluimos en un único ZIP todos los xml a tratar y se le entregamos a DUOS
					String ficheroZIPduos = "FACTURAS_ATR_" + Util.fechaHoyH24() + ".ZIP";
					List listaFicheros = Arrays.asList(ficheros);
					String numFicheros = "";
					
					try {
						//creamos el ZIP en el pendiente de DUOS
						numFicheros = Util.generarZip (directorioPendiente, listaFicheros, directorioPendienteDUOS, ficheroZIPduos);
						//enviamos el ZIP al FTP de DUOS
						if (!numFicheros.equals("0")) {
							//realizamos el envío del ZIP a DUOS vía FTP
							logger.info("Enviamos a DUOS el ZIP " + ficheroZIPduos + " con " + numFicheros + " ficheros.");
							
							UtilFTP ftp = new UtilFTP(this.ipFTP_DUOS, this.Usuario_FTP_DUOS,
									this.Password_FTP_DUOS, this.protocoloSeguro_FTP_DUOS,
									this.tipoTransferencia_FTP_DUOS);
							
							boolean envio = ftp.enviarFTP(directorioPendienteDUOS + "/" + ficheroZIPduos, DirecotrioFTP_DUOS, ficheroZIPduos);
							
							//boolean envio = Util.subirFTP(directorioPendienteDUOS + "/" + ficheroZIPduos, ficheroZIPduos, ipFTP_DUOS, Usuario_FTP_DUOS, Password_FTP_DUOS, DirecotrioFTP_DUOS);
							
							if (envio) {
								logger.info("DUOS - Fichero enviado correctamente a DUOS. Movemos el fichero original a la carpeta de enviados");
								//movemos el fichero original a la carpeta de enviados
								
			                    //fichero origen
								File ficheroOrigen = new File(directorioPendienteDUOS + "/" + ficheroZIPduos);
								//creamos el fichero destino
								File ficheroDestino = new File(directorioEnviosDUOS + "/" + ficheroZIPduos);
			                    //copiamos el original en el destino
			                    boolean b = Util.copyFile(ficheroOrigen, ficheroDestino);
			                    //borramos el original si se ha copiado correctamente
			                    if (b) ficheroOrigen.delete();
			                    else logger.error("Error al mover el fichero " + ficheroZIPduos + " al directorio " + directorioEnviosDUOS);
			                    
							} else {
								logger.info("DUOS - Fichero NO enviado a DUOS. Movemos el fichero original a la carpeta de errores");
								//movemos el fichero original a la carpeta de errores
								
								//fichero origen
								File ficheroOrigen = new File(directorioPendienteDUOS + "/" + ficheroZIPduos);
								//creamos el fichero destino
								File ficheroDestino = new File(directorioErroresDUOS + "/" + ficheroZIPduos);
			                    //copiamos el original en el destino
			                    boolean b = Util.copyFile(ficheroOrigen, ficheroDestino);
			                    //borramos el original si se ha copiado correctamente
			                    if (b) ficheroOrigen.delete();
			                    else logger.error("Error al mover el fichero " + ficheroZIPduos + " al directorio " + directorioErroresDUOS);
							}
						}
					} catch (Exception e) {
						logger.error("Error " + e.getMessage());
					} 
					logger.info("DUOS - FIN DE CREACIÓN Y ENVÍO DE FICHERO ZIP");
				}
			}

			// CARGA
			for (int i = 0; i < ficheros.length; i++) { // Array de ficheros
				totalFacturas = ficheros.length;
				sNombreFichero = ficheros[i].getName();
				logger.info("*XML[" + i + "]:" + sNombreFichero);
				inicializaVariables();
				if ((sNombreFichero.length() > 17)
						&& (sNombreFichero.substring(
								sNombreFichero.length() - 18,
								sNombreFichero.length() - 17).equals("."))
						&& (sNombreFichero.substring(
								sNombreFichero.length() - 4,
								sNombreFichero.length() - 3).equals("."))) {
					ticketEnergetico = sNombreFichero.substring(
							sNombreFichero.length() - 17,
							sNombreFichero.length() - 4);
					logger.debug("ticketEnergetico " + ticketEnergetico);
					if (ticketEnergetico.charAt(0) == 'V'
							|| ticketEnergetico.charAt(0) == 'F') {
						logger.debug("Cumple condiciones para ticket Energetico");
					} else {
						ticketEnergetico = "";
					}
				}

				try {
					// Asignación de xml a objeto java con jaxb////////////
					JAXBContext jc = JAXBContext.newInstance("com.eon.jaxb");
					Unmarshaller u = jc.createUnmarshaller();
					MensajeFacturacion facturaJaxb = (MensajeFacturacion) u
							.unmarshal(ficheros[i]);

					logger.debug("DOCUMENTO CARGADO del CUPS: " + facturaJaxb.getCabecera().getCodigo());

					// Se obtiene el esquema y se realiza la validación
					trataTipoSolicitud(facturaJaxb.getCabecera()
							.getCodigoDelProceso());

					String XmlDocumentUrl = ficheros[i].getPath();
					String SchemaUrl = directorioEsquemas + "/" + NombreSchema;
					SchemaValidator validator = new SchemaValidator();

					txtMensa = validator.validateSchema(SchemaUrl,
							XmlDocumentUrl);
					bEsquema = txtMensa.equals("");

					if ((bEsquema)) {
						logger.debug("La validación contra el Schema es válida.");
						String codFiscalFactura;
						// DTO´s
						FacdisFacturaDTO facDis = new FacdisFacturaDTO();
//						FacdisFacturaLecturaDTO facdislecturasDTO = new FacdisFacturaLecturaDTO();
						// DAOS
						FacdisFacturaDAO facturaFacDisDAO = new FacdisFacturaDAO();

						List<FacturaATR> facturas = facturaJaxb.getFacturas()
								.getFacturaATR();

						// OTRASFACTURAS//
						if (facturaJaxb.getFacturas().getOtrasFacturas() != null) {
							List<OtrasFacturas> otrasFacturas = facturaJaxb
									.getFacturas().getOtrasFacturas();

							for (int b = 0; b < otrasFacturas.size(); b++) {

								OtrasFacturas otraFactura = otrasFacturas
										.get(b);
								// datosComunes
								facDis = trataDatosOtrasFacturas(otraFactura);
								codFiscalFactura = otrasFacturas.get(b)
										.getDatosGeneralesOtrasFacturas()
										.getDatosGeneralesFactura()
										.getCodigoFiscalFactura();
								// Cumplimentamos el objeto con los datos de la
								// factura

								// Extraigo COMUNES del XML
								// datos CABECERA
								facDis.setCod_solicitud(facturaJaxb
										.getCabecera().getCodigoDeSolicitud()
										.toString());
								facDis.setId_distribuidora_cne(facturaJaxb
										.getCabecera()
										.getCodigoREEEmpresaEmisora()
										.toString());
								facDis.setId_comercializadora_cne(facturaJaxb
										.getCabecera()
										.getCodigoREEEmpresaDestino()
										.toString());
								facDis.setFecha_solicitud(facturaJaxb
										.getCabecera().getFechaSolicitud()
										.toString().substring(0, 10));
								// datos RegistroFin
								facDis.setFecha_valor(facturaJaxb.getFacturas()
										.getRegistroFin().getFechaValor()
										.toString());
								facDis.setFecha_limite_pago(facturaJaxb
										.getFacturas().getRegistroFin()
										.getFechaLimitePago().toString());
								facDis.setTotal(facturaJaxb.getFacturas()
										.getRegistroFin().getImporteTotal()
										.floatValue());
								facDis.setSaldo_factura(facturaJaxb
										.getFacturas().getRegistroFin()
										.getSaldoTotalFacturacion()
										.floatValue());
								facDis.setSaldo_cobro(facturaJaxb.getFacturas()
										.getRegistroFin().getSaldoTotalCobro()
										.floatValue());
								//otrasFact null en este campo
								if(facDis.getInd_envio_csa().equals("0")){
									facDis.setInd_envio_csa(null);
								}
								
								boolean existeFacDis = verificarExistenciaFactura(
										facDis, codFiscalFactura);
								
								if (!existeFacDis) {
									try {
										// insertamos Factura en BBDD////
										// Se inicia la transacción
										sqlMap.startTransaction();
										facturaFacDisDAO
												.insertarFacturaFacdisDAO(
														sqlMap, facDis);
										insertarDatosConceptosOtras(otraFactura);
										logger.debug("CommitTransaction, Insertado: " + SeqIdFacDis);
										sqlMap.commitTransaction();
										bCorrecto = "0";
									} catch (Exception e) {
										logger.error("Error " + e.getMessage());
										bCorrecto = "1";
									} finally {
										try {
											sqlMap.endTransaction();
										} catch (SQLException e) {
											logger.error("Error al cerrar la transacción." + e);
										}
									}
								}
							}
						}

						///FACTURAS///
						for (int b = 0; b < facturas.size(); b++) {
							codFiscalFactura = facturaJaxb.getFacturas()
									.getFacturaATR().get(b)
									.getDatosGeneralesFacturaATR()
									.getDatosGeneralesFactura()
									.getCodigoFiscalFactura().toString();
							FacturaATR factura = facturas.get(b);
							facDis = formarFacturaFacdis(factura);
							// Cumplimentamos el objeto con los datos de la
							// factura

							// Extraigo COMUNES del XML
							// datos CABECERA
							facDis.setCod_solicitud(facturaJaxb.getCabecera()
									.getCodigoDeSolicitud().toString());
							facDis.setId_distribuidora_cne(facturaJaxb
									.getCabecera().getCodigoREEEmpresaEmisora()
									.toString());
							facDis.setId_comercializadora_cne(facturaJaxb
									.getCabecera().getCodigoREEEmpresaDestino()
									.toString());
							facDis.setFecha_solicitud(facturaJaxb.getCabecera()
									.getFechaSolicitud().toString()
									.substring(0, 10));
							facDis.setFecha_valor(facturaJaxb.getFacturas()
									.getRegistroFin().getFechaValor()
									.toString());
							facDis.setFecha_limite_pago(facturaJaxb
									.getFacturas().getRegistroFin()
									.getFechaLimitePago().toString());
							// datos RegistroFin
							facDis.setTotal(facturaJaxb.getFacturas()
									.getRegistroFin().getImporteTotal()
									.floatValue());
							facDis.setSaldo_factura(facturaJaxb.getFacturas()
									.getRegistroFin()
									.getSaldoTotalFacturacion().floatValue());
							facDis.setSaldo_cobro(facturaJaxb.getFacturas()
									.getRegistroFin().getSaldoTotalCobro()
									.floatValue());
							// otras datos

							boolean existeFacDis = verificarExistenciaFactura(
									facDis, codFiscalFactura);
							if (!existeFacDis) {

								try {
									// insertamos Factura en BBDD////
									// Se inicia la transacción
									sqlMap.startTransaction();

									facturaFacDisDAO.insertarFacturaFacdisDAO(
											sqlMap, facDis);

									// insertamos en tabla FACTURA_LECTURA
									insertarDatosLecturas(factura);
									// insertamos en tabla FACTURA_REFACTURACIÓN
									insertarDatosRefacturacion(factura);

									// Tratamos periodos
									trataDatosPeriodos(factura);
									if (bCorrecto=="0") {
										logger.debug("CommitTransaction, Insertado: " + SeqIdFacDis);
										sqlMap.commitTransaction();
										bCorrecto = "0";
									}
								
								} catch (Exception e) {
									logger.error("Error " + e.getMessage());
									bCorrecto = "1";
								} finally {
									try {
										sqlMap.endTransaction();
									} catch (SQLException e) {
										logger.error("Error al cerrar la transacción." + e);
									}
								}
							}
						}

					} else {
						logger.info("LA FACTURA NO VALIDA CONTRA EL ESQUEMA");
						bCorrecto = "1";
					}

				} catch (Exception e) {
					logger.error("ERROR GENERAL DE LECTURA " + e.getMessage());
					bCorrecto="1";
				}
				////MOVEMOS FACTURA AL DIRECTORIO////
				try {
					switch (Integer.parseInt(bCorrecto)) {
						case 0: {
							trataFichero.moverFicheros(ficheros[i].getAbsolutePath(),
									new File(directorioProcesado + "/"
											+ ficheros[i].getName())
											.getAbsolutePath());
							totalProcesadas = totalProcesadas + 1;
							break;
						}
						case 1: {
							trataFichero.moverFicheros(ficheros[i].getAbsolutePath(),
									new File(directorioErrores + "/"
											+ ficheros[i].getName())
											.getAbsolutePath());
							totalErroneas = totalErroneas + 1;
							break;
						}
						case 2: {
							trataFichero.moverFicheros(ficheros[i].getAbsolutePath(),
									new File(directorioDuplicadas + "/"
											+ ficheros[i].getName())
											.getAbsolutePath());
							totalDuplicadas = totalDuplicadas + 1;
							break;
						}
					}
				} catch (Exception e) {
					logger.error("FALLO AL MOVER EL FICHERO. "
							+ e.getMessage());
				}
			}
		}
	}

	private void fnDescromprimirZips(String directorioPendiente) {
		// Listamos los fichero que sean zips
		// Filtro que devuelve true en el caso de tener extensión zip or ZIP
		File[] FilficherosZip = new File(directorioPendiente)
				.listFiles(new FilenameFilter() {
					public boolean accept(File dir, String name) {
						String Extension = name.substring(name.length() - 3,
								name.length()).toUpperCase();
						if ((Extension.equals("ZIP"))) {
							return true;

						} else
							return false;
					}
				});
		String[] ficherosZip = new String[FilficherosZip.length];
		for (int l = 0; l < FilficherosZip.length; l++) {
			ficherosZip[l] = FilficherosZip[l].getName();
		}

		String outfile = "";
		File Filetmp = null;
		ZipInputStream zin = null;
		FileOutputStream fout;
		ZipEntry ze;
		int length;
		byte[] outBuf = new byte[512];

		TratarFicheros trataFichero = new TratarFicheros();

		for (int i = 0; i < ficherosZip.length; i++) {
			try {
				zin = new ZipInputStream(new BufferedInputStream(
						new FileInputStream(directorioPendiente + "/"
								+ ficherosZip[i])));

				Filetmp = new File(directorioPendiente + "/" + ficherosZip[i]);

				while ((ze = zin.getNextEntry()) != null) {
					outfile = directorioPendiente
							+ "/"
							+ ze.getName().split("/")[(ze.getName().split("/")).length - 1];
					String Extension = outfile.substring(outfile.length() - 3,
							outfile.length()).toUpperCase();
					if ((Extension.equals("XML"))) {
						fout = new FileOutputStream(outfile);
						while ((length = zin.read(outBuf)) != -1) {
							fout.write(outBuf, 0, length);
						}
						fout.close();
					}
					logger.debug("Fichero descomprimido correctamente.");
				}
				zin.close();
				// Lo muevo al directorio

				trataFichero.desplazar(Filetmp, new File(directorioZipProcesado
						+ "/" + Filetmp.getName()));
			} catch (Exception e) {
				logger.error("Error Descomprimiento el fichero ZIP.["
						+ ficherosZip[i] + "-->" + outfile + "]:"
						+ e.getMessage());
				try {
					// Se hace este paso para cerrar zin y liberar el fichero
					// para poder desplazarlo
					if (zin != null) {
						zin.close();
					}
				} catch (Exception ex) {
					// No hace falta introducir ningun mensaje
				}
				// Movemos el fichero a la carpeta de zips erroneos a la carpeta
				// del dia
				trataFichero.desplazar(Filetmp, new File(directorioZipErrores + "/" + Filetmp.getName()));
			}
		}
	}

	// Impuesto Electrico
	public void trataDatosImpuestoElectrico(ImpuestoElectrico impuesto) {
		BaseImponible.add(impuesto.getBaseImponible());
		Coeficiente.add(impuesto.getCoeficiente());
		Porcentaje.add(impuesto.getPorcentaje());
		Importe.add(impuesto.getImporte());
	}

	// Alquileres
	public void trataDatosAlquileres(Alquileres alquiler) {
		ImporteFacturacionAlquileres.add(alquiler.getImporteFacturacionAlquileres());
	}

	public void trataTipoSolicitud(String codigoDelProceso) {
		if (codigoDelProceso.equals("F1")) {
			NombreSchema = "Facturacion.xsd";
		}
	}

	public boolean trataDatosRefacturaciones(FacturaATR refacFactura) {
		// Refacturaciones
		FacdisFacturaRefacturacionDTO facdisrefacturacionDto = new FacdisFacturaRefacturacionDTO();
		Refacturaciones refacturaciones = refacFactura.getRefacturaciones();
		List<Refacturacion> terminos = refacturaciones.getRefacturacion();
		List<ConceptoIVA> concepIva = refacFactura.getConceptoIVA();
		List<ConceptoIEIVA> concepIeIva = refacFactura.getConceptoIEIVA();
		if (concepIeIva.size() > 0 && concepIva.size() > 0) {
			// Si tiene conceptoIVA no debe tener conceptoIEIVA y viceversa, el
			// xsd lo permite pero es erróneo.
			logger.debug("Error 1 en la Refacturación, factura errónea");
			return false;
		} else {
			// List Terminos=Refacturaciones.getChildren("Refacturacion",
			// refacFactura.getRefacturaciones());
			TotalTerminosRE.add(String.valueOf(terminos.size()));
			if ((concepIeIva.size() + concepIva.size()) != terminos.size()) {
				// Si tiene conceptoIVA no debe tener conceptoIEIVA y viceversa,
				// el xsd lo permite pero es erróneo.
				logger.debug("Error 2 en la Refacturación, factura errónea");
				return false;
			} else {
				for (int i = 0; i < terminos.size(); i++) {
					Refacturacion refacturacion = terminos.get(i);
					TipoRefacturaciones.add(refacturacion.getTipo());

					for (int num = 0; num < concepIeIva.size(); num++) {
						if (refacturacion.getTipo().equals(
								TipoConceptoIEIVA.get(num))) {
							ImporteConceptoIEIVARefacturaciones
									.add(ImporteConceptoIEIVA.get(num));
							ObservacionesIEIVARefacturaciones
									.add(ObservacionesIEIVA.get(num));
							ImporteConceptoIVARefacturaciones.add(0);
							ObservacionesIVARefacturaciones.add("");
						}
					}
					for (int num = 0; num < concepIva.size(); num++) {
						if (refacturacion.getTipo().equals(
								TipoConceptoIVA.get(num))) {
							ImporteConceptoIEIVARefacturaciones.add(0);
							ObservacionesIEIVARefacturaciones.add("");
							ImporteConceptoIVARefacturaciones
									.add(ImporteConceptoIVA.get(num));
							ObservacionesIVARefacturaciones
									.add(ObservacionesIVA.get(num));
						}
					}

					RFechaDesdeRefacturaciones.add(refacturacion.getRFechaDesde());
					RFechaHastaRefacturaciones.add(refacturacion.getRFechaHasta());
					if (refacturacion.getRConsumo() != null) {
						RConsumoRefacturaciones.add(refacturacion.getRConsumo());
					} else {
						RConsumoRefacturaciones.add(0);
					}
					if (refacturacion.getImporteTotal() != null) {
						ImporteTotalRefacturaciones.add(refacturacion.getImporteTotal());
					} else {
						ImporteTotalRefacturaciones.add(0);
					}
					if (refacturacion.getObservaciones() != null) {
						ObservacionesRefacturaciones.add(refacturacion.getObservaciones());
					} else {
						ObservacionesRefacturaciones.add("");
					}
				}
				return true;
			}
		}
	}

	public void recuperaIdFatrFacdis() throws SQLException {
		int intSecuencia = (Integer) sqlMap.queryForObject("recuperaIdFatrFacdis", null);

		if (intSecuencia == 0) {
			logger.error("NO HAY VALOR PARA LA SECUENCIA GRUPO_FACTURAFACDIS_ATR.");
		} else {
			SeqIdFacDis = intSecuencia;
		}
	}

	public void insertarDatosLecturas(FacturaATR factura) throws Exception {
		
		FacdisFacturaLecturaDAO facdislecturasDAO = new FacdisFacturaLecturaDAO();
		FacdisFacturaLecturaDTO facdisLecturas = new FacdisFacturaLecturaDTO();
		facdisLecturas.setId_fatr(SeqIdFacDis);
		facdisLecturas.setFecha_factura(factura.getDatosGeneralesFacturaATR()
				.getDatosGeneralesFactura().getFechaFactura().toString());
		List<Medidas> medidas = factura.getMedidas();
		for (int m = 0; m < medidas.size(); m++) {
			// APARATOS
			Medidas laMedida = medidas.get(m);
			if (laMedida.getAparato() != null) {
				List<Aparato> losAparatos = laMedida.getAparato();
				// laFactura.setCupspm(laMedida.getCodUnificadoPuntoSuministro());
				// String CUPSPM=CodUnificadoPuntoSuministro.get(m).toString();
				// numMed=numAp;
				for (int a = 0; a < losAparatos.size(); a++) {
					Aparato elAparato = losAparatos.get(a);

					facdisLecturas.setTipoaparato(elAparato.getTipo()
							.toString());
					facdisLecturas.setMarca(elAparato.getMarca());
					facdisLecturas.setNumserie(elAparato.getNumeroSerie());
					facdisLecturas.setCodigodh(elAparato.getCodigoDH());

					// facdisLecturas.setId_fatr(SeqIdFacturaAtr);

					// num=numInt;
					// Integradores
					if (elAparato.getIntegrador() != null) {
						List<Integrador> losIntegradores = elAparato
								.getIntegrador();
						for (int j = 0; j < losIntegradores.size(); j++) {
							Integrador elIntegrador = losIntegradores.get(j);

							// String strPeriodo = obtenerPeriodo(sqlMap,
							// CodigoPeriodo.get(j).toString(),0,"LECTURAS");

							facdisLecturas.setId_magnitud_cne(elIntegrador
									.getMagnitud().value());

							facdisLecturas.setId_periodo_cne(elIntegrador
									.getCodigoPeriodo());

							facdisLecturas.setFecha_fin(elIntegrador
									.getLecturaHasta().getFechaHora()
									.toString().substring(0, 10));

							// lecturas.setCupsPm(CUPSPM);
							facdisLecturas
									.setCtemult(elIntegrador
											.getConstanteMultiplicadora()
											.doubleValue());
							facdisLecturas.setNruedasdec(elIntegrador
									.getNumeroRuedasDecimales().toString());
							facdisLecturas.setNruedasent(elIntegrador
									.getNumeroRuedasEnteras().toString());
							facdisLecturas.setConsumocalc(elIntegrador
									.getConsumoCalculado().doubleValue());
							facdisLecturas.setId_procedeini_cne(elIntegrador
									.getLecturaDesde().getProcedencia());
							if(elIntegrador
									.getLecturaDesde().getLectura()!=null && !elIntegrador
									.getLecturaDesde().getLectura().equals("")){
							facdisLecturas.setLecturaini(elIntegrador
									.getLecturaDesde().getLectura().doubleValue());
							}
							facdisLecturas.setId_procedefin_cne(elIntegrador
									.getLecturaHasta().getProcedencia());
							facdisLecturas.setFecha_ini(elIntegrador
									.getLecturaDesde().getFechaHora()
									.toString().substring(0, 10));
							facdisLecturas.setFecha_fin(elIntegrador
									.getLecturaHasta().getFechaHora()
									.toString().substring(0, 10));
							if(elIntegrador
									.getLecturaHasta().getLectura()!=null && !elIntegrador
									.getLecturaHasta().getLectura().equals("")){
							facdisLecturas.setLecturafin(elIntegrador
									.getLecturaHasta().getLectura().doubleValue());
							}

							if (elIntegrador.getAjuste() != null) {
								facdisLecturas
										.setId_notivoajuste_cne(elIntegrador
												.getAjuste()
												.getCodigoMotivoAjuste());
								facdisLecturas
										.setAjuste_integrador(elIntegrador
												.getAjuste()
												.getAjustePorIntegrador()
												.doubleValue());
							} else {
								facdisLecturas.setId_notivoajuste_cne("");
								facdisLecturas.setAjuste_integrador(0);
							}

							if (elIntegrador.getAnomalia() != null) {
								facdisLecturas.setId_anomalia_cne(elIntegrador
										.getAnomalia().getTipoAnomalia());

							} else {
								facdisLecturas.setId_anomalia_cne("");

							}

							if (elIntegrador.getFechaHoraMaximetro() != null) {
								facdisLecturas.setF_maximetro(elIntegrador
										.getFechaHoraMaximetro().toString()
										.substring(0, 10));
							} else {
								facdisLecturas.setF_maximetro(null);
							}

							try {
								facdislecturasDAO.insertarLecturasDAO(sqlMap,
										facdisLecturas);
								bCorrecto = "0";
							} catch (Exception e) {
								logger.error("Error " + e.getMessage());
								throw e;
							}
						}
					}
				}
			}
		}
	}

	// OTRAS FACTURAS//NUEVO
	public FacdisFacturaDTO trataDatosOtrasFacturas(OtrasFacturas otraFacturas) {
		int idTipoFactura;
		TipoIVAIGIC elIva = otraFacturas.getIVA();
		TipoIVAIGIC elIvaR = otraFacturas.getIVAIGICReducido();
		
		FacdisFacturaDTO laFactura = new FacdisFacturaDTO();

		Calendar cal = Calendar.getInstance();
		cupsPm=null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			recuperaIdFatrFacdis();
		} catch (SQLException e) {
			logger.error("Error " + e.getMessage());
		}

		String tipoFactura = otraFacturas.getDatosGeneralesOtrasFacturas()
				.getDatosGeneralesFactura().getTipoFactura();
		List<Concepto> concepto = null;
		if (otraFacturas.getConcepto().size() > 0) {
			concepto = otraFacturas.getConcepto();
		}

		for (int c = 0; c < concepto.size(); c++) {
			Concepto elConcepto = concepto.get(c);
			laFactura.setId_fatr(SeqIdFacDis);
			laFactura.setNum_factura(otraFacturas
					.getDatosGeneralesOtrasFacturas()
					.getDatosGeneralesFactura().getNumeroFactura());
			laFactura.setCups(otraFacturas.getDatosGeneralesOtrasFacturas()
					.getDireccionSuministro().getCUPS());
			laFactura.setContrato(String.valueOf(otraFacturas
					.getDatosGeneralesOtrasFacturas().getContrato()));
			if (otraFacturas.getDatosGeneralesOtrasFacturas()
					.getDatosGeneralesFactura()
					.getIndicativoFacturaRectificadora() != null) {
				laFactura.setId_tipofactrec_cne(otraFacturas
						.getDatosGeneralesOtrasFacturas()
						.getDatosGeneralesFactura()
						.getIndicativoFacturaRectificadora().toString());
			}
			if (otraFacturas.getDatosGeneralesOtrasFacturas()
					.getDatosGeneralesFactura().getNumeroFacturaRectificada() != null) {
				laFactura.setNum_factura_rectificada(otraFacturas
						.getDatosGeneralesOtrasFacturas()
						.getDatosGeneralesFactura()
						.getNumeroFacturaRectificada().toString());
			}
			laFactura.setObservaciones(otraFacturas
					.getDatosGeneralesOtrasFacturas()
					.getDatosGeneralesFactura().getObservaciones());
			laFactura.setId_tipofactura_cne(otraFacturas
					.getDatosGeneralesOtrasFacturas()
					.getDatosGeneralesFactura().getTipoFactura().toString());
			laFactura.setId_tipo_identif_cne(otraFacturas
					.getDatosGeneralesOtrasFacturas().getCliente()
					.getTipoCIFNIF().value());
			laFactura.setCif(otraFacturas.getDatosGeneralesOtrasFacturas()
					.getCliente().getIdentificador());
			laFactura.setId_tipo_identif_cne(otraFacturas
					.getDatosGeneralesOtrasFacturas().getCliente()
					.getTipoCIFNIF().value());
			laFactura.setId_tipofactper_cne("1");
			laFactura.setDir_suministro(otraFacturas
					.getDatosGeneralesOtrasFacturas().getDireccionSuministro()
					.getDirSuministro());
			laFactura.setMunicipio((int) otraFacturas
					.getDatosGeneralesOtrasFacturas().getDireccionSuministro()
					.getMunicipio());
			laFactura.setTipo_moneda(otraFacturas
					.getDatosGeneralesOtrasFacturas()
					.getDatosGeneralesFactura().getTipoMoneda());
			laFactura.setFecha_boe(otraFacturas
					.getDatosGeneralesOtrasFacturas().getFechaBOE().toString());
			
			laFactura.setCupspm(cupsPm);

			// IVA
			if (elIva != null) {
				laFactura.setIvaigic_n(elIva.getImporte().floatValue());
				laFactura.setPct_ivaigic_n(elIva.getPorcentaje().floatValue());
				laFactura.setBase_ivaigic_n(elIva.getBaseImponible()
						.floatValue());
			}
			// IVAR
			if (elIvaR != null) {
				laFactura.setIvaigic_r(elIvaR.getImporte().floatValue());
				laFactura.setPct_ivaigic_r(elIvaR.getPorcentaje().floatValue());
				laFactura.setBase_ivaigic_r(elIvaR.getBaseImponible()
						.floatValue());
			}
			// }
		}

		String nombreFicheroDocumento = "/" + fechaDia + "/" + sNombreFichero;
		laFactura.setDocumento(nombreFicheroDocumento.toString().replaceAll(
				"\'", "\'\'"));
		laFactura.setTicket_energetico(ticketEnergetico);
		laFactura.setValidado(null);
		laFactura.setFecha_validado(null);

		// fechas del xml
		laFactura.setFecha_boe(otraFacturas.getDatosGeneralesOtrasFacturas()
				.getFechaBOE().toString());
		laFactura.setFecha_factura(otraFacturas
				.getDatosGeneralesOtrasFacturas().getDatosGeneralesFactura()
				.getFechaFactura().toString());
		laFactura.setFecha_grabacion(sdf.format(cal.getTime()));

		return laFactura;
	}

	public void trataDatosPeriodos(FacturaATR factura) throws Exception {
		FacdisFacturaPeriodosDTO facdisPeriodos = new FacdisFacturaPeriodosDTO();

		facdisPeriodos.setId_fatr(SeqIdFacDis);
		facdisPeriodos.setFecha_factura(factura.getDatosGeneralesFacturaATR()
				.getDatosGeneralesFactura().getFechaFactura().toString()
				.substring(0, 10));

		// Potencia Máxima (PM)
		if (factura.getPotencia().getTerminoPotencia() != null && bCorrecto=="0") {
			trataDatosPotenciaMaxima(factura, facdisPeriodos);
		}
		// EXCESO POTENCIA
		if (factura.getExcesoPotencia() != null && bCorrecto=="0") {
			trataDatosExcesoPotencia(factura, facdisPeriodos);
		}
		// ENERGIA ACTIVA
		if (factura.getEnergiaActiva() != null && bCorrecto=="0") {
			trataDatosEnergiaActiva(factura, facdisPeriodos);
		}
		// ENERGIA REACTIVA
		if (factura.getEnergiaReactiva() != null && bCorrecto=="0") {
			trataDatosEnergiaReactiva(factura, facdisPeriodos);
		}
	}

	public void trataDatosPotenciaMaxima(FacturaATR factura, FacdisFacturaPeriodosDTO facdisPeriodos)  throws Exception {
		Potencia pM = factura.getPotencia();
		List<TerminoPotencia> terminosPotencia = pM.getTerminoPotencia();
		
		try {
		
			// Se llama a la función
			List<FacdisFacturaPeriodosDTO> periodos = trataDatosEnergia("PM", factura, terminosPotencia);
	
			for (int i = 0; i < periodos.size(); i++) {
				FacdisFacturaPeriodosDTO periodo = periodos.get(i);
				
				periodosDao.insertarPeriodosFacdisDAO(sqlMap, periodo);
				bCorrecto = "0";
			}
		
		} catch (Exception e) {
			logger.error("Error Insertando PM " + e.getMessage());
			throw e;
		}
		
		
	}

	public void trataDatosExcesoPotencia(FacturaATR factura, FacdisFacturaPeriodosDTO facdisPeriodos) throws Exception {
		ExcesoPotencia eX = factura.getExcesoPotencia();
		List<com.eon.jaxb.Facturas.FacturaATR.ExcesoPotencia.Periodo> losPeriodos = eX.getPeriodo();
		
		try {
		
			// Se llama a la función
			List<FacdisFacturaPeriodosDTO> periodos = trataDatosEnergia("EP", factura, losPeriodos);
	
			for (int i = 0; i < periodos.size(); i++) {
				FacdisFacturaPeriodosDTO periodo = periodos.get(i);
				periodosDao.insertarPeriodosFacdisDAO(sqlMap, periodo);
				bCorrecto = "0";
			
			}
			
		} catch (Exception e) {
			logger.error("Error Insertando PM " + e.getMessage());
			throw e;
		}
	}

	public void trataDatosEnergiaActiva(FacturaATR factura, FacdisFacturaPeriodosDTO facdisPeriodos) throws Exception {
		EnergiaActiva eA = factura.getEnergiaActiva();
		List<TerminoEnergiaActiva> terminosEA = eA.getTerminoEnergiaActiva();
		
		try {
		
			if (eA.getImporteTotalEnergiaActiva() != null) {
				importeTotalEA = eA.getImporteTotalEnergiaActiva().doubleValue();
			}
			// Se llama a la función
			List<FacdisFacturaPeriodosDTO> periodos = trataDatosEnergia("AE", factura, terminosEA);
	
			for (int i = 0; i < periodos.size(); i++) {
	
				FacdisFacturaPeriodosDTO periodo = periodos.get(i);
	
				
				periodosDao.insertarPeriodosFacdisDAO(sqlMap, periodo);
				bCorrecto = "0";
			
			}
		
		} catch (Exception e) {
			logger.error("Error Insertando PM " + e.getMessage());
			throw e;
		}	
		
	}

	public void trataDatosEnergiaReactiva(FacturaATR factura, FacdisFacturaPeriodosDTO facdisPeriodos) throws Exception {
		// Energia Reactiva
		EnergiaReactiva eR = factura.getEnergiaReactiva();
		List<TerminoEnergiaReactiva> terminosER = eR.getTerminoEnergiaReactiva();
		
		try {
		
			// Se llama a la función
			List<FacdisFacturaPeriodosDTO> periodos = trataDatosEnergia("R1", factura, terminosER);
	
			for (int i = 0; i < periodos.size(); i++) {
	
				FacdisFacturaPeriodosDTO periodo = periodos.get(i);
			
				periodosDao.insertarPeriodosFacdisDAO(sqlMap, periodo);
				bCorrecto = "0";
			}
			
		} catch (Exception e) {
			logger.error("Error Insertando PM " + e.getMessage());
			throw e;
		}	
	}

	/*
	private static List <FacdisTarifaMagnitudDTO> recuperaDatosTarifaMagnitud (String tarifa, String magnitud, int periodos) throws Exception {
		if(!magnitud.equals(null)){
			magnitudActual=magnitud;
		}
		FacdisTarifaMagnitudDAO tarifaMagnitud=new FacdisTarifaMagnitudDAO();
		List<FacdisTarifaMagnitudDTO> datos = null;
		try {
			datos = tarifaMagnitud.recuperarTarifaMagnitud(sqlMap, tarifa, magnitud, periodos);
		} catch (Exception e) {
			logger.error("recuperaDatosTarifaMagnitud--FALLO AL RECUPERAR DATOS. " + e.getMessage());
			throw e;
		}
		return datos;
	}*/
	
	private static List <FacdisTarifaMagnitudDTO> recuperaDatosTarifaMagnitud (String tarifa, String magnitud, int periodos) throws Exception {
		if(!magnitud.equals(null)){
			magnitudActual=magnitud;
		}
		List<FacdisTarifaMagnitudDTO> datos = new ArrayList<FacdisTarifaMagnitudDTO>();
		
		for (FacdisTarifaMagnitudDTO facdisTarifaMagnitudDTO: listaTodos)	{
			
			if (facdisTarifaMagnitudDTO.getId_tarifa_cne().equals(tarifa) &&
				facdisTarifaMagnitudDTO.getId_magnitud_cne().equals(magnitud) &&
				facdisTarifaMagnitudDTO.getNum_periodos_xml()==periodos ) {
				datos.add(facdisTarifaMagnitudDTO);
			}
		}
		
		return datos;
	}
	
	private static List <FacdisFacturaPeriodosDTO> trataDatosTarifaMagnitud(List<FacdisTarifaMagnitudDTO> datos, FacturaATR factura, List terminos) throws Exception {
		
		ArrayList<FacdisFacturaPeriodosDTO> periodos = new ArrayList<FacdisFacturaPeriodosDTO>();
		List<Periodo> losPeriodos = null;
		
		if (datos!=null && datos.size()>0) {
		
			String magnitud=datos.get(0).getId_magnitud_cne();
			String tarifa=datos.get(0).getId_tarifa_cne();
			//int numObjetos=datos.size();
			FacdisFacturaPeriodosDTO periodo = null;
			if("PM".equals(magnitud)){
				losPeriodos = ((TerminoPotencia) terminos.get(0)).getPeriodo();
				
				for(int a=0;a<losPeriodos.size();a++){
					for(int b=0;b<datos.size();b++){
						if(a==datos.get(b).getPosicion()-1){
							periodo = new FacdisFacturaPeriodosDTO();
							periodo.setId_fatr(SeqIdFacDis);
							periodo.setFecha_factura(factura.getDatosGeneralesFacturaATR()
									.getDatosGeneralesFactura().getFechaFactura()
									.toString().substring(0, 10));
							periodo.setId_magnitud_cne(magnitud);
							
							periodo.setId_periodo_cne(Integer.toString(datos.get(b).getPeriodo()));
							periodo.setPotcontratada(losPeriodos.get(a)
									.getPotenciaContratada().doubleValue());
							periodo.setFecha_ini(((TerminoPotencia) terminos.get(0))
									.getFechaDesde().toString().substring(0, 10));
							periodo.setFecha_fin(((TerminoPotencia) terminos.get(0))
									.getFechaHasta().toString().substring(0, 10));
							periodo.setPotmaxdeman(losPeriodos.get(a)
									.getPotenciaMaxDemandada().doubleValue());
							periodo.setPotafacturar(losPeriodos.get(a)
									.getPotenciaAFacturar().doubleValue());
							periodo.setPrecio(losPeriodos.get(a).getPrecioPotencia()
									.doubleValue());
							periodos.add(periodo);
						}
					}
				}
			}

			if ("AE".equals(magnitud)) {
				List<com.eon.jaxb.Facturas.FacturaATR.EnergiaActiva.TerminoEnergiaActiva.Periodo> losPeriodosAE = ((TerminoEnergiaActiva) terminos
					.get(0)).getPeriodo();
				
				for(int a=0;a<losPeriodosAE.size();a++){
					for(int b=0;b<datos.size();b++){
						if(a==datos.get(b).getPosicion()-1){
							periodo = new FacdisFacturaPeriodosDTO();
							periodo.setId_fatr(SeqIdFacDis);
							periodo.setFecha_factura(factura.getDatosGeneralesFacturaATR().getDatosGeneralesFactura()
								.getFechaFactura().toString().substring(0, 10));
							periodo.setId_magnitud_cne(magnitud);
							periodo.setId_periodo_cne(Integer.toString(datos.get(b).getPeriodo()));
							
							periodo.setFecha_ini(((TerminoEnergiaActiva) terminos.get(0)).getFechaDesde().toString().substring(0, 10));
							periodo.setFecha_fin(((TerminoEnergiaActiva) terminos.get(0)).getFechaHasta().toString().substring(0, 10));
								
							periodo.setValor(losPeriodosAE.get(a).getValorEnergiaActiva().doubleValue());
							periodo.setPrecio(losPeriodosAE.get(a).getPrecioEnergia().doubleValue());
							
							periodos.add(periodo);
						}
					}
				}
			}
			
			if ("R1".equals(magnitud)) {
				List<com.eon.jaxb.Facturas.FacturaATR.EnergiaReactiva.TerminoEnergiaReactiva.Periodo> losPeriodosR = ((TerminoEnergiaReactiva) terminos
					.get(0)).getPeriodo();
				
				for(int a=0;a<losPeriodosR.size();a++){
					for(int b=0;b<datos.size();b++){
						if(a==datos.get(b).getPosicion()-1){
							periodo = new FacdisFacturaPeriodosDTO();
							periodo.setId_fatr(SeqIdFacDis);
							periodo.setFecha_factura(factura.getDatosGeneralesFacturaATR().getDatosGeneralesFactura()
								.getFechaFactura().toString().substring(0, 10));
							periodo.setId_magnitud_cne(magnitud);
							periodo.setId_periodo_cne(Integer.toString(datos.get(b).getPeriodo()));
							
							periodo.setFecha_ini(((TerminoEnergiaReactiva) terminos.get(0)).getFechaDesde().toString().substring(0, 10));
							periodo.setFecha_fin(((TerminoEnergiaReactiva) terminos.get(0)).getFechaHasta().toString().substring(0, 10));
							
							periodo.setValor(losPeriodosR.get(a).getValorEnergiaReactiva().doubleValue());
							periodo.setPrecio(losPeriodosR.get(a).getPrecioEnergiaReactiva().doubleValue());
							
							periodos.add(periodo);
						}
					}
				}
			}

			if ("EP".equals(magnitud)) {
				List<com.eon.jaxb.Facturas.FacturaATR.ExcesoPotencia.Periodo> losPeriodosEP = factura.getExcesoPotencia().getPeriodo();
				
				for(int a=0;a<losPeriodosEP.size();a++){
					for(int b=0;b<datos.size();b++){
						if(a==datos.get(b).getPosicion()-1){
							periodo = new FacdisFacturaPeriodosDTO();
							periodo.setId_fatr(SeqIdFacDis);
							periodo.setFecha_factura(factura.getDatosGeneralesFacturaATR().getDatosGeneralesFactura()
								.getFechaFactura().toString().substring(0, 10));
							periodo.setId_magnitud_cne(magnitud);
							periodo.setId_periodo_cne(Integer.toString(datos.get(b).getPeriodo()));
							
							periodo.setFecha_ini(factura.getPotencia().getTerminoPotencia().get(0).getFechaDesde().toString().substring(0, 10));
							//periodo.setFecha_fin(factura.getPotencia().getTerminoPotencia().get(0).getFechaDesde().toString().substring(0, 10));
							
							periodo.setExcpot(losPeriodosEP.get(a).getValorExcesoPotencia().doubleValue());
							
							periodos.add(periodo);
						}
					}
				}
			}

			// Totalizador
			FacdisFacturaPeriodosDTO periodoTotalizador = new FacdisFacturaPeriodosDTO();
			if (!periodos.isEmpty()) {
				Double valorTotalizador = 0.0;
				for(int i=0;i<periodos.size();i++) {
					if(periodos.get(i).getValor()!=null) {
						valorTotalizador = valorTotalizador + Double.valueOf(periodos.get(i).getValor());
					}
				}
				
				if (valorTotalizador != 0.0) {
					periodoTotalizador.setValor(valorTotalizador);
				}else{
					if("R1".equals(magnitud)||"AE".equals(magnitud)){
						valorTotalizador = 0.0;
						periodoTotalizador.setValor(valorTotalizador);
					}
					if("EP".equals(magnitud)|| "PM".equals(magnitud)){
						periodoTotalizador.setValor(null);
					}
					
				}
				
			}
			
			periodoTotalizador.setId_fatr(SeqIdFacDis);
			periodoTotalizador.setFecha_factura(factura.getDatosGeneralesFacturaATR().getDatosGeneralesFactura().getFechaFactura().toString().substring(0, 10));
			
			if ("PM".equals(magnitud)) {
				periodoTotalizador.setFecha_ini(((TerminoPotencia) terminos.get(0)).getFechaDesde().toString().substring(0, 10));
				periodoTotalizador.setPrecio(factura.getPotencia().getImporteTotalTerminoPotencia().doubleValue());
				//PenalizacionNoIcp
				if(factura.getPotencia().getPenalizacionNoICP() != null && (!factura.getPotencia().getPenalizacionNoICP().equals(""))){
					periodoTotalizador.setPenalizacionnoicp(factura.getPotencia().getPenalizacionNoICP());
				}
			}
			if ("AE".equals(magnitud)) {
				periodoTotalizador.setFecha_ini(((TerminoEnergiaActiva) terminos.get(0)).getFechaDesde().toString().substring(0, 10));
				periodoTotalizador.setPrecio(factura.getEnergiaActiva().getImporteTotalEnergiaActiva().doubleValue());
			}
			if ("R1".equals(magnitud)) {
				periodoTotalizador.setFecha_ini(((TerminoEnergiaReactiva) terminos.get(0)).getFechaDesde().toString().substring(0, 10));
				periodoTotalizador.setPrecio(factura.getEnergiaReactiva().getImporteTotalEnergiaReactiva().doubleValue());
			}
			if ("EP".equals(magnitud)) {
				periodoTotalizador.setFecha_ini(factura.getPotencia().getTerminoPotencia().get(0).getFechaDesde().toString().substring(0, 10));
				periodoTotalizador.setPrecio(factura.getExcesoPotencia().getImporteTotalExcesos().doubleValue());
			}

			periodoTotalizador.setId_periodo_cne(Integer.toString(datos.get(0).getPeriodo()-1));
			periodoTotalizador.setId_magnitud_cne(magnitud);

			periodos.add(periodoTotalizador);
		
		} else {
				String sMsg = "--ERROR--Valores de periodos inesperados en el XML----Magnitud:" + magnitudActual;
				logger.error(sMsg);
				throw new Exception(sMsg);
		}
		return periodos;
	}
	
	private static List<FacdisFacturaPeriodosDTO> trataDatosEnergia(String magnitud, FacturaATR factura, List terminos) throws Exception {
		
		ArrayList<FacdisFacturaPeriodosDTO> periodos = new ArrayList<FacdisFacturaPeriodosDTO>();
		String tarifa = factura.getDatosGeneralesFacturaATR().getDatosFacturaATR().getCodigoTarifa().toString();
		int nPeriodos = 0;
		
		List <FacdisTarifaMagnitudDTO> datosTarifaMagnitud = null;
		if ("PM".equals(magnitud)) {
			List<Periodo> losPeriodos = ((TerminoPotencia) terminos.get(0)).getPeriodo();
			nPeriodos = losPeriodos.size();
		}
		if ("EP".equals(magnitud)) {
			List<com.eon.jaxb.Facturas.FacturaATR.ExcesoPotencia.Periodo> losPeriodos = factura.getExcesoPotencia().getPeriodo();
			nPeriodos = losPeriodos.size();
		}
		if ("AE".equals(magnitud)) {
			List<com.eon.jaxb.Facturas.FacturaATR.EnergiaActiva.TerminoEnergiaActiva.Periodo> losPeriodos = ((TerminoEnergiaActiva) terminos.get(0)).getPeriodo();
			nPeriodos = losPeriodos.size();
		}
		if ("R1".equals(magnitud)) {
			List<com.eon.jaxb.Facturas.FacturaATR.EnergiaReactiva.TerminoEnergiaReactiva.Periodo> losPeriodos = ((TerminoEnergiaReactiva) terminos.get(0)).getPeriodo();
			nPeriodos = losPeriodos.size();
		}

		datosTarifaMagnitud = recuperaDatosTarifaMagnitud(tarifa, magnitud, nPeriodos);
		periodos=(ArrayList<FacdisFacturaPeriodosDTO>) trataDatosTarifaMagnitud(datosTarifaMagnitud, factura, terminos);

		return periodos;
	}

	public void insertarDatosConceptosOtras(OtrasFacturas otraFacturas) throws Exception {
		FacdisFacturaConceptoDAO facdisConceptoDao = new FacdisFacturaConceptoDAO();
		FacdisFacturaConceptoDTO facdisConceptoDto = new FacdisFacturaConceptoDTO();
		List lstConcepto = new ArrayList();
		if (otraFacturas.getConcepto() != null) {
			lstConcepto = otraFacturas.getConcepto();
			for (int num = 0; num < lstConcepto.size(); num++) {
				Concepto concepto = (Concepto) lstConcepto.get(num);
				facdisConceptoDto.setIdFatr(SeqIdFacDis);
				if (concepto.getTipoConcepto() != null) {
					facdisConceptoDto.setIdConceptoCne(concepto
							.getTipoConcepto());
				} else {
					// ImporteConceptoIVA.add(0);
				}
				if (concepto.getImporteTotalConcepto() != null) {
					facdisConceptoDto.setImporte(concepto
							.getImporteTotalConcepto().doubleValue());
				}
				if (concepto.getImporteUnidadConcepto() != null) {
					facdisConceptoDto.setUnidades(concepto
							.getImporteUnidadConcepto().doubleValue());
				}

				logger.debug("Tipo Concepto  " + concepto.getTipoConcepto());
				try {
					facdisConceptoDao.insertarFacdisConceptosDAO(sqlMap,
							facdisConceptoDto);
					bCorrecto = "0";
				} catch (SQLException e) {
					logger.error("Error " + e.getMessage());
					throw e;
				}
			}
		}
	}

	public void insertarDatosRefacturacion(FacturaATR factura) throws Exception {

		// REFACTURACIONES
		Refacturaciones lasRefacturaciones = new Refacturaciones();
		FacdisFacturaRefacturacionDTO refacturacion = new FacdisFacturaRefacturacionDTO();
		FacdisFacturaRefacturacionDAO facdisRefacturacionDao = new FacdisFacturaRefacturacionDAO();
		List<Refacturacion> terminosRefacturaciones = null;
		refacturacion.setIdFatr(Integer.toString(SeqIdFacDis));

		if (factura.getRefacturaciones() != null) {
			lasRefacturaciones = factura.getRefacturaciones();
		}

		if (factura.getConceptoIEIVA().size() > 0
				&& factura.getConceptoIVA().size() > 0) {
			// Si tiene conceptoIVA no debe tener conceptoIEIVA y viceversa, el
			// xsd lo permite pero es erróneo.
			logger.error("Error 1 en la Refacturación, factura errónea");
			throw new Exception("Error 1 en la Refacturación, factura errónea");
			
		} else {
			if (lasRefacturaciones.getRefacturacion() != null) {
				terminosRefacturaciones = lasRefacturaciones.getRefacturacion();
			}

			if ((factura.getConceptoIEIVA().size())
					+ (factura.getConceptoIVA().size()) != terminosRefacturaciones
						.size()) {
				// Si tiene conceptoIVA no debe tener conceptoIEIVA y viceversa,
				// el xsd lo permite pero es erróneo.
				logger.error("Error 2 en la Refacturación, factura errónea");
				throw new Exception("Error 2 en la Refacturación, factura errónea");
			} else {
				for (int i = 0; i < terminosRefacturaciones.size(); i++) {
					Refacturacion laRefacturacion = terminosRefacturaciones
							.get(i);

					for (int num = 0; num < factura.getConceptoIEIVA().size(); num++) {
						ConceptoIEIVA concepIe = factura.getConceptoIEIVA()
								.get(num);
						if (laRefacturacion.getTipo().equals(
								concepIe.getConcepto())) {
							refacturacion
									.setIdTConcepto(concepIe.getConcepto());
							refacturacion.setImporteConceptoIeIva(concepIe
									.getImporteConceptoIEIVA().doubleValue());
							refacturacion.setObservacionesIeIva(concepIe
									.getObservaciones());
							refacturacion.setImporteConceptoIva(0);
							refacturacion.setObservacionesIva("");

						}

					}
					for (int num = 0; num < factura.getConceptoIVA().size(); num++) {
						ConceptoIVA concepIva = factura.getConceptoIVA().get(
								num);
						if (laRefacturacion.getTipo().equals(
								concepIva.getConcepto())) {
							refacturacion.setIdTConcepto(concepIva
									.getConcepto());
							refacturacion.setImporteConceptoIva(concepIva
									.getImporteConceptoIVA().doubleValue());
							refacturacion.setObservacionesIva(concepIva
									.getObservaciones());
							refacturacion.setImporteConceptoIeIva(0);
							refacturacion.setObservacionesIeIva("");

						}
					}

					refacturacion.setRefFechaDesde(laRefacturacion
							.getRFechaDesde().toString().substring(0, 10));
					refacturacion.setRefFechaHasta(laRefacturacion
							.getRFechaHasta().toString().substring(0, 10));

					if (laRefacturacion.getRConsumo() != null) {
						refacturacion.setRefConsumo(laRefacturacion
								.getRConsumo().doubleValue());
					}
					if (laRefacturacion.getImporteTotal() != null) {
						refacturacion.setImporteTotal(laRefacturacion
								.getImporteTotal().doubleValue());
					}
					if (laRefacturacion.getObservaciones() != null) {
						refacturacion.setObservaciones(laRefacturacion
								.getObservaciones());
					} else {
						refacturacion.setObservaciones("");
					}

					try {
						facdisRefacturacionDao.insertarRefacturacionFacdisDAO(
								sqlMap, refacturacion);
						bCorrecto = "0";
					} catch (SQLException e) {
						logger.error("Error " + e.getMessage());
						throw e;
					}
				}
			}
		}
	}

	public FacdisFacturaDTO formarFacturaFacdis(FacturaATR factura)	throws SQLException {
		boolean facErronea = false;
		
		FacdisFacturaDTO laFactura = new FacdisFacturaDTO();
		List<TipoIVAIGIC> iva = factura.getIVA();
		List<TipoIVAIGIC> ivaR = factura.getIVAIGICReducido();

		// Recupero el cupsPM//
		if (factura.getMedidas() != null) {
			List medidas = factura.getMedidas();
			for (int d = 0; d < medidas.size(); d++) {
				Medidas unaMedida = (Medidas) medidas.get(d);
				if (unaMedida.getCodUnificadoPuntoSuministro() != null
						&& (!unaMedida.getCodUnificadoPuntoSuministro().equals(
								""))) {
					cupsPm = unaMedida.getCodUnificadoPuntoSuministro();
				}else{
					cupsPm=null;
				}
			}
		}
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		recuperaIdFatrFacdis();
		laFactura.setId_fatr(SeqIdFacDis);
		laFactura.setNum_factura(factura.getDatosGeneralesFacturaATR()
				.getDatosGeneralesFactura().getNumeroFactura());
		laFactura.setCups(factura.getDatosGeneralesFacturaATR()
				.getDireccionSuministro().getCUPS());
		laFactura.setContrato(String.valueOf(factura
				.getDatosGeneralesFacturaATR().getContrato()));
		laFactura.setId_tipofactrec_cne(factura.getDatosGeneralesFacturaATR()
				.getDatosGeneralesFactura().getIndicativoFacturaRectificadora()
				.value());
		laFactura.setNum_factura_rectificada(factura
				.getDatosGeneralesFacturaATR().getDatosGeneralesFactura()
				.getNumeroFacturaRectificada());
		laFactura.setObservaciones(factura.getDatosGeneralesFacturaATR()
				.getDatosGeneralesFactura().getObservaciones());
		laFactura.setId_tipofactper_cne(factura.getDatosGeneralesFacturaATR()
				.getDatosFacturaATR().getTipoFacturacion());
		laFactura.setId_tipofactura_cne(factura.getDatosGeneralesFacturaATR()
				.getDatosGeneralesFactura().getTipoFactura());
		laFactura.setId_tipo_identif_cne(factura.getDatosGeneralesFacturaATR()
				.getCliente().getTipoCIFNIF().value());
		laFactura.setCif(factura.getDatosGeneralesFacturaATR().getCliente()
				.getIdentificador());
		laFactura.setId_tipo_identif_cne(factura.getDatosGeneralesFacturaATR()
				.getCliente().getTipoCIFNIF().value());
		laFactura.setModo_facturacion(factura.getDatosGeneralesFacturaATR()
				.getDatosFacturaATR().getModoFacturacion());
		laFactura.setDir_suministro(factura.getDatosGeneralesFacturaATR()
				.getDireccionSuministro().getDirSuministro());
		laFactura.setMunicipio((int) factura.getDatosGeneralesFacturaATR()
				.getDireccionSuministro().getMunicipio());
		laFactura.setTipo_moneda(factura.getDatosGeneralesFacturaATR()
				.getDatosGeneralesFactura().getTipoMoneda());
		laFactura.setCupspm(cupsPm);
		laFactura.setId_tarifa_cne(factura.getDatosGeneralesFacturaATR()
				.getDatosFacturaATR().getCodigoTarifa());
		laFactura.setNum_meses(factura.getDatosGeneralesFacturaATR()
				.getDatosFacturaATR().getPeriodo().getNumeroMeses()
				.doubleValue());
		if (factura.getDatosGeneralesFacturaATR().getDatosFacturaATR()
				.getModoControlPotencia() != null) {
			laFactura.setModo_control_potencia(factura
					.getDatosGeneralesFacturaATR().getDatosFacturaATR()
					.getModoControlPotencia().intValue());
		}
		laFactura.setBase_ie(factura.getImpuestoElectrico().getBaseImponible()
				.floatValue());
		laFactura.setCoef_ie(factura.getImpuestoElectrico().getCoeficiente()
				.floatValue());
		laFactura.setPct_ie(factura.getImpuestoElectrico().getPorcentaje()
				.floatValue());
		laFactura.setIe(factura.getImpuestoElectrico().getImporte()
				.floatValue());
		laFactura.setInd_at_med_bt(factura.getDatosGeneralesFacturaATR()
				.getDatosFacturaATR().getIndAltamedidoenBaja());

		if (factura.getAlquileres() != null) {
			if (!factura.getAlquileres().getImporteFacturacionAlquileres()
					.equals(null)) {
				laFactura.setImporte_alquileres(factura.getAlquileres()
						.getImporteFacturacionAlquileres().floatValue());
			}
		}

		// IMPUESTO ELECTRICO
		ImpuestoElectrico impuesto = factura.getImpuestoElectrico();
		if (impuesto != null) {
			trataDatosImpuestoElectrico(impuesto);
		}
		// ALQUILERES
		Alquileres alquileres = factura.getAlquileres();
		if (alquileres != null) {
			trataDatosAlquileres(alquileres);
		}

		// Importe Intereses
		if (factura.getImporteIntereses() != null) {
			ImporteIntereses.add(factura.getImporteIntereses());
		}
		// IVA
		if (iva != null) {
			for (int i = 0; i < iva.size(); i++) {
				// /se controla que solo lleguen 2 periodos de IVA///
				if (i > 1) {
					facErronea = true;
					break;
				}
				if (i > 0) {
					laFactura.setIvaigic_n2(iva.get(i).getImporte()
							.floatValue());
					laFactura.setPct_ivaigic_n2(iva.get(i).getPorcentaje()
							.floatValue());
					laFactura.setBase_ivaigic_n2(iva.get(i).getBaseImponible()
							.floatValue());
				} else {
					laFactura
							.setIvaigic_n(iva.get(i).getImporte().floatValue());
					laFactura.setPct_ivaigic_n(iva.get(i).getPorcentaje()
							.floatValue());
					laFactura.setBase_ivaigic_n(iva.get(i).getBaseImponible()
							.floatValue());
				}
			}
		}
		// IVAR
		if (ivaR != null) {
			for (int i = 0; i < ivaR.size(); i++) {
				// /se controla que solo lleguen 2 periodos de IVA///
				if (i > 1) {
					facErronea = true;
					break;
				}
				if (i > 0) {
					laFactura.setIvaigic_r2(ivaR.get(i).getImporte()
							.floatValue());
					laFactura.setPct_ivaigic_r2(ivaR.get(i).getPorcentaje()
							.floatValue());
					laFactura.setBase_ivaigic_r2(ivaR.get(i).getBaseImponible()
							.floatValue());
				} else {
					laFactura.setIvaigic_r(ivaR.get(i).getImporte()
							.floatValue());
					laFactura.setPct_ivaigic_r(ivaR.get(i).getPorcentaje()
							.floatValue());
					laFactura.setBase_ivaigic_r(ivaR.get(i).getBaseImponible()
							.floatValue());

				}
			}
		}

		String nombreFicheroDocumento = "/" + fechaDia + "/" + sNombreFichero;
		laFactura.setDocumento(nombreFicheroDocumento.toString().replaceAll(
				"\'", "\'\'"));
		laFactura.setTicket_energetico(ticketEnergetico);
		laFactura.setValidado(null);
		laFactura.setFecha_validado(null);

		// fechas del xml
		laFactura.setFecha_boe(factura.getDatosGeneralesFacturaATR()
				.getDatosFacturaATR().getFechaBOE().toString());
		laFactura.setFecha_factura(factura.getDatosGeneralesFacturaATR()
				.getDatosGeneralesFactura().getFechaFactura().toString());
		laFactura.setFecha_grabacion(sdf.format(cal.getTime()));
		laFactura.setFecha_ini(factura.getDatosGeneralesFacturaATR()
				.getDatosFacturaATR().getPeriodo().getFechaDesdeFactura()
				.toString());
		laFactura.setFecha_fin(factura.getDatosGeneralesFacturaATR()
				.getDatosFacturaATR().getPeriodo().getFechaHastaFactura()
				.toString());
		return laFactura;
		// insertamos en BBDD//

	}

	public void inicializaVariables() {
		// Datos Generales Factura ATR- DireccionSuministro

		CupsSum.clear();
		Municipio.clear();
		DirSuministro.clear();

		// Impuesto Eléctrico
		BaseImponible.clear();
		Coeficiente.clear();
		Porcentaje.clear();
		Importe.clear();

		// Alquileres
		ImporteFacturacionAlquileres.clear();

		// Refacturaciones
		TipoRefacturaciones.clear();
		ImporteConceptoIEIVARefacturaciones.clear();
		ObservacionesIEIVARefacturaciones.clear();
		ImporteConceptoIVARefacturaciones.clear();
		ObservacionesIVARefacturaciones.clear();
		RFechaDesdeRefacturaciones.clear();
		RFechaHastaRefacturaciones.clear();
		RConsumoRefacturaciones.clear();
		ImporteTotalRefacturaciones.clear();
		ObservacionesRefacturaciones.clear();

		// Importe Intereses
		ImporteIntereses.clear();

		// Concepto IEIVA IVA
		TipoConceptoIEIVA.clear();
		ImporteConceptoIEIVA.clear();
		ObservacionesIEIVA.clear();
		TipoConceptoIVA.clear();
		ImporteConceptoIVA.clear();
		ObservacionesIVA.clear();

		// Totales
		TotalPeriodosExcesoPotencia.clear();
		TotalTerminosEA.clear();
		TotalPeriodosEA.clear();
		TotalTerminosER.clear();
		TotalPeriodosER.clear();
		TotalTerminosRE.clear();
		TotalTerminosIVA.clear();
		TotalTerminosIVAReducido.clear();
		TotalMedidas.clear();
	}

	private boolean verificarExistenciaFactura(FacdisFacturaDTO factura, String codFiscal) {
		// variable para controlar facturas erróneas//
		boolean facErronea = false;
		
		boolean existFacdis = false;
		// función que comprueba la existencia de la factura en BBDD y si es
		// así, la mueve al directorio correspondiente//
		FacdisFacturaDTO existeFacDis = null;
		String numeroFacturaVerificar = factura.getNum_factura().toString();
		String DireccionSuministroVerificar = factura.getDir_suministro()
				.toString();
		String cupsVerificar = factura.getCups().toString();

		if (numeroFacturaVerificar == null
				|| numeroFacturaVerificar.trim().equals("")) {
			logger.info("NumeroFactura está vacío");
			existFacdis = true;
		} else {
			if (!numeroFacturaVerificar.equals(codFiscal)) {
				logger.debug("Distinto CodFiscal que NumeroFactura");
				// errónea//
				bCorrecto = "1";
				facErronea = true;
			} else {
				HashMap map = new HashMap();
				map.put("numeroFacturaVerificar", numeroFacturaVerificar);
				map.put("cupsVerificar", cupsVerificar);
				try {
					existeFacDis = (FacdisFacturaDTO) sqlMap.queryForObject(
							"buscaFacturaFacDis", map);
				} catch (SQLException e) {
					logger.error("Error " + e.getMessage());
				}

				if (existeFacDis != null) {
					if (existeFacDis.getValidado() != null
							&& existeFacDis.getValidado().equals("OK")) {
						existFacdis = true;
						logger.debug("Factura duplicada OK");
						// /duplicada/////////
						bCorrecto = "2";
					} else {
						logger.debug("Factura duplicada NO OK, la eliminamos de la BBDD");
						// Como la factura está inválida, primero la copiamos a
						// las tablas de duplicadas y luego la eliminanos de la
						// original
						// para que se pueda cargar la nueva que ha llegado, que
						// puede ser correcta.
						try {
							// Se inicia la transacción
							sqlMap.startTransaction();
							sqlMap.insert("insertar_Factura_facDis_duplicadas",
									Integer.toString(existeFacDis.getId_fatr()));
							sqlMap.delete("borrar_ConceptosFacdis_duplicados",
									Integer.toString(existeFacDis.getId_fatr()));
							sqlMap.delete("borrar_LecturasFacdis_duplicados",
									Integer.toString(existeFacDis.getId_fatr()));
							sqlMap.delete("borrar_facDis_validacion_duplica",
									Integer.toString(existeFacDis.getId_fatr()));
							sqlMap.delete("borrar_periodos_facDis_duplicados",
									Integer.toString(existeFacDis.getId_fatr()));
							sqlMap.delete("borrar_refacturacion_facDis_duplica",
									Integer.toString(existeFacDis.getId_fatr()));
							sqlMap.delete("borrar_factura_facDis",
									Integer.toString(existeFacDis.getId_fatr()));
							sqlMap.commitTransaction();
							totalSustituidas++;
							existFacdis = false;
						} catch (SQLException e) {
							bCorrecto = "1";
							facErronea = true;
							logger.error("Error " + e.getMessage());
						} finally {
							try {
								sqlMap.endTransaction();
							} catch (SQLException e) {
								logger.error("Error al cerrar la transacción."
										+ e);
							}
						}
					}
				}
			}
		}
		return existFacdis;
	}

	/**
	 * Funcion que escribe el resumen del proceso en el fichero de log
	 */
	private static void EscribirResultado() {
		LogProceso.mensaje(" ");
		LogProceso.mensaje("PROCESO DE CARGA DE FACTURAS:");
		LogProceso
				.mensaje("  -----------------------------------------------  ");
		LogProceso.mensaje(" ");

		LogProceso.registro("TOTAL FACTURAS RECIBIDAS =", totalFacturas);
		LogProceso.registro("TOTAL FACTURAS PROCESADAS =", totalProcesadas);
		LogProceso.registro("TOTAL FACTURAS SUSTITUIDAS =", totalSustituidas);
		LogProceso.registro("TOTAL FACTURAS ERRONEAS =", totalErroneas);
		LogProceso.registro("TOTAL FACTURAS DUPLICADAS =", totalDuplicadas);

		LogProceso.mensaje(" ");
	}
}