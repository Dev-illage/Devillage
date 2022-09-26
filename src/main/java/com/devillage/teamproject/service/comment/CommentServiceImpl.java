package com.devillage.teamproject.service.comment;

import com.devillage.teamproject.entity.Comment;
import com.devillage.teamproject.entity.Post;
import com.devillage.teamproject.entity.ReComment;
import com.devillage.teamproject.entity.User;
import com.devillage.teamproject.exception.BusinessLogicException;
import com.devillage.teamproject.exception.ExceptionCode;
import com.devillage.teamproject.repository.comment.CommentRepository;
import com.devillage.teamproject.repository.comment.ReCommentRepository;
import com.devillage.teamproject.security.util.JwtTokenUtil;
import com.devillage.teamproject.service.post.PostService;
import com.devillage.teamproject.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ReCommentRepository reCommentRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final PostService postService;
    private final UserService userService;

    @Override
    @Transactional
    public Comment createComment(Comment comment, String token) {
        User user = userService.findVerifiedUser(jwtTokenUtil.getUserId(token));
        Post post = postService.getPost(comment.getPost().getId());
        return commentRepository.save(Comment.createComment(comment, user, post));
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
