package com.eon.fatr.batch;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
import com.ibatis.common.jdbc.SimpleDataSource;
import com.ibatis.sqlmap.client.SqlMapClient;

public class Batch_FACDIS_Backup {
	// Código de retorno para Control-M
	private static int controlM_ExitCode;
	
	private static boolean todoOK;
	
	private static String nombreServicio= "PROCESO_BATCH_BACKUP";
	
	// Contadores para el resumen
	private static int deben;
	private static int existen;
	private static int noExisten;
	
	private static ZipOutputStream zos;
	
	private static SqlMapClient sqlMap;

	private static Log logger = LogFactory.getFactory().getInstance(Batch_FACDIS_Backup.class);
	
	public Batch_FACDIS_Backup(String nombreFichero) {
	}

	public static void main(String[] args) {
		
		controlM_ExitCode = 0;
		
    	BasicConfigurator.configure();
		
		try {
			logger.info("Inicio proceso BACKUP (FACDIS)");
	
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
				
				Batch_FACDIS_Backup backup = new Batch_FACDIS_Backup(args[0]);
				
				// Recuperamos todas las facturas cuya Fecha_Factura sea anterior a la fecha actual menos los días de la variable TIEMPO_ONLINE
				String tiempoOnline = PropiedadesAplicacion.getInstance().getPropiedad("TIEMPO_ONLINE");
				List<FacdisFacturaDTO> datos = backup.recuperaFacturas(tiempoOnline, "recuperaFacturasBackup");
				deben = datos.size();

				// si todo ha ido bien, actualizamos el campo (DOCUMENTO_BACKUP) en base de datos (PONEMOS el mismo valor que en documento)
				todoOK = backup.actualizarFacturas(datos);

				/*
				// Recuperamos todas las facturas cuya Fecha_factura sea anterior a la fecha actual menos los días de la variable TIEMPO_ONLINEBACKUP
				List<FacdisFacturaDTO> datosBackup = cargaFacturas.recuperaFacturas(tiempoOnlineBackup,"DESTINO","recuperaFacturasBackupZIP");
				*/
				// Generamos el zip con todas las carpetas de la carpeta ZipBackup
				String nombreCompletoFichero = "";
				if (datos != null && datos.size() > 0) {
					nombreCompletoFichero = backup.generarZip(PropiedadesAplicacion.getInstance().getPropiedad("DIRECTORIOPENDIENTEBACKUP"));
				}
				
				// Subimos el zip al FTP destino
				if (!nombreCompletoFichero.equals("")) {
					String PROTOCOLO_SEGURO= PropiedadesAplicacion.getInstance().getPropiedad("FTPSECUREMODE");
					String TIPO_TRANSFERENCIA = PropiedadesAplicacion.getInstance().getPropiedad("FTPTRANSFERTYPE");
					String ipFTP = PropiedadesAplicacion.getInstance().getPropiedad("IP_FTP_DESTINO");
					String Usuario_FTP = PropiedadesAplicacion.getInstance().getPropiedad("USUARIO_FTP_DESTINO");
					String Password_FTP = PropiedadesAplicacion.getInstance().getPropiedad("CLAVE_FTP_DESTINO");
					String Directorio_FTP = PropiedadesAplicacion.getInstance().getPropiedad("DIRECTORIO_FTP_DESTINO");

					UtilFTP ftp = new UtilFTP(ipFTP, Usuario_FTP, Password_FTP, PROTOCOLO_SEGURO, TIPO_TRANSFERENCIA);
					boolean bEnviar = ftp.enviarFTP(nombreCompletoFichero, Directorio_FTP, nombreCompletoFichero);
					
					// Si se ha subido correctamente, lo eliminamos del Origen
					if (bEnviar) {
						boolean bBorrado = TratarFicheros.deleteFile(nombreCompletoFichero);
					}
				}
	
				// Eliminamos los ficheros sueltos del backup
				if (todoOK) {
					backup.borrarCarpeta(PropiedadesAplicacion.getInstance().getPropiedad("DIRECTORIOPENDIENTEBACKUP"));
				}

				logger.info("FIN DEL PROCESO de ENVIO AL SERVIDOR DE BACKUP DE FACTURAS ATR.");
				todoOK = true;

	    	} catch (IOException ioe) {
	    		logger.error("Error IO: " + ioe.getMessage());
	    		controlM_ExitCode = 2;
	    	} catch (SQLException ioe) {
	    		logger.error("Error SQL: " + ioe.getMessage());
	    		controlM_ExitCode = 5;
	    	} catch (Exception e) {
	    		logger.debug("Error al procesar envio al backup en motor:" + e.getMessage());
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
					EjecucionesServicios.setEjecucion(sqlMap, "Batch_FACDIS_Backup", fechaejecucion, PropiedadesAplicacion.getRutaLog(), todoOK, "FACDIS");
				}
			} catch (Exception ex) {
				logger.error("No se ha guardado la fecha de ejecucion en la base de datos: " + ex.getMessage());
				controlM_ExitCode = 1;
		    }
			
