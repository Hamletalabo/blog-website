package org.hamlet.blogwebsite.repository;

import org.hamlet.blogwebsite.entity.model.BlogComment;
import org.hamlet.blogwebsite.entity.model.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlogCommentRepository extends JpaRepository<BlogComment, Long> {
    List<BlogComment> findByBlogPostAndParentCommentIsNull(BlogPost blogPost);
    List<BlogComment> findByParentCommentId(Long parentCommentId);
    List<BlogComment> findByParentComment(BlogComment parentComment);
    long countByBlogPost(BlogPost blogPost);
    long countByParentComment(BlogComment parentComment);

}
