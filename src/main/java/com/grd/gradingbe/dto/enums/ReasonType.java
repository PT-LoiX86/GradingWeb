package com.grd.gradingbe.dto.enums;

public enum ReasonType
{
    SPAM("spam"),
    ABUSIVE("abusive"),
    OFFENSIVE("offensive"),
    OFF_TOPIC("off_topic");

    private final String type;

    ReasonType(String type)
    {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
