package com.grd.gradingbe.enums;

import lombok.Getter;

@Getter
public enum AuthenticationType {
    LOCAL("local"),
    GOOGLE("google"),
    FACEBOOK("facebook"),
    GITHUB("github");

    private final String type;

    AuthenticationType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
