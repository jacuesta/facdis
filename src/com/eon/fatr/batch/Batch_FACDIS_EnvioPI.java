package com.eon.fatr.batch;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;

import com.eon.fatr.batch.general.EjecucionesServicios;
import com.eon.fatr.componentes.TratamientoDatosPIDAO;
import com.eon.fatr.utilidades.LogProceso;
import com.eon.fatr.utilidades.PropiedadesAplicacion;
import com.eon.fatr.utilidades.TratarFicheros;
import com.eon.fatr.utilidades.Util;
import com.eon.fatr.utilidades.UtilFTP;
import com.ibatis.common.jdbc.SimpleDataSource;
import com.ibatis.sqlmap.client.SqlMapClient;

public class Batch_FACDIS_EnvioPI {
	// Código de retorno para Control-M
	private static int controlM_ExitCode;
	private static SqlMapClient sqlMap;

	private static boolean todoOK;

	private static String nombreServicio= "PROCESO_BATCH_MIRCOMPI";
	private static String nombreInterfaz = "";
	private static long fichDesc = 0;
	private static long fichGen = 0;
	private static long regGen = 0;
	private static long fichSinResp= 0;
	private static String nombreFichDesc="";
	private static Date fechaIniPI=null;
	private static Date fechaFinPI=null;
	private static Date fechaIniGen=null;
	private static Date fechaFinGen=null;
	private static Date fechaIniEnv=null;
	private static Date fechaFinEnv=null;
	private static int iResultado=0;
	
	public static boolean lecturaPaquetes = false;
	public static boolean continuarLectura = true;
	
	private static Log logger = LogFactory.getFactory().getInstance(Batch_FACDIS_EnvioPI.class);
	
