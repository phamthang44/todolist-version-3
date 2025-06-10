package com.greenwich.todo.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.greenwich.todo.entity.Task;
import com.greenwich.todo.entity.Todolist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodolistResponseDTO {
    private Long id;
    private String title;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdAt;
    private List<TaskResponseDTO> tasks;

    public static TodolistResponseDTO fromEntity(Todolist todolist, List<TaskResponseDTO> tasks) {
        return TodolistResponseDTO.builder()
                .id(todolist.getId())
                .title(todolist.getTitle())
                .description(todolist.getDescription())
                .createdAt(todolist.getCreatedAt())
                .tasks(tasks)
                .build();
    }
}
