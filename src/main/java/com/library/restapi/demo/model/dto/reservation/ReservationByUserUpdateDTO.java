package com.library.restapi.demo.model.dto.reservation;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record ReservationByUserUpdateDTO(
        @NotBlank(message = "Title is required.")
        @Size(min = 2, max = 50, message = "Title must be between 2 and 50 characters.")
        String bookTitle,
        @Future
        @NotNull(message = "DueDate is required.")
        LocalDateTime dueDate
) {
}
