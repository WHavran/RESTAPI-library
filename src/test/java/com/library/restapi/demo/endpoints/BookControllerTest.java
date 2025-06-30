package com.library.restapi.demo.endpoints;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.restapi.demo.model.dto.book.BookUpdateDTO;
import com.library.restapi.demo.repository.BookRepository;
import com.library.restapi.demo.service.BookService;
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
public class BookControllerTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookService bookService;

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
                INSERT INTO Location (`shelf`, `section`, `floor`) VALUES
                ('A1', 'Fiction', 1)
                """);
        jdbc.execute("""
                INSERT INTO Book (title, isbn, genre, published_date, author_id, location_id, is_active) VALUES
                ('1984', '9780451524935', 'Dystopian', '1949-06-08', 1, 1, TRUE),
                ('Animal Farm', '9780451526342', 'Political Satire', '1945-08-17', 1, 1, TRUE),
                ('Harry Potter and the Philosopher''s Stone', '9780747532699', 'Fantasy', '1997-06-26', 2, 1, TRUE),
                ('Harry Potter and the Chamber of Secrets', '9780747538493', 'Fantasy', '1998-07-02', 2, 1, FALSE)""");

        jdbc.execute("""
                INSERT INTO USERS (username, email, password, registered_date, enabled, account_non_expired, account_non_locked, credentials_non_expired)
                VALUES ('john_doe', 'john@example.com', '$2a$10$PRXz5ZyWnQ.HCDOriGb9tuMOLIL4oqsv.exjjZYGUxYAQja8EL.VS', '2022-01-15', 1, 1, 1, 1)
                """);

        jdbc.execute("""
                INSERT INTO Reservation (`user_id`, `book_id`, `reserved_at`, `due_date`, `returned_at`) VALUES
                (1, 1, '2024-01-01 10:00:00', '2024-01-15 10:00:00', '2024-01-12 11:00:00'),
                (1, 2, '2024-05-01 10:00:00', '2024-05-15 10:00:00', '2024-05-13 09:00:00'),
                (1, 2, '2025-05-09 12:36:23', '2025-06-07 10:07:39', NULL)
                """);
    }

    @Test
    public void getOnePublicHttpRequestSuccess() throws Exception {
        assertTrue(bookRepository.findById(1).isPresent());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/book/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("1984")))
                .andExpect(jsonPath("$.isbn", is("9780451524935")))
                .andExpect(jsonPath("$.genre", is("Dystopian")))
                .andExpect(jsonPath("$.publishedDate", is("1949-06-08")))
                .andExpect(jsonPath("$.authorFullName", is("George Orwell")))
                .andExpect(jsonPath("$.location", is("floor:[1] shelf:[A1] section:[Fiction]")));
    }

    @Test
    public void getOnePublicHttpRequestFailed() throws Exception {
        assertFalse(bookRepository.findById(0).isPresent());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/book/{id}", 0))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Entity was not found")));

    }

    @Test
    public void getAllHttpRequestSuccess() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders.get("/api/book/all=active"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/book/all=archived"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));

    }

    @Test
    public void getAllHttpRequestFailed() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/author/all=all"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void postCreateNewHttpRequestSuccess() throws Exception{

        BookUpdateDTO newBookDTO = new BookUpdateDTO(0, "Fantastic Beasts and Where to Find Them", "0123456789123",
                "Fantasy", LocalDate.of(2016,1,1), "J.K. Rowling",
                "floor:[1] shelf:[A1] section:[Fiction]", true, null);

        mockMvc.perform(post("/api/book/create").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newBookDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(greaterThan(0)))
                .andExpect(jsonPath("$.title", is("Fantastic Beasts and Where to Find Them")))
                .andExpect(jsonPath("$.isbn", is("0123456789123")))
                .andExpect(jsonPath("$.publishedDate", is("2016-01-01")))
                .andExpect(jsonPath("$.location", is("floor:[1] shelf:[A1] section:[Fiction]")))
                .andExpect(jsonPath("$.reservations", emptyIterable()));
    }

    @Test
    public void postCreateNewHttpRequestFailed() throws Exception{

        BookUpdateDTO newBookDTO = new BookUpdateDTO(1, "F", "012345",
                "Fantasy", LocalDate.of(2016,1,1), "J.K. Rowling",
                "floor:1 shelf:A1 section[Fiction]", false, null);

        mockMvc.perform(post("/api/book/create").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newBookDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.listOfErrors[0].field", is("isbn")))
                .andExpect(jsonPath("$.listOfErrors[0].message", is("Isbn must be 13 characters.")))
                .andExpect(jsonPath("$.listOfErrors[1].field", is("location")))
                .andExpect(jsonPath("$.listOfErrors[1].message", is("Location must be in format: floor:[number] shelf:[letter+number] section:[text]")))
                .andExpect(jsonPath("$.listOfErrors[2].field", is("title")))
                .andExpect(jsonPath("$.listOfErrors[2].message", is("Title must be between 2 and 50 characters.")));

        BookUpdateDTO newBookDTOWrongAuthor = new BookUpdateDTO(0, "Fantastic Beasts and Where to Find Them", "0123456789123",
                "Fantasy", LocalDate.of(2016,1,1), "Joanne Rowling",
                "floor:[1] shelf:[A1] section:[Fiction]", true, null);

        mockMvc.perform(post("/api/book/create").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newBookDTOWrongAuthor)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Author not found: Joanne Rowling")));

        BookUpdateDTO newBookDTOWrongLocation = new BookUpdateDTO(0, "Fantastic Beasts and Where to Find Them", "0123456789123",
                "Fantasy", LocalDate.of(2016,1,1), "J.K. Rowling",
                "floor:[100] shelf:[Z1] section:[Comedy]", true, null);

        mockMvc.perform(post("/api/book/create").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newBookDTOWrongLocation)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Location not found: floor:[100] shelf:[Z1] section:[Comedy]")));

        BookUpdateDTO newBookDTOWrongAuthorsFormat = new BookUpdateDTO(0, "Fantastic Beasts and Where to Find Them", "0123456789123",
                "Fantasy", LocalDate.of(2016,1,1), "J.K.Rowling",
                "floor:[100] shelf:[Z1] section:[Comedy]", true, null);

        mockMvc.perform(post("/api/book/create").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newBookDTOWrongAuthorsFormat)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Wrong authors name format. Allowed format: 'Firstname Lastname'")));
    }

    @Test
    public void putUpdateHttpRequestSuccess() throws Exception{
        assertTrue(bookRepository.findById(1).isPresent());
        BookUpdateDTO bookForUpdate = new BookUpdateDTO(1, "1984 original", "9780451524935",
                "Dystopian", LocalDate.of(1949,6,8),
                "George Orwell","floor:[1] shelf:[A1] section:[Fiction]", true, null);

        mockMvc.perform(put("/api/book/update").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookForUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("1984 original")))
                .andExpect(jsonPath("$.isbn", is("9780451524935")))
                .andExpect(jsonPath("$.publishedDate", is("1949-06-08")))
                .andExpect(jsonPath("$.location", is("floor:[1] shelf:[A1] section:[Fiction]")))
                .andExpect(jsonPath("$.reservations", iterableWithSize(1)));
    }

    @Test
    public void putUpdateHttpRequestFailed() throws Exception{
        assertFalse(bookRepository.findById(0).isPresent());
        BookUpdateDTO bookForUpdateWrongId = new BookUpdateDTO(0, "1984 original", "9780451524935",
                "Dystopian", LocalDate.of(1949,6,8),
                "George Orwell","floor:[1] shelf:[A1] section:[Fiction]", true, null);

        mockMvc.perform(put("/api/book/update").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookForUpdateWrongId)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Entity was not found")));

    }

    @Test
    public void softDeleteHttpRequestSuccess() throws Exception{
        assertTrue(bookRepository.findById(1).isPresent());
        assertEquals(true, bookService.getOneBookUpdateDTOById(1).isActive());

        mockMvc.perform(delete("/api/book/delete/{id}", 1))
                .andExpect(status().isNoContent());

        assertEquals(false, bookService.getOneBookUpdateDTOById(1).isActive());
    }

    @Test
    public void softDeleteHttpRequestFailed() throws Exception{
        assertTrue(bookRepository.findById(2).isPresent());

        mockMvc.perform(delete("/api/book/delete/{id}", 2))
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Delete book with active reservation is not allowed.")));

    }

    @AfterEach
    public void setUpDbAndDataAfterEach() {
        jdbc.execute("""
                    DELETE FROM reservation;
                    ALTER TABLE reservation ALTER COLUMN reservation_id RESTART WITH 1;
                    DELETE FROM book;
                    ALTER TABLE book ALTER COLUMN book_id RESTART WITH 1;
                    DELETE FROM location;
                    ALTER TABLE location ALTER COLUMN location_id RESTART WITH 1;
                    DELETE FROM author;
                    ALTER TABLE author ALTER COLUMN author_id RESTART WITH 1;
                    DELETE FROM users;
                    ALTER TABLE users ALTER COLUMN user_id RESTART WITH 1;
                """);
    }

}
