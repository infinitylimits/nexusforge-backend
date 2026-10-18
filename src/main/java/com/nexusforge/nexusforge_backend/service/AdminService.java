package com.nexusforge.nexusforge_backend.service;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.nexusforge.nexusforge_backend.dto.PageResponse;
import com.nexusforge.nexusforge_backend.dto.UserSummaryResponse;
import com.nexusforge.nexusforge_backend.entity.User;
import com.nexusforge.nexusforge_backend.exception.ResourceNotFoundException;
import com.nexusforge.nexusforge_backend.repository.CommentRepository;
import com.nexusforge.nexusforge_backend.repository.FollowRepository;
import com.nexusforge.nexusforge_backend.repository.PostRepository;
import com.nexusforge.nexusforge_backend.repository.ProjectRepository;
import com.nexusforge.nexusforge_backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final ProjectRepository projectRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    public PageResponse<UserSummaryResponse> listUsers(Pageable pageable) {
        return PageResponse.from(userRepository.findAll(pageable).map(UserSummaryResponse::from));
    }

    @Transactional
    public void deleteUser(Long userId, String adminUsername) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
        User admin = userRepository.findByUsername(adminUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + adminUsername));

        if (user.getId().equals(admin.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot delete your own account");
        }

        // order matters: children first, parent last
        commentRepository.deleteByPostAuthorId(userId);   // comments on this user's posts
        commentRepository.deleteByUserId(userId);         // comments written by this user
        followRepository.deleteAllForUser(userId);        // follows in both directions
        postRepository.deleteByAuthorId(userId);          // this user's posts
        projectRepository.deleteByOwnerId(userId);        // this user's projects
        userRepository.delete(user);                      // finally the user
    }
}