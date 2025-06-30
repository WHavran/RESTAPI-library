package com.library.restapi.demo.service;

import com.library.restapi.demo.model.dto.author.AuthorListDTO;
import com.library.restapi.demo.model.dto.author.AuthorUpdateDTO;
import com.library.restapi.demo.model.entity.Author;

import java.util.List;
import java.util.Map;

public interface AuthorService {

    Author findEntityById(int theId);

    Author findEntityWithDetailsById(int theId);

    AuthorListDTO getOneAuthorDTOById(int theId);

    AuthorUpdateDTO getOneAuthorUpdateDTOById(int theId);

    List<AuthorListDTO> getListActiveAuthorsByActive(boolean isActive);

    AuthorUpdateDTO createNewAuthor(AuthorUpdateDTO authorDTO);

    AuthorUpdateDTO updateAuthor(AuthorUpdateDTO authorDTO);

    AuthorUpdateDTO patchUpdateAuthor(Map<String, Object> patchInput, int authorId);

    void softDelete(int theId);


}
