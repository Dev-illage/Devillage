package com.devillage.teamproject.repository.role;

import com.devillage.teamproject.entity.Role;
import com.devillage.teamproject.entity.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role getRoleByRoleType(RoleType roleType);
}
