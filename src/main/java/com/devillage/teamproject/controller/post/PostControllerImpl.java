package com.devillage.teamproject.controller.post;

import com.devillage.teamproject.dto.*;
import com.devillage.teamproject.entity.Bookmark;
import com.devillage.teamproject.entity.Comment;
import com.devillage.teamproject.entity.Post;
import com.devillage.teamproject.entity.ReportedPost;
import com.devillage.teamproject.security.resolver.AccessToken;
import com.devillage.teamproject.service.comment.CommentService;
import com.devillage.teamproject.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class PostControllerImpl implements PostController {

    private final PostService postService;
    private final CommentService commentService;

    @Override
    public PostDto.Response postPost(@AccessToken AuthDto.UserInfo userInfo, PostDto.Post request) {
        Post savedPost = postService.savePost(request.toEntity(), request.getCategory(), request.getTags(), userInfo.getId());
        return PostDto.Response.of(savedPost);
    }

    @Override
    public SingleResponseDto<PostDto.Response.PostDetail> getPost(AuthDto.UserInfo userInfo, Long postId) {
        Post post = postService.getPost(postId);
        Page<Comment> commentPage = commentService.findComments(postId, 0, 10);
        return SingleResponseDto.of(PostDto.Response.PostDetail.of(post, commentPage,
                userInfo == null ? (Long) 0L : userInfo.getId()));
    }

    @Override
    public PostDto.Response patchPost(AuthDto.UserInfo userInfo, Long postId, PostDto.Patch request) {
        Post updatedPost = postService.editPost(request.toEntity(),request.getCategory(),request.getTags(),userInfo.getId(),postId);
        return PostDto.Response.of(updatedPost);
    }

    @Override
    public PostDto.Response.BookmarkDto postBookmark(AuthDto.UserInfo userInfo, Long postId) {

        Bookmark bookmark = postService.postBookmark(userInfo.getId(), postId);
        return PostDto.Response.BookmarkDto.of(
                userInfo.getId(),
                bookmark.getPost().getId(),
                bookmark.getId());
    }

    @Override
    public PostDto.Response.ReportDto postReport(AuthDto.UserInfo userInfo, Long postId, ReportDto reportDto) {

        ReportedPost reportedPost = postService.postReport(
                userInfo.getId(), postId, reportDto.getReportType(), reportDto.getContent());
        return PostDto.Response.ReportDto.of(
                userInfo.getId(),
                reportedPost.getPost().getId(),
                reportedPost.getId());
    }

    @Override
    public PostDto.Response.LikeDto postLike(AuthDto.UserInfo userInfo, Long postId) {

        Post post = postService.postLike(userInfo.getId(), postId);
        return PostDto.Response.LikeDto.of(
                userInfo.getId(),
                post.getId(),
                post.getLikeCount());
    }

    @Override
    public DoubleResponseDto<PostDto.Response.SimplePostDto> getPostsByCategory(String category, int page, int size) {
        Page<Post> posts = postService.getPostsByCategory(category, page, size);
        return DoubleResponseDto.of(
                posts.stream()
                        .map(PostDto.Response.SimplePostDto::of)
                        .collect(Collectors.toList()),
                posts
        );
    }

    @Override
    public DoubleResponseDto<PostDto.Response.SimplePostDto> getPostsBySearch(String q, int page, int size) {
        Page<Post> posts = postService.getPostsBySearch(q, page, size);
        return DoubleResponseDto.of(
                posts.stream()
                        .map(PostDto.Response.SimplePostDto::of)
                        .collect(Collectors.toList()),
                posts
        );
    }

    @Override
    public DoubleResponseDto<PostDto.Response.SimplePostDto> getPostsByTag(String q, int page, int size) {
        Page<Post> posts = postService.getPostsByTag(q, page, size);
        return DoubleResponseDto.of(
                posts.stream()
                        .map(PostDto.Response.SimplePostDto::of)
                        .collect(Collectors.toList()),
                posts
        );
    }

    @Override
    public DoubleResponseDto<PostDto.Response.SimplePostDto> getPostsByBookmark(AuthDto.UserInfo userInfo, int page, int size) {
        Page<Post> posts = postService.getPostsByBookmark(userInfo.getId(), page, size);
        return DoubleResponseDto.of(
                posts.stream()
                        .map(PostDto.Response.SimplePostDto::of)
                        .collect(Collectors.toList()),
                posts
        );
    }

    @Override
    public void deletePost(AuthDto.UserInfo userInfo,Long postId) {
        postService.deletePost(postId);
    }
}
