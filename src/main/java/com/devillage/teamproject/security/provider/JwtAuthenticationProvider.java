package com.devillage.teamproject.security.provider;

import com.devillage.teamproject.security.token.JwtAuthenticationToken;
import com.devillage.teamproject.security.util.JwtConstants;
import com.devillage.teamproject.security.util.JwtTokenUtil;
import io.jsonwebtoken.Claims;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final JwtTokenUtil jwtTokenUtil;

    public JwtAuthenticationProvider(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Claims claims = jwtTokenUtil.parseAccessToken(((JwtAuthenticationToken)authentication).getJwtToken());
        Collection<GrantedAuthority> authorities = getAuthorities(claims);
        String email = claims.getSubject();

        return new JwtAuthenticationToken(email, null, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
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
