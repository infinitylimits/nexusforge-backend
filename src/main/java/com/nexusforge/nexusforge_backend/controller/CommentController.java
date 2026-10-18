package com.nexusforge.nexusforge_backend.controller;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nexusforge.nexusforge_backend.dto.CommentRequest;
import com.nexusforge.nexusforge_backend.dto.CommentResponse;
import com.nexusforge.nexusforge_backend.dto.PageResponse;
import com.nexusforge.nexusforge_backend.service.CommentService;
import com.nexusforge.nexusforge_backend.util.PageUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private static final Set<String> SORT_FIELDS = Set.of("createdAt");

    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentResponse> add(@PathVariable Long postId,
                                               @Valid @RequestBody CommentRequest req,
                                               Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.add(postId, req, auth.getName()));
    }

    @GetMapping("/posts/{postId}/comments")
    public PageResponse<CommentResponse> list(@PathVariable Long postId,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size,
                                              @RequestParam(defaultValue = "asc") String direction) {
        return commentService.list(postId,
                PageUtil.of(page, size, "createdAt", direction, SORT_FIELDS, "createdAt"));
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication auth) {
        commentService.delete(id, auth.getName());
        return ResponseEntity.noContent().build();
    }
}
