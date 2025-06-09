package com.greenwich.todo.dto.request;

import com.greenwich.todo.dto.validator.PrioritySubset;
import com.greenwich.todo.dto.validator.StatusSubset;
import com.greenwich.todo.util.Priority;
import com.greenwich.todo.util.Status;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class TaskUpdateDTO {
    @Size(max = 100, message = "Title must not exceed 100 characters")
    private String title;

    private String description;

    @StatusSubset(anyOf = {Status.PENDING, Status.IN_PROGRESS, Status.COMPLETED}, message = "Status must be TODO, IN_PROGRESS, or DONE")
    private Status status;

    @PrioritySubset(anyOf = {Priority.LOW, Priority.MEDIUM, Priority.HIGH}, message = "Priority must be LOW, MEDIUM, or HIGH")
    private Priority priority;

    private Long todolistId;
}
