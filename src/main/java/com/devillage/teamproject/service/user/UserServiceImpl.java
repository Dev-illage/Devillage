package com.devillage.teamproject.service.user;

import com.devillage.teamproject.entity.User;
import com.devillage.teamproject.exception.BusinessLogicException;
import com.devillage.teamproject.exception.ExceptionCode;
import com.devillage.teamproject.repository.user.UserRepository;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User joinUser(User user) {
        return null;
    }

    @Override
    public User findUser(Long userId) {
        return null;
    }

    @Override
    public User editUser(User user) {
        return null;
    }

    @Override
    public List<User> findUsers(int page, int size) {
        return null;
    }

    @Override
    public void deleteUser(Long memberId) {

    }
}
