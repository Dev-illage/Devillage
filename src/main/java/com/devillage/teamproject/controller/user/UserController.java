package com.devillage.teamproject.controller.user;

import com.devillage.teamproject.dto.AuthDto;
import com.devillage.teamproject.dto.FileDto;
import com.devillage.teamproject.dto.SingleResponseDto;
import com.devillage.teamproject.dto.UserDto;
import com.devillage.teamproject.security.resolver.AccessToken;
import com.devillage.teamproject.security.util.JwtConstants;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/users")
public interface UserController {

    @PostMapping("/block/{user-id}")
    @ResponseStatus(HttpStatus.OK)
    SingleResponseDto postBlock(@PathVariable("user-id") Long targetId,
                                @RequestHeader(JwtConstants.AUTHORIZATION_HEADER) String token);

    @PatchMapping("/profile")
    @ResponseStatus(HttpStatus.OK)
    Long patchProfile(@AccessToken AuthDto.UserInfo userInfo,
                      @Valid @RequestBody UserDto.PatchProfile patchProfile);

    @PatchMapping("/password")
    @ResponseStatus(HttpStatus.OK)
    boolean patchPassword(@AccessToken AuthDto.UserInfo userInfo,
                          @Valid @RequestBody UserDto.PasswordDto passwordDto);


    @GetMapping("/profile")
    @ResponseStatus(HttpStatus.OK)
    UserDto.Response getProfile(@RequestHeader(JwtConstants.AUTHORIZATION_HEADER) String token);

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteUser(@RequestHeader(JwtConstants.AUTHORIZATION_HEADER) String token);

    @PostMapping("/profile/{user-id}/avatar")
    @ResponseStatus(HttpStatus.CREATED)
    FileDto.SimpleResponse postAvatar(@AccessToken AuthDto.UserInfo userInfo, @PathVariable("user-id") Long userId,
                                      @RequestPart MultipartFile imageFile);

    @DeleteMapping("/profile/{user-id}/avatar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteUser(@AccessToken AuthDto.UserInfo userInfo, @PathVariable("user-id") Long userId);
}
