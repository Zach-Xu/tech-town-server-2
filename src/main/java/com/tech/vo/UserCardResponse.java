package com.tech.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCardResponse {

    private int following;

    private int followers;

    private int questions;

    private int answers;

    private boolean isFollowed;

    private String avatar;
}
