package com.devillage.teamproject.controller.user;

import com.devillage.teamproject.dto.SingleResponseDto;
import com.devillage.teamproject.dto.UserDto;
import com.devillage.teamproject.service.user.UserService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserControllerImpl implements UserController {
    private final UserService userService;

    public UserControllerImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Long postBlock(Long id) {
        return null;
    }

    @Override
    public Long postProfile(Long id, UserDto.PatchProfile request) {
        return null;
    }

    @Override
    public Long postPassword(Long id, String password) {
        return null;
    }

    @Override
    public UserDto.Response getProfile(Long id) {
        return UserDto.Response.of(userService.findUser(id));
    }

    @Override
    public void deleteUser(Long id) {
        userService.deleteUser(id);
    }
}
