package com.eon.fatr.componentes;

import java.sql.SQLException;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibatis.sqlmap.client.SqlMapClient;

public class ValidacionDAO {
	private static Log logger = LogFactory.getFactory().getInstance(ValidacionDAO.class);
	private static SqlMapClient sqlMap;

	/**
	 * 
	 * @return  sqlMap
	 */
	public static SqlMapClient getSqlMap() {
		return sqlMap;
	}
	
	/**
	 * 
	 * @param sqlMap  
	 */
	public void setSqlMap(SqlMapClient sqlMap) {
		ValidacionDAO.sqlMap = sqlMap;
	}	

	public Map<String,String> selectValidaciones () throws Exception{
		if(logger.isDebugEnabled())logger.debug(" -- IN selectValidaciones -- ");
		
		try {
			Map<String,String> validaciones = sqlMap.queryForMap("selectValidaciones", null,"DESCRIPCION", "TOTAL");
			
			if(logger.isDebugEnabled())logger.debug(" -- OUT selectValidaciones -- ");
			
			return validaciones;
			
		} catch (Exception e) {
			logger.error("Error al buscar validaciones: " + e.getMessage());
			throw e;
		}
	}
	
	public int borrarValidacionesKO(SqlMapClient sqlMap) throws SQLException {
		int b = 0;
		b = sqlMap.update("borrarValidacionesKO", null);
		return b;
	}

	public int borrarValidacionesActivas(SqlMapClient sqlMap) throws SQLException {
		int b = 0;
		b = sqlMap.delete("borrarValidacionesActivas", null);
		return b;
	}

	public void insertarValidacion_v_duplicados(SqlMapClient sqlMap) throws SQLException {
		sqlMap.insert("insertarValidacion_v_duplicados", null);
	}

	public int updateValidado_v_duplicados(SqlMapClient sqlMap) throws SQLException {
		int b = 0;
		b = sqlMap.update("updateValidado_v_duplicados", null);
		return b;
	}

	public void insertarValidacion_v_interseccion(SqlMapClient sqlMap) throws SQLException {
		sqlMap.insert("insertarValidacion_v_interseccion", null);
	}

	public int updateValidado_v_interseccion(SqlMapClient sqlMap) throws SQLException {
		int b = 0;
		b = sqlMap.update("updateValidado_v_interseccion", null);
		return b;
	}

	public void insertarValidacion_v_dobleref(SqlMapClient sqlMap) throws SQLException {
		sqlMap.insert("insertarValidacion_v_dobleref", null);
	}

	public int updateValidado_v_dobleref(SqlMapClient sqlMap) throws SQLException {
		int b = 0;
		b = sqlMap.update("updateValidado_v_dobleref", null);
		return b;
	}

	public void insertarValidacion_v_estados(SqlMapClient sqlMap) throws SQLException {
		sqlMap.insert("insertarValidacion_v_estados", null);
	}

	public int updateValidado_v_estados(SqlMapClient sqlMap) throws SQLException {
		int b = 0;
		b = sqlMap.update("updateValidado_v_estados", null);
		return b;
	}

	public int marcarValidacionesRealizadas(SqlMapClient sqlMap) throws SQLException {
		int b = 0;
		b = sqlMap.update("marcarValidacionesRealizadas", null);
		return b;
	}
}