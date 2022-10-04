package com.devillage.teamproject.service.user;

import com.devillage.teamproject.entity.Block;
import com.devillage.teamproject.entity.User;

import java.util.List;

public interface UserService {
    User joinUser(User user);

    User findUser(String token);

    User editUser(User user);

    List<User> findUsers(int page, int size);

    void deleteUser(String token);

    Block blockUser(Long destUserId, String token);

    User findVerifiedUser(Long userId);

    Long checkUserPassword(Long id, String password, Long tokenId
    );

    boolean updatePassword(String token,String password);
}
