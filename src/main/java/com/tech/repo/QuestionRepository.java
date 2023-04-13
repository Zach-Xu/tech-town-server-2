package com.tech.repo;

import com.tech.model.Inbox;
import com.tech.model.Message;
import com.tech.model.Question;
import com.tech.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    Long countByUser_Id(Long userId);

    Optional<List<Question>> findAllByBookmarkedUsersIn(List<User> users);

    Page<Question> findDistinctByTags_TagNameContainsIgnoreCase(String keyword, Pageable pageable);
    Page<Question> findAllByOrderByCreatedTimeDesc(Pageable pageable);

    Page<Question> findAllByOrderByViewsDesc(Pageable pageable);

    Page<Question> findAllByTitleContainsIgnoreCase(String keyword, Pageable pageable);

    Page<Question> findDistinctByTags_TagNameContainsIgnoreCaseOrTitleContainsIgnoreCase(String keyword, String keyword2, Pageable pageable);

    boolean existsByIdAndBookmarkedUsersIn(Long questionId, List<User> users);
}
