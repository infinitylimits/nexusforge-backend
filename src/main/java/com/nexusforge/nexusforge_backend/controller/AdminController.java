package com.nexusforge.nexusforge_backend.controller;

import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nexusforge.nexusforge_backend.dto.PageResponse;
import com.nexusforge.nexusforge_backend.dto.UserSummaryResponse;
import com.nexusforge.nexusforge_backend.service.AdminService;
import com.nexusforge.nexusforge_backend.util.PageUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private static final Set<String> SORT_FIELDS = Set.of("createdAt", "username");

    private final AdminService adminService;

    @GetMapping("/users")
    public PageResponse<UserSummaryResponse> users(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return adminService.listUsers(
                PageUtil.of(page, size, "createdAt", "desc", SORT_FIELDS, "createdAt"));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id, Authentication auth) {
        adminService.deleteUser(id, auth.getName());
        return ResponseEntity.noContent().build();
    }
}
