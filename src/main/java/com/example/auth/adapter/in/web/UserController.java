package com.example.auth.adapter.in.web;

import com.example.auth.application.core.domain.User;
import com.example.auth.application.port.in.UserInputPort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static java.util.Optional.ofNullable;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserInputPort userInputPort;

    public UserController(UserInputPort userInputPort) {
        this.userInputPort = userInputPort;
    }

    @GetMapping("/me")
    public ResponseEntity<User> getLoggedUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }

        var user = ofNullable(userInputPort.findByUsername(userDetails.getUsername()))
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(user);
    }
}
