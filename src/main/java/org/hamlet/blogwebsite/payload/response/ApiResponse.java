package org.hamlet.blogwebsite.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse {
    private String responseCode;

    private String responseMessage;

    private ApiResponse apiResponse;
}
