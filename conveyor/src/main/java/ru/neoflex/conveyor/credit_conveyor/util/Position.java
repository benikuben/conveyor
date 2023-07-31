package ru.neoflex.conveyor.credit_conveyor.util;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Position {
    MIDDLE_MANAGER("middle manager"),
    TOP_MANAGER("top manager");

    private final String position;

    Position(String s) {
        this.position = s;
    }

    @Override
    public String toString() {
        return position;
    }

    @JsonCreator
    public static Position fromString(String value) {
        return value.equals(MIDDLE_MANAGER.toString()) ? MIDDLE_MANAGER :
                (value.equals(TOP_MANAGER.toString()) ? TOP_MANAGER : null);

    }
}
