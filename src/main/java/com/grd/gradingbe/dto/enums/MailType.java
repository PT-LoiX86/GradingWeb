package com.grd.gradingbe.dto.enums;

public enum MailType
{
    REGISTRATION("registration"),
    CHANGE_PASSWORD("change_password");

    private final String type;

    MailType(String type)
    {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
