package com.library.restapi.demo.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.library.restapi.demo.exceptions.EntityNotFound;
import com.library.restapi.demo.model.entity.Author;
import com.library.restapi.demo.model.entity.Book;
import com.library.restapi.demo.model.entity.Location;
import com.library.restapi.demo.model.entity.User;
import com.library.restapi.demo.repository.*;
import com.library.restapi.demo.service.SupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SupportServiceImpl implements SupportService {

    AuthorRepository authorRepository;
    BookRepository bookRepository;
    LocationRepository locationRepository;
    UserRepository userRepository;
    ReservationRepository reservationRepository;
    ObjectMapper objectMapper;

    @Autowired
    public SupportServiceImpl(AuthorRepository authorRepository, BookRepository bookRepository,
                              LocationRepository locationRepository, UserRepository userRepository,
                              ReservationRepository reservationRepository, ObjectMapper objectMapper) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.locationRepository = locationRepository;
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void checkIfBookExistByBookTitle(String bookTitle) {
        boolean exist = bookRepository.existsByTitle(bookTitle);
        if (!exist){
            throw new EntityNotFound("Book not found by bookTitle: " + bookTitle);
        }
    }

    @Override
    public void checkIfAuthorExistByUsername(String username) {
        boolean exist = userRepository.existsByUsername(username.trim());
        if (!exist){
            throw new EntityNotFound("User not found by username: " + username);
        }
    }

    @Override
    public Book findBookByTitle(String bookTitle) {
        return bookRepository.findByTitle(bookTitle)
                .orElseThrow(() -> new EntityNotFound("Book not found by bookTitle: " + bookTitle));
    }

    @Override
    public User findUserByName(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFound("User not found by bookTitle: " + username));
    }

    @Override
    public Author findAuthorByFullName(String fullNameInput) {
        String[] fullName = fullNameInput.split(" ");
        if (fullName.length != 2){
            throw new IllegalArgumentException("Wrong authors name format. Allowed format: 'Firstname Lastname'");
        }
        return authorRepository.findByFirstNameAndLastName(fullName[0], fullName[1])
                .orElseThrow(() -> new EntityNotFound("Author not found: " + fullNameInput));
    }

    @Override
    public Location findLocationByString(String input) {
        String[] parts = input.split(" ");
        Map<String, String> mapLocation = new HashMap<>();

        for (var part : parts){
            String[] keyValue = part.split(":\\[");
            String key = keyValue[0];
            String value = keyValue[1].substring(0, keyValue[1].length() - 1);
            mapLocation.put(key,value);
        }

        return locationRepository.findByFloorAndShelfAndSection(
                Integer.parseInt(mapLocation.get("floor")),
                mapLocation.get("shelf"),
                mapLocation.get("section")).orElseThrow(() -> new EntityNotFound("Location not found: " + input));
    }

    @Override
    public boolean checkIfBookHasActiveReservation(int bookId) {
        int countOfActiveReservations = reservationRepository.getCountOfActiveReservationByBookId(bookId);
        return countOfActiveReservations != 0;
    }

    @Override
    public boolean checkIfUserHasActiveReservation(int userId) {
        int countOfActiveReservations = reservationRepository.getCountOfActiveReservationByUserId(userId);
        return countOfActiveReservations != 0;
    }

    @Override
    public <T> T patchMergeEntity(Map<String, Object> patchInput, T inputEntity, Class<T> entityClass) {
        try {
            ObjectReader updater = objectMapper.readerForUpdating(inputEntity);
            return updater.readValue(objectMapper.writeValueAsString(patchInput));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to patch " + entityClass.getSimpleName(), e);
        }
    }
}
