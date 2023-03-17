package com.tech.utils;

public enum InboxType {
    REGULAR("regular"),
    BOT("bot");

    private final String value;

    InboxType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
