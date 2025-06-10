package com.greenwich.todo.dto.response;

import com.greenwich.todo.util.UserStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {

    private Long id;
    private String username;
    private String email;
    private String role;
    private UserStatus status;

}
