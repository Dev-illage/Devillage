package com.devillage.teamproject.controller.comment;

import com.devillage.teamproject.dto.CommentDto;
import com.devillage.teamproject.security.util.JwtConstants;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/posts")
public interface CommentController {

    @PostMapping("/{post-id}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    CommentDto.Response postComment(CommentDto.Post request, @PathVariable("post-id") Long postId,
                                    @RequestHeader(JwtConstants.AUTHORIZATION_HEADER) String token);

    @PostMapping("/{post-id}/comments/{comment-id}")
    @ResponseStatus(HttpStatus.CREATED)
    CommentDto.ReCommentResponse postReComment(@PathVariable("post-id") Long postId,
                                               @PathVariable("comment-id") Long commentId,
                                               @RequestBody CommentDto.ReCommentPost request,
                                               @RequestHeader(JwtConstants.AUTHORIZATION_HEADER) String token);

    @PostMapping("/{post-id}/comments/{comment-id}/like")
    @ResponseStatus(HttpStatus.OK)
    Long postLike(@PathVariable("post-id") Long postId,
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
                                     @RequestBody CommentDto.Patch request);

    @PatchMapping("/{post-id}/comments/{comment-id}/{re-comment-id}")
    @ResponseStatus(HttpStatus.OK)
    CommentDto.ReCommentResponse patchReComment(@PathVariable("post-id") Long postId,
                                                @PathVariable("comment-id") Long commentId,
                                                @PathVariable("re-comment-id") Long reCommentId,
                                                @RequestBody CommentDto.Patch request);

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
}
