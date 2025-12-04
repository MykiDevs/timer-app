package org.ikitadevs.timerapp;


import lombok.extern.slf4j.Slf4j;
import org.ikitadevs.timerapp.dto.request.UserCreateDto;
import org.ikitadevs.timerapp.dto.request.UserPatchDto;
import org.ikitadevs.timerapp.entities.User;
import org.ikitadevs.timerapp.exceptions.UserAlreadyExistsException;
import org.ikitadevs.timerapp.mappers.UserMapper;
import org.ikitadevs.timerapp.repositories.UserRepository;
import org.ikitadevs.timerapp.services.UserService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.file.AccessDeniedException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class UserServiceTests {
    @BeforeAll
    static void setupAll() {
        log.info("Test is started!");
    }

    @AfterAll
    static void tearDownAll() {
        log.info("Test is ended!");
    }
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;



    @Test
    void whenUpdateUser_withCurrentUser_shouldSuccessfulPasswordChange() {
        UUID userUuid = UUID.randomUUID();
        User currentUser = new User();
        currentUser.setUuid(userUuid);

        User oldUser = new User();
        oldUser.setUuid(userUuid);
        oldUser.setPassword("oldEncodedPassword");

        UserPatchDto userPatchDto = new UserPatchDto("name", "oldRawPassword", "newRawPassword", "test@test.test");

        when(userRepository.findByUuid(currentUser.getUuid())).thenReturn(Optional.of(oldUser));

        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn("newEncodedPassword");

        try {
            userService.updateUser(userPatchDto, currentUser);
            log.info("User after updating: {}", oldUser);
        } catch (AccessDeniedException e) {
            log.error("{}", e.getMessage());
        }
        verify(passwordEncoder).matches("oldRawPassword", "oldEncodedPassword");
        verify(passwordEncoder).encode("newRawPassword");
        assertThat(oldUser.getPassword()).isEqualTo("newEncodedPassword");
    }

    @Test
    void whenUpdateUser_withExistingEmail_shouldThrowException() {
        UserCreateDto userCreateDto = new UserCreateDto("John Doe", "123321", "test@test.test");
        when(userRepository.existsByEmail("test@test.test")).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> {
            userService.createUser(userCreateDto);
        });
    }


}
