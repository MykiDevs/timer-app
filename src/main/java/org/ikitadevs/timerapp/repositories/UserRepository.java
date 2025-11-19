package org.ikitadevs.timerapp.repositories;

import org.ikitadevs.timerapp.entities.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUuid(UUID uuid);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.avatar WHERE u.uuid = :uuid")
    Optional<User> findByUuidWithAvatar(@Param("uuid") UUID uuid);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.avatar WHERE u.id = :id")
    Optional<User> findByIdWithAvatar(@Param("id") Long id);


    void deleteByUuid(UUID uuid);

    Optional<User> findByEmail(String email);


    @Query("SELECT u FROM User u LEFT JOIN FETCH u.avatar WHERE u.email = :email")
    Optional<User> findByEmailWithAvatar(@Param("email") String email);

    boolean existsByEmail(String email);
}
