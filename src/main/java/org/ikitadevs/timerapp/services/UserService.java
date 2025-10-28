package org.ikitadevs.timerapp.services;


import jakarta.persistence.EntityNotFoundException;
import
import lombok.RequiredArgsConstructor;
import org.ikitadevs.timerapp.dto.request.UserCreateDto;
import org.ikitadevs.timerapp.dto.request.UserLoginDto;
import org.ikitadevs.timerapp.dto.request.UserPatchDto;
import org.ikitadevs.timerapp.dto.response.UserResponseDto;
import org.ikitadevs.timerapp.entities.User;
import org.ikitadevs.timerapp.entities.enums.Role;
import org.ikitadevs.timerapp.exceptions.UserAlreadyExistsException;
import org.ikitadevs.timerapp.mappers.UserMapper;
import org.ikitadevs.timerapp.mappers.context.CycleAvoidingMappingContext;
import org.ikitadevs.timerapp.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.swing.text.html.parser.Entity;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtService jwtService;

    @Transactional(readOnly = true)
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User doesn't exists!"));
    }
    @Transactional(readOnly = true)
    public User getByIdWithAvatar(Long id) {
        return userRepository.findByIdWithAvatar(id)
                .orElseThrow(() -> new EntityNotFoundException("User doesn't exists!"));
    }
    @Transactional(readOnly = true)
    public User getByUuid(UUID uuid) {
        return userRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("User doesn't exists!"));
    }
    @Transactional(readOnly = true)
    public User getByEmailWithAvatar(String email) {
        return userRepository.findByEmailWithAvatar(email)
                .orElseThrow(() -> new EntityNotFoundException("User doesn't exists!"));
    }
    @Transactional(readOnly = true)
    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User doesn't exists!"));
    }

    @Transactional
    public UserResponseDto createUser(UserCreateDto userCreateDto) {
        if(userRepository.existsByEmail(userCreateDto.getEmail())) throw new UserAlreadyExistsException("User with this email already exists!");
        User user = userMapper.toEntity(userCreateDto);
        user.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        user.setActive(true);
        user.setRoles(Set.of(Role.ROLE_USER));
        userRepository.save(user);
        UserResponseDto userResponseDto = userMapper.toUserResponseDto(user, new CycleAvoidingMappingContext());
        userResponseDto.setAccessToken(jwtService.generateAccessToken(user));
        userResponseDto.setRefreshToken(jwtService.generateRefreshToken(user));
        return userResponseDto;
    }

    public UserResponseDto loginUser(UserLoginDto userLoginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userLoginDto.getEmail(),
                        userLoginDto.getPassword()
                )
        );
        User user = (User) authentication.getPrincipal();
        UserResponseDto userResponseDto = userMapper.toUserResponseDto(user, new CycleAvoidingMappingContext());
        userResponseDto.setAccessToken(jwtService.generateAccessToken(user));
        userResponseDto.setRefreshToken(jwtService.generateRefreshToken(user));
        return userResponseDto;
    }

    @Transactional
    public User updateUser(UserPatchDto userPatchDto, Long userId) {
        User user = getByIdWithAvatar(userId);
        userMapper.UpdateUserFromDto(userPatchDto, user);
        if (userPatchDto.getPassword() != null) {
            String password = passwordEncoder.encode(userPatchDto.getPassword());
            user.setPassword(password);
        }
        return userRepository.save(user);
    }
}