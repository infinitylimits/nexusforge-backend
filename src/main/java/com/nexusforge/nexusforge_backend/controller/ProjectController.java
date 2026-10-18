package com.nexusforge.nexusforge_backend.controller;

import java.util.Set;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nexusforge.nexusforge_backend.dto.PageResponse;
import com.nexusforge.nexusforge_backend.dto.ProjectRequest;
import com.nexusforge.nexusforge_backend.dto.ProjectResponse;
import com.nexusforge.nexusforge_backend.service.ProjectService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private static final Set<String> SORT_FIELDS = Set.of("createdAt", "title");

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectResponse> create(@Valid @RequestBody ProjectRequest req,
                                                  Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(projectService.create(req, auth.getName()));
    }

    @GetMapping
    public PageResponse<ProjectResponse> list(
            @RequestParam(required = false) String techStack,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 50);
        String safeSort = SORT_FIELDS.contains(sortBy) ? sortBy : "createdAt";
        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(safeSort).ascending()
                : Sort.by(safeSort).descending();

        Pageable pageable = PageRequest.of(safePage, safeSize, sort);
        return projectService.list(techStack, pageable);
    }

    @GetMapping("/{id}")
    public ProjectResponse getById(@PathVariable Long id) {
        return projectService.getById(id);
    }

    @PutMapping("/{id}")
    public ProjectResponse update(@PathVariable Long id,
                                  @Valid @RequestBody ProjectRequest req,
                                  Authentication auth) {
        return projectService.update(id, req, auth.getName());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication auth) {
        projectService.delete(id, auth.getName());
        return ResponseEntity.noContent().build();
    }
}