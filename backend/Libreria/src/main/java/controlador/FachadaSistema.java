package controlador;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

public class FachadaSistema {
	
	private static FachadaSistema instancia;
	private GestorDocumentos gestorDocumentos;
	private GestorUsuarios gestorUsuarios;
	private GestorReservas gestorReservas;
	
	private FachadaSistema() {
        this.gestorUsuarios = new GestorUsuarios();
        this.gestorDocumentos = new GestorDocumentos();
        this.gestorReservas = new GestorReservas();
    }
	
	public static synchronized FachadaSistema getInstancia() {
        if (instancia == null) {
            instancia = new FachadaSistema();
        }
        return instancia;
    }
	
	public String registrarUsuario(HttpServletRequest request) throws IOException, SQLException {
        return gestorUsuarios.registrarUsuario(request);
    }

    public String loginUsuario(HttpServletRequest request) throws IOException, SQLException {
        return gestorUsuarios.loginUsuario(request);
    }

    public String obtenerUsuario(String usuario) throws IOException, SQLException {
        return gestorUsuarios.obtenerUsuario(usuario);
    }

    public String crearDocumento(HttpServletRequest request, String usuario) throws IOException, SQLException {
        return gestorDocumentos.crearDocumento(request, usuario);
    }

    public String modificarDocumento(HttpServletRequest request, String usuario) throws IOException, SQLException {
        return gestorDocumentos.modificarDocumento(request, usuario);
    }
    
    public String reservarDocumento(HttpServletRequest request, String usuario) throws IOException, SQLException{
    	int iddocumento = gestorReservas.crearReserva(request, usuario);
    	return gestorDocumentos.modificarEstado(iddocumento, "Reservado", usuario);
    }
    
    public String entregarDocumento(HttpServletRequest request, String usuario) throws IOException, SQLException{
    	int iddocumento = gestorReservas.entregarLibro(request);
    	return gestorDocumentos.modificarEstado(iddocumento, "Entregado", usuario);
    }
    
    public String eliminarDocumento(HttpServletRequest request, String usuario) throws IOException, SQLException{
    	return gestorDocumentos.eliminarDocumento(request, usuario);
    }
    
    public String habilitarDocumento(HttpServletRequest request, String usuario) throws IOException, SQLException{
    	return gestorDocumentos.habilitarDocumento(request, usuario);
    }

    public String buscarEventos(HttpServletRequest request) throws IOException, SQLException{
    	return gestorDocumentos.buscarEventos(request);
    }

    public String obtenerDocumentos(String usuario) throws IOException, SQLException{
    	return gestorDocumentos.obtenerDocumentos(usuario);
    }
    
    public String obtenerDocumento(HttpServletRequest request) throws IOException, SQLException{
    	return gestorDocumentos.obtenerDocumento(request);
    }
    
    public String obtenerPorTitulo(HttpServletRequest request) throws IOException, SQLException{
    	return gestorDocumentos.obtenerPorTitulo(request);
    }

    public String obtenerReservas(String usuario) throws IOException, SQLException{
    	return gestorReservas.consutalReservas(usuario);
    }
    
    public String consultarUsuario(HttpServletRequest request) throws IOException, SQLException{
    	return gestorUsuarios.consultarUsuario(request);
    }
}
