package com.tech.repo;

import com.tech.model.User;
import com.tech.vo.SearchUserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);


    @Query(value = "SELECT CAST(u.id AS SIGNED) AS user_id, u.username, u.avatar, u.created_time, p.bio, GROUP_CONCAT(s.skill_name) AS skills " +
            "FROM tb_user u " +
            "JOIN tb_profile p ON u.id = p.user_id " +
            "LEFT JOIN tb_skill s ON p.user_id = s.user_id " +
            "WHERE LOWER(u.username) LIKE %:keyword% " +
            "GROUP BY u.id",
            nativeQuery = true)
    Page<Object[]> findProfileWithUserByUsernameContains(@Param("keyword") String keyword, Pageable pageable);


}
