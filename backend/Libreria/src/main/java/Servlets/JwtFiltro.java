package Servlets;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.jsonwebtoken.Claims;

import config.AppConfig;

@WebFilter({"/usuario/consultar", "/documento", "/documento/eventos","/usuario/datos", "/documento/crear", "/documento/modificar", "/usuario/documentos",
		"/documento/reservar", "/documento/entregar", "/documento/eliminar", "/documento/habilitar", "/documento/titulo", "/usuario/reservas"})
public class JwtFiltro implements Filter {
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse) response;
        HttpServletRequest req = (HttpServletRequest) request;
        
        // Si es una solicitud OPTIONS (preflight), respondemos directamente con 200 OK
        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            res.setHeader("Access-Control-Allow-Origin", AppConfig.getAllowedOrigins());
            res.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            res.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
            res.setStatus(HttpServletResponse.SC_OK);  // Respondemos con un 200 OK
            return;
        }

        // Si no es una solicitud OPTIONS, seguimos con el proceso normal (verificar el token)
        res.setHeader("Access-Control-Allow-Origin", AppConfig.getAllowedOrigins());
        res.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        res.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        
        // Verificar el token en la cabecera Authorization
        String headerAuth = req.getHeader("Authorization");

        if (headerAuth == null || !headerAuth.startsWith("Bearer ")) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.setContentType("application/json");
            res.getWriter().write("{\"error\": \"Token no proporcionado o inválido\"}");
            return;
        }

        String token = headerAuth.substring(7);

        try {
            Claims claims = JwtUtil.validarToken(token); // Método para validar el token
            req.setAttribute("usuario", claims.getSubject()); // Seteamos el usuario en el request
            chain.doFilter(request, response); // Continuamos con la cadena de filtros
        } catch (Exception e) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.setContentType("application/json");
            res.getWriter().write("{\"error\": \"Token inválido o expirado\"}");
        }
    }
}
