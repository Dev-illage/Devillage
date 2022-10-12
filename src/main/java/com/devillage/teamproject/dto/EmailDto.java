package com.devillage.teamproject.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailDto {
    @Email
    @NotBlank(message = "이메일은 공백일 수 없습니다.")
    private String email;



    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class AuthInfo{
        @Email
        @NotBlank(message = "이메일은 공백일 수 없습니다.")
        private String email;

        @NotBlank(message = "인증번호는 공백일 수 없습니다.")
        private String authKey;

    }

}
