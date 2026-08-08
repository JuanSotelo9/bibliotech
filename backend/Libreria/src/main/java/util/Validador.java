package util;

import modelo.BusinessException;

public class Validador {
	
	private Validador() {
	}
	
	public static void validarNoVacio(String campo, String codigo, String mensaje) {
		if (campo == null || campo.trim().isEmpty()) {
			throw new BusinessException(400, codigo, mensaje);
		}
	}
	
	public static void validarEmail(String email) {
		if (email == null || !email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
			throw new BusinessException(400, "EMAIL_INVALIDO", "El correo electrónico no tiene un formato válido");
		}
	}
	
	public static void validarTelefono(String telefono) {
		if (telefono == null || !telefono.matches("^\\d{10}$")) {
			throw new BusinessException(400, "TELEFONO_INVALIDO", "El teléfono debe tener 10 dígitos");
		}
	}
	
	public static void validarLongitudMinima(String campo, int longitudMinima, String codigo, String mensaje) {
		if (campo == null || campo.length() < longitudMinima) {
			throw new BusinessException(400, codigo, mensaje);
		}
	}
}
