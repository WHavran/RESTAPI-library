package com.library.restapi.demo.model.dto.book;

import com.library.restapi.demo.model.dto.reservation.ReservationForBookDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record BookUpdateDTO(
        int id,
        @NotBlank(message = "Title is required.")
        @Size(min = 2, max = 50, message = "Title must be between 2 and 50 characters.")
        String title,
        @NotBlank(message = "Isbn is required.")
        @Size(min = 13, max = 13, message = "Isbn must be 13 characters.")
        String isbn,
        @NotBlank(message = "Genre is required.")
        @Size(min = 4, max = 20, message = "Genre must be between 4 and 20 characters.")
        String genre,
        @NotNull(message = "Published date is required.")
        LocalDate publishedDate,
        @NotBlank(message = "Authors name is required.")
        @Size(min = 4, max = 50, message = "Authors name must be between 4 and 50 characters.")
        String authorFullName,
        @NotBlank(message = "Location name is required.")
        @Pattern(
                regexp = "^floor:\\[\\d+\\] shelf:\\[[A-Za-z]\\d+\\] section:\\[[\\w\\s-]+\\]$",
                message = "Location must be in format: floor:[number] shelf:[letter+number] section:[text]"
        )
        String location,
        @NotNull(message = "IsActive is required")
        boolean isActive,
        List<ReservationForBookDTO> reservations
) {

    public void addReservation(ReservationForBookDTO reservation){
        reservations.add(reservation);
    }


}
