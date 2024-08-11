package org.hamlet.blogwebsite.controller;

import lombok.RequiredArgsConstructor;
import org.hamlet.blogwebsite.payload.request.EditUserDetailsRequest;
import org.hamlet.blogwebsite.payload.response.ApiResponse;
import org.hamlet.blogwebsite.payload.response.EditUserDetailsResponse;
import org.hamlet.blogwebsite.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping("/reset-password")
    public ResponseEntity<ApiResponse> updatePassword(@RequestParam String oldPassword,
                                                      @RequestParam String newPassword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        ApiResponse response = userService.resetPassword(currentUsername, oldPassword, newPassword);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/edit-details")
    public ResponseEntity<?> editUserDetails(@RequestBody EditUserDetailsRequest userDetailsRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        EditUserDetailsResponse response = userService.editUserDetails(currentUsername, userDetailsRequest);
        return ResponseEntity.ok(response);
    }
}
