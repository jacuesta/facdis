package com.eon.fatr.componentes;

import java.sql.SQLException;

import com.ibatis.sqlmap.client.SqlMapClient;

public class FacdisFacturaDAO {
	
	public void insertarFacturaFacdisDAO(SqlMapClient map, FacdisFacturaDTO factura) throws SQLException{
		map.insert("insertarFacturaFacdis", factura);
	}
}
