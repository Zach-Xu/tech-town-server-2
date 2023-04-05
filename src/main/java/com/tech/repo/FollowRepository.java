package com.tech.repo;

import com.tech.model.Follow;
import com.tech.utils.FollowStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    Long countByUserIdAndStatus(Long userId, FollowStatus status);

    Long countByFollowerIdAndStatus(Long userId, FollowStatus status);

    Boolean existsByUserIdAndFollowerIdAndStatus(Long userId, Long followerId, FollowStatus status);
}
