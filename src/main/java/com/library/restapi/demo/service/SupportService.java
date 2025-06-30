package com.library.restapi.demo.service;

import com.library.restapi.demo.model.entity.Author;
import com.library.restapi.demo.model.entity.Book;
import com.library.restapi.demo.model.entity.Location;
import com.library.restapi.demo.model.entity.User;

import java.util.Map;

public interface SupportService {

    void checkIfBookExistByBookTitle(String bookTitle);

    void checkIfAuthorExistByUsername(String username);

    Book findBookByTitle(String bookTitle);

    User findUserByName(String username);

    Author findAuthorByFullName(String fullNameInput);

    Location findLocationByString(String input);

    boolean checkIfBookHasActiveReservation(int bookId);

    boolean checkIfUserHasActiveReservation(int userId);

    <T> T patchMergeEntity(Map<String, Object> patchInput, T inputEntity, Class<T> entityClass);

}
