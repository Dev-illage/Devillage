package com.devillage.teamproject.controller.comment;

import com.devillage.teamproject.dto.AuthDto;
import com.devillage.teamproject.dto.CommentDto;
import com.devillage.teamproject.dto.DoubleResponseDto;
import com.devillage.teamproject.dto.PostDto;
import com.devillage.teamproject.security.resolver.AccessToken;
import com.devillage.teamproject.security.util.JwtConstants;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/posts")
public interface CommentController {

    @PostMapping("/{post-id}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    CommentDto.Response postComment(@Valid @RequestBody CommentDto.Post request, @PathVariable("post-id") Long postId,
                                    @RequestHeader(JwtConstants.AUTHORIZATION_HEADER) String token);

    @PostMapping("/{post-id}/comments/{comment-id}")
    @ResponseStatus(HttpStatus.CREATED)
    CommentDto.ReCommentResponse postReComment(@PathVariable("post-id") Long postId,
                                               @PathVariable("comment-id") Long commentId,
                                               @Valid @RequestBody CommentDto.ReCommentPost request,
                                               @RequestHeader(JwtConstants.AUTHORIZATION_HEADER) String token);

    @PostMapping("/{post-id}/comments/{comment-id}/like")
    @ResponseStatus(HttpStatus.OK)
    PostDto.Response.CommentLikeDto likeComment(@AccessToken AuthDto.UserInfo userInfo,@PathVariable("post-id") Long postId,
                                                @PathVariable("comment-id") Long commentId);

    @GetMapping("/{post-id}/comments/{comment-id}")
    @ResponseStatus(HttpStatus.OK)
    CommentDto.Response getComment(@PathVariable("post-id") Long postId,
                                   @PathVariable("comment-id") Long commentId,
                                   @RequestParam("page") Long page,
                                   @RequestParam("size") Long size);

    @GetMapping("/{post-id}/comments/{comment-id}/{re-comment-id}")
    @ResponseStatus(HttpStatus.OK)
    CommentDto.ReCommentResponse getReComment(@PathVariable("post-id") Long postId,
                                              @PathVariable("comment-id") Long commentId,
                                              @PathVariable("re-comment-id") Long reCommentId,
                                              @RequestParam("page") Long page,
                                              @RequestParam("size") Long size);

    @PatchMapping("/{post-id}/comments/{comment-id}")
    @ResponseStatus(HttpStatus.OK)
    CommentDto.Response patchComment(@PathVariable("post-id") Long postId,
                                     @PathVariable("comment-id") Long commentId,
                                     @Valid @RequestBody CommentDto.Patch request);

    @PatchMapping("/{post-id}/comments/{comment-id}/{re-comment-id}")
    @ResponseStatus(HttpStatus.OK)
    CommentDto.ReCommentResponse patchReComment(@PathVariable("post-id") Long postId,
                                                @PathVariable("comment-id") Long commentId,
                                                @PathVariable("re-comment-id") Long reCommentId,
                                                @Valid @RequestBody CommentDto.Patch request);

    @DeleteMapping("/{post-id}/comments/{comment-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteComment(@PathVariable("post-id") Long postId,
                       @PathVariable("comment-id") Long commentId,
                       @RequestHeader(JwtConstants.AUTHORIZATION_HEADER) String token);

    @DeleteMapping("/{post-id}/comments/{comment-id}/{re-comment-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteReComment(@PathVariable("post-id") Long postId,
                         @PathVariable("comment-id") Long commentId,
                         @PathVariable("re-comment-id") Long reCommentID);

    @GetMapping("/{post-id}/comments")
    @ResponseStatus(HttpStatus.OK)
    DoubleResponseDto getComments(@PathVariable("post-id") Long postId,
                                  @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                  @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
                                  @AccessToken AuthDto.UserInfo userInfo);
}
