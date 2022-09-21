package com.devillage.teamproject.controller.auth;

import com.devillage.teamproject.dto.AuthDto;
import com.devillage.teamproject.dto.ResponseDto;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
public interface AuthController {

    @PostMapping("/token")
    @ResponseStatus(HttpStatus.OK)
    ResponseDto.SingleResponseDto<AuthDto.Response> postAuth(@RequestBody AuthDto.Login request);

    @PostMapping("/new")
    @ResponseStatus(HttpStatus.OK)
    Long postJoin(AuthDto.JOIN request);

    @PostMapping("/token/refresh")
    @ResponseStatus(HttpStatus.OK)
    Long postRefresh(@RequestHeader Authorization authorization);

    @DeleteMapping("/token")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    Long deleteAuth();
}
