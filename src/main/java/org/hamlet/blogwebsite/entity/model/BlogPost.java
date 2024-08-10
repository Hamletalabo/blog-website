package org.hamlet.blogwebsite.entity.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "blog_post_tbl")

public class BlogPost extends BaseClass{

    private String title;
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    @OneToMany(mappedBy = "blogPost")
    private Set<BlogComment> comments;

    @ManyToMany
    @JoinTable(name = "blog_post_likes",
            joinColumns = @JoinColumn(name = "blog_post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> likedBy;
}
