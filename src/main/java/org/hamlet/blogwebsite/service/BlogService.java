package org.hamlet.blogwebsite.service;

import org.hamlet.blogwebsite.entity.model.BlogPost;
import org.hamlet.blogwebsite.payload.request.BlogPostRequest;
import org.hamlet.blogwebsite.payload.request.CommentRequest;
import org.hamlet.blogwebsite.payload.response.ApiResponse;
import org.hamlet.blogwebsite.payload.response.BlogPostResponse;
import org.hamlet.blogwebsite.payload.response.CommentResponse;

import java.util.List;

public interface BlogService {
    BlogPostResponse createPost(String username, BlogPostRequest blogPostRequest);

    List<BlogPostResponse> getAllPosts();

    List<BlogPostResponse> getAuthorPost(String username);

    List<BlogPostResponse> filterPostsByAuthorOrContent(String keyword);

    ApiResponse deletePost(Long postId);

    CommentResponse createComment(CommentRequest commentRequest, Long blogPostId, String commenter);

    CommentResponse replyToComment(Long commentId, CommentRequest replyRequest, String replier);
    ApiResponse deleteCommentOrReply(Long commentId, String username);
    List<CommentResponse> getCommentsWithReplyCount(Long blogPostId);

    List<CommentResponse> getAllComments(Long blogPostId);

    List<CommentResponse> getAllReplies(Long commentId);

    int countComments(Long blogPostId);


    ApiResponse likePost(Long blogPostId, String username);

    ApiResponse unlikePost(Long blogPostId, String username);

    int countLikes(Long blogPostId);





}
