package com.example.springboot.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

@Service
public class TokenService {

    private final PrivateKey privateKey;

    // public TokenService() throws Exception {
    //     String keyContent = new String(Files.readAllBytes(Paths.get("src/main/resources/private.key")))
    //             .replaceAll("-----BEGIN PRIVATE KEY-----", "")
    //             .replaceAll("-----END PRIVATE KEY-----", "")
    //             .replaceAll("\\s", "");
    //     byte[] keyBytes = Base64.getDecoder().decode(keyContent);
    //     PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
    //     this.privateKey = KeyFactory.getInstance("RSA").generatePrivate(spec);
    // }

    public TokenService() throws Exception {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("private.key")) {
            if (is == null) {
                throw new RuntimeException("private.key not found in classpath");
            }
            String keyContent = new String(is.readAllBytes())
                    .replaceAll("-----BEGIN PRIVATE KEY-----", "")
                    .replaceAll("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] keyBytes = Base64.getDecoder().decode(keyContent);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            this.privateKey = KeyFactory.getInstance("RSA").generatePrivate(spec);
        }
    }


    public String generateToken(String username, String role) {
        long expirationMillis = 1000 * 60 * 60; // 1 hour
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }
}
