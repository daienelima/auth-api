package com.example.auth;

import com.example.auth.adapter.in.domain.AuthRequest;
import com.example.auth.adapter.in.domain.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminAccessTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testUserCannotAccessAdminEndpoint() {
        RegisterRequest request = new RegisterRequest("user1", "123456");

        restTemplate.postForEntity("/api/auth/register", request, String.class);

        AuthRequest login = new AuthRequest("user1", "123456");

        ResponseEntity<Map> loginResp = restTemplate.postForEntity("/api/auth/login", login, Map.class);
        String token = (String) loginResp.getBody().get("token");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<String> resp = restTemplate.exchange("/api/admin/users", HttpMethod.GET, entity, String.class);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }
}
