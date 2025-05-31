package com.example.auth.application.core.usecase;

import com.example.auth.application.core.domain.Auth;
import com.example.auth.application.core.domain.Role;
import com.example.auth.application.core.domain.User;
import com.example.auth.application.port.in.LoginInputPort;
import com.example.auth.application.port.out.RefreshTokenOutputPort;
import com.example.auth.application.port.out.UserOutputPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthUsecase implements LoginInputPort {

    private final UserOutputPort userOutputPort;
    private final PasswordEncoder passwordEncoder;
    private final TokenUsecase tokenUsecase;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    public AuthUsecase(UserOutputPort userOutputPort, PasswordEncoder passwordEncoder, TokenUsecase tokenUsecase) {
        this.userOutputPort = userOutputPort;
        this.passwordEncoder = passwordEncoder;
        this.tokenUsecase = tokenUsecase;
    }


    public void register(User user) {
        if (userOutputPort.existsByUsername(user.username())) {
            throw new RuntimeException("Username already exists");
        }
        userOutputPort.save(new User(null, user.username(),
                passwordEncoder.encode(user.password()), List.of(new Role(null, "ROLE_USER"))));
    }

    public Auth login(User user) {
        var userDb = userOutputPort.findByUsername(user.username());

        if (userDb == null || !passwordEncoder.matches(user.password(), userDb.password())) {
            throw new RuntimeException("Invalid credentials");
        }

        var token = tokenUsecase.generateToken(userDb);
        var refreshTokenStr = tokenUsecase.generateRefreshToken(user.username());

        return new Auth(token, refreshTokenStr);
    }
}
