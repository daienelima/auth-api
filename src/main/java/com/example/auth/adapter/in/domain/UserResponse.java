package com.example.auth.adapter.in.domain;

import java.util.List;

public record UserResponse(String username, String password, List<String> roles) {
}
