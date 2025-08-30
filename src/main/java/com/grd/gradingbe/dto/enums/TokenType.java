package com.grd.gradingbe.dto.enums;

public enum TokenType
{
    ACCESS("access"),
    REFRESH("refresh"),
    PAYLOAD("payload"),
    GOOGLE_IDTOKEN("google_idtoken");

    private final String type;

    TokenType(String type)
    {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
