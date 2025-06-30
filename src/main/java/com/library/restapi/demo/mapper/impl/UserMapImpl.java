package com.library.restapi.demo.mapper.impl;

import com.library.restapi.demo.exceptions.ObjectIsNull;
import com.library.restapi.demo.mapper.ReservationMapper;
import com.library.restapi.demo.mapper.UserMapper;
import com.library.restapi.demo.model.dto.reservation.ReservationForUserDTO;
import com.library.restapi.demo.model.dto.user.UserDetailDTO;
import com.library.restapi.demo.model.dto.user.UserProfileDTO;
import com.library.restapi.demo.model.dto.user.UserProfileEditDTO;
import com.library.restapi.demo.model.dto.user.UserUpdateDTO;
import com.library.restapi.demo.model.entity.Role;
import com.library.restapi.demo.model.entity.User;
import com.library.restapi.demo.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapImpl implements UserMapper {

    private final ReservationMapper reservationMapper;
    private final SecurityService securityService;

    @Autowired
    public UserMapImpl(ReservationMapper reservationMapper, SecurityService securityService) {
        this.reservationMapper = reservationMapper;
        this.securityService = securityService;
    }

    @Override
    public UserDetailDTO mapEntityToUserDetailDTO(User entity) {

        List<ReservationForUserDTO> reservationForUserDTO = entity.getReservations().stream()
                .map(reservationMapper::mapEntityToReservationForUserDTO)
                .toList();

        Set<String> roles = entity.getRoles().stream()
                .map(role -> role.getId().getRole())
                .collect(Collectors.toSet());


        return new UserDetailDTO(
                entity.getId(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getRegisteredDate(),
                entity.getIsEnabled(),
                entity.getIsAccountNonExpired(),
                entity.getIsAccountNonLocked(),
                entity.getIsAccountNonExpired(),
                reservationForUserDTO,
                roles);
    }

    @Override
    public UserProfileDTO mapEntityToUserProfileDTO(User entity) {
        List<ReservationForUserDTO> reservationForUserDTO = entity.getReservations().stream()
                .map(reservationMapper::mapEntityToReservationForUserDTO)
                .toList();

        return new UserProfileDTO(
                entity.getUsername(),
                entity.getEmail(),
                reservationForUserDTO
        );
    }

    @Override
    public User mapDetailDTOToEntityCreate(UserUpdateDTO newUser) {

        if (newUser == null) {
            throw new ObjectIsNull();
        }

        String hashedPassword = securityService.hashPasswordBcrypt(newUser.password());

        return new User(
                0,
                newUser.username(),
                newUser.email(),
                hashedPassword,
                newUser.registeredDate(),
                newUser.enabled(),
                newUser.accountNonExpired(),
                newUser.accountNonLocked(),
                newUser.credentialsNonExpired());

    }

    @Override
    public void mapDetailDTOToEntityUpdate(UserUpdateDTO userDTO, User dbUser, Set<Role> roles) {

        dbUser.setUsername(userDTO.username());
        dbUser.setEmail(userDTO.email());
        dbUser.setRegisteredDate(userDTO.registeredDate());
        dbUser.setEnabled(userDTO.enabled());
        dbUser.setAccountNonExpired(userDTO.accountNonExpired());
        dbUser.setAccountNonLocked(userDTO.accountNonLocked());
        dbUser.setCredentialsNonExpired(userDTO.credentialsNonExpired());

        dbUser.getRoles().clear();
        dbUser.getRoles().addAll(roles);

        if (userDTO.password() != null && (userDTO.password().length() > 5 && userDTO.password().length() < 50)){
            String hashedPassword = securityService.hashPasswordBcrypt(userDTO.password());
            dbUser.setPassword(hashedPassword);
        } else if (userDTO.password() != null && (50 < userDTO.password().length())) {
            throw new IllegalArgumentException("Password can have MAX 50 chars");
        }

    }

    @Override
    public void mapUserProfileEditDTOToEntityUpdateByUser(UserProfileEditDTO userDTO, User dbUser) {
        String hashedPassword = securityService.hashPasswordBcrypt(userDTO.password());
        dbUser.setEmail(userDTO.email());
        dbUser.setPassword(hashedPassword);
    }


}
