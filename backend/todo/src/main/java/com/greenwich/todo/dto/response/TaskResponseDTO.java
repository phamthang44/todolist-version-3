package com.greenwich.todo.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.greenwich.todo.util.Priority;
import com.greenwich.todo.util.Status;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Builder
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
}
