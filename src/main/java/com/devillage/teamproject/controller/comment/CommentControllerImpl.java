package com.devillage.teamproject.controller.comment;

import com.devillage.teamproject.dto.CommentDto;
import com.devillage.teamproject.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentControllerImpl implements CommentController {

    private final CommentService commentService;

    @Override
    public CommentDto.Response postComment(CommentDto.Post request, Long postId, String token) {
        return CommentDto.Response.of(commentService.createComment(request.toEntity(postId), token));
    }

    @Override
    public CommentDto.ReCommentResponse postReComment(Long postId, Long commentId,
                                                      CommentDto.ReCommentPost request, String token) {
        return CommentDto.ReCommentResponse.of(
                commentService.createReComment(request.toEntity(postId, commentId), token));
    }

    @Override
    public Long postLike(Long postId, Long commentId) {
        return null;
    }

    @Override
    public CommentDto.Response getComment(Long postId, Long commentId, Long page, Long size) {
        return null;
    }

    @Override
    public CommentDto.ReCommentResponse getReComment(Long postId, Long commentId, Long reCommentId, Long page, Long size) {
        return null;
    }

    @Override
    public Long patchComment(Long postId, Long commentId, CommentDto.Patch request) {
        return null;
    }

    @Override
    public Long patchReComment(Long postId, Long commentId, Long reCommentId, CommentDto.ReCommentPatch request) {
        return null;
    }

    @Override
    public void deleteComment(Long postId, Long commentId, String token) {
        commentService.deleteComment(commentId, token);
    }

    @Override
    public void deleteReComment(Long postId, Long commentId, Long reCommentID) {
        commentService.deleteReComment(postId, commentId, reCommentID);
    }
}
