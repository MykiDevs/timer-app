package org.ikitadevs.timerapp.repositories;

import jakarta.persistence.Entity;
import lombok.RequiredArgsConstructor;
import org.ikitadevs.timerapp.entities.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUuid(UUID uuid);

    @EntityGraph(attributePaths = {"avatar"})
    Optional<User> findByIdWithAvatar(Long id);

    Optional<User> findByEmail(String email);


    @EntityGraph(attributePaths = {"avatar"})
    Optional<User> findByEmailWithAvatar(String email);

    boolean existsByEmail(String email);
}
