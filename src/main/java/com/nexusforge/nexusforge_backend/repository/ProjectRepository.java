package com.nexusforge.nexusforge_backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.nexusforge.nexusforge_backend.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    Page<Project> findByOwnerId(Long ownerId, Pageable pageable);

    Page<Project> findByTechStackContainingIgnoreCase(String techStack, Pageable pageable);
}