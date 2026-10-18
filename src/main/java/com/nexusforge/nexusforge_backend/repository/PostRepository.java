package com.nexusforge.nexusforge_backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nexusforge.nexusforge_backend.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByAuthorId(Long authorId, Pageable pageable);

    Page<Post> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);

    @Query("select p from Post p where p.author.id in "
         + "(select f.following.id from Follow f where f.follower.id = :userId)")
    Page<Post> findFeed(@Param("userId") Long userId, Pageable pageable);

    void deleteByAuthorId(Long authorId);
}