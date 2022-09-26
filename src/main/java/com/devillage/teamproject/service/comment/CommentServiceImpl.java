package com.devillage.teamproject.service.comment;

import com.devillage.teamproject.entity.Comment;
import com.devillage.teamproject.entity.ReComment;
import com.devillage.teamproject.exception.BusinessLogicException;
import com.devillage.teamproject.exception.ExceptionCode;
import com.devillage.teamproject.repository.comment.CommentRepository;
import com.devillage.teamproject.repository.comment.ReCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ReCommentRepository reCommentRepository;

    @Override
    public Comment createComment() {
        return null;
    }

    @Override
    public Comment findComment() {
        return null;
    }

    @Override
    public Comment editComment() {
        return null;
    }

    @Override
    public List<Comment> findComments() {
        return null;
    }

    @Override
    public void deleteComment() {

    }

    @Override
    public ReComment createReComment() {
        return null;
    }

    @Override
    public ReComment findReComment() {
        return null;
    }

    @Override
    public ReComment editReComment() {
        return null;
    }

    @Override
    public List<ReComment> findReComments() {
        return null;
    }

    @Override
    public void deleteReComment(Long postId, Long commentId, Long reCommentId) {
        Optional<ReComment> optionalReComment = reCommentRepository.findById(reCommentId);

        ReComment reComment = optionalReComment.orElseThrow(
                () -> new BusinessLogicException(ExceptionCode.RE_COMMENT_NOT_FOUND)
        );

        if (!reComment.getComment().getId().equals(commentId)
                || !reComment.getComment().getPost().getId().equals(postId)) {
            throw new BusinessLogicException(ExceptionCode.ID_DOES_NOT_MATCH);
        }

        reCommentRepository.deleteById(reCommentId);
    }
}
