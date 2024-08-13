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
public class CommentResponse {

    private ApiResponse apiResponse;

    private String blogTitle;

    private String comment;

    private String commenter;

    private LocalDateTime createdAt;

    private long replyCount;
}
