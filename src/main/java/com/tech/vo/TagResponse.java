package com.tech.vo;

import lombok.Data;

@Data
public class TagResponse extends BaseRepsonse {
    private String tagName;

    private String description;
}
