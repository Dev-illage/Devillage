package com.devillage.teamproject.security.filter;

import com.devillage.teamproject.security.token.JwtAuthenticationToken;
import io.jsonwebtoken.ExpiredJwtException;
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
            response.sendRedirect("/auth/refresh");
        } finally {
            filterChain.doFilter(request,response);
        }
    }

    private void checkJwt(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, String jwt) throws IOException, ServletException {
        if (!StringUtils.hasLength(jwt) || !jwt.startsWith(BEARER_TYPE)) {
            filterChain.doFilter(request, response);
        } else {
            getAuthentication(jwt);
        }
    }

    private void getAuthentication(String jwt) {
        Authentication authentication = new JwtAuthenticationToken(getToken(jwt));
        authenticationManager.authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public String getToken(String jwt) {
        String[] splittedJwt = jwt.split(" ");
        return splittedJwt[1];
    }
}
