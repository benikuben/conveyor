package ru.neoflex.conveyor.credit_conveyor.util.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class OverEighteenYearsOldValidator implements ConstraintValidator<OverEighteenYearsOld, LocalDate> {
    private LocalDate currentDate;

    @Override
    public void initialize(OverEighteenYearsOld constraintAnnotation) {
        currentDate = LocalDate.now();
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        return localDate != null && !localDate.isAfter(currentDate);
    }
}
