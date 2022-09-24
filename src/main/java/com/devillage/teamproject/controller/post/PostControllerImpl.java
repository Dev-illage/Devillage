package com.devillage.teamproject.controller.post;

import com.devillage.teamproject.dto.MultiResponseDto;
import com.devillage.teamproject.dto.PostDto;
import com.devillage.teamproject.dto.SingleResponseDto;
import com.devillage.teamproject.entity.Bookmark;
import com.devillage.teamproject.entity.Like;
import com.devillage.teamproject.entity.Post;
import com.devillage.teamproject.entity.ReportedPost;
import com.devillage.teamproject.security.util.JwtTokenUtil;
import com.devillage.teamproject.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class PostControllerImpl implements PostController {

    private final PostService postService;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public SingleResponseDto postPost(PostDto.Post request) {
        Post post = PostDto.Post.toEntity(request);
        Post savedPost = postService.savePost(post);

        return SingleResponseDto.of((PostDto.Response.of(savedPost)));
    }

    @Override
    public PostDto.Response getPost(Long id) {
        return null;
    }

    @Override
    public SingleResponseDto<PostDto.Response.BookmarkDto> postBookmark(String accessToken, Long postId) {

        Long userId = jwtTokenUtil.getUserId(accessToken);
        Bookmark bookmark = postService.postBookmark(accessToken, postId);
        return SingleResponseDto.of(
                PostDto.Response.BookmarkDto.of(userId, postId, bookmark.getId())
        );
    }

    @Override
    public SingleResponseDto<PostDto.Response.ReportDto> postReport(String accessToken, Long postId) {

        Long userId = jwtTokenUtil.getUserId(accessToken);
        ReportedPost reportedPost = postService.postReport(accessToken, postId);
        return SingleResponseDto.of(
                PostDto.Response.ReportDto.of(userId, postId, reportedPost.getId())
        );
    }

    @Override
    public SingleResponseDto<PostDto.Response.LikeDto> postLike(String accessToken, Long postId) {

        Long userId = jwtTokenUtil.getUserId(accessToken);
        Like like = postService.postLike(accessToken, postId);
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
