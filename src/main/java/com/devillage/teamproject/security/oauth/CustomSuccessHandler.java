package com.devillage.teamproject.security.oauth;

import com.devillage.teamproject.entity.RefreshToken;
import com.devillage.teamproject.entity.User;
import com.devillage.teamproject.entity.enums.RoleType;
import com.devillage.teamproject.exception.BusinessLogicException;
import com.devillage.teamproject.exception.ExceptionCode;
import com.devillage.teamproject.repository.token.RefreshTokenRepository;
import com.devillage.teamproject.repository.user.UserRepository;
import com.devillage.teamproject.security.util.JwtConstants;
import com.devillage.teamproject.security.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.devillage.teamproject.security.util.JwtConstants.*;


@RequiredArgsConstructor
@Component
@Slf4j
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (response.isCommitted()) {
            log.info("duplicated commit");
            return;
        }

        OAuth2User user = (OAuth2User) authentication.getPrincipal();
        String email = user.getAttributes().get("email") == null ? makeEmail(user) : (String) user.getAttributes().get("email");

        User findUser = userRepository.findUserByEmail(email).orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));

        String accessToken = jwtTokenUtil.createAccessToken(findUser.getEmail(), findUser.getId(), List.of(RoleType.ROLE_USER.toString()));
        String refreshToken = jwtTokenUtil.createRefreshToken(findUser.getEmail(), findUser.getId(), List.of(RoleType.ROLE_USER.toString()));

        refreshTokenRepository.save(new RefreshToken(refreshToken));

        String url = getURL(accessToken, refreshToken);

        getRedirectStrategy().sendRedirect(request, response, url);
    }

    private String getURL(String accessToken, String refreshToken) {
        return UriComponentsBuilder.fromUriString(REDIRECT_URL_OAUTH2)
                .queryParam("accessToken", accessToken)
                .queryParam("refreshToken", refreshToken)
                .build()
                .toUriString();
    }

    private String makeEmail(OAuth2User user) {
        Integer id =  (Integer)user.getAttributes().get("id");
        String provider = "github";
        String url = "@github.com";

        return id+provider+url;
    }

}
