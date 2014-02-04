package com.eon.fatr.componentes;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.eon.fatr.utilidades.Fichero;
import com.eon.fatr.utilidades.PropiedadesAplicacion;
import com.eon.fatr.utilidades.Util;
import com.ibatis.sqlmap.client.SqlMapClient;
	
public class TratamientoDatosPIDAO {
	
	private static FileOutputStream out;
	private static BufferedOutputStream bos;
	private static Log logger = LogFactory.getFactory().getInstance(TratamientoDatosPIDAO.class);
	
	public List<?> obtenerFicherosSinRespuestaPI(SqlMapClient sqlMap, String nombre, String esquema) throws SQLException{
		logger.debug(" -- IN obtenerFicherosSinRespuesta -- ");
		HashMap<String, String> MAP = new HashMap<String, String>();
		MAP.put("nombre", nombre);
		MAP.put("esquema", esquema);
		List<?> lista = sqlMap.queryForList("comprobarFicheroEnviadoPI", MAP);
		
		logger.debug(" -- OUT obtenerFicherosSinRespuesta -- ");
		
		return lista;
	}

	public boolean actualizarIndFactPI(SqlMapClient sqlMap,HashMap MAP) throws SQLException {
		logger.debug(" -- IN obtenerFicherosSinRespuesta -- ");
		
		boolean aIndFact=false;
		sqlMap.update("ActualizarIndFactPI", MAP);
		aIndFact=true;
	
		logger.debug(" -- OUT obtenerFicherosSinRespuesta -- ");
		
		return aIndFact;
	}

	public List obtenerDatosFicheroPI(SqlMapClient sqlMap, String nombreInterfaz, int i) throws Exception{
		String fechaDesde = PropiedadesAplicacion.getInstance().getPropiedad("fechaDesde" + i).trim();
				
		String regMax = PropiedadesAplicacion.getInstance().getPropiedad("numeroLineas_E_" + i);
		String esquema = PropiedadesAplicacion.getInstance().getPropiedad("esquema" + i);
		HashMap map = new HashMap();
		map.put("identificador",nombreInterfaz);
		map.put("regMax",regMax);
		map.put("esquema", esquema);
		map.put("fechaDesde",fechaDesde);
		
		//Se obtienen los datos principales de la factura
		
		List listaDatos = sqlMap.queryForList("obtenerDatosFacturaPI", map);
		
		return listaDatos;
	}
	
	public boolean actualizarDatostPI(SqlMapClient sqlMap, int i) throws Exception {
		boolean aIndFact=false;
		String fechaDesde = PropiedadesAplicacion.getInstance().getPropiedad("fechaDesde" + i).trim();
		String esquema = PropiedadesAplicacion.getInstance().getPropiedad("esquema" + i);
		HashMap map = new HashMap();
		map.put("esquema", esquema);
		map.put("fechaDesde",fechaDesde);
		
		sqlMap.update("ActualizarIndFactPI", map);
		aIndFact=true;
		return aIndFact;
	}
	
	public boolean actualizarTablaPI(SqlMapClient sqlMap, String directorioConFecha, String sFActual, int iFichero) throws Exception
	{
	    boolean aResultado=false;
		
		String fecha=Util.fechaHoyYYYYMMDD();
		String indEnvResp="C";
		try{
			String esquema = PropiedadesAplicacion.getInstance().getPropiedad("esquema" + iFichero);
			
			sqlMap.startTransaction();
		
			//Insertar en la tabla de control los ficheros cargados del PI
			
			ControlInterfazDTO controlInterfazDTO= new ControlInterfazDTO(directorioConFecha +"/"+ sFActual , fecha , indEnvResp , esquema);
			ControlInterfazPIDAO controlInterfazPIDAO = new ControlInterfazPIDAO();
			controlInterfazPIDAO.insertFicherosTablaControlPI(sqlMap,controlInterfazDTO);
			
			// Actualizar en la tabla de control
			
			//String fichero = sFActual.substring(0,sFActual.length()-5) + "E";
			String fichero = sFActual;
			
			//Actualizar en la tabla de control los ficheros de Envio
			
			ControlInterfazDTO controlInterfazDTOAct=new ControlInterfazDTO(fichero, fecha , indEnvResp , esquema);
			TratamientoDatosPIDAO actualizarDatosPIDAO= new TratamientoDatosPIDAO();
			actualizarDatosPIDAO.actualizarFicherosTablaControlPI(sqlMap, controlInterfazDTOAct);
			aResultado=true;
			
			//Se realiza el commit
			sqlMap.commitTransaction();
			
		}catch(Exception e){
			logger.error(" Error al actualizar el fichero procesado " + sFActual);
		}finally{
			sqlMap.endTransaction();
		}
		return aResultado;
	}
	
	public boolean actualizarFicherosTablaControlPI(SqlMapClient sqlMap, ControlInterfazDTO controlInterfazDTO) throws SQLException{
		logger.debug(" -- IN actualizarFicherosTablaControl -- ");
		
		boolean bResultado = false;
		sqlMap.update("ActualizarTablaControlPI", controlInterfazDTO);
		bResultado = true;
		
		logger.debug(" -- OUT actualizarFicherosTablaControl -- ");
		
		return bResultado;
	}
	
