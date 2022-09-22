package com.devillage.teamproject.controller.post;

import com.devillage.teamproject.dto.PostDto;
import com.devillage.teamproject.dto.SingleResponseDto;
import com.devillage.teamproject.entity.Bookmark;
import com.devillage.teamproject.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public Long postReport(Long id) {
        return null;
    }

    @Override
    public Long postLike(Long id) {
        return null;
    }

    @Override
    public Long patchPost(Long id, PostDto.Patch request) {
        return null;
    }

    @Override
    public List<PostDto.Response> getPosts(String category, Long page, Long size) {
        return null;
    }

    @Override
    public void deletePost(Long id) {

    }
}
