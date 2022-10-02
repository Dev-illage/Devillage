package com.devillage.teamproject.util.security;

import com.devillage.teamproject.security.provider.JwtAuthenticationProvider;
import com.devillage.teamproject.security.token.JwtAuthenticationToken;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser withMockCustomUser) {
        JwtAuthenticationToken user = new JwtAuthenticationToken(withMockCustomUser.username(), null, List.of(new SimpleGrantedAuthority(withMockCustomUser.role())));
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(user);
        return context;
    }
}
