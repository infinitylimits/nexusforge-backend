package com.nexusforge.nexusforge_backend.controller;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nexusforge.nexusforge_backend.dto.PageResponse;
import com.nexusforge.nexusforge_backend.dto.PostResponse;
import com.nexusforge.nexusforge_backend.dto.ProjectResponse;
import com.nexusforge.nexusforge_backend.dto.UpdateProfileRequest;
import com.nexusforge.nexusforge_backend.dto.UserProfileResponse;
import com.nexusforge.nexusforge_backend.service.PostService;
import com.nexusforge.nexusforge_backend.service.ProjectService;
import com.nexusforge.nexusforge_backend.service.UserService;
import com.nexusforge.nexusforge_backend.util.PageUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private static final Set<String> SORT_FIELDS = Set.of("createdAt", "title");

    private final UserService userService;
    private final ProjectService projectService;
    private final PostService postService;

    @GetMapping("/me")
    public UserProfileResponse me(Authentication auth) {
        return userService.getProfile(auth.getName(), auth.getName());
    }

    @PutMapping("/me")
    public UserProfileResponse updateMe(@Valid @RequestBody UpdateProfileRequest req,
                                        Authentication auth) {
        return userService.updateProfile(auth.getName(), req);
    }

    @GetMapping("/{username}")
    public UserProfileResponse profile(@PathVariable String username, Authentication auth) {
        return userService.getProfile(username, auth.getName());
    }

    @GetMapping("/{username}/projects")
    public PageResponse<ProjectResponse> projects(@PathVariable String username,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size) {
        return projectService.listByUser(username,
                PageUtil.of(page, size, "createdAt", "desc", SORT_FIELDS, "createdAt"));
    }

    @GetMapping("/{username}/posts")
    public PageResponse<PostResponse> posts(@PathVariable String username,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size) {
        return postService.listByUser(username,
                PageUtil.of(page, size, "createdAt", "desc", SORT_FIELDS, "createdAt"));
    }
}