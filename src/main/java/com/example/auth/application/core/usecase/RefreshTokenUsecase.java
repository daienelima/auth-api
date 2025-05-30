package com.example.auth.application.core.usecase;

import com.example.auth.application.core.domain.Auth;
import com.example.auth.application.core.domain.RefreshToken;
import com.example.auth.application.port.in.RefreshTokenInputPort;
import com.example.auth.application.port.out.RefreshTokenOutputPort;
import com.example.auth.application.port.out.UserOutputPort;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RefreshTokenUsecase implements RefreshTokenInputPort {

    private final RefreshTokenOutputPort refreshTokenOutputPort;
    private final UserOutputPort userOutputPort;
    private final TokenUsecase tokenUsecase;

    public RefreshTokenUsecase(RefreshTokenOutputPort refreshTokenOutputPort, UserOutputPort userOutputPort, TokenUsecase tokenUsecase) {
        this.refreshTokenOutputPort = refreshTokenOutputPort;
        this.userOutputPort = userOutputPort;
        this.tokenUsecase = tokenUsecase;
    }

    @Override
    public Auth refreshToken(RefreshToken refreshToken) {
        var token = refreshTokenOutputPort.findTopByUsernameOrderByExpiryDateDesc(refreshToken.username())
                .filter(rt -> rt.token().equals(refreshToken.token()))
                .filter(rt -> rt.expiryDate().after(new Date()))
                .orElse(null);

        if (token == null) {
            return null;
        }

        var user = userOutputPort.findByUsername(refreshToken.username());
        if (user == null) {
            return null;
        }

        var refreshTokenStr = tokenUsecase.generateRefreshToken(user.username());

        return new Auth(tokenUsecase.generateToken(user), refreshTokenStr);
    }
}
