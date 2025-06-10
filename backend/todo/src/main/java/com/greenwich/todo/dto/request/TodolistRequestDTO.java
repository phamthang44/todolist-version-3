package com.greenwich.todo.dto.request;

import com.greenwich.todo.entity.Task;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class TodolistRequestDTO {

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must not exceed 100 characters")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull
    private Long userId;
//    private final List<Task> tasks = new ArrayList<>();
    //liệu có giải pháp nào thay thằng user id này ko ?

//    @NotNull(message = "User id is required")
//    private String userId;
}
