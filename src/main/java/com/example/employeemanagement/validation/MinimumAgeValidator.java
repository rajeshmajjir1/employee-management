package com.example.employeemanagement.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class MinimumAgeValidator
        implements ConstraintValidator<MinimumAge, LocalDate> {

    private int minimumAge;

    @Override
    public void initialize(MinimumAge annotation) {
        this.minimumAge = annotation.value();
    }

    @Override
    public boolean isValid(
            LocalDate dateOfBirth,
            ConstraintValidatorContext context) {

        // Let @NotNull handle null values
        if (dateOfBirth == null) {
            return true;
        }

        if (dateOfBirth.isAfter(LocalDate.now())) {
            return false;
        }

        int age = Period.between(
                dateOfBirth,
                LocalDate.now()
        ).getYears();

        return age >= minimumAge;
    }
}