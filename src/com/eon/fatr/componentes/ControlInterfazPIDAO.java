package com.eon.fatr.componentes;

import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibatis.sqlmap.client.SqlMapClient;


public class ControlInterfazPIDAO {
	
	private static Log logger = LogFactory.getFactory().getInstance(ControlInterfazPIDAO.class);
	
	public boolean insertFicherosTablaControlPI(SqlMapClient sqlMap, ControlInterfazDTO controlInterfazDTO) throws SQLException{
		
		logger.debug(" -- IN insertFicherosTablaControl -- ");
		
		boolean bResultado = false;
		
		sqlMap.insert("InsertarTablaControlPI", controlInterfazDTO);
		bResultado = true;
		
		logger.debug(" -- OUT insertFicherosTablaControl -- ");
		
		return bResultado;
		
	}
}

