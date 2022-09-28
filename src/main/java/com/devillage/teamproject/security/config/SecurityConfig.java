package com.devillage.teamproject.security.config;

import com.devillage.teamproject.security.oauth.CustomOauth2Service;
import com.devillage.teamproject.security.oauth.CustomSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@EnableGlobalAuthentication
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationConfig authenticationConfig;
    private final CustomSuccessHandler customSuccessHandler;
    private final CustomOauth2Service customOauth2Service;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .csrf().disable()
                .cors().configurationSource(corsConfigurationSource())
//                .cors().disable()
//                .configurationSource(corsConfigurationSource())
                .and()
                .apply(authenticationConfig)
                .and()
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .mvcMatchers("/auth/**").permitAll()
                .mvcMatchers(HttpMethod.POST,"/posts/**").permitAll()
                .anyRequest().permitAll()
                .and()
                .oauth2Login()
//                .defaultSuccessUrl("/auth/oauth")
                .successHandler(customSuccessHandler)
                .userInfoEndpoint()
                .userService(customOauth2Service);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedHeader("*");
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedHeader("*");
        config.setAllowedMethods(List.of("GET","POST","DELETE","PATCH","OPTION","PUT"));
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
