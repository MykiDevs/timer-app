package org.ikitadevs.timerapp.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ikitadevs.timerapp.entities.enums.TimerState;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Entity
@Setter
@Getter
@Table(name = "timers")
@NoArgsConstructor
public class Timer {

    @Version
    private Long version;

    public Timer(String name, int durationInHours, int durationInMinutes, int durationInSeconds) {
        this.name = name;
        this.initialDuration = Duration.ofHours(durationInHours)
                .plusMinutes(durationInMinutes)
                .plusSeconds(durationInSeconds);
        this.timerState = TimerState.CREATED;
    }

    @Column(nullable = false, unique = true, updatable = false)
    private UUID uuid;

    @Id
    @SequenceGenerator(name ="timer_seq_id", sequenceName = "timer_seq_id", allocationSize = 20)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "timer_seq_id")
    private Long id;


    @Enumerated(EnumType.STRING)
    @NotNull
    TimerState timerState;

    @NotNull(message = "Name can't be empty!")
    @Size(max = 30, message = "Name is too long!")
    String name;

    @NotNull
    private Duration initialDuration;
    private Duration remainingDuration;
    private Instant startTime;
    private Instant endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void start() {
        if(this.timerState == TimerState.CREATED) {
            this.timerState = TimerState.RUNNING;
            this.startTime = Instant.now();
            this.endTime = this.startTime.plus(initialDuration);
            this.remainingDuration = this.initialDuration;
        }
    }

    public void pause() {
        if(this.timerState == TimerState.RUNNING) {
            this.timerState = TimerState.PAUSED;
            this.remainingDuration = Duration.between(Instant.now(), this.endTime);
            if(this.remainingDuration.isNegative()) this.remainingDuration = Duration.ZERO;
            this.endTime = null;
        } else {
          System.out.println("Timer is not running, you can't pause it!");
        }
    }

    public void resume() {
        if(this.timerState == TimerState.PAUSED && remainingDuration != null) {
            this.timerState = TimerState.RUNNING;
            this.endTime = Instant.now().plus(this.remainingDuration);
        }
    }

    public void finish() {
        this.timerState = TimerState.FINISHED;
        this.startTime = null;
        this.endTime = null;
        this.remainingDuration = this.initialDuration;
    }

    @PrePersist
    public void prePersist() {
        if(this.uuid == null) {
            this.uuid = UUID.randomUUID();
        }
    }

    @Override
    public String toString() {
        return "Timer{" +
                "version=" + version +
                ", uuid=" + uuid +
                ", id=" + id +
                ", timerState=" + timerState +
                ", name='" + name + '\'' +
                ", initialDuration=" + initialDuration +
                ", remainingDuration=" + remainingDuration +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", user=" + user +
                '}';
    }
}
