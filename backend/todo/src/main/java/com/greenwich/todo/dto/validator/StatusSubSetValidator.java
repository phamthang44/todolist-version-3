package com.greenwich.todo.dto.validator;

import com.greenwich.todo.util.Status;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class StatusSubSetValidator implements ConstraintValidator<StatusSubset, Status> {
    private Status[] statuses;

    @Override
    public void initialize(StatusSubset constraint) {
        this.statuses = constraint.anyOf();
    }

    @Override
    public boolean isValid(Status value, ConstraintValidatorContext context) {
        return value == null || Arrays.asList(statuses).contains(value);
    }
}