package org.hamlet.blogwebsite.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EditProfileRequest {
    private String firstname;
    private String lastname;
    private String phoneNumber;
    private String country;
    private String bio;
    private String profilePicture;
}
