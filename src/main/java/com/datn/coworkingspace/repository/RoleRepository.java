package com.datn.coworkingspace.repository;

import com.datn.coworkingspace.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByCode(String code);

}
