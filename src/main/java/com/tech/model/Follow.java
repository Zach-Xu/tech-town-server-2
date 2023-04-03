package com.tech.model;

import com.tech.utils.FollowStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "Follow")
@Table(name = "tb_follow")
public class Follow extends BaseEntity{

    @Column( name = "user_id", nullable = false)
    private Long userId;

    @Column( name = "follower_id", nullable = false)
    private Long followerId;

    @Enumerated(EnumType.ORDINAL)
    @Column( name = "status", nullable = false, columnDefinition = "smallint default 1 comment 'follow status, 0 for unfollow, 1 for follow'")
    private FollowStatus status;


}
