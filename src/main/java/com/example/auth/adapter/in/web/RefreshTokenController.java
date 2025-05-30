package com.example.auth.adapter.in.web;

import com.example.auth.adapter.in.domain.AuthResponse;
import com.example.auth.adapter.in.domain.RefreshRequest;
import com.example.auth.application.core.domain.RefreshToken;
import com.example.auth.application.port.in.RefreshTokenInputPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class RefreshTokenController {

    private final RefreshTokenInputPort refreshTokenInputPort;

    public RefreshTokenController(RefreshTokenInputPort refreshTokenInputPort) {
        this.refreshTokenInputPort = refreshTokenInputPort;
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
