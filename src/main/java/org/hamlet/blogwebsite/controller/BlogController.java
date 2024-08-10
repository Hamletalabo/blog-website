package org.hamlet.blogwebsite.controller;

import lombok.RequiredArgsConstructor;
import org.hamlet.blogwebsite.payload.request.BlogPostRequest;
import org.hamlet.blogwebsite.payload.response.BlogPostResponse;
import org.hamlet.blogwebsite.service.BlogService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/blog")
@RequiredArgsConstructor
public class BlogController {
    private final BlogService blogService;

    @PostMapping("/create-post")
    public ResponseEntity<BlogPostResponse> createPost(@RequestBody BlogPostRequest blogPostRequest){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        BlogPostResponse response =blogService.createPost(currentUsername, blogPostRequest);
        return ResponseEntity.ok(response);
    }

}
