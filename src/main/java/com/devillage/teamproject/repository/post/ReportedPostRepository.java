package com.devillage.teamproject.repository.post;

import com.devillage.teamproject.entity.ReportedPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReportedPostRepository extends JpaRepository<ReportedPost, Long> {
    Optional<ReportedPost> findByUserIdAndPostId(Long userId, Long postId);
}
