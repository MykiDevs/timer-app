package org.ikitadevs.timerapp.repositories;

import org.ikitadevs.timerapp.entities.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUuid(UUID uuid);

    @EntityGraph(attributePaths = {"avatar"})
    Optional<User> findByIdWithAvatar(Long id);


    void deleteByUuid(UUID uuid);

    Optional<User> findByEmail(String email);


    @EntityGraph(attributePaths = {"avatar"})
    Optional<User> findByEmailWithAvatar(String email);

    boolean existsByEmail(String email);
}
