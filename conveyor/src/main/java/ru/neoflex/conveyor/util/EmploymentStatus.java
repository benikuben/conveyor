package ru.neoflex.conveyor.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.annotation.Generated;

/**
 * Employment status
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-08-11T19:08:55.611294400+03:00[Europe/Moscow]")
public enum EmploymentStatus {
  
  UNEMPLOYED("UNEMPLOYED"),
  
  SELF_EMPLOYED("SELF_EMPLOYED"),
  
  EMPLOYED("EMPLOYED"),
  
  BUSINESS_OWNER("BUSINESS_OWNER");

  private String value;

  EmploymentStatus(String value) {
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
  public static EmploymentStatus fromValue(String value) {
    for (EmploymentStatus b : EmploymentStatus.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

