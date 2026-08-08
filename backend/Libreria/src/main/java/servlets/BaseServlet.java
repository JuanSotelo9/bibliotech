package servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import modelo.BusinessException;
import modelo.ErrorResponse;

public abstract class BaseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected static final ObjectMapper objectMapper = new ObjectMapper();
	protected static final Logger logger = LoggerFactory.getLogger(BaseServlet.class);
	
	protected void sendJson(HttpServletResponse resp, Object obj) throws IOException {
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		if (obj instanceof String) {
			resp.getWriter().write((String) obj);
		} else {
			resp.getWriter().write(objectMapper.writeValueAsString(obj));
		}
	}
	
	protected static void sendError(HttpServletResponse resp, int status, String codigo, String mensaje) throws IOException {
		resp.setStatus(status);
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		resp.getWriter().write(new ErrorResponse(status, codigo, mensaje).toJson());
	}
	
	protected void handleError(HttpServletResponse resp, Exception e) throws IOException {
		if (e instanceof BusinessException) {
			BusinessException be = (BusinessException) e;
			logger.warn("Error de negocio [{}]: {}", be.getCodigo(), be.getMensaje());
			sendError(resp, be.getStatus(), be.getCodigo(), be.getMensaje());
		} else if (e instanceof IOException) {
			logger.error("Error al leer el cuerpo de la solicitud", e);
			sendError(resp, 400, "ERROR_JSON", "Error al leer el cuerpo de la solicitud");
		} else if (e instanceof SQLException) {
			logger.error("Error en la base de datos", e);
			sendError(resp, 500, "ERROR_BD", "Error en la base de datos");
		} else {
			logger.error("Error interno del servidor", e);
			sendError(resp, 500, "ERROR_INTERNO", "Error interno del servidor");
		}
	}
}
