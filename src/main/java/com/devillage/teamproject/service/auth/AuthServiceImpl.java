package com.devillage.teamproject.service.auth;

import com.devillage.teamproject.dto.AuthDto;
import com.devillage.teamproject.entity.User;
import com.devillage.teamproject.exception.BusinessLogicException;
import com.devillage.teamproject.exception.ExceptionCode;
import com.devillage.teamproject.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public User joinUser(User user) {
        if (userRepository.findUserByEmail(user.getEmail()).isEmpty()) {
            passwordEncoder.encode(user.getPassword());
        } else {
            throw new BusinessLogicException(ExceptionCode.EXISTING_USER);
        }

        return userRepository.save(user);
    }
}
