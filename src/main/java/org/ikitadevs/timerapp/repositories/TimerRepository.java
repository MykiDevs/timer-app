package org.ikitadevs.timerapp.repositories;

import jakarta.persistence.Entity;
import org.ikitadevs.timerapp.entities.Timer;
import org.ikitadevs.timerapp.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TimerRepository extends JpaRepository<Timer, Long> {
    boolean existsByIdAndUser(Long timerId, User user);
    boolean existsByUuidAndUser(UUID uuid, User user);

    List<Timer> findAllByUser_id(Long id);

    Optional<Timer> findByUuid(UUID uuid);

    Optional<Timer> findByUuidAndUser(UUID uuid, User user);

    Pageable findAllByUser(User user);

    Page<Timer> findAllByUser_Id(Pageable pageable, Long userId);
}