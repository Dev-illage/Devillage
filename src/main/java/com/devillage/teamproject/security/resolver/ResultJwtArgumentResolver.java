package com.devillage.teamproject.security.resolver;

import com.devillage.teamproject.dto.AuthDto;
import com.devillage.teamproject.exception.BusinessLogicException;
import com.devillage.teamproject.security.util.JwtConstants;
import com.devillage.teamproject.security.util.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.List;

import static com.devillage.teamproject.exception.ExceptionCode.MALFORMED_JWT_EXCEPTION;
import static com.devillage.teamproject.security.util.JwtConstants.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResultJwtArgumentResolver implements HandlerMethodArgumentResolver {
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(AccessToken.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        log.info("시작");
        String accessToken = webRequest.getHeader(AUTHORIZATION_HEADER);
        log.info("accessToken = {}", accessToken);

        if ( accessToken==null || accessToken.equals(NULL_TOKEN) ) {
//            throw new BusinessLogicException(MALFORMED_JWT_EXCEPTION);
            return null;
        }

        String token = jwtTokenUtil.splitToken(accessToken);
        Claims claims = jwtTokenUtil.parseAccessToken(token);
        String email = claims.getSubject();
        List<String> roles = (List) claims.get(ROLES);
        Long id = Long.valueOf((Integer)claims.get(SEQUENCE));

        return AuthDto.UserInfo.builder()
                .id(id)
                .email(email)
                .roles(roles)
                .build();
    }
}
