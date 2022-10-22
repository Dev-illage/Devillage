package com.devillage.teamproject.dto;

import com.devillage.teamproject.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class RankingDto {

    private Long ranking;
    private Long userId;
    private String nickname;
    private FileDto.SimpleResponse avatar;
    private String statusMessage;
    private Long point;
    private Long contents;
    private Long comments;

    public static RankingDto of(User user, Long ranking) {
        return RankingDto.builder()
                .ranking(ranking)
                .userId(user.getId())
                .nickname(user.getNickName())
                .avatar(FileDto.SimpleResponse.of(user.getAvatar()))
                .statusMessage(user.getStatusMessage())
                .point(user.getPoint())
                .contents(user.getPostCount())
                .comments(user.getCommentCount())
                .build();
    }

}
