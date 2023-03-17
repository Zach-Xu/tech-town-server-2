package com.tech.repo;

import com.tech.model.Inbox;
import com.tech.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InboxRepository extends JpaRepository<Inbox, Long> {

    Optional<Inbox> findByParticipantsIn(List<User> participants);
}
