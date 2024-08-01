package org.hamlet.blogwebsite.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthRequest {
    private String firstname;
    private String lastname;
    private String username;
    private String phoneNumber;
    private String country;
    private String state;
    private String email;
    private String password;
    private String confirmPassword;
}
