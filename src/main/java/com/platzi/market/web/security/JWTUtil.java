package com.platzi.market.web.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
@Component
public class JWTUtil {
    private static final String KEY = "pl4tz1";
    public String generateToken(UserDetails userDetails){
        return Jwts.builder().setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*10))
                .signWith(SignatureAlgorithm.HS256,KEY).compact();
    }
    /*Para realizar el proceso de autorización de nuestras peticiones
    * 1 Validar que el jwt sea valido y esté correcto por lo tanto el token debe estar asociado al usuario
    * que hace la petición y que no haya expirado
    * return Validar que el usuario que viene en la petición sea el mismo que extractUsername(token)  y
    * que no haya expirado*/

    public Boolean validateToken(String token, UserDetails userDetails){

        return userDetails.getUsername().equals(extractUsername(token)) && !isTokenExpired(token);
    }
    //getSubject(): es donde está el usuario de la petición
    public String extractUsername(String token){
        return getClaims(token).getSubject();
    }
    public Boolean isTokenExpired(String token){
        return getClaims(token).getExpiration().before(new Date());
    }
    /*Para gestionar que no haya expirado el token:
    * al parser le añado la llave de la firma y cuando verifique que esa firma sea correcta
    * parseClaimsJws(token).getBody() va a obtener los claims o el cuerpo de jwt
    * claims: objeetos dentro del jwt*/
    private Claims getClaims(String token){

        return Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody();
    }
}
