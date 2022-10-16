package com.devillage.teamproject.controller.auth;

import com.devillage.teamproject.dto.AuthDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.devillage.teamproject.security.util.JwtConstants.REFRESH_HEADER;

@RequestMapping("/auth")
public interface AuthController {

    @PostMapping("/token")
    @ResponseStatus(HttpStatus.CREATED)
    AuthDto.Token postAuth(@Valid @RequestBody AuthDto.Login request);

    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    Long postJoin(@Valid @RequestBody AuthDto.JOIN request);

    @PostMapping("/token/refresh")
    @ResponseStatus(HttpStatus.OK)
    AuthDto.Token postRefresh(@RequestHeader(REFRESH_HEADER) String refreshToken);

    @DeleteMapping("/token")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    String deleteAuth(@RequestHeader(REFRESH_HEADER) String token);
}
