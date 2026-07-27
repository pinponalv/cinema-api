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

    public String createToken(Authentication authentication) {
        Algorithm algorithm = Algorithm.HMAC256(privateKey);

        //obtengo el usuario que se quiere registrar
        String getUser = authentication.getPrincipal().toString();

        String getAuthorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String generateToken = JWT.create()
                .withIssuer(this.userGenerator) //el usuario que genera el nombre (pinpon)
                .withSubject(getUser) //usuario al que se le genera el token
                .withClaim("authorities", getAuthorities) //datos traidos del jwt
                .withIssuedAt(new Date()) //fecha de creacion
                .withExpiresAt(new Date(System.currentTimeMillis() + 1800000)) //vence en 30min el token
                .withJWTId(UUID.randomUUID().toString()) //genero un id random y lo paso a string
                .withNotBefore(new Date()) //valido para usar cuando se crea
                .sign(algorithm);

        //System.out.println(generateToken);
        return generateToken;
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
