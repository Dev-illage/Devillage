package com.devillage.teamproject.security.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
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

    public String createToken(String userEmail, Long userSequence, List<String> roles, byte[] token, Long expire) {
        Claims claims = Jwts.claims()
                .setSubject(userEmail);
        claims.put(ROLES, roles);
        claims.put(SEQUENCE, userSequence);
        log.info(roles.toString());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime()+expire))
                .signWith(getSigningKey(token), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims parseToken(String token, byte[] keyByte) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey(keyByte))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String createAccessToken(String userEmail, Long userSequence, List<String> roles) {
        return createToken(userEmail, userSequence, roles, secretByteKey, ACCESS_TOKEN_EXPIRE_COUNT);
    }

    public String createRefreshToken(String userEmail, Long userSequence, List<String> roles) {
        return createToken(userEmail, userSequence, roles, refreshByteKey, REFRESH_TOKEN_EXPIRE_COUNT);
    }

    public Claims parseRefreshToken(String token) {
        return parseToken(token, refreshByteKey);
    }

    public Claims parseAccessToken(String token) {
        return parseToken(token, secretByteKey);
    }

    public Key getSigningKey(byte[] byteKey) {
        return Keys.hmacShaKeyFor(byteKey);
    }

    @Deprecated
    public String getUserEmail(String token) {
        return parseAccessToken(splitToken(token)).getSubject();
    }

    @Deprecated
    public Long getUserId(String token) {
        return Long.valueOf((Integer) parseAccessToken(splitToken(token)).get(SEQUENCE));
    }

    public String splitToken(String token) {
        return token.substring(7);
    }
}
