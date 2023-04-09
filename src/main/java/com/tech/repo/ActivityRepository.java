package com.tech.repo;

import com.tech.model.Activity;
import com.tech.utils.ActionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    Optional<List<Activity>> findALLByUserId(Long userId);

    int deleteAllByQuestion_IdAndUserIdAndAction(Long questionId, Long userId, ActionType actionType);
}
