package com.nexusforge.nexusforge_backend.dto;

import java.time.LocalDateTime;

public record UserProfileResponse(
        Long id,
        String username,
        String bio,
        LocalDateTime createdAt,
        long followersCount,
        long followingCount,
        boolean followedByMe) {
}