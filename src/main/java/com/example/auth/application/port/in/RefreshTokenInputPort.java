package com.example.auth.application.port.in;

import com.example.auth.application.core.domain.Auth;
import com.example.auth.application.core.domain.RefreshToken;

public interface RefreshTokenInputPort {
    Auth refreshToken(RefreshToken refreshToken);
}
