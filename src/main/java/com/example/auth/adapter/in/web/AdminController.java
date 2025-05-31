package com.example.auth.adapter.in.web;

import com.example.auth.adapter.in.domain.UserResponse;
import com.example.auth.application.core.domain.Role;
import com.example.auth.application.port.in.UserInputPort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

   private final UserInputPort userInputPort;

    public AdminController(UserInputPort userInputPort) {
        this.userInputPort = userInputPort;
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        var users = userInputPort.findAllUsers();
        var userResponses = users.stream()
                .map(user -> new UserResponse(
                        user.username(),
                        user.password(),
                        user.roles().stream()
                                .map(Role::name)
                                .toList()
                ))
                .toList();

        return ResponseEntity.ok(users.isEmpty() ? List.of() : userResponses);
    }

    @PatchMapping("/user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateRole(@RequestHeader("X-username") String userName,
                                        @RequestHeader("X-Role") String role) {

        userInputPort.updateRoles(userName, role);
        return ResponseEntity.ok().build();
    }
}
