package com.devillage.teamproject.controller.user;

import com.devillage.teamproject.dto.AuthDto;
import com.devillage.teamproject.dto.SingleResponseDto;
import com.devillage.teamproject.dto.UserDto;
import com.devillage.teamproject.entity.Block;
import com.devillage.teamproject.service.file.FileService;
import com.devillage.teamproject.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {
    private final UserService userService;
    private final FileService fileService;

    @Override
    public SingleResponseDto postBlock(Long targetId, String token) {
        Long srcUserId = 1L; // jwt 적용전
        Block block = userService.blockUser(targetId, token);
        return SingleResponseDto.of(UserDto.BlockUserDto.of(block));
    }

    @Override
    public Long patchProfile(AuthDto.UserInfo userInfo, UserDto.PatchProfile patchProfile) {
        userService.editUser(userInfo.getId(), patchProfile.getNickName(), patchProfile.getStatusMessage());
        return userInfo.getId();
    }

    @Override
    public boolean patchPassword(AuthDto.UserInfo userInfo, UserDto.PasswordDto passwordDto) {
        userService.updatePassword(userInfo, passwordDto.getPassword(), passwordDto.getUpdatePassword());
        return true;
    }

    @Override
    public UserDto.Response getProfile(String token) {
        return UserDto.Response.of(userService.findUser(token));
    }

    @Override
    public void deleteUser(String token) {
        userService.deleteUser(token);
    }

    @Override
    public UserDto.Response postAvatar(AuthDto.UserInfo userInfo, MultipartFile imageFile,
                                       HttpServletRequest request) {
        return UserDto.Response.of(fileService.addAvatarToUser(userInfo.getId(), imageFile, request));
    }

    @Override
    public void deleteAvatar(AuthDto.UserInfo userInfo) {
        fileService.deleteUserAvatar(userInfo.getId());
    }


}
