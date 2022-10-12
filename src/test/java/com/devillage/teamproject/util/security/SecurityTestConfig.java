package com.devillage.teamproject.util.security;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.HttpMethod.GET;

@TestConfiguration
public class SecurityTestConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .csrf().disable()
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .mvcMatchers("/auth/**").permitAll()
                .mvcMatchers(GET, "/posts/**").permitAll()
                .mvcMatchers(POST,"/posts/**").hasAnyRole("USER","MANAGER","ADMIN")
                .mvcMatchers(PATCH, "/posts/**").hasAnyRole("USER","MANAGER","ADMIN")
                .mvcMatchers(DELETE, "/posts/**").hasAnyRole("USER","MANAGER","ADMIN")
                .mvcMatchers(GET, "/users/**").hasAnyRole("USER","MANAGER","ADMIN")
                .mvcMatchers(POST, "/users/**").hasAnyRole("USER","MANAGER","ADMIN")
                .mvcMatchers(DELETE,"/users/**").hasAnyRole("USER","MANAGER","ADMIN")
                .mvcMatchers(PATCH,"/users/**").hasAnyRole("USER","MANAGER","ADMIN")
                .mvcMatchers(GET,"/files/**").permitAll()
                .mvcMatchers(POST,"/files/**").hasAnyRole("USER","MANAGER","ADMIN")
                .mvcMatchers(GET, "/test/**").hasAnyRole("USER","MANAGER","ADMIN")
                .mvcMatchers(GET, "/chat/**").hasAnyRole("USER","MANAGER","ADMIN")
                .anyRequest().denyAll()
                .and()
                .oauth2Login();

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedHeader("*");
        config.setAllowedMethods(List.of("GET","POST","DELETE","PATCH","OPTION","PUT"));
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}