package com.eon.fatr.componentes;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.eon.fatr.utilidades.Fichero;
import com.eon.fatr.utilidades.PropiedadesAplicacion;
import com.eon.fatr.utilidades.Util;
import com.ibatis.sqlmap.client.SqlMapClient;
	
public class TratamientoDatosCSADAO {
	
	private static FileOutputStream out;
	private static BufferedOutputStream bos;
	private static Log logger = LogFactory.getFactory().getInstance(TratamientoDatosCSADAO.class);
	
	public List<?> obtenerFicherosSinRespuesta(SqlMapClient sqlMap, String nombre, String esquema) throws SQLException{
		logger.debug(" -- IN obtenerFicherosSinRespuesta -- ");
		HashMap<String, String> MAP = new HashMap<String, String>();
		MAP.put("nombre", nombre);
		MAP.put("esquema", esquema);
		
		List<?> lista = sqlMap.queryForList("comprobarFicheroEnviado", MAP);
		
		logger.debug(" -- OUT obtenerFicherosSinRespuesta -- ");
		
		return lista;
	}
	
	public static List recuperarPeriodosMagnitudes(SqlMapClient sqlMap, String idFactura, String magnitud, int iSeccion) throws Exception{
		logger.debug(" -- IN recuperarPeriodosMagnitudes -- ");
		String esquema = PropiedadesAplicacion.getInstance().getPropiedad("esquema" + iSeccion);
		HashMap map = new HashMap();
		map.put("idFactura",idFactura);
		map.put("magnitud",magnitud);
		map.put("esquema", esquema);
		
		List lista = sqlMap.queryForList("obtenerPeriodosMagnitud", map);
		
		logger.debug(" -- OUT recuperarPeriodosMagnitudes -- ");
		
		return lista;
	}
	
	public static String recuperarMagnitud(SqlMapClient sqlMap, String magnitud, int iSeccion) throws Exception{
		logger.debug(" -- IN recuperarMagnitud -- ");
		String esquema = PropiedadesAplicacion.getInstance().getPropiedad("esquema" + iSeccion);
		HashMap map = new HashMap();
		map.put("magnitud",magnitud);
		map.put("esquema", esquema);		
		
		magnitud = (String) sqlMap.queryForObject("obtenerMagnitud", map);

		logger.debug(" -- OUT recuperarMagnitud -- ");

		return magnitud;
	}
	
	public static List recuperarDatosLecturas(SqlMapClient sqlMap, String idFactura, String magnitud, int iSeccion) throws Exception{
		logger.debug(" -- IN recuperarDatosLecturas -- ");
		String esquema = PropiedadesAplicacion.getInstance().getPropiedad("esquema" + iSeccion);
		HashMap map = new HashMap();
		map.put("idFactura",idFactura);
		map.put("magnitud",magnitud);
		map.put("esquema", esquema);
		
		List lista = sqlMap.queryForList("recuperarDatosLecturas", map);
		
		logger.debug(" -- OUT recuperarDatosLecturas -- ");
		
		return lista;
	}
	
	public String procesarDatos(SqlMapClient sqlMap, List listaReg, String nombre, String esquema) throws SQLException{
		logger.debug(" -- IN procesarDatos -- ");
		
		Iterator it = listaReg.iterator();
		String sTipo = "";
		String sValor = "";
		String sFecha = "";
		TratamientoDatosCSADAO tratamientoDatosDAO= new TratamientoDatosCSADAO();
		
		while(it.hasNext()){
			List lista = (List)it.next();
			if(lista != null && lista.size() > 0){
				sTipo = (String)lista.get(0);
				sValor = (String)lista.get(1);
				
				//Dependiendo del tipo de dato sabemos si es una fecha o un ID de factura
				//	01 -> Fecha
				//	02 -> ID de factura
				if(sTipo.equalsIgnoreCase("01")){
					//Se recupera la fecha
					sFecha = sValor;
					
				} else {
					//Se actualiza el indicador de la factura
					if(sTipo.equalsIgnoreCase("02")){
						try{	
							HashMap MAP = new HashMap();
							MAP.put("idFact", sValor);
							MAP.put("sFecha", sFecha);
							MAP.put("sIndicador", nombre);
							MAP.put("esquema",esquema);
							
							tratamientoDatosDAO.actualizarIndFact(sqlMap,MAP);
						}catch(SQLException e){
							logger.error(" Se ha producido un error al actualizar la factura " + sValor + ". ERROR: " + e.getMessage());
						}
					}
				}
			}
		}
		
		logger.debug(" -- OUT procesarDatos -- ");
		return sFecha;
	}
	
