package com.itnetwork.club_community.domain.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private Key key;

    public JwtTokenProvider(@Value("${jwt.secret}") String sKey) {
        this.key = Keys.hmacShaKeyFor(sKey.getBytes());
    }

    public String GenerateToken(String email){
        long now = System.currentTimeMillis();
        long expire = 1000L * 60 * 60 * 3;

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expire))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean IsValidate(String token){
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch(JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String GetEmailFromToken(String token){
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

        return claims.getSubject();
    }

}
