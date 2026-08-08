package controlador;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import org.mindrot.jbcrypt.BCrypt;

import modelo.BusinessException;
import modelo.persistencia.UsuarioDAO;
import modelo.persistencia.UsuarioDTO;

class GestorUsuariosTest {

    private UsuarioDAO usuarioDAO;
    private HttpServletRequest request;
    private GestorUsuarios gestor;

    @BeforeEach
    void setUp() throws Exception {
        usuarioDAO = mock(UsuarioDAO.class);
        request = mock(HttpServletRequest.class);
        gestor = new GestorUsuarios(usuarioDAO);
    }

    private void cuerpoJson(String json) throws Exception {
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
    }

    private static String usuarioJson() {
        return "{"
                + "\"nombre\": \"juan\","
                + "\"contrasena\": \"secreta1\","
                + "\"correoElectronico\": \"juan@correo.com\","
                + "\"direccionFisica\": \"Calle 123\","
                + "\"numeroTelefonico\": \"3001234567\""
                + "}";
    }

    @Test
    void registrarUsuario_exito() throws Exception {
        cuerpoJson(usuarioJson());
        when(usuarioDAO.buscarPorNombre("juan")).thenReturn(null);

        String resultado = gestor.registrarUsuario(request);

        assertEquals("{\"mensaje\": \"Creado\"}", resultado);
        verify(usuarioDAO).crear(any(UsuarioDTO.class));
    }

    @Test
    void registrarUsuario_emailInvalido() throws Exception {
        cuerpoJson("{\"nombre\": \"juan\",\"contrasena\": \"secreta1\",\"correoElectronico\": \"no-es-correo\",\"numeroTelefonico\": \"3001234567\"}");

        BusinessException ex = assertThrows(BusinessException.class, () -> gestor.registrarUsuario(request));

        assertEquals("EMAIL_INVALIDO", ex.getCodigo());
        verify(usuarioDAO, never()).crear(any(UsuarioDTO.class));
    }

    @Test
    void registrarUsuario_contrasenaCorta() throws Exception {
        cuerpoJson("{\"nombre\": \"juan\",\"contrasena\": \"abc\",\"correoElectronico\": \"juan@correo.com\",\"numeroTelefonico\": \"3001234567\"}");

        BusinessException ex = assertThrows(BusinessException.class, () -> gestor.registrarUsuario(request));

        assertEquals("CONTRASENA_CORTA", ex.getCodigo());
        verify(usuarioDAO, never()).crear(any(UsuarioDTO.class));
    }

    @Test
    void loginUsuario_exito() throws Exception {
        String hash = BCrypt.hashpw("secreta1", BCrypt.gensalt());
        UsuarioDTO encontrado = new UsuarioDTO.Builder()
                .setNombre("juan")
                .setContrasena(hash)
                .build();

        cuerpoJson(usuarioJson());
        when(usuarioDAO.buscarPorNombre("juan")).thenReturn(encontrado);

        String resultado = gestor.loginUsuario(request);

        assertTrue(resultado.contains("\"token\""));
        assertTrue(resultado.length() > 10);
    }

    @Test
    void loginUsuario_contrasenaIncorrecta() throws Exception {
        String hash = BCrypt.hashpw("otra-clave", BCrypt.gensalt());
        UsuarioDTO encontrado = new UsuarioDTO.Builder()
                .setNombre("juan")
                .setContrasena(hash)
                .build();

        cuerpoJson(usuarioJson());
        when(usuarioDAO.buscarPorNombre("juan")).thenReturn(encontrado);

        BusinessException ex = assertThrows(BusinessException.class, () -> gestor.loginUsuario(request));

        assertEquals(401, ex.getStatus());
        assertEquals("CREDENCIALES_INVALIDAS", ex.getCodigo());
    }

    @Test
    void loginUsuario_usuarioNoExiste() throws Exception {
        cuerpoJson(usuarioJson());
        when(usuarioDAO.buscarPorNombre("juan")).thenReturn(null);

        BusinessException ex = assertThrows(BusinessException.class, () -> gestor.loginUsuario(request));

        assertEquals(404, ex.getStatus());
        assertEquals("USUARIO_NO_ENCONTRADO", ex.getCodigo());
    }
}
