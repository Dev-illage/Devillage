package com.devillage.teamproject.repository.comment;

import com.devillage.teamproject.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
