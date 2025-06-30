package com.library.restapi.demo.repository;

import com.library.restapi.demo.model.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Integer> {

    Optional<Location> findByFloorAndShelfAndSection(int floor, String shelf, String section);

    @Query("""
            SELECT l FROM Location l
            LEFT JOIN FETCH l.storedBooks
            WHERE l.id = :id
            """)
    Optional<Location> findOneWithDetailsById(@Param("id") int id);

    @Query("""
            SELECT l FROM Location l
            LEFT JOIN FETCH l.storedBooks
            """)
    List<Location> findAllLocationWithDetails();

}
