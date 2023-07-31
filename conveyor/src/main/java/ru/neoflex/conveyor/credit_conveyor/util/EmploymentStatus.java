package ru.neoflex.conveyor.credit_conveyor.util;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum EmploymentStatus {
    UNEMPLOYED("unemployed"),
    SELF_EMPLOYED("self-employed"),
    BUSINESS_OWNER("business owner");
    private final String employmentStatus;

    EmploymentStatus(String s) {
        this.employmentStatus = s;
    }

    @Override
    public String toString() {
        return employmentStatus;
    }

    @JsonCreator
    public static EmploymentStatus getValueOfEnum(String value) {
        return value.equals(UNEMPLOYED.toString()) ? UNEMPLOYED :
                (value.equals(SELF_EMPLOYED.toString()) ? SELF_EMPLOYED :
                        value.equals(BUSINESS_OWNER.toString()) ? BUSINESS_OWNER : null);
    }
}
