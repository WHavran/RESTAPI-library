package com.library.restapi.demo.mapper.impl;

import com.library.restapi.demo.exceptions.ObjectIsNull;
import com.library.restapi.demo.mapper.AuthorMapper;
import com.library.restapi.demo.model.dto.author.AuthorListDTO;
import com.library.restapi.demo.model.dto.author.AuthorUpdateDTO;
import com.library.restapi.demo.model.entity.Author;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class AuthorMapImpl implements AuthorMapper {

    @Override
    public AuthorListDTO mapEntityToListDTO(Author entity) {

        if (entity == null) {
            throw new RuntimeException("Entity can't be null");
        }

        String fullName = entity.getFirstName() + " " + entity.getLastName();
        String age = countAgeByDates(entity.getBirthday(), entity.getDeathDay());

        List<String> listOfBookTitles = new ArrayList<>();
        for (var book : entity.getBooks()) {
            listOfBookTitles.add(book.getTitle());
        }

        return new AuthorListDTO(
                entity.getId(),
                fullName,
                entity.getBio(),
                age,
                entity.getNationality(),
                listOfBookTitles);
    }

    @Override
    public AuthorUpdateDTO mapEntityToUpdateDTO(Author entity) {

        if (entity == null) {
            throw new ObjectIsNull();
        }

        return new AuthorUpdateDTO(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getBio(),
                entity.getBirthday(),
                entity.getDeathDay(),
                entity.getNationality(),
                entity.getIsActive());
    }

    @Override
    public Author mapUpdateDTOToEntityCreate(AuthorUpdateDTO updateDTO) {

        if (updateDTO == null) {
            throw new ObjectIsNull();
        }

        int setId = 0;

        return new Author(
                setId,
                updateDTO.firstName(),
                updateDTO.lastName(),
                updateDTO.bio(),
                updateDTO.birthDay(),
                updateDTO.deathDay(),
                updateDTO.nationality(),
                updateDTO.isActive());
    }

    @Override
    public void mapUpdateDTOToEntityUpdate(AuthorUpdateDTO updateDTO, Author entity) {

        entity.setFirstName(updateDTO.firstName());
        entity.setLastName(updateDTO.lastName());
        entity.setBio(updateDTO.bio());
        entity.setBirthday(updateDTO.birthDay());
        entity.setDeathDay(updateDTO.deathDay());
        entity.setNationality(updateDTO.nationality());
        entity.setIsActive(updateDTO.isActive());

    }

    private static String countAgeByDates(LocalDate birthDay, LocalDate deathday) {

        if (birthDay == null || (LocalDate.now().getYear() - birthDay.getYear()) < 5) {
            throw new RuntimeException("Invalid birth day");
        }

        int birthYear = birthDay.getYear();
        int deathYear = (deathday != null) ? deathday.getYear() : 0;
        int yearNow = LocalDate.now().getYear();
        int age = 0;
        String ageField = "";

        if (deathYear == 0) {
            age = yearNow - birthYear;
            ageField = String.valueOf(age);
        } else {
            age = deathYear - birthYear;
            ageField = age + " (†" + deathday + ")";
        }
        return ageField;
    }
}
