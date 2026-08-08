package Servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import config.AppConfig;
import controlador.FachadaSistema;

@WebServlet({"/documento", "/documento/eventos","/documento/crear", "/documento/modificar", "/documento/reservar",
		"/documento/entregar", "/documento/eliminar", "/documento/habilitar", "/documento/titulo"})
public class ServletDocumentos extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private FachadaSistema gestor;
	
	public void init() throws ServletException {
        super.init();
        gestor = FachadaSistema.getInstancia();
        
    }
	
	 // Manejo de las solicitudes OPTIONS para permitir CORS
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setCORSHeaders(response);
        response.setStatus(HttpServletResponse.SC_OK);  // Responde con 200 OK
    }
    
    // Método para agregar los encabezados CORS
    private void setCORSHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", AppConfig.getAllowedOrigins());
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
    }
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	setCORSHeaders(response);
    	response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String urlPath = request.getRequestURI().substring(request.getContextPath().length());
        String jsonResponse;
        String usuario = (String) request.getAttribute("usuario");
        if ("/documento/crear".equals(urlPath)) {
            jsonResponse = gestor.crearDocumento(request, usuario);
        }
        else if("/documento/modificar".equals(urlPath)) {
        	jsonResponse = gestor.modificarDocumento(request, usuario);
        }
        else if("/documento/reservar".equals(urlPath)) {
        	jsonResponse = gestor.reservarDocumento(request, usuario);
        }
        else if("/documento/entregar".equals(urlPath)) {
        	jsonResponse = gestor.entregarDocumento(request, usuario);
        }
        else if("/documento/eliminar".equals(urlPath)) {
        	jsonResponse = gestor.eliminarDocumento(request, usuario);
        }
        else if("/documento/habilitar".equals(urlPath)) {
        	jsonResponse = gestor.habilitarDocumento(request, usuario);
        }
        else if("/documento".equals(urlPath)) {
        	jsonResponse = gestor.obtenerDocumento(request);
        } else if("/documento/eventos".equals(urlPath)) {
        	jsonResponse = gestor.buscarEventos(request);
        } else if("/documento/titulo".equals(urlPath)) {
        	jsonResponse = gestor.obtenerPorTitulo(request);
        } 
        else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            jsonResponse = "{\"mensaje\": \"URL no encontrada\"}";
        }

        response.getWriter().write(jsonResponse);
    }
}
