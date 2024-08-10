package org.hamlet.blogwebsite.repository;

import org.hamlet.blogwebsite.entity.model.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
}
