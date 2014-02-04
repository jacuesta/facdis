package com.eon.fatr.componentes;

import java.sql.SQLException;

import com.ibatis.sqlmap.client.SqlMapClient;

public class FacdisFacturaRefacturacionDAO {

	public void insertarRefacturacionFacdisDAO(SqlMapClient map, FacdisFacturaRefacturacionDTO refacturacion) throws SQLException{
		map.insert("insertarRefacturacionFacDis", refacturacion);
	}
}