package com.greenwich.todo.entity;

import com.greenwich.todo.util.UserStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true, columnDefinition = "NVARCHAR(50)")
    private String username;

    @Column(name = "password", nullable = false, columnDefinition = "NVARCHAR(100)")
    private String password;

    @Column(name = "email", nullable = false, unique = true, columnDefinition = "NVARCHAR(100)")
    private String email;

    @Column(name = "role", nullable = false, columnDefinition = "NVARCHAR(10)")
    private String role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Todolist> todoLists = new ArrayList<>();

    @Column(name = "status", nullable = false, columnDefinition = "ENUM('ACTIVE', 'BANNED')")
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

}
