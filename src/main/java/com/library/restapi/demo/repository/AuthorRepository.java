package com.library.restapi.demo.repository;

import com.library.restapi.demo.model.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Integer> {

    @Query("""
            SELECT a FROM Author a
            LEFT JOIN FETCH a.books WHERE a.id = :id
            """)
    Optional<Author> findEntityWithDetailsById(@Param("id") int id);

    @Query("""
            SELECT a FROM Author a
            LEFT JOIN FETCH a.books WHERE a.isActive = :isActive
            """)
    List<Author> findAllEntitiesWithDetails(@Param("isActive") boolean isActive);

    Optional<Author> findByFirstNameAndLastName(String firstName, String lastName);

}
