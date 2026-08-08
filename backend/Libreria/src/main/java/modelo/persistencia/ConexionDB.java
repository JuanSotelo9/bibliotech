package modelo.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import config.AppConfig;

public class ConexionDB {
	
	private static ConexionDB instance;
	private Connection conexion;
	private static final String URL = AppConfig.getDbUrl();
	private static final String USER = AppConfig.getDbUser();
	private static final String PASSWORD = AppConfig.getDbPassword();

	
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
