package com.library.restapi.demo.repository;

import com.library.restapi.demo.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Integer> {

    @Query("""
            SELECT DISTINCT b FROM Book b
            JOIN FETCH b.author
            JOIN FETCH b.location
            JOIN FETCH b.reservations
            WHERE b.id = :id
            """)
    Optional<Book> findEntityWithAllDetailsById(@Param("id") int id);

    @Query("""
            SELECT DISTINCT b FROM Book b
            JOIN FETCH b.author
            JOIN FETCH b.location
            WHERE b.id = :id
            """)
    Optional<Book> findEntityWithoutReservationsById(@Param("id") int id);

    @Query("""
            SELECT DISTINCT b FROM Book b
            JOIN FETCH b.author
            JOIN FETCH b.location
            JOIN FETCH b.reservations
            WHERE b.isActive = :isActive
            """)
    List<Book> findAllEntitiesWithAllDetailsById(@Param("isActive") boolean isActive);

    @Query("""
            SELECT DISTINCT b FROM Book b
            JOIN FETCH b.author
            JOIN FETCH b.location
            WHERE b.isActive = :isActive
            """)
    List<Book> findAllEntitiesWithoutReservationsById(@Param("isActive") boolean isActive);

    Optional<Book> findByTitle(String title);

    boolean existsByTitle(String title);
}
