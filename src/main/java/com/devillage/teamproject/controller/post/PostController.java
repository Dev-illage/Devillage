package com.devillage.teamproject.controller.post;

import com.devillage.teamproject.dto.*;
import com.devillage.teamproject.security.resolver.AccessToken;
import com.devillage.teamproject.security.util.JwtConstants;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/posts")
public interface PostController {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    PostDto.Response postPost(@AccessToken AuthDto.UserInfo userInfo, @RequestBody PostDto.Post request);

    @GetMapping("/{post-id}")
    @ResponseStatus(HttpStatus.OK)
    SingleResponseDto<PostDto.Response.PostDetail> getPost(@PathVariable("post-id") Long userId);

    @PostMapping("/{post-id}/bookmark")
    @ResponseStatus(HttpStatus.OK)
    PostDto.Response.BookmarkDto postBookmark(
            @AccessToken AuthDto.UserInfo userInfo,
            @PathVariable("post-id") Long postId);

    @PostMapping("/{post-id}/report")
    @ResponseStatus(HttpStatus.OK)
    PostDto.Response.ReportDto postReport(
            @AccessToken AuthDto.UserInfo userInfo,
            @PathVariable("post-id") Long id);

    @PostMapping("/{post-id}/like")
    @ResponseStatus(HttpStatus.OK)
    PostDto.Response.LikeDto postLike(
            @AccessToken AuthDto.UserInfo userInfo,
            @PathVariable("post-id") Long id);

    @PatchMapping("/{post-id}")
    @ResponseStatus(HttpStatus.OK)
    PostDto.Response patchPost(@AccessToken AuthDto.UserInfo userInfo,@PathVariable("post-id") Long id,
                               PostDto.Patch request);

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    DoubleResponseDto<PostDto.Response.SimplePostDto> getPostsByCategory(@RequestParam String category,
                                                                         @RequestParam int page,
                                                                         @RequestParam int size);

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    DoubleResponseDto<PostDto.Response.SimplePostDto> getPostsBySearch(@RequestParam String q,
                                                                       @RequestParam int page,
                                                                       @RequestParam int size);

    @GetMapping("/bookmark")
    @ResponseStatus(HttpStatus.OK)
    DoubleResponseDto<PostDto.Response.SimplePostDto> getPostsByBookmark(
            @AccessToken AuthDto.UserInfo userInfo,
            @RequestParam int page,
            @RequestParam int size);

    @DeleteMapping("/{post-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deletePost(@PathVariable("post-id") Long id);
}
