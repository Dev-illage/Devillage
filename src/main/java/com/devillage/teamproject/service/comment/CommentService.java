package com.devillage.teamproject.service.comment;

import com.devillage.teamproject.entity.Comment;
import com.devillage.teamproject.entity.ReComment;

import java.util.List;

public interface CommentService {
    Comment createComment(Comment comment, String token);

    Comment findComment();

    Comment editComment(Long postId, Long commentId, String content);

    List<Comment> findComments();

    void deleteComment(Long commentId, String token);

    ReComment createReComment(ReComment reComment, String token);

    ReComment findReComment();

    ReComment editReComment(Long postId, Long commentId, Long reCommentId, String content);

    List<ReComment> findReComments();

    void deleteReComment(Long postId, Long commentId, Long reCommentId);

    public Comment findVerifiedComment(Long commentId);
}
