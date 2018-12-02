package persistencia;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ControllerBD {

	private final DBBroker con;

	public ControllerBD() {

		this.con = new DBBroker();

	}

	public void CrearConexion() throws SQLException {

		this.con.EstablecerConexion();
	}

	public ResultSet mandarSql(String sql) throws SQLException {
		ResultSet aux_result = this.con.EjecutarSentencia(sql);
		return aux_result;

	}
	public Boolean mandarSqlinsert(String sql) throws SQLException {
		Boolean aux_result = this.con.Ejecutar(sql);
		return aux_result;

	}
	
}
