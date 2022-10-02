package com.devillage.teamproject.util.auth;

import com.devillage.teamproject.entity.User;
import com.devillage.teamproject.entity.enums.UserStatus;
import com.devillage.teamproject.util.ReflectionForStatic;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

import static com.devillage.teamproject.security.util.JwtConstants.*;

public class AuthTestUtils implements ReflectionForStatic {
    public static String createToken(String userEmail, Long userSequence, List<String> roles, byte[] token, Long expire) {
        Claims claims = Jwts.claims()
                .setSubject(userEmail);
        claims.put(ROLES, roles);
        claims.put(SEQUENCE, userSequence);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime()+expire))
                .signWith(Keys.hmacShaKeyFor(token), SignatureAlgorithm.HS256)
                .compact();
    }

    public static Claims parseToken(String token, byte[] keyByte) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(keyByte))
                .build()
                .parseClaimsJws(token)
                .getBody();
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
        ReflectionForStatic.setField(user, "userStatus", UserStatus.ACTIVE);
        return user;
    }
}
