package ru.neoflex.conveyor.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.annotation.Generated;

/**
 * Position
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-08-11T19:08:55.611294400+03:00[Europe/Moscow]")
public enum Position {
  
  WORKER("WORKER"),
  
  MID_MANAGER("MID_MANAGER"),
  
  TOP_MANAGER("TOP_MANAGER"),
  
  OWNER("OWNER");

  private String value;

  Position(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static Position fromValue(String value) {
    for (Position b : Position.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

