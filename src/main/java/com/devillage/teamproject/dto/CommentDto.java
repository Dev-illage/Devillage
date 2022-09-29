package com.devillage.teamproject.dto;

import com.devillage.teamproject.entity.Comment;
import com.devillage.teamproject.entity.ReComment;
import lombok.*;

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

    //TODO : ReComment Post 임시 작성, 구현 시 주석 삭제
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ReCommentPost {

    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class ReCommentResponse {
        private Long reCommentId;
        private String content;
        private Long userId;
        private Long commentId;
        private Long postId;

        public static CommentDto.ReCommentResponse of(ReComment reComment) {
            return ReCommentResponse.builder()
                    .reCommentId(reComment.getId())
                    .content(reComment.getContent())
                    .userId(reComment.getUser().getId())
                    .commentId(reComment.getComment().getId())
                    .postId(reComment.getComment().getPost().getId())
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
