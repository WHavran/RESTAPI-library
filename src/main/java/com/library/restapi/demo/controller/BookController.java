package com.library.restapi.demo.controller;

import com.library.restapi.demo.model.dto.book.BookUpdateDTO;
import com.library.restapi.demo.model.dto.book.BookViewDTO;
import com.library.restapi.demo.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/book")
public class BookController{

    private BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookViewDTO> showOneBook(@PathVariable int id){
        BookViewDTO book = bookService.getOneBookViewDTOById(id);
        return ResponseEntity.ok(book);
    }

    @GetMapping("/update/{id}")
    public ResponseEntity<BookUpdateDTO> showOneUpdateBook(@PathVariable int id){
        BookUpdateDTO book = bookService.getOneBookUpdateDTOById(id);
        return ResponseEntity.ok(book);
    }

    @GetMapping("/all=active")
    public ResponseEntity<List<BookViewDTO>> showAllActiveBooks(){
        List<BookViewDTO> listBooks = bookService.getListBookViewDTOByActive(true);
        return ResponseEntity.ok(listBooks);
    }

    @GetMapping("/all=archived")
    public ResponseEntity<List<BookViewDTO>> showAllArchivedBooks(){
        List<BookViewDTO> listBooks = bookService.getListBookViewDTOByActive(false);
        return ResponseEntity.ok(listBooks);
    }

    @PostMapping("/create")
    public ResponseEntity<BookUpdateDTO> createNewBook(@Valid @RequestBody BookUpdateDTO bookDTO){

        BookUpdateDTO dbBook = bookService.createNewBook(bookDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(dbBook.id())
                .toUri();

        return ResponseEntity.created(location).body(dbBook);
    }

    @PutMapping("/update")
    public ResponseEntity<BookUpdateDTO> updateExistBook(@Valid @RequestBody BookUpdateDTO bookDTO){
        BookUpdateDTO book = bookService.updateBook(bookDTO);
        return ResponseEntity.ok(book);

    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<BookUpdateDTO> patchUpdateBook(@PathVariable int id,
                                                         @RequestBody Map<String, Object> patchInput){
        BookUpdateDTO bookUpdateDTO = bookService.patchUpdateBook(patchInput, id);
        return ResponseEntity.ok(bookUpdateDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> softDelete(@PathVariable int id){
        bookService.softDelete(id);
        return ResponseEntity.noContent().build();
    }

}
