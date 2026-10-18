package com.nexusforge.nexusforge_backend.controller;

import java.util.Set;

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
import com.nexusforge.nexusforge_backend.dto.PostRequest;
import com.nexusforge.nexusforge_backend.dto.PostResponse;
import com.nexusforge.nexusforge_backend.service.PostService;
import com.nexusforge.nexusforge_backend.util.PageUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private static final Set<String> SORT_FIELDS = Set.of("createdAt", "title");

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> create(@Valid @RequestBody PostRequest req, Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.create(req, auth.getName()));
    }

    @GetMapping
    public PageResponse<PostResponse> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        return postService.list(keyword,
                PageUtil.of(page, size, sortBy, direction, SORT_FIELDS, "createdAt"));
    }
    @GetMapping("/feed")
    public PageResponse<PostResponse> feed(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication auth) {
        return postService.feed(auth.getName(),
                PageUtil.of(page, size, "createdAt", "desc", SORT_FIELDS, "createdAt"));
    }

    @GetMapping("/{id}")
    public PostResponse getById(@PathVariable Long id) {
        return postService.getById(id);
    }

    @PutMapping("/{id}")
    public PostResponse update(@PathVariable Long id, @Valid @RequestBody PostRequest req,
                               Authentication auth) {
        return postService.update(id, req, auth.getName());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication auth) {
        postService.delete(id, auth.getName());
        return ResponseEntity.noContent().build();
    }
}