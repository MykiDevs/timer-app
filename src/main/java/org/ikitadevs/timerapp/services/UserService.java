package org.ikitadevs.timerapp.services;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.ikitadevs.timerapp.dto.request.PaginationRequestDto;
import org.ikitadevs.timerapp.dto.request.UserCreateDto;
import org.ikitadevs.timerapp.dto.request.UserLoginDto;
import org.ikitadevs.timerapp.dto.request.UserPatchDto;
import org.ikitadevs.timerapp.dto.response.PaginationResponseDto;
import org.ikitadevs.timerapp.dto.response.UserResponseDto;
import org.ikitadevs.timerapp.entities.User;
import org.ikitadevs.timerapp.entities.enums.Role;
import org.ikitadevs.timerapp.exceptions.UserAlreadyExistsException;
import org.ikitadevs.timerapp.mappers.UserMapper;
import org.ikitadevs.timerapp.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.nio.file.AccessDeniedException;
import java.util.List;
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

    @Transactional(readOnly = true)
    public PaginationResponseDto<List<UserResponseDto>> getAllUsersWithPagination(PaginationRequestDto paginationRequestDto) {
        Sort sort = Sort.by(org.springframework.data.domain.Sort.Direction.fromString(paginationRequestDto.getSortDirection()), paginationRequestDto.getSortBy());
        Pageable pageable = PageRequest.of(paginationRequestDto.getPage(), paginationRequestDto.getSize(), sort);
        Page<User> usersPage = userRepository.findAll(pageable);
        List<UserResponseDto> usersList = usersPage.getContent().stream()
                .map(userMapper::toUserResponseDto)
                .toList();
        return new PaginationResponseDto<>(
                usersList,
                usersPage.getNumber(),
                usersPage.getSize(),
                paginationRequestDto.getSortBy(),
                paginationRequestDto.getSortDirection(),
                usersPage.getNumberOfElements(),
                usersPage.getTotalPages()
        );
    }
    @Transactional
    public UserResponseDto createUser(UserCreateDto userCreateDto) {
        if(userRepository.existsByEmail(userCreateDto.getEmail())) throw new UserAlreadyExistsException("User with this email already exists!");
        User user = userMapper.toEntity(userCreateDto);
        user.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        user.setActive(true);
        user.setRoles(Set.of(Role.ROLE_USER));
        userRepository.save(user);
        UserResponseDto userResponseDto = userMapper.toUserResponseDto(user);
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
        UserResponseDto userResponseDto = userMapper.toUserResponseDto(user);
        userResponseDto.setAccessToken(jwtService.generateAccessToken(user));
        userResponseDto.setRefreshToken(jwtService.generateRefreshToken(user));
        return userResponseDto;
    }

    @Transactional
    public UserResponseDto updateOwnProfile(UserPatchDto userPatchDto, User currentUser) throws AccessDeniedException {
        User oldUser = getByUuid(currentUser.getUuid());
        if(!oldUser.getUuid().equals(currentUser.getUuid())) throw new AccessDeniedException("You haven't access to update this resource!");
        userMapper.UpdateUserFromDto(userPatchDto, oldUser);
        if (userPatchDto.getPassword() != null) {
            String password = passwordEncoder.encode(userPatchDto.getPassword());
            oldUser.setPassword(password);
        }
        UserResponseDto userResponseDto = userMapper.toUserResponseDto(oldUser);
        return userResponseDto;
    }

    @Transactional
    public void deleteUser(User user) {
        userRepository.delete(user);
    }
}