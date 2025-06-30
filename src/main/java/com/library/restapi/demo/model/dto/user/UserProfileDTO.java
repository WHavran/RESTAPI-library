package com.library.restapi.demo.model.dto.user;

import com.library.restapi.demo.model.dto.reservation.ReservationForUserDTO;

import java.util.List;

public record UserProfileDTO(
        String username,
        String email,
        List<ReservationForUserDTO> reservationList
) {
}
