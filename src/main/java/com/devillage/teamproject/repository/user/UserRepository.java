package com.devillage.teamproject.repository.user;

import com.devillage.teamproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
