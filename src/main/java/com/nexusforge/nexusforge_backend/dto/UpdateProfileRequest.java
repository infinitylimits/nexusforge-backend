package com.nexusforge.nexusforge_backend.dto;

import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(
        @Size(max = 500) String bio) {
}
