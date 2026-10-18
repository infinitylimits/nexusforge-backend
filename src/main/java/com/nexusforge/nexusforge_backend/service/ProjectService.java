package com.nexusforge.nexusforge_backend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexusforge.nexusforge_backend.dto.ProjectRequest;
import com.nexusforge.nexusforge_backend.dto.PageResponse;
import com.nexusforge.nexusforge_backend.dto.ProjectResponse;
import com.nexusforge.nexusforge_backend.entity.Project;
import com.nexusforge.nexusforge_backend.entity.Role;
import com.nexusforge.nexusforge_backend.entity.User;
import com.nexusforge.nexusforge_backend.exception.ForbiddenException;
import com.nexusforge.nexusforge_backend.exception.ResourceNotFoundException;
import com.nexusforge.nexusforge_backend.repository.ProjectRepository;
import com.nexusforge.nexusforge_backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Transactional
    public ProjectResponse create(ProjectRequest req, String username) {
        User owner = getUser(username);
        Project project = Project.builder()
                .title(req.title())
                .description(req.description())
                .techStack(req.techStack())
                .githubUrl(req.githubUrl())
                .owner(owner)
                .build();
        return ProjectResponse.from(projectRepository.save(project));
    }

    @Transactional(readOnly = true)
    public ProjectResponse getById(Long id) {
        return ProjectResponse.from(getProject(id));
    }

    @Transactional(readOnly = true)
    public PageResponse<ProjectResponse> list(String techStack, Pageable pageable) {
        Page<Project> page;
        if (techStack != null && !techStack.isBlank()) {
            page = projectRepository.findByTechStackContainingIgnoreCase(techStack.trim(), pageable);
        } else {
            page = projectRepository.findAll(pageable);
        }
        return PageResponse.from(page.map(ProjectResponse::from));
    }

    @Transactional
    public ProjectResponse update(Long id, ProjectRequest req, String username) {
        Project project = getProject(id);
        checkOwnerOrAdmin(project, getUser(username));

        project.setTitle(req.title());
        project.setDescription(req.description());
        project.setTechStack(req.techStack());
        project.setGithubUrl(req.githubUrl());
        return ProjectResponse.from(projectRepository.save(project));
    }

    @Transactional
    public void delete(Long id, String username) {
        Project project = getProject(id);
        checkOwnerOrAdmin(project, getUser(username));
        projectRepository.delete(project);
    }

    @Transactional(readOnly = true)
    public PageResponse<ProjectResponse> listByUser(String username, Pageable pageable) {
        User user = getUser(username);
        return PageResponse.from(projectRepository.findByOwnerId(user.getId(), pageable)
                .map(ProjectResponse::from));
    }
    private Project getProject(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id " + id));
    }

    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }

    private void checkOwnerOrAdmin(Project project, User user) {
        boolean isOwner = project.getOwner().getId().equals(user.getId());
        boolean isAdmin = user.getRole() == Role.ADMIN;
        if (!isOwner && !isAdmin) {
            throw new ForbiddenException("You can only modify your own projects");
        }
    }
}