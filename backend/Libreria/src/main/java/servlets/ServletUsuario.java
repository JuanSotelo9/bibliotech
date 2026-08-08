package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import config.AppConfig;
import controlador.FachadaSistema;

@WebServlet({"/usuario/datos", "/usuario/registrar", "/usuario/login", "/usuario/documentos", "/usuario/reservas", "/usuario/consultar"})
public class ServletUsuario extends BaseServlet {
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setCORSHeaders(response);
        String urlPath = request.getRequestURI().substring(request.getContextPath().length());

        try {
            String jsonResponse;

            if ("/usuario/registrar".equals(urlPath)) {
                jsonResponse = gestor.registrarUsuario(request);
            } else if ("/usuario/login".equals(urlPath)) {
                jsonResponse = gestor.loginUsuario(request);
            } else if ("/usuario/consultar".equals(urlPath)) {
                jsonResponse = gestor.consultarUsuario(request);
            } else {
                sendError(response, 404, "URL_NO_ENCONTRADA", "URL no encontrada");
                return;
            }

            sendJson(response, jsonResponse);
        } catch (Exception e) {
            handleError(response, e);
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setCORSHeaders(response);
        String usuario = (String) request.getAttribute("usuario");
        String urlPath = request.getRequestURI().substring(request.getContextPath().length());

        try {
            String jsonResponse;

            if ("/usuario/datos".equals(urlPath)) {
                jsonResponse = gestor.obtenerUsuario(usuario);
            } else if ("/usuario/documentos".equals(urlPath)) {
                jsonResponse = gestor.obtenerDocumentos(usuario);
            } else if ("/usuario/reservas".equals(urlPath)) {
                jsonResponse = gestor.obtenerReservas(usuario);
            } else {
                sendError(response, 404, "URL_NO_ENCONTRADA", "URL no encontrada");
                return;
            }

            sendJson(response, jsonResponse);
        } catch (Exception e) {
            handleError(response, e);
        }
    }

    // Método para agregar los encabezados CORS
    private void setCORSHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", AppConfig.getAllowedOrigins());
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
    }
}
