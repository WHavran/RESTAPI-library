package com.library.restapi.demo.repository;

import com.library.restapi.demo.model.dto.user.UserListDTO;
import com.library.restapi.demo.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("""
            SELECT new com.library.restapi.demo.model.dto.user.UserListDTO(
                u.id,
                u.username,
                u.email,
                u.registeredDate,
                CASE WHEN COUNT(r) > 0 THEN TRUE ELSE FALSE END)
            FROM User u
            LEFT JOIN u.reservations r ON r.returnedAt IS NULL
            GROUP BY u.id, u.username, u.email, u.registeredDate
            """)
    List<UserListDTO> findAllUserListDTO();

    @Query("""
            SELECT u FROM User u
            LEFT JOIN FETCH u.reservations
            LEFT JOIN FETCH u.roles
            WHERE u.id = :id
            """)
    Optional<User> findEntityWithAllDetails(@Param("id") int theId);

    @Query("""
            SELECT u FROM User u
            LEFT JOIN FETCH u.reservations
            LEFT JOIN FETCH u.roles
            WHERE u.username = :username
            """)
    Optional<User> findEntityWithAllDetailsByUsername(@Param("username") String username);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
}