			//Escribimos el resumen del proceso
			 EscribirResultado();
			
			//Escribimos el pie que muestra el tiempo que ha tardado el proceso
			LogProceso.Pie();
	    	logger.info("FINALIZACIÓN DEL PROCESO DE ENVIO AL SERVIDOR DE BACKUP DE FACTURAS ATR (FACDIS)");
	    	
			//Cerramos todas las conexiones del pool
	    	if (sqlMap != null) {
				SimpleDataSource ds = (SimpleDataSource) sqlMap.getDataSource();
		        ds.forceCloseAll();
			}
	    	System.exit(controlM_ExitCode);
	    }
	}

	/**
	 * Funcion que escribe el resumen del proceso en el fichero de log
	 */
	private static void EscribirResultado(){
		LogProceso.mensaje(" ");
		LogProceso.mensaje("PROCESO DE ENVIO BACKUP:");
		LogProceso.mensaje("  -----------------------------------------------  ");
		LogProceso.mensaje(" ");
		
		LogProceso.registro("Ficheros que deben moverse a backup", deben);
		LogProceso.registro("Ficheros realmente movidos a backup", existen);
		LogProceso.registro("Ficheros no movidos a backup", noExisten);

		LogProceso.mensaje(" ");
	}
	
	//Método que lee el fichero y recupera los datos en BBDD.
	private List<FacdisFacturaDTO> recuperaFacturas(String dias, String nombreQuery) {

		logger.info("COMIENZA LA RECUPERACIÓN DE FACTURAS PARA BACKUP");
		
		String ruta = "";
		String nombreFichero = "";
		List<FacdisFacturaDTO> datos = null;
		boolean bFicheroXML = true;
		
		try {
			datos = sqlMap.queryForList(nombreQuery, dias);

			if (datos.size()>0) {
				// Recorremos el listado y recuperamos el documento xml
				ListIterator<FacdisFacturaDTO> listado = datos.listIterator();
				while (listado.hasNext()) {
					FacdisFacturaDTO factura = (FacdisFacturaDTO) listado.next();
					// Pasamos la ruta y el nombre del documento
					if (factura.getDocumento() != null) {
						ruta = factura.getDocumento().substring(0, factura.getDocumento().lastIndexOf("/"));
						nombreFichero = factura.getDocumento().substring(factura.getDocumento().lastIndexOf("/") + 1, factura.getDocumento().length());
						bFicheroXML = procesaDirectorio(ruta, nombreFichero);
						// Si hemos podido recuperar el fichero, lo indicamos
						if (bFicheroXML) {
							factura.setExisteXML(true);
						} else {
							factura.setExisteXML(false);
						}
					} else {
						logger.info("Para el numero de factura // Id_fatr " + factura.getNum_factura() + " //" + factura.getId_fatr() + " no existe documento xml.");
					}
				}
			} else {
				logger.info("NO EXISTEN FACTURAS PARA REALIZAR BACKUP...");
			}
		} catch (SQLException e) {
			logger.error("Error al recuperar las facturas validas:: " + e.toString());
    		controlM_ExitCode = 5;
		} catch (Exception e) {
			logger.debug("Error al procesar envio al backup en motor:" + e.getMessage());
			controlM_ExitCode = 1;
		}
		
		logger.info("FIN DE LA RECUPERACIÓN DE FACTURAS.");
		return datos;
	}

	private boolean actualizarFacturas(List<FacdisFacturaDTO> datos) {
		boolean bCorrecto = false;
		logger.info("COMIENZA LA ACTUALIZACIÓN DE DOCUMENTO_BACKUP PARA LAS FACTURAS...");
		
		try {
			if (datos.size()>0) {
				// Recorremos el listado y recuperamos el documento xml
				ListIterator<FacdisFacturaDTO> listado = datos.listIterator();
				while (listado.hasNext()) {
					FacdisFacturaDTO factura = (FacdisFacturaDTO) listado.next();
					// Si hemos podido descargar el documento XML del FTP de MIRCOM
					// actualizamos la fecha
					if (factura.isExisteXML()) {
						// Si el documento viene vacío es porque será la misma ruta, al pasar de FTP original a FTP backup
						sqlMap.update("actualizaFacturasBackup", ""+factura.getId_fatr());
						logger.info("Para el id_fatr " + factura.getId_fatr() + " se ha actualizado el campo documento_backup.");
					} else {
						logger.info("Para el id_fatr " + factura.getId_fatr() + " NO se ha actualizado el campo documento_backup.");
					}
				}
				bCorrecto = true;
			} else {
				logger.info("NO EXISTEN FACTURAS PARA REALIZAR BACKUP...");
				bCorrecto = true;
			}

		} catch (SQLException e) {
			logger.error("Error al actualizar el campo documento_backup para la factura: " + e.toString());
			bCorrecto = false;
		}
		logger.info("FIN DE LA ACTUALIZACIÓN DOCUMENTO_BACKUP DE FACTURAS.");
		
		return bCorrecto;
	}
	
	/**
	 * A partir del directorio de Procesado, localizamos el fichero xml para entregar al Portal
	 * @param DirectorioFTP
	 * @param nombreFichero
	 * @return
	 */
	private boolean procesaDirectorio(String ruta, String nombreFichero) {

		boolean bCorrecto = true;
		String directorioPendiente = "";
		boolean bBorrado = true;
		
		try {
			String directorioOrigen = PropiedadesAplicacion.getInstance().getPropiedad("DIRECTORIOPROCESADO") + ruta + "/";
			String nombreCompletoOrigen = directorioOrigen +  nombreFichero;
			File origen = new File(PropiedadesAplicacion.getInstance().getPropiedad("DIRECTORIOPROCESADO") + ruta + "/" + nombreFichero);
			// Creamos la estructura de carpeta igual que en el FTP
			File directorioLocal = new File (PropiedadesAplicacion.getInstance().getPropiedad("DIRECTORIOPENDIENTEBACKUP") + ruta);
			directorioLocal.mkdir();

			directorioPendiente = directorioLocal.getAbsolutePath();
			String nombreCompleto = directorioPendiente + "/" + nombreFichero;
			File destino = new File(nombreCompleto);

			if (origen.exists()) {
				bCorrecto = TratarFicheros.copyFile(origen, destino);
				bBorrado = TratarFicheros.deleteFile(nombreCompletoOrigen);
	//			if (bBorrado) {
//					bBorrado = TratarFicheros.deleteFile(nombreCompleto);
					if (bBorrado) {
						// Si el directorio se ha quedado vacío, lo eliminamos.
						File directorio = new File(directorioOrigen);
						File[] ficheros = directorio.listFiles();
						if (ficheros == null || ficheros.length == 0) {
							logger.info("Eliminamos el directorio " + directorio.toString() + " porque está vacío.");
							directorio.delete();
						}
//					}
				existen++;
				}
			} else {
				noExisten++;
			}			
		} catch (Exception e) {
			logger.error("Error al intentar copiar el fichero:" + nombreFichero);
			bCorrecto = false;
		}
		return bCorrecto;
	}
	
	private void borrarCarpeta(String fileName) { 
		try {
			File file = new File(fileName);
			String[] fileNames = file.list();  
			if (fileNames != null) {  
				for (int i = 0; i< fileNames.length; i++){   
					TratarFicheros.deleteFile(PropiedadesAplicacion.getInstance().getPropiedad("DIRECTORIOPENDIENTEBACKUP") + "/" + fileNames[i]);  
				}  
			}  			
		} catch (Exception e) {
			logger.error("Error al borrarFicheros " + e.fillInStackTrace());
		}
	}
	
	/**  
	* Creates a Zip archive. If the name of the file passed in is a  
	* directory, the directory's contents will be made into a Zip file.  
	*/ 
	private String generarZip(String fileName) {  
	
		String nombreFichero = "";
		
		try {
			nombreFichero = PropiedadesAplicacion.getInstance().getPropiedad("DIRECTORIOZIPBACKUP") + "/" +
					PropiedadesAplicacion.getInstance().getPropiedad("NOMBREZIP") + TratarFicheros.recuperarFecha() + ".zip";
			zos = new ZipOutputStream(new FileOutputStream(nombreFichero));  
			
			File file = new File(fileName);
			recurseFiles(file);  

			zos.close();
			
			
		} catch (Exception e) {
			logger.error("Error al generarZip " + e.fillInStackTrace());
			nombreFichero = "";
		}
	
		return nombreFichero;
	}
	
	/**  
	 * Recurses down a directory and its subdirectories to look for  
	 * files to add to the Zip. If the current file being looked at  
	 * is not a directory, the method adds it to the Zip file.  
	 */ 

	private void recurseFiles(File file) throws IOException, FileNotFoundException { 

		if (file.isDirectory()) {  
			String[] fileNames = file.list();  
			if (fileNames != null) {  
				for (int i = 0; i< fileNames.length; i++){   
					recurseFiles(new File(file, fileNames[i]));  
				}  
			}  
		} else {  
			byte[] buf = new byte[1024];  
			int len;
			String a = file.toString().substring(file.toString().lastIndexOf("\\") - 8, file.toString().length());
			ZipEntry zipEntry = new ZipEntry(a);
			FileInputStream fin = new FileInputStream(file);  
			BufferedInputStream in = new BufferedInputStream(fin);  
			zos.putNextEntry(zipEntry);  
			while ((len = in.read(buf)) >= 0) {  
				zos.write(buf, 0, len);  
			}  
			in.close();  
			zos.closeEntry();
			
			file.delete();
		}  
	}
}