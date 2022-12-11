package com.datn.coworkingspace.service;

import com.datn.coworkingspace.dto.MessageResponse;
import com.datn.coworkingspace.dto.RoleDTO;
import com.datn.coworkingspace.entity.Role;

import java.util.List;

public interface IRoleService {

    List<Role> findAll();

    Role findById(Long theId);

    Role findByCode(String code);

    MessageResponse createRole(RoleDTO theRoleDto);

    MessageResponse updateRole(Long theId, RoleDTO theRoleDto);

}
