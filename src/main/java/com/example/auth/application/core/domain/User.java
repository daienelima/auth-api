package com.example.auth.application.core.domain;

import java.util.List;

public record User(Long id,
                   String username,
                   String password,
                   List<Role> roles) {
}
