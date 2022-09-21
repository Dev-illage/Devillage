package com.devillage.teamproject.security.provider;

import com.devillage.teamproject.entity.User;

import com.devillage.teamproject.security.token.JwtAuthenticationToken;
import com.devillage.teamproject.security.util.JwtConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final byte[] secretKeyByte;

    public JwtAuthenticationProvider(@Value("${jwt.secretKey}") String secretKey) {
        this.secretKeyByte = secretKey.getBytes();
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Claims claims = parseJwtAccessToken(authentication.toString());
        Collection<GrantedAuthority> authorities = getAuthorities(claims);
        String username = claims.getSubject();

        return new JwtAuthenticationToken(username, null, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private Claims parseJwtAccessToken(String jwt) {
        return (Claims)Jwts.parserBuilder()
                .setSigningKey(secretKeyByte)
                .build()
                .parseClaimsJwt(jwt)
                .getBody();
    }

    private Collection<GrantedAuthority> getAuthorities(Claims claims) {
        List<String> roles = (List) claims.get(JwtConstants.ROLES);
        List<GrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(
                        role -> authorities.add(() -> role)
                );
        return authorities;
    }
}
