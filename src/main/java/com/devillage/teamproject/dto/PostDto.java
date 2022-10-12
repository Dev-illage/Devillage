package com.devillage.teamproject.dto;

import com.devillage.teamproject.entity.Comment;
import com.devillage.teamproject.entity.enums.CategoryType;
import lombok.*;
import org.springframework.data.domain.Page;

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
        private Long postId;
//        private String category;
//        private String title;
//        private List<TagDto.Response> tag;
//        private String content;

        public static Response of(com.devillage.teamproject.entity.Post post){
            return Response.builder()
                    .postId(post.getId())
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
            private Long likeCount;
            private boolean isLiked;
            private boolean isBookmarked;
            private DoubleResponseDto<CommentDto.ResponseWithReComment> comments;

            public static PostDetail of(com.devillage.teamproject.entity.Post post, Page<Comment> commentPage,
                                        Long userId){
                return PostDetail.builder()
                        .key(post.getId())
                        .title(post.getTitle())
                        .category(post.getCategory().getCategoryType().name())
                        .createdAt(post.getCreatedAt())
                        .content(post.getContent())
                        .isModified(post.getLastModifiedAt().isAfter(post.getCreatedAt()))
                        .clicks(post.getClicks())
                        .tag(post.getTags().stream()
                                .map(postTag -> TagDto.Response.of(postTag.getTag()))
                                .collect(Collectors.toList()))
                        .author(UserDto.AuthorInfo.of(post.getUser()))
                        .likeCount(post.getLikeCount())
                        .isLiked(post.getLikes().stream().map(
                                like -> like.getUser().getId()
                        ).collect(Collectors.toList()).contains(userId))
                        .isBookmarked(post.getBookmarks().stream().map(
                                bookmark -> bookmark.getUser().getId()
                        ).collect(Collectors.toList()).contains(userId))
                        .comments(DoubleResponseDto.of(commentPage.stream().map(
                                comment -> CommentDto.ResponseWithReComment.of(comment, userId)
                        ).collect(Collectors.toList()), commentPage))
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
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class Post {
        private Long postId;
        private CategoryType category;
        private String title;
        private List<String> tags;
        private String content;

        public com.devillage.teamproject.entity.Post toEntity() {
            com.devillage.teamproject.entity.Post post = new com.devillage.teamproject.entity.Post(
                    this.postId,
                    this.title,
                    this.content
            );
            return post;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class Patch {
        private Long postId;
        private CategoryType category;
        private String title;
        private List<String> tags;
        private String content;

        public com.devillage.teamproject.entity.Post toEntity() {
            com.devillage.teamproject.entity.Post post = new com.devillage.teamproject.entity.Post(
                    this.postId,
                    this.title,
                    this.content
            );
            return post;
        }
//        public static Response of(com.devillage.teamproject.entity.Post post){
//            return Response.builder()
//                    .postId(post.getId())
//                    .title(post.getTitle())
//                    .content(post.getContent())
//                    .build();
//        }

    }
}
