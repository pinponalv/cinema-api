package com.example.cinema_api.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtUtils {
    @Value("${security.jwt.private.key}")
    private String privateKey;
    @Value("${security.jwt.user.generator}")
    private String userGenerator;

    private static final long accessTokenExpiration = 1800000; //30 minutes
    private static final long refreshTokenExpiration = 604800000; //7 days

    public String generateAccesToken(Authentication authentication) {
        return buildToken(authentication, accessTokenExpiration, "access");
    }

    public String generateRefreshToken(Authentication authentication) {
        return buildToken(authentication, refreshTokenExpiration, "refresh");
    }


    private String buildToken(Authentication authentication, long expirationMs, String tokenType) {
        Algorithm algorithm = Algorithm.HMAC256(privateKey);
        String getUser = authentication.getPrincipal().toString(); //usuario que se quiere autenticar

        String getAuthorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        //header.payload.signature son las 3 partes de un jwt
        /**Notas para withClaim("type", tokenType):
         * "type" funciona como una etiqueta/nombre, pudo a ver llevado cualquier otro nombre como "banana"
         * "type" es como quedara guardado dentor del jwt de esta manera:
         * {
         *     "type" : "access" o "refresh"
         * }
         * **/
        return JWT.create()
                .withIssuer(this.userGenerator) //el usuario que emite el token (pinpon)
                .withSubject(getUser)//usuario al que se le genera el token
                .withClaim("authorities", getAuthorities)//roles y permisos que tiene el usuario
                .withClaim("type", tokenType) //distingue entre access o refresh segun que metodo lo llame
                .withIssuedAt(new Date()) //fecha de creacion
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationMs))
                .withJWTId(UUID.randomUUID().toString())
                .withNotBefore(new Date()) //valido para usar cuando se crea
                .sign(algorithm); //firmo criptograficamente el payload con el algoritmo

    }

    //decodificar y validar token
    public DecodedJWT verifyToken(String token) {
        try{
            Algorithm algorithm = Algorithm.HMAC256(privateKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(this.userGenerator).build();

            //devuelve el jwt decodificado si esta bien
            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT;
        }catch (JWTVerificationException e){
            throw  new JWTVerificationException("Invalid token");
        }
    }

    //method get username of token
    public String extractUser(DecodedJWT decodedJWT){
        //subject es el user que estableci para generar el token (pinpon)
        return decodedJWT.getSubject().toString();
    }

    //method for get claims especific
    public Claim getSpecificClaim(DecodedJWT decodedJWT, String claim){
        return decodedJWT.getClaim(claim);
    }

    //method for all claim
    public Map<String, Claim> allClaims(DecodedJWT decodedJWT){
        return decodedJWT.getClaims();
    }
}
