package com.greenwich.todo.controller;

import com.greenwich.todo.dto.request.TaskRequestDTO;
import com.greenwich.todo.dto.request.TaskUpdateDTO;
import com.greenwich.todo.dto.response.ResponseData;
import com.greenwich.todo.dto.response.ResponseError;
import com.greenwich.todo.dto.response.TaskResponseDTO;
import com.greenwich.todo.entity.Todolist;
import com.greenwich.todo.exception.GlobalExceptionHandler;
import com.greenwich.todo.exception.ResourceNotFoundException;
import com.greenwich.todo.service.impl.TaskServiceImpl;
import com.greenwich.todo.util.Priority;
import com.greenwich.todo.util.Status;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping("/create")
    public ResponseEntity<ResponseData<TaskResponseDTO>> createTask(@Valid @RequestBody TaskRequestDTO taskRequestDTO) {
        log.info("Create task with data: {}", taskRequestDTO);

        try {
            if (taskRequestDTO == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task request data is null");
            }
            TaskResponseDTO result = taskService.createTask(taskRequestDTO);
            log.info("Create task response data: {}", result);

            ResponseData<TaskResponseDTO> response = new ResponseData<>(HttpStatus.CREATED.value(), "Task created successfully", result);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            ResponseData<TaskResponseDTO> errorResponse =
                    new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<ResponseData<TaskResponseDTO>> getTaskById(@Min(1) @PathVariable("taskId") Long taskId) {
        log.info("Get task by id: {}", taskId);

        try {
            if (taskId == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task id is null");
            }
            TaskResponseDTO result = taskService.getTaskById(taskId);
            log.info("Get task response data: {}", result);

            ResponseData<TaskResponseDTO> response = new ResponseData<>(HttpStatus.OK.value(), "task found !", result);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponseData<TaskResponseDTO> errorResponse =
                    new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }

    }

    @PutMapping("/{taskId}")
    public ResponseEntity<ResponseData> updateTaskById(
            @Min(1) @PathVariable("taskId") Long taskId,
            @Valid @RequestBody TaskRequestDTO taskRequestDTO) {
        log.info("Update task by id: {}, with data: {}", taskId, taskRequestDTO);

        try {
            if (taskId == null || taskRequestDTO == null) {
                throw new ResourceNotFoundException("Task id or request data is null");
            }
            TaskResponseDTO result = taskService.updateTask(taskId, taskRequestDTO);
            log.info("Update task response data: {}", result);

            ResponseData<TaskResponseDTO> response = new ResponseData<>(HttpStatus.OK.value(), "Task updated successfully", result);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            ResponseError errorResponse = new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            ResponseError errorResponse = new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PatchMapping("/{taskId}")
    public ResponseEntity<ResponseData> updateTaskTitleById(
            @Min(1) @PathVariable("taskId") Long taskId,
            @Valid @RequestBody TaskUpdateDTO taskUpdateDTO) {
        log.info("Update task title by id: {}, with taskUpdateDTO obj: {}", taskId, taskUpdateDTO);

        try {
            if (taskId == null || taskUpdateDTO == null) {
                throw new ResourceNotFoundException("Task id or info of object is null or empty");
            }
            TaskResponseDTO result = taskService.updateTaskPartial(taskId, taskUpdateDTO);
            log.info("Update task title response data: {}", result);

            ResponseData<TaskResponseDTO> response = new ResponseData<>(HttpStatus.OK.value(), "Task title updated successfully", result);
            return ResponseEntity.ok(response);

        } catch (ResourceNotFoundException e) {
            ResponseError errorResponse = new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            ResponseError errorResponse = new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }



    @DeleteMapping("/{taskId}")
    public ResponseEntity<ResponseData> deleteTaskById(@Min(1) @PathVariable("taskId") Long taskId) {
        log.info("Delete task by id: {}", taskId);

        try {
            if (taskId == null) {
                throw new ResourceNotFoundException("Task id is null");
            }
            boolean result = taskService.deleteTask(taskId);
            log.info("Delete task response: {}", result);

            if (result) {
                ResponseData<?> response = new ResponseData<>(HttpStatus.NO_CONTENT.value(), "Task deleted successfully");
                return ResponseEntity.ok(response);
            } else {
                throw new ResourceNotFoundException("Task not found with id: " + taskId);
            }
        } catch (ResourceNotFoundException e) {
            ResponseError errorResponse = new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            ResponseError errorResponse = new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
