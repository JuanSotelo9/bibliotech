package modelo.factory.dao;

import modelo.persistencia.dao.ArticuloDAO;
import modelo.persistencia.dao.LibroDAO;
import modelo.persistencia.dao.PonenciaDAO;

public class FabricaDAO implements DAOFactory{

	@Override
	public ArticuloDAO crearArticulo() {
		ArticuloDAO articuloDAO = new ArticuloDAO(); 
		return articuloDAO;
	}

	@Override
	public LibroDAO crearLibro() {
		LibroDAO libroDAO = new LibroDAO(); 
		return libroDAO;
	}

	@Override
	public PonenciaDAO crearPonencia() {
		PonenciaDAO ponenciaDAO = new PonenciaDAO(); 
		return ponenciaDAO;
	}

}
