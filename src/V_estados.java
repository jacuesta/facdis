import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.eon.fatr.componentes.ValidacionDAO;
import com.ibatis.sqlmap.client.SqlMapClient;

public class V_estados {

	private static Log logger = LogFactory.getFactory().getInstance( V_estados.class);
	
	public V_estados() {
	}
	
	public boolean execute( SqlMapClient sqlMap, String xIn) throws SQLException {
		
		boolean bCorrecto = true;
		
		ValidacionDAO val = new ValidacionDAO();
		
		// Actualizando {FRA_ML_ATR_VALIDACION} ---------------------------------------
		val.insertarValidacion_v_estados(sqlMap);
		
		// Actualizando {FACTURA_ML_ATR, validado} ---------------------------------------
		int i = val.updateValidado_v_estados(sqlMap);
		
		return bCorrecto;
	}
}
	
