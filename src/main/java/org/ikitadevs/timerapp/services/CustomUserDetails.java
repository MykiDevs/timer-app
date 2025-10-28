package org.ikitadevs.timerapp.services;


import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.ikitadevs.timerapp.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmailWithAvatar(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
