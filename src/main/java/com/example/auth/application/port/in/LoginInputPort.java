package com.example.auth.application.port.in;

import com.example.auth.application.core.domain.Auth;
import com.example.auth.application.core.domain.User;

public interface LoginInputPort {

    void register(User user);
    Auth login(User user);
}
