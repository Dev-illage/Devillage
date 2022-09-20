package com.devillage.teamproject.service.user;

import com.devillage.teamproject.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    User joinUser(User user);

    User findUser(Long userId);

    User editUser(User user);

    List<User> findUsers(int page, int size);

    void deleteUser(Long memberId);
}
