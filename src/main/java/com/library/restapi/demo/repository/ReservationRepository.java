package com.library.restapi.demo.repository;

import com.library.restapi.demo.model.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    @Query("""
            SELECT r FROM Reservation r
            WHERE (:isActive = false AND r.returnedAt IS NULL)
            OR (:isActive = true AND r.returnedAt IS NOT NULL)
            """)
    List<Reservation> findByReservedAtNullOrNotNull(@Param("isActive") boolean activeCheck);

    @Query("""
            SELECT r FROM Reservation r
            JOIN r.user u
            WHERE (u.username = :inputUsername)
            """)
    List<Reservation> findByUsername(@Param("inputUsername") String username);

    @Query("""
            SELECT r FROM Reservation r
            JOIN r.book b
            WHERE (b.title = :inputTitle)
            """)
    List<Reservation> findByBookTitle(@Param("inputTitle") String BookTitle);

    @Query("""
            SELECT COUNT(r)
            FROM Reservation r
            WHERE r.user.id = :inUserId
            AND r.returnedAt IS NULL
            """)
    int getCountOfActiveReservationByUserId(@Param("inUserId") int userId);

    @Query("""
            SELECT COUNT(r)
            FROM Reservation r
            WHERE r.book.id = :inBookId
            AND r.returnedAt IS NULL
            """)
    int getCountOfActiveReservationByBookId(@Param("inBookId") int bookTitle);

    boolean existsByIdAndReturnedAtIsNotNull(int id);

}
