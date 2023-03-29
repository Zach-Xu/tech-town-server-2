package com.tech.repo;

import com.tech.model.Inbox;
import com.tech.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InboxRepository extends JpaRepository<Inbox, Long> {

    @Query(value = "SELECT * FROM tb_inbox i WHERE i.id IN (SELECT ui.inbox_id FROM user_inbox ui WHERE ui.user_id IN (:userIds) GROUP BY ui.inbox_id HAVING COUNT(ui.inbox_id) = 2)", nativeQuery = true)
    Optional<Inbox> findByParticipants(@Param("userIds") List<Long> userIds);

    Optional<List<Inbox>> findAllByParticipantsIn(List<User> participants);


}
