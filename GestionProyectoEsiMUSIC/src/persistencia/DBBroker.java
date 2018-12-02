package persistencia;



import java.sql.DriverManager;
import java.sql.ResultSet;

import java.sql.SQLException;

import java.sql.Statement;

import javax.swing.JOptionPane;

import com.mysql.jdbc.Connection;

/**
 * 
 * 
 * 
 * @author 
 * 
 */

public class DBBroker {

	private Connection conexion;

	private Statement sentencia;

	private final String controlador;

	private final String nombre_bd;

	private final String usuarioBD;

	private final String passwordBD;

	public DBBroker() {

		this.controlador = "com.mysql.jdbc.Driver";

		this.nombre_bd = "127.0.0.1:3306/mydb";

		this.usuarioBD = "root";

		this.passwordBD = "gestionesi";

	}

	public boolean EstablecerConexion() throws SQLException {

		try {

			conexion = (Connection) DriverManager.getConnection("jdbc:mysql://"
					+ this.nombre_bd, this.usuarioBD, this.passwordBD);

		} catch (SQLException e) {

			JOptionPane.showMessageDialog(null,
					"Error al realizar la conexion " + e);

			return false;

		}

		try {

			this.sentencia = this.conexion.createStatement(

			ResultSet.TYPE_SCROLL_INSENSITIVE,

			ResultSet.CONCUR_READ_ONLY);

		}

		catch (SQLException e) {

			JOptionPane.showMessageDialog(null,
					"Error al crear el objeto sentencia " + e);

			return false;

		}

		return true;

	}

	public ResultSet EjecutarSentencia(String sql) throws SQLException {

		ResultSet rs;

		rs = this.sentencia.executeQuery(sql);

		return rs;

	}
	
	public boolean Ejecutar(String sql) throws SQLException {
		Boolean result;
		result=this.sentencia.execute(sql);
		
		return result;
	}

}