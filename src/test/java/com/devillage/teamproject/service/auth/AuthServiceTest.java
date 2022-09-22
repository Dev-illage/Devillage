package com.devillage.teamproject.service;

import com.devillage.teamproject.entity.User;
import com.devillage.teamproject.repository.user.UserRepository;
import com.devillage.teamproject.security.config.PasswordEncoderConfig;
import com.devillage.teamproject.service.auth.AuthServiceImpl;
import com.devillage.teamproject.util.Reflection;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest implements Reflection {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    public void injectBean() throws NoSuchFieldException, IllegalAccessException {
        setField(authService, "passwordEncoder", passwordEncoder);
    }

    @Test
    @DisplayName("joinUser Success test")
    public void test1() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        // given
        String email = "email@test.com";
        String nickName = "jamjamTest";
        String password = "testPassword123!";

        User user = newInstance(User.class);
        setField(user, "email", email);
        setField(user, "nickName", nickName);
        setField(user, "password", password);

        given(userRepository.save(any(User.class))).willReturn(user);

        // when

        User savedUser = authService.joinUser(user);

        Assertions.assertThat(savedUser.getPassword()).isEqualTo(user.getPassword());
        Assertions.assertThat(savedUser.getEmail()).isEqualTo(user.getEmail());
        Assertions.assertThat(savedUser.getNickName()).isEqualTo(user.getNickName());
    }
}
