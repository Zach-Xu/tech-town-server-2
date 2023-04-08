package com.tech.repo;

import com.tech.model.Inbox;
import com.tech.model.Question;
import com.tech.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    Long countByUser_Id(Long userId);

    Optional<List<Question>> findAllByBookmarkedUsersIn(List<User> users);

    boolean existsByIdAndBookmarkedUsersIn(Long questionId, List<User> users);
}
