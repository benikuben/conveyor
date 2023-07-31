package ru.neoflex.conveyor.credit_conveyor.util;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum MaritalStatus {
    MARRIED("married"),
    DIVORCED("divorced");
    private final String maritalStatus;

    MaritalStatus(String s) {
        this.maritalStatus = s;
    }

    @Override
    public String toString() {
        return maritalStatus;
    }

    @JsonCreator
    public static MaritalStatus fromString(String value) {
        return value.equals(MARRIED.toString()) ? MARRIED :
                (value.equals(DIVORCED.toString()) ? DIVORCED : null);
    }
}
