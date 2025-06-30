package com.library.restapi.demo.endpoints;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.restapi.demo.model.dto.author.AuthorUpdateDTO;
import com.library.restapi.demo.repository.AuthorRepository;
import com.library.restapi.demo.service.AuthorService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
@TestPropertySource("classpath:application-test.properties")
public class AuthorControllerTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    public void setupDbAndDataBeforeEach() {
        jdbc.execute("""
                    INSERT INTO Author (first_name, last_name, bio, birth_date, death_date, nationality, is_active) VALUES
                        ('George', 'Orwell', 'George Orwell was a British writer and journalist known for his novels "1984" and "Animal Farm". His works explore themes of totalitarianism, surveillance, and social injustice.\n\nHe also wrote essays and worked as a critic, often reflecting on class and politics in society.', 
                         '1903-06-25', '1950-01-21', 'British', TRUE),
                        ('J.K.', 'Rowling', 'J.K. Rowling is a British author best known for creating the Harry Potter series. Her books have sold millions of copies and been adapted into successful films.\n\nShe also writes crime novels under the pseudonym Robert Galbraith.', 
                         '1965-07-31', NULL, 'British', TRUE),
                         ('Karel', 'VonTester', 'None wants to read his books. Archived', '2000-02-08', NULL, 'Czech', FALSE);
                """);

        jdbc.execute("""
                INSERT INTO book (book_id, title, isbn, genre, published_date, author_id, location_id, is_active)
                VALUES (1, '1984', '9780451524935', 'Dystopian', '1949-06-08', 1, null, TRUE)""");
    }

    @Test
    public void getOnePublicHttpRequestSuccess() throws Exception{

        assertTrue(authorRepository.findById(1).isPresent());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/author/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.fullName", is("George Orwell")))
                .andExpect(jsonPath("$.bio", notNullValue()))
                .andExpect(jsonPath("$.age", is("47 (†1950-01-21)")))
                .andExpect(jsonPath("$.nationality", is("British")))
                .andExpect(jsonPath("$.booksTitles", containsInAnyOrder("1984")));
    }

    @Test
    public void getOnePublicHttpRequestFailed() throws Exception{

        assertFalse(authorRepository.findById(0).isPresent());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/author/{id}", 0))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.status",is(400)))
                .andExpect(jsonPath("$.error", is("Entity was not found")));
    }

    @Test
    public void getAllHttpRequestSuccess() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/author/all=active"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/author/all=archived"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void getAllHttpRequestFailed() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/author/all"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void postCreateNewHttpRequestSuccess() throws Exception{

        AuthorUpdateDTO newAuthorDTO = new AuthorUpdateDTO(0, "Tester", "Testovic",
                "Testing bio for example", LocalDate.of(1992,2,3), null,"Czech", true);

        mockMvc.perform(post("/api/author/create").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAuthorDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(greaterThan(0)))
                .andExpect(jsonPath("$.deathDay").value(nullValue()))
                .andExpect(jsonPath("$.firstName").value("Tester"));

        assertDoesNotThrow(() -> authorService.findEntityById(3), "New author should be found by id");
    }

    @Test
    public void postCreateNewHttpRequestFailed() throws Exception{

        AuthorUpdateDTO newAuthorDTO = new AuthorUpdateDTO(99, "", "B",
                "Testing bio for example", LocalDate.of(2105,2,3), null,"Czech", true);

        mockMvc.perform(post("/api/author/create").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAuthorDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.listOfErrors[0].field", is("birthDay")))
                .andExpect(jsonPath("$.listOfErrors[0].message", is("Birthday must be in the past.")))
                .andExpect(jsonPath("$.listOfErrors[1].field", is("firstName")))
                .andExpect(jsonPath("$.listOfErrors[1].message", is("Firstname is required.")))
                .andExpect(jsonPath("$.listOfErrors[2].field", is("firstName")))
                .andExpect(jsonPath("$.listOfErrors[2].message", is("Firstname must be between 2 and 30 characters.")))
                .andExpect(jsonPath("$.listOfErrors[3].field", is("lastName")))
                .andExpect(jsonPath("$.listOfErrors[3].message", is("Lastname must be between 2 and 30 characters.")));
    }

    @Test
    public void putUpdateHttpRequestSuccess() throws Exception{
        assertTrue(authorRepository.findById(1).isPresent());
        AuthorUpdateDTO authorForUpdate = new AuthorUpdateDTO(1, "Georgo", "Orwello",
                "Georgo Orwello is great-grandson of George Orwell. He was born in Italy", LocalDate.of(1990,2,3), null,"Italy", true);

        mockMvc.perform(put("/api/author/update").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorForUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.deathDay").value(nullValue()))
                .andExpect(jsonPath("$.firstName").value("Georgo"))
                .andExpect(jsonPath("$.lastName").value("Orwello"))
                .andExpect(jsonPath("$.nationality").value("Italy"));

    }

    @Test
    public void putUpdateHttpRequestFailed() throws Exception{
        assertFalse(authorRepository.findById(0).isPresent());
        AuthorUpdateDTO authorForUpdate = new AuthorUpdateDTO(0, "Georgo", "Orwello",
                "Georgo Orwello is great-grandson of George Orwell. He was born in Italy", LocalDate.of(1990,2,3), null,"Italy", true);

        mockMvc.perform(put("/api/author/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorForUpdate)))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.status",is(400)))
                .andExpect(jsonPath("$.error", is("Entity was not found")));
    }

    @Test
    public void patchUpdateHttpRequestSuccess() throws Exception{
        assertTrue(authorRepository.findById(1).isPresent());
        String patchJson = """
        {
            "firstName": "Joanne",
            "deathDay": "2025-06-06",
            "isActive": false
        }
        """;
        mockMvc.perform(patch("/api/author/update/{id}", 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(patchJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.firstName", is("Joanne")))
                .andExpect(jsonPath("$.lastName", is("Rowling")))
                .andExpect(jsonPath("$.deathDay", is("2025-06-06")))
                .andExpect(jsonPath("$.isActive", is(false)));
    }


    @Test
    public void deleteHttpRequestSuccess() throws Exception{
        assertTrue(authorRepository.findById(2).isPresent());
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/author/delete/{id}", 2))
                .andExpect(status().isNoContent());
        assertEquals(authorService.findEntityById(2).getIsActive(), false, "IsActive == false (after softDelete");
    }

    @Test
    public void deleteHttpRequestFailed() throws Exception{

        assertFalse(authorRepository.findById(0).isPresent());
        assertTrue(authorRepository.findById(1).isPresent());
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/author/delete/{id}", 0))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.status",is(400)))
                .andExpect(jsonPath("$.error", is("Entity was not found")));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/author/delete/{id}", 1))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.status",is(400)))
                .andExpect(jsonPath("$.error", is("Delete/archive author with active books is not allowed.")));
    }

    @AfterEach
    public void setUpDbAndDataAfterEach() {
        jdbc.execute("DELETE from book");
        jdbc.execute("ALTER TABLE book ALTER COLUMN book_id RESTART WITH 1");
        jdbc.execute("DELETE from author");
        jdbc.execute("ALTER TABLE author ALTER COLUMN author_id RESTART WITH 1");
    }

}

