package com.devillage.teamproject.util.auth;

import com.devillage.teamproject.entity.User;
import com.devillage.teamproject.util.ReflectionForStatic;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

import static com.devillage.teamproject.security.util.JwtConstants.REFRESH_TOKEN_EXPIRE_COUNT;

public class AuthTestUtils implements ReflectionForStatic {
    public static String createToken(String email, List<String> roles, Long expire, String secretKey, Long sequence) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("sequence", sequence);
        claims.put("roles", roles);

        byte[] secretKeyBytes = secretKey.getBytes();

        return Jwts.builder()
                .addClaims(claims)
                .setExpiration(new Date(new Date().getTime()+expire))
                .signWith(Keys.hmacShaKeyFor(secretKeyBytes), SignatureAlgorithm.HS256)
                .compact();
    }

    public static String createRefreshToken (String email, List<String> roles, Long expire, String secretKey) {
        byte[] secretKeyBytes = secretKey.getBytes();

        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime()+REFRESH_TOKEN_EXPIRE_COUNT))
                .setSubject(email)
                .signWith(Keys.hmacShaKeyFor(secretKeyBytes), SignatureAlgorithm.HS256)
                .compact();
    }

    public static User createTestUser(String email, String nickName, String password) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        User user = ReflectionForStatic.newInstance(User.class);
        ReflectionForStatic.setField(user, "email", email);
        ReflectionForStatic.setField(user, "nickName", nickName);
        ReflectionForStatic.setField(user, "password", password);
        return user;
    }

    public static User createTestUser(String email, String password) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        User user = ReflectionForStatic.newInstance(User.class);
        ReflectionForStatic.setField(user, "email", email);
        ReflectionForStatic.setField(user, "password", password);
        return user;
    }
}
