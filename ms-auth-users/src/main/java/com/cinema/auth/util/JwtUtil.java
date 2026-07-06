package com.cinema.auth.util;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

// Clase utilitaria para generar y validar tokens JWT, los tokens jwt son los que se usan para la autenticación y autorización en microservicios
@Component// component para que spring lo pueda inyectar en otros servicios
public class JwtUtil {

    // Llave secreta para firmar los tokens. En producción se lee desde variables de entorno.
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    
    // El token expirará en 24 horas (86400000 milisegundos)
    private final long jwtExpirationMs = 86400000;

    public String generateToken(String email, String role) {// Genera un token JWT con el email y el rol del usuario
        Map<String, Object> claims = new HashMap<>();// Creamos un mapa de claims (información que queremos guardar en el token)
        claims.put("role", role); // Metemos el rol dentro del token para que los otros servicios lo lean

        return Jwts.builder()//returnamos el token generado con los claims, el email, la fecha de emisión y la fecha de expiración
                .setClaims(claims)//seteamos los claims que queremos guardar en el token
                .setSubject(email)//seteamos el subject del token, que será el email del usuario
                .setIssuedAt(new Date())//seteamos la fecha de emisión del token
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))//seteamos la fecha de expiración del token
                .signWith(key)//firmamos el token con la llave secreta
                .compact();//compact() genera el token en formato String
    }
}