package com.nexusforge.nexusforge_backend.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexusforge.nexusforge_backend.dto.CommentRequest;
import com.nexusforge.nexusforge_backend.dto.CommentResponse;
import com.nexusforge.nexusforge_backend.dto.PageResponse;
import com.nexusforge.nexusforge_backend.entity.Comment;
import com.nexusforge.nexusforge_backend.entity.Post;
import com.nexusforge.nexusforge_backend.entity.Role;
import com.nexusforge.nexusforge_backend.entity.User;
import com.nexusforge.nexusforge_backend.exception.ForbiddenException;
import com.nexusforge.nexusforge_backend.exception.ResourceNotFoundException;
import com.nexusforge.nexusforge_backend.repository.CommentRepository;
import com.nexusforge.nexusforge_backend.repository.PostRepository;
import com.nexusforge.nexusforge_backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentResponse add(Long postId, CommentRequest req, String username) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id " + postId));
        User user = getUser(username);

        Comment comment = Comment.builder()
                .content(req.content())
                .post(post)
                .user(user)
                .build();
        return CommentResponse.from(commentRepository.save(comment));
    }

    @Transactional(readOnly = true)
    public PageResponse<CommentResponse> list(Long postId, Pageable pageable) {
        if (!postRepository.existsById(postId)) {
            throw new ResourceNotFoundException("Post not found with id " + postId);
        }
        return PageResponse.from(commentRepository.findByPostId(postId, pageable)
                .map(CommentResponse::from));
    }

    @Transactional
    public void delete(Long commentId, String username) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id " + commentId));
        User user = getUser(username);

        boolean isOwner = comment.getUser().getId().equals(user.getId());
        if (!isOwner && user.getRole() != Role.ADMIN) {
            throw new ForbiddenException("You can only delete your own comments");
        }
        commentRepository.delete(comment);
    }

    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }
}