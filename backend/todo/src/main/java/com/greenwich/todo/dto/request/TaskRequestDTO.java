package com.greenwich.todo.dto.request;

import com.greenwich.todo.dto.validator.PrioritySubset;
import com.greenwich.todo.dto.validator.StatusSubset;
import com.greenwich.todo.util.Priority;
import com.greenwich.todo.util.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * DTO for creating or updating a task.
 * Contains validation annotations to ensure the integrity of the data.
 */

@Getter
@NoArgsConstructor
public class TaskRequestDTO {

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must not exceed 100 characters")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Status is required")
    @StatusSubset(anyOf = {Status.PENDING, Status.IN_PROGRESS, Status.COMPLETED}, message = "Status must be TODO, IN_PROGRESS, or DONE")
    private Status status;

    @NotNull(message = "Priority is required")
    @PrioritySubset(anyOf = {Priority.LOW, Priority.MEDIUM, Priority.HIGH}, message = "Priority must be LOW, MEDIUM, or HIGH")
    private Priority priority;

    @NotNull(message = "Todolist ID is required")
    Long todolistId;
}
