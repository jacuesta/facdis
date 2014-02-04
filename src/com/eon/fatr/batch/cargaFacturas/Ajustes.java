package com.eon.fatr.batch.cargaFacturas;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.eon.fatr.componentes.FacdisFacturaDTO;
import com.eon.fatr.componentes.FacdisFacturaLecturaDTO;
import com.ibatis.sqlmap.client.SqlMapClient;

public class Ajustes {
	private static Log logger = LogFactory.getFactory().getInstance(Ajustes.class);

//	public static void actualizarDatos(SqlMapClient sqlMap, String IdInicio, String IdFin) {
	public static void actualizarDatos(SqlMapClient sqlMap) {		
		logger.info("INICIO DEL PROCESO DE AJUSTES.");
//		Actualizar Factura NORMAL
		logger.info("COMIENZA AJUSTE NUMERO RECTIFICADA");
		actualizarNumRectificada(sqlMap);
		logger.info("TERMINA AJUSTE NUMERO RECTIFICADA");
//		logger.info("COMIENZA AJUSTE DE LECTURAS PARA MEDIDAS");
//		actualizarLecturasParaMedidas(sqlMap, IdInicio, IdFin);
//		logger.info("TERMINA AJUSTE DE LECTURAS PARA MEDIDAS");
		logger.info("FIN DEL PROCESO DE AJUSTES.");
	}

	private static void actualizarNumRectificada(SqlMapClient sqlMap){
		int id_fatr = 0;
		String cups = "";
		String num_factura = "";
		String num_factura_rectificada = "";		

		try {
			sqlMap.startTransaction();
			
			List lstFacturas = (List) sqlMap.queryForList("recuperarFacturasMlAtrNormal", null);
			
			for (int i=0;i<lstFacturas.size();i++) {
				FacdisFacturaDTO factura = new FacdisFacturaDTO();
				factura = (FacdisFacturaDTO) lstFacturas.get(i);
				
				id_fatr = factura.getId_fatr();
				cups = factura.getCups();
				num_factura = factura.getNum_factura();
				num_factura_rectificada = factura.getNum_factura_rectificada();

				logger.debug("Actualizamos Normal del CUPS: " + cups + " NumFactura: " + num_factura_rectificada + " con Rectificada: " + num_factura);
				actualizarFactura(sqlMap, id_fatr, cups, num_factura, num_factura_rectificada);
			}
			sqlMap.commitTransaction();

		} catch (Exception eop) {
			logger.error("ERROR: al actualizarNumRectificada " + eop);
		} finally {
			try {
				sqlMap.endTransaction();
			} catch (SQLException e){logger.error("ERROR: al cerrar la transacción de actualizarNumRectificada " + e);}
		}
	}

/*
	private static String tieneRegistro(SqlMapClient sqlMap, String numFactura, String IdDistribuidora, String tipoRectificadora, String tipoBus) throws SQLException {
		HashMap map = new HashMap();
		map.put("numFactura", numFactura);
		map.put("IdDistribuidora", IdDistribuidora);
		map.put("tipoRectificadora", tipoRectificadora);
		map.put("tipoBus", tipoBus);
		String Numero = (String) sqlMap.queryForObject("tieneRegistro", map);

		return Numero;
	}
*/

	private static void actualizarFactura(SqlMapClient sqlMap, int id_fatr,	String cups, String num_factura, String num_factura_rectificada) throws SQLException {
		HashMap map = new HashMap();
		map.put("id_fatr", id_fatr);
		map.put("cups", cups);
		map.put("num_factura", num_factura);
		map.put("num_factura_rectificada", num_factura_rectificada);
		try {
			sqlMap.update("actualizarFactura", map);
		} catch (Exception eop) {
			logger.error("ERROR: al actualizarFactura " + eop);
		}
	}

