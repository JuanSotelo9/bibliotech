package modelo.persistencia;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import config.AppConfig;

public class ConexionDB {
	
	private static ConexionDB instance;
	private static HikariDataSource dataSource;
	
	private ConexionDB() {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl(AppConfig.getDbUrl());
		config.setUsername(AppConfig.getDbUser());
		config.setPassword(AppConfig.getDbPassword());
		config.setMaximumPoolSize(AppConfig.getDbMaxPoolSize());
		config.setMinimumIdle(AppConfig.getDbMinIdle());
		config.setConnectionTimeout(AppConfig.getDbConnectionTimeout());
		config.setDriverClassName("org.postgresql.Driver");
		dataSource = new HikariDataSource(config);
	}
	
	public static ConexionDB getInstance() {
	    if (instance == null) {
	        synchronized (ConexionDB.class) {
	            if (instance == null) {
	                instance = new ConexionDB();
	            }
	        }
	    }
	    return instance;
	}
	
	public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
	
	public void closeConection() {
        if (dataSource != null && !dataSource.isClosed()) {
        	dataSource.close();
        }
    }

}
