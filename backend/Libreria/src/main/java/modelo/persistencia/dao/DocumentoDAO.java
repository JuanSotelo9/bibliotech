package modelo.persistencia.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import modelo.documento.dto.DocumentoDTO;
import modelo.persistencia.ConexionDB;

public class DocumentoDAO{

	public int crear(DocumentoDTO documento) throws SQLException {
	    String sql = "INSERT INTO documento (titulo, fechapublicacion, autores, diapublicacion, mespublicacion, editorial, estado, propietario, tipo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING iddocumento";

	    try (Connection conexion = ConexionDB.getInstance().getConnection();
	         PreparedStatement pstmt = conexion.prepareStatement(sql)) {

	        pstmt.setString(1, documento.getTitulo());
	        if (!documento.getFechaPublicacion().isEmpty()) {
	            pstmt.setDate(2, java.sql.Date.valueOf(documento.getFechaPublicacion()));
	        } else {
	            pstmt.setNull(2, java.sql.Types.DATE);
	        }
	        pstmt.setString(3, documento.getAutores());
	        pstmt.setString(4, documento.getDiaPublicacion());
	        pstmt.setString(5, documento.getMesPublicacion());
	        pstmt.setString(6, documento.getEditorial());
	        pstmt.setString(7, documento.getEstado());
	        pstmt.setString(8, documento.getPropietario());
	        pstmt.setString(9, documento.getTipo());

	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                return rs.getInt(1); 
	            } else {
	                throw new SQLException("No se pudo obtener el ID generado.");
	            }
	        }
	    }
	}
	   

	public void actualizar(DocumentoDTO documento) throws SQLException {
	    String sql = "UPDATE documento SET titulo = ?, fechapublicacion = ?, autores = ?, diapublicacion = ?, " +
	                 "mespublicacion = ?, editorial = ?, estado = ? WHERE iddocumento = ?";
	    
	    try (Connection conn = ConexionDB.getInstance().getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {

	        pstmt.setString(1, documento.getTitulo());
	        if (!documento.getFechaPublicacion().isEmpty()) {
	            pstmt.setDate(2, java.sql.Date.valueOf(documento.getFechaPublicacion()));
	        } else {
	            pstmt.setNull(2, java.sql.Types.DATE);
	        }
	        pstmt.setString(3, documento.getAutores());
	        pstmt.setString(4, documento.getDiaPublicacion());
	        pstmt.setString(5, documento.getMesPublicacion());
	        pstmt.setString(6, documento.getEditorial());
	        pstmt.setString(7, documento.getEstado());
	        pstmt.setInt(8, documento.getIdDocumento());
			pstmt.executeUpdate();
	    }
	}

    public DocumentoDTO buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM documento WHERE iddocumento = ?";
        try (Connection conn = ConexionDB.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new DocumentoDTO.BuilderDoc()
                            .setIdDocumento(rs.getInt("iddocumento"))
                            .setTitulo(rs.getString("titulo"))
                            .setFechaPublicacion(rs.getString("fechapublicacion"))
                            .setAutores(rs.getString("autores"))
                            .setDiaPublicacion(rs.getString("diapublicacion"))
                            .setMesPublicacion(rs.getString("mespublicacion"))
                            .setEditorial(rs.getString("editorial"))
                            .setEstado(rs.getString("estado"))
                            .setPropietario(rs.getString("propietario"))
                            .setTipo(rs.getString("tipo"))
                            .build();
                }
            }
        }
        return null;
    }

    public void actualizarEstado(DocumentoDTO documento) throws SQLException {
	    String sql = "UPDATE documento SET estado = ? WHERE iddocumento = ?";
	    
	    try (Connection conn = ConexionDB.getInstance().getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {

	        pstmt.setString(1, documento.getEstado());
	        pstmt.setInt(2, documento.getIdDocumento());
			pstmt.executeUpdate();
	    }
	}
    
  	public List<Map<String, Object>> buscarPorNombre(String nombre) throws SQLException {
  	    String sql = "SELECT iddocumento, titulo, estado FROM documento WHERE propietario = ?";
  	    List<Map<String, Object>> listaResultados = new ArrayList<>();

  	    try (Connection conn = ConexionDB.getInstance().getConnection();
  	         PreparedStatement pstmt = conn.prepareStatement(sql)) {

  	        pstmt.setString(1, nombre);

  	        try (ResultSet rs = pstmt.executeQuery()) {
  	            while (rs.next()) {
  	                Map<String, Object> resultado = new HashMap<>();
  	                resultado.put("id", rs.getInt("iddocumento"));
  	                resultado.put("titulo", rs.getString("titulo"));
  	                resultado.put("estado", rs.getString("estado"));
  	                listaResultados.add(resultado);
  	            }
  	        }
  	    }
  	    return listaResultados;
  	}
  	
  	public List<DocumentoDTO> buscarPorTitulo(String nombre) throws SQLException {
  	    String sql = "SELECT * FROM documento WHERE UPPER(titulo) LIKE ? AND estado != 'Eliminado'";
  	    List<DocumentoDTO> listaDocumentos = new ArrayList<>();

  	    try (Connection conn = ConexionDB.getInstance().getConnection();
  	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
  	        
  	        pstmt.setString(1, "%" + nombre.toUpperCase() + "%"); // Búsqueda insensible a mayúsculas

  	        try (ResultSet rs = pstmt.executeQuery()) {
  	            while (rs.next()) {
  	                DocumentoDTO documento = new DocumentoDTO.BuilderDoc()
  	                        .setIdDocumento(rs.getInt("iddocumento"))
  	                        .setTitulo(rs.getString("titulo"))
  	                        .setFechaPublicacion(rs.getString("fechapublicacion"))
  	                        .setAutores(rs.getString("autores"))
  	                        .setEditorial(rs.getString("editorial"))
  	                        .setPropietario(rs.getString("propietario"))
  	                        .setTipo(rs.getString("tipo"))
  	                        .build();
  	                
  	                listaDocumentos.add(documento);
  	            }
  	        }
  	    }
  	    return listaDocumentos;
  	}
  	
}
