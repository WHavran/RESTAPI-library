package com.library.restapi.demo.service;

import com.library.restapi.demo.model.dto.book.BookUpdateDTO;
import com.library.restapi.demo.model.dto.book.BookViewDTO;
import com.library.restapi.demo.model.entity.Book;

import java.util.List;
import java.util.Map;

public interface BookService {

    Book findEntityById(int theId);

    Book findEntityWithDetailsById(int theId);

    BookViewDTO getOneBookViewDTOById(int theId);

    BookUpdateDTO getOneBookUpdateDTOById(int theId);

    List<BookViewDTO> getListBookViewDTOByActive(boolean isActive);

    BookUpdateDTO createNewBook(BookUpdateDTO bookDTO);

    BookUpdateDTO updateBook(BookUpdateDTO bookDTO);

    BookUpdateDTO patchUpdateBook(Map<String, Object> patchInput, int theId);

    void softDelete(int theId);
}
