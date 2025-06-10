package com.greenwich.todo.controller;

import com.greenwich.todo.dto.request.UserRequestDTO;
import com.greenwich.todo.dto.request.UserUpdatePatchDTO;
import com.greenwich.todo.dto.request.UserUpdatePutDTO;
import com.greenwich.todo.dto.response.ResponseData;
import com.greenwich.todo.dto.response.ResponseError;
import com.greenwich.todo.dto.response.UserResponseDTO;
import com.greenwich.todo.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@Validated
@RequestMapping("/api/v1/user")
@Tag(name = "User Management", description = "Endpoints for managing users")
public class UserController {
    // This class is currently empty, but you can add user-related endpoints here.
    // For example, you might want to add methods for user registration, login, profile management, etc.
    // Make sure to implement the necessary services and DTOs for user management.

    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@Min(1) @PathVariable Long id) {
        log.info("Fetching user with id: {}", id);
        try {
            UserResponseDTO user = userService.findUserById(id);
            ResponseData<UserResponseDTO> response = new ResponseData<>(200, "User fetched successfully", user);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching user: {}", e.getMessage());
            ResponseError errorResponse = new ResponseError(500, "Internal server error: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

//    @PostMapping("/register")
//    public ResponseEntity<ResponseData<UserResponseDTO>> registerUser(@RequestBody UserRequestDTO userDto) {
//        log.info("Registering new user: {}", userDto);
//        try {
//            UserResponseDTO registeredUser = userService.createUser(userDto);
//            ResponseData<UserResponseDTO> response = new ResponseData<>(201, "User registered successfully", registeredUser);
//            return ResponseEntity.status(201).body(response);
//        } catch (Exception e) {
//            log.error("Error registering user: {}", e.getMessage());
//            ResponseError errorResponse = new ResponseError(500, "Internal server error: " + e.getMessage());
//            return ResponseEntity.status(500).body(errorResponse);
//        }
//    }

    @PostMapping("/register")
    public ResponseEntity<ResponseData<UserResponseDTO>> registerUser(@Valid @RequestBody UserRequestDTO userDto) {
        log.info("Registering new user with email: {}", userDto.getEmail());
        try {
            UserResponseDTO registeredUser = userService.createUser(userDto);
            ResponseData<UserResponseDTO> response = new ResponseData<>(201, "User registered successfully", registeredUser);
            return ResponseEntity.status(201).body(response);
        } catch (IllegalArgumentException e) {
            log.error("Validation error registering user: {}", e.getMessage());
            ResponseData<UserResponseDTO> response = new ResponseData<>(400, "Validation error: " + e.getMessage(), null);
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            log.error("Error registering user: {}", e.getMessage());
            ResponseData<UserResponseDTO> response = new ResponseData<>(500, "Internal server error: " + e.getMessage(), null);
            return ResponseEntity.status(500).body(response);
        }
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<ResponseData<UserResponseDTO>> deleteUser(@PathVariable Long id) {
//        try {
//            if(id == null) {
//                throw new RuntimeException("No User Id provided");
//            }
//            log.info("Deleting user with id: {}", id);
//            boolean isDeleted = userService.delete(id);
//            if (isDeleted) {
//                ResponseData<UserResponseDTO> response = new ResponseData<>(200, "User deleted successfully", null);
//                return ResponseEntity.ok(response);
//            } else {
//                ResponseError errorResponse = new ResponseError(404, "User not found");
//                return ResponseEntity.status(404).body(errorResponse);
//            }
//        } catch (Exception e) {
//            log.error("Error deleting user: {}", e.getMessage());
//            ResponseError errorResponse = new ResponseError(500, "Internal server error: " + e.getMessage());
//            return ResponseEntity.status(500).body(errorResponse);
//        }
//    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<UserResponseDTO>> deleteUser(@Min(1) @PathVariable Long id) {
        try {
            if (id == null) {
                ResponseData<UserResponseDTO> response = new ResponseData<>(400, "No User Id provided", null);
                return ResponseEntity.badRequest().body(response);
            }
            log.info("Deleting user with id: {}", id);
            boolean isDeleted = userService.delete(id);
            if (isDeleted) {
                ResponseData<UserResponseDTO> response = new ResponseData<>(200, "User deleted successfully", null);
                return ResponseEntity.ok(response);
            } else {
                ResponseData<UserResponseDTO> response = new ResponseData<>(404, "User not found", null);
                return ResponseEntity.status(404).body(response);
            }
        } catch (Exception e) {
            log.error("Error deleting user: {}", e.getMessage());
            ResponseData<UserResponseDTO> response = new ResponseData<>(500, "Internal server error: " + e.getMessage(), null);
            return ResponseEntity.status(500).body(response);
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ResponseData<UserResponseDTO>> banUser(@Min(1) @PathVariable Long id) {
        try {
            if (id == null) {
                ResponseData<UserResponseDTO> response = new ResponseData<>(400, "No User Id provided", null);
                return ResponseEntity.badRequest().body(response);
            }
            log.info("Ban user with id: {}", id);
            UserResponseDTO updatedUser = userService.updateUserStatus(id);
            if (updatedUser != null) {
                ResponseData<UserResponseDTO> response = new ResponseData<>(200, "User's status(ACTIVE/BANNED) updated successfully", updatedUser);
                return ResponseEntity.ok(response);
            } else {
                ResponseData<UserResponseDTO> response = new ResponseData<>(404, "User not found", null);
                return ResponseEntity.status(404).body(response);
            }
        } catch (Exception e) {
            log.error("Error deleting user: {}", e.getMessage());
            ResponseData<UserResponseDTO> response = new ResponseData<>(500, "Internal server error: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseData<UserResponseDTO>> updateUser(
            @Min(value = 1, message = "User Id must be greater than 0") @PathVariable Long id,
            @Valid @RequestBody UserUpdatePutDTO userDto) {
        log.info("Updating user with id: {}", id);
        try {
            if(id == null) {
                ResponseData<UserResponseDTO> response = new ResponseData<>(400, "No User Id provided", null);
                return ResponseEntity.badRequest().body(response);
            }
            log.info("Ban user with id: {}", id);
            UserResponseDTO updatedUser = userService.updateUser(id, userDto);
            if (updatedUser != null) {
                ResponseData<UserResponseDTO> response = new ResponseData<>(200, "User updated successfully", updatedUser);
                return ResponseEntity.ok(response);
            } else {
                ResponseData<UserResponseDTO> response = new ResponseData<>(404, "User not found", null);
                return ResponseEntity.status(404).body(response);
            }
        } catch (Exception e) {
            log.error("Error deleting user: {}", e.getMessage());
            ResponseData<UserResponseDTO> response = new ResponseData<>(500, "Internal server error: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseData<UserResponseDTO>> updatePartialUser(
            @Min(value = 1, message = "User Id must be greater than 0") @PathVariable Long id,
            @Valid @RequestBody UserUpdatePatchDTO userDto) {
        log.info("Partially updating user with id: {}", id);
        try {
            if(id == null) {
                ResponseData<UserResponseDTO> response = new ResponseData<>(400, "No User Id provided", null);
                return ResponseEntity.badRequest().body(response);
            }
            UserResponseDTO updatedUser = userService.updatePartialUser(id, userDto);
            if (updatedUser != null) {
                ResponseData<UserResponseDTO> response = new ResponseData<>(200, "User partially updated successfully", updatedUser);
                return ResponseEntity.ok(response);
            } else {
                ResponseData<UserResponseDTO> response = new ResponseData<>(404, "User not found", null);
                return ResponseEntity.status(404).body(response);
            }
        } catch (Exception e) {
            log.error("Error partially updating user: {}", e.getMessage());
            ResponseData<UserResponseDTO> response = new ResponseData<>(500, "Internal server error: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }


}
