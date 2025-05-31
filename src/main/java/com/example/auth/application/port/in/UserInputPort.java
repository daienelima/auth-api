package com.example.auth.application.port.in;

import com.example.auth.application.core.domain.User;

import java.util.List;

public interface UserInputPort {
    User findByUsername(String username);
    void updateRoles(String username, String role);
    List<User> findAllUsers();
}
