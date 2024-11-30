package com.banquemisr.challenge05.enums;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum HistoryAction {
    COMPLETED,
    IN_PROGRESS,
    UPDATED,
    CREATED;
    @JsonCreator
    public static HistoryAction fromValue(String value) {
        for (HistoryAction action : values()) {
            if (action.name().equalsIgnoreCase(value)) {
                return action;
            }
        }
        throw new IllegalArgumentException("Invalid action value: " + value + ". Allowed values are [CREATED, UPDATED, IN_PROGRESS, COMPLETED].");
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }

}
