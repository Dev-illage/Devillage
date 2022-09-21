package com.devillage.teamproject.controller.auth;

import com.devillage.teamproject.dto.AuthDto;
import com.devillage.teamproject.entity.User;
import com.devillage.teamproject.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {
    private final AuthService authService;

    @Override
    public Long postAuth() {
        return null;
    }

    @Override
    public Long postJoin(@RequestBody AuthDto.JOIN request) {
        User joinedUser = authService.joinUser(request.toEntity());
        return joinedUser.getId();
    }

    @Override
    public Long postRefresh() {
        return null;
    }

    @Override
    public Long deleteAuth() {
        return null;
    }
}
