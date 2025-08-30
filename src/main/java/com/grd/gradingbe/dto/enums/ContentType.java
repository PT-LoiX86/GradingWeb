package com.grd.gradingbe.dto.enums;

public enum ContentType
{
    POST("post"),
    COMMENT("comment");

    private final String type;

    ContentType(String type)
    {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
