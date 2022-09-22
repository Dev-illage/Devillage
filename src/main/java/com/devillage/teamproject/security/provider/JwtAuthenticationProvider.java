package com.devillage.teamproject.security.provider;

import com.devillage.teamproject.entity.User;

import com.devillage.teamproject.security.token.JwtAuthenticationToken;
import com.devillage.teamproject.security.util.JwtConstants;
import com.devillage.teamproject.security.util.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
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
    private final byte[] secretKeyByte;
    private final JwtTokenUtil jwtTokenUtil;

    public JwtAuthenticationProvider(@Value("${jwt.secretKey}") String secretKey,
                                     JwtTokenUtil jwtTokenUtil) {
        this.secretKeyByte = secretKey.getBytes();
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Claims claims = parseJwtAccessToken(((JwtAuthenticationToken)authentication).getJwtToken());
        Collection<GrantedAuthority> authorities = getAuthorities(claims);
        String username = claims.getSubject();

        return new JwtAuthenticationToken(username, null, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }



    private Claims parseJwtAccessToken(String jwt) {
            return Jwts.parserBuilder()
                    .setSigningKey(jwtTokenUtil.getSigningKey(secretKeyByte))
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
