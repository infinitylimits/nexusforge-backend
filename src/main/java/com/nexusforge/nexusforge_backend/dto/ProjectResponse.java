package com.nexusforge.nexusforge_backend.dto;

import java.time.LocalDateTime;

import com.nexusforge.nexusforge_backend.entity.Project;

public record ProjectResponse(
        Long id,
        String title,
        String description,
        String techStack,
        String githubUrl,
        Long ownerId,
        String ownerUsername,
        LocalDateTime createdAt) {

    public static ProjectResponse from(Project p) {
        return new ProjectResponse(
                p.getId(),
                p.getTitle(),
                p.getDescription(),
                p.getTechStack(),
                p.getGithubUrl(),
                p.getOwner().getId(),
                p.getOwner().getUsername(),
                p.getCreatedAt());
    }
}