package com.devillage.teamproject.controller.comment;

import com.devillage.teamproject.dto.CommentDto;
import com.devillage.teamproject.dto.DoubleResponseDto;
import com.devillage.teamproject.entity.Comment;
import com.devillage.teamproject.entity.ReComment;
import com.devillage.teamproject.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

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

//    @Override
//    public boolean likeComment(Long postId, Long commentId, String token) {
//        Comment comment = commentService.likeComment(postId,commentId,token);
//        Long count = comment.getLikeCount();
//        if(count==1) return true;
//        else return false;
//
//    }

    @Override
    public CommentDto.Response getComment(Long postId, Long commentId, Long page, Long size) {
        return null;
    }

    @Override
    public CommentDto.ReCommentResponse getReComment(Long postId, Long commentId, Long reCommentId, Long page, Long size) {
        return null;
    }

    @Override
    public CommentDto.Response patchComment(Long postId, Long commentId, CommentDto.Patch request) {
        Comment comment = commentService.editComment(postId, commentId, request.getContent());
        return CommentDto.Response.of(comment);
    }

    @Override
    public CommentDto.ReCommentResponse patchReComment(Long postId, Long commentId, Long reCommentId, CommentDto.Patch request) {
        ReComment reComment = commentService.editReComment(postId, commentId, reCommentId, request.getContent());
        return CommentDto.ReCommentResponse.of(reComment);
    }

    @Override
    public void deleteComment(Long postId, Long commentId, String token) {
        commentService.deleteComment(commentId, token);
    }

    @Override
    public void deleteReComment(Long postId, Long commentId, Long reCommentID) {
        commentService.deleteReComment(postId, commentId, reCommentID);
    }

    @Override
    public DoubleResponseDto getComments(Long postId, Integer page, Integer size) {
        Page<Comment> commentPage = commentService.findComments(postId, page - 1, size);
        return DoubleResponseDto.of(commentPage.stream().map(
                CommentDto.ResponseWithReComment::of
        ).collect(Collectors.toList()), commentPage);
    }
}