	public boolean actualizarIndFact(SqlMapClient sqlMap,HashMap MAP) throws SQLException {
		logger.debug(" -- IN obtenerFicherosSinRespuesta -- ");
		
		boolean aIndFact=false;
		sqlMap.update("ActualizarIndFact", MAP);
		aIndFact=true;
	
		logger.debug(" -- OUT obtenerFicherosSinRespuesta -- ");
		
		return aIndFact;
	}

	public List obtenerDatosFichero(SqlMapClient sqlMap, String nombreInterfaz, int i)throws Exception{
		String fechaDesde = PropiedadesAplicacion.getInstance().getPropiedad("fechaDesde" + i).trim();
		String distOK = PropiedadesAplicacion.getInstance().getPropiedad("distribuidora_OK" + i).trim();
		String distKO = PropiedadesAplicacion.getInstance().getPropiedad("distribuidora_KO" + i).trim();
		String comerOK = PropiedadesAplicacion.getInstance().getPropiedad("comercializadora_OK" + i).trim();
		String comerKO = PropiedadesAplicacion.getInstance().getPropiedad("comercializadora_KO" + i).trim();
		String tarifas = PropiedadesAplicacion.getInstance().getPropiedad("tarifas" + i).trim();
		String tension = PropiedadesAplicacion.getInstance().getPropiedad("tension" + i).trim();
				
		String regMax = PropiedadesAplicacion.getInstance().getPropiedad("numeroLineas_E_" + i);
		String esquema = PropiedadesAplicacion.getInstance().getPropiedad("esquema" + i);
		HashMap map = new HashMap();
		map.put("identificador",nombreInterfaz);
		map.put("regMax",regMax);
		map.put("esquema", esquema);
		map.put("fechaDesde",fechaDesde);
		map.put("tension", tension);
		
		if(!distOK.equals("")) map.put("distOK",Arrays.asList(distOK.split(";")));
		if(!distKO.equals("")) map.put("distKO",Arrays.asList(distKO.split(";")));
		if(!comerOK.equals("")) map.put("comerOK",Arrays.asList(comerOK.split(";")));
		if(!comerKO.equals("")) map.put("comerKO",Arrays.asList(comerKO.split(";")));
		if(!tarifas.equals("")) map.put("tarifas",Arrays.asList(tarifas.split(";")));
		
		//Se obtienen los datos principales de la factura
		List listaDatos = sqlMap.queryForList("obtenerDatosFactura", map);
		
		return listaDatos;
	}
	
