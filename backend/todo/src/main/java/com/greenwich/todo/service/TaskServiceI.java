package com.greenwich.todo.service;

import com.greenwich.todo.dto.request.TaskRequestDTO;
import com.greenwich.todo.dto.response.TaskResponseDTO;
import com.greenwich.todo.util.Priority;
import com.greenwich.todo.util.Status;

public interface TaskServiceI {
    //method for task management
    //String title, String description, Status status, Priority priority, Long todolistId
    TaskResponseDTO createTask(TaskRequestDTO taskRequestDTO);

    TaskResponseDTO updateTask(Long id, TaskRequestDTO taskRequestDTO);
    TaskResponseDTO getTaskById(Long id);
    void deleteTask(Long id);
    TaskResponseDTO[] getAllTasksByTodolistId(Long todolistId);
    TaskResponseDTO[] getAllTasksByStatusAndPriority(Long todolistId, Status status, Priority priority);
    TaskResponseDTO[] getAllTasksByStatus(Long todolistId, Status status);
    TaskResponseDTO[] getAllTasksByPriority(Long todolistId, Priority priority);


}
