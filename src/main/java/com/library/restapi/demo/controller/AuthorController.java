package com.library.restapi.demo.controller;

import com.library.restapi.demo.model.dto.author.AuthorListDTO;
import com.library.restapi.demo.model.dto.author.AuthorUpdateDTO;
import com.library.restapi.demo.service.AuthorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/author")
public class AuthorController {

    private AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorListDTO> showOneAuthor(@PathVariable int id){
        AuthorListDTO authorDTO = authorService.getOneAuthorDTOById(id);
        return ResponseEntity.ok(authorDTO);
    }

    @GetMapping("/update/{id}")
    public ResponseEntity<AuthorUpdateDTO> showOneAuthorUpdate(@PathVariable int id){
        AuthorUpdateDTO authorDTO = authorService.getOneAuthorUpdateDTOById(id);
        return ResponseEntity.ok(authorDTO);
    }

    @GetMapping("/all=active")
    public ResponseEntity<List<AuthorListDTO>>showAllActive(){
        List<AuthorListDTO> listAuthors = authorService.getListActiveAuthorsByActive(true);
        return ResponseEntity.ok(listAuthors);
    }
    @GetMapping("/all=archived")
    public ResponseEntity<List<AuthorListDTO>> showAllArchived(){
        List<AuthorListDTO> listAuthors = authorService.getListActiveAuthorsByActive(false);
        return ResponseEntity.ok(listAuthors);
    }

    @PostMapping("/create")
    public ResponseEntity<AuthorUpdateDTO> createNewAuthor(@Valid @RequestBody AuthorUpdateDTO authorDTO){

        AuthorUpdateDTO dbAuthor = authorService.createNewAuthor(authorDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(dbAuthor.id())
                .toUri();

        return ResponseEntity.created(location).body(dbAuthor);
    }

    @PutMapping("/update")
    public ResponseEntity<AuthorUpdateDTO> updateExistAuthor(@Valid @RequestBody AuthorUpdateDTO authorUpdateDTO){
        AuthorUpdateDTO updatedDTO = authorService.updateAuthor(authorUpdateDTO);
        return ResponseEntity.ok(updatedDTO);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<AuthorUpdateDTO> patchUpdateAuthor(@PathVariable int id,
                                                             @RequestBody Map<String, Object> patchInput){

        AuthorUpdateDTO authorListDTO= authorService.patchUpdateAuthor(patchInput, id);
        return ResponseEntity.ok(authorListDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> softDelete(@PathVariable int id){
        authorService.softDelete(id);
        return ResponseEntity.noContent().build();
    }

}