	public boolean actualizarTabla(SqlMapClient sqlMap, String directorioConFecha, String sFActual, int iFichero) throws Exception
	{
	    boolean aResultado=false;
		
		String fecha=Util.fechaHoyYYYYMMDD();
		String indEnvResp="C";
		try{
			String esquema = PropiedadesAplicacion.getInstance().getPropiedad("esquema" + iFichero);
			
			sqlMap.startTransaction();
		
			//Insertar en la tabla de control los ficheros cargados del CSA
			
			ControlInterfazDTO controlInterfazDTO= new ControlInterfazDTO(directorioConFecha +"/"+ sFActual , fecha , indEnvResp , esquema);
			ControlInterfazDAO controlInterfazDAO = new ControlInterfazDAO();
			controlInterfazDAO.insertFicherosTablaControl(sqlMap,controlInterfazDTO);
			
			// Actualizar en la tabla de control
			
			String fichero = sFActual.substring(0,sFActual.length()-5) + "E";
			
			//Actualizar en la tabla de control los ficheros de Envio
			
			ControlInterfazDTO controlInterfazDTOAct=new ControlInterfazDTO(fichero, fecha , indEnvResp , esquema);
			TratamientoDatosCSADAO actualizarDatosDAO= new TratamientoDatosCSADAO();
			actualizarDatosDAO.actualizarFicherosTablaControl(sqlMap, controlInterfazDTOAct);
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
	
	public boolean actualizarFicherosTablaControl(SqlMapClient sqlMap, ControlInterfazDTO controlInterfazDTO) throws SQLException{
		logger.debug(" -- IN actualizarFicherosTablaControl -- ");
		
		boolean bResultado = false;
		sqlMap.update("ActualizarTablaControl", controlInterfazDTO);
		bResultado = true;
		
		logger.debug(" -- OUT actualizarFicherosTablaControl -- ");
		
		return bResultado;
	}
	
	public void crearFichero(String nombreInterfaz, String ruta) throws FileNotFoundException{
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
	public int generarFicheroTexto(SqlMapClient sqlMap, List lista, String nombreInterfaz, String ruta, int iSeccion) throws Exception{
		
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
				
		        List listaFinal = obtenerListaFinal(sqlMap, lista, iSeccion);
		        
		        Fichero.crearFichero(ruta + nombreInterfaz);
				
		        Fichero.escribirFichero(listaFinal, listaTipo_Escritura, listaLongitud_Escritura);
		        
		        Fichero.cerrarFichero();
		        
		        error = 0;
			}
		}catch(FileNotFoundException fnfe){
			logger.error("MIRCOMCSA: ERROR. Fichero " + nombreInterfaz + " no encontrado. " + fnfe.toString());
			error = 1;
		}
		catch(SQLException ade){
			logger.error("MIRCOMCSA: ERROR en acceso a datos de fichero " + nombreInterfaz + ". " + ade.toString());
			error = 1;
		}
		catch(IOException ioe){
			logger.error("MIRCOMCSA: ERROR en acceso a fichero " + nombreInterfaz + ". " + ioe.toString());
			error = 1;
		}
		catch(Exception e){
			logger.error("MIRCOMCSA: ERROR al generar el fichero " + nombreInterfaz + ". " + e.toString());
			error = 1;
		}
		
		logger.debug(" -- OUT generarFicheroTexto");
		
		return error;
	}
	
	private List obtenerListaFinal( SqlMapClient sqlMap, List listaFactura, int iSeccion) throws java.sql.SQLException, java.io.IOException, Exception{
	   	
   		List listaFinal = new ArrayList();
   		
   		Iterator it = listaFactura.iterator();
		//try{
   		while(it.hasNext()){
   			FacdisFacturaDTO f = (FacdisFacturaDTO)it.next();
   			List lDatosFactura = new ArrayList();
   			//Se van añadiendo datos de la factura a la lista para añadir a la lista final
   			String idFactura = ""+f.getId_fatr();
   			String tarifa = f.getDesc_tarifa_cne() == null?"":f.getDesc_tarifa_cne();
   			
   			lDatosFactura.add( ""+f.getId_fatr()); // ID factura
   			lDatosFactura.add( f.getCups() == null?"":f.getCups()); // CUPS
   			lDatosFactura.add( f.getNum_factura() == null?"":f.getNum_factura()); // NUM_FACTURA
   			lDatosFactura.add( f.getId_distribuidora_cne() == null?"":f.getId_distribuidora_cne()); // Distribuidora
   			lDatosFactura.add( f.getId_comercializadora_cne() == null?"":f.getId_comercializadora_cne()); // Comercializadora

   			lDatosFactura.add( f.getDesc_tipofactura_cne() == null?"":f.getDesc_tipofactura_cne()); // Tipo Factura
   			lDatosFactura.add( f.getDesc_tipofactrec_cne() == null?"":f.getDesc_tipofactrec_cne()); // Ind Fact Rect

   			lDatosFactura.add( f.getNum_factura_rectificada() == null?"":f.getNum_factura_rectificada()); // Num Fact Rect
   			lDatosFactura.add( f.getFecha_factura() == null?"":f.getFecha_factura()); // Fecha Factura
   			
   			lDatosFactura.add( f.getDesc_tipofactper_cne() == null?"":f.getDesc_tipofactper_cne()); // Tipo Facturación
   			
   			lDatosFactura.add( f.getTipoOrigen() == null?"":f.getTipoOrigen()); // Tipo Origen
   			lDatosFactura.add(tarifa); // Tarifa
   			lDatosFactura.add( f.getFecha_ini() == null?"":f.getFecha_ini()); // Fecha Inicio
   			lDatosFactura.add( f.getFecha_fin() == null?"":f.getFecha_fin()); // Fecha Fin
   			
   			//AT
   			int iAT = f.getAt();
   			
   			//Potencia contratada
   			List PC = new ArrayList();
   			
   			//Potencia demandada
   			List PD = new ArrayList();
   			
   			//Se recuperan los periodos correspondientes a Periodos para pot. contratada y demandada = maxima.
   			List lstPeriodosMagnitud = recuperarPeriodosMagnitudes(sqlMap, idFactura, "11", iSeccion); 

   			for(int z=0;z<6;z++){
				String potCont = "";
				String potDem = "";
				
   				if(z<lstPeriodosMagnitud.size()){
					PeriodosMagnitudDTO periodo = (PeriodosMagnitudDTO)lstPeriodosMagnitud.get(z);
	   				
	   				//Potencia Contratada
	   				if(periodo.getPotContratada() != null && !periodo.getPotContratada().equals("")) potCont = periodo.getPotContratada();
	   				
	   				//Potencia Demandada
	   				if(periodo.getPotMaxDemandada() != null && !periodo.getPotMaxDemandada().equals("")) potDem = periodo.getPotMaxDemandada();
	   			}
   				
   				PC.add(potCont);
   				PD.add(potDem);
   			}
   			
   			//Se añaden las potencias contratadas
   			lDatosFactura.add(PC.get(0));
   			lDatosFactura.add(PC.get(1));
   			lDatosFactura.add(PC.get(2));
   			lDatosFactura.add(PC.get(3));
   			lDatosFactura.add(PC.get(4));
   			lDatosFactura.add(PC.get(5));
   			
   			//Se guardan los datos principales de la factura en una lista
   			List listaDatosPpal = new ArrayList(lDatosFactura);
   			
   			//Dependiendo de las magnitudes se insertarán más o menos registros
   			//Dependiendo de las magnitudes que tengamos sabremos los registros que es necesario insertar
   			//en el fichero. Será uno por magnitud.
   			String magnitudes = PropiedadesAplicacion.getInstance().getPropiedad("magnitudes" + iSeccion);
   			String magnitud = "";
   			String idMagnitud = "";
   			String m[] = magnitudes.split(";");
   			
   			//Potencia de la magnitud
   			List PotM = null;
   			
   			for(int j = 0;j<m.length;j++){
   				PotM = new ArrayList();
   				//Si hay más de una magnitud se debe insertar un registro por magnitud
   				if(j>0){
   					//Se añade el registro correspondiente a la magnitud anterior
   					listaFinal.add(lDatosFactura);
   					//Se recuperan los datos principales válidos para la siguiente magnitud
   					lDatosFactura = new ArrayList(listaDatosPpal);
   				}
   				
   				idMagnitud = m[j];
   				
   				magnitud =recuperarMagnitud(sqlMap, idMagnitud, iSeccion);
   				
   				if(!idMagnitud.equals("11"))lstPeriodosMagnitud = recuperarPeriodosMagnitudes(sqlMap, idFactura, idMagnitud, iSeccion);
   				
   				for(int z=0;z<6;z++){
   					String dato = "";
   	   				String valorPot = "";
   	   				
   	   				if(idMagnitud.equals("11")){
   	   					valorPot = (String)PD.get(z);
   	   					logger.debug(valorPot);
   	   				}
   	   				else{
   	   				
	   	   				if(z<lstPeriodosMagnitud.size() && lstPeriodosMagnitud.size() > 0){
	   						PeriodosMagnitudDTO periodo = (PeriodosMagnitudDTO)lstPeriodosMagnitud.get(z);
	   		   				
	   						if(idMagnitud.equals("7")) dato = periodo.getValor();
	   		   				if(idMagnitud.equals("1")) dato = periodo.getValor();
	   		   				if(idMagnitud.equals("10")) dato = periodo.getExcesoPotencia();
   		   					
	   		   				if(dato != null && !dato.equals("")) valorPot = dato;
	   		   			}
   	   				}
   	   				
   	   				//Se añade el valor de la potencia a la lista
   	   				PotM.add(valorPot);
   	   			}
   				
   				//Se añaden los datos
   				lDatosFactura.add(PotM.get(0));
   	   			lDatosFactura.add(PotM.get(1));
   	   			lDatosFactura.add(PotM.get(2));
   	   			lDatosFactura.add(PotM.get(3));
   	   			lDatosFactura.add(PotM.get(4));
   	   			lDatosFactura.add(PotM.get(5));
   	   			
   	   			lDatosFactura.add(magnitud);
   	   			
   	   			//Se recupera fecha max, DH y número de serie de lecturas dependiendo de la magnitud.
   	   			List lstLecturas = recuperarDatosLecturas(sqlMap,idFactura, idMagnitud,iSeccion);
   	   			
   	   			String fechaMax = "";
	   			String DH = "";
	   			String numSerie = "";
  				String idPeriodo = "";
	   			String sIndDH = "";
	   			String sPeriodosDH = "";
	   			String consumoPunto = "";
	   			String consumoLlano = "";
	   			String consumoValle = "";
	   			
   	   			//Consumos equipo definitivo
	   			String D1 = "";
	   			String D2 = "";
	   			String D3 = "";
	   			String D4 = "";
	   			String D5 = "";
	   			String D6 = "";
	   			
	   			boolean bEquipoDef = false;
	   			
	   			if(lstLecturas.size() > 0){
   	   				
	   	   			//Se recorren los datos generales del primer registro
   	   				HashMap hmDatosL = (HashMap)lstLecturas.get(0);
   	   				
   	   				//Se informan los datos
	   	   			fechaMax = (String)hmDatosL.get("FECHAMAX") == null?"":(String)hmDatosL.get("FECHAMAX");
	   	   			DH = (String)hmDatosL.get("CODIGODH") == null?"":(String)hmDatosL.get("CODIGODH");
	   	   			numSerie = (String)hmDatosL.get("NUMSERIE") == null?"":((String)hmDatosL.get("NUMSERIE")).trim();
	   	   			idPeriodo = (String)hmDatosL.get("ID_PERIODO") == null?"":(String)hmDatosL.get("ID_PERIODO");
	   	   			if(idPeriodo.length() == 2)
	   	   				sIndDH = idPeriodo.substring(0,1);
	   	   			
	   	   			//Según la DH tendremos distintos periodos
	   	   			sPeriodosDH = obtenerPeridos(DH);
	   	   			
	   	   			//Para la obtención de consumos nos fijamos en primer lugar 
	   	   			//de que CODIGODH coincida con el primer dígito de ID_PERIODO
	   	   			//y de que el número de registros recuperados coincida con los esperados
	   	   			//para la DH. Este tratamiento se hace únicamente para magnitud 7(AE)
	   	   			if((sIndDH.equals(DH) && lstLecturas.size() == Integer.parseInt(sPeriodosDH)) || !idMagnitud.equals("7")){
	   	   				String consumo = "";
	   	   				
	   	   				for(int i =0;i<lstLecturas.size();i++){
	   	   					hmDatosL = (HashMap)lstLecturas.get(i);
	   	   					idPeriodo = (String)hmDatosL.get("ID_PERIODO") == null?"":(String)hmDatosL.get("ID_PERIODO");
	   	   					consumo = hmDatosL.get("CONSUMOCALC") == null?"":"" + hmDatosL.get("CONSUMOCALC");
	   	   					
	   	   					//Si se trata de AT las inserciones se hacen en el aparato definitivo
	   	   					if(iAT == 1){
	   	   						//Solamente se considera que son válidas las DH 3 y 6
	   	   						if(DH.equals("3") || DH.equals("6")){
	   	   							
		   	   						//DH6 -> Equipo definitivo
			   	   					if(i == 0) D1 = consumo;
			   	   					if(i == 1) D2 = consumo;
			   	   					if(i == 2) D3 = consumo;
			   	   					if(i == 3) D4 = consumo;
			   	   					if(i == 4) D5 = consumo;
			   	   					if(i == 5) D6 = consumo;
	   	   						}
	   	   					} else {
	   	   					
		   	   					//Según el idPeriodo se determina a que consumo pertenece
		   	   					//DH0 -> Periodo 01 -> Consumo Llano (excepto tarifa 2.0DHA que es consumo Punta)
		   	   					if(idPeriodo.equals("01")) {
		   	   						if(tarifa.equals("2.0DHA")) consumoPunto = consumo;
		   	   						else consumoLlano = consumo;
		   	   					}
		   	   					//DH0 -> Periodo 03 -> Consumo Valle
		   	   					if(idPeriodo.equals("03")) consumoValle = consumo;
		   	   					
		   	   					//DH1 -> Periodo 10 -> Consumo Llano
		   	   					if(idPeriodo.equals("10")) consumoLlano = consumo;
		   	   					
		   	   					//DH2 -> Periodo 21 -> Consumo Punta
		   	   					if(idPeriodo.equals("21")) consumoPunto = consumo;
		   	   					//DH2 -> Periodo 22 -> Consumo Llano
		   	   					if(idPeriodo.equals("22")) consumoLlano = consumo;
		   	   					
		   	   					//DH3 -> Periodo 31 -> Consumo Punta
		   	   					if(idPeriodo.equals("31")) consumoPunto = consumo;
		   	   					//DH3 -> Periodo 32 -> Consumo Llano
		   	   					if(idPeriodo.equals("32")) consumoLlano = consumo;
		   	   					//DH3 -> Periodo 33 -> Consumo Valle
		   	   					if(idPeriodo.equals("33")) consumoValle = consumo;
		   	   					
		   	   					//DH4 -> Periodo 41 -> Consumo Punta
		   	   					if(idPeriodo.equals("41")) consumoPunto = consumo;
		   	   					//DH4 -> Periodo 42 -> Consumo Llano
		   	   					if(idPeriodo.equals("42")) consumoLlano = consumo;
		   	   					//DH4 -> Periodo 43 -> Consumo Valle
		   	   					if(idPeriodo.equals("43")) consumoValle = consumo;
		   	   					
		   	   					//DH6 -> Equipo definitivo
		   	   					if(idPeriodo.equals("61")) D1 = consumo;
		   	   					if(idPeriodo.equals("62")) D2 = consumo;
		   	   					if(idPeriodo.equals("63")) D3 = consumo;
		   	   					if(idPeriodo.equals("64")) D4 = consumo;
		   	   					if(idPeriodo.equals("65")) D5 = consumo;
		   	   					if(idPeriodo.equals("66")) D6 = consumo;
	   	   					}
	   	   				}
	   	   			}
   	   			}
   	   			
   	   			//Fecha Max
   	   			lDatosFactura.add(fechaMax);
   	   			
   	   			//Tipo de equipo.  
   	   			//BT y DH <> 6 --> Equipo Transitorio.
   	   			//AT o DH = 6  --> Equipo Definitivo.
   	   			String tipoEquipo = "T";
   	   			if(iAT == 1 || DH.equals("6")){
   	   				tipoEquipo = "R";
   	   				bEquipoDef = true;
   	   			}
   	   			lDatosFactura.add(tipoEquipo);
   	   			
   	   			//Datos del equipo transitorio
   	   			lDatosFactura.add(consumoPunto);
   	   			lDatosFactura.add(consumoLlano);
   	   			lDatosFactura.add(consumoValle);
	   	   		
   	   			if(!bEquipoDef){
   	   				lDatosFactura.add(DH);
   	   				lDatosFactura.add(numSerie);
	   	   		}
	   	   		else{ 
	   	   			lDatosFactura.add("");
	   	   			lDatosFactura.add("");
	   	   		}
	   	   		
	   	   		//Datos del equipo transitorio
	   			lDatosFactura.add(D1);
	   			lDatosFactura.add(D2);
	   			lDatosFactura.add(D3);
	   	   		lDatosFactura.add(D4);
	   	   		lDatosFactura.add(D5);
	   	   		lDatosFactura.add(D6);
	   	   		if(bEquipoDef)
	   	   			lDatosFactura.add(numSerie);
	   	   		else
	   	   			lDatosFactura.add("");
	   	   		
	   	   		lDatosFactura.add( f.getTicket_energetico() ==null?"":f.getTicket_energetico()); // Ticket Energetico
	   	   	}
   			
   			//Al final se añade la información a la lista final
   			listaFinal.add(lDatosFactura);
   		   		   			
   		}
   		return listaFinal;
   }
   
	public boolean actualizarTablaEnv(SqlMapClient sqlMap, String directorioConFechaSal, String sFActual, int iFichero) throws Exception {
	    boolean eResultado=false;
		String fecha=Util.fechaHoyYYYYMMDD();
		try{
			String esquema = PropiedadesAplicacion.getInstance().getPropiedad("esquema" + iFichero);
			// Actualizar en la tabla de control
			sqlMap.startTransaction();
			
			String fichero=directorioConFechaSal+"/"+sFActual;
			String indEnvResp="E";
		
			ControlInterfazDTO controlInterfazDTO= new ControlInterfazDTO(fichero , fecha , indEnvResp , esquema);
			ControlInterfazDAO controlInterfazDAO = new ControlInterfazDAO();
			controlInterfazDAO.insertFicherosTablaControl(sqlMap,controlInterfazDTO);
			
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
	
	public static String obtenerPeridos(String DH) {
		String nroPeriodos = "0";

		if(DH.equals("0"))nroPeriodos = "2";
		if(DH.equals("1"))nroPeriodos = "1";
		if(DH.equals("2"))nroPeriodos = "2";
		if(DH.equals("3"))nroPeriodos = "3";
		if(DH.equals("4"))nroPeriodos = "3";
		if(DH.equals("6"))nroPeriodos = "6";
		return nroPeriodos;
	}
}