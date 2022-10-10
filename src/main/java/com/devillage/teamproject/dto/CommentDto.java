package com.devillage.teamproject.dto;

import com.devillage.teamproject.entity.Comment;
import com.devillage.teamproject.entity.Post;
import com.devillage.teamproject.entity.ReComment;
import com.devillage.teamproject.entity.enums.CommentStatus;
import lombok.*;

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
        private String content;
        private List<ReCommentResponse> reComments = new ArrayList<>();
        private long likeCount;
        private LocalDateTime createdAt;
        private LocalDateTime lastModifiedAt;
        private Boolean isLiked;

        @Deprecated
        public static ResponseWithReComment of(Comment comment) {
            return comment.getCommentStatus() == CommentStatus.DELETED ?
                    ResponseWithReComment.builder()
                            .commentId(comment.getId())
                            .userId(null)
                            .content(null)
                            .reComments(comment.getReComments().stream().map(
                                    ReCommentResponse::of
                            ).collect(Collectors.toList()))
                            .createdAt(comment.getCreatedAt())
                            .lastModifiedAt(comment.getLastModifiedAt())
                            .build() :
                    ResponseWithReComment.builder()
                            .commentId(comment.getId())
                            .userId(comment.getUser().getId())
                            .content(comment.getContent())
                            .reComments(comment.getReComments().stream().map(
                                    ReCommentResponse::of
                            ).collect(Collectors.toList()))
                            .createdAt(comment.getCreatedAt())
                            .lastModifiedAt(comment.getLastModifiedAt())
                            .build();
        }

        public static ResponseWithReComment of(Comment comment, Long userId) {
            return comment.getCommentStatus() == CommentStatus.DELETED ?
                    ResponseWithReComment.builder()
                            .commentId(comment.getId())
                            .userId(null)
                            .nickname(null)
                            .content(null)
                            .reComments(comment.getReComments().stream().map(
                                    ReCommentResponse::of
                            ).collect(Collectors.toList()))
                            .likeCount(0L)
                            .createdAt(comment.getCreatedAt())
                            .lastModifiedAt(comment.getLastModifiedAt())
                            .isLiked(
                                    comment.getCommentLikes().stream().map(
                                            commentLike -> commentLike.getUser().getId()
                                    ).collect(Collectors.toList()).contains(userId)
                            )
                            .build() :
                    ResponseWithReComment.builder()
                            .commentId(comment.getId())
                            .userId(comment.getUser().getId())
                            .nickname(comment.getUser().getNickName())
                            .content(comment.getContent())
                            .reComments(comment.getReComments().stream().map(
                                    ReCommentResponse::of
                            ).collect(Collectors.toList()))
                            .likeCount(comment.getCommentLikes().size())
                            .createdAt(comment.getCreatedAt())
                            .lastModifiedAt(comment.getLastModifiedAt())
                            .isLiked(false)
                            .build();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class Post {
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
        private String content;
        private LocalDateTime createdAt;
        private LocalDateTime lastModifiedAt;
        private Boolean isLiked;

        public static ReCommentResponse of(ReComment reComment) {
            return ReCommentResponse.builder()
                    .reCommentId(reComment.getId())
                    .userId(reComment.getUser().getId())
                    .content(reComment.getContent())
                    .createdAt(reComment.getCreatedAt())
                    .lastModifiedAt(reComment.getLastModifiedAt())
                    .isLiked(false)
                    .build();
        }

        public static ReCommentResponse of(ReComment reComment, Long userId) {
            return ReCommentResponse.builder()
                    .reCommentId(reComment.getId())
                    .userId(reComment.getUser().getId())
                    .content(reComment.getContent())
                    .createdAt(reComment.getCreatedAt())
                    .lastModifiedAt(reComment.getLastModifiedAt())
                    .isLiked(
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
        String content;
    }
}
