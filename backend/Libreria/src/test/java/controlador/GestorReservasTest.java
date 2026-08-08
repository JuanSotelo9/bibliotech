package controlador;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.StringReader;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import modelo.BusinessException;
import modelo.otros.dto.ReservaDTO;
import modelo.persistencia.dao.ReservaDAO;

class GestorReservasTest {

    private ReservaDAO reservaDAO;
    private HttpServletRequest request;
    private GestorReservas gestor;

    @BeforeEach
    void setUp() {
        reservaDAO = mock(ReservaDAO.class);
        request = mock(HttpServletRequest.class);
        gestor = new GestorReservas(reservaDAO);
    }

    private void cuerpoJson(String json) throws Exception {
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
    }

    @Test
    void crearReserva_documentoDisponible() throws Exception {
        cuerpoJson("{\"documento\": 5, \"idreserva\": 1}");
        when(reservaDAO.existeReservaActiva(5)).thenReturn(false);

        int resultado = gestor.crearReserva(request, "juan");

        assertEquals(5, resultado);
        verify(reservaDAO).crear(any(ReservaDTO.class));
    }

    @Test
    void crearReserva_documentoYaReservado() throws Exception {
        cuerpoJson("{\"documento\": 5, \"idreserva\": 1}");
        when(reservaDAO.existeReservaActiva(5)).thenReturn(true);

        BusinessException ex = assertThrows(BusinessException.class, () -> gestor.crearReserva(request, "juan"));

        assertEquals(400, ex.getStatus());
        assertEquals("DOCUMENTO_YA_RESERVADO", ex.getCodigo());
        verify(reservaDAO, never()).crear(any(ReservaDTO.class));
    }

    @Test
    void entregarLibro() throws Exception {
        cuerpoJson("{\"documento\": 5, \"idreserva\": 1}");
        when(reservaDAO.actualizar(any(ReservaDTO.class))).thenReturn(5);

        int resultado = gestor.entregarLibro(request);

        assertEquals(5, resultado);
        verify(reservaDAO).actualizar(any(ReservaDTO.class));
    }
}
