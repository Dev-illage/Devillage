package com.devillage.teamproject.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostDto {

    //TODO : Response DTO 임시 작성, 구현 시 주석 삭제
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Response {

        @Getter
        @AllArgsConstructor(access = AccessLevel.PROTECTED)
        public static class SimplePostDto {
            private final Long id;
            private final String title;
            private final String content;
            private final Long clicks;
            private final String category;
            private final List<TagDto.Response> tags;
            private final List<FileDto.Response> files;
            private final LocalDateTime createdAt;
            private final LocalDateTime lastModifiedAt;

            public static SimplePostDto of(com.devillage.teamproject.entity.Post post) {
                return new SimplePostDto(
                        post.getId(),
                        post.getTitle(),
                        post.getContent(),
                        post.getClicks(),
                        post.getCategory().getCategoryType().name(),
                        post.getTags().stream()
                                .map(postTag -> TagDto.Response.of(postTag.getTag()))
                                .collect(Collectors.toList()),
                        post.getPostsFile().stream()
                                .map(postsFile -> FileDto.Response.of(postsFile.getFile()))
                                .collect(Collectors.toList()),
                        post.getCreatedAt(),
                        post.getLastModifiedAt()
                );
            }
        }

        @Getter
        @AllArgsConstructor(access = AccessLevel.PROTECTED)
        public static class BookmarkDto {
            private final Long user;
            private final Long post;
            private final Long bookmark;

            public static BookmarkDto of(Long userId, Long postId, Long bookmarkId) {
                return new BookmarkDto(userId, postId, bookmarkId);
            }
        }

        @Getter
        @AllArgsConstructor(access = AccessLevel.PROTECTED)
        public static class ReportDto {
            private final Long user;
            private final Long post;
            private final Long report;

            public static ReportDto of(Long userId, Long postId, Long reportedPostId) {
                return new ReportDto(userId, postId, reportedPostId);
            }
        }

        @Getter
        @AllArgsConstructor(access = AccessLevel.PROTECTED)
        public static class LikeDto {
            private final Long user;
            private final Long post;
            private final Long like;

            public static LikeDto of(Long userId, Long postId, Long likeId) {
                return new LikeDto(userId, postId, likeId);
            }
        }

    }

    //TODO : Post DTO 임시 작성, 구현 시 주석 삭제
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Post {

    }

    //TODO : Patch DTO 임시 작성, 구현 시 주석 삭제
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Patch {

    }
}
