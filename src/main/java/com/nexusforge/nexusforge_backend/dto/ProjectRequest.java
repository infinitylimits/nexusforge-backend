package com.nexusforge.nexusforge_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProjectRequest(
        @NotBlank @Size(max = 150) String title,
        @NotBlank @Size(max = 2000) String description,
        @Size(max = 255) String techStack,
        @Size(max = 255) String githubUrl) {
}