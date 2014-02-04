package com.eon.fatr.batch;

import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;

import com.eon.fatr.batch.general.EjecucionesServicios;
import com.eon.fatr.componentes.ValidacionDAO;
import com.eon.fatr.utilidades.LogProceso;
import com.eon.fatr.utilidades.PropiedadesAplicacion;
import com.eon.fatr.utilidades.TratarFicheros;
import com.ibatis.common.jdbc.SimpleDataSource;
import com.ibatis.sqlmap.client.SqlMapClient;

public class Batch_FACDIS_Validaciones{
	// Código de retorno para Control-M
	private static int controlM_ExitCode;
	private static SqlMapClient sqlMap;
	
	private static boolean todoOK;
	
	static String aListaValidaciones[] = null;
	static String numeroFactura = "";
	static int iListaValidaciones = 0;
	static int iListaValidacionesOK = 0;
	static int iListaValidacionesError = 0;
	
	private static Log logger = LogFactory.getFactory().getInstance( Batch_FACDIS_Validaciones.class);
	
    public Batch_FACDIS_Validaciones()
    {}

    public static void main(String args[]) throws Exception{
    	
    	controlM_ExitCode = 0;
    	
    	BasicConfigurator.configure();
    	
    	// control de parametros
    	String xIn = "";
    	int x = 0 ;
		// Bucle control de ejecución de clases de validación.
    	try {
    		logger.info("Inicio proceso VALIDACIONES");
    		
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
			
	    	aListaValidaciones = PropiedadesAplicacion.getInstance().getPropiedad("CLASES").split(",");

			try {
				todoOK = false; 			

				LogProceso.Cabecera("FACDIS - VALIDACIONES");

				// --------------------------------------------------------
	    		//Definiendo los parametros y cargando los valores en ellos
	    		// --------------------------------------------------------

	    		Class tipoparametros[] = new Class[2];
	    		
	    		tipoparametros[0] = SqlMapClient.class;
	    		tipoparametros[1] = String.class;
	    		
	    		Object argumentos[] = new Object[2];
	    		argumentos[0] = sqlMap ;
	    		for(x=2;x<args.length;x++){
	    			if( x !=1){
	    				xIn = xIn + ", ";
	    			}
	    			xIn = xIn + args[x];
	    		}
				argumentos[1] = xIn;
				boolean vCorrecto = true;
				
				//Antes de comenzar con las validaciones, borramos primero las validaciones que había hasta el momento, 
				ValidacionDAO  validacionDAO = new ValidacionDAO();
				
				sqlMap.startTransaction();
				
				validacionDAO.setSqlMap(sqlMap);
				validacionDAO.borrarValidacionesKO(sqlMap);
				validacionDAO.borrarValidacionesActivas(sqlMap);
				
				sqlMap.commitTransaction();
				sqlMap.endTransaction();				

				iListaValidaciones = aListaValidaciones.length;
	    		// --------------------------------------------------------
				for(int i = 0; i < aListaValidaciones.length; i++){
	            	try {
						logger.info("COMIENZA LA EJECUCIÓN DE LA VALIDACIÓN: " + aListaValidaciones[i] );
	            		
						sqlMap.startTransaction();
						
						//Declaración de las clases ----------------------------------------
	            		Class<?> cls = Class.forName(aListaValidaciones[i]);
	            		Object o = cls.newInstance();
	            		
	            		//Declaración del metodo que se va a ejecutar ----------------------
	            		Method meth = cls.getMethod( "execute" , tipoparametros );
	            		
	            		//Ejecutando el metodo pasandoles los valores al parametro----------
	            		meth.invoke( o, argumentos );            			

	            		logger.info("FINALIZA LA EJECUCIÓN DE LA VALIDACIÓN: " + aListaValidaciones[i] );
	            		sqlMap.commitTransaction();
	            		iListaValidacionesOK +=1;
	            	} catch (Exception e){
						logger.error("Error al procesar la validacion:" + aListaValidaciones[i] + " Error: " + e.getMessage());
	            		iListaValidacionesError +=1;
						vCorrecto = false;
	            	} finally {
						sqlMap.endTransaction();
					}
	            }

	            // Actualizacion Final -----------------------------------------------------
	    		validacionDAO.marcarValidacionesRealizadas(sqlMap);
	            
				if  (vCorrecto == false) {
					Exception e = new Exception("ERROR AL PROCESAR LAS VALIDACIONES");
					throw e;
				}

				todoOK = true;
	            
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
					EjecucionesServicios.setEjecucion(sqlMap, "Batch_FACDIS_Validaciones", fechaejecucion, PropiedadesAplicacion.getRutaLog(), todoOK, "FACDIS");
				
					//Escribimos el resumen del proceso
					 EscribirResultado();
					
					//Escribimos el pie que muestra el tiempo que ha tardado el proceso
					LogProceso.Pie();
				}
			} catch (Exception ex) {
				logger.error("No se ha guardado la fecha de ejecucion en la base de datos: " + ex.getMessage());
				controlM_ExitCode = 1;
		    }
			
			logger.info("FINALIZACIÓN DEL PROCESO DE VALIDACIÓN (FACDIS)");
	    	
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
		
		ValidacionDAO  validacionDAO = new ValidacionDAO();
		Map<String,String> mapValidaciones;
		
		LogProceso.mensaje(" ");
		LogProceso.mensaje("PROCESO DE VALIDACION:");
		LogProceso.mensaje("  -----------------------------------------------  ");
		LogProceso.mensaje(" ");
		try {
			validacionDAO.setSqlMap(sqlMap);
			mapValidaciones = validacionDAO.selectValidaciones();
			
			LogProceso.mensaje(".   ERRORES DE VALIDACION DETECTADOS: ");
			if (mapValidaciones != null){
				Iterator it = mapValidaciones.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry e = (Map.Entry)it.next();
					String valor = "" + e.getValue();
					LogProceso.registro("" + e.getKey() , Long.parseLong(valor));
				}
				LogProceso.mensaje(" ");
			}	
			
		}catch (Exception e){
			logger.info("Error EscribirResultado(): " + e.getMessage());
		}
		
		LogProceso.registro("Tipos de Validaciones realizadas correctamente", iListaValidacionesOK);
		LogProceso.registro("Tipos de Validaciones Incorrectas", iListaValidacionesError);
		LogProceso.mensaje(" ");
	}
}