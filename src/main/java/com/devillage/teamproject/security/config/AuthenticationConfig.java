package com.devillage.teamproject.security.config;

import com.devillage.teamproject.security.exception.FilterErrorManager;
import com.devillage.teamproject.security.filter.JwtAuthenticationFilter;
import com.devillage.teamproject.security.provider.JwtAuthenticationProvider;
import com.devillage.teamproject.security.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class AuthenticationConfig extends AbstractHttpConfigurer<AuthenticationConfig, HttpSecurity> {
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final FilterErrorManager filterErrorManager;

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);

        builder.addFilterBefore(new JwtAuthenticationFilter(authenticationManager, filterErrorManager), UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(jwtAuthenticationProvider);
    }
}
