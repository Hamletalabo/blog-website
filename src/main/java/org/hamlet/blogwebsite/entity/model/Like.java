package org.hamlet.blogwebsite.entity.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "likes_tbl")
public class Like extends BaseClass{

    @ManyToOne
    @JoinColumn(name = "post_id")
    private BlogPost post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
