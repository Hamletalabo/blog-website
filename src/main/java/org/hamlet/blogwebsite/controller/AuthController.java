package org.hamlet.blogwebsite.controller;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.hamlet.blogwebsite.payload.request.AuthRequest;
import org.hamlet.blogwebsite.payload.request.LoginRequest;
import org.hamlet.blogwebsite.payload.response.AuthResponse;
import org.hamlet.blogwebsite.payload.response.LoginResponse;
import org.hamlet.blogwebsite.service.AuthService;
import org.hamlet.blogwebsite.service.TokenValidationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final TokenValidationService tokenValidationService;

    @PostMapping("/register-user")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody AuthRequest authRequest) throws MessagingException {
        return  ResponseEntity.ok(authService.register(authRequest));
    }

    @PostMapping("/login-user")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest loginRequest){

        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @GetMapping("/confirm")
    public ResponseEntity<?> confirmEmail(@RequestParam("token") String token){

        String result = tokenValidationService.validateToken(token);
        if ("Email confirmed successfully".equals(result)) {
            return ResponseEntity.ok(Collections.singletonMap("message", result));
        } else {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", result));
        }

    }
}
