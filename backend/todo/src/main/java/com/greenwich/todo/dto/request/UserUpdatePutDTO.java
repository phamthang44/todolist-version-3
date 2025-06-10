package com.greenwich.todo.dto.request;

import com.greenwich.todo.dto.validator.EnumPattern;
import com.greenwich.todo.util.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdatePutDTO {

    @NotBlank
    private String username;

    @EnumPattern(name="email" ,regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Invalid email format")
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String role;

    @EnumPattern(name = "status", regexp = "^(ACTIVE|BANNED)$", message = "Status must be either ACTIVE or BANNED")
    private UserStatus status;

}
