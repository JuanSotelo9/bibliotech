package controlador;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;

import modelo.BusinessException;
import modelo.Reserva;
import modelo.otros.dto.ReservaDTO;
import modelo.persistencia.dao.ReservaDAO;

public class GestorReservas {
	
	private ReservaDAO reservaDAO;
	private ObjectMapper objectMapper;
	
	public GestorReservas() {
		this(new ReservaDAO());
	}

	GestorReservas(ReservaDAO reservaDAO) {
		this.reservaDAO = reservaDAO;
		this.objectMapper = new ObjectMapper();
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
	
	public int crearReserva(HttpServletRequest request, String usuario) throws IOException, SQLException {
		Reserva reserva = objectMapper.readValue(request.getReader(), Reserva.class);
		reserva.setEstado("Reservado");
		reserva.setUsuario(usuario);
		LocalDate fechaActual = LocalDate.now();
        String fechaFormateada = fechaActual.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        reserva.setFechareserva(fechaFormateada);
        if (reservaDAO.existeReservaActiva(reserva.getDocumento())) {
            throw new BusinessException(400, "DOCUMENTO_YA_RESERVADO", "El documento ya está reservado");
        }
		ReservaDTO reservaDTO = construirReserva(reserva);
		reservaDAO.crear(reservaDTO);
		return reserva.getDocumento();
	}
	
	public int entregarLibro(HttpServletRequest request) throws IOException, SQLException {
		Reserva reserva = objectMapper.readValue(request.getReader(), Reserva.class);
		reserva.setEstado("Entregado");
		LocalDate fechaActual = LocalDate.now();
        String fechaFormateada = fechaActual.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        reserva.setFechaentrega(fechaFormateada);
		ReservaDTO reservaDTO = construirReserva(reserva);
		return reservaDAO.actualizar(reservaDTO);
	}

	public String consutalReservas(String usuario) throws SQLException {
		return reservaDAO.consultarReservas(usuario);
	}

}
