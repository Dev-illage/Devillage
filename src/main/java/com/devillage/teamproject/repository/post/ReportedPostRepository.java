package com.devillage.teamproject.repository.post;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportedPostRepository extends JpaRepository<com.devillage.teamproject.entity.ReportedPost, Long> {
    List<com.devillage.teamproject.entity.ReportedPost> findByUserIdAndPostId(Long userId, Long postId);
}
