package com.library.restapi.demo.service;

import com.library.restapi.demo.model.dto.user.*;
import com.library.restapi.demo.model.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService {

    User findEntityById(int theId);

    User findEntityWithDetailsById(int theId);

    User findEntityWithDetailsByUserName(String username);

    UserDetailDTO getOneUserWithDetailsById(int theId);

    UserProfileDTO getUserProfileDTO(String username);

    List<UserListDTO> findAllUsersByDTO();

    UserDetailDTO createNewUser(UserUpdateDTO inputUserDTO);

    UserDetailDTO updateUser(UserUpdateDTO inputUserDTO);

    UserProfileDTO updateUserProfileByUser(UserProfileEditDTO inputUserDTO, String username);

    UserDetailDTO patchUpdateUser(Map<String, Object> patchInput, int inUserId);

    void hardDelete(int theId);


}
