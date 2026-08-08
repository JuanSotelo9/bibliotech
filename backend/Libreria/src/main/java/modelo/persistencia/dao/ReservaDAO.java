package modelo.persistencia.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import modelo.otros.dto.ReservaDTO;
import modelo.persistencia.ConexionDB;

public class ReservaDAO{

	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	public void crear(ReservaDTO reserva)throws SQLException{
		String sql = "INSERT INTO reserva (fechareserva, estado, documento, usuario) VALUES (?, ?, ?, ?)";

	    try (Connection conexion = ConexionDB.getInstance().getConnection();
	         PreparedStatement pstmt = conexion.prepareStatement(sql)) {
	    	
	    	pstmt.setDate(1, java.sql.Date.valueOf(reserva.getFechaReserva()));
	        pstmt.setString(2, reserva.getEstado());
	        pstmt.setInt(3, reserva.getDocumento());
	        pstmt.setString(4, reserva.getUsuario());
	     
	        pstmt.executeUpdate();
	    }
		
	}
	
	public int actualizar(ReservaDTO reserva)throws SQLException{
		String sql = "UPDATE reserva SET estado = ?, fechaentrega = ? WHERE idreserva = ? RETURNING documento";

	    try (Connection conexion = ConexionDB.getInstance().getConnection();
	         PreparedStatement pstmt = conexion.prepareStatement(sql)) {
	    	
	    	pstmt.setString(1, reserva.getEstado());
	    	pstmt.setDate(2, java.sql.Date.valueOf(reserva.getFechaEntrega()));
	        pstmt.setInt(3, reserva.getIdReserva());
	     
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                return rs.getInt(1); 
	            } else {
	                throw new SQLException("No se pudo obtener el ID generado.");
	            }
	        }
	    }
	}
	
	public String consultarReservas(String usuario) throws SQLException {
	    String sql = "SELECT r.idreserva, d.titulo, d.tipo, r.fechareserva, r.fechaentrega, r.estado " +
	                 "FROM documento d JOIN reserva r ON d.iddocumento = r.documento WHERE usuario = ?";

	    List<Map<String, Object>> reservas = new ArrayList<>();

	    try (Connection conexion = ConexionDB.getInstance().getConnection();
	         PreparedStatement pstmt = conexion.prepareStatement(sql)) {

	        pstmt.setString(1, usuario);

	        try (ResultSet rs = pstmt.executeQuery()) {
	            while (rs.next()) {
	                Map<String, Object> reserva = new LinkedHashMap<>();
	                reserva.put("idreserva", rs.getInt("idreserva"));
	                reserva.put("titulo", rs.getString("titulo"));
	                reserva.put("tipo", rs.getString("tipo"));
	                reserva.put("fechareserva", String.valueOf(rs.getDate("fechareserva")));
	                reserva.put("fechaentrega", String.valueOf(rs.getDate("fechaentrega")));
	                reserva.put("estado", rs.getString("estado"));
	                reservas.add(reserva);
	            }
	        }
	    }

	    try {
	        return objectMapper.writeValueAsString(reservas);
	    } catch (IOException e) {
	        throw new SQLException("Error al serializar las reservas", e);
	    }
	}

}
