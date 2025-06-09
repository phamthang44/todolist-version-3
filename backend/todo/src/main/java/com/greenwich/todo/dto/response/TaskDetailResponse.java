package com.greenwich.todo.dto.response;

import com.greenwich.todo.entity.Task;

public class TaskDetailResponse extends ResponseData<TaskResponseDTO>{
    public TaskDetailResponse(int status, String message, TaskResponseDTO data) {
        super(status, message, data);
    }

    public TaskDetailResponse(int status, String message) {
        super(status, message);
    }

}
