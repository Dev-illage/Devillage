package com.devillage.teamproject.repository.user;

import com.devillage.teamproject.entity.Block;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlockRepository extends JpaRepository<Block, Long> {
    Optional<Block> findBySrcUserIdAndDestUserId(Long srcUserId, Long destUserId);
}
