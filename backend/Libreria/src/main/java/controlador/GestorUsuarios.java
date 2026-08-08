package controlador;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.mindrot.jbcrypt.BCrypt;

import servlets.JwtUtil;
import modelo.BusinessException;
import modelo.persistencia.UsuarioDAO;
import modelo.persistencia.UsuarioDTO;
import modelo.Usuario;

public class GestorUsuarios {
    private UsuarioDAO usuarioDAO;
    private ObjectMapper objectMapper;

    public GestorUsuarios() {
        usuarioDAO = new UsuarioDAO();
        objectMapper = new ObjectMapper(); // Jackson para convertir JSON
    }

    public String registrarUsuario(HttpServletRequest request) throws IOException, SQLException {
        Usuario usuario = objectMapper.readValue(request.getReader(), Usuario.class);
        UsuarioDTO usuarioDTO = new UsuarioDTO.Builder()
        	    .setNombre(usuario.getNombre())
        	    .setCorreoElectronico(usuario.getCorreoElectronico())
        	    .setDireccionFisica(usuario.getDireccionFisica())
        	    .setNumeroTelefonico(usuario.getNumeroTelefonico())
        	    .setContrasena(BCrypt.hashpw(usuario.getContrasena(), BCrypt.gensalt()))
        	    .build();
        UsuarioDTO encontrado = usuarioDAO.buscarPorNombre(usuario.getNombre());
        if (encontrado != null) {
            throw new BusinessException(400, "USUARIO_EXISTENTE", "Nombre de usuario ya existe");
        }
        usuarioDAO.crear(usuarioDTO);
        return "{\"mensaje\": \"Creado\"}";
    }

    public String loginUsuario(HttpServletRequest request) throws IOException, SQLException {
        Usuario usuario = objectMapper.readValue(request.getReader(), Usuario.class);
        UsuarioDTO encontrado = usuarioDAO.buscarPorNombre(usuario.getNombre());

        if (encontrado == null) {
            throw new BusinessException(404, "USUARIO_NO_ENCONTRADO", "Nombre de usuario no existe");
        }
        if (!BCrypt.checkpw(usuario.getContrasena(), encontrado.getContrasena())) {
            throw new BusinessException(401, "CREDENCIALES_INVALIDAS", "Contraseña inválida");
        }
        return "{\"token\": \"" + JwtUtil.generarToken(usuario.getNombre()) + "\"}";
    }
    
    public String obtenerUsuario(String usuario) throws SQLException, JsonProcessingException {
        UsuarioDTO encontrado = usuarioDAO.buscarPorNombre(usuario);
        
        if (encontrado == null) {
            throw new BusinessException(404, "USUARIO_NO_ENCONTRADO", "Usuario no encontrado");
        }
        Usuario usuarioObj = new Usuario();
        usuarioObj.setNombre(encontrado.getNombre());
        usuarioObj.setCorreoElectronico(encontrado.getCorreoElectronico());
        usuarioObj.setDireccionFisica(encontrado.getDireccionFisica());
        usuarioObj.setNumeroTelefonico(encontrado.getNumeroTelefonico());

        return objectMapper.writeValueAsString(usuarioObj);
    }
    
    public String consultarUsuario(HttpServletRequest request) throws IOException, SQLException {
        Usuario usuario = objectMapper.readValue(request.getReader(), Usuario.class);
        return obtenerUsuario(usuario.getNombre()); 
    }
}
