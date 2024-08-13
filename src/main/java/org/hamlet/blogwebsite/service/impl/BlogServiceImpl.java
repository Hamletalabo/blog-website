package org.hamlet.blogwebsite.service.impl;

import lombok.RequiredArgsConstructor;
import org.hamlet.blogwebsite.entity.model.BlogComment;
import org.hamlet.blogwebsite.entity.model.BlogPost;
import org.hamlet.blogwebsite.entity.model.User;
import org.hamlet.blogwebsite.exception.DoesNotExistException;
import org.hamlet.blogwebsite.exception.UnauthorizedException;
import org.hamlet.blogwebsite.exception.UserNotFoundException;
import org.hamlet.blogwebsite.payload.request.BlogPostRequest;
import org.hamlet.blogwebsite.payload.request.CommentRequest;
import org.hamlet.blogwebsite.payload.response.ApiResponse;
import org.hamlet.blogwebsite.payload.response.BlogPostResponse;
import org.hamlet.blogwebsite.payload.response.CommentResponse;
import org.hamlet.blogwebsite.repository.BlogCommentRepository;
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
    private final BlogCommentRepository blogCommentRepository;
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

    @Override
    public CommentResponse createComment(CommentRequest commentRequest, Long blogPostId, String commenter) {
        User user = userRepository.findByUsername(commenter).orElseThrow(null);
        if (user == null){
            throw new UserNotFoundException("User not found");
        }

        BlogPost post = blogPostRepository.findById(blogPostId).orElseThrow(null);
        if (post == null){
            throw new DoesNotExistException("Post not found");
        }

        BlogComment comment = BlogComment.builder()
                .commenter(user)
                .comment(commentRequest.getComment())
                .blogPost(post)
                .build();
        blogCommentRepository.save(comment);

        return CommentResponse.builder()
                .blogTitle(post.getTitle())
                .comment(comment.getComment())
                .createdAt(comment.getCreatedAt())
                .commenter(user.getFirstname()+ " " + user.getLastname())
                .apiResponse(ApiResponse.builder()
                        .responseCode("200")
                        .responseMessage("comment successfully").build())
                .build();
    }

    @Override
    public CommentResponse replyToComment(Long commentId, CommentRequest replyRequest, String replier) {
        User user = userRepository.findByUsername(replier).orElseThrow(null);
        if (user==null){
            throw new UserNotFoundException("User not found");
        }
        BlogComment parentComment = blogCommentRepository.findById(commentId).orElseThrow(null);
        if (parentComment== null){
            throw new DoesNotExistException("Comment not found");
        }
        BlogComment reply = BlogComment.builder()
                .commenter(user)
                .comment(replyRequest.getComment())
                .parentComment(parentComment)
                .blogPost(parentComment.getBlogPost())
                .build();
        blogCommentRepository.save(reply);
        return null;
    }

    @Override
    public ApiResponse deleteCommentOrReply(Long commentId, String username) {
        BlogComment comment = blogCommentRepository.findById(commentId)
                .orElseThrow(() -> new DoesNotExistException("Comment or reply not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Check if the user is the commenter or the author of the post
        if (!comment.getCommenter().equals(user) && !comment.getBlogPost().getAuthor().equals(user)) {
            throw new UnauthorizedException("You are not authorized to delete this comment or reply");
        }

        // If it's a reply, delete the reply
        if (comment.getParentComment() != null) {
            blogCommentRepository.delete(comment);
            return ApiResponse.builder()
                    .responseCode("888")
                    .responseMessage("Reply deleted successfully")
                    .build();
        }

        // If it's a comment, delete the comment and its replies
        List<BlogComment> replies = blogCommentRepository.findByParentComment(comment);
        blogCommentRepository.deleteAll(replies);
        blogCommentRepository.delete(comment);

        return ApiResponse.builder()
                .responseCode("888")
                .responseMessage("Comment and its replies deleted successfully")
                .build();
    }

    @Override
    public List<CommentResponse> getCommentsWithReplyCount(Long blogPostId) {
        BlogPost post = blogPostRepository.findById(blogPostId)
                .orElseThrow(() -> new DoesNotExistException("Post not found"));

        // Fetch comments without a parent (top-level comments) for the blog post
        List<BlogComment> comments = blogCommentRepository.findByBlogPostAndParentCommentIsNull(post);

        // Build a response with each comment and its respective reply count
        return comments.stream()
                .map(comment -> CommentResponse.builder()
                        .comment(comment.getComment())
                        .createdAt(comment.getCreatedAt())
                        .commenter(comment.getCommenter().getFirstname() + " " + comment.getCommenter().getLastname())
                        .replyCount(blogCommentRepository.countByParentComment(comment))
                        .build())
                .collect(Collectors.toList());
    }


    @Override
    public List<CommentResponse> getAllComments(Long blogPostId) {
        BlogPost post = blogPostRepository.findById(blogPostId).orElseThrow(() -> new DoesNotExistException("Post not found"));

        List<BlogComment> comments = blogCommentRepository.findByBlogPostAndParentCommentIsNull(post);

        return comments.stream().map(comment -> CommentResponse.builder()
                        .blogTitle(post.getTitle())
                        .comment(comment.getComment())
                        .createdAt(comment.getCreatedAt())
                        .commenter(comment.getCommenter().getFirstname() + " " + comment.getCommenter().getLastname())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentResponse> getAllReplies(Long commentId) {
        BlogComment parentComment = blogCommentRepository.findById(commentId).orElseThrow(() -> new DoesNotExistException("Comment not found"));

        List<BlogComment> replies = blogCommentRepository.findByParentComment(parentComment);

        return replies.stream().map(reply -> CommentResponse.builder()
                        .blogTitle(parentComment.getBlogPost().getTitle())
                        .comment(reply.getComment())
                        .createdAt(reply.getCreatedAt())
                        .commenter(reply.getCommenter().getFirstname() + " " + reply.getCommenter().getLastname())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public int countComments(Long blogPostId) {
        BlogPost post = blogPostRepository.findById(blogPostId)
                .orElseThrow(() -> new DoesNotExistException("Post not found"));

        // Count all comments and replies associated with the blog post
        long commentCount = blogCommentRepository.countByBlogPost(post);
        return (int) commentCount;
    }

    @Override
    public ApiResponse likePost(Long blogPostId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        BlogPost post = blogPostRepository.findById(blogPostId)
                .orElseThrow(() -> new DoesNotExistException("Post not found"));

        if (post.getLikedBy().contains(user)) {
            throw new IllegalStateException("User has already liked this post");
        }
        post.getLikedBy().add(user);
        blogPostRepository.save(post);

        return ApiResponse.builder()
                .responseCode("200")
                .responseMessage("Post liked successfully")
                .build();
    }

    @Override
    public ApiResponse unlikePost(Long blogPostId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        BlogPost post = blogPostRepository.findById(blogPostId)
                .orElseThrow(() -> new DoesNotExistException("Post not found"));

        if (!post.getLikedBy().contains(user)) {
            throw new IllegalStateException("User has not liked this post");
        }

        post.getLikedBy().remove(user);
        blogPostRepository.save(post);

        return ApiResponse.builder()
                .responseCode("200")
                .responseMessage("Post unliked successfully")
                .build();
    }

    @Override
    public int countLikes(Long blogPostId) {
        BlogPost post = blogPostRepository.findById(blogPostId)
                .orElseThrow(() -> new DoesNotExistException("Post not found"));

        return post.getLikedBy().size();
    }


}