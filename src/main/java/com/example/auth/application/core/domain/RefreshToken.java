package com.example.auth.application.core.domain;

import java.util.Date;

public record RefreshToken(Long id,
                           String username,
                           String token,
                           Date expiryDate) {
}
