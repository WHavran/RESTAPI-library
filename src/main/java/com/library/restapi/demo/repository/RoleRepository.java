package com.library.restapi.demo.repository;

import com.library.restapi.demo.model.entity.Role;
import com.library.restapi.demo.model.helper.RoleId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, RoleId> {

    List<Role> findByIdRole(String role);
}
