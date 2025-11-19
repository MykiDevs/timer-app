package org.ikitadevs.timerapp.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;


@Setter
@Getter
@Table(name = "avatars")
@Entity
public class Avatar {
    @Id
    @SequenceGenerator(name = "avatar_seq_id", sequenceName = "avatar_seq_id", allocationSize = 20)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "avater_seq_id")
    private Long id;

    private String path;

    @OneToOne(mappedBy = "avatar", fetch = FetchType.EAGER)
    private User user;
}
