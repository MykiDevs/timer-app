package org.ikitadevs.timerapp.filters;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.ikitadevs.timerapp.entities.User;
import org.ikitadevs.timerapp.entities.enums.Role;
import org.ikitadevs.timerapp.services.JwtService;
import org.ikitadevs.timerapp.services.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    final JwtService jwtService;
    final UserService userService;
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer")){
            filterChain.doFilter(request, response);
            return;
        }
        final String jwt = authHeader.substring(7);

        if(SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                if(jwtService.validateToken(jwt)) {
                    User userDetails = new User();
                    userDetails.setUuid(jwtService.extractUuid(jwt));
                    userDetails.setEmail(jwtService.extractEmail(jwt));
                    userDetails.setActive(jwtService.extractActiveState(jwt));
                    Set<Role> roles = jwtService.extractRoles(jwt).stream()
                                    .map(Role::valueOf)
                                            .collect(Collectors.toSet());
                    userDetails.setRoles(roles);

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
        } catch (Exception e) {
                e.printStackTrace();
        }

        }
        filterChain.doFilter(request, response);
}
}
