package com.greenwich.todo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UserRequestDTO {

    @NotBlank
    private String email;

    @NotBlank
    private String password;

}
