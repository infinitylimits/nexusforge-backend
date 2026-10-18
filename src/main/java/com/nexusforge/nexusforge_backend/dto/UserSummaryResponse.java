package com.nexusforge.nexusforge_backend.dto;
import java.time.LocalDateTime;

import com.nexusforge.nexusforge_backend.entity.User;

public record UserSummaryResponse(
        Long id,
        String username,
        String email,
        String role,
        LocalDateTime createdAt) {

    public static UserSummaryResponse from(User u) {
        return new UserSummaryResponse(u.getId(), u.getUsername(), u.getEmail(),
                u.getRole().name(), u.getCreatedAt());
    }
}