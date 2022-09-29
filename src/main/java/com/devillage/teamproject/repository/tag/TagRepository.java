package com.devillage.teamproject.repository.tag;

import com.devillage.teamproject.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag,Long> {
    Optional<Tag> findTagByName(String name);
}