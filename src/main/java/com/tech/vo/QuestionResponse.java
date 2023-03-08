package com.tech.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class QuestionResponse extends BaseRepsonse{

    private String title;

    private UserResponse user;

    private List<TagResponse> tags;

    private LocalDateTime createdTime;

    private Integer numOfAnswers;
}


