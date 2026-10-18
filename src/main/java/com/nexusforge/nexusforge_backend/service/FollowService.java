package com.nexusforge.nexusforge_backend.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.nexusforge.nexusforge_backend.dto.FollowStatsResponse;
import com.nexusforge.nexusforge_backend.entity.Follow;
import com.nexusforge.nexusforge_backend.entity.User;
import com.nexusforge.nexusforge_backend.exception.ResourceNotFoundException;
import com.nexusforge.nexusforge_backend.repository.FollowRepository;
import com.nexusforge.nexusforge_backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    @Transactional
    public void follow(String targetUsername, String viewerUsername) {
        User target = find(targetUsername);
        User me = find(viewerUsername);

        if (target.getId().equals(me.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot follow yourself");
        }
        if (followRepository.existsByFollowerIdAndFollowingId(me.getId(), target.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "You already follow this user");
        }
        followRepository.save(Follow.builder().follower(me).following(target).build());
    }

    @Transactional
    public void unfollow(String targetUsername, String viewerUsername) {
        User target = find(targetUsername);
        User me = find(viewerUsername);

        Follow follow = followRepository.findByFollowerIdAndFollowingId(me.getId(), target.getId())
                .orElseThrow(() -> new ResourceNotFoundException("You are not following this user"));
        followRepository.delete(follow);
    }

    @Transactional(readOnly = true)
    public FollowStatsResponse stats(String targetUsername, String viewerUsername) {
        User target = find(targetUsername);
        User me = find(viewerUsername);

        return new FollowStatsResponse(
                target.getUsername(),
                followRepository.countByFollowingId(target.getId()),
                followRepository.countByFollowerId(target.getId()),
                followRepository.existsByFollowerIdAndFollowingId(me.getId(), target.getId()));
    }

    private User find(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }
}
