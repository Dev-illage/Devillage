package com.devillage.teamproject.controller.auth;

import com.devillage.teamproject.dto.AuthDto;
import com.devillage.teamproject.entity.User;
import com.devillage.teamproject.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static com.devillage.teamproject.security.util.JwtConstants.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {
    private final AuthService authService;

    @Override
    public AuthDto.Token postAuth(AuthDto.Login request) {
        return authService.loginUser(request.toEntity());
    }

    @Override
    public Long postJoin(AuthDto.JOIN request) {
        User joinedUser = authService.joinUser(request.toEntity());
        return joinedUser.getId();
    }

    @Override
    public AuthDto.Token postRefresh(String refreshToken) {
        return authService.reIssueToken(refreshToken);
    }

    @Override
    public String deleteAuth(String token) {
        authService.deleteToken(token);
        return "OK";
    }
}
