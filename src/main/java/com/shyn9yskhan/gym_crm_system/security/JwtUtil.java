package com.shyn9yskhan.gym_crm_system.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.time.Instant;

public class JwtUtil {
    private final Key key;
    private final long expirationSeconds;

    public JwtUtil(String secret, long expirationSeconds) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.expirationSeconds = expirationSeconds;
    }

    public String generateToken(String username) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationSeconds * 1000L);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build()
                    .parseClaimsJws(token).getBody().getSubject();
        } catch (JwtException ex) {
            return null;
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException ex) {
            return false;
        }
    }

    /**
     * Returns the expiration date of the token, or null if token is invalid.
     */
    public Date getExpiration(String token) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build()
                    .parseClaimsJws(token).getBody();
            return claims.getExpiration();
        } catch (JwtException ex) {
            return null;
        }
    }

    /**
     * Convenience: returns expiration as Instant, or null if token invalid/no expiration.
     */
    public Instant getExpirationInstant(String token) {
        Date exp = getExpiration(token);
        return exp != null ? exp.toInstant() : null;
    }
}
