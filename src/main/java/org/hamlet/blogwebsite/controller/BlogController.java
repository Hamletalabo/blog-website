package org.hamlet.blogwebsite.controller;

import lombok.RequiredArgsConstructor;
import org.hamlet.blogwebsite.payload.request.BlogPostRequest;
import org.hamlet.blogwebsite.payload.response.ApiResponse;
import org.hamlet.blogwebsite.payload.response.BlogPostResponse;
import org.hamlet.blogwebsite.service.BlogService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @DeleteMapping("/delete-post/{postId}")
    public ResponseEntity<ApiResponse> deletePost(@PathVariable Long postId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ApiResponse response = blogService.deletePost(postId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getAllPosts")
    public ResponseEntity<List<BlogPostResponse>> getAllPosts() {
        List<BlogPostResponse> response = blogService.getAllPosts();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getPostsByAuthor")
    public ResponseEntity<List<BlogPostResponse>> getPostsByAuthor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        List<BlogPostResponse> response = blogService.findByAuthor(currentUsername);
        return ResponseEntity.ok(response);
    }
}
