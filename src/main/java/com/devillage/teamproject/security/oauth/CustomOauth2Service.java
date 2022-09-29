package com.devillage.teamproject.security.oauth;

import com.devillage.teamproject.entity.User;
import com.devillage.teamproject.entity.enums.RoleType;
import com.devillage.teamproject.repository.user.UserRepository;
import com.devillage.teamproject.security.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Collections;

@RequiredArgsConstructor
@Component
public class CustomOauth2Service implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest
                .getClientRegistration()
                .getRegistrationId();

        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        Oauth2UserManager oauth2UserManager = Oauth2UserManager.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
        User user = oauth2UserManager.createUser();

        if ( userRepository.findUserByEmail(user.getEmail()).isEmpty() )
        { userRepository.save(user); }

        DefaultOAuth2User defaultOAuth2User = new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(RoleType.ROLE_USER.toString()))
                , oAuth2User.getAttributes(), userNameAttributeName);

        return defaultOAuth2User;
    }
}