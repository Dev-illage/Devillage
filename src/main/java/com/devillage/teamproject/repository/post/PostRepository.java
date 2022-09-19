package com.devillage.teamproject.repository.post;

import com.devillage.teamproject.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
