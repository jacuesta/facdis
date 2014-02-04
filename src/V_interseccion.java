import java.sql.SQLException;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.eon.fatr.componentes.ValidacionDAO;
import com.ibatis.sqlmap.client.SqlMapClient;

public class V_interseccion {

	private static Log logger = LogFactory.getFactory().getInstance( V_interseccion.class);
	
	public V_interseccion() {
	}
	
	public boolean execute( SqlMapClient sqlMap, String xIn) throws SQLException {
		
		boolean bCorrecto = true;
		
		ValidacionDAO val = new ValidacionDAO();
		
		// Actualizando {FRA_ML_ATR_VALIDACION} ---------------------------------------
		val.insertarValidacion_v_interseccion(sqlMap);
		
		// Actualizando {FACTURA_ML_ATR, validado} ---------------------------------------
		int i = val.updateValidado_v_interseccion(sqlMap);
		
		return bCorrecto;
	}
}
	
