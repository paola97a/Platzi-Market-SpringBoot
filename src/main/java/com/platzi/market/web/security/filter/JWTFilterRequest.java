package com.platzi.market.web.security.filter;

import com.platzi.market.domain.service.PlatziUserDetailsService;
import com.platzi.market.web.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JWTFilterRequest extends OncePerRequestFilter {
    /*Filtro de peticiones JWT y extiende de
    OncePerRequestFilter :para que este filtro se ejecute cada que existe una petición*/
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private PlatziUserDetailsService platziUserDetailsService;

    /*Comprobar si lo que viene en el encabezado de la petición es un Token y si ese Token es correcto */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer")){
            String jwt = authorizationHeader.substring(7); //almacena el Token desde el caracter 7 quitando bearer_
            String username = jwtUtil.extractUsername(jwt);

            /*si el usuario es != de nulo y verificar que aún no haya ingresado a nuestra aplicación */
            if(username  != null && SecurityContextHolder.getContext().getAuthentication()==null){
                UserDetails userDetails = platziUserDetailsService.loadUserByUsername(username);
                /* Preguntar si el jwt es correcto*/

                if(jwtUtil.validateToken(jwt,userDetails)){
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }
        filterChain.doFilter(request,response);
    }
}
