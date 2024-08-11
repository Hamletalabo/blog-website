package org.hamlet.blogwebsite.repository;

import org.hamlet.blogwebsite.entity.model.BlogPost;
import org.hamlet.blogwebsite.entity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {

    List<BlogPost> findByAuthor(User author);
}
