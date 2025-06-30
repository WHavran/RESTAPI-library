package com.library.restapi.demo.model.dto.location;

import java.util.List;

public record LocationDetailDTO(
        int id,
        int floor,
        String shelf,
        String section,
        List<String> bookTitles
) {
}
