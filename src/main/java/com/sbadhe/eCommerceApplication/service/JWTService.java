package com.sbadhe.eCommerceApplication.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.sbadhe.eCommerceApplication.Model.LocalUser;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JWTService {

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.expiryInSeconds}")
    private int expiryInSeonds;

    @Value("${jwt.algorithm.key}")
    private String algorithmKey;

    private Algorithm algorithm;

    private static final String USERNAME_KEY = "USERNAME";
    private static final String EMAIL_KEY = "EMAILKEY";

    @PostConstruct
    public void postConstruct(){
        algorithm = Algorithm.HMAC256(algorithmKey);
    }

    public String generateJWT(LocalUser user) {
        return JWT.create()
                .withClaim(USERNAME_KEY,user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + (1000 * expiryInSeonds)))
                .withIssuer(issuer)
                .sign(algorithm);
    }

    public String GenerateVerificationJWT(LocalUser user){
        return JWT.create()
                .withClaim(EMAIL_KEY,user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + (10000+ expiryInSeonds)))
                .withIssuer(issuer)
                .sign(algorithm);
    }

    public String getUsername(String token){
        return JWT.decode(token).getClaim(USERNAME_KEY).asString();
    }

}
