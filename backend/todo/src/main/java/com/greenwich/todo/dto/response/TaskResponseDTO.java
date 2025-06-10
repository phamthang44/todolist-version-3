package com.greenwich.todo.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.greenwich.todo.entity.Task;
import com.greenwich.todo.util.Priority;
import com.greenwich.todo.util.Status;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Setter
public class TaskResponseDTO {

    private Long id;
    private String title;
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    private Status status;
    private Priority priority;

    private Long todoListId;

    public static TaskResponseDTO from(Task task) {
        return new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getCreatedAt(),
                task.getUpdatedAt(),
                task.getStatus(),
                task.getPriority(),
                task.getTodolist() != null ? task.getTodolist().getId() : null
        );
    }
}
