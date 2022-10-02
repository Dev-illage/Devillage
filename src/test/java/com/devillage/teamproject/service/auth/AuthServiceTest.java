package com.devillage.teamproject.service.auth;

import com.devillage.teamproject.dto.AuthDto;
import com.devillage.teamproject.entity.RefreshToken;
import com.devillage.teamproject.entity.Role;
import com.devillage.teamproject.entity.User;
import com.devillage.teamproject.entity.UserRoles;
import com.devillage.teamproject.entity.enums.RoleType;
import com.devillage.teamproject.repository.role.RoleRepository;
import com.devillage.teamproject.repository.token.RefreshTokenRepository;
import com.devillage.teamproject.repository.user.UserRepository;
import com.devillage.teamproject.repository.user_roles.UserRolesRepository;
import com.devillage.teamproject.security.util.JwtConstants;
import com.devillage.teamproject.security.util.JwtTokenUtil;
import com.devillage.teamproject.util.ReflectionForStatic;
import com.devillage.teamproject.util.TestConfig;
import com.devillage.teamproject.util.auth.AuthTestUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

import static com.devillage.teamproject.security.util.JwtConstants.*;
import static com.devillage.teamproject.util.ReflectionForStatic.*;
import static com.devillage.teamproject.util.TestConstants.*;
import static com.devillage.teamproject.util.TestConstants.ROLES;
import static com.devillage.teamproject.util.TestConstants.SECRET_KEY;
import static com.devillage.teamproject.util.auth.AuthTestUtils.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRolesRepository userRolesRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    public void injectBean() throws NoSuchFieldException, IllegalAccessException {
        setField(authService, "passwordEncoder", passwordEncoder);
    }

    @Test
    @DisplayName("joinUser Success test")
    public void test1() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        // given
        User user = createTestUser(EMAIL1, NICKNAME1, PASSWORD1);
        Role role = newInstance(Role.class);
        setField(role,"roleType",RoleType.ROLE_USER);
        UserRoles userRoles = new UserRoles(role, user);

        given(userRepository.save(any(User.class))).willReturn(user);
        given(roleRepository.getRoleByRoleType(any(RoleType.class))).willReturn(role);
        given(userRolesRepository.save(userRoles)).willReturn(userRoles);

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

        String accessToken = createToken(EMAIL1,  ID1, ROLES, SECRET_KEY.getBytes(), SECRET_EXPIRE);
        String refreshToken = createToken(EMAIL1,  ID1, ROLES, REFRESH_KEY.getBytes(), SECRET_EXPIRE);

        User savedUser = createTestUser(EMAIL1, passwordEncoder.encode(PASSWORD1));
        setField(savedUser, "id", ID1);

        Role role = newInstance(Role.class);
        setField(role, "roleType", RoleType.ROLE_USER);
        UserRoles userRoles = new UserRoles(role, savedUser);
        List<UserRoles> userRolesList = List.of(userRoles);
        setField(savedUser, "userRoles", userRolesList);

        given(userRepository.findUserByEmail(EMAIL1)).willReturn(Optional.of(savedUser));
        given(jwtTokenUtil.createAccessToken(savedUser.getEmail(),savedUser.getId(),ROLES)).willReturn(accessToken);
        given(jwtTokenUtil.createRefreshToken(savedUser.getEmail(), savedUser.getId(),ROLES)).willReturn(refreshToken);

        // when
        AuthDto.Token token = authService.loginUser(user);

        // then
        assertThat(token.getBearer()).isEqualTo(BEARER);
        assertThat(token.getAccessToken()).isEqualTo(accessToken);
        assertThat(token.getRefreshToken()).isEqualTo(refreshToken);
    }

    @Test
    @DisplayName("tokenReissue success test")
    public void test3()  {
        // given
        String splitToken = createToken(EMAIL1,  ID1, ROLES, REFRESH_KEY.getBytes(), SECRET_EXPIRE);
        String request = BEARER + splitToken;
        RefreshToken existingToken = new RefreshToken(splitToken);

        Claims claims = Jwts.claims()
                        .setSubject(EMAIL1);
        claims.put(SEQUENCE, ID1.intValue());
        claims.put(JwtConstants.ROLES, ROLES);;

        given(refreshTokenRepository.findRefreshTokenByTokenValue(splitToken)).willReturn(Optional.of(existingToken));
        given(jwtTokenUtil.parseRefreshToken(splitToken)).willReturn(claims);
        given(jwtTokenUtil.splitToken(request)).willReturn(splitToken);
        given(jwtTokenUtil.createAccessToken(EMAIL1, ID1, ROLES)).willReturn(createToken(EMAIL1, ID1, ROLES, SECRET_KEY.getBytes(), SECRET_EXPIRE));
        given(jwtTokenUtil.createRefreshToken(EMAIL1, ID1, ROLES)).willReturn(createToken(EMAIL1, ID1, ROLES, REFRESH_KEY.getBytes(), SECRET_EXPIRE));


        // when
        AuthDto.Token token = authService.reIssueToken(request);

        String accessTokenSubject = AuthTestUtils.parseToken(token.getAccessToken(), SECRET_KEY.getBytes()).getSubject();
        String refreshTokenSubject = AuthTestUtils.parseToken(token.getRefreshToken(), REFRESH_KEY.getBytes()).getSubject();

        // then
        assertThat(accessTokenSubject).isEqualTo(EMAIL1);
        assertThat(refreshTokenSubject).isEqualTo(EMAIL1);
    }

    @Test
    @DisplayName("deleteToken success token")
    public void test4() {
        // given
        String request = BEARER + createToken(EMAIL1, ID1, ROLES, REFRESH_KEY.getBytes(), SECRET_EXPIRE);

        String existingToken = request.substring(7);

        given(jwtTokenUtil.splitToken(request)).willReturn(existingToken);
        given(refreshTokenRepository.findRefreshTokenByTokenValue(existingToken)).willReturn(Optional.of(new RefreshToken(request)));

        // when
        authService.deleteToken(request);

        // then
        verify(refreshTokenRepository, times(1)).delete(new RefreshToken(request));
    }
}
