package com.library.restapi.demo.mapper;

import com.library.restapi.demo.model.dto.author.AuthorListDTO;
import com.library.restapi.demo.model.dto.author.AuthorUpdateDTO;
import com.library.restapi.demo.model.entity.Author;

public interface AuthorMapper {

    AuthorListDTO mapEntityToListDTO(Author entity);

    AuthorUpdateDTO mapEntityToUpdateDTO(Author entity);

    Author mapUpdateDTOToEntityCreate(AuthorUpdateDTO updateDTO);

    void mapUpdateDTOToEntityUpdate(AuthorUpdateDTO updateDTO, Author entity);


}
