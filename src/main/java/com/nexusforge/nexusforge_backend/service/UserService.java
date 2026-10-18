package com.nexusforge.nexusforge_backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexusforge.nexusforge_backend.dto.UpdateProfileRequest;
import com.nexusforge.nexusforge_backend.dto.UserProfileResponse;
import com.nexusforge.nexusforge_backend.entity.User;
import com.nexusforge.nexusforge_backend.exception.ResourceNotFoundException;
import com.nexusforge.nexusforge_backend.repository.FollowRepository;
import com.nexusforge.nexusforge_backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    @Transactional(readOnly = true)
    public UserProfileResponse getProfile(String username, String viewerUsername) {
        User user = find(username);
        User viewer = find(viewerUsername);
        boolean followedByMe = followRepository
                .existsByFollowerIdAndFollowingId(viewer.getId(), user.getId());
        return toProfile(user, followedByMe);
    }

    @Transactional
    public UserProfileResponse updateProfile(String username, UpdateProfileRequest req) {
        User user = find(username);
        user.setBio(req.bio());
        userRepository.save(user);
        return toProfile(user, false);
    }

    private User find(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }

    private UserProfileResponse toProfile(User user, boolean followedByMe) {
        return new UserProfileResponse(
                user.getId(),
                user.getUsername(),
                user.getBio(),
                user.getCreatedAt(),
                followRepository.countByFollowingId(user.getId()),
                followRepository.countByFollowerId(user.getId()),
                followedByMe);
    }
}