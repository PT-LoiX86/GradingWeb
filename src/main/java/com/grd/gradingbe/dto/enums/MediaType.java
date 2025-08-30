package com.grd.gradingbe.dto.enums;

public enum MediaType
{
    IMAGE("image"),
    VIDEO("video");

    private final String type;

    MediaType(String type)
    {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
