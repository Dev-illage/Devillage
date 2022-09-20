package com.devillage.teamproject.controller.user;

import com.devillage.teamproject.dto.UserDto;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserControllerImpl implements UserController {
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
        return null;
    }

    @Override
    public void deleteUser(Long id) {

    }
}
