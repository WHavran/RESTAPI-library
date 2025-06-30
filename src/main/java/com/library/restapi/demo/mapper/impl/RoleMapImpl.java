package com.library.restapi.demo.mapper.impl;

import com.library.restapi.demo.mapper.RoleMapper;
import com.library.restapi.demo.model.entity.Role;
import com.library.restapi.demo.model.entity.User;
import com.library.restapi.demo.model.helper.UserRoleTypes;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class RoleMapImpl implements RoleMapper {

    @Override
    public Set<Role> mapStringInputsToRoles(Set<String> roles, User inUser) {

        boolean inputIsCorrect = checkIfInputRolesAreCorrect(roles);

        if (inputIsCorrect){
            Set<Role> rolesForUser = new HashSet<>();

            for (var role : roles){
                rolesForUser.add(new Role(inUser, role.toUpperCase()));
            }
            return rolesForUser;

        } else {
            throw new IllegalArgumentException("Wrong roles");
        }

    }

    private static boolean checkIfInputRolesAreCorrect(Set<String> inputRoles){

        boolean inputIsCorrect = inputRoles.stream()
                .map(String::toUpperCase)
                .allMatch(role -> {
                    try {
                        UserRoleTypes.valueOf(role);
                        return true;
                    } catch (IllegalArgumentException e){
                        return false;
                    }
                });

        return inputIsCorrect;
    }
}
