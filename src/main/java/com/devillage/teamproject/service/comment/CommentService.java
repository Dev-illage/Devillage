package com.devillage.teamproject.service.comment;

import com.devillage.teamproject.dto.CommentDto;
import com.devillage.teamproject.entity.Comment;
import com.devillage.teamproject.entity.ReComment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommentService {
    Comment createComment();

    Comment findComment();

    Comment editComment();

    List<Comment> findComments();

    void deleteComment();

    ReComment createReComment();

    ReComment findReComment();

    ReComment editReComment();

    List<ReComment> findReComments();

    void deleteReComment();
}
