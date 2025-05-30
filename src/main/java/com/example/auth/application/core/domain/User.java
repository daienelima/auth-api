package com.example.auth.application.core.domain;

import java.util.List;

public record User(String username, String password, List<Role> roles) {
}
