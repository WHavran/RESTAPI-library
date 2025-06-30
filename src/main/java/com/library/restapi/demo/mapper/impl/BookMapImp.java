package com.library.restapi.demo.mapper.impl;

import com.library.restapi.demo.mapper.BookMapper;
import com.library.restapi.demo.mapper.ReservationMapper;
import com.library.restapi.demo.model.dto.book.BookUpdateDTO;
import com.library.restapi.demo.model.dto.book.BookViewDTO;
import com.library.restapi.demo.model.dto.reservation.ReservationForBookDTO;
import com.library.restapi.demo.model.entity.Author;
import com.library.restapi.demo.model.entity.Book;
import com.library.restapi.demo.model.entity.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookMapImp implements BookMapper {

    private final ReservationMapper reservationMapper;

    @Autowired
    public BookMapImp(ReservationMapper reservationMapper) {
        this.reservationMapper = reservationMapper;
    }

    @Override
    public BookViewDTO mapEntityToBookViewDTO(Book entity) {

        String[] authorNameAndLocation = getAuthorNameAndLocation(entity);
        String authorFullName = authorNameAndLocation[0];
        String location = authorNameAndLocation[1];

        return new BookViewDTO(
                entity.getId(),
                entity.getTitle(),
                entity.getIsbn(),
                entity.getGenre(),
                entity.getPublishedDate(),
                authorFullName,
                location
        );
    }

    @Override
    public BookUpdateDTO mapEntityToBookUpdateDTO(Book entity) {

        String[] authorNameAndLocation = getAuthorNameAndLocation(entity);
        String authorFullName = authorNameAndLocation[0];
        String location = authorNameAndLocation[1];

        List<ReservationForBookDTO> reservationForUserDTO = entity.getReservations().stream()
                .map(reservationMapper::mapEntityToReservationForBookDTO)
                .toList();

        return new BookUpdateDTO(
                entity.getId(),
                entity.getTitle(),
                entity.getIsbn(),
                entity.getGenre(),
                entity.getPublishedDate(),
                authorFullName,
                location,
                entity.getIsActive(),
                reservationForUserDTO
        );
    }

    @Override
    public Book mapBookUpdateDTOToEntityCreate(BookUpdateDTO inputDTO, Author inAuthor, Location inLocation) {

        return new Book(
                0,
                inputDTO.title(),
                inputDTO.isbn(),
                inputDTO.genre(),
                inputDTO.publishedDate(),
                inAuthor,
                inLocation,
                inputDTO.isActive()
        );

    }

    @Override
    public Book mapUpdateDTOToEntityForUpdate(BookUpdateDTO inputDTO, Book updatedBook, Author inAuthor, Location inLocation) {

        updatedBook.setTitle(inputDTO.title());
        updatedBook.setIsbn(inputDTO.isbn());
        updatedBook.setGenre(inputDTO.genre());
        updatedBook.setPublishedDate(inputDTO.publishedDate());
        updatedBook.setIsActive(inputDTO.isActive());

        if (inLocation != null){
            updatedBook.setLocation(inLocation);
        }
        if (inAuthor != null){
            updatedBook.setAuthor(inAuthor);
        }

        return updatedBook;
    }

    private String[] getAuthorNameAndLocation(Book entity){

        String authorFullName = entity.getAuthor().getFirstName() + " " + entity.getAuthor().getLastName();
        String location = "floor:[" + entity.getLocation().getFloor() +
                "] shelf:[" + entity.getLocation().getShelf() +
                "] section:[" + entity.getLocation().getSection() + "]";

        return new String[]{authorFullName, location};
    }
}
