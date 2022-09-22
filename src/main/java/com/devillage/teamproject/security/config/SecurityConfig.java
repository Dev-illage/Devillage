package com.devillage.teamproject.security.config;

import com.devillage.teamproject.security.exception.CustomAuthenticationEntryPoint;
import com.devillage.teamproject.security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.SecurityConfigurer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableGlobalAuthentication
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationConfig authenticationConfig;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .csrf().disable()
                .cors(Customizer.withDefaults())
                .apply(authenticationConfig)
                .and()
                .authorizeRequests()
                .mvcMatchers(HttpMethod.GET,"/auth/1").hasRole("USER")
                .mvcMatchers(HttpMethod.POST,"/auth/**").permitAll()
                .mvcMatchers(HttpMethod.DELETE,"/auth/**").permitAll()
                .mvcMatchers(HttpMethod.POST,"/posts/**").hasAnyRole("USER","MANAGER","ADMIN")
                .anyRequest().permitAll()
                .and()
                .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint());

        return http.build();
    }
}
