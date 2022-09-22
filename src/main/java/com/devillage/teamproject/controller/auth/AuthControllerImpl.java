package com.devillage.teamproject.controller.auth;

import com.devillage.teamproject.dto.AuthDto;
import com.devillage.teamproject.dto.ResponseDto;
import com.devillage.teamproject.entity.User;
import com.devillage.teamproject.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.web.bind.annotation.*;

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
    public Long postRefresh(@RequestHeader Authorization authorization) {
        return null;
    }

    @Override
    public Long deleteAuth() {
        return null;
    }

    @GetMapping("/1")
    public Long returnNum() {
        return 1L;
    }
}
