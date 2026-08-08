package modelo.persistencia.dao;

import java.sql.SQLException;

public interface DAO<T>{ //La haremos genereica, pues puede tener diferentes DTO
	void crear(T DTO) throws SQLException;
	T buscarPorId(int id) throws SQLException; 
	T buscarPorNombre(String nombre) throws SQLException;  
	void actualizar(T DTO) throws SQLException;
}
