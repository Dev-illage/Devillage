package com.devillage.teamproject.controller.user;

import com.devillage.teamproject.dto.UserDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/users")
public interface UserController {

    @PostMapping("/block/{user-id}")
    @ResponseStatus(HttpStatus.OK)
    Long postBlock(@PathVariable("user-id") Long id);

    @PatchMapping("/profile/{user-id}")
    @ResponseStatus(HttpStatus.OK)
    Long postProfile(@PathVariable("user-id") Long id,
                     UserDto.PatchProfile request);

    @PatchMapping("/pwd/{user-id}")
    @ResponseStatus(HttpStatus.OK)
    Long postPassword(@PathVariable("user-id") Long id,
                      String password);

    @GetMapping("/profile/{user-id}")
    @ResponseStatus(HttpStatus.OK)
    UserDto.Response getProfile(@PathVariable("user-id") Long id);

    @DeleteMapping("/{user-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteUser(@PathVariable("user-id") Long id);
}
