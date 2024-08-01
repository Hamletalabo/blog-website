package org.hamlet.blogwebsite.entity.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hamlet.blogwebsite.entity.enums.Roles;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_tbl")
public class User extends BaseClass implements UserDetails {

    private String firstname;
    private String lastname;

    private String username;
    private String phoneNumber;
    private String country;
    private String state;
    private String email;
    private String password;
    private String confirmPassword;


    @OneToMany(mappedBy = "author")
    private Set<BlogPost> blogPosts;

    @OneToMany(mappedBy = "commenter")
    private Set<BlogComment> comments;

    @OneToMany(mappedBy = "sender")
    private Set<Chat> sentMessages;

    @OneToMany(mappedBy = "receiver")
    private Set<Chat> receivedMessages;

    @Enumerated(value = EnumType.STRING)
    private Roles roles;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE" + roles.name()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
