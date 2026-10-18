package com.nexusforge.nexusforge_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nexusforge.nexusforge_backend.entity.Follow;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);

    Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);

    long countByFollowingId(Long userId);

    long countByFollowerId(Long userId);

    @Modifying
    @Query("delete from Follow f where f.follower.id = :userId or f.following.id = :userId")
    void deleteAllForUser(@Param("userId") Long userId);
}
