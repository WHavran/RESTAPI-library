package com.library.restapi.demo.model.dto.location;

public record LocationListDTO(
        int id,
        int floor,
        String shelf,
        String section,
        int storedBooks
) {
}
