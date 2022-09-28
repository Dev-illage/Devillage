package com.devillage.teamproject.security.oauth;

import com.devillage.teamproject.entity.File;
import com.devillage.teamproject.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class Oauth2UserManager {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;
    private String provider;

    @Builder
    public Oauth2UserManager(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String picture, String provider) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.provider = provider;
    }

    public static Oauth2UserManager of(String registrationId,
                                       String usernameAttributeName,
                                       Map<String, Object> attributes) {
        if (registrationId.equals("google")) {
            return ofGoogle(usernameAttributeName, attributes);
        } else if ( registrationId.equals("github")) {
            return ofGithub(usernameAttributeName, attributes);
        }

        throw new IllegalArgumentException();
    }

    private static Oauth2UserManager ofGoogle(String userNameAttributeName,
                                              Map<String, Object> attributes) {
        return Oauth2UserManager.builder()
                .name((String) attributes.get("name"))
                .attributes(attributes)
                .email((String) attributes.get("url"))
                .picture((String) attributes.get("picture"))
                .provider((String) attributes.get("provider"))
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static Oauth2UserManager ofGithub(String userNameAttributeName,
                                              Map<String, Object> attributes) {
        return Oauth2UserManager.builder()
                .name((String) attributes.get("login"))
                .attributes(attributes)
                .email((String) attributes.get("url"))
                .picture((String) attributes.get("avatar_url"))
                .provider("github")
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public User createUser() {
        return new User(this.email, null, this.name, this.picture, this.provider);
    }

}