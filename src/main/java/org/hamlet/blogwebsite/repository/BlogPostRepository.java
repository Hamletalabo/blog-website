package org.hamlet.blogwebsite.repository;

import org.hamlet.blogwebsite.entity.model.BlogPost;
import org.hamlet.blogwebsite.entity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {

    List<BlogPost> findByAuthor(User author);

//    List<BlogPost> findByAuthor_FirstnameContainingIgnoreCaseOrAuthor_LastnameContainingIgnoreCaseAndTitleContainingIgnoreCaseOrContentContainingIgnoreCase(
//            String firstNameKeyword,
//            String lastNameKeyword,
//            String titleKeyword,
//            String contentKeyword
//    );

    @Query("SELECT bp FROM BlogPost bp WHERE " +
            "LOWER(bp.author.firstname) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(bp.author.lastname) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(CONCAT(bp.author.firstname, ' ', bp.author.lastname)) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(bp.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(bp.content) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<BlogPost> searchByKeyword(@Param("keyword") String keyword);


}
