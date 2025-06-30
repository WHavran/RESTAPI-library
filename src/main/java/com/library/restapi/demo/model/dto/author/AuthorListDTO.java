package com.library.restapi.demo.model.dto.author;

import java.util.List;

public record AuthorListDTO(
        int id,
        String fullName,
        String bio,
        String age,
        String nationality,
        List<String> booksTitles) {
}
