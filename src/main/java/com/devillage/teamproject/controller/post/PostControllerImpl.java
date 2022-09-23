package com.devillage.teamproject.controller.post;

import com.devillage.teamproject.dto.MultiResponseDto;
import com.devillage.teamproject.dto.PostDto;
import com.devillage.teamproject.dto.SingleResponseDto;
import com.devillage.teamproject.entity.Bookmark;
import com.devillage.teamproject.entity.Like;
import com.devillage.teamproject.entity.Post;
import com.devillage.teamproject.entity.ReportedPost;
import com.devillage.teamproject.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class PostControllerImpl implements PostController {

    private final PostService postService;

    @Override
    public Long postPost(PostDto.Post request) {
        return null;
    }

    @Override
    public PostDto.Response getPost(Long id) {
        return null;
    }

    @Override
    public SingleResponseDto<PostDto.Response.BookmarkDto> postBookmark(Long postId) {
        Long userId = 1L; // Security 메서드 구현 필요
        Bookmark bookmark = postService.postBookmark(postId);
        return SingleResponseDto.of(
                PostDto.Response.BookmarkDto.of(userId, postId, bookmark.getId())
        );
    }

    @Override
    public SingleResponseDto<PostDto.Response.ReportDto> postReport(Long postId) {
        Long userId = 1L; // Security 메서드 구현 필요
        ReportedPost reportedPost = postService.postReport(postId);
        return SingleResponseDto.of(
                PostDto.Response.ReportDto.of(userId, postId, reportedPost.getId())
        );
    }

    @Override
    public SingleResponseDto<PostDto.Response.LikeDto> postLike(Long postId) {
        Long userId = 1L; // Security 메서드 구현 필요
        Like like = postService.postLike(postId);
        return SingleResponseDto.of(
                PostDto.Response.LikeDto.of(userId, postId, like.getId())
        );
    }

    @Override
    public Long patchPost(Long id, PostDto.Patch request) {
        return null;
    }

    @Override
    public MultiResponseDto<PostDto.Response.SimplePostDto> getPosts(String category, int page, int size) {
        Page<Post> posts = postService.getPosts(category, page, size);
        return MultiResponseDto.of(
                posts.stream()
                        .map(PostDto.Response.SimplePostDto::of)
                        .collect(Collectors.toList()),
                posts
        );
    }

    @Override
    public void deletePost(Long id) {

    }
}
