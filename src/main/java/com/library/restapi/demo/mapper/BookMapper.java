package com.library.restapi.demo.mapper;

import com.library.restapi.demo.model.dto.book.BookUpdateDTO;
import com.library.restapi.demo.model.dto.book.BookViewDTO;
import com.library.restapi.demo.model.entity.Author;
import com.library.restapi.demo.model.entity.Book;
import com.library.restapi.demo.model.entity.Location;

public interface BookMapper {

    BookViewDTO mapEntityToBookViewDTO(Book entity);

    BookUpdateDTO mapEntityToBookUpdateDTO(Book entity);

    Book mapBookUpdateDTOToEntityCreate(BookUpdateDTO inputDTO, Author author, Location location);

    Book mapUpdateDTOToEntityForUpdate(BookUpdateDTO inputDTO, Book updatedBook, Author inAuthor, Location inLocation);

}
