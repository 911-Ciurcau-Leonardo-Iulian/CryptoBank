package com.cyberasap.cryptobank.jwt;

import com.cyberasap.cryptobank.domain.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenUtil {
    private static final long EXPIRE_DURATION = 365L * 24 * 60 * 60 * 1000; // 365 days

    public static String SECRET_KEY;

    @Value("${app.config.secret-key}")
    private void setSecretKey(String secretKey) {
        SECRET_KEY = secretKey;
    }

    public String generateAccessToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getEmail());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuer("CryptoBank")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = getUserDetailsFromToken(token);
        return new UsernamePasswordAuthenticationToken(userDetails, "");
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    private UserDetails getUserDetailsFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        String email = claims.getSubject();

        return User.builder()
                .email(email)
                .password("")
                .build();
    }

    public static String getEmailFromAuthorizationHeader(String header) {
        if (header == null || !header.startsWith("Bearer ")) {
            return null;
        }
        String token = header.substring(7);
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
