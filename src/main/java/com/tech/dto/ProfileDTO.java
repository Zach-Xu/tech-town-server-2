package com.tech.dto;

import com.tech.model.Skill;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProfileDTO {

    private String username;

    private String bio;

    private String github;

    private List<Skill> skills;

}
