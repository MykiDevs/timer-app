package org.ikitadevs.timerapp.entities;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.ikitadevs.timerapp.entities.enums.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Column(nullable = false, unique = true, updatable = false)
    private UUID uuid;

    @Id
    @SequenceGenerator(name = "user_seq_id", sequenceName = "user_seq_id", allocationSize = 20)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "user_seq_id")
    private Long id;

    @Size(max = 30, message = "Too big size for username!")
    @NotEmpty(message = "Name can't be empty!")
    private String name;

    @NotEmpty(message = "Password can't be empty!")
    private String password;

    @OneToOne(cascade = CascadeType.ALL, optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "avatar_id")
    private Avatar avatar;

    @Email
    @Size(max = 30, message = "Too big size for email!")
    @NotEmpty(message = "Email can't be empty!")
    private String email;

    private boolean active;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "users_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }



    @Override
    public boolean isAccountNonExpired() {return true;}
    @Override
    public boolean isAccountNonLocked() {return true; }
    @Override
    public boolean isEnabled() {return active;}
    @Override
    public String getUsername() {
        return email;
    }


    @Transient
    public boolean isAdmin() {
        if(getAuthorities() == null) {
            return false;
        }
        return getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }

    @PrePersist
    public void prePersist() {
        if(this.uuid == null) {
            this.uuid = UUID.randomUUID();
        }
    }
}
