package com.example.cinema_api.security.filter;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.cinema_api.security.jwt.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

public class JwtTokenValidator extends OncePerRequestFilter {
    private JwtUtils jwtUtils;
    public JwtTokenValidator(JwtUtils jwtUtils) {this.jwtUtils = jwtUtils;}

    //Es un metodo que trae la interfaz OncePerRequestFilter
    //Los parametros siempre son, request, response, filter chain
    //Nunca deben ser nulos - usar el import de spring
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain) throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            try {
                //validate token
                DecodedJWT decodedJWT = jwtUtils.verifyToken(token);

                String type =  jwtUtils.getSpecificClaim(decodedJWT,"type").asString();
                if(!"access".equals(type)) {
                    SecurityContextHolder.clearContext();
                    chain.doFilter(request, response);
                    return;
                }

                //damos acceso si el token es valido
                String user = jwtUtils.extractUser(decodedJWT);
                String authorities = jwtUtils.getSpecificClaim(decodedJWT, "authorities").asString();

                //es una cadena de caracteres separadas por comas y lo paso a una lista
                Collection<? extends GrantedAuthority> authoritiesList = AuthorityUtils
                        .commaSeparatedStringToAuthorityList(authorities);

                //create new instancia of authentication and add email and permissions
                Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, authoritiesList);
                //obtener el estado actual del security context holder
                SecurityContext context = SecurityContextHolder.getContext();
                context.setAuthentication(authentication);
                SecurityContextHolder.setContext(context);

            }catch(Exception e) {
                // CORRECCIÓN: Captura la excepción y limpia el contexto para evitar errores de validación no controlados
                SecurityContextHolder.clearContext();
            }
        }
        chain.doFilter(request, response);
    }
}
