package com.devillage.teamproject.service.comment;

import com.devillage.teamproject.entity.Comment;
import com.devillage.teamproject.entity.ReComment;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {

    @Override
    @Transactional
    public Comment createComment() {
        return null;
    }

    @Override
    public Comment findComment() {
        return null;
    }

    @Override
    @Transactional
    public Comment editComment() {
        return null;
    }

    @Override
    public List<Comment> findComments() {
        return null;
    }

    @Override
    @Transactional
    public void deleteComment() {

    }

    @Override
    @Transactional
    public ReComment createReComment() {
        return null;
    }

    @Override
    public ReComment findReComment() {
        return null;
    }

    @Override
    @Transactional
    public ReComment editReComment() {
        return null;
    }

    @Override
    public List<ReComment> findReComments() {
        return null;
    }

    @Override
    @Transactional
    public void deleteReComment() {

    }
}
