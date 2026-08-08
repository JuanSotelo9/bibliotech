package controlador;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.mindrot.jbcrypt.BCrypt;

import Servlets.JwtUtil;
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

    public String registrarUsuario(HttpServletRequest request) throws JsonProcessingException {
        try {
            Usuario usuario = objectMapper.readValue(request.getReader(), Usuario.class);
            UsuarioDTO usuarioDTO = new UsuarioDTO.Builder()
            	    .setNombre(usuario.getNombre())
            	    .setCorreoElectronico(usuario.getCorreoElectronico())
            	    .setDireccionFisica(usuario.getDireccionFisica())
            	    .setNumeroTelefonico(usuario.getNumeroTelefonico())
            	    .setContrasena(BCrypt.hashpw(usuario.getContrasena(), BCrypt.gensalt()))
            	    .build();
            UsuarioDTO encontrado = usuarioDAO.buscarPorNombre(usuario.getNombre());
            if (encontrado == null) {
                usuarioDAO.crear(usuarioDTO);
                return "{\"mensaje\": \"Creado\"}";
            } else {
                return "{\"mensaje\": \"Nombre de usuario ya existe\"}";
            }
        } catch (IOException e) {
        	Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("mensaje", "Error al leer JSON: " + e.getMessage());
            return objectMapper.writeValueAsString(errorResponse);
        } catch (SQLException e) {
        	Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("mensaje", "Error en la base de datos: " + e.getMessage());

            return objectMapper.writeValueAsString(errorResponse);
        }
    }

    public String loginUsuario(HttpServletRequest request) throws JsonProcessingException {
        try {
            Usuario usuario = objectMapper.readValue(request.getReader(), Usuario.class);
            UsuarioDTO encontrado = usuarioDAO.buscarPorNombre(usuario.getNombre());

            if (encontrado != null) {
                if (BCrypt.checkpw(usuario.getContrasena(), encontrado.getContrasena())) {
                    return "{\"token\": \"" + JwtUtil.generarToken(usuario.getNombre()) + "\"}";
                } else {
                    return "{\"mensaje\": \"Contraseña inválida\"}";
                }
            } else {
                return "{\"mensaje\": \"Nombre de usuario no existe\"}";
            }
        } catch (IOException e) {
        	Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("mensaje", "Error al leer JSON: " + e.getMessage());
            return objectMapper.writeValueAsString(errorResponse);
        } catch (SQLException e) {
        	Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("mensaje", "Error en la base de datos: " + e.getMessage());

            return objectMapper.writeValueAsString(errorResponse);
        }
    }
    
    public String obtenerUsuario(String usuario) throws JsonProcessingException {
        try {
            UsuarioDTO encontrado = usuarioDAO.buscarPorNombre(usuario);
            
            if (encontrado != null) {
                Usuario usuarioObj = new Usuario();
                usuarioObj.setNombre(encontrado.getNombre());
                usuarioObj.setCorreoElectronico(encontrado.getCorreoElectronico());
                usuarioObj.setDireccionFisica(encontrado.getDireccionFisica());
                usuarioObj.setNumeroTelefonico(encontrado.getNumeroTelefonico());

                return objectMapper.writeValueAsString(usuarioObj);
            }
            return null; 
        } catch (SQLException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("mensaje", "Error en la base de datos: " + e.getMessage());
            
            try {
                return objectMapper.writeValueAsString(errorResponse);
            } catch (IOException ioException) {
                return "{\"mensaje\": \"Error al procesar JSON\"}";
            }
        }
    }
    
    public String consultarUsuario(HttpServletRequest request) throws IOException {
        
        	Usuario usuario = objectMapper.readValue(request.getReader(), Usuario.class);
            
            return obtenerUsuario(usuario.getNombre()); 
        
    }
}
