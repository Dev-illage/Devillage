package com.devillage.teamproject.controller.auth;

import com.devillage.teamproject.dto.AuthDto;
import com.devillage.teamproject.dto.ResponseDto;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.devillage.teamproject.security.util.JwtConstants.REFRESH_HEADER;

@RequestMapping("/auth")
public interface AuthController {

    @PostMapping("/token")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseDto.SingleResponseDto<AuthDto.Token> postAuth(@RequestBody AuthDto.Login request);

    @PostMapping("/new")
    @ResponseStatus(HttpStatus.OK)
    Long postJoin(AuthDto.JOIN request);

    @PostMapping("/token/refresh")
    @ResponseStatus(HttpStatus.OK)
    ResponseDto.SingleResponseDto<AuthDto.Token> postRefresh(@RequestHeader(REFRESH_HEADER) String refreshToken);

    @DeleteMapping("/token")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    String deleteAuth(String token);
}
