package com.library.restapi.demo.model.dto.user;

import java.time.LocalDate;

public record UserListDTO(
        int id,
        String name,
        String email,
        LocalDate registeredDate,
        boolean hasActiveReservation
) {
}