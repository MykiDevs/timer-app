package org.ikitadevs.timerapp.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@Entity
@Table(name = "admins")
@Getter
@Setter
public class Admin implements UserDetails {

    @Id
    @SequenceGenerator(name = "admin_seq_id", sequenceName = "admin_seq_id", allocationSize = 20)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "admin_seq_id")
    private Long id;

    private String email;

    private String password;
}