package com.devillage.teamproject.service.user;

import com.devillage.teamproject.entity.Block;
import com.devillage.teamproject.entity.User;

import java.util.List;

public interface UserService {
    User joinUser(User user);

    User findUser(Long userId);

    User editUser(User user);

    List<User> findUsers(int page, int size);

    void deleteUser(Long userId);

    Block blockUser(Long destUserId, String token);
}
