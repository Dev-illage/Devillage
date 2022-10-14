package com.devillage.teamproject.repository.comment;

import com.devillage.teamproject.entity.Comment;
import com.devillage.teamproject.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentLikeRepository extends JpaRepository<CommentLike,Long> {
    Long countByCommentId(Long commentId);
    List<CommentLike> findByCommentIdAndUserIdAndPostId(Long commentId,Long userId,Long PostId);
    void deleteByCommentIdAndUserIdAndPostId(Long commentId,Long userId,Long PostId);
}