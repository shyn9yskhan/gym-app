package com.shyn9yskhan.gym_crm_system.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.time.Instant;

/**
 * Utility class for creating and validating JSON Web Tokens (JWT).
 *
 * <p>Uses an HMAC secret key (HS256) to sign tokens. All parsing/validation methods
 * handle {@link JwtException} internally and return {@code null} or {@code false}
 * on failure so callers can decide how to react.</p>
 */
public class JwtUtil {

    private final Key key;
    private final long expirationSeconds;

    /**
     * Create a JwtUtil.
     *
     * @param secret            secret used to sign JWTs (must be sufficiently long for HS256)
     * @param expirationSeconds token lifetime in seconds
     */
    public JwtUtil(String secret, long expirationSeconds) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.expirationSeconds = expirationSeconds;
    }

    /**
     * Generate a signed JWT for the given username.
     *
     * @param username subject (username) to put into the token's "sub" claim
     * @return compact JWT string
     */
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

    /**
     * Extract the username (subject) from a JWT.
     *
     * @param token compact JWT string
     * @return username if the token is valid; {@code null} if token is invalid or cannot be parsed
     */
    public String extractUsername(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build()
                    .parseClaimsJws(token).getBody().getSubject();
        } catch (JwtException ex) {
            return null;
        }
    }

    /**
     * Validate the token signature and structure.
     *
     * @param token compact JWT string
     * @return {@code true} if token is valid; {@code false} otherwise
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException ex) {
            return false;
        }
    }

    /**
     * Get expiration {@link Date} from token.
     *
     * @param token compact JWT string
     * @return expiration date if token is valid; {@code null} if token is invalid or expiration is not present
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
     * Get expiration as {@link Instant}.
     *
     * @param token compact JWT string
     * @return expiration instant if token is valid; {@code null} otherwise
     */
    public Instant getExpirationInstant(String token) {
        Date exp = getExpiration(token);
        return exp != null ? exp.toInstant() : null;
    }
}
