package com.library.restapi.demo.mapper;

import com.library.restapi.demo.model.entity.Role;
import com.library.restapi.demo.model.entity.User;

import java.util.Set;

public interface RoleMapper {

    Set<Role> mapStringInputsToRoles(Set<String> roles,  User inUser);

}
