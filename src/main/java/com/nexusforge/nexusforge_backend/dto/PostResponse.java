package com.nexusforge.nexusforge_backend.dto;

import java.time.LocalDateTime;

import com.nexusforge.nexusforge_backend.entity.Post;

public record PostResponse(
        Long id,
        String title,
        String content,
        Long authorId,
        String authorUsername,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {

    public static PostResponse from(Post p) {
        return new PostResponse(p.getId(), p.getTitle(), p.getContent(),
                p.getAuthor().getId(), p.getAuthor().getUsername(),
                p.getCreatedAt(), p.getUpdatedAt());
    }
}
