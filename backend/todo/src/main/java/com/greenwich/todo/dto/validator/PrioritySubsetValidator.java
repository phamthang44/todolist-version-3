package com.greenwich.todo.dto.validator;

import com.greenwich.todo.util.Priority;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class PrioritySubsetValidator implements ConstraintValidator<PrioritySubset, Priority> {
    private Priority[] priorities;

    @Override
    public void initialize(PrioritySubset constraint) {
        this.priorities = constraint.anyOf();
    }

    @Override
    public boolean isValid(Priority value, ConstraintValidatorContext context) {
        return value == null || Arrays.asList(priorities).contains(value);
    }
}