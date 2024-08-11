package org.hamlet.blogwebsite.service.impl;

import lombok.RequiredArgsConstructor;
import org.hamlet.blogwebsite.entity.model.BlogPost;
import org.hamlet.blogwebsite.entity.model.User;
import org.hamlet.blogwebsite.exception.DoesNotExistException;
import org.hamlet.blogwebsite.payload.request.BlogPostRequest;
import org.hamlet.blogwebsite.payload.response.ApiResponse;
import org.hamlet.blogwebsite.payload.response.BlogPostResponse;
import org.hamlet.blogwebsite.repository.BlogPostRepository;
import org.hamlet.blogwebsite.repository.UserRepository;
import org.hamlet.blogwebsite.service.BlogService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BlogServiceImpl implements BlogService {
    private final BlogPostRepository blogPostRepository;
    private final UserRepository userRepository;
    @Override
    public BlogPostResponse createPost(String username, BlogPostRequest blogPostRequest) {

        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        BlogPost blogPost = BlogPost.builder()
                .title(blogPostRequest.getTitle())
                .content(blogPostRequest.getContent())
                .author(author)
                .build();

        blogPostRepository.save(blogPost);

        return BlogPostResponse.builder()
                .title(blogPost.getTitle())
                .createdAt(blogPost.getCreatedAt())
                .authorName(author.getUsername()+ " " + author.getLastname())
                .authorProfilePictureUrl(author.getProfilePicture())
                .build();
    }

    @Override
    public List<BlogPostResponse> getAllPosts() {

        List<BlogPost> blogPosts = blogPostRepository.findAll();

        return blogPosts.stream().map(post -> BlogPostResponse.builder()
                        .title(post.getTitle())
                        .content(post.getContent())
                        .createdAt(post.getCreatedAt())
                        .authorName(post.getAuthor().getFirstname()+ " " + post.getAuthor().getLastname())
                        .authorProfilePictureUrl(post.getAuthor().getProfilePicture())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<BlogPostResponse> getAuthorPost(String username) {
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<BlogPost> blogPosts = blogPostRepository.findByAuthor(author);

        return blogPosts.stream().map(post -> BlogPostResponse.builder()
                        .title(post.getTitle())
                        .content(post.getContent())
                        .createdAt(post.getCreatedAt())
                        .authorName(post.getAuthor().getFirstname()+ " " + post.getAuthor().getLastname())
                        .authorProfilePictureUrl(post.getAuthor().getProfilePicture())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<BlogPostResponse> filterPostsByAuthorOrContent(String keyword) {
        List<BlogPost> blogPosts = blogPostRepository
                .searchByKeyword(
                        keyword
                );

        return blogPosts.stream().map(post -> BlogPostResponse.builder()
                        .title(post.getTitle())
                        .content(post.getContent())
                        .createdAt(post.getCreatedAt())
                        .authorName(post.getAuthor().getFirstname() + " " + post.getAuthor().getLastname())
                        .authorProfilePictureUrl(post.getAuthor().getProfilePicture())
                        .build())
                .collect(Collectors.toList());
    }


    @Override
    public ApiResponse deletePost(Long postId) {
        BlogPost post = blogPostRepository.findById(postId).orElse(null);
        if (post==null){
            throw new DoesNotExistException("post does not exist");
        }
        blogPostRepository.delete(post);
        return ApiResponse.builder()
                .responseCode("888")
                .responseMessage("Post deleted successfully")
                .build();
    }


}