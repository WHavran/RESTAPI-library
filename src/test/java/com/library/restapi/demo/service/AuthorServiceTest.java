package com.library.restapi.demo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.restapi.demo.DemoApplication;
import com.library.restapi.demo.model.entity.Author;
import com.library.restapi.demo.repository.AuthorRepository;
import com.library.restapi.demo.service.impl.AuthorServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = DemoApplication.class)
public class AuthorServiceTest {

    @Autowired
    private AuthorServiceImpl authorService;
    @MockitoBean
    private AuthorRepository repository;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void margePatchInputWithEntity(){

        Author authorInput = new Author(1, "Michal", "Novak", "Novak is newbie",
                LocalDate.of(1996, 10, 2), null, "Czech", true);

        Author authorExpect= new Author(1, "Karel", "Novak", "Novak became a beast, but his life was short.",
                LocalDate.of(1996, 10, 2), LocalDate.of(2022, 1, 2), "Czech", false);


        Map<String, Object> patchInput = new HashMap<>();
        patchInput.put("firstName", "Karel");
        patchInput.put("bio", "Novak became a beast, but his life was short.");
        patchInput.put("isActive", false);
        patchInput.put("deathDay", "2022-01-02");

        assertAuthorsValuesEquals(authorExpect, ReflectionTestUtils.invokeMethod(authorService, "patchInputEntityMerge", patchInput, authorInput));

    }

    public static void assertAuthorsValuesEquals(Author expected, Author actual) {
        assertEquals(expected.getId(), actual.getId(), "Id's not equals");
        assertEquals(expected.getFirstName(), actual.getFirstName(), "Firstname's not equals ");
        assertEquals(expected.getLastName(), actual.getLastName(), "Lastname's not equals");
        assertEquals(expected.getBio(), actual.getBio(), "Bio's not equals");
        assertEquals(expected.getBirthday(), actual.getBirthday(), "Birthday's not equals");
        assertEquals(expected.getDeathDay(), actual.getDeathDay(), "Deathday's not equals");
        assertEquals(expected.getNationality(), actual.getNationality(), "Nationalities not equals");
        assertEquals(expected.getIsActive(), actual.getIsActive(), "Active statuses not equals");
    }

}
