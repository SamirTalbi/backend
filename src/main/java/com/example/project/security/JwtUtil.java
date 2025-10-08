package com.example.project.security;

import com.example.project.model.UserModel;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private final String jwtSecret = "superSecretKeyForJwtTokenVerySecureAndLongEnough123!";
    private final long jwtExpirationMs = 86400000; // 1 jour

    private final Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

    public String generateToken(UserModel user) {
        return Jwts.builder()
            .setSubject(user.getEmail())
            .claim("id", user.getId())
            .claim("role", user.getRole().toString())
            .setIssuedAt(new Date())
            .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }


    // ✅ Récupère l’email depuis le token
    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    // ✅ Récupère tous les claims
    public Map<String, Object> getClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody();
    }

    // ✅ Vérifie si le token est valide
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
