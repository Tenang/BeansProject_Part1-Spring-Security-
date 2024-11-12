package jeryck.dev.beans.securite;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jeryck.dev.beans.service.UtilisateurService;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Service
public class JwtFilter extends OncePerRequestFilter {

    private UtilisateurService utilisateurService;
    private  JwtService jwtService;

    public JwtFilter(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = null;
        String username  = null;

        // bearer: eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6Implcnlja3NvbnRlbmFuZ0BnbWFpbC5jb20iLCJub20iOiJGcmFuY2lzIEplcnlja3NvbiAifQ.7dng2bgNk4H8hBDb9A9VY90iMzvmvgLIq6Y6MynSCvM
       final String authorization = request.getHeader("Authorization");

       if (authorization!=null && authorization.startsWith("Bearer")){
           token = authorization.substring(7);

           this.utilisateurService

       }

    }
}
