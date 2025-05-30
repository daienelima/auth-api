package com.example.auth.adapter.in.web;

import com.example.auth.adapter.out.entity.RefreshTokenEntity;
import com.example.auth.adapter.out.repository.RefreshTokenRepository;
import com.example.auth.adapter.out.entity.UserEntity;
import com.example.auth.adapter.out.repository.UserRepository;
import com.example.auth.adapter.in.domain.AuthResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/api/token")
public class TokenController {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    private final String jwtSecret = "mySecretKey";

    public TokenController(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @PostMapping("/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthResponse> generateToken(@PathVariable String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        String token = Jwts.builder()
                .setSubject(user.getUsername())
                .claim("roles", user.getRoles())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();

        refreshTokenRepository.deleteByUsername(username);
        RefreshTokenEntity refresh = new RefreshTokenEntity();
        refresh.setUsername(username);
        refresh.setToken(UUID.randomUUID().toString());
        refresh.setExpiryDate(new Date(System.currentTimeMillis() + 604800000));
        refreshTokenRepository.save(refresh);

        System.out.printf("ADMIN generated token for user '%s'%n", username);
        return ResponseEntity.ok(new AuthResponse(token, refresh.getToken()));
    }
}
