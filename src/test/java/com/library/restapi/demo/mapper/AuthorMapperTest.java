package com.library.restapi.demo.mapper;

import com.library.restapi.demo.DemoApplication;
import com.library.restapi.demo.model.dto.author.AuthorListDTO;
import com.library.restapi.demo.model.entity.Author;
import com.library.restapi.demo.model.entity.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = DemoApplication.class)
public class AuthorMapperTest {

    @Autowired
    ApplicationContext appContext;

    @Autowired
    AuthorMapper authorMapper;

    @Test
    void countAgeByDatesTest(){
        LocalDate birthday = LocalDate.of(1980, 1, 1);
        LocalDate deathday = LocalDate.of(2000,1,1);
        LocalDate now = LocalDate.now();
        String nowMinusBirth = Integer.toString((now.getYear())-(birthday.getYear()));

        assertEquals("20 (†2000-01-01)", ReflectionTestUtils.invokeMethod(authorMapper, "countAgeByDates", birthday, deathday));
        assertEquals(nowMinusBirth, ReflectionTestUtils.invokeMethod(authorMapper, "countAgeByDates", birthday, null));
        assertThrows(RuntimeException.class, () -> ReflectionTestUtils.invokeMethod(authorMapper, "countAgeByDates", null, null));
    }

    @Test
    void mapEntityToListDTOTest(){

        Author entity = new Author(1, "Jiri", "Test", "This is testing author",
                LocalDate.of(1980, 1, 1),  LocalDate.of(2000,1,1), "Czech", true);
        AuthorListDTO expect = new AuthorListDTO(1, "Jiri Test", "This is testing author",
                "20 (†2000-01-01)", "Czech", new ArrayList<>());

        assertEquals(expect, authorMapper.mapEntityToListDTO(entity), "Result != expectation");

        Book book = new Book("Testing Drama", "0123456789123", "Drama", LocalDate.of(2022, 2, 2));
        entity.getBooks().add(book);
        expect = new AuthorListDTO(1, "Jiri Test", "This is testing author",
                "20 (†2000-01-01)", "Czech", new ArrayList<>(List.of("Testing Drama")));

        assertEquals(expect, authorMapper.mapEntityToListDTO(entity), "Result != expectation");

        assertThrows(RuntimeException.class, () -> authorMapper.mapEntityToListDTO(null));

    }

}
