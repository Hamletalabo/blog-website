package org.hamlet.blogwebsite.entity.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "comment_tbl")
public class BlogComment extends BaseClass {

    @Column(name = "content", nullable = false)
    private String comment;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User commenter;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private BlogPost blogPost;

    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    private BlogComment parentComment;
}
