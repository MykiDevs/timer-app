package org.ikitadevs.timerapp.repositories;

import jakarta.persistence.Entity;
import org.ikitadevs.timerapp.entities.Timer;
import org.ikitadevs.timerapp.entities.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TimerRepository extends JpaRepository<Timer, Long> {
    boolean existsByIdAndUser(Long timerId, User user);

    Optional<Timer> findByUuid(UUID uuid);

    Optional<Timer> findByUuidAndUser(UUID uuid, User user);

    @EntityGraph(attributePaths = "avatar")
    Optional<Timer> findByUuidAndUserWithAvatar(UUID uuid, User user);

    @EntityGraph(attributePaths = "avatar")
    Optional<Timer> findByUuidWithAvatar(UUID uuid);
}