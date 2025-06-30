package com.library.restapi.demo.model.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserProfileEditDTO(
        @Email(message = "Invalid email format.")
        String email,
        @NotBlank(message = "Password is required.")
        @Size(min = 6, max = 30, message = "Password must have length between 2 and 30 characters.")
        String password
) {

}
