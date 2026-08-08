package modelo.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
	
	private static ConexionDB instance;
	private Connection conexion;
	private static final String URL = "jdbc:postgresql://localhost:5432/LibreriaDB";
	private static final String USER = "postgres";
	private static final String PASSWORD = "juan1234";

	
	private ConexionDB() throws SQLException {
		try {
		    Class.forName("org.postgresql.Driver"); 
		    this.conexion = DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (ClassNotFoundException e) {
		    throw new SQLException("Driver JDBC no encontrado", e);
		} catch (SQLException e) {
		    throw new SQLException("Error al conectar con la base de datos", e);
		}
    }
	
	public static ConexionDB getInstance() throws SQLException {
	    if (instance == null || instance.getConnection().isClosed()) {
	        synchronized (ConexionDB.class) {
	            if (instance == null || instance.getConnection().isClosed()) {
	                instance = new ConexionDB();
	            }
	        }
	    }
	    return instance;
	}
	
	public Connection getConnection() {
        return conexion;
    }
	
	public void closeConection() {
        try {
            if (conexion != null && !conexion.isClosed()) {
            	conexion.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
