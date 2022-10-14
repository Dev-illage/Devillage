package com.devillage.teamproject.controller.auth;

import com.devillage.teamproject.dto.AuthDto;
import com.devillage.teamproject.dto.EmailDto;
import com.devillage.teamproject.entity.User;
import com.devillage.teamproject.service.auth.AuthService;
import com.devillage.teamproject.service.auth.EmailAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static com.devillage.teamproject.security.util.JwtConstants.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {
    private final AuthService authService;
    private final EmailAuthService emailAuthService;

    @Override
    public AuthDto.Token postAuth(@RequestBody AuthDto.Login request) {
        return authService.loginUser(request.toEntity());
    }

    @Override
    public Long postJoin(@RequestBody AuthDto.JOIN request) {
        User joinedUser = authService.joinUser(request.toEntity());
        return joinedUser.getId();
    }

    @Override
    public AuthDto.Token postRefresh(@RequestHeader(REFRESH_HEADER) String refreshToken) {
        return authService.reIssueToken(refreshToken);
    }

    @Override
    public String deleteAuth(@RequestHeader(REFRESH_HEADER) String token) {
        authService.deleteToken(token);
        return "OK";
    }
    @Override
    public boolean sendEmail(EmailDto emailDto) {
        emailAuthService.sendEmail(emailDto.getEmail());
        return true;
    }
    @Override
    public boolean verifyAuthKey(EmailDto.AuthInfo authInfo){
        emailAuthService.verifyAuthKey(authInfo.getEmail(), authInfo.getAuthKey());
        return true;
    }
}
