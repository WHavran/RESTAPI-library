package com.library.restapi.demo.model.dto.reservation;

import java.time.LocalDateTime;

public record ReservationForUserDTO(
        int id,
        String bookTitle,
        LocalDateTime reservedAt,
        LocalDateTime dueDate,
        LocalDateTime returnedAt
) {
}
