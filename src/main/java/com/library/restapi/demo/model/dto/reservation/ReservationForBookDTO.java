package com.library.restapi.demo.model.dto.reservation;

import java.time.LocalDateTime;

public record ReservationForBookDTO(
        int id,
        String username,
        LocalDateTime reservedAt,
        LocalDateTime dueDate,
        LocalDateTime returnedAt
) {
}
