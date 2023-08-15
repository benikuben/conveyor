package ru.neoflex.conveyor.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.annotation.Generated;

/**
 * Gender
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-08-11T19:08:55.611294400+03:00[Europe/Moscow]")
public enum Gender {
  
  MALE("MALE"),
  
  FEMALE("FEMALE"),
  
  NON_BINARY("NON_BINARY");

  private String value;

  Gender(String value) {
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
  public static Gender fromValue(String value) {
    for (Gender b : Gender.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

