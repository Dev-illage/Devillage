package com.devillage.teamproject.dto;

import com.devillage.teamproject.entity.Block;
import com.devillage.teamproject.entity.User;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
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
        private FileDto.SimpleResponse avatar;

        public static Response of(User user) {
            return Response.builder()
                    .email(user.getEmail())
                    .nickname(user.getNickName())
                    .statusMessage(user.getStatusMessage())
                    .passwordModifiedAt(user.getPwdLastModifiedAt())
                    .avatar(FileDto.SimpleResponse.of(user.getAvatar()))
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PatchProfile {
        @NotBlank
        @Pattern(regexp = "^(?=.*[a-z0-9가-힣])[a-z0-9가-힣]{2,16}$")
        private String nickName;
        @NotBlank
        private String statusMessage;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class BlockUserDto {
        @Getter
        @AllArgsConstructor
        @Builder
        public static class Response {
            private Long blockId;
            private Long srcUserId;
            private Long targetUserId;
        }

        public static Response of(Block block) {
            return Response.builder()
                    .blockId(block.getId())
                    .srcUserId(block.getSrcUser().getId())
                    .targetUserId(block.getDestUser().getId())
                    .build();
        }

        public static Response of(Long blockId, Long srcUserId, Long targetUserId) {
            return Response.builder()
                    .blockId(blockId).srcUserId(srcUserId).targetUserId(targetUserId)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    public static class AuthorInfo {
        private long authorId;
        private String authorName;

        public static AuthorInfo of(User user) {
            return AuthorInfo.builder()
                    .authorId(user.getId())
                    .authorName(user.getNickName())
                    .build();
        }
    }
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    public static class PasswordDto {
        @NotBlank
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$")
        private String password;
        @NotBlank
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$")
        private String updatePassword;

    }


}
