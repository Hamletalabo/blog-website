package org.hamlet.blogwebsite.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hamlet.blogwebsite.entity.enums.Roles;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthRequest {
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
}
