package com.library.restapi.demo.model.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Set;

public record UserUpdateDTO(
        int id,
        @NotBlank(message = "Username is required.")
        @Size(min = 2, max = 30, message = "Username must be between 2 and 30 characters.")
        String username,
        @NotBlank(message = "Email is required.")
        @Email(message = "Invalid email format.")
        String email,
        String password,
        @NotNull(message = "Registered date is required.")
        LocalDate registeredDate,
        @NotNull(message = "Enabled is required")
        boolean enabled,
        @NotNull(message = "AccountNonExpired is required")
        boolean accountNonExpired,
        @NotNull(message = "AccountNonLocked is required")
        boolean accountNonLocked,
        @NotNull(message = "CredentialsNonExpired is required")
        boolean credentialsNonExpired,
        Set<String> roles
) {
}
