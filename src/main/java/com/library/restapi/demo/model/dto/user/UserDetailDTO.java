package com.library.restapi.demo.model.dto.user;

import com.library.restapi.demo.model.dto.reservation.ReservationForUserDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public record UserDetailDTO(
        int id,
        String username,
        String email,
        LocalDate registeredDate,
        boolean enabled,
        boolean accountNonExpired,
        boolean accountNonLocked,
        boolean credentialsNonExpired,
        List<ReservationForUserDTO> reservationList,
        Set<String> roles
) {
}
