package com.library.restapi.demo.model.dto.book;

import java.time.LocalDate;

public record BookViewDTO(
        int id,
        String title,
        String isbn,
        String genre,
        LocalDate publishedDate,
        String authorFullName,
        String location) {}
