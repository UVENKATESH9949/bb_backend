package com.BrainBlitz.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.BrainBlitz.entity.Role;

import java.security.Key;
import java.util.Date;

@Component
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    // ✅ Generate JWT token
    public String generateToken(String email, Role role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ✅ Extract email from token
    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // ✅ Validate token
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
    
 // Add this method to JwtService.java
    public Date extractExpiration(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

 // ═══════════════════════════════════════════════════════════════════════════
 // JwtService — ADD these two methods to your existing JwtService class
 //
 // Your existing JwtService already has: generateToken, extractUsername,
 // validateToken, etc.
 //
 // ADD the following method if it doesn't already exist:
 // ═══════════════════════════════════════════════════════════════════════════

 // ─── ADD THIS METHOD to your existing JwtService class ───────────────────────
 //
//     @Autowired
//     private BlacklistedTokenRepository blacklistedTokenRepository;
 //
//     /**
//      * Checks if this token has been blacklisted (i.e., user logged out).
//      * Called by /auth/me to reject invalidated tokens.
//      */
//     public boolean isTokenBlacklisted(String token) {
//         return blacklistedTokenRepository.existsByToken(token);
//     }
 //
 // ─────────────────────────────────────────────────────────────────────────────
 //
 // NOTE: Your BlacklistedTokenRepository already has existsByToken(String token)
 // as mentioned in your documentation, so this is a 3-line addition.
 //
 // If your JwtService already has this method, no change is needed.
 // ─────────────────────────────────────────────────────────────────────────────

 // This file is a reference guide — not a full replacement of JwtService.
 // Only add what's missing.

 public class JwtServiceAdditions {
     // Reference only — see comments above
 }
    
}