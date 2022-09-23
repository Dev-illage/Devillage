package com.devillage.teamproject.repository.post;

import com.devillage.teamproject.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    List<Bookmark> findByUserIdAndPostId(Long userId, Long postId);
}
