package com.devillage.teamproject.util.security;

import com.devillage.teamproject.security.token.JwtAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

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