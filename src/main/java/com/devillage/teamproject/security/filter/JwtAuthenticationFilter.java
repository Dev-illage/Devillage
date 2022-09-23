package com.devillage.teamproject.security.filter;

import com.devillage.teamproject.controller.exception.JwtAuthenticationException;
import com.devillage.teamproject.exception.ExceptionCode;
import com.devillage.teamproject.security.token.JwtAuthenticationToken;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.SignatureException;

import static com.devillage.teamproject.exception.ExceptionCode.*;
import static com.devillage.teamproject.security.util.JwtConstants.*;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = request.getHeader(AUTHORIZATION_HEADER);

        try {
            checkJwt(request, response, filterChain, jwt);
        } catch (ExpiredJwtException e) {
            throw new JwtAuthenticationException(EXPIRED_JWT_EXCEPTION);
        } catch (UnsupportedJwtException e) {
            throw new JwtAuthenticationException(UNSUPPORTED_JWT_EXCEPTION);
        } catch (MalformedJwtException e) {
            throw new JwtAuthenticationException(MALFORMED_JWT_EXCEPTION);
        }
    }

    private void checkJwt(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, String jwt) throws IOException, ServletException {
        if (!StringUtils.hasLength(jwt) || !jwt.startsWith(BEARER_TYPE)) {
            filterChain.doFilter(request, response);
        } else {
            getAuthentication(jwt);
            filterChain.doFilter(request, response);
        }
    }

    private void getAuthentication(String jwt) {
        Authentication authentication = new JwtAuthenticationToken(getToken(jwt));
        authenticationManager.authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public String getToken(String jwt) {
        String[] splitJwt = jwt.split(" ");
        return splitJwt[1];
    }
}
