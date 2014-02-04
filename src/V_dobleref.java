import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibatis.sqlmap.client.SqlMapClient;

import com.eon.fatr.componentes.ValidacionDAO;

public class V_dobleref {

	private static Log logger = LogFactory.getFactory().getInstance( V_dobleref.class);
	
	public V_dobleref() {
	}
	
	public boolean execute( SqlMapClient sqlMap, String xIn) throws SQLException {
		
		boolean bCorrecto = true;
		
		ValidacionDAO val = new ValidacionDAO();
		
		// Actualizando {FRA_ML_ATR_VALIDACION} ---------------------------------------
		val.insertarValidacion_v_dobleref(sqlMap);
		
		// Actualizando {FACTURA_ML_ATR, validado} ---------------------------------------
		int i = val.updateValidado_v_dobleref(sqlMap);
		
		return bCorrecto;
	}
}
	
