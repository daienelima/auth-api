package com.example.auth.application.core.usecase;

import com.example.auth.application.port.in.UserInputPort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Service
public class CustomUserDetailsUsecase implements UserDetailsService {

    private final UserInputPort userInputPort;

    public CustomUserDetailsUsecase(UserInputPort userInputPort) {
        this.userInputPort = userInputPort;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return ofNullable(userInputPort.findByUsername(username))
                .map(user -> new User(
                        user.username(),
                        user.password(),
                        user.roles().stream()
                                .map(role -> new SimpleGrantedAuthority(role.name()))
                                .collect(Collectors.toList())
                ))
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}
