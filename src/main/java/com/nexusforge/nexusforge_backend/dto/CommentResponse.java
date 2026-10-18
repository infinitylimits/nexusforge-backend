package com.nexusforge.nexusforge_backend.dto;

import java.time.LocalDateTime;

import com.nexusforge.nexusforge_backend.entity.Comment;

public record CommentResponse(
        Long id,
        String content,
        Long postId,
        Long userId,
        String username,
        LocalDateTime createdAt) {

    public static CommentResponse from(Comment c) {
        return new CommentResponse(c.getId(), c.getContent(), c.getPost().getId(),
                c.getUser().getId(), c.getUser().getUsername(), c.getCreatedAt());
    }
}