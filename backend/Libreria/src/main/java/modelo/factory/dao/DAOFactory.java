package modelo.factory.dao;

import modelo.persistencia.dao.ArticuloDAO;
import modelo.persistencia.dao.LibroDAO;
import modelo.persistencia.dao.PonenciaDAO;

public interface DAOFactory {

	LibroDAO crearLibro(); 
	PonenciaDAO crearPonencia(); 
	ArticuloDAO crearArticulo();

}
