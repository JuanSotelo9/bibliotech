package modelo;

public class BusinessException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private final int status;
	private final String codigo;
	private final String mensaje;
	
	public BusinessException(int status, String codigo, String mensaje) {
		super(mensaje);
		this.status = status;
		this.codigo = codigo;
		this.mensaje = mensaje;
	}
	
	public int getStatus() {
		return status;
	}
	
	public String getCodigo() {
		return codigo;
	}
	
	public String getMensaje() {
		return mensaje;
	}
}
