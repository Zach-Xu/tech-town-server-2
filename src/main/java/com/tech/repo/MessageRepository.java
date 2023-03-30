package com.tech.repo;

import com.tech.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    Optional<List<Message>> findALLByInbox_IdOrderByCreatedTimeAsc(Long inboxId);
}
