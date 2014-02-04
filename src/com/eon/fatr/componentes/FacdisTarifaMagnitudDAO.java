package com.eon.fatr.componentes;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibatis.sqlmap.client.SqlMapClient;

public class FacdisTarifaMagnitudDAO {
	
	private static Log logger = LogFactory.getFactory().getInstance(FacdisTarifaMagnitudDAO.class);
	
	public static List recuperarTarifaMagnitud(SqlMapClient sqlMap, String tarifa, String magnitud, int periodos) throws Exception{
		logger.debug(" -- IN recuperarTarifaMagnitud -- ");
		HashMap map = new HashMap();
		map.put("tarifa",tarifa);
		map.put("magnitud",magnitud);
		map.put("periodos", periodos);
		
		List lista = sqlMap.queryForList("recuperaDatosTarifaMagnitud", map);
		
		logger.debug(" -- OUT recuperarTarifaMagnitud -- ");
		
		return lista;
	}

	public static List recuperarTodosTarifaMagnitud(SqlMapClient sqlMap) throws Exception{
		logger.debug(" -- IN recuperarTodosTarifaMagnitud -- ");
		
		List lista = sqlMap.queryForList("recuperaDatosTodosTarifaMagnitud");
		
		logger.debug(" -- OUT recuperarTodosTarifaMagnitud -- ");
		
		return lista;
	}
}