	public void crearFicheroPI(String nombreInterfaz, String ruta) throws FileNotFoundException{
		out = new FileOutputStream(ruta + nombreInterfaz);
		bos = new BufferedOutputStream(out);
	}
	
	public void cerrarFichero() throws IOException, Exception {
		bos.close();
		out.close();
		bos = null;
		out = null;
	}
	
	/**
	 * @param lista - Lista con los datos a escribir en el fichero.
	 * @param nombreInforme - Nombre del informe a generar. 
	 * @param cadenaCabecera - Cadena con los nombres de las columnas del informe.
	 * @param ruta - Ruta del fichero.
	 * @param modo - "C" si el fichero se escribe completo y "P" si se escribe por paquetes.
	 * @return
	 * @throws Exception
	 */
	public int generarFicheroTextoPI(SqlMapClient sqlMap, List lista, String nombreInterfaz, String ruta, int iSeccion) throws Exception{
		
		int error = 1;
		
		try{
			error = Fichero.leerPropiedadesEscritura("" + iSeccion);
			if (error == 1){
				logger.error("Error al recuperar las propiedades del properties para fichero " + nombreInterfaz);
			}
			else{
				//Se cargan las longitudes de los campos del fichero de escritura 
				List listaLongitud_Escritura = Fichero.cargarLongitudesCampos("ESCRITURA"); 
		        //Se cargan los tipos de los campos del fichero de escritura 
		        List listaTipo_Escritura = Fichero.cargarTiposCampos("ESCRITURA"); 
				
		        List listaFinal = obtenerListaFinalPI(sqlMap, lista, iSeccion);
		        
		        Fichero.crearFichero(ruta + nombreInterfaz);
				
		        Fichero.escribirFichero(listaFinal, listaTipo_Escritura, listaLongitud_Escritura);
		        
		        Fichero.cerrarFichero();
		        
		        error = 0;
			}
		}catch(FileNotFoundException fnfe){
			logger.error("MIRCOMPI: ERROR. Fichero " + nombreInterfaz + " no encontrado. " + fnfe.toString());
			error = 1;
		}
		catch(SQLException ade){
			logger.error("MIRCOMPI: ERROR en acceso a datos de fichero " + nombreInterfaz + ". " + ade.toString());
			error = 1;
		}
		catch(IOException ioe){
			logger.error("MIRCOMPI: ERROR en acceso a fichero " + nombreInterfaz + ". " + ioe.toString());
			error = 1;
		}
		catch(Exception e){
			logger.error("MIRCOMPI: ERROR al generar el fichero " + nombreInterfaz + ". " + e.toString());
			error = 1;
		}
		
		logger.debug(" -- OUT generarFicheroTexto");
		
		return error;
	}
	
	private List obtenerListaFinalPI( SqlMapClient sqlMap, List listaFactura, int iSeccion) throws java.sql.SQLException, java.io.IOException, Exception {
	   	
   		List listaFinal = new ArrayList();
   		
   		Iterator it = listaFactura.iterator();
		//try{
   		while(it.hasNext()) {
   			FacdisFacturaDTO f = (FacdisFacturaDTO)it.next();
   			List lDatosFactura = new ArrayList();
   			//Se van añadiendo datos de la factura a la lista para añadir a la lista final
   			lDatosFactura.add( f.getNum_factura() == null?"":f.getNum_factura()); // NUM_FACTURA
   			lDatosFactura.add( f.getFecha_limite_pago() == null?"":f.getFecha_limite_pago());//FECHACOBRO
   			lDatosFactura.add( f.getCod_solicitud() == null?"":f.getCod_solicitud());//NROAGRUPACION
   			lDatosFactura.add( f.getCups() == null?"":f.getCups()); // CUPS
 //  			lDatosFactura.add( ""+f.getId_fatr());//IDFATR
   			lDatosFactura.add( f.getId_tipofactrec_cne() == null?"":f.getId_tipofactrec_cne());//INDFACTREC
			//Al final se añade la información a la lista final
			listaFinal.add(lDatosFactura);
   		}
   		return listaFinal;
	}
   
	public boolean actualizarTablaEnvPI(SqlMapClient sqlMap, String directorioConFechaSal, String sFActual, int iFichero) throws Exception {
	    boolean eResultado=false;
		String fecha=Util.fechaHoyYYYYMMDD();
		try{
			String esquema = PropiedadesAplicacion.getInstance().getPropiedad("esquema" + iFichero);
			// Actualizar en la tabla de control
			sqlMap.startTransaction();
			
			String fichero=directorioConFechaSal+"/"+sFActual;
			String indEnvResp="E";
		
			ControlInterfazDTO controlInterfazDTO= new ControlInterfazDTO(fichero , fecha , indEnvResp , esquema);
			ControlInterfazPIDAO controlInterfazDAO = new ControlInterfazPIDAO();
			controlInterfazDAO.insertFicherosTablaControlPI(sqlMap,controlInterfazDTO);
			
			eResultado=true;
			
			//Se realiza el commit
			sqlMap.commitTransaction();
			
		}catch(Exception e){
			logger.error(" Error al actualizar la tabla de control del fichero procesado " + sFActual);
		}finally{
			sqlMap.endTransaction();
		}
		
		return eResultado;
	}
}