package com.devillage.teamproject.service.user;

import com.devillage.teamproject.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    User joinUser();

    User findUser();

    User editUser();

    List<User> findUsers();

    void deleteUser();
}
