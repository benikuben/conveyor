package ru.neoflex.conveyor.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.annotation.Generated;

/**
 * Martial status
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-08-11T19:08:55.611294400+03:00[Europe/Moscow]")
public enum MaritalStatus {
  
  MARRIED("MARRIED"),
  
  DIVORCED("DIVORCED"),
  
  SINGLE("SINGLE"),
  
  WIDOW_WIDOWER("WIDOW_WIDOWER");

  private String value;

  MaritalStatus(String value) {
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
  public static MaritalStatus fromValue(String value) {
    for (MaritalStatus b : MaritalStatus.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

