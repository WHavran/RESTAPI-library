package com.library.restapi.demo.model.dto.reservation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record ReservationViewDTO(
        int id,
        @NotBlank(message = "Title is required.")
        @Size(min = 2, max = 30, message = "Title must be between 2 and 30 characters.")
        String bookTitle,
        @NotBlank(message = "Username is required.")
        @Size(min = 2, max = 30, message = "Username must be between 2 and 30 characters.")
        String username,
        @NotNull(message = "Reservedat is required.")
        LocalDateTime reservedAt,
        @NotNull(message = "DueDate is required.")
        LocalDateTime dueDate,
        LocalDateTime returnedAt
) {
}
