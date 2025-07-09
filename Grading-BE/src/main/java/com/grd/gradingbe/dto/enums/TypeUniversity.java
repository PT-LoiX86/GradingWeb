package com.grd.gradingbe.dto.enums;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public enum TypeUniversity {
    PUBLIC("Public"),
    PRIVATE("Private");

    private final String value;

    TypeUniversity(String value) {
        this.value = value;
    }

    public static TypeUniversity fromValue(@NotBlank(message = "Type of university cannot be blank") String typeUniversity) {
        for (TypeUniversity type : TypeUniversity.values()) {
            if (type.value.equalsIgnoreCase(typeUniversity)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid type of university: " + typeUniversity);
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
