package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppConfig {

	private static final Properties propiedades = new Properties();
	private static final Pattern ENV_PATTERN = Pattern.compile("\\$\\{([^:}]+):([^}]*)\\}");

	static {
		try (InputStream in = AppConfig.class.getClassLoader().getResourceAsStream("application.properties")) {
			if (in == null) {
				throw new IllegalStateException("No se encontro el archivo application.properties");
			}
			Properties raw = new Properties();
			raw.load(in);
			for (String key : raw.stringPropertyNames()) {
				propiedades.setProperty(key, resolver(raw.getProperty(key)));
			}
		} catch (IOException e) {
			throw new IllegalStateException("Error al cargar application.properties", e);
		}
	}

	private static String resolver(String valor) {
		if (valor == null) return null;
		Matcher m = ENV_PATTERN.matcher(valor);
		if (!m.matches()) return valor;
		String envVar = m.group(1);
		String envValue = System.getenv(envVar);
		return (envValue != null && !envValue.isEmpty()) ? envValue : m.group(2);
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
		String pwd = get("db.password");
		if ("changeme".equals(pwd)) {
			throw new IllegalStateException("DB_PASSWORD no configurada. Crea un archivo .env o define la variable de entorno.");
		}
		return pwd;
	}
	
	public static int getDbMaxPoolSize() {
		return Integer.parseInt(get("db.maxPoolSize"));
	}
	
	public static int getDbMinIdle() {
		return Integer.parseInt(get("db.minIdle"));
	}
	
	public static long getDbConnectionTimeout() {
		return Long.parseLong(get("db.connectionTimeout"));
	}
	
	public static String getJwtSecret() {
		String secret = get("jwt.secret");
		if ("changeme".equals(secret)) {
			throw new IllegalStateException("JWT_SECRET no configurada. Crea un archivo .env o define la variable de entorno.");
		}
		return secret;
	}
	
	public static String getAllowedOrigins() {
		return get("cors.allowedOrigins");
	}
}
