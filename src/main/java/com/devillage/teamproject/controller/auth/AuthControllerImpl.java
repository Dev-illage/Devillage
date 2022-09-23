package com.devillage.teamproject.controller.auth;

import com.devillage.teamproject.dto.AuthDto;
import com.devillage.teamproject.dto.ResponseDto;
import com.devillage.teamproject.entity.User;
import com.devillage.teamproject.security.util.JwtConstants;
import com.devillage.teamproject.security.util.JwtTokenUtil;
import com.devillage.teamproject.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.web.bind.annotation.*;

import static com.devillage.teamproject.security.util.JwtConstants.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {
    private final AuthService authService;

    @Override
    public ResponseDto.SingleResponseDto<AuthDto.Token> postAuth(@RequestBody AuthDto.Login request) {
        AuthDto.Token token = authService.loginUser(request.toEntity());
        return ResponseDto.SingleResponseDto.of(token);
    }

    @Override
    public Long postJoin(@RequestBody AuthDto.JOIN request) {
        User joinedUser = authService.joinUser(request.toEntity());
        return joinedUser.getId();
    }

    @Override
    public ResponseDto.SingleResponseDto<AuthDto.Token> postRefresh(@RequestHeader(REFRESH_HEADER) String refreshToken) {
        AuthDto.Token token = authService.reIssue(refreshToken);
        return ResponseDto.SingleResponseDto.of(token);
    }

    @Override
    public Long deleteAuth() {
        return null;
    }
}
