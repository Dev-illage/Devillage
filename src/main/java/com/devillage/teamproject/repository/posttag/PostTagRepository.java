package com.devillage.teamproject.repository.posttag;

import com.devillage.teamproject.entity.Post;
import com.devillage.teamproject.entity.PostTag;
import com.devillage.teamproject.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostTagRepository extends JpaRepository<PostTag, Long> {
    Page<PostTag> findByTag(Tag tag, Pageable pageable);
    void deleteByPostId(Long postId);
}
