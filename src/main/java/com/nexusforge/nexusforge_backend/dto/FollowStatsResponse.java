package com.nexusforge.nexusforge_backend.dto;

public record FollowStatsResponse(
        String username,
        long followersCount,
        long followingCount,
        boolean followedByMe) {
}