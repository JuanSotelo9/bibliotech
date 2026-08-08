package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {
	
	private static final Properties propiedades = new Properties();
	
	static {
		try (InputStream in = AppConfig.class.getClassLoader().getResourceAsStream("application.properties")) {
			if (in == null) {
				throw new IllegalStateException("No se encontró el archivo application.properties");
			}
			propiedades.load(in);
		} catch (IOException e) {
			throw new IllegalStateException("Error al cargar application.properties", e);
		}
	}
	
	public static String get(String clave) {
		return propiedades.getProperty(clave);
	}
	
	public static String getDbUrl() {
		return get("db.url");
	}
	
	public static String getDbUser() {
		return get("db.user");
	}
	
	public static String getDbPassword() {
		return get("db.password");
	}
	
	public static String getJwtSecret() {
		return get("jwt.secret");
	}
	
	public static String getAllowedOrigins() {
		return get("cors.allowedOrigins");
	}
}
