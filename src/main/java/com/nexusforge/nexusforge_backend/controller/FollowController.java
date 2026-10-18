package com.nexusforge.nexusforge_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexusforge.nexusforge_backend.dto.FollowStatsResponse;
import com.nexusforge.nexusforge_backend.service.FollowService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users/{username}")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping("/follow")
    public ResponseEntity<Void> follow(@PathVariable String username, Authentication auth) {
        followService.follow(username, auth.getName());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/follow")
    public ResponseEntity<Void> unfollow(@PathVariable String username, Authentication auth) {
        followService.unfollow(username, auth.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/follow-stats")
    public FollowStatsResponse stats(@PathVariable String username, Authentication auth) {
        return followService.stats(username, auth.getName());
    }
}