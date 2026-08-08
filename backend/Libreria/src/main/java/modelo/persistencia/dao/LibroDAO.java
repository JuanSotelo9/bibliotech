package modelo.persistencia.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import modelo.documento.dto.LibroDTO;
import modelo.persistencia.ConexionDB;

public class LibroDAO implements DAO<LibroDTO>{

	public void crear(LibroDTO libro) throws SQLException {
		String sql = "INSERT INTO libro (iddocumento, numeropaginas, isbn) VALUES (?, ?, ?)";
		try(Connection conexion = ConexionDB.getInstance().getConnection();
			PreparedStatement pstmt = conexion.prepareStatement(sql)){
			
			pstmt.setInt(1, libro.getIdDocumento());
			if (!libro.getNumeroPaginas().isEmpty()) {
				pstmt.setInt(2, Integer.parseInt(libro.getNumeroPaginas()));
	        } else {
	        	pstmt.setNull(2, java.sql.Types.INTEGER);
	        }
			
			pstmt.setString(3, libro.getIsbn());
			pstmt.executeUpdate();
		}
	}


	public void actualizar(LibroDTO libro) throws SQLException {
	    
	    String sqlLibro = "UPDATE libro SET isbn = ?, numeropaginas = ? WHERE iddocumento = ?";

	    try (Connection conn = ConexionDB.getInstance().getConnection()) {
	        conn.setAutoCommit(false); 

	        try (PreparedStatement pstmt = conn.prepareStatement(sqlLibro)) {

	        	pstmt.setString(1, libro.getIsbn());
	        	if (!libro.getNumeroPaginas().isEmpty()) {
					pstmt.setInt(2, Integer.parseInt(libro.getNumeroPaginas()));
		        } else {
		        	pstmt.setNull(2, java.sql.Types.INTEGER);
		        }
	        	pstmt.setInt(3, libro.getIdDocumento());
	        	pstmt.executeUpdate();

	            conn.commit(); // Confirma los cambios
	        } catch (SQLException e) {
	            conn.rollback(); // Revierte en caso de error
	            throw e;
	        }
	    }
	 }

	public LibroDTO buscarPorId(int id) throws SQLException {
	    String sql = "SELECT isbn, numeropaginas FROM libro WHERE iddocumento = ?";
	    
	    try (Connection conn = ConexionDB.getInstance().getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, id);
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                return new LibroDTO.BuilderLibro()
	                        .setIsbn(rs.getString("isbn"))
	                        .setNumeroPaginas(rs.getString("numeropaginas"))
	                        .build();
	            }
	        }
	    }
	    return null;
	}


	@Override
	public LibroDTO buscarPorNombre(String nombre) throws SQLException {
		String sql = "SELECT l.iddocumento, l.isbn, l.numeropaginas, d.titulo, d.fechapublicacion, d.autores, d.editorial, d.estado, d.propietario, d.tipo " +
		             "FROM libro l JOIN documento d ON l.iddocumento = d.iddocumento WHERE UPPER(d.titulo) LIKE ?";
		try (Connection conn = ConexionDB.getInstance().getConnection();
		     PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, "%" + nombre.toUpperCase() + "%");
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return new LibroDTO.BuilderLibro()
							.setIsbn(rs.getString("isbn"))
							.setNumeroPaginas(rs.getString("numeropaginas"))
							.setIdDocumento(rs.getInt("iddocumento"))
							.setTitulo(rs.getString("titulo"))
							.setFechaPublicacion(rs.getString("fechapublicacion"))
							.setAutores(rs.getString("autores"))
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



}
