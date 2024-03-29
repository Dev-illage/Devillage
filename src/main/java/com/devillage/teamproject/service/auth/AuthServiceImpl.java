package com.devillage.teamproject.service.auth;

import com.devillage.teamproject.dto.AuthDto;
import com.devillage.teamproject.entity.RefreshToken;
import com.devillage.teamproject.entity.Role;
import com.devillage.teamproject.entity.User;
import com.devillage.teamproject.entity.UserRoles;
import com.devillage.teamproject.entity.enums.RoleType;
import com.devillage.teamproject.entity.enums.UserStatus;
import com.devillage.teamproject.exception.BusinessLogicException;
import com.devillage.teamproject.exception.ExceptionCode;
import com.devillage.teamproject.repository.role.RoleRepository;
import com.devillage.teamproject.repository.token.RefreshTokenRepository;
import com.devillage.teamproject.repository.user.UserRepository;
import com.devillage.teamproject.repository.user_roles.UserRolesRepository;
import com.devillage.teamproject.security.util.JwtConstants;
import com.devillage.teamproject.security.util.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.devillage.teamproject.security.util.JwtConstants.*;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService{
    private final UserRolesRepository userRolesRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public User joinUser(User user) {
        duplicateEmailCheck(user);
        user.setUserDefaults();

        Role role = roleRepository.getRoleByRoleType(RoleType.ROLE_USER);
        User savedUser = userRepository.save(user);

        userRolesRepository.save(new UserRoles(role, user));
        return savedUser;
    }

    @Override
    public AuthDto.Token loginUser(User user) {
        User findUser = findUserByEmail(user.getEmail());

        checkUser(user, findUser);
        List<String> roles = getRolesFromUser(findUser);

        AuthDto.Token responseTokenDto = createTokenDto(findUser.getEmail(), findUser.getId(), roles);
        refreshTokenRepository.save(new RefreshToken(responseTokenDto.getRefreshToken()));

        return responseTokenDto;
    }

    @Override
    public AuthDto.Token reIssueToken(String reqeust) {
        String token = jwtTokenUtil.splitToken(reqeust);

        RefreshToken existingToken = checkIfTokenExists(token);

        Claims claims = jwtTokenUtil.parseRefreshToken(existingToken.getTokenValue());
        String email = claims.getSubject();
        List<String> roles = (List<String>)claims.get(ROLES);
        Long userSequence = Long.valueOf((Integer)claims.get(SEQUENCE));

        AuthDto.Token responseTokenDto = createTokenDto(email, userSequence, roles);

        refreshTokenRepository.delete(existingToken);
        refreshTokenRepository.save(new RefreshToken(responseTokenDto.getRefreshToken()));

        return responseTokenDto;
    }

    @Override
    public void deleteToken(String request) {
        String token = jwtTokenUtil.splitToken(request);
        RefreshToken existingToken = checkIfTokenExists(token);
        refreshTokenRepository.delete(existingToken);
    }

    private void userStatusVerification(User findUser) {
        switch (findUser.getUserStatus()) {
            case ACTIVE:
                break;
            case BLOCK:
                throw new BusinessLogicException(ExceptionCode.BLOCKED_USER);
            case RESIGNED:
                throw new BusinessLogicException(ExceptionCode.USER_NOT_FOUND);
        }
    }

    private void userProviderVerification(User findUser) {
        if (findUser.getOauthProvider() != null) {
            throw new IllegalArgumentException("Wrong Approach");
        }
    }

    private User findUserByEmail(String email) {
        return userRepository
                .findUserByEmail(email)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));
    }

    private List<String> getRolesFromUser(User findUser) {
        return findUser.getUserRoles()
                .stream()
                .map(m -> m.getRole().getRoleType().name())
                .collect(Collectors.toList());
    }

    private void checkUser(User user, User findUser) {
        userStatusVerification(findUser);
        userProviderVerification(findUser);
        passwordVerification(user, findUser);
    }

    private void passwordVerification(User user, User findUser) {
        if ( !findUser.passwordVerification(passwordEncoder, user.getPassword()) ) {
            throw new AuthenticationCredentialsNotFoundException("Invalid input value");
        }
    }

    private void duplicateEmailCheck(User user) {
        if (userRepository.findUserByEmail(user.getEmail()).isEmpty()) {
            user.passwordEncryption(passwordEncoder);
        } else {
            throw new BusinessLogicException(ExceptionCode.EXISTING_USER);
        }
    }

    private RefreshToken checkIfTokenExists(String token) {

        return refreshTokenRepository
                .findRefreshTokenByTokenValue(token)
                .orElseThrow(() -> new MalformedJwtException("유효하지 않은 토큰"));
    }

    public AuthDto.Token createTokenDto (String email, Long id, List<String> roles) {
        String accessToken = jwtTokenUtil.createAccessToken(email, id, roles);
        String refreshToken = jwtTokenUtil.createRefreshToken(email, id, roles);

        return AuthDto.Token.of(BEARER_TYPE, accessToken, refreshToken);
    }
}
