package com.devillage.teamproject.dto;

import com.devillage.teamproject.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class JOIN {
        @NotEmpty
        @Pattern(regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")
        private String email;

        @NotEmpty
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$") // 영문, 특수문자 8자 이상 20자 이하
        private String password;

        @NotEmpty
        @Pattern(regexp = "^(?=.*[a-z0-9가-힣])[a-z0-9가-힣]{2,16}$") // 영문, 숫자 8자 이상 16자 이하(자음,모음만 사용 불가능)
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
}
