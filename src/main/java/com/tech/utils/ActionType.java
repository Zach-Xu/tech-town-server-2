package com.tech.utils;

public enum ActionType {

    QUESTION("question"),

    ANSWER("answer"),

    VOTE("vote");

    private final String value;

    ActionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
