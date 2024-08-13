package org.hamlet.blogwebsite.controller;

import lombok.RequiredArgsConstructor;
import org.hamlet.blogwebsite.payload.request.BlogPostRequest;
import org.hamlet.blogwebsite.payload.request.CommentRequest;
import org.hamlet.blogwebsite.payload.response.ApiResponse;
import org.hamlet.blogwebsite.payload.response.BlogPostResponse;
import org.hamlet.blogwebsite.payload.response.CommentResponse;
import org.hamlet.blogwebsite.service.BlogService;
import org.springframework.http.HttpStatus;
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
        List<BlogPostResponse> response = blogService.getAuthorPost(currentUsername);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search-posts")
    public ResponseEntity<List<BlogPostResponse>> filterPosts(@RequestParam String keyword){
        List<BlogPostResponse> responses = blogService.filterPostsByAuthorOrContent(keyword);
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/posts/{postId}/create-comment")
    public ResponseEntity<CommentResponse> createComment(@PathVariable Long postId, @RequestBody CommentRequest commentRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        CommentResponse response = blogService.createComment(commentRequest, postId, currentUsername);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/comments/{commentId}/reply-comment")
    public ResponseEntity<CommentResponse> replyToComment( @PathVariable Long commentId, @RequestBody CommentRequest commentRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        CommentResponse response = blogService.replyToComment(commentId, commentRequest, currentUsername);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/posts/{postId}/getAllComments")
    public ResponseEntity<List<CommentResponse>> getAllComments(@PathVariable Long postId) {
        List<CommentResponse> responses = blogService.getAllComments(postId);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    // Endpoint for getting all replies to a comment
    @GetMapping("/comments/{commentId}/replies")
    public ResponseEntity<List<CommentResponse>> getAllReplies(@PathVariable Long commentId) {
        List<CommentResponse> responses = blogService.getAllReplies(commentId);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    // Endpoint for deleting a comment
    @DeleteMapping("/comments/{commentId}/delete-comment")
    public ResponseEntity<ApiResponse> deleteComment( @PathVariable Long commentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        ApiResponse response = blogService.deleteCommentOrReply(commentId, currentUsername);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    // Endpoint for counting comments including replies
    @GetMapping("/posts/{postId}/comments/count-all-comments")
    public ResponseEntity<Integer> countCommentsIncludingReplies(@PathVariable Long postId) {
        int count = blogService.countComments(postId);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    // Endpoint for liking a post
    @PostMapping("/posts/{postId}/like-post")
    public ResponseEntity<ApiResponse> likePost(@RequestParam Long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        ApiResponse response = blogService.likePost(postId, currentUsername);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Endpoint for unliking a post
    @PostMapping("/posts/{postId}/unlike-post")
    public ResponseEntity<ApiResponse> unlikePost(@PathVariable Long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        ApiResponse response = blogService.unlikePost(postId, currentUsername);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Endpoint for counting likes on a post
    @GetMapping("/posts/{postId}/likes/likes-count")
    public ResponseEntity<Integer> countLikes(@PathVariable Long postId) {
        int count = blogService.countLikes(postId);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    // Endpoint to get comments with their respective reply counts
    @GetMapping("/posts/{postId}/comments-replies")
    public ResponseEntity<List<CommentResponse>> getCommentsWithReplyCount(@PathVariable Long postId) {
        List<CommentResponse> comments = blogService.getCommentsWithReplyCount(postId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }
}
