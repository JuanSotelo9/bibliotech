package controlador;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.StringReader;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import modelo.documento.dto.ArticuloDTO;
import modelo.documento.dto.DocumentoDTO;
import modelo.documento.dto.LibroDTO;
import modelo.documento.dto.PonenciaDTO;
import modelo.factory.dao.FabricaDAO;
import modelo.otros.dto.EventoDTO;
import modelo.persistencia.dao.ArticuloDAO;
import modelo.persistencia.dao.DocumentoDAO;
import modelo.persistencia.dao.EventoDAO;
import modelo.persistencia.dao.LibroDAO;
import modelo.persistencia.dao.PonenciaDAO;

class GestorDocumentosTest {

    private DocumentoDAO documentoDAO;
    private FabricaDAO fabrica;
    private EventoDAO eventoDAO;
    private ArticuloDAO articuloDAO;
    private LibroDAO libroDAO;
    private PonenciaDAO ponenciaDAO;
    private HttpServletRequest request;
    private GestorDocumentos gestor;

    @BeforeEach
    void setUp() throws Exception {
        documentoDAO = mock(DocumentoDAO.class);
        fabrica = mock(FabricaDAO.class);
        eventoDAO = mock(EventoDAO.class);
        articuloDAO = mock(ArticuloDAO.class);
        libroDAO = mock(LibroDAO.class);
        ponenciaDAO = mock(PonenciaDAO.class);
        request = mock(HttpServletRequest.class);
        gestor = new GestorDocumentos(documentoDAO, fabrica, eventoDAO);

        when(fabrica.crearArticulo()).thenReturn(articuloDAO);
        when(fabrica.crearLibro()).thenReturn(libroDAO);
        when(fabrica.crearPonencia()).thenReturn(ponenciaDAO);
    }

    private void cuerpoJson(String json) throws Exception {
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
    }

    private static String articuloJson() {
        return "{\"tipo\":\"articulo\",\"titulo\":\"Artículo\",\"fechaPublicacion\":\"2024-01-01\","
                + "\"autores\":\"Autor\",\"editorial\":\"Ed\",\"ssn\":\"123456\"}";
    }

    private static String libroJson() {
        return "{\"tipo\":\"libro\",\"titulo\":\"Libro\",\"fechaPublicacion\":\"2024-01-01\","
                + "\"autores\":\"Autor\",\"editorial\":\"Ed\",\"isbn\":\"978-1\",\"numPaginas\":\"200\"}";
    }

    private static String ponenciaJson() {
        return "{\"tipo\":\"ponencia\",\"titulo\":\"Ponencia\",\"fechaPublicacion\":\"2024-01-01\","
                + "\"autores\":\"Autor\",\"editorial\":\"Ed\",\"nombreCongreso\":\"Congreso\",\"isbn\":\"978-2\"}";
    }

    @Test
    void crearDocumento_articulo() throws Exception {
        when(documentoDAO.crear(any(DocumentoDTO.class))).thenReturn(1);
        cuerpoJson(articuloJson());

        String resultado = gestor.crearDocumento(request, "juan");

        assertEquals("{\"mensaje\": \"1\"}", resultado);
        verify(documentoDAO).crear(any(DocumentoDTO.class));
        verify(articuloDAO).crear(any(ArticuloDTO.class));
        verify(eventoDAO).crear(any(EventoDTO.class));
    }

    @Test
    void crearDocumento_libro() throws Exception {
        when(documentoDAO.crear(any(DocumentoDTO.class))).thenReturn(2);
        cuerpoJson(libroJson());

        String resultado = gestor.crearDocumento(request, "juan");

        assertEquals("{\"mensaje\": \"2\"}", resultado);
        verify(libroDAO).crear(any(LibroDTO.class));
        verify(eventoDAO).crear(any(EventoDTO.class));
    }

    @Test
    void crearDocumento_ponencia() throws Exception {
        when(documentoDAO.crear(any(DocumentoDTO.class))).thenReturn(3);
        cuerpoJson(ponenciaJson());

        String resultado = gestor.crearDocumento(request, "juan");

        assertEquals("{\"mensaje\": \"3\"}", resultado);
        verify(ponenciaDAO).crear(any(PonenciaDTO.class));
        verify(eventoDAO).crear(any(EventoDTO.class));
    }

    @Test
    void modificarDocumento() throws Exception {
        cuerpoJson("{\"tipo\":\"libro\",\"titulo\":\"Libro Editado\",\"fechaPublicacion\":\"2024-01-01\","
                + "\"autores\":\"Autor\",\"editorial\":\"Ed\",\"isbn\":\"978-1\",\"numPaginas\":\"250\"}");

        String resultado = gestor.modificarDocumento(request, "juan");

        assertEquals("{\"mensaje\": \"Actualizado\"}", resultado);
        verify(documentoDAO).actualizar(any(DocumentoDTO.class));
        verify(libroDAO).actualizar(any(LibroDTO.class));
        verify(eventoDAO).crear(any(EventoDTO.class));
    }

    @Test
    void eliminarDocumento_softDelete() throws Exception {
        cuerpoJson("{\"iddocumento\": 10}");

        String resultado = gestor.eliminarDocumento(request, "juan");

        assertEquals("{\"mensaje\": \"Actualizado\"}", resultado);
        verify(documentoDAO).actualizarEstado(argThatEstado("Eliminado"));
        verify(eventoDAO).crear(any(EventoDTO.class));
    }

    @Test
    void habilitarDocumento() throws Exception {
        cuerpoJson("{\"iddocumento\": 10}");

        String resultado = gestor.habilitarDocumento(request, "juan");

        assertEquals("{\"mensaje\": \"Actualizado\"}", resultado);
        verify(documentoDAO).actualizarEstado(argThatEstado("Disponible"));
        verify(eventoDAO).crear(any(EventoDTO.class));
    }

    private static DocumentoDTO argThatEstado(String estado) {
        return org.mockito.ArgumentMatchers.argThat(dto -> dto != null && estado.equals(dto.getEstado()));
    }
}