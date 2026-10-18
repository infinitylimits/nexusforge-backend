package com.nexusforge.nexusforge_backend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexusforge.nexusforge_backend.dto.PageResponse;
import com.nexusforge.nexusforge_backend.dto.PostRequest;
import com.nexusforge.nexusforge_backend.dto.PostResponse;
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
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public PostResponse create(PostRequest req, String username) {
        Post post = Post.builder()
                .title(req.title())
                .content(req.content())
                .author(getUser(username))
                .build();
        return PostResponse.from(postRepository.save(post));
    }

    @Transactional(readOnly = true)
    public PostResponse getById(Long id) {
        return PostResponse.from(getPost(id));
    }

    @Transactional(readOnly = true)
    public PageResponse<PostResponse> list(String keyword, Pageable pageable) {
        Page<Post> page = (keyword != null && !keyword.isBlank())
                ? postRepository.findByTitleContainingIgnoreCase(keyword.trim(), pageable)
                : postRepository.findAll(pageable);
        return PageResponse.from(page.map(PostResponse::from));
    }

    @Transactional
    public PostResponse update(Long id, PostRequest req, String username) {
        Post post = getPost(id);
        checkOwnerOrAdmin(post, getUser(username));
        post.setTitle(req.title());
        post.setContent(req.content());
        return PostResponse.from(postRepository.save(post));
    }

    @Transactional
    public void delete(Long id, String username) {
        Post post = getPost(id);
        checkOwnerOrAdmin(post, getUser(username));
        commentRepository.deleteByPostId(id);
        postRepository.delete(post);
    }
    @Transactional(readOnly = true)
    public PageResponse<PostResponse> feed(String username, Pageable pageable) {
        User user = getUser(username);
        return PageResponse.from(postRepository.findFeed(user.getId(), pageable)
                .map(PostResponse::from));
    }
    @Transactional(readOnly = true)
    public PageResponse<PostResponse> listByUser(String username, Pageable pageable) {
        User user = getUser(username);
        return PageResponse.from(postRepository.findByAuthorId(user.getId(), pageable)
                .map(PostResponse::from));
    }

    private Post getPost(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id " + id));
    }

    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }

    private void checkOwnerOrAdmin(Post post, User user) {
        boolean isOwner = post.getAuthor().getId().equals(user.getId());
        if (!isOwner && user.getRole() != Role.ADMIN) {
            throw new ForbiddenException("You can only modify your own posts");
        }
    }

	
}