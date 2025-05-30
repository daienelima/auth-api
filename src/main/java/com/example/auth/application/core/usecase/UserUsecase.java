package com.example.auth.application.core.usecase;

import com.example.auth.adapter.in.domain.AuthResponse;
import com.example.auth.application.core.domain.Role;
import com.example.auth.application.core.domain.User;
import com.example.auth.application.port.in.UserInputPort;
import com.example.auth.application.port.out.UserOutputPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserUsecase implements UserInputPort {
    private final UserOutputPort userOutputPort;

    public UserUsecase(UserOutputPort userOutputPort) {
        this.userOutputPort = userOutputPort;
    }

    public AuthResponse addRoleUser(String username, String role) {
        var user = userOutputPort.findByUsername(username);

        if (user.roles().stream().anyMatch(r -> r.name().equals(role))) {
            throw new RuntimeException("Role already assigned to user");
        }

        user.roles().add(new Role(null, role));
        userOutputPort.save(user);

        return new AuthResponse("Role added successfully", null);
    }

    public User findByUsername(String username) {
       return userOutputPort.findByUsername(username);
    }

    @Override
    public void updateRoles(User user) {

    }

    @Override
    public List<User> findAllUsers() {
        return List.of();
    }
}
