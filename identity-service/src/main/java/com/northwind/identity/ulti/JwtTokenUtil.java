package com.northwind.identity.ulti;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.Map;

public class JwtTokenUtil {
    @Value("${token.expire-duration}")
    private long expireDuration;
    @Value("${token.secret-key}")
    private String secretKey;

    public JwtTokenUtil() {
    }

    public String generateToken(String subject) {
        Date issueAt = new Date();
        Date expiryDate = new Date(issueAt.getTime() + expireDuration);
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(issueAt)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public Map<String, String> getSubjectFromJWT(String token, TokenExtractor extractor) {
        Claims claims = (Claims)Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        return extractor.extract(claims.getSubject());
    }

    public void validateToken(String authToken) {
        Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken);
    }
}