package com.eon.fatr.componentes;


import java.sql.SQLException;

import com.ibatis.sqlmap.client.SqlMapClient;

public class FacdisFacturaPeriodosDAO {
	
	public void insertarPeriodosFacdisDAO(SqlMapClient map, FacdisFacturaPeriodosDTO periodo) throws SQLException{
		map.insert("insertarPeriodosFacdis", periodo);
	}
}