	public static void main(String args[]) {
		
		controlM_ExitCode = 0;
		
    	BasicConfigurator.configure();		
		
		String txtnumeroInterfaces = "";
		int numeroInterfaces = 0;

		try {
			logger.info("Inicio proceso MIRCOMPI");
		
			if (args.length < 3) {
				// Numero de argumentos incorrectos
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
				
				//Se generan los ficheros necesarios
				txtnumeroInterfaces = PropiedadesAplicacion.getInstance().getPropiedad("numeroInterfaces");
				numeroInterfaces = txtnumeroInterfaces == null?0:Integer.parseInt(txtnumeroInterfaces);
				
				//A partir del número de ficheros, se van recogiendo las variables correspondientes
				for(int i=1;i<=numeroInterfaces;i++) {
					//Se deben recuperar los parámetros necesarios para este fichero
					nombreInterfaz = PropiedadesAplicacion.getInstance().getPropiedad("nombreInterfaz" + i);
					logger.debug(" ** Procesando Fichero: " + nombreInterfaz);
					//1- Se procesan los ficheros pendientes
					comprobarFicherosPendientes(sqlMap, i);
					//2- Se lanza el proceso
					iResultado = lanzarProceso(sqlMap, nombreInterfaz, i);
					//3- Se envía el fichero

					if(iResultado == 0) {
						enviarFichero(sqlMap, i);
					} else {
						nombreInterfaz="";
					}
	
					todoOK = true;
				}
	            
	    	} catch (IOException ioe) {
	    		logger.error("Error IO: " + ioe.getMessage());
	    		controlM_ExitCode = 2;
	    	} catch (SQLException ioe) {
	    		logger.error("Error SQL: " + ioe.getMessage());
	    		controlM_ExitCode = 5;
	    	} catch (Exception e) {
	    		logger.error("Error al procesar las validaciones en motor:" + e.getMessage());
	    		controlM_ExitCode = 1;
	    	} finally {
				sqlMap.endTransaction();
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
					EjecucionesServicios.setEjecucion(sqlMap, "Batch_FACDIS_EnvioPI", fechaejecucion, PropiedadesAplicacion.getRutaLog(), todoOK, "FACDIS");
				}
			} catch (Exception ex) {
				logger.error("No se ha guardado la fecha de ejecucion en la base de datos: " + ex.getMessage());
				controlM_ExitCode = 1;
		    }
			
			//Escribimos el resumen del proceso
			 EscribirResultado();
			
			//Escribimos el pie que muestra el tiempo que ha tardado el proceso
			LogProceso.Pie();
	    	logger.info("FINALIZACIÓN DEL PROCESO DE VALIDACIÓN (FACDIS)");
	    	
			//Cerramos todas las conexiones del pool
	    	if (sqlMap != null) {
				SimpleDataSource ds = (SimpleDataSource) sqlMap.getDataSource();
		        ds.forceCloseAll();
			}
	    	System.exit(controlM_ExitCode);
	    }
	}			
	
	public static int lanzarProceso(SqlMapClient sqlMap, String fichero, int i) throws SQLException, IOException, Exception{
		
		//Se inicializan las variables del informe
		fichGen=0;
		fechaIniGen=new Date();
		
		int error = 1;
		String nombre = PropiedadesAplicacion.getInstance().getPropiedad("nombreInterfaz" + i);
		String esquema = PropiedadesAplicacion.getInstance().getPropiedad("esquema" + i);
		
		//Se debe comprobar si hay algún fichero enviado al PI que no tiene respuesta.
		//Si es así no se debe generar ningún fichero mientras no se tenga respuesta.
		TratamientoDatosPIDAO gen = new TratamientoDatosPIDAO();
		List<?> listaFicherosSinRespuesta = gen.obtenerFicherosSinRespuestaPI(sqlMap, nombre, esquema);
		fichSinResp=listaFicherosSinRespuesta.size();
		List<?> listaDatos = new ArrayList(); 
		
		if(listaFicherosSinRespuesta.size() > 0){
			logger.debug(" -- Hay ficheros que todavía no han tenido respuesta del PI: " + listaFicherosSinRespuesta.get(0));
		} else {
			//Se obtienen los datos del fichero
			listaDatos = gen.obtenerDatosFicheroPI(sqlMap, nombre, i);
		
			if(listaDatos != null && listaDatos.size() > 0){
				String directorio = PropiedadesAplicacion.getInstance().getPropiedad("directorioFicherosSalida") + Util.fechaHoyYYYYMMDD() + File.separator;
				File f = new File(directorio);
				f.mkdirs();
				
				String fecha = Util.fechaCompleta();
				
				nombreInterfaz += fecha + "E"; 
				
				error = gen.generarFicheroTextoPI(sqlMap, listaDatos, nombreInterfaz, directorio, i);
				
				if(error == 0){
					logger.info("El Fichero " + nombreInterfaz + " se ha generado con éxito.");
					// Actualizamos
					gen.actualizarDatostPI(sqlMap, i);
					fichGen=1;
				}
			} else {
				logger.info("No se han recuperado facturas para generar el fichero: " + nombreInterfaz);
			}
			
		}
		
		regGen=listaDatos.size();
		//nombreFich=nombreInterfaz;
		fechaFinGen=new Date();
	
		return error;
	}
	
	public static void enviarFichero(SqlMapClient sqlMap, int iFichero) throws Exception {
		
		fechaIniEnv=new Date();
		//Recuperar fichero de la ruta día actual
		//Se recupera la información necesaria para recuperar los ficheros 
		String ipFTPEnv  = PropiedadesAplicacion.getInstance().getPropiedad("ipFTP_E_" + iFichero); 
		String Usuario_FTP_Env = PropiedadesAplicacion.getInstance().getPropiedad("Usuario_FTP_E_" + iFichero); 
		String Password_FTP_Env = PropiedadesAplicacion.getInstance().getPropiedad("Password_FTP_E_" + iFichero);
		String Directorio_FTP_Env = PropiedadesAplicacion.getInstance().getPropiedad("Directorio_FTP_E_" + iFichero);
		String directorioSalida_Env = PropiedadesAplicacion.getInstance().getPropiedad("directorioFicherosSalida");
		String ftpSecureMode_Env = PropiedadesAplicacion.getInstance().getPropiedad("ftpSecureMode_E_" + iFichero);
		String ftpTransferType = PropiedadesAplicacion.getInstance().getPropiedad("ftpTransferType_E_" + iFichero);
		//Se crea la ruta destino según la fecha en el caso de que no exista
		String directorioConFechaSal = directorioSalida_Env + Util.fechaHoyYYYYMMDD();
		
		File fs = new File(directorioConFechaSal);
		fs.mkdirs();
		String[] fPendientesEnv = fs.list();
		TratamientoDatosPIDAO actEnv = new TratamientoDatosPIDAO();
		//Se recupera el fichero del directorio de Salida
		
		for(int i = 0; i < fPendientesEnv.length;i++){
			
			String sFActual = fPendientesEnv[i];
			
			if(sFActual.equals(nombreInterfaz)){
			
				String ficheroSalida=directorioConFechaSal+File.separator+sFActual;
				
				//Enviar por ftp				
				//El fichero se sube al ftp
				UtilFTP conectorFTP = new UtilFTP(ipFTPEnv, Usuario_FTP_Env, Password_FTP_Env, ftpSecureMode_Env, ftpTransferType); 
				boolean bEnvio = conectorFTP.enviarFTP( ficheroSalida ,Directorio_FTP_Env, sFActual );
				
				if (bEnvio)
					//Actualizar tabla control
					actEnv.actualizarTablaEnvPI(sqlMap, directorioConFechaSal, sFActual, iFichero);
				else
					logger.error("Error en el envío del fichero.");
			}
		}
		fechaFinEnv=new Date();
	}
	
	private static void EscribirResultado(){
		//Definicion de variables
		int longitudMinimaTexto = 75;

		
		//Escribir el resultado del proceso
		
		LogProceso.mensaje("");
		LogProceso.mensaje("********************************************************************************************************");
		LogProceso.mensaje("PROCESADO DE DATOS DEL FICHERO:  " + nombreFichDesc.toUpperCase());
		LogProceso.mensaje("********************************************************************************************************");
		LogProceso.mensaje("");
		LogProceso.mensaje("");
		LogProceso.mensaje("   TRATAMIENTO DE FICHEROS PENDIENTES EN EL PI 														");
		LogProceso.mensaje("-------------------------------------------------------------------------");
		LogProceso.mensaje("");
		LogProceso.registro(Util.AjustarTextos("Total Ficheros descargados del PI:",longitudMinimaTexto) , fichDesc);
		LogProceso.registroString(Util.AjustarTextos("Se ha descargado del PI un fichero con nombre:",longitudMinimaTexto) ,nombreFichDesc );
		LogProceso.mensaje("");
		LogProceso.DurProceso(fechaIniPI,fechaFinPI);
		LogProceso.mensaje("");
		LogProceso.mensaje("");
		LogProceso.mensaje("    TRATAMIENTO DE FICHEROS GENERADOS Y ENVIADOS 														");
		LogProceso.mensaje("-------------------------------------------------------------------------");
		LogProceso.mensaje("");
		LogProceso.registro(Util.AjustarTextos("Total Ficheros pendientes de respuesta: ",longitudMinimaTexto) , fichSinResp);
		LogProceso.registro(Util.AjustarTextos("Total Ficheros generados y enviados: ",longitudMinimaTexto) , fichGen);
		LogProceso.registroString(Util.AjustarTextos("Se ha generado un fichero con nombre: ",longitudMinimaTexto) ,nombreInterfaz );
		LogProceso.registro(Util.AjustarTextos("El fichero contiene información relativa al siguiente número de facturas: ",longitudMinimaTexto) ,regGen );
		LogProceso.mensaje("");
		LogProceso.mensaje("");
		LogProceso.mensaje("+++++++++++++++++++++++++++++++++++   TIEMPO DE GENERACIÓN DEL FICHERO   +++++++++++++++++++++++++++++++++");
		LogProceso.mensaje("");
		LogProceso.DurProceso(fechaIniGen,fechaFinGen);
		LogProceso.mensaje("");
		LogProceso.mensaje("++++++++++++++++++++++++++++++++++++++   TIEMPO DE ENVIO DEL FICHERO  ++++++++++++++++++++++++++++++++++++");
		LogProceso.mensaje("");
		if(iResultado==0){
			LogProceso.DurProceso(fechaIniEnv,fechaFinEnv);
			}else{
				LogProceso.DurEnvio("SAP PI");
			}
		LogProceso.mensaje("");
	}
	
	public static void comprobarFicherosPendientes(SqlMapClient sqlMap, int iFichero) throws Exception {
		
		//Se recupera la información necesaria para recuperar los ficheros 
		String nombre = PropiedadesAplicacion.getInstance().getPropiedad("nombreInterfaz" + iFichero);
		String ipFTP  = PropiedadesAplicacion.getInstance().getPropiedad("ipFTP_R_" + iFichero); 
		String Usuario_FTP = PropiedadesAplicacion.getInstance().getPropiedad("Usuario_FTP_R_" + iFichero); 
		String Password_FTP = PropiedadesAplicacion.getInstance().getPropiedad("Password_FTP_R_" + iFichero);
		String Directorio_FTP = PropiedadesAplicacion.getInstance().getPropiedad("Directorio_FTP_R_" + iFichero);
		String directorioDestino = PropiedadesAplicacion.getInstance().getPropiedad("directorioFicherosEntrada");
		String ftpSecureMode = PropiedadesAplicacion.getInstance().getPropiedad("ftpSecureMode_R_" + iFichero);
		String ftpTransferType = PropiedadesAplicacion.getInstance().getPropiedad("ftpTransferType_R_" + iFichero);		

		String esquema = PropiedadesAplicacion.getInstance().getPropiedad("esquema" + iFichero);
		
		fechaIniPI=new Date();
		
		//Se crea la ruta destino según la fecha en el caso de que no exista
		String directorioConFecha = directorioDestino + Util.fechaHoyYYYYMMDD();
		
		File f = new File(directorioConFecha);
		f.mkdirs();
		
		//Se recuperan los ficheros
		UtilFTP conectorFTP = new UtilFTP(ipFTP, Usuario_FTP, Password_FTP, ftpSecureMode, ftpTransferType); 
		List<String> listaFicherosFTP=conectorFTP.recuperarFTP(nombre, directorioConFecha, Directorio_FTP);
	
		//Se recupera una lista de ficheros recuperados
		TratamientoDatosPIDAO gen = new TratamientoDatosPIDAO();
		
		String sFActual = "";
		if(listaFicherosFTP != null && listaFicherosFTP.size() > 0){
		
			for(int i =0; i< listaFicherosFTP.size();i++)
			{
				File fichFTP = new File((String)listaFicherosFTP.get(i));
				sFActual=fichFTP.getName();
			
				//Actualizar en la tabla de control.
				gen.actualizarTablaPI(sqlMap, directorioConFecha, sFActual, iFichero);
			}
		}
		fichDesc=listaFicherosFTP.size();
		nombreFichDesc=sFActual;
		fechaFinPI=new Date();
	}
}