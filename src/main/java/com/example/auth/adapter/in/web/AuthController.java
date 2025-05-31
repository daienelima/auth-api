package com.example.auth.adapter.in.web;

import com.example.auth.adapter.in.domain.AuthRequest;
import com.example.auth.adapter.in.domain.AuthResponse;
import com.example.auth.adapter.in.domain.RefreshRequest;
import com.example.auth.adapter.in.domain.RegisterRequest;
import com.example.auth.application.core.domain.RefreshToken;
import com.example.auth.application.core.domain.User;
import com.example.auth.application.port.in.LoginInputPort;
import com.example.auth.application.port.in.RefreshTokenInputPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final LoginInputPort loginInputPort;
    private final RefreshTokenInputPort refreshTokenInputPort;

    public AuthController(LoginInputPort loginInputPort, RefreshTokenInputPort refreshTokenInputPort) {
        this.loginInputPort = loginInputPort;
        this.refreshTokenInputPort = refreshTokenInputPort;
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        loginInputPort.register(new User(null, request.username(), request.password(), null));
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        var auth = loginInputPort.login(new User(null,request.username(), request.password(), null));
        return ResponseEntity.ok(new AuthResponse(auth.token(), auth.refreshToken()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshRequest request) {

        var auth = refreshTokenInputPort.refreshToken(new RefreshToken(null,
                request.refreshToken(), request.username(), null));

        return auth != null ?
                ResponseEntity.ok(new AuthResponse(auth.token(), auth.refreshToken())) :
                ResponseEntity.status(401).build();
    }

}
