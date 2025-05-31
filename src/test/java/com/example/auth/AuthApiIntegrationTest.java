package com.example.auth;

import com.example.auth.adapter.in.domain.AuthRequest;
import com.example.auth.adapter.in.domain.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthApiIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testRegisterAndLogin() {
        RegisterRequest registerRequest = new RegisterRequest("john", "password");

        ResponseEntity<String> registerResponse = restTemplate.postForEntity("/api/auth/register", registerRequest, String.class);
        assertThat(registerResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        AuthRequest authRequest = new AuthRequest("john", "password");

        ResponseEntity<String> loginResponse = restTemplate.postForEntity("/api/auth/login", authRequest, String.class);
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(loginResponse.getBody()).contains("token");
    }
}
