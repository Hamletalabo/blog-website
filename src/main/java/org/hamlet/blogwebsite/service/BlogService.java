package org.hamlet.blogwebsite.service;

import org.hamlet.blogwebsite.entity.model.User;
import org.hamlet.blogwebsite.payload.request.BlogPostRequest;
import org.hamlet.blogwebsite.payload.response.BlogPostResponse;

public interface BlogService {
    BlogPostResponse createPost(String username, BlogPostRequest blogPostRequest);
}
