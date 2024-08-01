package org.hamlet.blogwebsite.entity.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comment_tbl")
public class BlogComment extends BaseClass {

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User commenter;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private BlogPost blogPost;
}
