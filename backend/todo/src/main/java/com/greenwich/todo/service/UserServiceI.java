package com.greenwich.todo.service;

import com.greenwich.todo.dto.request.UserRequestDTO;
import com.greenwich.todo.dto.request.UserUpdatePatchDTO;
import com.greenwich.todo.dto.request.UserUpdatePutDTO;
import com.greenwich.todo.dto.response.UserResponseDTO;
import com.greenwich.todo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserServiceI {

    Optional<User> getUserById(Long id);
    Page<User> findAll(Pageable pageable);
    UserResponseDTO createUser(UserRequestDTO userRequestDTO);
    UserResponseDTO updateUser(Long id, UserUpdatePutDTO userRequestDTO);
    UserResponseDTO updatePartialUser(Long id, UserUpdatePatchDTO userRequestDTO);
    boolean delete(Long id);
    Optional<UserResponseDTO> findByEmail(String email);
    List<UserResponseDTO> findAll();
    UserResponseDTO findUserById(Long id);
    UserResponseDTO updateUserStatus(Long id);
}
