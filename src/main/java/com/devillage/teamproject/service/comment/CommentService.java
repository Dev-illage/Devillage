package com.devillage.teamproject.service.comment;

import com.devillage.teamproject.entity.Comment;
import com.devillage.teamproject.entity.ReComment;

import java.util.List;

public interface CommentService {
    Comment createComment(Comment comment, String token);

    Comment findComment();

    Comment editComment();

    List<Comment> findComments();

    void deleteComment();

    ReComment createReComment();

    ReComment findReComment();

    ReComment editReComment();

    List<ReComment> findReComments();

    void deleteReComment(Long postId, Long commentId, Long reCommentId);
}
