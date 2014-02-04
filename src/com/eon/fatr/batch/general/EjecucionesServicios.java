package com.eon.fatr.batch.general;

//Importaciones
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Date;

import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * 
 * @author A157483
 *
 */
public class EjecucionesServicios {
	//Definicion de variables
	@SuppressWarnings("serial")
	class CodigoVacio extends Exception{}

	public static void setEjecucion(SqlMapClient sqlMap, String codigo, String fechaEjecucion, String rutaLog, boolean todoOK, String esquema) throws SQLException, Exception{
		//
		int codigo_resultado = 2;
		String ruta_log = "";
		
		try{
			//Verificacion de valores no nulos
			if (codigo == null || codigo.equalsIgnoreCase("") || sqlMap == null){
				//Falta codigo del servicio
				codigo_resultado = 1;
			}
			else{
				//Creamos el hashmap para almacenar los parámatros
				HashMap<String, String> datos = new HashMap<String, String>();
				datos.put("codigo", codigo);
				datos.put("esquema", esquema);

				//Si tenemos todos los parámetros correctos ...
				datos.put("fecha", getFecha(fechaEjecucion) );
				
				//Asignamos el valor del campo ruta_log
				if (rutaLog != null){ ruta_log = rutaLog; }
				datos.put("ruta_log", ruta_log);
				
				//Abrimos la transaccion para asegurar que el update y el insert se hacen los dos a la vez
				sqlMap.startTransaction();
				
				//Se actualiza la fecha de última ejecución correcta
				if (todoOK){
					codigo_resultado = 3;
					//Lanzamos la actualización de la tabla SERVICIO para cambiar la fecha de última ejecución
					sqlMap.update("BatchActualizaFechaUltimaEjecucion", datos);
				}
				
				codigo_resultado = 4;
				//Insertamos en Ejecuciones_servicios los datos del servicio
				sqlMap.insert("Insert_EJECUCIONES_SERVICIOS", datos);
				
				sqlMap.commitTransaction();
				sqlMap.endTransaction();
				
				//Si todo va correcto, arrojamos un código 0 como resultado
				codigo_resultado = 0;
			}
		}finally{
			//Lanzamos la excepción si se produce alguna interrupción antes de terminar el proceso correctamente
			switch (codigo_resultado){
			case 0:
				break;
			case 1:
				throw new Exception("Codigo de servicio vacio");
			case 2:
				throw new Exception("Fecha incorrecta");
			default:
				throw new Exception("Error al insertar o actualizar la fecha de ejcucion del servicio");
		}
	}
}	
	
	/**
	 * 
	 * @param fechaEjecucion
	 * @return
	 */
	private static String getFecha(String fechaEjecucion){
		//Definicion de variables
		String fecha = "";
	
		//Verificacion de la fecha
		if ( fechaEjecucion == null || fechaEjecucion.equalsIgnoreCase("") ){
			//Si la fecha es nula o vacía, asignamos la fecha de hoy como fecha de ejecución
			Date fechaHoy = new Date();
			//Le damos formato a la fecha
			SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			fecha = formato.format(fechaHoy);
		}
		else {
			fecha = fechaEjecucion;
		}
		
		return fecha;
	}
}
