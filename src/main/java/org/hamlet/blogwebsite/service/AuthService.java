package org.hamlet.blogwebsite.service;

import jakarta.mail.MessagingException;
import org.hamlet.blogwebsite.payload.request.AuthRequest;
import org.hamlet.blogwebsite.payload.request.LoginRequest;
import org.hamlet.blogwebsite.payload.response.AuthResponse;
import org.hamlet.blogwebsite.payload.response.LoginResponse;

public interface AuthService {
    AuthResponse register(AuthRequest authRequest) throws MessagingException;
    LoginResponse login (LoginRequest loginRequest);
}
