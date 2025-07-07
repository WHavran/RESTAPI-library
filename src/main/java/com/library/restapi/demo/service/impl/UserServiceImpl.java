package com.library.restapi.demo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.restapi.demo.exceptions.EmptyViewListException;
import com.library.restapi.demo.exceptions.EntityDeletionNotAllowedException;
import com.library.restapi.demo.exceptions.EntityNotFound;
import com.library.restapi.demo.mapper.RoleMapper;
import com.library.restapi.demo.mapper.UserMapper;
import com.library.restapi.demo.model.dto.user.*;
import com.library.restapi.demo.model.entity.Reservation;
import com.library.restapi.demo.model.entity.Role;
import com.library.restapi.demo.model.entity.User;
import com.library.restapi.demo.repository.UserRepository;
import com.library.restapi.demo.service.SecurityService;
import com.library.restapi.demo.service.SupportService;
import com.library.restapi.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    UserMapper userMapper;
    RoleMapper roleMapper;
    ObjectMapper objectMapper;
    SecurityService securityService;
    SupportService supportService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper,
                           RoleMapper roleMapper, ObjectMapper objectMapper, SecurityService securityService,
                           SupportService supportService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.objectMapper = objectMapper;
        this.securityService = securityService;
        this.supportService = supportService;
    }

    @Override
    public User findEntityById(int theId) {
        return userRepository.findById(theId)
                .orElseThrow(EntityNotFound::new);
    }

    @Override
    public User findEntityWithDetailsById(int theId) {
        return userRepository.findEntityWithAllDetails(theId)
                .orElseThrow(EntityNotFound::new);
    }

    @Override
    public User findEntityWithDetailsByUserName(String username) {
        return userRepository.findEntityWithAllDetailsByUsername(username)
                .orElseThrow(EntityNotFound::new);
    }

    @Override
    public UserDetailDTO getOneUserWithDetailsById(int theId) {
        User entity = findEntityWithDetailsById(theId);
        return userMapper.mapEntityToUserDetailDTO(entity);

    }

    @Override
    public UserProfileDTO getUserProfileDTO(String username) {
        User entity = findEntityWithDetailsByUserName(username);
        return userMapper.mapEntityToUserProfileDTO(entity);
    }

    @Override
    public List<UserListDTO> findAllUsersByDTO() {
        List<UserListDTO> userList = userRepository.findAllUserListDTO();
        if (userList.isEmpty()){
            throw new EmptyViewListException();
        }
        return userList;
    }

    @Override
    @Transactional
    public UserDetailDTO createNewUser(UserUpdateDTO inputUserDTO) {

        User newUser = userMapper.mapDetailDTOToEntityCreate(inputUserDTO);
        userRepository.save(newUser);

        Set<Role> rolesForUser = roleMapper.mapStringInputsToRoles(inputUserDTO.roles(), newUser);

        newUser.getRoles().clear();
        newUser.getRoles().addAll(rolesForUser);
        userRepository.save(newUser);

        return userMapper.mapEntityToUserDetailDTO(newUser);
    }

    @Override
    @Transactional
    public UserDetailDTO updateUser(UserUpdateDTO inputUserDTO) {

        User dbUser = findEntityWithDetailsById(inputUserDTO.id());
        if (!inputUserDTO.enabled()){
            deleteValidateNoActiveReservations(dbUser.getReservations());
        }

        Set<Role> roles = roleMapper.mapStringInputsToRoles(inputUserDTO.roles(), dbUser);

        userMapper.mapDetailDTOToEntityUpdate(inputUserDTO, dbUser, roles);
        userRepository.save(dbUser);

        return userMapper.mapEntityToUserDetailDTO(dbUser);
    }

    @Override
    @Transactional
    public UserProfileDTO updateUserProfileByUser(UserProfileEditDTO inputUserDTO, String username) {

        User entity = findEntityWithDetailsByUserName(username);
        userMapper.mapUserProfileEditDTOToEntityUpdateByUser(inputUserDTO, entity);

        userRepository.save(entity);

        return userMapper.mapEntityToUserProfileDTO(entity);
    }

    @Override
    @Transactional
    public UserDetailDTO patchUpdateUser(Map<String, Object> patchInput, int inUserId) {

        if (patchInput.containsKey("id")){
            throw new IllegalArgumentException("Id is not allowed in request body ");
        }

        User dbUser = findEntityWithDetailsById(inUserId);
        if (Boolean.FALSE.equals(patchInput.get("enabled"))){
            deleteValidateNoActiveReservations(dbUser.getReservations());
        }

        dbUser = patchInputEntityMerge(patchInput, dbUser);
        userRepository.save(dbUser);

        return userMapper.mapEntityToUserDetailDTO(dbUser);
    }

    @Override
    @Transactional
    public void hardDelete(int theId) {
        User entity = findEntityWithDetailsById(theId);
        deleteValidateNoActiveReservations(entity.getReservations());
        userRepository.delete(entity);

    }

    private User patchInputEntityMerge(Map<String, Object> patchInput, User inputUser){

        if (patchInput.containsKey("password")){
            if (patchInput.get("password").toString().length() > 5 && patchInput.get("password").toString().length() < 50){
                String hashedPassword = securityService.hashPasswordBcrypt(patchInput.get("password").toString());
                patchInput.put("password", hashedPassword);

            } else if (50 < patchInput.get("password").toString().length() || patchInput.get("password").toString().length() < 5) {
                throw new IllegalArgumentException("New password allowed length between 5-50 chars");
            }
        }

        if (patchInput.containsKey("roles")){
            Object rolesObject = patchInput.get("roles");
            Set<String> transformRolesList = null;

            if (rolesObject instanceof List<?> tempList) {
                boolean allStrings = tempList.stream()
                        .allMatch(item -> item instanceof String);
                if (allStrings) {
                    transformRolesList = tempList.stream()
                            .map(item -> (String) item)
                            .collect(Collectors.toSet());
                } else {
                    throw new IllegalArgumentException("Wrong roles");
                }
            } else {
                throw new IllegalArgumentException("Wrong roles");
            }

            Set<Role> roles = roleMapper.mapStringInputsToRoles(transformRolesList, inputUser);
            inputUser.getRoles().clear();
            inputUser.getRoles().addAll(roles);

            patchInput.remove("roles");
        }

        return supportService.patchMergeEntity(patchInput, inputUser, User.class);
    }

    private void deleteValidateNoActiveReservations(List<Reservation> reservations){

        int countOfActiveReservations = (int) reservations.stream()
                .filter(reservation -> reservation.getReturnedAt() == null)
                .count();

        if (countOfActiveReservations != 0){
            throw new EntityDeletionNotAllowedException("Delete user with active reservation is not allowed.");
        }
    }
}
