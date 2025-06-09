package com.greenwich.todo.controller;

import com.greenwich.todo.dto.response.ResponseData;
import com.greenwich.todo.dto.response.ResponseError;
import com.greenwich.todo.dto.response.TaskDetailResponse;
import com.greenwich.todo.dto.response.TaskResponseDTO;
import com.greenwich.todo.exception.GlobalExceptionHandler;
import com.greenwich.todo.exception.ResourceNotFoundException;
import com.greenwich.todo.service.impl.TaskServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@Validated
@RequestMapping("/api/task")
@Slf4j
@Tag(name = "Task Controller", description = "API for task management")
public class TaskController {

    TaskServiceImpl taskService;

    public TaskController(TaskServiceImpl taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{taskId}")
    @Operation()
    public ResponseData<TaskResponseDTO> getTaskById(@Min(1) @PathVariable("taskId") Long taskId) {
        log.info("Get task by id: {}", taskId);

        try {
            if (taskId == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task id is null");
            }
            TaskResponseDTO result = taskService.getTaskById(taskId);
            log.info("Get task response data: {}", result);
            return new TaskDetailResponse(HttpStatus.OK.value(), "user found !", result);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }

    }
}
