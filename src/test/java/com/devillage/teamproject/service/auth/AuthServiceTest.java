package com.devillage.teamproject.service.auth;

import com.devillage.teamproject.dto.AuthDto;
import com.devillage.teamproject.entity.Role;
import com.devillage.teamproject.entity.User;
import com.devillage.teamproject.entity.UserRoles;
import com.devillage.teamproject.entity.enums.RoleType;
import com.devillage.teamproject.repository.user.UserRepository;
import com.devillage.teamproject.security.util.JwtTokenUtil;
import com.devillage.teamproject.util.ReflectionForStatic;
import com.devillage.teamproject.util.TestConfig;
import com.devillage.teamproject.util.auth.AuthTestUtils;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.devillage.teamproject.util.ReflectionForStatic.*;
import static com.devillage.teamproject.util.TestConstants.*;
import static com.devillage.teamproject.util.auth.AuthTestUtils.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@Slf4j
@Import({TestConfig.class})
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest implements ReflectionForStatic {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @BeforeEach
    public void injectBean() throws NoSuchFieldException, IllegalAccessException {
        setField(authService, "passwordEncoder", passwordEncoder);
    }

    @Test
    @DisplayName("joinUser Success test")
    public void test1() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        // given
        User user = createTestUser(EMAIL1, NICKNAME1, PASSWORD1);

        given(userRepository.save(any(User.class))).willReturn(user);

        // when
        User savedUser = authService.joinUser(user);

        assertThat(savedUser.getPassword()).isEqualTo(user.getPassword());
        assertThat(savedUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(savedUser.getNickName()).isEqualTo(user.getNickName());
    }

    @Test
    @DisplayName("loginUser Success test")
    public void test2() throws NoSuchFieldException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        //given
        User user = createTestUser(EMAIL1, PASSWORD1);

        String accessToken = createToken(EMAIL1, ROLES, SECRET_EXPIRE, SECRET_KEY, ID1);
        String refreshToken = createRefreshToken(EMAIL1, ROLES, SECRET_EXPIRE, REFRESH_KEY);

        User savedUser = createTestUser(EMAIL1, passwordEncoder.encode(PASSWORD1));
        setField(savedUser, "id", ID1);

        Role role = newInstance(Role.class);
        setField(role, "roleType", RoleType.ROLE_USER);
        UserRoles userRoles = new UserRoles(role, savedUser);
        List<UserRoles> userRolesList = List.of(userRoles);
        setField(savedUser, "userRoles", userRolesList);

        given(userRepository.findUserByEmail(EMAIL1)).willReturn(Optional.of(savedUser));
        given(jwtTokenUtil.createAccessToken(savedUser.getEmail(),savedUser.getId(),ROLES)).willReturn(accessToken);
        given(jwtTokenUtil.createRefreshToken(savedUser.getEmail())).willReturn(refreshToken);

        // when
        AuthDto.Token token = authService.loginUser(user);

        // then
        assertThat(token.getBearer()).isEqualTo(BEARER);
        assertThat(token.getAccessToken()).isEqualTo(accessToken);
        assertThat(token.getRefreshToken()).isEqualTo(refreshToken);
    }
}
