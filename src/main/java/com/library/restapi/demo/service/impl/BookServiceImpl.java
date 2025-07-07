package com.library.restapi.demo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.restapi.demo.exceptions.EmptyViewListException;
import com.library.restapi.demo.exceptions.EntityDeletionNotAllowedException;
import com.library.restapi.demo.exceptions.EntityNotFound;
import com.library.restapi.demo.mapper.BookMapper;
import com.library.restapi.demo.model.dto.book.BookUpdateDTO;
import com.library.restapi.demo.model.dto.book.BookViewDTO;
import com.library.restapi.demo.model.entity.Author;
import com.library.restapi.demo.model.entity.Book;
import com.library.restapi.demo.model.entity.Location;
import com.library.restapi.demo.model.entity.Reservation;
import com.library.restapi.demo.repository.AuthorRepository;
import com.library.restapi.demo.repository.BookRepository;
import com.library.restapi.demo.repository.LocationRepository;
import com.library.restapi.demo.service.BookService;
import com.library.restapi.demo.service.SupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class BookServiceImpl implements BookService {

    AuthorRepository authorRepository;
    LocationRepository locationRepository;
    BookRepository bookRepository;
    BookMapper bookMapper;
    ObjectMapper objectMapper;
    SupportService supportService;

    @Autowired
    public BookServiceImpl(AuthorRepository authorRepository, LocationRepository locationRepository,
                           BookRepository bookRepository, BookMapper bookMapper, ObjectMapper objectMapper, SupportService supportService) {
        this.authorRepository = authorRepository;
        this.locationRepository = locationRepository;
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.objectMapper = objectMapper;
        this.supportService = supportService;
    }

    @Override
    public Book findEntityById(int theId) {
        return bookRepository.findById(theId)
                .orElseThrow(EntityNotFound::new);

    }

    @Override
    public Book findEntityWithDetailsById(int theId) {
        return bookRepository.findEntityWithoutReservationsById(theId)
                .orElseThrow(EntityNotFound::new);
    }

    @Override
    public BookViewDTO getOneBookViewDTOById(int theId) {
        Book dbBook = findEntityWithDetailsById(theId);
        return bookMapper.mapEntityToBookViewDTO(dbBook);
    }

    @Override
    public BookUpdateDTO getOneBookUpdateDTOById(int theId) {
        Book dbBook = findEntityWithDetailsById(theId);
        return bookMapper.mapEntityToBookUpdateDTO(dbBook);
    }

    @Override
    public List<BookViewDTO> getListBookViewDTOByActive(boolean isActive) {
        List<Book> dbEntities = bookRepository.findAllEntitiesWithoutReservationsById(isActive);
        if (dbEntities.isEmpty()){
            throw new EmptyViewListException();
        }
        return dbEntities.stream()
                .map(bookMapper::mapEntityToBookViewDTO)
                .toList();
    }

    @Override
    @Transactional
    public BookUpdateDTO createNewBook(BookUpdateDTO bookDTO) {

        Author dbAuthor = supportService.findAuthorByFullName(bookDTO.authorFullName());
        Location dbLocation = supportService.findLocationByString(bookDTO.location());

        Book dbBook = bookMapper.mapBookUpdateDTOToEntityCreate(bookDTO, dbAuthor, dbLocation);
        bookRepository.save(dbBook);

        return bookMapper.mapEntityToBookUpdateDTO(dbBook);
    }

    @Override
    @Transactional
    public BookUpdateDTO updateBook(BookUpdateDTO bookDTO) {

        Book entity = findEntityById(bookDTO.id());
        if (!bookDTO.isActive()){
            deleteValidateNoActiveReservations(entity.getReservations());
        }

        Author dbAuthor = supportService.findAuthorByFullName(bookDTO.authorFullName());
        Location dbLocation = supportService.findLocationByString(bookDTO.location());

        Book dbBook = bookMapper.mapUpdateDTOToEntityForUpdate(bookDTO,entity ,dbAuthor, dbLocation);
        bookRepository.save(dbBook);

        return bookMapper.mapEntityToBookUpdateDTO(dbBook);
    }

    @Override
    @Transactional
    public BookUpdateDTO patchUpdateBook(Map<String, Object> patchInput, int theId) {

        if (patchInput.containsKey("id")){
            throw new IllegalArgumentException("Id is not allowed in request body ");
        }

        Book dbBook = findEntityWithDetailsById(theId);
        if (Boolean.FALSE.equals(patchInput.get("isActive"))){
            deleteValidateNoActiveReservations(dbBook.getReservations());
        }

        dbBook = patchInputEntityMerge(patchInput, dbBook);
        bookRepository.save(dbBook);

        return bookMapper.mapEntityToBookUpdateDTO(dbBook);
    }

    @Override
    @Transactional
    public void softDelete(int theId) {

        Book entity = findEntityWithDetailsById(theId);
        deleteValidateNoActiveReservations(entity.getReservations());
        entity.setIsActive(false);

        bookRepository.save(entity);
    }

    private Book patchInputEntityMerge(Map<String, Object> patchInput, Book dbBook){

        if (patchInput.containsKey("authorFullName")){
            String authorsFullName = (String) patchInput.get("authorFullName");
            Author dbAuthor = supportService.findAuthorByFullName(authorsFullName);
            dbBook.setAuthor(dbAuthor);
            patchInput.remove("authorFullName");
        }

        if (patchInput.containsKey("location")){
            String locationInput = (String) patchInput.get("location");
            Location dbLocation = supportService.findLocationByString(locationInput);
            dbBook.setLocation(dbLocation);
            patchInput.remove("location");
        }

        return supportService.patchMergeEntity(patchInput, dbBook, Book.class);
    }

    private void deleteValidateNoActiveReservations(List<Reservation> reservations){

        int countOfActiveReservations = (int) reservations.stream()
                .filter(reservation -> reservation.getReturnedAt() == null)
                .count();

        if (countOfActiveReservations != 0){
            throw new EntityDeletionNotAllowedException("Delete book with active reservation is not allowed.");
        }
    }

}
