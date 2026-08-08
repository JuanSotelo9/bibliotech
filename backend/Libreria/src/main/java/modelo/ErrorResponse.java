package modelo;

import java.io.IOException;
import java.io.Serializable;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class ErrorResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	private final int codigo;
	private final String error;
	private final String detalle;
	
	public ErrorResponse(int codigo, String error, String detalle) {
		this.codigo = codigo;
		this.error = error;
		this.detalle = detalle;
	}
	
	public int getCodigo() {
		return codigo;
	}
	
	public String getError() {
		return error;
	}
	
	public String getDetalle() {
		return detalle;
	}
	
	public String toJson() {
		try {
			return objectMapper.writeValueAsString(this);
		} catch (IOException e) {
			return "{\"codigo\":" + codigo + ",\"error\":\"" + error + "\",\"detalle\":\"" + detalle + "\"}";
		}
	}
}
