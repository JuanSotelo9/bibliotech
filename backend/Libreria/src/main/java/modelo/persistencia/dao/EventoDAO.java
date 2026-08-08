package modelo.persistencia.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import modelo.otros.dto.EventoDTO;
import modelo.persistencia.ConexionDB;

public class EventoDAO{

	public void crear(EventoDTO evento) throws SQLException {
		String sql = "INSERT INTO evento (tipoevento, usuario, documento, fechaevento) VALUES (?, ?, ?, ?)";

	    try (Connection conexion = ConexionDB.getInstance().getConnection();
	         PreparedStatement pstmt = conexion.prepareStatement(sql)) {

	        pstmt.setString(1, evento.getTipoEvento());
	        pstmt.setString(2, evento.getUsuario());
	        pstmt.setInt(3, evento.getDocumento());
	        if (!evento.getFecha().isEmpty()) {
	            pstmt.setDate(4, java.sql.Date.valueOf(evento.getFecha()));
	        } else {
	            pstmt.setNull(4, java.sql.Types.DATE);
	        }
	        pstmt.executeUpdate();
	    }
	}
	
	//Metodo para retornar eventos de un documento
		public List<String> buscarPorDocumento(int id) throws SQLException {
		    String sql = "SELECT e.tipoevento, d.titulo, d.tipo, e.usuario, e.fechaevento " +
		                 "FROM evento e " +
		                 "JOIN documento d ON e.documento = d.iddocumento " +
		                 "WHERE d.iddocumento = ?";

		    List<String> eventos = new ArrayList<>();

		    try (Connection conn = ConexionDB.getInstance().getConnection();
		         PreparedStatement pstmt = conn.prepareStatement(sql)) {
		        
		        pstmt.setInt(1, id);

		        try (ResultSet rs = pstmt.executeQuery()) {
		            while (rs.next()) {
		                String eventoStr = rs.getString("tipoevento") + " - " +
		                                   rs.getString("titulo") + "|" +
		                                   rs.getString("tipo") + ", " +
		                                   rs.getString("usuario") + ", " +
		                                   rs.getString("fechaevento");
		                
		                eventos.add(eventoStr);
		            }
		        }
		    }
		    return eventos;
	}


}
