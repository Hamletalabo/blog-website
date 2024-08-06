package org.hamlet.blogwebsite.repository;

import org.hamlet.blogwebsite.entity.model.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface JwtTokenRepository extends JpaRepository<JwtToken, Long> {


    @Query("SELECT jt FROM JwtToken jt WHERE jt.user.id = :userId")
    List<JwtToken> findAllValidTokenByUser(Long userId);

    Optional<JwtToken> findByToken(String token);
}