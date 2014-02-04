package com.eon.fatr.componentes;


import java.sql.SQLException;

import com.ibatis.sqlmap.client.SqlMapClient;

public class FacdisFacturaConceptoDAO {
	
	public void insertarFacdisConceptosDAO(SqlMapClient map, FacdisFacturaConceptoDTO conceptos) throws SQLException{
		map.insert("insertarConceptosFacdis", conceptos);
	}
	
//	public List recuperaConceptos(SqlMapClient sqlMap, String idfatr) throws SQLException{
//		List concepto=null;
//		concepto = (List) sqlMap.queryForList("recuperaConceptos", idfatr);
//		return concepto;
//	}
}

