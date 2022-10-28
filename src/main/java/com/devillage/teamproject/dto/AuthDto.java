package com.devillage.teamproject.dto;

import com.devillage.teamproject.entity.User;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;

import static com.devillage.teamproject.dto.ValidConstants.*;
import static com.devillage.teamproject.dto.ValidConstants.CAN_NOT_NULL;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class JOIN {
        @NotEmpty(message = CAN_NOT_NULL)
        @Pattern(regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", message = INVALID_EMAIL)
        private String email;

        @NotEmpty(message = CAN_NOT_NULL)
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$", message = INVALID_PASSWORD) // 소문자 + 대문자 + 특수문자 8자 이상 20자 이하
        private String password;

        @NotEmpty(message = CAN_NOT_NULL)
        @Pattern(regexp = "^(?=.*[a-z0-9가-힣])[a-z0-9가-힣].{1,16}$", message = INVALID_NICKNAME) // 영문, 숫자, 한글 2자 이상 16자 이하(공백 및 초성, 자음 불가능)
        private String nickname;

        @Builder
        public JOIN(String email, String password, String nickname) {
            this.email = email;
            this.password = password;
            this.nickname = nickname;
        }

        public User toEntity() {
            return User.builder()
                    .nickName(this.nickname)
                    .email(this.email)
                    .password(this.password)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Login {
        private String email;
        private String password;

        @Builder
        public Login(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public User toEntity() {
            return User.builder()
                    .email(email)
                    .password(password)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    public static class Token {
        private String bearer;
        private String accessToken;
        private String refreshToken;

        public static Token of(String bearer, String accessToken, String refreshToken) {
            return Token.builder()
                    .bearer(bearer)
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    public static class UserInfo {
        private String email;
        private Long id;
        private List<String> roles;
    }
}
