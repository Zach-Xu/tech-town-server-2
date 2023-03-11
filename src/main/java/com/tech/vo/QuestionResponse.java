package com.tech.vo;

import com.tech.model.Tag;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class QuestionResponse extends BaseRepsonse{

    private String title;

    private UserResponse user;

    private List<Tag> tags;

    private LocalDateTime createdTime;

    private int numOfAnswers;

    private int votes;

    private int views;
}


