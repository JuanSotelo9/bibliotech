package controlador;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import modelo.Reserva;
import modelo.OtrosDTO.ReservaDTO;
import modelo.persistenciaDAO.ReservaDAO;

public class GestorReservas {
	
	private static final Logger logger = LoggerFactory.getLogger(GestorReservas.class);
	
	private ReservaDAO reservaDAO;
	private ObjectMapper objectMapper;
	
	public GestorReservas() {
		reservaDAO = new ReservaDAO();
		objectMapper = new ObjectMapper();
	}
	
	public ReservaDTO construirReserva(Reserva reserva) {
		return new ReservaDTO.BuilderReserva()
				.setDocumento(reserva.getDocumento())
				.setEstado(reserva.getEstado())
				.setFechaEntrega(reserva.getFechaentrega())
				.setFechaReserva(reserva.getFechareserva())
				.setIdReserva(reserva.getIdreserva())
				.setUsuario(reserva.getUsuario())
				.build();
	}
	
	public int crearReserva(HttpServletRequest request, String usuario) throws JsonProcessingException {
		
		try {
			Reserva reserva = objectMapper.readValue(request.getReader(), Reserva.class);
			reserva.setEstado("Reservado");
			reserva.setUsuario(usuario);
			LocalDate fechaActual = LocalDate.now();
	        String fechaFormateada = fechaActual.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	        reserva.setFechareserva(fechaFormateada);
			ReservaDTO reservaDTO = construirReserva(reserva);
			reservaDAO.crear(reservaDTO);
			return reserva.getDocumento();
		}catch (IOException e) {
        	Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("mensaje", "Error al leer JSON: " + e.getMessage());
            logger.error("Error al leer JSON: {}", e.getMessage());
            return -1;
        } catch (SQLException e) {
        	Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("mensaje", "Error en la base de datos: " + e.getMessage());
            logger.error("Error en la base de datos: {}", e.getMessage());
            return -1;
        }
	}
	
	public int entregarLibro(HttpServletRequest request) throws JsonProcessingException {
		try {
			Reserva reserva = objectMapper.readValue(request.getReader(), Reserva.class);
			reserva.setEstado("Entregado");
			LocalDate fechaActual = LocalDate.now();
	        String fechaFormateada = fechaActual.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	        reserva.setFechaentrega(fechaFormateada);
			ReservaDTO reservaDTO = construirReserva(reserva);
			return reservaDAO.actualizar(reservaDTO);
		}catch (IOException e) {
        	Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("mensaje", "Error al leer JSON: " + e.getMessage());
            logger.error("Error al leer JSON: {}", e.getMessage());
            return -1;
        } catch (SQLException e) {
        	Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("mensaje", "Error en la base de datos: " + e.getMessage());
            logger.error("Error en la base de datos: {}", e.getMessage());
            return -1;
        }
	}

	public String consutalReservas(String usuario) throws JsonProcessingException {
		try {
			return reservaDAO.consultarReservas(usuario);
		}catch (SQLException e) {
        	Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("mensaje", "Error en la base de datos: " + e.getMessage());
            return objectMapper.writeValueAsString(errorResponse);
        }
	}

}
