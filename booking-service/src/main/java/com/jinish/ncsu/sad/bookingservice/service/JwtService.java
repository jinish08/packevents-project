package com.jinish.ncsu.sad.bookingservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys; // Import this
import jakarta.annotation.PostConstruct; // Import this
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key; // Import this
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    // IMPORTANT: In a real app, this should be in application.properties
    private final static String secretString = "your-super-secret-and-long-enough-key-for-hs256-and-it-must-be-at-least-this-long";

    private Key key;

    // This method runs once after the service is created
    @PostConstruct
    public void init() {
        // Convert the string key into a secure Key object
        this.key = Keys.hmacShaKeyFor(secretString.getBytes());
    }


    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
