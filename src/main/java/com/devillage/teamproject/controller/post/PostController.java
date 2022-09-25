package com.devillage.teamproject.controller.post;

import com.devillage.teamproject.dto.MultiResponseDto;
import com.devillage.teamproject.dto.PostDto;
import com.devillage.teamproject.dto.SingleResponseDto;
import com.devillage.teamproject.security.util.JwtConstants;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/posts")
public interface PostController {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    PostDto.Response postPost(@RequestBody PostDto.Post request);

    @GetMapping("/{post-id}")
    @ResponseStatus(HttpStatus.OK)
    MultiResponseDto<PostDto.Response.PostDetail> getPost(@PathVariable("post-id") Long id);

    @PostMapping("/{post-id}/bookmark")
    @ResponseStatus(HttpStatus.OK)
    SingleResponseDto<PostDto.Response.BookmarkDto> postBookmark(
            @RequestHeader(JwtConstants.AUTHORIZATION_HEADER) String accessToken,
            @PathVariable("post-id") Long postId);

    @PostMapping("/{post-id}/report")
    @ResponseStatus(HttpStatus.OK)
    SingleResponseDto<PostDto.Response.ReportDto> postReport(
            @RequestHeader(JwtConstants.AUTHORIZATION_HEADER) String accessToken,
            @PathVariable("post-id") Long id);

    @PostMapping("/{post-id}/like")
    @ResponseStatus(HttpStatus.OK)
    PostDto.Response.LikeDto postLike(
            @RequestHeader(JwtConstants.AUTHORIZATION_HEADER) String accessToken,
            @PathVariable("post-id") Long id);

    @PatchMapping("/{post-id}")
    @ResponseStatus(HttpStatus.OK)
    Long patchPost(@PathVariable("post-id") Long id,
                   PostDto.Patch request);

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    MultiResponseDto<PostDto.Response.SimplePostDto> getPosts(@RequestParam String category,
                                                              @RequestParam int page,
                                                              @RequestParam int size);

    @DeleteMapping("/{post-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deletePost(@PathVariable("post-id") Long id);
}
