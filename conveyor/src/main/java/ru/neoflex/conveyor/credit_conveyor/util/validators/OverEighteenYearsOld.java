package ru.neoflex.conveyor.credit_conveyor.util.validators;

import jakarta.validation.Constraint;
import jakarta.validation.constraints.Past;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OverEighteenYearsOldValidator.class)
@Past
public @interface OverEighteenYearsOld {
    String message() default "Возраст должен быть больше 18 лет";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
