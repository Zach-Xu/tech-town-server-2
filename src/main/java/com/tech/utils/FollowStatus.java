package com.tech.utils;

public enum FollowStatus {

    UNFOLLOW(0),

    FOLLOW(1);

    private final Integer value;

    FollowStatus(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
