package com.devillage.teamproject.controller.auth;

import com.devillage.teamproject.dto.AuthDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.devillage.teamproject.security.util.JwtConstants.REFRESH_HEADER;

@RequestMapping("/auth")
public interface AuthController {

    @PostMapping("/token")
    @ResponseStatus(HttpStatus.CREATED)
    AuthDto.Token postAuth(@RequestBody AuthDto.Login request);

    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    Long postJoin(AuthDto.JOIN request);

    @PostMapping("/token/refresh")
    @ResponseStatus(HttpStatus.OK)
    AuthDto.Token postRefresh(String refreshToken);

    @DeleteMapping("/token")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    String deleteAuth(String token);
}
