package com.devillage.teamproject.controller;

import com.devillage.teamproject.dto.AuthDto;
import com.devillage.teamproject.security.resolver.AccessToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/test")
    public String test(@AccessToken AuthDto.UserInfo userInfo, @RequestBody String hi) {
        System.out.println(userInfo.getEmail());
        System.out.println(userInfo.getId());
        System.out.println(userInfo.getRoles().toString());
        return "Id = " + userInfo.getId() + ", email = " + userInfo.getEmail() + ", String = " + hi;
    }
}
