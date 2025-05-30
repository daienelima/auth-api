package com.example.auth.adapter.in.web;

import com.example.auth.adapter.in.domain.AuthRequest;
import com.example.auth.adapter.in.domain.AuthResponse;
import com.example.auth.adapter.in.domain.RegisterRequest;
import com.example.auth.application.core.domain.User;
import com.example.auth.application.port.in.LoginInputPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final LoginInputPort loginInputPort;

    public AuthController(LoginInputPort loginInputPort) {
        this.loginInputPort = loginInputPort;
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        loginInputPort.register(new User(request.username(), request.password(), null));
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        var auth = loginInputPort.login(new User(request.username(), request.password(), null));
        return ResponseEntity.ok(new AuthResponse(auth.token(), auth.refreshToken()));
    }
}
