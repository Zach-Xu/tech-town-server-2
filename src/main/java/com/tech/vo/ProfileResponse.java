package com.tech.vo;

import com.tech.model.Skill;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ProfileResponse extends BaseRepsonse{

    private LocalDateTime joinTime;

    private Long userId;

    private String username;

    private int following;

    private int followers;

    private int questions;

    private int answers;

    private String bio;

    private boolean isFollowed;

    private List<Skill> skills;

    private String github;


}
