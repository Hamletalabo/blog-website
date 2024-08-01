package org.hamlet.blogwebsite.service;

import org.hamlet.blogwebsite.payload.request.AuthRequest;
import org.hamlet.blogwebsite.payload.response.AuthResponse;

public interface AuthService {
    AuthResponse register(AuthRequest authRequest);
}
