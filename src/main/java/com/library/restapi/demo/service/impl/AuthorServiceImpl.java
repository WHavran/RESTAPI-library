package com.library.restapi.demo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.restapi.demo.exceptions.EmptyViewListException;
import com.library.restapi.demo.exceptions.EntityDeletionNotAllowedException;
import com.library.restapi.demo.exceptions.EntityNotFound;
import com.library.restapi.demo.mapper.AuthorMapper;
import com.library.restapi.demo.model.dto.author.AuthorListDTO;
import com.library.restapi.demo.model.dto.author.AuthorUpdateDTO;
import com.library.restapi.demo.model.entity.Author;
import com.library.restapi.demo.model.entity.Book;
import com.library.restapi.demo.repository.AuthorRepository;
import com.library.restapi.demo.service.AuthorService;
import com.library.restapi.demo.service.SupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AuthorServiceImpl implements AuthorService {

    AuthorRepository authorRepository;
    AuthorMapper authorMapper;
    ObjectMapper objectMapper;
    SupportService supportService;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository, AuthorMapper authorMapper, ObjectMapper objectMapper, SupportService supportService) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
        this.objectMapper = objectMapper;
        this.supportService = supportService;
    }

    @Override
    public Author findEntityById(int theId) {
        return authorRepository.findById(theId)
                .orElseThrow(EntityNotFound::new);
    }

    @Override
    public Author findEntityWithDetailsById(int theId) {
        return authorRepository.findEntityWithDetailsById(theId)
                .orElseThrow(EntityNotFound::new);
    }

    @Override
    public AuthorListDTO getOneAuthorDTOById(int theId) {
        Author entity = findEntityWithDetailsById(theId);
        return authorMapper.mapEntityToListDTO(entity);
    }

    @Override
    public AuthorUpdateDTO getOneAuthorUpdateDTOById(int theId) {
        Author entity = findEntityWithDetailsById(theId);
        return authorMapper.mapEntityToUpdateDTO(entity);
    }

    @Override
    public List<AuthorListDTO> getListActiveAuthorsByActive(boolean isActive) {

        List<Author> listEntities = authorRepository.findAllEntitiesWithDetails(isActive);
        if (listEntities.isEmpty()){
            throw new EmptyViewListException();
        }
        return listEntities.stream()
                .map(author -> authorMapper.mapEntityToListDTO(author))
                .toList();
    }

    @Override
    public AuthorUpdateDTO createNewAuthor(AuthorUpdateDTO authorDTO) {
        Author entity = authorMapper.mapUpdateDTOToEntityCreate(authorDTO);
        authorRepository.save(entity);

        return authorMapper.mapEntityToUpdateDTO(entity);
    }

    @Override
    public AuthorUpdateDTO updateAuthor(AuthorUpdateDTO authorDTO) {

        Author authorDb = findEntityWithDetailsById(authorDTO.id());
        if (!authorDTO.isActive()){
            deleteValidateNoActiveBooks(authorDb.getBooks());
        }

        authorMapper.mapUpdateDTOToEntityUpdate(authorDTO, authorDb);
        authorRepository.save(authorDb);

        return authorMapper.mapEntityToUpdateDTO(authorDb);
    }

    @Override
    public AuthorUpdateDTO patchUpdateAuthor(Map<String, Object> patchInput, int authorId) {

        if (patchInput.containsKey("id")){
            throw new IllegalArgumentException("Id is not allowed in request body ");
        }

        Author author = findEntityWithDetailsById(authorId);

        if (Boolean.FALSE.equals(patchInput.get("isActive"))){
            deleteValidateNoActiveBooks(author.getBooks());
        }

        author = patchInputEntityMerge(patchInput, author);
        authorRepository.save(author);

        return authorMapper.mapEntityToUpdateDTO(author);
    }

    @Override
    public void softDelete(int theId) {

        Author entity = findEntityWithDetailsById(theId);
        deleteValidateNoActiveBooks(entity.getBooks());
        entity.setIsActive(false);

        authorRepository.save(entity);
    }

    private Author patchInputEntityMerge(Map<String, Object> patchInput, Author inputAuthor){

        return supportService.patchMergeEntity(patchInput, inputAuthor, Author.class);
    }

    private void deleteValidateNoActiveBooks(List<Book> authorsBooks){

        int numberOfActiveBooks = (int) authorsBooks.stream()
                .filter(Book::getIsActive)
                .count();

        if (numberOfActiveBooks != 0){
            throw new EntityDeletionNotAllowedException("Delete/archive author with active books is not allowed.");
        }
    }

}
