package org.hamlet.blogwebsite.service;

import org.hamlet.blogwebsite.entity.model.BlogPost;
import org.hamlet.blogwebsite.payload.request.BlogPostRequest;
import org.hamlet.blogwebsite.payload.response.ApiResponse;
import org.hamlet.blogwebsite.payload.response.BlogPostResponse;

import java.util.List;

public interface BlogService {
    BlogPostResponse createPost(String username, BlogPostRequest blogPostRequest);

    List<BlogPostResponse> getAllPosts();

    List<BlogPostResponse> findByAuthor(String username);

    ApiResponse deletePost(Long postId);



}
