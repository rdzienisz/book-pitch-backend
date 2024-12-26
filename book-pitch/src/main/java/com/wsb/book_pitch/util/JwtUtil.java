package com.wsb.book_pitch.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.util.Base64;
import java.util.Date;
import java.util.List;

public class JwtUtil {

    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; // 24 hours

    private static final String SECRET_KEY = "mySecretmySecretmySecretmySecret"
            + "mySecretmySecretmySecretmySecretmySecret";

    public static String generateToken(String username, List<String> roles) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(Keys.hmacShaKeyFor(Base64.getDecoder().decode(SECRET_KEY)),
                        SignatureAlgorithm.HS256)
                .compact();
    }

    public static Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(Base64.getDecoder().decode(SECRET_KEY)))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }
}