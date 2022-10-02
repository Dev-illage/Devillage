package com.devillage.teamproject.controller.user;

import com.devillage.teamproject.dto.AuthDto;
import com.devillage.teamproject.dto.SingleResponseDto;
import com.devillage.teamproject.dto.UserDto;
import com.devillage.teamproject.security.resolver.AccessToken;
import com.devillage.teamproject.security.util.JwtConstants;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/users")
public interface UserController {

    @PostMapping("/block/{user-id}")
    @ResponseStatus(HttpStatus.OK)
    SingleResponseDto postBlock(@PathVariable("user-id") Long targetId,
                                @RequestHeader(JwtConstants.AUTHORIZATION_HEADER) String token);

    @PatchMapping("/profile/{user-id}")
    @ResponseStatus(HttpStatus.OK)
    Long patchProfile(@PathVariable("user-id") Long id,
                      UserDto.PatchProfile request);

    @PostMapping("/profile/{user-id}")
    @ResponseStatus(HttpStatus.OK)
    Long postProfile(@PathVariable("user-id") Long id,
                     @RequestBody String password,
                     @AccessToken AuthDto.UserInfo userInfo);

    @PatchMapping("/pwd/{user-id}")
    @ResponseStatus(HttpStatus.OK)
    Long postPassword(@PathVariable("user-id") Long id,
                      String password);

    @GetMapping("/profile")
    @ResponseStatus(HttpStatus.OK)
    UserDto.Response getProfile(@RequestHeader(JwtConstants.AUTHORIZATION_HEADER) String token);

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteUser(@RequestHeader(JwtConstants.AUTHORIZATION_HEADER) String token);
}
