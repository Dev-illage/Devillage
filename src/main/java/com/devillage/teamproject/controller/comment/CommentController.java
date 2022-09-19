package com.devillage.teamproject.controller.comment;

import com.devillage.teamproject.dto.CommentDto;
import com.devillage.teamproject.dto.PostDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController("posts")
public interface CommentController {

    @PostMapping("/{post-id}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    Long postComment(CommentDto.Post request);

    @PostMapping("/{post-id}/comments/{comment-id}")
    @ResponseStatus(HttpStatus.CREATED)
    Long postReComment(@PathVariable("post-id") Long postId,
                       @PathVariable("comment-id") Long commentId,
                       CommentDto.ReCommentPost request);

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
    Long patchComment(@PathVariable("post-id") Long postId,
                      @PathVariable("comment-id") Long commentId,
                      CommentDto.Patch request);

    @PatchMapping("/{post-id}/comments/{comment-id}/{re-comment-id}")
    @ResponseStatus(HttpStatus.OK)
    Long patchReComment(@PathVariable("post-id") Long postId,
                        @PathVariable("comment-id") Long commentId,
                      @PathVariable("re-comment-id") Long reCommentId,
                      CommentDto.ReCommentPatch request);

    @DeleteMapping("/{post-id}/comments/{comment-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteComment(@PathVariable("post-id") Long postId,
                       @PathVariable("comment-id") Long id);

    @DeleteMapping("/{post-id}/comments/{comment-id}/{re-comment-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteReComment(@PathVariable("post-id") Long postId,
                         @PathVariable("comment-id") Long commentId,
                         @PathVariable("re-comment-id") Long reCommentID);
}
