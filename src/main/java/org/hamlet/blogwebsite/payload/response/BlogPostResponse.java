package org.hamlet.blogwebsite.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlogPostResponse {
    private String title;
    private LocalDateTime createdAt;
    private String authorName;
    private String authorProfilePictureUrl;

}
