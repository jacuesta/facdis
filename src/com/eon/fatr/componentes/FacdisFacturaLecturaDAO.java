package com.eon.fatr.componentes;

import java.sql.SQLException;

import com.ibatis.sqlmap.client.SqlMapClient;

public class FacdisFacturaLecturaDAO {
	public void insertarLecturasDAO(SqlMapClient map, FacdisFacturaLecturaDTO lecturas) throws SQLException{
		map.insert("insertarLecturasFacdis", lecturas);
	}
	
//	public List recuperaLecturas(SqlMapClient sqlMap, String idfatr) throws SQLException{
//		List lecturas=null;
//		lecturas = (List) sqlMap.queryForList("recuperaLecturasFacdis", idfatr);
//		return lecturas;
//	}
}
