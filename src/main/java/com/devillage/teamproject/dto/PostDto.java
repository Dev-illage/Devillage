package com.devillage.teamproject.dto;

import com.devillage.teamproject.entity.*;
import com.devillage.teamproject.entity.enums.CategoryType;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostDto {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Response {
        private CategoryType category;
        private String title;
        private List<TagDto.Response> tags;
        private String content;

        public static Response of(com.devillage.teamproject.entity.Post post){
            return Response.builder()
                    .category(post.getCategory().getCategoryType())
                    .title(post.getTitle())
                    .tags(post.getTags().stream()
                            .map(tag -> TagDto.Response.of(tag.getTag()))
                            .collect(Collectors.toList()))
                    .content(post.getContent())
                    .build();
        }

        @Getter
        @Builder
        @AllArgsConstructor
        public static class PostDetail{
            private Long key;
            private String title;
            private String category;
            private LocalDateTime createdAt;
            private String content;
            private boolean isModified;
            private Long clicks;
            private List<TagDto.Response> tag;
            private UserDto.AuthorInfo author;
            private List<Comment> commentList;

            public static PostDetail of(com.devillage.teamproject.entity.Post post){
                return PostDetail.builder()
                        .key(post.getId())
                        .title(post.getTitle())
//                        .category(post.getCategory().getCategoryType().name())
                        .clicks(post.getClicks())
                        .createdAt(post.getCreatedAt())
//                        .isModified(post.getLastModifiedAt().isAfter(post.getCreatedAt()))
                        .content(post.getContent())
//                        .tag(post.getTags().stream()
//                                .map(tag -> TagDto.Response.of(tag.getTag()))
//                                .collect(Collectors.toList()))
//                        .author(UserDto.AuthorInfo.of(post.getUser()))
//                        .commentList(post.getComments())
                        .build();
            }

        }


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


    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Post {
        private CategoryType category;
        private String title;
        private List<String> tags;
        private String content;

        public com.devillage.teamproject.entity.Post toEntity() {
            com.devillage.teamproject.entity.Post post = new com.devillage.teamproject.entity.Post(
                    this.title,
                    this.content
            );
            return post;
        }


    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Patch {
        private CategoryType category;
        private String title;
        private List<String> tags;
        private String content;

        public com.devillage.teamproject.entity.Post toEntity() {
            com.devillage.teamproject.entity.Post post = new com.devillage.teamproject.entity.Post(
                    this.title,
                    this.content
            );
            return post;
        }

    }
}