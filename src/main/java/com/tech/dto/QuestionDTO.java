package com.tech.dto;

import com.tech.model.Tag;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QuestionDTO {

    private String title;

    private String content;

    private List<Tag> tags;
}
