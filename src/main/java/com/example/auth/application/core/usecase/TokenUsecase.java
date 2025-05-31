package com.example.auth.application.core.usecase;

import com.example.auth.application.core.domain.RefreshToken;
import com.example.auth.application.core.domain.User;
import com.example.auth.application.port.out.RefreshTokenOutputPort;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Service
public class TokenUsecase {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    private final RefreshTokenOutputPort refreshTokenOutputPort;

    public TokenUsecase(RefreshTokenOutputPort refreshTokenOutputPort) {
        this.refreshTokenOutputPort = refreshTokenOutputPort;
    }

    public String generateToken(User user) {
        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
        return Jwts.builder()
                .setSubject(user.username())
                .claim("roles", user.roles())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String username) {
        var refreshTokenStr = UUID.randomUUID().toString();

        var refreshToken = new RefreshToken(null,
                username, refreshTokenStr,
                new Date(System.currentTimeMillis() + refreshExpiration));

       refreshTokenOutputPort.save(refreshToken);

        return refreshTokenStr;
    }
}
