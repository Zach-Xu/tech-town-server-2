package com.tech.vo;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.tech.model.Skill;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class SearchUserResponse {

    private Long id;

    private String username;

    private String avatar;

    private LocalDateTime createdTime;

    private String bio;

    private String[] skills;

}
