package com.greenwich.todo.controller;

import com.greenwich.todo.dto.request.TodolistRequestDTO;
import com.greenwich.todo.dto.request.TodolistUpdateDTO;
import com.greenwich.todo.dto.response.PageResponseDTO;
import com.greenwich.todo.dto.response.ResponseData;
import com.greenwich.todo.dto.response.ResponseError;
import com.greenwich.todo.dto.response.TodolistResponseDTO;
import com.greenwich.todo.service.impl.TodolistServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/todolist")
@Validated
@Slf4j
@Tag(name = "Todolist Controller", description = "API for todolist management")
public class TodolistController {

    private final TodolistServiceImpl todolistService;

    public TodolistController(TodolistServiceImpl todolistService) {
        this.todolistService = todolistService;
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseData<List<TodolistResponseDTO>>> getAllTodoLists() {
        log.info("Fetching all todoLists");

        try {
            List<TodolistResponseDTO> todoLists = List.of(todolistService.getAllTodolists());
            ResponseData<List<TodolistResponseDTO>> response = new ResponseData<>(HttpStatus.OK.value(), "Fetched all todoLists successfully", todoLists);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching todoLists: {}", e.getMessage());
            ResponseData<List<TodolistResponseDTO>> errorResponse = new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/all2")
    public ResponseEntity<ResponseData<List<TodolistResponseDTO>>> getAllTodoListsByUserId(
            @RequestParam(defaultValue = "1") @Min(1) int pageNo,
            @RequestParam(defaultValue = "20") @Min(1) int pageSize
    ) {
        log.info("Fetching all todoLists but pageNo and pageSize: {} {}", pageNo, pageSize);

        try {
            PageResponseDTO<TodolistResponseDTO> todoLists = todolistService.getAllTodoListsPaginated(pageNo, pageSize);
            ResponseData<List<TodolistResponseDTO>> response = new ResponseData<List<TodolistResponseDTO>>(HttpStatus.OK.value(), "Fetched all todoLists successfully", (List<TodolistResponseDTO>) todoLists);
            log.info("Fetched {} todoLists for pageNo: {}, pageSize: {}", todoLists.getContent().size(), pageNo, pageSize);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching todoLists for pageNo and pageSize: {} {}", pageNo, pageSize, e);
            ResponseData<List<TodolistResponseDTO>> errorResponse = new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseData<TodolistResponseDTO>> createTodoList(
            @Valid @RequestBody TodolistRequestDTO todolistRequest) {
        log.info("Creating todoList with data: {}", todolistRequest);
        todolistRequest.setUserId(1L);

        try {
            if (todolistRequest == null) {
                return ResponseEntity.badRequest().body(new ResponseData<>(HttpStatus.BAD_REQUEST.value(), "TodoList request data is null"));
            }
            TodolistResponseDTO result = todolistService.createTodolist(todolistRequest);
            log.info("Created todoList response data: {}", result);
            ResponseData<TodolistResponseDTO> response = new ResponseData<>(HttpStatus.CREATED.value(), "TodoList created successfully", result);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Error creating todoList: {}", e.getMessage());
            ResponseError errorResponse = new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error: " + e.getMessage());
            ResponseError sqlError = errorResponse.validateSqlError(e.getMessage());
            if (sqlError != null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(sqlError);
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseData<TodolistResponseDTO>> updateTodoList(
            @Min(1) @PathVariable("id") Long id,
            @Valid @RequestBody TodolistRequestDTO todolistRequest) {
        log.info("Updating todoList with id: {}, data: {}", id, todolistRequest);

        try {
            if (id == null || todolistRequest == null) {
                return ResponseEntity.badRequest().body(new ResponseData<>(HttpStatus.BAD_REQUEST.value(), "TodoList id or request data is null"));
            }
            TodolistResponseDTO result = todolistService.updateTodolist(id, todolistRequest);
            log.info("Updated todoList response data: {}", result);
            ResponseData<TodolistResponseDTO> response = new ResponseData<>(HttpStatus.OK.value(), "TodoList updated successfully", result);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error updating todoList: {}", e.getMessage());
            ResponseData<TodolistResponseDTO> errorResponse = new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseData<TodolistResponseDTO>> updatePartialTodoList(
            @Min(1) @PathVariable("id") Long id,
            @Valid @RequestBody TodolistUpdateDTO todolistRequest
    ) {
        log.info("Partially updating todoList with id: {}, data: {}", id, todolistRequest);

        try {
            if (id == null || todolistRequest == null) {
                return ResponseEntity.badRequest().body(new ResponseData<>(HttpStatus.BAD_REQUEST.value(), "TodoList id or request data is null"));
            }
            TodolistResponseDTO result = todolistService.updatePartialTodolist(id, todolistRequest);
            log.info("Partially updated todoList response data: {}", result);
            ResponseData<TodolistResponseDTO> response = new ResponseData<>(HttpStatus.OK.value(), "TodoList partially updated successfully", result);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error partially updating todoList: {}", e.getMessage());
            ResponseData<TodolistResponseDTO> errorResponse = new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<TodolistResponseDTO>> deleteTodoList(@Min(1) @PathVariable("id") Long id) {
        log.info("Deleting todoList with id: {}", id);

        try {
            if (id == null) {
                return ResponseEntity.badRequest().body(new ResponseData<>(HttpStatus.BAD_REQUEST.value(), "TodoList id is null"));
            }
            boolean isDeleted = todolistService.deleteTodolist(id);
            if (!isDeleted) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseData<>(HttpStatus.NOT_FOUND.value(), "TodoList not found"));
            }
            ResponseData<TodolistResponseDTO> response = new ResponseData<>(HttpStatus.OK.value(), "TodoList deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error deleting todoList: {}", e.getMessage());
            ResponseData<TodolistResponseDTO> errorResponse = new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<TodolistResponseDTO>> getTodolistById(@Min(1) @PathVariable("id") Long id) {
        log.info("Fetching todoList by id: {}", id);

        try {
            if (id == null) {
                return ResponseEntity.badRequest().body(new ResponseData<>(HttpStatus.BAD_REQUEST.value(), "TodoList id is null"));
            }
            TodolistResponseDTO result = todolistService.findTodolistById(id);
            if (result == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseData<>(HttpStatus.NOT_FOUND.value(), "TodoList not found"));
            }
            log.info("Fetched todoList response data: {}", result);
            ResponseData<TodolistResponseDTO> response = new ResponseData<>(HttpStatus.OK.value(), "TodoList found", result);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching todoList by id: {}", e.getMessage());
            ResponseData<TodolistResponseDTO> errorResponse = new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

}
