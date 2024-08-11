package org.hamlet.blogwebsite.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EditUserDetailsResponse {

        private String firstname;
        private String lastname;
        private String phoneNumber;
        private String country;
        private String bio;

}
