package ru.neoflex.conveyor.credit_conveyor.util;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Gender {

    MALE("male"),

    FEMALE("female"),

    NON_BINARY("non binary");

    private final String gender;

    Gender(String s) {
        this.gender = s;
    }

    @Override
    public String toString() {
        return gender;
    }

    @JsonCreator
    public static Gender fromString(String value) {
        return value.equals(MALE.toString()) ? MALE :
                (value.equals(FEMALE.toString()) ? FEMALE :
                        value.equals(NON_BINARY.toString()) ? NON_BINARY : null);
    }
}