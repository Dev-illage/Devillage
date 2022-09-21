package com.devillage.teamproject.service.auth;

import com.devillage.teamproject.dto.AuthDto;
import com.devillage.teamproject.entity.User;

public interface AuthService {
    User joinUser(User user);

    AuthDto.Response loginUser(User user);
}
