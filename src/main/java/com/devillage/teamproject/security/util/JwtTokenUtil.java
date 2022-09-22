package com.devillage.teamproject.security.util;

import com.devillage.teamproject.entity.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static com.devillage.teamproject.security.util.JwtConstants.*;

@Slf4j
@Component
public class JwtTokenUtil {
    private final byte[] secretByteKey;
    private final byte[] refreshByteKey;

    public JwtTokenUtil(@Value("${jwt.secretKey}") String secretKey,
                        @Value("${jwt.refreshKey}") String refreshKey) {
        this.secretByteKey = secretKey.getBytes();
        this.refreshByteKey = refreshKey.getBytes();
    }


    public String createAccessToken(String userEmail, Long userSequence, List<String> roles) {
        Claims claims = Jwts.claims()
                .setSubject(userEmail);
        claims.put(ROLES, roles);
        claims.put(SEQUENCE, userSequence);
        log.info(roles.toString());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime()+ACCESS_TOKEN_EXPIRE_COUNT))
                .signWith(getSigningKey(secretByteKey), SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(String email) {
        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime()+REFRESH_TOKEN_EXPIRE_COUNT))
                .setSubject(email)
                .signWith(getSigningKey(refreshByteKey), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims parseRefreshToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey(refreshByteKey))
                .build()
                .parseClaimsJwt(token)
                .getBody();
    }

    public Key getSigningKey(byte[] byteKey) {
        return Keys.hmacShaKeyFor(byteKey);
    }
}
