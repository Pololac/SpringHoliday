package com.hb.cda.springholiday.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter{
    private JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        //On récupère le contenu du header Authorization où peut se trouver le token
        String authHeader = request.getHeader("Authorization");

        // Si on est sur la route login ou refresh, qu'on a pas de header ou pas de type jwt, on passe, on s'arrête ici
        if (request.getRequestURI().startsWith("/api/login")
                || request.getRequestURI().startsWith("/api/refresh-token")
                || authHeader == null
                || !authHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Récupération du JWT si présent dans le header et vérification qu'il est "legit"
        String jwt = authHeader.substring(7);

        try {
            UserDetails user = jwtUtil.validateToken(jwt);
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
            filterChain.doFilter(request, response);
        } catch (AuthorizationDeniedException e) {
            response.sendError(HttpStatus.FORBIDDEN.value(), e.getMessage());
        }
    }
}
