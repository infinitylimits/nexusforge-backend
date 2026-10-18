package com.nexusforge.nexusforge_backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nexusforge.nexusforge_backend.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByPostId(Long postId, Pageable pageable);

    void deleteByPostId(Long postId);

    void deleteByUserId(Long userId);

    @Modifying
    @Query("delete from Comment c where c.post.id in "
         + "(select p.id from Post p where p.author.id = :userId)")
    void deleteByPostAuthorId(@Param("userId") Long userId);
}