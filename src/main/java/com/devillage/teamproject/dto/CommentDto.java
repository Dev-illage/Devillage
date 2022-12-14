package com.devillage.teamproject.dto;

import com.devillage.teamproject.entity.Comment;
import com.devillage.teamproject.entity.Post;
import com.devillage.teamproject.entity.ReComment;
import com.devillage.teamproject.entity.enums.CommentStatus;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long commentId;
        private String content;
        private Long userId;
        private Long postId;

        public static Response of(Comment comment) {
            return Response.builder()
                    .commentId(comment.getId())
                    .content(comment.getContent())
                    .userId(comment.getUser().getId())
                    .postId(comment.getPost().getId())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class ResponseWithReComment {
        private Long commentId;
        private Long userId;
        private String nickname;
        private FileDto.SimpleResponse avatar;
        private String content;
        private long likeCount;
        private LocalDateTime createdAt;
        private LocalDateTime lastModifiedAt;
        private Boolean isLiked;
        private List<ReCommentResponse> reComments = new ArrayList<>();

        @Deprecated
        public static ResponseWithReComment of(Comment comment) {
            return of(comment, 0L);
        }

        public static ResponseWithReComment of(Comment comment, Long userId) {
            return comment.getCommentStatus() == CommentStatus.DELETED ?
                    ResponseWithReComment.builder()
                            .commentId(comment.getId())
                            .userId(null)
                            .nickname(null)
                            .avatar(null)
                            .content(null)
                            .reComments(comment.getReComments().stream().map(
                                    ReCommentResponse::of
                            ).collect(Collectors.toList()))
                            .likeCount(0L)
                            .createdAt(comment.getCreatedAt())
                            .lastModifiedAt(comment.getLastModifiedAt())
                            .isLiked(false)
                            .build() :
                    ResponseWithReComment.builder()
                            .commentId(comment.getId())
                            .userId(comment.getUser().getId())
                            .nickname(comment.getUser().getNickName())
                            .avatar(FileDto.SimpleResponse.of(comment.getUser().getAvatar()))
                            .content(comment.getContent())
                            .reComments(comment.getReComments().stream().map(
                                    ReCommentResponse::of
                            ).collect(Collectors.toList()))
                            .likeCount(comment.getCommentLikes().size())
                            .createdAt(comment.getCreatedAt())
                            .lastModifiedAt(comment.getLastModifiedAt())
                            .isLiked(
                                    comment.getCommentLikes().stream().map(
                                            commentLike -> commentLike.getUser().getId()
                                    ).collect(Collectors.toList()).contains(userId)
                            )
                            .build();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class Post {
        @NotBlank
        private String content;

        public Comment toEntity() {
            return Comment.builder()
                    .content(this.content)
                    .build();
        }

        public Comment toEntity(Long postId) {
            return Comment.builder()
                    .content(this.content)
                    .post(com.devillage.teamproject.entity.Post.builder().id(postId).build())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class ReCommentPost {
        @NotBlank
        private String content;

        public ReComment toEntity(Long postId, Long commentId) {
            return ReComment.builder()
                    .comment(Comment.builder()
                            .id(commentId)
                            .post(com.devillage.teamproject.entity.Post.builder()
                                    .id(postId).build())
                            .build())
                    .content(this.content)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class ReCommentResponse {
        private Long reCommentId;
        private Long userId;
        private String nickname;
        private FileDto.SimpleResponse avatar;
        private Long parentCommentId;
        private String content;
        private long likeCount;
        private LocalDateTime createdAt;
        private LocalDateTime lastModifiedAt;
        private Boolean isLiked;

        public static ReCommentResponse of(ReComment reComment) {
            return of(reComment, null);
        }

        public static ReCommentResponse of(ReComment reComment, Long userId) {
            return ReCommentResponse.builder()
                    .reCommentId(reComment.getId())
                    .userId(reComment.getUser().getId())
                    .avatar(FileDto.SimpleResponse.of(reComment.getUser().getAvatar()))
                    .parentCommentId(reComment.getComment().getId())
                    .nickname(reComment.getUser().getNickName())
                    .content(reComment.getContent())
                    .createdAt(reComment.getCreatedAt())
                    .lastModifiedAt(reComment.getLastModifiedAt())
                    .isLiked(userId == null ? false :
                            reComment.getReCommentLikes().stream().map(
                                    reCommentLike -> reCommentLike.getUser().getId()
                            ).collect(Collectors.toList()).contains(userId)
                    )
                    .build();
        }

    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class Patch {
        @NotBlank
        String content;
    }
}
