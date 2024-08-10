package org.hamlet.blogwebsite.service.impl;

import lombok.RequiredArgsConstructor;
import org.hamlet.blogwebsite.entity.model.BlogPost;
import org.hamlet.blogwebsite.entity.model.User;
import org.hamlet.blogwebsite.exception.UserNotFoundException;
import org.hamlet.blogwebsite.payload.request.BlogPostRequest;
import org.hamlet.blogwebsite.payload.response.BlogPostResponse;
import org.hamlet.blogwebsite.repository.BlogPostRepository;
import org.hamlet.blogwebsite.repository.UserRepository;
import org.hamlet.blogwebsite.service.BlogService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BlogServiceImpl implements BlogService {
    private final BlogPostRepository blogPostRepository;
    private final UserRepository userRepository;
    @Override
    public BlogPostResponse createPost(String username, BlogPostRequest blogPostRequest) {

//        User user = userRepository.findByUsername(username).orElse(null);
//                .orElseThrow(() -> new UserNotFoundException("User not found"));
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Create a new BlogPost entity
        BlogPost blogPost = BlogPost.builder()
                .title(blogPostRequest.getTitle())
                .content(blogPostRequest.getContent())
                .author(author)
                .build();

        // Save the blog post
        blogPostRepository.save(blogPost);

        // Create and return the response
        return BlogPostResponse.builder()
                .title(blogPost.getTitle())
                .createdAt(blogPost.getCreatedAt())
                .authorName(author.getUsername())
                .authorProfilePictureUrl(author.getProfilePicture())
                .build();
    }
}