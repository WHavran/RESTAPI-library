package com.library.restapi.demo.model.dto.author;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record AuthorUpdateDTO(
        int id,
        @NotBlank(message = "Firstname is required.")
        @Size(min = 2, max = 30, message = "Firstname must be between 2 and 30 characters.")
        String firstName,
        @NotBlank(message = "Lastname is required.")
        @Size(min = 2, max = 30, message = "Lastname must be between 2 and 30 characters.")
        String lastName,
        String bio,
        @NotNull(message = "Birthday is required.")
        @Past(message = "Birthday must be in the past.")
        LocalDate birthDay,
        LocalDate deathDay,
        @NotNull(message = "Nationality is required")
        String nationality,
        @NotNull(message = "IsActive is required")
        boolean isActive) {
}
