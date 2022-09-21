package com.devillage.teamproject.dto;

import com.devillage.teamproject.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDto {

    //TODO : Response 임시 작성, 구현 시 주석 삭제
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    public static class Response {
        private String email;
        private String nickname;
        private String statusMessage;
        private LocalDateTime passwordModifiedAt;

        public static Response of(User user) {
            return Response.builder()
                    .email(user.getEmail())
                    .nickname(user.getNickName())
                    .statusMessage(user.getStatusMessage())
                    .passwordModifiedAt(user.getPwdLastModifiedAt())
                    .build();
        }
    }

    //TODO : PatchProfile 임시 작성, 구현 시 주석 삭제
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PatchProfile {

    }
}
