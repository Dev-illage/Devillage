package com.devillage.teamproject.controller.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController("/auth")
public interface AuthController {

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    Long postAuth();

    @PostMapping("/new")
    @ResponseStatus(HttpStatus.OK)
    Long postJoin();

    @PostMapping("/token/refresh")
    @ResponseStatus(HttpStatus.OK)
    Long postRefresh();

    @DeleteMapping("/token")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    Long deleteAuth();
}