	private static void actualizarLecturasParaMedidas(SqlMapClient sqlMap, String IdInicio, String IdFin){
		try {
			// Actualizamos todas las lecturas
			// Todas las lecturas que vienen para la tarifa 3.1A y las 6.x son periodos
			// lecturavalida=0
			sqlMap.update("actualizarLecturas1", null);
		} catch (Exception eop) {
			logger.error("Error al actualizarLecturas1." + eop);
		}

		String LecturaValida = "1";		
		try {
			// lecturavalida=1
			HashMap map = new HashMap();
			map.put("LecturaValida", LecturaValida);
			sqlMap.update("actualizarLecturas2", map);
		} catch (Exception eop) {
			logger.error("ERROR: al actualizarLecturas2 " + eop);
		}
			
		try {
			List lstFacturas = (List) sqlMap.queryForList("recuperarFacturasLecturas", null);
			
			for (int i=0;i<lstFacturas.size();i++) {
				FacdisFacturaDTO factura = new FacdisFacturaDTO();
				factura = (FacdisFacturaDTO) lstFacturas.get(i);

				int IdFATR = factura.getId_fatr();
				
				HashMap map = new HashMap();
				map.put("IdFATR", IdFATR);
				
				try {
					int numDHs = Integer.parseInt((String) sqlMap.queryForObject("recuperarCount", map));

					if (numDHs>1) { //Tiene más de 1 discriminación horaria diferente,
						// Si numDHs = 3 - lecturavalida=0 si no lecturavalida=1
						sqlMap.update("actualizarLecturas3", map);

					} else {//Sólo tiene una DH.
						try {
							List <FacdisFacturaLecturaDTO> lstLecturas = (List) sqlMap.queryForList("recuperarLecturasUnaDiscriminacion", IdFATR);
							
							for (int k=0;k<lstLecturas.size();k++) {
								
								FacdisFacturaLecturaDTO lectura = new FacdisFacturaLecturaDTO();
								lectura = (FacdisFacturaLecturaDTO) lstLecturas.get(k);
								
								String id_magnitud_cne = lectura.getId_magnitud_cne();
								String id_periodo_cne = lectura.getId_periodo_cne();
								String consumocalc = String.valueOf(lectura.getConsumocalc());
								String fecha_ini = lectura.getFecha_ini();
								String fecha_fin = lectura.getFecha_fin();
								
								HashMap map2 = new HashMap();
								map2.put("IdFATR", IdFATR);
								map2.put("id_magnitud_cne", id_magnitud_cne);
								map2.put("id_periodo_cne", id_periodo_cne);
								map2.put("consumocalc", consumocalc);
								
								logger.debug(" -- IdFATR: " + IdFATR + " id_magnitud_cne: " + id_magnitud_cne + ", id_periodo_cne: " + id_periodo_cne + ", consumocalc: " + consumocalc);
								
								try {
									List lstPeriodos = (List) sqlMap.queryForList("recuperarPeriodos", map2);
									if (lstPeriodos.size() > 0) { 
										//Tiene registros asociados en periodos, con lo cual, no es una lectura válida.
										//Hay que actualizar el dato de la DH del Equipo, buscado en el IMU.
										LecturaValida = "0";
									}
								} catch (Exception eop) {
									logger.error("ERROR: al recuperarPeriodos " + eop);
								}

								map2.put("LecturaValida", LecturaValida);
								map2.put("fecha_ini", fecha_ini);
								map2.put("fecha_fin", fecha_fin);
								
								logger.debug(" -- LecturaValida: " + LecturaValida + ", fecha_ini: " + fecha_ini + ", fecha_fin: " + fecha_fin);
								
								try {
									sqlMap.update("actualizarLecturas4", map2);
								} catch (Exception eop) {
									logger.error("ERROR: al actualizarLecturas4 " + eop);
								}
							}
						} catch (Exception e) {
							logger.error("ERROR: al recuperarLecturasUnaDiscriminacion " + e.toString());
						}
					}
				} catch (Exception e) {
					logger.error("ERROR: al recuperarCount " + e.toString());
				}
			}
		} catch (Exception e) {
			logger.error("ERROR: al recuperarFacturasLecturas " + e.toString());
		}
	}
}