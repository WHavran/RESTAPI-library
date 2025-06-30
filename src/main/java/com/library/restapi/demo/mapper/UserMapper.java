package com.library.restapi.demo.mapper;

import com.library.restapi.demo.model.dto.user.UserDetailDTO;
import com.library.restapi.demo.model.dto.user.UserProfileDTO;
import com.library.restapi.demo.model.dto.user.UserProfileEditDTO;
import com.library.restapi.demo.model.dto.user.UserUpdateDTO;
import com.library.restapi.demo.model.entity.Role;
import com.library.restapi.demo.model.entity.User;

import java.util.Set;

public interface UserMapper {

    UserDetailDTO mapEntityToUserDetailDTO(User entity);

    UserProfileDTO mapEntityToUserProfileDTO(User entity);

    User mapDetailDTOToEntityCreate(UserUpdateDTO newUser);

    void mapDetailDTOToEntityUpdate(UserUpdateDTO userDTO, User dbUser, Set<Role> roles);

    void mapUserProfileEditDTOToEntityUpdateByUser (UserProfileEditDTO userDTO, User dbUser);



}
